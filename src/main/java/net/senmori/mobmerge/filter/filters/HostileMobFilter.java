package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HostileMobFilter implements EntityFilter {
    @Override
    public List<EntityType> getAllowedTypes() {
        List<EntityType> result = Lists.newArrayList();
        result.addAll(Stream.of(EntityType.values())
                            .filter(type -> type.getEntityClass() != null)
                            .filter(type -> Monster.class.isAssignableFrom(type.getEntityClass()))
                            .collect(Collectors.toList())); // all hostile mobs
        result.add(EntityType.GHAST);
        result.add(EntityType.MAGMA_CUBE);
        result.add(EntityType.SLIME);
        return result;
    }
}
