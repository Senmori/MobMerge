package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

/**
 * This condition checks if two entities both have the same age as the required value.<br>
 * i.e. both entities must be adults, or both must NOT be adults
 */
public class EntityAgeCondition extends DefaultEntityCondition<Boolean> {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Ageable && other instanceof Ageable) {
            return ( (Ageable) entity ).isAdult() == ( (Ageable) other ).isAdult();
        }
        /*
        if(entity instanceof Zombie && other instanceof Zombie) {
            return ((Zombie)entity).isBaby() == ((Zombie)other).isBaby();
        }
        */
        return true;
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("matchEntityAge");
    }
}
