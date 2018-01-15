package net.senmori.mobmerge.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.senlib.annotation.Excluded;
import org.apache.commons.lang3.Validate;
import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handles the registration of {@link Condition}s.
 */
public final class ConditionManager {
    private static ConditionManager INSTANCE = new ConditionManager();

    private final BiMap<NamespacedKey, Condition> CONDITIONS = HashBiMap.create();
    private final BiMap<NamespacedKey, Condition> DEFAULT_CONDITIONS = HashBiMap.create();

    private ConditionManager() {

    }


    public static ConditionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Register a new condition.
     * <br>
     * Default conditions cannot be registered via this method. Those must be registered via {@link #registerDefaultCondition(Condition)}
     * @param condition the condition to register
     * @return the registered condition or null if the condition or condition class was already registered
     */
    @Nullable
    public <T extends Condition> T registerCondition(T condition) {
        Validate.notNull(condition, "Cannot register a null condition");
        Validate.isTrue(!isDefaultCondition(condition), "Cannot register a default condition as non-default");
        CONDITIONS.putIfAbsent(condition.getKey(), condition);
        return condition;
    }

    /**
     * Register a new default condition.<br>
     * A default condition is a condition which is excluded from serialization via the {@link Excluded} annotation.
     * @param condition the new default condition to register
     * @return the registered condition or null if the condition or condition class was already registered
     */
    @Nullable
    public <T extends Condition> T registerDefaultCondition(T condition) {
        Validate.notNull(condition, "Cannot register a null condition");
        Validate.isTrue(isDefaultCondition(condition), "Cannot register a non-default condition as default");
        Condition cond = DEFAULT_CONDITIONS.putIfAbsent(condition.getKey(), condition);
        return condition;
    }

    /**
     * Gets the appropriate condition for the given key
     * @param key the key to check for
     * @return the appropriate condition or null if no condition exists by that name
     */
    @Nullable
    public Condition getCondition(String key) {
        if(DEFAULT_CONDITIONS.values().stream().anyMatch(condition -> condition.getKey().getKey().equals(key))) {
            return DEFAULT_CONDITIONS.values().stream().filter(cond -> cond.getKey().getKey().equals(key))
                    .findFirst().orElse(null);
        }
        return CONDITIONS.values().stream().filter(cond -> cond.getKey().getKey().equals(key)).findFirst().orElse(null);
    }

    /**
     * Check if the given condition is a default condition.<br>
     * A default condition is a condition which is excluded from serialization via the {@link EntityCondition} annotation.
     * @param condition the condition to check
     * @return true if the Condition is a default condition
     */
    public boolean isDefaultCondition(Condition condition) {
        Class<? extends Condition> aClass = condition.getClass();
        if(aClass.isAnnotationPresent(EntityCondition.class)) {
            // test for default
            return aClass.getAnnotation(EntityCondition.class).defaultCondition();
        }
        return false;
    }

    /**
     * Get the matching {@link NamespacedKey} for a condition
     * @param condition the condition to check for
     * @return the appropriate {@link NamespacedKey} or null
     */
    @Nullable
    public NamespacedKey getKey(Condition condition) {
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
    public boolean removeCondition(NamespacedKey key) {
        return CONDITIONS.remove(key) != null;
    }

    /**
     * Sorts the given list of conditions by their {@link Priority}
     * @param conditions the list of conditions to sort
     * @return a sorted list of conditions
     */
    public <T extends Condition> List<T> sortConditionsByPriority(List<T> conditions) {
        return conditions.stream().sorted(new ConditionComparator()).collect(Collectors.toList());
    }

    /**
     * Gets a copy of the default conditions, sorted by their {@link Priority}
     * @return a sorted copy of the default conditions
     */
    public List<Condition> getDefaultConditions() {
        return sortConditionsByPriority(Lists.newArrayList(DEFAULT_CONDITIONS.values())); // sort them
    }

    /**
     * Gets a copy of all registered, non-default conditions, sorted by their {@link Priority}
     * @return a sorted copy of all registered, non-default conditions.
     */
    public List<Condition> getConditions() {
        return sortConditionsByPriority(Lists.newArrayList(CONDITIONS.values()));
    }
}
