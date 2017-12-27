package net.senmori.mobmerge.tasks;

import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateConfigTask extends BukkitRunnable {
    private final int TICKS_PER_SECOND = 20;

    private final ConfigManager manager;
    private long period;

    public UpdateConfigTask(ConfigManager configManager) {
        this.manager = configManager;
        this.period = TICKS_PER_SECOND * ConfigManager.CONFIG_UPDATE_INTERVAL.getValue().intValue();

        this.runTaskTimer(manager.getPlugin(), 0L, period);
    }

    @Override
    public void run() {
        manager.saveConfig(); // save current values
        manager.loadConfig();
    }
}
