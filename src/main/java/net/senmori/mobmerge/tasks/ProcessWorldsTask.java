package net.senmori.mobmerge.tasks;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.options.EntityMatcherOptions;
import net.senmori.mobmerge.options.EntityOptionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        if(ConfigManager.INTERVAL.getValue().intValue() <= 0) {
            ConfigManager.INTERVAL.setValue(1);
        }
        this.period = ConfigManager.TICKS_PER_SECOND * ConfigManager.INTERVAL.getValue().longValue();

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
            if(!entity.isValid()) continue;
            if(!optionManager.getOptions().containsKey(entity.getType())) {
                continue; // not merging this entity; ignore it
            }
            EntityMatcherOptions options = optionManager.getOptions().get(entity.getType());
            Vector radius = options.getRadius();
            int originalCount = options.getCount(entity);
            int removedCount = 0;
            List<Entity> nearby = entity.getNearbyEntities(radius.getX(), radius.getY(), radius.getZ()).stream()
                    .filter(other -> !HumanEntity.class.isInstance(other)) // not a player, or human entity
                    .filter(LivingEntity.class::isInstance) // but it is a living entity
                    .filter(Entity::isValid)
                    .filter(other -> !other.getUniqueId().equals(entity.getUniqueId()))
                    .filter(other -> other.getType() == entity.getType())
                    .collect(Collectors.toList());
            for(Entity other : nearby) {
                if(options.test(entity, other)) {
                    // merge mobs
                    int otherCount = options.getCount(other);
                    if(otherCount < 0) {
                        if(ConfigManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                            MobMerge.LOG.warning("Invalid entity type for option. Expected \'" + options.getEntityType() + "\'. Found \'" + other.getType() + "\'");
                            continue; // this should never happen.
                        }
                    }
                    if(originalCount + removedCount + otherCount < options.getMaxCount()) {
                        other.remove();
                        removedCount += otherCount;
                    }
                }
            } // end nearby entities
            if(removedCount > 0) {
                int newCount = originalCount + removedCount;
                LivingEntity living = (LivingEntity)entity;
                living.setCustomName(options.getChatColor() + String.valueOf(newCount));
                living.setCustomNameVisible(true); //TODO: remove this?
                if(!living.getScoreboardTags().contains(ConfigManager.MERGED_ENTITY_TAG.getValue())) {
                    living.addScoreboardTag(ConfigManager.MERGED_ENTITY_TAG.getValue());
                }
            }
        }
    }
}
