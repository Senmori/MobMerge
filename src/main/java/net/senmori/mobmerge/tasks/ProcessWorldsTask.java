package net.senmori.mobmerge.tasks;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.util.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.regex.Pattern;

public class ProcessWorldsTask extends BukkitRunnable {
    private final Pattern CUSTOM_NAME_PATTERN = Pattern.compile("\\d+");

    private final JavaPlugin plugin;
    private final ConfigManager manager;

    private long period;
    public ProcessWorldsTask(ConfigManager manager) {
        this.manager = manager;
        this.plugin = manager.getPlugin();
        this.period = ConfigManager.TICKS_PER_SECOND * ConfigManager.INTERVAL.getValue().intValue();

        this.runTaskTimer(plugin, 0L, period);
    }

    @Override
    public void run() {
        for(World world : Bukkit.getWorlds()) {
            if(ConfigManager.EXCLUDED_WORLDS.contains(world)) continue;
            MobMerge.LOG.info("Processing world : " + world.getName());
            processWorld(world);
        }
    }

    private void processWorld(World world) {
        List<EntityType> validTypes = ConfigManager.MOBS.getValue();
        ChatColor color = ConfigManager.DEFAULT_COLOR.getValue();
        int radius = ConfigManager.RADIUS.getValue().intValue();
        for(Entity entity : world.getEntities()) {
            if(!( entity instanceof LivingEntity ) || !entity.isValid()) continue;
            if(!validTypes.contains(entity.getType())) continue;

            int removedCount = 0;
            int originalCount = EntityUtil.getEntityCount(entity);
            List<Entity> nearby = entity.getNearbyEntities(radius, radius, radius);
            MobMerge.LOG.info("Found " + nearby.size() + " nearby entities near " + entity.getType());
            if(!nearby.isEmpty()) {
                for(Entity other : nearby) {
                    if(EntityUtil.match(entity, other)) {
                        // merge mobs
                        int otherCount = EntityUtil.getEntityCount(other);
                        if(originalCount + removedCount + otherCount <= ConfigManager.MAX_COUNT.getValue().intValue()) {
                            other.remove();
                            removedCount += otherCount;
                        }
                    }
                }
            }
            if(removedCount > 0) {
                ((LivingEntity)entity).setCustomName(color + Integer.toString(originalCount + removedCount));
            }
        }
    }
}
