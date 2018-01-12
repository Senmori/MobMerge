package net.senmori.mobmerge.filter.filters;

import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LivingEntityFilter implements EntityFilter {
    @Override
    public List<EntityType> getAllowedTypes() {
        List<EntityType> result = Stream.of(EntityType.values())
                                        .filter(type -> type.getEntityClass() != null)
                                        .filter(EntityType::isAlive)
                                        .collect(Collectors.toList());
        result.remove(EntityType.ARMOR_STAND);
        return result;
    }
}
