package net.senmori.mobmerge.condition.defaults;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import net.senmori.senlib.annotation.Excluded;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

@Excluded(reason = "defaultCondition")
public class LeashedEntityCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if (entity instanceof LivingEntity) {
            return ! ( (LivingEntity) entity ).isLeashed();
        }
        return ! ( other instanceof LivingEntity ) || ! ( (LivingEntity) other ).isLeashed();
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("leashedEntity");
    }

    @Override
    public Condition clone() {
        return new LeashedEntityCondition();
    }
}
