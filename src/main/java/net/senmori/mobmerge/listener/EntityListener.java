package net.senmori.mobmerge.listener;

import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.option.EntityOptionManager;
import net.senmori.mobmerge.util.EntityUtil;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {

    private final ConfigManager manager;
    private final EntityOptionManager optionManager;
    public EntityListener(ConfigManager configManager) {
        this.manager = configManager;
        this.optionManager = configManager.getEntityOptionManager();

        manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        int count = EntityUtil.getEntityCount(entity, optionManager.getOptions().get(entity.getType()));
        if(count > 1) {
            LivingEntity clone = (LivingEntity)entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
            if(count > 2) {
                clone.setCustomName(ConfigManager.DEFAULT_COLOR.getValue() + Integer.toString(count -1) + " x " + WordUtils.capitalize(entity.getType().getName()));
            }
        }
    }
}
