package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import net.senmori.senlib.annotation.Excluded;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This condition checks if both entities are valid.<br>
 * A valid entity is one whom is not null and {@link Entity#isValid()} returns true.
 */
@Excluded(reason = "defaultCondition")
public class ValidEntityCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity != null && other != null && entity.isValid() && other.isValid();
    }

    @Override
    public Priority getPriority() {
        return Priority.DEFAULT;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("validEntity");
    }

    @Override
    public ValidEntityCondition clone() {
        return new ValidEntityCondition();
    }
}
