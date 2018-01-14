package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Set;

public class ZombieFilter implements EntityFilter {
    private final Set<EntityType> types;

    public ZombieFilter() {
        types = Sets.newHashSet(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.HUSK);
    }

    @Override
    public List<EntityType> getAllowedTypes() {
        return Lists.newArrayList(types);
    }

    @Override
    public String getName() {
        return "zombie_filter";
    }

    @Override
    public boolean apply(Entity entity) {
        return entity != null && types.contains(entity.getType());
    }
}
