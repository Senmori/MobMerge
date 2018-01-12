package net.senmori.mobmerge.condition.type;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This condition tests for a string value on two entities.<br>
 * This could be a custom name, or scoreboard tags, or anything else than can be serialized as a string.
 */
public abstract class StringCondition implements Condition {
    private String value = "";

    public StringCondition() {
        value = "";
    }

    public StringCondition(String value) {
        this.value = value;
    }

    @Override
    public Condition setRequiredValue(String requiredValue) {
        this.value = StringUtil.isNullOrEmpty(requiredValue) ? "" : requiredValue;
        return null;
    }

    @Override
    public String getRequiredValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    public abstract boolean test(Entity entity, Entity other);

    public abstract Priority getPriority();

    public abstract NamespacedKey getKey();

    public abstract Condition clone();
}
