package net.senmori.mobmerge.tasks;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.options.EntityMatcherOptions;
import net.senmori.mobmerge.options.EntityOptionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.regex.Pattern;

public class ProcessWorldsTask extends BukkitRunnable {
    private static final Pattern EQUALS = Pattern.compile("=");

    private final JavaPlugin plugin;
    private final ConfigManager manager;
    private final EntityOptionManager optionManager;

    private long period;
    public ProcessWorldsTask(ConfigManager manager) {
        this.manager = manager;
        this.plugin = manager.getPlugin();
        this.optionManager = manager.getEntityOptionManager();
        this.period = ConfigManager.TICKS_PER_SECOND * ConfigManager.INTERVAL.getValue().intValue();

        this.runTaskTimer(plugin, period, period);
    }

    @Override
    public void run() {
        for(World world : Bukkit.getWorlds()) {
            if(ConfigManager.EXCLUDED_WORLDS.contains(world)) continue;
            processWorld(world);
        }
    }

    private void processWorld(World world) {
        for(LivingEntity entity : world.getLivingEntities()) {
            if(!entity.getWorld().getChunkAt(entity.getLocation()).isLoaded()) continue; // got an entity in a lazy/unloaded chunk
            if(!entity.getType().isAlive()) continue; // ignore non-living entities
            if(!optionManager.getOptions().containsKey(entity.getType())) {
                continue; // not merging this entity; ignore it
            }
            EntityMatcherOptions options = optionManager.getOptions().get(entity.getType());
            Vector radius = options.getRadius();
            int originalCount = options.getCount(entity);
            int removedCount = 0;
            for(Entity other : entity.getNearbyEntities(radius.getX(), radius.getY(), radius.getZ())) {
                if(other.getType() != entity.getType()) continue;
                if(options.test(entity, other)) {
                    // merge mobs
                    int otherCount = options.getCount(other);
                    if(otherCount < 0) {
                        if(ConfigManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                            MobMerge.LOG.warning("Invalid entity type for option. Expected \'" + options.getEntityType() + "\'. Found \'" + other.getType() + "\'");
                            continue; // this should never happen.
                        }
                    }
                    if(otherCount > 0 && originalCount + removedCount + otherCount < options.getMaxCount()) {
                        removedCount += otherCount;
                        other.getPassengers().forEach(Entity::remove);
                        other.remove(); // remove passengers from
                    }
                }
            } // end nearby entities
            if(removedCount > 0) {
                int newCount = originalCount + removedCount;
                LivingEntity le = (LivingEntity)entity;
                le.setCustomName(options.getChatColor() + String.valueOf(newCount));
                le.setCustomNameVisible(true); //TODO: remove this?
                if(!le.getScoreboardTags().contains(ConfigManager.MERGED_ENTITY_TAG.getValue())) {
                    le.addScoreboardTag(ConfigManager.MERGED_ENTITY_TAG.getValue());
                }
            }
        }
    }
}
