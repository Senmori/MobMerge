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
    private static final Map<String, EntityFilter> filters = Maps.newHashMap();

    private static final EntityFilters INSTANCE = new EntityFilters();

    private EntityFilters() {
        register(new HostileMobFilter());
        register(new LivingEntityFilter());
        register(new PassiveMobFilter());
        register(new TameableFilter());
        register(new ZombieFilter());
        register(new SkeletonFilter());
    }

    public static EntityFilters getInstance() {
        return INSTANCE;
    }

    public Map<String, EntityFilter> getFilters() {
        return ImmutableMap.copyOf(filters);
    }

    public <T extends EntityFilter> T register(T entityFilter) {
        filters.put(entityFilter.getName(), entityFilter);
        return entityFilter;
    }

    public boolean removeFilter(String key) {
        return filters.remove(key) != null;
    }

    public boolean hasFilter(String key) {
        return filters.containsKey(key.toLowerCase());
    }

    public EntityFilter getFilter(String key) {
        return filters.get(key.toLowerCase());
    }
}
