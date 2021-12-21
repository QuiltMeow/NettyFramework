package ew.sr.x1c.quilt.meow.server;

import ew.sr.x1c.quilt.meow.configuration.ConfigurationAdapter;
import ew.sr.x1c.quilt.meow.configuration.YamlConfig;
import ew.sr.x1c.quilt.meow.constant.Constant;
import ew.sr.x1c.quilt.meow.plugin.PluginManager;
import ew.sr.x1c.quilt.meow.schedule.TaskScheduler;
import ew.sr.x1c.quilt.meow.schedule.TaskScheduler.ScheduleTimer;
import ew.sr.x1c.quilt.meow.server.netty.NettyServer;
import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Synchronized;

public class Entry extends GeneralManager {

    @Getter
    private final PluginManager pluginManager;

    @Getter
    private final Logger logger;

    @Getter
    private final String version;

    @Getter
    private final String name;

    @Getter
    private final File pluginFolder;

    @Getter
    private NettyServer server;

    @Getter
    private final ConfigurationAdapter config;

    @Getter
    private final ClientStorage clientStorage;

    private Thread shutdownThread;

    private void registerLogHandler(Handler... handlerList) {
        for (Handler handler : handlerList) {
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        }
    }

    public Entry(Handler... handlerList) {
        logger = Logger.getLogger(Entry.class.getName());
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        registerLogHandler(handlerList);
        GeneralManager.setInstance(this);

        config = new YamlConfig();
        try {
            config.load();
        } catch (Exception ex) {
            logger.log(Level.WARNING, "設定檔案讀取失敗", ex);
        }
        name = config.getString("name", Constant.DEFAULT_NAME);
        version = config.getString("version", Constant.DEFAULT_VERSION);

        pluginFolder = new File("./plugin");
        pluginManager = new PluginManager();
        clientStorage = new ClientStorage();
    }

    public void loadExternalPlugin() {
        pluginFolder.mkdir();
        pluginManager.detectAllPlugin(pluginFolder);
        pluginManager.loadAllPlugin();
        pluginManager.enableAllPlugin();
    }

    @Override
    public void start() {
        logger.log(Level.INFO, "服務端啟動");
        server = new NettyServer(config.getInt("port", Constant.DEFAULT_PORT));
        server.start();
        logger.log(Level.INFO, "服務端端口 : TCP {0}", String.valueOf(server.getPort()));
        loadExternalPlugin();
        logger.log(Level.INFO, "服務端啟動完畢");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    @Synchronized
    public void stop() {
        if (server == null || shutdownThread != null) {
            return;
        }
        logger.log(Level.INFO, "服務端關閉");
        shutdownThread = new Thread(new Shutdown());
        shutdownThread.start();
    }

    @Override
    public File getPluginFolder() {
        return pluginFolder;
    }

    @Override
    public void broadcast(byte[] message) {
        clientStorage.broadcastPacket(message);
    }

    @Override
    public TaskScheduler getScheduler() {
        return ScheduleTimer.getInstance();
    }

    @Override
    public ClientStorage getClientStorage() {
        return clientStorage;
    }

    @Override
    public int getOnlineCount() {
        return clientStorage.getOnlineCount();
    }

    @Override
    public Server getServer() {
        return server;
    }
}
