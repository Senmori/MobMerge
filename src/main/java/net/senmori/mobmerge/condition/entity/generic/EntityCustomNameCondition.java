package net.senmori.mobmerge.condition.entity.generic;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.Nameable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This condition tests that two entities has specific custom names.<br>
 * If the custom name is not set, it tests to check that the custom names are null and/or empty.
 */
public class EntityCustomNameCondition extends EntityCondition<String> {
    private String requiredCustomName = null;

    public EntityCustomNameCondition() {}

    private EntityCustomNameCondition(String name) {
        this.requiredCustomName = name;
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Nameable && other instanceof Nameable) {
            return matches(entity.getCustomName(), requiredCustomName) && matches(other.getCustomName(), requiredCustomName);
        }
        return false;
    }

    private boolean matches(String entityName, String requiredName) {
        if (StringUtil.isNullOrEmpty(entityName) && StringUtil.isNullOrEmpty(requiredName)) {
            return true;
        }
        return entityName.equals(requiredName);
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public Condition withRequiredValue(String requiredValue) {
        if(StringUtil.isNullOrEmpty(requiredValue)) {
            MobMerge.debug("Attempted to set " + this.getClass().getSimpleName() + " required value with a null/empty string!");
            return this;
        }
        return new EntityCustomNameCondition(requiredValue);
    }

    @Override
    public String getRequiredValue() {
        return requiredCustomName;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("customName");
    }
}
