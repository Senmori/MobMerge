package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.Excluded;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;

@Excluded(reason = "defaultCondition")
public class EntityAgeCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Ageable && other instanceof Ageable) {
            return ((Ageable)entity).isAdult() == ((Ageable)other).isAdult();
        }
        return true; // return true because this is compared to ALL entities; so if they aren't Ageable, we don't care.
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("validEntityAge");
    }
}
