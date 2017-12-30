package net.senmori.mobmerge.action;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.senmori.mobmerge.action.death.EntityDeathAction;
import net.senmori.mobmerge.action.death.entity.AgeableDeathAction;
import net.senmori.mobmerge.action.death.entity.ColorableDeathAction;
import net.senmori.mobmerge.action.death.entity.ZombieDeathAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Set;

public final class EntityActionManager {

    private static final BiMap<NamespacedKey, EntityAction> REGISTERED_ACTIONS = HashBiMap.create();
    private static final Set<EntityAction> defaultDeathActions = Sets.newHashSet();
    private static final Set<EntityAction> defaultMergeActions = Sets.newHashSet();
    private static final Multimap<EntityType, EntityAction> deathActionMap = ArrayListMultimap.create();
    private static final Multimap<EntityType, EntityAction> mergeActionMap = ArrayListMultimap.create();

    public static void init() {
        // register actions here
        registerDefaultDeathAction(new EntityDeathAction());
        registerDefaultDeathAction(new AgeableDeathAction());
        registerDefaultDeathAction(new ColorableDeathAction());
        registerDeathAction(EntityType.ZOMBIE, new ZombieDeathAction());
    }

    public static <T extends EntityAction> T getAction(NamespacedKey key) {
        return (T)REGISTERED_ACTIONS.get(key);
    }

    /**
     * DEATH ACTIONS
     */

    public static <T extends EntityAction> T registerDefaultDeathAction(T action) {
        REGISTERED_ACTIONS.put(action.getKey(), action);
        defaultDeathActions.add(action);
        return action;
    }

    public static <T extends EntityAction> T registerDeathAction(EntityType type, T action) {
        deathActionMap.put(type, action);
        REGISTERED_ACTIONS.putIfAbsent(action.getKey(), action);
        return action;
    }

    public static List<EntityAction> getDeathActionsFor(EntityType type) {
        List<EntityAction> result = Lists.newArrayList(defaultDeathActions);
        result.addAll(deathActionMap.get(type));
        return result;
    }

    public static List<EntityAction> getDefaultDeathActions() {
        return Lists.newArrayList(defaultDeathActions);
    }

    public static <T extends EntityAction> T getDefaultDeathAction(NamespacedKey key) {
        if(defaultDeathActions.contains(REGISTERED_ACTIONS.get(key))) {
            return (T)REGISTERED_ACTIONS.get(key);
        }
        return null;
    }

    /**
     * MERGE ACTIONS
     */

    public static <T extends EntityAction> T registerDefaultMergeAction(T action) {
        REGISTERED_ACTIONS.put(action.getKey(), action);
        defaultMergeActions.add(action);
        return action;
    }

    public static <T extends EntityAction> T registerMergeAction(EntityType type, T action) {
        mergeActionMap.put(type, action);
        REGISTERED_ACTIONS.putIfAbsent(action.getKey(), action);
        return action;
    }


    public static List<EntityAction> getDefaultMergeActions() {
        return Lists.newArrayList(defaultMergeActions);
    }

    public static List<EntityAction> getMergeActionsFor(EntityType type) {
        List<EntityAction> result = Lists.newArrayList(defaultMergeActions);
        result.addAll(mergeActionMap.get(type));
        return result;
    }

    public static <T extends EntityAction> T getDefaultMergeAction(NamespacedKey key) {
        if(defaultMergeActions.contains(REGISTERED_ACTIONS.get(key))) {
            return (T)REGISTERED_ACTIONS.get(key);
        }
        return null;
    }

}
