package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.Excluded;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

@Excluded(reason = "defaultCondition")
public class EntityTypeCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity.getType() == other.getType();
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("validEntityType");
    }

    @Override
    public EntityTypeCondition clone() {
        return new EntityTypeCondition();
    }
}
