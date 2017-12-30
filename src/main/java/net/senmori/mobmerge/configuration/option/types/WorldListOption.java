package net.senmori.mobmerge.configuration.option.types;

import org.bukkit.World;

import java.util.List;

public class WorldListOption extends StringListOption {

    public static WorldListOption newOption(String key, List<String> defaultValues) {
        return new WorldListOption(key, defaultValues);
    }

    protected WorldListOption(String key, List<String> defaultValue) {
        super(key, defaultValue);
    }

    public boolean excludeWorld(World world) {
        return excludeWorld(world, false);
    }

    public boolean excludeWorld(World world, boolean useUUIDs) {
        return list.add( useUUIDs ? world.getUID().toString() : world.getName() );
    }

    public boolean removeWorld(World world) {
        return list.removeIf(w -> w.equals(world.getName())) || list.removeIf(w -> w.equals(world.getUID().toString()));
    }

    public boolean contains(World world) {
        return list.stream().anyMatch(w -> w.equals(world.getName())) || list.stream().anyMatch(w -> w.equals(world.getUID().toString()));
    }
}
