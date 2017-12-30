package net.senmori.mobmerge.action.death.entity;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.action.EntityAction;
import net.senmori.mobmerge.option.EntityMatcherOptions;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.material.Colorable;

public class ColorableDeathAction implements EntityAction {
    @Override
    public void perform(Entity entity, Entity other, EntityMatcherOptions options) {
        if(entity instanceof Colorable && other instanceof Colorable) {
            ((Colorable)other).setColor(((Colorable)entity).getColor());
        }
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("onColorableDeath");
    }
}
