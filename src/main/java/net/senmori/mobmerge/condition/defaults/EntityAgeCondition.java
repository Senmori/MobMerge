package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import net.senmori.senlib.annotation.Excluded;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

/**
 * This condition tests if two entities have the same age.<br>
 * If the entities are {@link Ageable}, e.g. cows, pigs, etc., then it checks if they both are either an adult/baby<br>
 * If the entities are {@link Zombie}s, then it checks if both entities are adults/babies.
 */
@Excluded(reason = "defaultCondition")
public class EntityAgeCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Ageable && other instanceof Ageable) {
            return ((Ageable)entity).isAdult() == ((Ageable)other).isAdult();
        }
        if(entity instanceof Zombie && other instanceof Zombie) {
            return ((Zombie)entity).isBaby() == ((Zombie)other).isBaby();
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

    @Override
    public EntityAgeCondition clone() {
        return new EntityAgeCondition();
    }
}
