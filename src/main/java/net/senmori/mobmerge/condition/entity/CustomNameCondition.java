package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.senlib.util.StringUtils;
import org.bukkit.entity.Entity;

@EntityCondition(description = "This condition tests that two entities do NOT have custom names before being merged.")
public class CustomNameCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        return StringUtils.equals(first.getCustomName(), other.getCustomName());
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public String getName() {
        return "no-custom-name";
    }
}
