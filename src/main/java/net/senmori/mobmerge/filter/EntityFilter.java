package net.senmori.mobmerge.filter;

import org.bukkit.entity.EntityType;

import java.util.List;

public interface EntityFilter {

    public List<EntityType> getAllowedTypes();
}
