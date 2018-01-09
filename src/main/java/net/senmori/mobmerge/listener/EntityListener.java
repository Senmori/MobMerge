package net.senmori.mobmerge.listener;

import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.options.EntityOptionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityListener implements Listener {

    private final JavaPlugin plugin;
    private final ConfigManager manager;
    private final EntityOptionManager optionManager;
    private final ConditionManager conditionManager;
    public EntityListener(ConfigManager configManager) {
        this.plugin = configManager.getPlugin();
        this.manager = configManager;
        this.optionManager = configManager.getEntityOptionManager();
        this.conditionManager = configManager.getConditionManager();

        manager.getPlugin().getServer().getPluginManager().registerEvents(this, configManager.getPlugin());
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
    }
}
