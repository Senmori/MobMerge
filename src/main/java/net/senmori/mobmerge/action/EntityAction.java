package net.senmori.mobmerge.action;

import net.senmori.mobmerge.option.EntityMatcherOptions;
import org.bukkit.Keyed;
import org.bukkit.entity.Entity;

/**
 * This is the contract for all Entity actions.<br>
 * Actions are performed on an entity AFTER it passes all applicable conditions.<br>
 * These actions include the entity that is being operated on, and all nearby entities.
 */
public interface EntityAction extends Keyed {

    public void perform(Entity entity, Entity other, EntityMatcherOptions options);
}
