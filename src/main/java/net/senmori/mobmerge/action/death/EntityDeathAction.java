package net.senmori.mobmerge.action.death;

import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.action.EntityAction;
import net.senmori.mobmerge.action.EntityActionManager;
import net.senmori.mobmerge.option.EntityMatcherOptions;
import net.senmori.mobmerge.util.EntityUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class EntityDeathAction implements EntityAction {
    @Override
    public void perform(Entity entity, Entity other, EntityMatcherOptions options) {
        int count = EntityUtil.getEntityCount(entity, options);
        if(count > 1) {
            LivingEntity clone = (LivingEntity)entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
            if(count > 2) {
                clone.setCustomName(options.getChatColor() + Integer.toString(count - 1));
            }
            EntityActionManager.getDeathActionsFor(entity.getType()).forEach(action -> action.perform(entity, clone, options));
        }
    }

    @Override
    public NamespacedKey getKey() {
        return MobMerge.newKey("onDeath");
    }
}
