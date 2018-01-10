package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.Excluded;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

@Excluded(reason = "defaultCondition")
public class EntityColorCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Colorable && other instanceof Colorable) {
            return ((Colorable)entity).getColor() == ((Colorable)other).getColor();
        }
        return true; // return true because if they aren't Colorable, we don't care about them in this condition.
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("validEntityColor");
    }

    @Override
    public EntityColorCondition clone() {
        return new EntityColorCondition();
    }
}
