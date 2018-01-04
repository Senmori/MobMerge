package net.senmori.mobmerge.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.annotation.Excluded;
import net.senmori.mobmerge.condition.defaults.EntityAgeCondition;
import net.senmori.mobmerge.condition.defaults.EntityColorCondition;
import net.senmori.mobmerge.condition.defaults.EntityTypeCondition;
import net.senmori.mobmerge.condition.defaults.ValidEntityCondition;
import org.apache.commons.lang3.Validate;
import org.bukkit.NamespacedKey;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class handles the registration of {@link Condition}s.
 */
public final class ConditionManager {
    private static final BiMap<NamespacedKey, Condition> CONDITIONS = HashBiMap.create();
    private static final BiMap<NamespacedKey, Condition> DEFAULT_CONDITIONS = HashBiMap.create();
    private static final Map<NamespacedKey, Class<? extends Condition>> CLASS_MAP = Maps.newHashMap();

    public static void initDefaults() {
        registerDefaultCondition(new ValidEntityCondition());
        registerDefaultCondition(new EntityTypeCondition());
        registerDefaultCondition(new EntityColorCondition());
        registerDefaultCondition(new EntityAgeCondition());
    }

    /**
     * Register a new condition.
     * <br>
     * Default conditions cannot be registered via this method. Those must be registered via {@link #registerDefaultCondition(Condition)}
     * @param condition the condition to register
     * @return the registered condition
     */
    public static <T extends Condition> T registerCondition(T condition) {
        Validate.isTrue(!isDefaultCondition(condition), "Cannot register a default condition as non-default");
        CONDITIONS.put(condition.getKey(), condition);
        CLASS_MAP.put(condition.getKey(), condition.getClass());
        return condition;
    }

    /**
     * Register a new default condition.<br>
     * A default condition is a condition which is excluded from serialization via the {@link Excluded} annotation.
     * @param condition the new default condition to register
     * @return the registered condition
     */
    public static <T extends Condition> T registerDefaultCondition(T condition) {
        Validate.isTrue(isDefaultCondition(condition), "Cannot register a non-default condition as default");
        DEFAULT_CONDITIONS.put(condition.getKey(), condition);
        CLASS_MAP.put(condition.getKey(), condition.getClass());
        return condition;
    }

    /**
     * Gets the appropriate condition for the given key
     * @param key the key to check for
     * @return the appropriate condition or null
     */
    public static Condition getCondition(NamespacedKey key) {
        if(DEFAULT_CONDITIONS.get(key) != null) {
            return DEFAULT_CONDITIONS.get(key);
        }
        return CONDITIONS.get(key);
    }

    /**
     * Get the condition class which belongs to the given NamespacedKey.
     * @param key the key to check for
     * @return the appropriate condition class, or null
     */
    public static Class<? extends Condition> getConditionClass(NamespacedKey key) {
        return CLASS_MAP.get(key);
    }

    /**
     * Check if the given condition is a default condition.<br>
     * A default condition is a condition which is excluded from serialization via the {@link Excluded} annotation.
     * @param condition the condition to check
     * @return true if the Condition is a default condition
     */
    public static boolean isDefaultCondition(Condition condition) {
        return condition.getClass().isAnnotationPresent(Excluded.class);
    }

    /**
     * Get the matching {@link NamespacedKey} for a condition
     * @param condition the condition to check for
     * @return the appropriate {@link NamespacedKey} or null
     */
    public static NamespacedKey getKey(Condition condition) {
        NamespacedKey key = null;
        if((key = DEFAULT_CONDITIONS.inverse().get(condition)) != null) {
            return key;
        }
        return CONDITIONS.inverse().get(condition);
    }

    /**
     * Remove a condition that has the given {@link NamespacedKey}
     * @param key the key to remove
     * @return true if the condition was successfully removed
     */
    public static boolean removeCondition(NamespacedKey key) {
        return CONDITIONS.remove(key) != null;
    }

    /**
     * Sorts the given list of conditions by their {@link Priority}
     * @param conditions the list of conditions to sort
     * @return a sorted list of conditions
     */
    public static <T extends Condition> List<T> sortConditionsByPriority(List<T> conditions) {
        return conditions.stream().sorted(new Comparator<T>() {
            @Override
            public int compare(T first, T second) {
                return Integer.compare(first.getPriority().getPriorityID(), second.getPriority().getPriorityID());
            }
        }).collect(Collectors.toList());
    }

    /**
     * Gets a copy of the default conditions, sorted by their {@link Priority}
     * @return a sorted copy of the default conditions
     */
    public static List<Condition> getDefaultConditions() {
        return sortConditionsByPriority(Lists.newArrayList(DEFAULT_CONDITIONS.values())); // sort them
    }

    /**
     * Gets a copy of all registered, non-default conditions, sorted by their {@link Priority}
     * @return a sorted copy of all registered, non-default conditions.
     */
    public static List<Condition> getConditions() {
        return sortConditionsByPriority(Lists.newArrayList(CONDITIONS.values()));
    }
}
