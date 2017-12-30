package net.senmori.mobmerge.listener;

import net.senmori.mobmerge.action.death.EntityDeathAction;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.option.EntityOptionManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {

    private final ConfigManager manager;
    private final EntityOptionManager optionManager;
    private final EntityDeathAction deathAction = new EntityDeathAction();
    public EntityListener(ConfigManager configManager) {
        this.manager = configManager;
        this.optionManager = configManager.getEntityOptionManager();

        manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        deathAction.perform(event.getEntity(), (Entity)null, optionManager.getOptions().get(event.getEntity().getType()));
    }
}
