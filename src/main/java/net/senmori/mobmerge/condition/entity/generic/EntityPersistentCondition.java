package net.senmori.mobmerge.condition.entity.generic;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityPersistentCondition extends EntityCondition<Boolean> {
    private boolean persistent = true;

    public EntityPersistentCondition() {

    }

    private EntityPersistentCondition(boolean value) {
        this.persistent = value;
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof LivingEntity && other instanceof LivingEntity) {
            // have to negate the required value because Bukkit negates the 'persistent' variable in NMS
            return ((LivingEntity)entity).getRemoveWhenFarAway() == !getRequiredValue() && ((LivingEntity)other).getRemoveWhenFarAway() == !getRequiredValue();
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
            MobMerge.debug("Attempted to set " + this.getClass().getSimpleName() + " required value with a null/empty string!");
            return this;
        }
        boolean value = Boolean.parseBoolean(requiredValue);
        return new EntityPersistentCondition(value);
    }

    @Override
    public Boolean getRequiredValue() {
        return persistent;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("isPersistent");
    }
}
