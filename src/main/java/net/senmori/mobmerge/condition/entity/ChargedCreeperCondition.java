package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;

/**
 * This condition tests two {@link Creeper}s and checks if their {@link Creeper#isPowered()} status is the same as {@link #getRequiredValue()}.<br>
 * If false, both entities must NOT be powered.<br>
 * If true, both entities MUST be powered.<br>
 * To ignore this condition, remove it from the {@link net.senmori.mobmerge.options.EntityMatcherOptions}
 */
public class ChargedCreeperCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Creeper && other instanceof Creeper) {
            return ((Creeper)entity).isPowered() == getRequiredValue() && ((Creeper)other).isPowered() == getRequiredValue();
        }
        return false;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("charged");
    }
}