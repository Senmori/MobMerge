package net.senmori.mobmerge.configuration.options.types;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.stream.Stream;

public class EntityTypeListOption extends ListOption<EntityType> {
    private List<EntityType> types = Lists.newArrayList();

    public static EntityTypeListOption newOption(String key, List<EntityType> types) {
        return new EntityTypeListOption(key, types);
    }

    protected EntityTypeListOption(String key, List<EntityType> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public List<EntityType> getValue() {
        return types;
    }

    @Override
    public void setList(List<EntityType> value) {
        if(value == null || value.isEmpty()) {
            types = Lists.newArrayList(); // reset array list
            return;
        }
        Stream stream = value.stream().filter(t -> t instanceof EntityType);
        if(stream.count() > 0) {
            types.clear();
            stream.forEach(c -> {
                types.add((EntityType)c);
            });
        }
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        if(!(config.get(getPath()) instanceof List)) return false; // it's not a list

        types.clear();
        List<String> list = config.getStringList(getPath());
        for(String str : list) {
            try {
                //TODO: Implement conditions on entities when attempting to merge them
                types.add(EntityType.fromName(str));
            } catch (IllegalArgumentException e) {
                if (ConfigManager.VERBOSE.getValue()) {
                    MobMerge.LOG.warning("Failed to load entity with name " + str);
                }
                return false;
            }
        }
        return !types.isEmpty();
    }
}
