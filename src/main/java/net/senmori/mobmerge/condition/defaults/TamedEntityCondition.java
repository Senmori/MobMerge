package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.Excluded;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;

@Excluded(reason = "defaultCondition")
public class TamedEntityCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Tameable) {
            return !( (Tameable) entity).isTamed();
        }
        return !(other instanceof Tameable) || !( (Tameable) other).isTamed();
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("tamedEntity");
    }

    @Override
    public Condition clone() {
        return new TamedEntityCondition();
    }
}
