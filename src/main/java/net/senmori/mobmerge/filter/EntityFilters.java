package net.senmori.mobmerge.filter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.filter.filters.HostileMobFilter;
import net.senmori.mobmerge.filter.filters.LivingEntityFilter;
import net.senmori.mobmerge.filter.filters.PassiveMobFilter;
import net.senmori.mobmerge.filter.filters.SkeletonFilter;
import net.senmori.mobmerge.filter.filters.TameableFilter;
import net.senmori.mobmerge.filter.filters.ZombieFilter;

import java.util.Map;

public final class EntityFilters {

    private static Map<String, EntityFilter> filters = Maps.newHashMap();

    public static final HostileMobFilter HOSTILE = register("hostile", new HostileMobFilter());
    public static final LivingEntityFilter LIVING = register("living", new LivingEntityFilter());
    public static final PassiveMobFilter PASSIVE = register("passive", new PassiveMobFilter());
    public static final TameableFilter TAMEABLE = register("tameable", new TameableFilter());
    public static final ZombieFilter ZOMBIE = register("zombies", new ZombieFilter());
    public static final SkeletonFilter SKELETON = register("skeletons", new SkeletonFilter());

    public static Map<String, EntityFilter> getFilters() {
        return ImmutableMap.copyOf(filters);
    }

    public static <T extends EntityFilter> T register(String filterKey, T entityFilter) {
        filters.put(filterKey, entityFilter);
        return entityFilter;
    }

    public static boolean removeFilter(String key) {
        return filters.remove(key) != null;
    }

    public static boolean hasFilter(String key) {
        return filters.containsKey(key.toLowerCase());
    }

    public static EntityFilter getFilter(String key) {
        return filters.get(key.toLowerCase());
    }
}
