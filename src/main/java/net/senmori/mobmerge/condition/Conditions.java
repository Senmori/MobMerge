package net.senmori.mobmerge.condition;

import net.senmori.mobmerge.condition.entity.generic.EntityCustomNameCondition;
import net.senmori.mobmerge.condition.entity.generic.EntityHasAICondition;
import net.senmori.mobmerge.condition.entity.generic.EntityHasCustomNameCondition;
import net.senmori.mobmerge.condition.entity.generic.EntityPersistentCondition;
import net.senmori.mobmerge.condition.entity.villager.VillagerMatchProfessionCondition;
import net.senmori.mobmerge.condition.entity.zombie.ZombieAgeCondition;
import org.bukkit.entity.LivingEntity;

/**
 * This is a holder class that is used as a central place to store non-default {@link Condition}s.
 */
public final class Conditions {

    /**
     * This condition tests that two zombies are the same age(i.e. adult or baby).
     */
    public static final ZombieAgeCondition ZOMBIE_AGE  = ConditionManager.registerCondition(new ZombieAgeCondition());

    /**
     * This conditions tests that two villagers have the same profession.
     */
    public static final VillagerMatchProfessionCondition VILLAGER_PROFESSION = ConditionManager.registerCondition(new VillagerMatchProfessionCondition());

    /**
     * This condition tests that two living entities both have AI.
     */
    public static final EntityHasAICondition ENTITY_AI = ConditionManager.registerCondition(new EntityHasAICondition());

    /**
     *  This condition tests that two entities both have the same {@link LivingEntity#getRemoveWhenFarAway()} value.<br>
     *  The vaule defaults to true.(They are NOT persistent)
     */
    public static final EntityPersistentCondition ENTITY_PERSISTENT = ConditionManager.registerCondition(new EntityPersistentCondition());

    /**
     * This condition tests that two entities have a specific custom name.
     */
    public static final EntityCustomNameCondition ENTITY_CUSTOM_NAME = ConditionManager.registerCondition(new EntityCustomNameCondition());

    /**
     * This condition tests that two entities have a custom name set.
     */
    public static final EntityHasCustomNameCondition ENTITY_HAS_CUSTOM_NAME = ConditionManager.registerCondition(new EntityHasCustomNameCondition());
}
