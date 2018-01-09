package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.StringCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * This conditions tests if two entities both have the same scoreboard tag.<br>
 */
public class EntityScoreboardTagCondition extends StringCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        return entity.getScoreboardTags().contains(getRequiredValue()) && other.getScoreboardTags().contains(getRequiredValue());
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("entityTag");
    }
}
