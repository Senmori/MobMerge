package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

@EntityCondition(description = "This conditions tests that two Zombies have the same age.")
public class ZombieAgeCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof Zombie && other instanceof Zombie) {
            return ((Zombie)first).isBaby() == ((Zombie)other).isBaby();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public String getName() {
        return "zombie-age";
    }
}
