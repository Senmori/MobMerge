package net.senmori.mobmerge.condition.defaults;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

/**
 * This condition checks if two entities have the same {@link DyeColor}.
 */
public class EntityMatchingColorCondition extends DefaultEntityCondition<Boolean> {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Colorable && other instanceof Colorable) {
            return ((Colorable)entity).getColor() == ((Colorable)other).getColor();
        }
        return true;
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }
}
