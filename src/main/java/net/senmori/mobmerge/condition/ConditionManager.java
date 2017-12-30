package net.senmori.mobmerge.condition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.defaults.DefaultEntityCondition;
import net.senmori.mobmerge.condition.entity.villager.VillagerMatchProfessionCondition;
import net.senmori.mobmerge.condition.entity.zombie.ZombieAgeCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ConditionManager {
    private static final BiMap<NamespacedKey, Condition> CONDITIONS = HashBiMap.create();
    private static final BiMap<NamespacedKey, Condition> DEFAULT_CONDITIONS = HashBiMap.create();

    static {
        try {
            Class.forName(Condition.class.getName()); // load default conditions
            Class.forName(Conditions.class.getName()); // load optional conditions
        } catch (ClassNotFoundException e) {
            MobMerge.debug("Failed to find class \'Condition\'. I'd like to see how this is possible");
        }
    }

    public static void init() { }


    public static <T extends Condition> T registerCondition(T condition) {
        CONDITIONS.put(condition.getKey(), condition);
        return condition;
    }

    public static <T extends Condition> T registerDefaultCondition(T condition) {
        DEFAULT_CONDITIONS.put(condition.getKey(), condition);
        MobMerge.debug("Registered default condition: " + condition.getClass().getSimpleName());
        return condition;
    }

    public static <T extends Condition> T getCondition(NamespacedKey key) {
        return (T)CONDITIONS.get(key);
    }

    public static boolean isDefaultCondition(Condition condition) {
        return condition instanceof DefaultEntityCondition;
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
