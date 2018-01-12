package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.ColorableCondition;
import net.senmori.mobmerge.configuration.SettingsManager;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

import java.util.Locale;

public class MatchDyeColorCondition extends ColorableCondition {
    private DyeColor color = null;

    public MatchDyeColorCondition() {}

    /**
     * This condition checks if two entities have the same {@link DyeColor}.<br>
     * If the DyeColor is not set(i.e. used the default constructor), then it checks if both entities merely have the same color, not if they each have a specific color.
     * @param color the color to match
     */
    public MatchDyeColorCondition(DyeColor color) {
        this.color = color;
    }

    @Override
    public MatchDyeColorCondition setRequiredValue(String requiredValue) {
        try {
            this.color = DyeColor.valueOf(requiredValue.toUpperCase());
        } catch(IllegalArgumentException e) {
            this.color = null; // just in case
            if(settingsManager.VERBOSE.getValue() || MobMerge.isDebugMode()) {
                MobMerge.LOG.warning("Failed to find DyeColor with value \'" + requiredValue + "\'");
            }
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof Colorable && other instanceof Colorable) {
            Colorable fColorable = (Colorable)first;
            Colorable fOther = (Colorable)other;
            return color == null ? fColorable.getColor() == fOther.getColor() : (fColorable.getColor() == color && fOther.getColor() == color);
        }
        return false;
    }

    @Override
    public String getStringValue() {
        return color != null ? color.name().toLowerCase(Locale.ENGLISH) : "true";
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public MatchDyeColorCondition clone() {
        return this.color == null ? new MatchDyeColorCondition() : new MatchDyeColorCondition(this.getRequiredValue());
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("dyeColor");
    }
}
