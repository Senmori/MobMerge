package net.senmori.mobmerge.condition.verify;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;

@EntityCondition(defaultCondition = true)
public abstract class DefaultCondition implements Condition {

    public abstract boolean test(Entity entity, Entity other);


    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }
}
