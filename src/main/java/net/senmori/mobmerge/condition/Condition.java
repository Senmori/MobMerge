package net.senmori.mobmerge.condition;

import org.bukkit.Keyed;
import org.bukkit.entity.Entity;

import java.util.function.BiPredicate;

/**
 * Represents a condition which is applied to two entities of the same type.<br>
 */
public interface Condition extends Keyed, BiPredicate<Entity, Entity> {
    /**
     * This compares two objects of the same type.
     * @param first the first object
     * @param other the second object
     * @return true if the implementing class has compared the objects and found that they are equal in some manner.
     */
    boolean test(Entity first, Entity other);

    /**
     * Get the priority of this Condition.<br>
     * The priority dictates in which order Conditions are ran.<br>
     * A lower priority indicates the Condition will be ran first.<br>
     * Any implementing classes should not use negative values, as those are reserved for default conditions.
     * @return the condition's priority
     */
    Priority getPriority();

}
