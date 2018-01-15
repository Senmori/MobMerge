package net.senmori.mobmerge.condition.verify;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.EntityCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;

@EntityCondition(defaultCondition = true, description = "Checks if two entities are not tamed.")
public class NotTamedCondition extends DefaultCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Tameable && other instanceof Tameable) {
            return !((Tameable)entity).isTamed() && !((Tameable)other).isTamed();
        }
        return true;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("not-tamed");
    }
}
