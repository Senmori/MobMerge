package net.senmori.mobmerge.condition.type;

import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This condition is a holder class for all conditions which take a boolean as a required value.
 */
public abstract class BooleanCondition implements Condition {
    private boolean value;

    public BooleanCondition() {
        this.value = true;
    }

    public BooleanCondition(boolean value) {
        this.value = value;
    }

    @Override
    public BooleanCondition setRequiredValue(String requiredValue) {
        value = Boolean.parseBoolean(requiredValue);
        return this;
    }

    @Override
    public Boolean getRequiredValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(getRequiredValue());
    }

    public abstract boolean test(Entity entity, Entity other);

    public abstract Priority getPriority();

    public abstract NamespacedKey getKey();
}