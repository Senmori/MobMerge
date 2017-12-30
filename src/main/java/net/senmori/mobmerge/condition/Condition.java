package net.senmori.mobmerge.condition;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.defaults.EntityAgeCondition;
import net.senmori.mobmerge.condition.defaults.EntityMatchingColorCondition;
import net.senmori.mobmerge.condition.defaults.EntityTypeCondition;
import net.senmori.mobmerge.condition.defaults.ValidEntityCondition;
import org.bukkit.DyeColor;
import org.bukkit.Keyed;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

import java.util.function.BiPredicate;

public interface Condition<T, U> extends BiPredicate<T, T>, Keyed {

    /**
     * This condition tests if both entities are valid. (i.e. entity is not null, and {@link Entity#isValid()} returns true).<br>
     */
    ValidEntityCondition VALID_ENTITY = ConditionManager.registerDefaultCondition(new ValidEntityCondition());

    /**
     *  This conditions tests if the entities are of the same {@link org.bukkit.entity.EntityType}
     */
    EntityTypeCondition VALID_TYPE = ConditionManager.registerDefaultCondition(new EntityTypeCondition());

    /**
     *  This conditions tests the both entities implements {@link Colorable} and are the same {@link DyeColor}
     */
    EntityMatchingColorCondition VALID_COLOR = ConditionManager.registerDefaultCondition(new EntityMatchingColorCondition());

    /**
     *  This condition tests that both entities implement {@link Ageable} and are adults. See {@link Ageable#isAdult()}.
     */
    EntityAgeCondition VALID_AGE = ConditionManager.registerDefaultCondition(new EntityAgeCondition());

    /**
     * Get a new instance of this Condition while attempting to parse the given value.<br>
     * If the value cannot be parsed, this will return the old Condition.
     * @param requiredValue the new value to parse
     * @return the new Condition
     */
    default Condition withRequiredValue(String requiredValue) {
        return this;
    }

    /**
     * This compares two objects of the same type.
     * @param first the first object
     * @param other the second object
     * @return true if the implementing class has compared the objects and found that they are equal in some manner.
     */
    public boolean test(T first, T other);

    /**
     * Get the value this condition must meet/compared against to return true.<br>
     * @return the object the Condition must compare against
     */
    public U getRequiredValue();

    /**
     * Get the priority of this Condition.<br>
     * The priority dictates in which order Conditions are ran.<br>
     * A lower priority indicates the Condition will be ran first.<br>
     * Any implementing classes should not use negative values, as those are reserved for default conditions.
     * @return the condition's priority
     */
    public Priority getPriority();

    /**
     * Priority dictates in which order {@link Condition}s are ran.<br>
     * Higher priorities will run first.
     */
    public enum Priority {
        DEFAULT(-1),
        HIGHEST(1),
        HIGH(2),
        NORMAL(3),
        LOW(4),
        LOWEST(5);

        private final int priority;
        private Priority(int priority) {
            this.priority = priority;
        }

        public int getPriorityID() {
            return priority;
        }
    }
}
