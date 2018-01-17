package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;

@EntityCondition(description = "This condition tests that two Llamas have the same color")
public class LlamaColorCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof Llama && other instanceof Llama) {
            return ((Llama)first).getColor() == ((Llama)other).getColor();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public String getName() {
        return "llama-color";
    }
}
