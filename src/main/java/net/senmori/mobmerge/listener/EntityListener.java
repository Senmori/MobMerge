package net.senmori.mobmerge.listener;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.SettingsManager;
import net.senmori.mobmerge.options.EntityMatcherOptions;
import net.senmori.mobmerge.options.EntityOptionManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityListener implements Listener {

    private final JavaPlugin plugin;
    private final SettingsManager settingsManager;
    private final EntityOptionManager optionManager;
    private final ConditionManager conditionManager;
    public EntityListener(SettingsManager settingsManager) {
        this.plugin = settingsManager.getPlugin();
        this.settingsManager = settingsManager;
        this.optionManager = settingsManager.getEntityOptionManager();
        this.conditionManager = settingsManager.getConditionManager();

        this.settingsManager.getPlugin().getServer().getPluginManager().registerEvents(this, settingsManager.getPlugin());
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        MobMerge.debug("Entity Death: " + event.getEntity().getType());
        if(!optionManager.getOptions().containsKey(event.getEntity().getType())) return; // ignore entities that aren't registered
        EntityMatcherOptions options = optionManager.getOptionsFor(event.getEntity().getType());
        int count = options.getCount(event.getEntity());
        MobMerge.debug("Merged Entity death count: " + count);
        if(count < 0) {
            if(settingsManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                MobMerge.LOG.warning("Invalid entity type for option. Expected \'" + options.getEntityType() + "\'. Found \'" + event.getEntity().getType() + "\'");
                return;
            }
        }
        if(count < 2 || !event.getEntity().getScoreboardTags().contains(settingsManager.DEFAULT_SECTION.ENTITY_TAG.getValue())) {
            return; // it's not a merged entity
        }
        int newCount = count - 1;
        event.getEntity().getWorld().spawn(event.getEntity().getLocation(), event.getEntity().getType().getEntityClass(), (entity) -> {
            entity.setCustomName(options.getChatColor() + Integer.toString(newCount) + ChatColor.RESET);
            entity.setCustomNameVisible(true);
            entity.getScoreboardTags().add(settingsManager.DEFAULT_SECTION.ENTITY_TAG.getValue());
        });
    }
}
