package net.senmori.mobmerge.condition.entity.generic;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.Nameable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This condition checks two entities to see if they have valid custom names.<br>
 * Valid custom names are ones that are not null, nor empty.
 */
public class EntityHasCustomNameCondition extends EntityCondition<Boolean> {
    private boolean hasCustomName = false;

    public EntityHasCustomNameCondition() {}

    private EntityHasCustomNameCondition(boolean value) {
        this.hasCustomName = value;
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Nameable && other instanceof Nameable) {
            return customNameExists(entity.getCustomName()) == getRequiredValue() && customNameExists(other.getCustomName()) == getRequiredValue();
        }
        return false;
    }

    private boolean customNameExists(String customName) {
        return !StringUtil.isNullOrEmpty(customName);
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
        return new EntityHasCustomNameCondition(Boolean.parseBoolean(requiredValue));
    }

    @Override
    public Boolean getRequiredValue() {
        return hasCustomName;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("hasCustomName");
    }
}
