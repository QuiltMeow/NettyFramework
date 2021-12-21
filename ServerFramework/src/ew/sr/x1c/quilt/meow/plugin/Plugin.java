package ew.sr.x1c.quilt.meow.plugin;

import ew.sr.x1c.quilt.meow.server.GeneralManager;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import lombok.Getter;

public class Plugin {

    @Getter
    private PluginDescription description;

    @Getter
    private GeneralManager generalManager;

    @Getter
    private PluginManager pluginManager;

    @Getter
    private File file;

    @Getter
    private Logger logger;

    public void onLoad() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void inject() {
    }

    public final File getDataFolder() {
        return new File(generalManager.getPluginFolder(), description.getName());
    }

    public final InputStream getResourceAsStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    public final void init(GeneralManager generalManager, PluginManager pluginManager, PluginDescription description) {
        this.generalManager = generalManager;
        this.pluginManager = pluginManager;
        this.description = description;
        this.file = description.getFile();
        this.logger = new PluginLogger(this);
    }
}
