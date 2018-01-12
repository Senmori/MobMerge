package net.senmori.mobmerge.filter.filters;

import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Tameable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TameableFilter implements EntityFilter {
    @Override
    public List<EntityType> getAllowedTypes() {
        return Stream.of(EntityType.values())
                     .filter(type -> type.getEntityClass() != null)
                     .filter(type -> Tameable.class.isAssignableFrom(type.getEntityClass()))
                     .collect(Collectors.toList());
    }
}
