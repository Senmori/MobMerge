package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HostileMobFilter implements EntityFilter {
    private final Set<EntityType> types;
    public HostileMobFilter() {
        types = Sets.newHashSet();
        types.addAll(Stream.of(EntityType.values())
                           .filter(type -> type.getEntityClass() != null)
                           .filter(type -> Monster.class.isAssignableFrom(type.getEntityClass()))
                           .collect(Collectors.toSet())); // all hostile mobs
        types.add(EntityType.GHAST);
        types.add(EntityType.MAGMA_CUBE);
        types.add(EntityType.SLIME);
    }
    @Override
    public List<EntityType> getAllowedTypes() {
        return Lists.newArrayList(types);
    }

    @Override
    public String getName() {
        return "hostile_mobs";
    }

    @Override
    public boolean apply(Entity entity) {
        return entity != null && types.contains(entity.getType());
    }
}
