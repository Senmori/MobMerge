package net.senmori.mobmerge.configuration.option;

import com.google.common.collect.Lists;
import net.senmori.senlib.configuration.option.ListOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityTypeOption extends ListOption<EntityType> {
    private static final String LIVING_ENTITY_WILDCARD = "living";

    private boolean livingEntityWildcard = false;
    public EntityTypeOption(String key) {
        super(key, Lists.newArrayList());
        setWildcard(false);
    }

    public EntityTypeOption(String key, List<EntityType> defaultTypes) {
        super(key, defaultTypes);
        setWildcard(false);
    }

    @Override
    public List<EntityType> getValue() {
        return list;
    }

    @Override
    public void setValue(List list) {
        list.forEach(obj -> {
            if(obj instanceof EntityType) {
                this.list.add((EntityType)obj);
            }
            if(obj instanceof String) {
                String str = (String)obj;

                if(str.equalsIgnoreCase(LIVING_ENTITY_WILDCARD)) {
                    setWildcard(true);
                    populateLivingEntities(this.list);
                }

                EntityType type = EntityType.fromName(str.toLowerCase());
                if(type != null) {
                    this.list.add(type);
                }
            }
        });
    }

    private void populateLivingEntities(List<EntityType> list) {
        list.addAll(Arrays.stream(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList()));
    }

    @Override
    public boolean load(FileConfiguration config) {
        if(!config.contains(getPath())) return false;
        if(!config.isList(getPath())) return false;
        setWildcard(false);

        List<String> types = config.getStringList(getPath());

        for(String s : types) {
            if(s.equalsIgnoreCase(LIVING_ENTITY_WILDCARD)) {
                populateLivingEntities(this.list);
                setWildcard(true);
            }
            EntityType type = EntityType.fromName(s.toLowerCase());
            if(type != null) {
                this.list.add(type);
            }
        }
        return true;
    }

    public List<String> getStringList() {
        List<String> result = Lists.newArrayList();
        List<EntityType> values = getValue();
        if(isUsingWildcard()) {
            values.removeAll(this.list.stream().filter(EntityType::isAlive).collect(Collectors.toList()));
            result.add("living");
        }
        for(EntityType type : values) {
            if(type.getName() != null) {
                result.add(type.getName());
            }
        }
        return result;
    }

    @Override
    public void save(FileConfiguration config) {
        List<String> result = Lists.newArrayList();
        List<EntityType> values = getValue();
        config.set(getPath(), getStringList());
    }

    public boolean isUsingWildcard() {
        return livingEntityWildcard;
    }

    public void setWildcard(boolean value) {
        this.livingEntityWildcard = value;
    }
}
