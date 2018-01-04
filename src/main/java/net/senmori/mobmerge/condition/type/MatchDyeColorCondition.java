package net.senmori.mobmerge.condition.type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.gson.JsonUtils;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

import java.util.Locale;

public class MatchDyeColorCondition implements Condition {
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
        if(Boolean.parseBoolean(requiredValue)) {
            return this; // value is 'true', so we don't match a specific color
        }
        try {
            this.color = DyeColor.valueOf(requiredValue.toUpperCase());
        } catch(IllegalArgumentException e) {
            MobMerge.debug("Failed to find DyeColor with string " + requiredValue);
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
    public DyeColor getRequiredValue() {
        return color;
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
    public NamespacedKey getKey() {
        return MobMerge.newKey("dyeColor");
    }

    public static class Serializer extends Condition.Serializer<MatchDyeColorCondition> {
        public Serializer() {
            super(MobMerge.newKey("dyeColor"), MatchDyeColorCondition.class);
        }

        @Override
        public void serialize(JsonObject json, MatchDyeColorCondition type, JsonSerializationContext context) {
           json.addProperty("value", type.getStringValue());
        }

        @Override
        public MatchDyeColorCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            MatchDyeColorCondition condition = new MatchDyeColorCondition();
            if(JsonUtils.hasField(json, "value")) {
                condition.setRequiredValue(JsonUtils.getString(json, "value"));
            }
            return condition;
        }
    }
}
