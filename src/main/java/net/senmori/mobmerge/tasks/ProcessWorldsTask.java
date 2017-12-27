package net.senmori.mobmerge.tasks;

import net.senmori.mobmerge.configuration.ConfigManager;
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

import java.util.List;
import java.util.regex.Matcher;
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
        List<EntityType> validTypes = ConfigManager.MOBS.getValue();
        ChatColor color = ConfigManager.DEFAULT_COLOR.getValue();
        int radius = ConfigManager.RADIUS.getValue().intValue();
        for(Entity entity : world.getEntities()) {
            if(!( entity instanceof LivingEntity ) || !entity.isValid()) continue;
            if(!validTypes.contains(entity.getType())) continue;

            Entity original = entity;
            int removedCount = 0;
            int originalCount = getEntityCount(original);
            List<Entity> nearby = original.getNearbyEntities(radius, radius, radius);
            if(!nearby.isEmpty()) {
                for(Entity other : nearby) {
                    if(match(original, other)) {
                        // merge mobs
                        int otherCount = getEntityCount(other);
                        removedCount += otherCount;
                    }
                }
            }
            if(removedCount > 0) {
                ((LivingEntity)entity).setCustomName(color + Integer.toString(originalCount + removedCount));
            }
        }
    }

    private boolean match(Entity first, Entity second) {
        boolean match = false;
        if(first.getType() == second.getType()) {
            if(first instanceof Ageable && second instanceof Ageable) {
                if( ((Ageable)first).isAdult() != ((Ageable)second).isAdult() ) {
                    return false;
                }
            }
            if(first instanceof Colorable && second instanceof Colorable) {
                if( ((Colorable)first).getColor() != ((Colorable)second).getColor() ) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    private int getEntityCount(Entity entity) {
        int count = 1;

        String customName = entity.getCustomName();
        if(customName != null && customName.startsWith(ConfigManager.DEFAULT_CHAT_COLOR.toString())) {
            Matcher m = CUSTOM_NAME_PATTERN.matcher(ChatColor.stripColor(customName));
            if(m.find()) {
                try {
                    count = Integer.valueOf(m.group(1));
                } catch(NumberFormatException e) {}
            }
        }
        return count;
    }
}
