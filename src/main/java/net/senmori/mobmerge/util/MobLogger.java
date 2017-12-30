package net.senmori.mobmerge.util;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MobLogger extends Logger {

    private String pluginName;

    public MobLogger(Plugin plugin) {
        super(plugin.getClass().getCanonicalName(), (String)null);
        String prefix = plugin.getDescription().getPrefix();
        this.pluginName = prefix != null ? "[" + prefix + "] " : "[" + plugin.getDescription().getName() + "] ";
        this.setParent(plugin.getServer().getLogger());
        this.setLevel(Level.ALL);
    }

    public void setPluginName(String name) {
        this.pluginName = name;
    }

    public void log(LogRecord record) {
        record.setMessage(this.pluginName + record.getMessage());
        super.log(record);
    }
}
