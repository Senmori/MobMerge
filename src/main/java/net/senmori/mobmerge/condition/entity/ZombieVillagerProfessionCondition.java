package net.senmori.mobmerge.condition.entity;

import net.senmori.mobmerge.annotation.EntityCondition;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.Priority;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ZombieVillager;

@EntityCondition(description = "This conditions tests two zombie villagers to see if they have the same Profession.")
public class ZombieVillagerProfessionCondition implements Condition {
    @Override
    public boolean test(Entity first, Entity other) {
        if(first instanceof ZombieVillager && other instanceof ZombieVillager) {
            return ((ZombieVillager)first).getVillagerProfession() == ((ZombieVillager)other).getVillagerProfession();
        }
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public String getName() {
        return "zombie-villager-profession";
    }
}
