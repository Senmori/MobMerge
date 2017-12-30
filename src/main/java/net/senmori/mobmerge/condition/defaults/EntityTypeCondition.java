package net.senmori.mobmerge.condition.defaults;

import org.bukkit.entity.Entity;

/**
 * This condition checks to see if both entities are of the same {@link org.bukkit.entity.EntityType}
 */
public class EntityTypeCondition extends DefaultEntityCondition<Boolean> {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity.getType() == other.getType();
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }
}
