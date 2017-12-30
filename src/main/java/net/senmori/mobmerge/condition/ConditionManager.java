package net.senmori.mobmerge.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import org.bukkit.NamespacedKey;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ConditionManager {
    private static final BiMap<NamespacedKey, Condition> CONDITIONS = HashBiMap.create();
    private static final BiMap<NamespacedKey, Condition> DEFAULT_CONDITIONS = HashBiMap.create();

    static {
        try {
            Class.forName("net.senmori.mobmerge.condition.Condition");
        } catch (ClassNotFoundException e) {
            MobMerge.debug("Failed to find class \'Condition\'. I'd like to see how this is possible");
        }
    }

    public static void init() {}


    public static <T extends Condition> T registerCondition(NamespacedKey key, T condition) {
        CONDITIONS.put(key, condition);
        return condition;
    }

    public static <T extends Condition> T registerDefaultCondition(NamespacedKey key, T condition) {
        DEFAULT_CONDITIONS.put(key, condition);
        MobMerge.debug("Registered default condition: " + condition.getClass().getSimpleName());
        return condition;
    }

    public static <T extends Condition> T getCondition(NamespacedKey key) {
        return (T)CONDITIONS.get(key);
    }

    public static NamespacedKey getKey(Condition condition) {
        NamespacedKey key = null;
        if((key = DEFAULT_CONDITIONS.inverse().get(condition)) != null) {
            return key;
        }
        return CONDITIONS.inverse().get(condition);
    }

    public static boolean removeCondition(NamespacedKey key) {
        return CONDITIONS.remove(key) != null;
    }

    public static <T extends Condition> List<T> sortConditionsByPriority(List<T> conditions) {
        return conditions.stream().sorted(new Comparator<T>() {
            @Override
            public int compare(T first, T second) {
                return Integer.compare(first.getPriority().getPriorityID(), second.getPriority().getPriorityID());
            }
        }).collect(Collectors.toList());
    }

    public static List<Condition> getDefaultConditions() {
        return sortConditionsByPriority(Lists.newArrayList(DEFAULT_CONDITIONS.values())); // sort them
    }
}
