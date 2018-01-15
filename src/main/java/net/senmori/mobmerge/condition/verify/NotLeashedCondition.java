package net.senmori.mobmerge.condition.verify;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.EntityCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

@EntityCondition(defaultCondition = true, description = "Checks if two entities are not leashed.")
public class NotLeashedCondition extends DefaultCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof LivingEntity && other instanceof LivingEntity) {
            return !((LivingEntity)entity).isLeashed() && !((LivingEntity)other).isLeashed();
        }
        return true;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("not-leashed");
    }
}
