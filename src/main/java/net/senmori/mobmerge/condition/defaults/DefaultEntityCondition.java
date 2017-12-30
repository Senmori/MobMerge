package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.entity.Entity;

/**
 * A Default Entity Condition is a condition that does not get serialized because it is always performed on the entity <br>
 * regardless of the entity's type.<br>
 * Examples of default entity conditions would be if the entity is valid, or if the entity's existing count is too high to<br>
 * merge more entities into.
 * @param <T> the type of object that this entity is being compared against.
 */
public abstract class DefaultEntityCondition<T> extends EntityCondition<T> {

    public abstract boolean test(Entity entity, Entity other);

    public abstract T getRequiredValue();

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }
}
