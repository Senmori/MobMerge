package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;

@EntityCondition(description = "This conditions tests that two Parrots have the same color(variant)")
public class ParrotColorCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof Parrot && other instanceof Parrot) {
            return ((Parrot)first).getVariant() == ((Parrot)other).getVariant();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public String getName() {
        return "parrot-color";
    }
}
