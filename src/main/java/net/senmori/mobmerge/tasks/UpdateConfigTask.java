package net.senmori.mobmerge.tasks;

import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateConfigTask extends BukkitRunnable {

    private final ConfigManager manager;
    private long period;

    public UpdateConfigTask(ConfigManager configManager) {
        this.manager = configManager;
        reset();
    }

    public void reset() {
        this.period = ConfigManager.TICKS_PER_SECOND * ConfigManager.CONFIG_UPDATE_INTERVAL.getValue().intValue();

        this.runTaskTimer(manager.getPlugin(), period, period);
    }

    @Override
    public void run() {
        manager.saveConfig();
        manager.loadConfig();
    }
}
