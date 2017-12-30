package net.senmori.mobmerge.condition.entity.villager;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

public class VillagerMatchProfessionCondition extends EntityCondition<Boolean> {
    @Override
    public boolean test(Entity entity, Entity other) {
        if(entity instanceof Villager && other instanceof Villager) {
            Villager v = (Villager)entity;
            Villager otherV = (Villager)other;
            return v.getProfession() == otherV.getProfession();
        }
        return false;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public Boolean getRequiredValue() {
        return true;
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("matchVillagerProfession");
    }
}
