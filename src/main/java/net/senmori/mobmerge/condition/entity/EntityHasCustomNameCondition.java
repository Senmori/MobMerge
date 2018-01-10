package net.senmori.mobmerge.condition.entity;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This conditions tests two entities and checks to see if they have custom names.<br>
 * Custom names are valid if they are not null, and not empty.
 */
public class EntityHasCustomNameCondition extends BooleanCondition {

    public EntityHasCustomNameCondition(boolean hasCustomName) {
        super(hasCustomName);
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        return !StringUtil.isNullOrEmpty(entity.getCustomName()) == getRequiredValue() && !StringUtil.isNullOrEmpty(other.getCustomName()) == getRequiredValue();
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("hasCustomName");
    }

    @Override
    public EntityHasCustomNameCondition clone() {
        return new EntityHasCustomNameCondition(this.getRequiredValue());
    }
}
