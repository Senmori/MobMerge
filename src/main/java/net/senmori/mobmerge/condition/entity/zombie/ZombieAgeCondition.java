package net.senmori.mobmerge.condition.entity.zombie;

import io.netty.util.internal.StringUtil;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

/**
 * This condition checks if two zombies are the same age.(i.e. adult or baby)
 */
public class ZombieAgeCondition extends EntityCondition<Boolean> {
    private boolean matchAge = true;

    public ZombieAgeCondition() {}

    private ZombieAgeCondition(boolean matchAge) {
        this.matchAge = matchAge;
    }

    @Override
    public ZombieAgeCondition withRequiredValue(String value) {
        if(StringUtil.isNullOrEmpty(value)) {
            MobMerge.LOG.warning("Attempt to set null/empty value in " + this.getClass().getSimpleName());
            return this;
        }
        return new ZombieAgeCondition(Boolean.parseBoolean(value));
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity.getType() == EntityType.ZOMBIE && other.getType() == EntityType.ZOMBIE) {
            Zombie first = (Zombie)entity;
            Zombie second = (Zombie)other;
            return !matchAge || ( first.isBaby() == second.isBaby() );
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public Boolean getRequiredValue() {
        return matchAge;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("matchZombieAge");
    }
}
