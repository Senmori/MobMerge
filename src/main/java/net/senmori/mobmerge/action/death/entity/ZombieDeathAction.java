package net.senmori.mobmerge.action.death.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.action.EntityAction;
import net.senmori.mobmerge.option.EntityMatcherOptions;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

public class ZombieDeathAction implements EntityAction {
    @Override
    public void perform(Entity entity, Entity other, EntityMatcherOptions options) {
        if(entity instanceof Zombie && other instanceof Zombie) {
            Zombie zombie = (Zombie)entity;
            Zombie clone = (Zombie)other;

            clone.setBaby(zombie.isBaby());
            clone.getPassengers().forEach(Entity::remove); // remove all passengers
        }
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("onZombieDeath");
    }
}
