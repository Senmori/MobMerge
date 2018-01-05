package net.senmori.mobmerge.tasks;

import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateConfigTask extends BukkitRunnable {

    private final ConfigManager manager;
    private long period;

    public UpdateConfigTask(ConfigManager configManager) {
        this.manager = configManager;
    }

    @Override
    public void run() {
    }
}
