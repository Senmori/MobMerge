package net.senmori.mobmerge.util;

import net.senmori.mobmerge.option.EntityMatcherOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

public class EntityUtil {
    public static boolean match(Entity first, Entity second) {
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

    public static int getEntityCount(Entity entity, EntityMatcherOptions options) {
        int count = 1;

        if(options == null) {
            return count;
        }
        String customName = entity.getCustomName();
        if(customName != null && customName.startsWith(options.getChatColor().toString())) {
            try {
                count = Integer.parseInt(ChatColor.stripColor(customName));
            } catch(NumberFormatException e) {}
        }
        return count;
    }
}
