package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PassiveMobFilter implements EntityFilter {
    private final Set<EntityType> types;
    public PassiveMobFilter() {
        types = Stream.of(EntityType.values())
                     .filter(type -> type.getEntityClass() != null)
                     .filter(type -> Animals.class.isAssignableFrom(type.getEntityClass()))
                     .collect(Collectors.toSet());
    }
    @Override
    public List<EntityType> getAllowedTypes() {
        return Lists.newArrayList(types);
    }

    @Override
    public String getName() {
        return "passive_mobs";
    }

    @Override
    public boolean apply(Entity entity) {
        return entity != null && types.contains(entity.getType());
    }
}
