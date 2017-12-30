package net.senmori.mobmerge.condition.entity.zombie;

import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

/**
 * This condition checks if two zombies are the same age.(i.e. adult or baby)
 */
public class ZombieAgeCondition extends EntityCondition<Boolean> {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity.getType() == EntityType.ZOMBIE && other.getType() == EntityType.ZOMBIE) {
            Zombie first = (Zombie)entity;
            Zombie second = (Zombie)other;
            return first.isBaby() == second.isBaby();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }
}
