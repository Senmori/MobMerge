package net.senmori.mobmerge.condition.verify;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This conditions checks if two entities are
 */
@EntityCondition(defaultCondition = true, description = "Checks if two entities are not null and valid.")
public class ValidEntityCondition extends DefaultCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity != null && other != null && entity.isValid() && other.isValid();
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("valid-entity");
    }

    @Override
    public Priority getPriority() {
        return Priority.DEFAULT;
    }
}
