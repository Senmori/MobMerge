package net.senmori.mobmerge.configuration.option.types;

import net.senmori.mobmerge.configuration.option.ConfigOption;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;

public class ChatColorOption extends ConfigOption<ChatColor> {

    public static ChatColorOption newOption(String key, ChatColor defaultValue) {
        return new ChatColorOption(key, defaultValue, ChatColor.class);
    }

    protected ChatColorOption(String key, ChatColor defaultValue, Class<ChatColor> typeClass) {
        super(key, defaultValue, typeClass);
    }


    public void fromString(String color) {
        ChatColor chat = ChatColor.valueOf(color.toUpperCase());
        if(chat != null) {
            setValue(chat);
        }
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        // if it's not an '&' symbol
        String str = config.getString(getPath());
        if(str == null || str.isEmpty()) {
            return false;
        }
        try {
            ChatColor color = ChatColor.valueOf(config.getString(getPath()).toUpperCase());
            setValue(color);
            return getValue() == color;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getValue().name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return "ConfigOption={Path=" + getPath() + ", Value=" + getValue().name().toLowerCase() + "}";
    }
}
