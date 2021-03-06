package ew.sr.x1c.quilt.meow.plugin;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PluginLogger extends Logger {

    private final String pluginName;

    protected PluginLogger(Plugin plugin) {
        super(plugin.getClass().getCanonicalName(), null);
        pluginName = "[" + plugin.getDescription().getName() + "] ";
        setParent(plugin.getGeneralManager().getLogger());
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(pluginName + logRecord.getMessage());
        super.log(logRecord);
    }
}
