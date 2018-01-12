package net.senmori.mobmerge.filter.filters;

import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PassiveMobFilter implements EntityFilter {
    @Override
    public List<EntityType> getAllowedTypes() {
        return Stream.of(EntityType.values())
                     .filter(type -> type.getEntityClass() != null)
                     .filter(type -> Animals.class.isAssignableFrom(type.getEntityClass()))
                     .collect(Collectors.toList());
    }
}
