package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.StringCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This condition test two entities to make sure they have the same custom name as {@link #getRequiredValue()}.
 */
public class EntityCustomNameCondition extends StringCondition {

    public EntityCustomNameCondition() {
        super("");
    }

    public EntityCustomNameCondition(String name) {
        super(name);
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        return entity.getCustomName() != null && entity.getCustomName().equals(getRequiredValue()) && other.getCustomName() != null && other.getCustomName().equals(getRequiredValue());
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("customName");
    }

    @Override
    public EntityCustomNameCondition clone() {
        return new EntityCustomNameCondition(this.getRequiredValue());
    }
}
