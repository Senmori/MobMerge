package net.senmori.mobmerge.action.death.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.action.EntityAction;
import net.senmori.mobmerge.option.EntityMatcherOptions;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;

public class AgeableDeathAction implements EntityAction {
    @Override
    public void perform(Entity entity, Entity other, EntityMatcherOptions options) {
        if(entity instanceof Ageable && other instanceof Ageable) {
            if( ((Ageable)entity).isAdult()) {
                ((Ageable)other).setAdult();
            } else {
                ((Ageable)other).setBaby();
            }
        }
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("onAgeableDeath");
    }
}
