package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;

@EntityCondition(description = "This condition tests if two Horses have the same Color")
public class HorseColorCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof Horse && other instanceof Horse) {
            return ((Horse)first).getColor() == ((Horse)other).getColor();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public String getName() {
        return "horse-color";
    }
}
