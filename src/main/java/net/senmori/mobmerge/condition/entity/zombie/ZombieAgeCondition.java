package net.senmori.mobmerge.condition.entity.zombie;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.BooleanCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

public class ZombieAgeCondition extends BooleanCondition {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Zombie && other instanceof Zombie) {
            return ((Zombie)entity).isBaby() == ((Zombie)other).isBaby();
        }
        return false;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("zombieAge");
    }
}
