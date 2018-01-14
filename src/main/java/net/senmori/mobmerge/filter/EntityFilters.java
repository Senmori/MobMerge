package net.senmori.mobmerge.filter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public final class EntityFilters {

    private static Map<String, EntityFilter> filters = Maps.newHashMap();

    public static Map<String, EntityFilter> getFilters() {
        return ImmutableMap.copyOf(filters);
    }

    public static <T extends EntityFilter> T register(T entityFilter) {
        filters.put(entityFilter.getName(), entityFilter);
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
