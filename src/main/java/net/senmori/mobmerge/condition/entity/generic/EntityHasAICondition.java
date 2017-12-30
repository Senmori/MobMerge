package net.senmori.mobmerge.condition.entity.generic;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityHasAICondition extends EntityCondition<Boolean> {
    private boolean hasAI = true;

    public EntityHasAICondition() {}

    private EntityHasAICondition(boolean hasAI) {
        this.hasAI = hasAI;
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof LivingEntity && other instanceof LivingEntity) {
            return ((LivingEntity)entity).hasAI() == getRequiredValue() && ((LivingEntity)other).hasAI() == getRequiredValue();
        }
        return false;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public EntityHasAICondition withRequiredValue(String requiredValue) {
        if(StringUtil.isNullOrEmpty(requiredValue)) {
            MobMerge.debug("Attempted to set " + this.getClass().getSimpleName() + " required value with a null/empty string!");
            return this; // don't bother with null or empty strings
        }
        boolean value = "true".equalsIgnoreCase(requiredValue);
        return new EntityHasAICondition(value);
    }

    @Override
    public Boolean getRequiredValue() {
        return hasAI;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("hasAI");
    }
}
