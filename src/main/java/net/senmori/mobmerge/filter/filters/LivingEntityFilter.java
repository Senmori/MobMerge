package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LivingEntityFilter implements EntityFilter {
    private final Set<EntityType> types;
    public LivingEntityFilter() {
        types = Stream.of(EntityType.values())
                                        .filter(type -> type.getEntityClass() != null)
                                        .filter(EntityType::isAlive)
                                        .collect(Collectors.toSet());
        types.remove(EntityType.ARMOR_STAND);
        types.remove(EntityType.PLAYER);
    }
    @Override
    public List<EntityType> getAllowedTypes() {
        return Lists.newArrayList(types);
    }

    @Override
    public String getName() {
        return "living";
    }

    @Override
    public boolean apply(Entity entity) {
        return entity != null && types.contains(entity.getType());
    }
}
