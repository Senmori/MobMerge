package net.senmori.mobmerge.condition.verify;

import net.senmori.mobmerge.annotation.EntityCondition;
import org.bukkit.entity.Entity;

@EntityCondition(defaultCondition = true, description = "Checks if two entities are of the same EntityType")
public class ValidTypeCondition extends DefaultCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity.getType() == other.getType();
    }

    @Override
    public String getName() {
        return "entity-type";
    }
}
