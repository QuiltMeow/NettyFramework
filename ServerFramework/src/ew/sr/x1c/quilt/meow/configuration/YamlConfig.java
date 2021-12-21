package ew.sr.x1c.quilt.meow.configuration;

import ew.sr.x1c.quilt.meow.server.GeneralManager;
import ew.sr.x1c.quilt.meow.util.CaseInsensitiveMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class YamlConfig implements ConfigurationAdapter {

    private final Yaml yaml;
    private Map<String, Object> config;
    private final File file;

    public YamlConfig() {
        this(new File("./config.yml"));
    }

    public YamlConfig(File file) {
        this.file = file;
        DumperOptions option = new DumperOptions();
        option.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(option);
    }

    @Override
    public void load() {
        try {
            file.createNewFile();
            try (InputStream is = new FileInputStream(file)) {
                try {
                    config = (Map) yaml.load(is);
                } catch (YAMLException ex) {
                    throw new RuntimeException("無效的設定檔案", ex);
                }
            }

            if (config == null) {
                config = new CaseInsensitiveMap<>();
            } else {
                config = new CaseInsensitiveMap<>(config);
            }
        } catch (IOException ex) {
            throw new RuntimeException("讀取設定檔案時發生例外狀況", ex);
        }
    }

    @Override
    public <T> T get(String path, T def) {
        return get(path, def, config);
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String path, T def, Map subMap) {
        int index = path.indexOf('.');
        if (index == -1) {
            Object value = subMap.get(path);
            if (value == null && def != null) {
                value = def;
                subMap.put(path, def);
                save();
            }
            return (T) value;
        } else {
            String first = path.substring(0, index);
            String second = path.substring(index + 1, path.length());
            Map sub = (Map) subMap.get(first);
            if (sub == null) {
                sub = new LinkedHashMap();
                subMap.put(first, sub);
            }
            return get(second, def, sub);
        }
    }

    @Override
    public void set(String path, Object value) {
        set(path, value, config);
    }

    @SuppressWarnings("unchecked")
    private void set(String path, Object value, Map subMap) {
        int index = path.indexOf('.');
        if (index == -1) {
            if (value == null) {
                subMap.remove(path);
            } else {
                subMap.put(path, value);
            }
            save();
        } else {
            String first = path.substring(0, index);
            String second = path.substring(index + 1, path.length());
            Map sub = (Map) subMap.get(first);
            if (sub == null) {
                sub = new LinkedHashMap();
                subMap.put(first, sub);
            }
            set(second, value, sub);
        }
    }

    @Override
    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            yaml.dump(config, bw);
        } catch (IOException ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "無法儲存設定檔案", ex);
        }
    }

    @Override
    public int getInt(String path, int def) {
        return get(path, def);
    }

    @Override
    public String getString(String path, String def) {
        return get(path, def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return get(path, def);
    }

    @Override
    public Collection<?> getList(String path, Collection<?> def) {
        return get(path, def);
    }
}
