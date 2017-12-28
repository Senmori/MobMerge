package net.senmori.mobmerge.util;

import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityUtil {
    private static final Pattern CUSTOM_NAME_PATTERN = Pattern.compile("\\d+");

    public static boolean match(Entity first, Entity second) {
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

    public static int getEntityCount(Entity entity) {
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
