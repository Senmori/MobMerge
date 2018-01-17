package net.senmori.mobmerge.filter;

import net.senmori.mobmerge.filter.filters.HostileMobFilter;
import net.senmori.mobmerge.filter.filters.LivingEntityFilter;
import net.senmori.mobmerge.filter.filters.PassiveMobFilter;
import net.senmori.mobmerge.filter.filters.SkeletonFilter;
import net.senmori.mobmerge.filter.filters.TameableFilter;
import net.senmori.mobmerge.filter.filters.ZombieFilter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

/**
 * This interface represents a list of {@link EntityType} that can be used to filter entities.
 */
public interface EntityFilter extends Filter<EntityType, Entity> {
    /**
     * A filter that only accepts hostile mobs.
     */
    public static final HostileMobFilter HOSTILE = new HostileMobFilter();

    /**
     * A filter that only accepts all living entities EXCEPT players and armor stands.
     */
    public static final LivingEntityFilter LIVING = new LivingEntityFilter();

    /**
     * A filter that only accepts passive mobs.
     */
    public static final PassiveMobFilter PASSIVE = new PassiveMobFilter();

    /**
     * A filter that only accepts tameable mobs.
     */
    public static final TameableFilter TAMEABLE = new TameableFilter();

    /**
     * A filter that only accepts zombies.
     */
    public static final ZombieFilter ZOMBIE = new ZombieFilter();

    /**
     * A filter that only accepts skeletons.
     */
    public static final SkeletonFilter SKELETON = new SkeletonFilter();




    public List<EntityType> getAllowedTypes();

    public String getName();

    /**
     * Test the entity to see if this filter applies to it.
     * @param entity the entity to check
     * @return true if this entity's {@link EntityType} is a part of this filter.
     */
    public boolean apply(Entity entity);
}

