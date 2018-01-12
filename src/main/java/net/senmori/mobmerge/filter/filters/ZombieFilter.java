package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.EntityType;

import java.util.List;

public class ZombieFilter implements EntityFilter {
    @Override
    public List<EntityType> getAllowedTypes() {
        return Lists.newArrayList(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.HUSK);
    }
}
