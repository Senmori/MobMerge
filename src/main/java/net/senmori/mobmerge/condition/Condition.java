package net.senmori.mobmerge.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * Represents a condition which is applied to two entities of the same type.<br>
 * <br>
 * The required value can have different meanings depending on what is being tested on the entities.<br>
 * For example, if two zombies are being compared, with a required value of true, then both zombies must have the
 *     same age for the condition to return true.<br>
 * As well, if two colorable entities(sheep) are being compared, with a required value of {@link org.bukkit.DyeColor#CYAN},
 *      then both sheep must have the required color for the condition to return true.
 */
public interface Condition extends Keyed {

    /**
     * Set the required value of this Condition.
     * @param requiredValue the new value to parse
     * @return the modified Condition to chain calls.
     * @throws IllegalArgumentException if the argument could not be parsed correctly.
     */
    default Condition setRequiredValue(String requiredValue) {
        return this;
    }

    /**
     * This compares two objects of the same type.
     * @param first the first object
     * @param other the second object
     * @return true if the implementing class has compared the objects and found that they are equal in some manner.
     */
    public abstract boolean test(Entity first, Entity other);

    /**
     * Get the value this condition must meet/compared against to return true.<br>
     * @return the object the Condition must compare against
     */
    public abstract Object getRequiredValue();

    /**
     * Get the String version of the value this condition must meet/be compared against to return true.<br>
     * @return the String version of the object the Condition must be compared against.
     */
    default String getStringValue() {
        return String.valueOf(getRequiredValue());
    }

    /**
     * Get the priority of this Condition.<br>
     * The priority dictates in which order Conditions are ran.<br>
     * A lower priority indicates the Condition will be ran first.<br>
     * Any implementing classes should not use negative values, as those are reserved for default conditions.
     * @return the condition's priority
     */
    public abstract Priority getPriority();


    abstract class Serializer<T extends Condition> {
        private NamespacedKey name;
        private Class<T> conditionClass;

        protected Serializer(NamespacedKey name, Class<T> clazz) {
            this.name = name;
            this.conditionClass = clazz;
        }

        public NamespacedKey getName() {
            return this.name;
        }

        public Class<T> getConditionClass() {
            return this.conditionClass;
        }

        public abstract void serialize(JsonObject json, T type, JsonSerializationContext context);

        public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
    }
}
