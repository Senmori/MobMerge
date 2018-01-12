package net.senmori.mobmerge.filter.filters;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.filter.EntityFilter;
import org.bukkit.entity.EntityType;

import java.util.List;

public class SkeletonFilter implements EntityFilter {
    @Override
    public List<EntityType> getAllowedTypes() {
        return Lists.newArrayList(EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON);
    }
}
