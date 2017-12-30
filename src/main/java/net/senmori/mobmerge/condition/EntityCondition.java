package net.senmori.mobmerge.condition;

import org.bukkit.entity.Entity;

public abstract class EntityCondition<T> implements Condition<Entity, T> {

    public abstract boolean test(Entity entity, Entity other);

    public abstract Priority getPriority();

    public abstract T getRequiredValue();
}
