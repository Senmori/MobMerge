package net.senmori.mobmerge.configuration.option.types;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.configuration.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginLogger;

import java.util.List;

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
        types.clear();
        types.addAll(value);
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;

        types.clear();
        List<String> list = config.getStringList(getPath());
        if(!list.isEmpty()) {
            for(String str : list) {
                try {
                    //TODO: Implement conditions on entities when attempting to merge them
                    EntityType type = EntityType.fromName(str);
                    types.add(EntityType.fromName(str));
                } catch (IllegalArgumentException e) {
                    if (ConfigManager.VERBOSE.getValue()) {
                        MobMerge.LOG.warning("Failed to load entity with name " + str);
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void save(FileConfiguration config) {
        List<String> saved = Lists.newArrayList();
        types.stream().distinct().forEach(t -> {
            saved.add(t.getName());
        });
        config.set(getPath(), saved);
    }
}
