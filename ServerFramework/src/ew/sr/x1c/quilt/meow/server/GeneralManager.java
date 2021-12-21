package ew.sr.x1c.quilt.meow.server;

import com.google.common.base.Preconditions;
import ew.sr.x1c.quilt.meow.plugin.PluginManager;
import ew.sr.x1c.quilt.meow.schedule.TaskScheduler;
import java.io.File;
import java.util.logging.Logger;
import lombok.Getter;

public abstract class GeneralManager {

    @Getter
    private static GeneralManager instance;

    public static void setInstance(GeneralManager instance) {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkArgument(GeneralManager.instance == null, "實例已設定");
        GeneralManager.instance = instance;
    }

    public abstract String getName();

    public abstract String getVersion();

    public abstract Logger getLogger();

    public abstract Server getServer();

    public abstract PluginManager getPluginManager();

    public abstract void start();

    public abstract void stop();

    public abstract ClientStorage getClientStorage();

    public abstract File getPluginFolder();

    public abstract TaskScheduler getScheduler();

    public abstract void broadcast(byte[] message);

    public abstract int getOnlineCount();
}
