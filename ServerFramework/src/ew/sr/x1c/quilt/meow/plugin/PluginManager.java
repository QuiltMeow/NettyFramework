package ew.sr.x1c.quilt.meow.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import ew.sr.x1c.quilt.meow.command.Command;
import ew.sr.x1c.quilt.meow.command.CommandSender;
import ew.sr.x1c.quilt.meow.event.EventBus;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;

@RequiredArgsConstructor
public class PluginManager {

    private final Yaml yaml;
    private final EventBus eventBus;
    private final Map<String, Plugin> pluginMap;
    private Map<String, PluginDescription> toLoad;
    private final Multimap<Plugin, Listener> listenerByPlugin;

    private final Map<String, Command> commandMap;
    private final Multimap<Plugin, Command> commandByPlugin;

    @SuppressWarnings("unchecked")
    public PluginManager() {
        pluginMap = new LinkedHashMap<>();
        toLoad = new HashMap<>();
        listenerByPlugin = ArrayListMultimap.create();

        commandMap = new HashMap<>();
        commandByPlugin = ArrayListMultimap.create();

        Constructor yamlConstructor = new Constructor();
        PropertyUtils propertyUtil = yamlConstructor.getPropertyUtils();
        propertyUtil.setSkipMissingProperties(true);
        yamlConstructor.setPropertyUtils(propertyUtil);

        yaml = new Yaml(yamlConstructor);
        eventBus = new EventBus(GeneralManager.getInstance().getLogger());
    }

    public Collection<Plugin> getPluginList() {
        return pluginMap.values();
    }

    public Plugin getPlugin(String name) {
        return pluginMap.get(name);
    }

    public void loadAllPlugin() {
        Map<PluginDescription, Boolean> pluginStatus = new HashMap<>();
        toLoad.entrySet().forEach((entry) -> {
            PluginDescription plugin = entry.getValue();
            if (!loadPlugin(pluginStatus, new Stack<>(), plugin)) {
                GeneralManager.getInstance().getLogger().log(Level.WARNING, "載入插件 {0} 失敗", entry.getKey());
            }
        });
        toLoad = null;
    }

    public void enableAllPlugin() {
        pluginMap.values().forEach((plugin) -> {
            try {
                plugin.onEnable();
                GeneralManager.getInstance().getLogger().log(Level.INFO, "插件 {0} 已啟動 版本 : {1} 作者 : {2}", new Object[]{
                    plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAuthor()
                });
            } catch (Throwable th) {
                GeneralManager.getInstance().getLogger().log(Level.WARNING, "啟用插件時發生例外狀況 : " + plugin.getDescription().getName(), th);
            }
        });
    }

    private boolean loadPlugin(Map<PluginDescription, Boolean> pluginStatus, Stack<PluginDescription> dependStack, PluginDescription description) {
        if (pluginStatus.containsKey(description)) {
            return pluginStatus.get(description);
        }
        Set<String> dependency = new HashSet<>();
        dependency.addAll(description.getDepend());
        dependency.addAll(description.getSoftDepend());

        boolean status = true;
        for (String dependName : dependency) {
            PluginDescription depend = toLoad.get(dependName);
            Boolean dependStatus = (depend != null) ? pluginStatus.get(depend) : Boolean.FALSE;

            if (dependStatus == null) {
                if (dependStack.contains(depend)) {
                    StringBuilder dependencyGraph = new StringBuilder();
                    dependStack.forEach((element) -> {
                        dependencyGraph.append(element.getName()).append(" -> ");
                    });
                    dependencyGraph.append(description.getName()).append(" -> ").append(dependName);
                    GeneralManager.getInstance().getLogger().log(Level.WARNING, "偵測到環狀相依 : {0}", dependencyGraph);
                    status = false;
                } else {
                    dependStack.push(description);
                    dependStatus = loadPlugin(pluginStatus, dependStack, depend);
                    dependStack.pop();
                }
            }

            if (dependStatus == Boolean.FALSE && description.getDepend().contains(dependName)) {
                GeneralManager.getInstance().getLogger().log(Level.WARNING, "{0} (需求 {1}) 不可使用", new Object[]{
                    String.valueOf(dependName), description.getName()
                });
                status = false;
            }

            if (!status) {
                break;
            }
        }

        if (status) {
            try {
                URLClassLoader loader = new PluginClassloader(new URL[]{
                    description.getFile().toURI().toURL()
                });
                Class<?> main = loader.loadClass(description.getMain());
                Plugin pluginClass = (Plugin) main.getDeclaredConstructor().newInstance();

                pluginClass.init(GeneralManager.getInstance(), this, description);
                pluginMap.put(description.getName(), pluginClass);
                pluginClass.onLoad();
                GeneralManager.getInstance().getLogger().log(Level.INFO, "插件 {0} 已載入 版本 : {1} 作者 : {2}", new Object[]{
                    description.getName(), description.getVersion(), description.getAuthor()
                });
            } catch (Throwable th) {
                GeneralManager.getInstance().getLogger().log(Level.WARNING, "載入插件時發生例外狀況 : " + description.getName(), th);
            }
        }

        pluginStatus.put(description, status);
        return status;
    }

    public void detectAllPlugin(File folder) {
        Preconditions.checkNotNull(folder, "folder");
        Preconditions.checkArgument(folder.isDirectory(), "必須從目錄讀取");

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                try (JarFile jar = new JarFile(file)) {
                    JarEntry entry = jar.getJarEntry("plugin.yml");
                    Preconditions.checkNotNull(entry, "插件檔案必須存在 plugin.yml 描述檔案");

                    try (InputStream inputStream = jar.getInputStream(entry)) {
                        PluginDescription desc = yaml.loadAs(inputStream, PluginDescription.class);
                        Preconditions.checkNotNull(desc.getName(), "插件描述檔案 %s 沒有名稱", file);
                        Preconditions.checkNotNull(desc.getMain(), "插件描述檔案 %s 沒有入口點", file);

                        desc.setFile(file);
                        toLoad.put(desc.getName(), desc);
                    }
                } catch (Exception ex) {
                    GeneralManager.getInstance().getLogger().log(Level.WARNING, "無法從檔案 " + file + " 讀取插件", ex);
                }
            }
        }
    }

    public <T extends Event> T callEvent(T event) {
        Preconditions.checkNotNull(event, "event");

        long start = System.nanoTime();
        eventBus.post(event);
        event.postCall();

        long elapse = System.nanoTime() - start;
        if (elapse > 250000000) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "事件 {0} 花費 {1} 奈秒進行處理 !", new Object[]{
                event, elapse
            });
        }
        return event;
    }

    public void registerListener(Plugin plugin, Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Preconditions.checkArgument(!method.isAnnotationPresent(Subscribe.class),
                    "監聽器 %s 使用已廢棄訂閱標註進行註冊 ! 請更新至 @EventHandler", listener);
        }
        eventBus.register(listener);
        listenerByPlugin.put(plugin, listener);
    }

    public void unregisterListener(Listener listener) {
        eventBus.unregister(listener);
        listenerByPlugin.values().remove(listener);
    }

    public void unregisterListener(Plugin plugin) {
        for (Iterator<Listener> iterator = listenerByPlugin.get(plugin).iterator(); iterator.hasNext();) {
            eventBus.unregister(iterator.next());
            iterator.remove();
        }
    }

    public void registerCommand(Plugin plugin, Command command) {
        commandMap.put(command.getName().toLowerCase(Locale.ROOT), command);
        for (String alias : command.getAlias()) {
            commandMap.put(alias.toLowerCase(Locale.ROOT), command);
        }
        commandByPlugin.put(plugin, command);
    }

    public void unregisterCommand(Command command) {
        while (commandMap.values().remove(command)) {
        }
        commandByPlugin.values().remove(command);
    }

    public void unregisterCommand(Plugin plugin) {
        for (Iterator<Command> iterator = commandByPlugin.get(plugin).iterator(); iterator.hasNext();) {
            Command command = iterator.next();
            while (commandMap.values().remove(command)) {
            }
            iterator.remove();
        }
    }

    private Command getCommand(String commandName) {
        String commandLower = commandName.toLowerCase(Locale.ROOT);
        return commandMap.get(commandLower);
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        String trim = commandLine.trim();
        if (trim.isEmpty()) {
            return false;
        }
        String[] split = trim.split(" ");

        Command command = getCommand(split[0]);
        if (command == null) {
            return false;
        }
        if (!command.hasPermission(sender)) {
            return true;
        }

        String[] args = null;
        if (split.length > 1) {
            args = Arrays.copyOfRange(split, 1, split.length);
        }
        GeneralManager.getInstance().getLogger().log(Level.INFO, "{0} 執行了指令 : {1}", new Object[]{
            sender.getName(), commandLine
        });
        try {
            command.execute(sender, args);
        } catch (Exception ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "調度指令時發生例外狀況", ex);
        }
        return true;
    }

    public Collection<Map.Entry<String, Command>> getCommand() {
        return Collections.unmodifiableCollection(commandMap.entrySet());
    }
}
