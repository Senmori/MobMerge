package net.senmori.mobmerge.condition.type;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.configuration.SettingsManager;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

import java.util.Locale;

/**
 * This condition tests if two entities have a specific color, or if the color is null, if both entities are the same color
 */
public abstract class ColorableCondition implements Condition {
    protected static final SettingsManager settingsManager = MobMerge.getInstance().getSettingsManager();
    private DyeColor color = null;

    @Override
    public Condition setRequiredValue(String requiredValue) {
        Validate.notNull(requiredValue, "Required value cannot be null");
        try {
            color = DyeColor.valueOf(requiredValue.toUpperCase());
        } catch(IllegalArgumentException e) {
            MobMerge.debug("Could not load dye color with given value \'" + requiredValue + "\'");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DyeColor getRequiredValue() {
        return color;
    }

    @Override
    public String getStringValue() {
        return getRequiredValue() == null ? "null" : getRequiredValue().name().toLowerCase(Locale.ENGLISH);
    }

    public abstract boolean test(Entity entity, Entity other);

    public abstract Priority getPriority();

    public abstract NamespacedKey getKey();

    public abstract Condition clone();
}
