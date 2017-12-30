package net.senmori.mobmerge.condition.defaults;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;

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
        return true;
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }
}
