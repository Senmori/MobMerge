package net.senmori.mobmerge.configuration.options.types;

import net.senmori.mobmerge.configuration.options.ConfigOption;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ChatColorOption extends ConfigOption<ChatColor> {

    public static ChatColorOption newOption(String key, ChatColor defaultValue) {
        return new ChatColorOption(key, defaultValue, ChatColor.class);
    }

    protected ChatColorOption(String key, ChatColor defaultValue, Class<ChatColor> typeClass) {
        super(key, defaultValue, typeClass);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        // if it's not an '&' symbol
        String str = config.getString(getPath());
        if(str == null || str.isEmpty()) {
            return false;
        }
        ChatColor color = null;
        if(str.startsWith("&") && str.length() >= 2) {
           color = ChatColor.getByChar(str.charAt(1));
           if(color != null) {
               setValue(color);
               return true;
           }
           return false; // not a valid color
        } else {
            try {
                color = ChatColor.valueOf(config.getString(getPath()));
                setValue(color);
                return true;
            } catch(IllegalArgumentException e) {
                return false;
            }
        }
    }
}
