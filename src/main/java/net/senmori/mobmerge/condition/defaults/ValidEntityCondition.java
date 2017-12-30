package net.senmori.mobmerge.condition.defaults;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * This condition checks if two entities are both valid.<br>
 * i.e. not null, and {@link Entity#isValid()} returns true.
 */
public class ValidEntityCondition extends DefaultEntityCondition<Boolean> {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity != null && other != null && entity.isValid() && other.isValid() && entity instanceof LivingEntity && other instanceof LivingEntity;
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.DEFAULT;
    }
}
