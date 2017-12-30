package net.senmori.mobmerge.tasks;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.option.EntityMatcherOptions;
import net.senmori.mobmerge.option.EntityOptionManager;
import net.senmori.mobmerge.util.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.material.Colorable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ProcessWorldsTask extends BukkitRunnable {
    private final Pattern CUSTOM_NAME_PATTERN = Pattern.compile("\\d+");

    private final JavaPlugin plugin;
    private final ConfigManager manager;
    private final EntityOptionManager optionManager;

    private long period;
    public ProcessWorldsTask(ConfigManager manager) {
        this.manager = manager;
        this.plugin = manager.getPlugin();
        this.optionManager = manager.getEntityOptionManager();
        this.period = ConfigManager.TICKS_PER_SECOND * ConfigManager.INTERVAL.getValue().intValue();

        this.runTaskTimer(plugin, 0L, period);
    }

    @Override
    public void run() {
        for(World world : Bukkit.getWorlds()) {
            if(ConfigManager.EXCLUDED_WORLDS.contains(world)) continue;
            processWorld(world);
        }
    }

    private void processWorld(World world) {
        for(Entity entity : world.getEntities()) {
            if(!optionManager.getOptions().containsKey(entity.getType())) {
                continue; // not merging this entity; ignore it
            }
            EntityMatcherOptions options = optionManager.getOptions().get(entity.getType());
            Vector radius = options.getRadius();
            int originalCount = EntityUtil.getEntityCount(entity, options);
            int removedCount = 0;
            List<Entity> toRemove = Lists.newArrayList();
            for(Entity other : entity.getNearbyEntities(radius.getX(), radius.getY(), radius.getZ())) {
                if(other.getType() != entity.getType()) continue;
                if(options.test(entity, other)) {
                    // merge mobs
                    int otherCount = EntityUtil.getEntityCount(other, optionManager.getOptions().get(other.getType()));
                    if(originalCount + removedCount + otherCount < options.getMaxCount()) {
                        removedCount += otherCount;
                        toRemove.add(other);
                    }
                }
            } // end nearby entities
            if(removedCount > 0) {
                int newCount = originalCount + removedCount;
                ((LivingEntity)entity).setCustomName(options.getChatColor() + String.valueOf(newCount));
                ((LivingEntity)entity).setCustomNameVisible(true);
                toRemove.forEach(Entity::remove);
                toRemove.clear();
            }
        }
    }
}
