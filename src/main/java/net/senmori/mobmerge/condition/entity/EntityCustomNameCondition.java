package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.StringCondition;
import net.senmori.senlib.util.StringUtils;
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
        return StringUtils.equals(entity.getCustomName(), other.getCustomName());
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
