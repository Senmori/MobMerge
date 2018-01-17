package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;

@EntityCondition(description = "This condition compares slime sizes. If different, the slimes are not merged.")
public class SlimeSizeCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof Slime && other instanceof Slime) {
            return ((Slime)first).getSize() == ((Slime)other).getSize();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public String getName() {
        return "slime-size";
    }
}
