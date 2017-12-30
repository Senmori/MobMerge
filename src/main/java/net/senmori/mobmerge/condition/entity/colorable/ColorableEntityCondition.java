package net.senmori.mobmerge.condition.entity.colorable;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

public class ColorableEntityCondition extends EntityCondition<DyeColor> {
    private DyeColor color = null;

    public ColorableEntityCondition() {}

    private ColorableEntityCondition(DyeColor color) {
        this.color = color;
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Colorable && other instanceof Colorable) {
            if(color == null) {
                return ((Colorable)entity).getColor() == ((Colorable)other).getColor(); // check for color equality
            }
            return ((Colorable)entity).getColor() == color && ((Colorable)other).getColor() == color;
        }
        return false;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public Condition withRequiredValue(String requiredValue) {
        if(StringUtil.isNullOrEmpty(requiredValue)) {
            MobMerge.debug("Attempt to set a null/empty required value in " + this.getClass().getSimpleName());
            return this;
        }
        DyeColor newColor = DyeColor.valueOf(requiredValue.toUpperCase());
        return new ColorableEntityCondition(newColor);
    }

    @Override
    public DyeColor getRequiredValue() {
        return color;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("color");
    }
}
