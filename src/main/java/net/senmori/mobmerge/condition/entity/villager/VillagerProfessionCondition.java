package net.senmori.mobmerge.condition.entity.villager;

import net.senmori.mobmerge.condition.EntityCondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

public class VillagerProfessionCondition extends EntityCondition<Villager.Profession> {

    private Villager.Profession profession = null;

    public static VillagerProfessionCondition newCondition(Villager.Profession profession) {
        VillagerProfessionCondition cond = new VillagerProfessionCondition();
        cond.profession = profession;
        return cond;
    }

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
    public Villager.Profession getRequiredValue() {
        return profession;
    }
}
