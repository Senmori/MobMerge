package net.senmori.mobmerge.configuration.option;

import com.google.common.collect.Lists;
import net.senmori.mobmerge.configuration.resolver.EntityTypeListResolver;
import net.senmori.senlib.configuration.option.ListOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityTypeOption extends ListOption<EntityType> {
    private static final EntityTypeListResolver resolver = new EntityTypeListResolver();

    private boolean livingEntityWildcard = false;
    public EntityTypeOption(String key) {
        super(key, Lists.newArrayList());
        setWildcard(false);
        setResolver(resolver);
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
            if (obj instanceof EntityType) {
                this.list.add((EntityType) obj);
            }
            if (obj instanceof String) {
                String str = (String) obj;

                if (str.equalsIgnoreCase("living")) {
                    setWildcard(true);
                    populateLivingEntities(this.list);
                }

                EntityType type = EntityType.fromName(str.toLowerCase());
                if (type != null) {
                    this.list.add(type);
                }
            }
        });
        this.list = this.list.stream().distinct().collect(Collectors.toList()); // filter duplicates
        checkWildcard();
    }

    @Override
    public boolean load(FileConfiguration config) {
        setWildcard(false);
        setList(resolver.resolve(config, getPath()));

        checkWildcard();
        return this.currentValue != null;
    }

    @Override
    public void save(FileConfiguration config) {
        config.set(getPath(), getStringList());
    }

    public List<String> getStringList() {
        checkWildcard();
        List<String> result = Lists.newArrayList();
        List<EntityType> values = getValue();
        if(isUsingWildcard()) {
            result.add("living");
        }
        for(EntityType type : values) {
            if(isUsingWildcard() && type.isAlive()) continue;
            if(type.isAlive()) {
                result.add(type.getName());
            }
        }
        return result.stream().distinct().collect(Collectors.toList()); // remove duplicates
    }

    public boolean isUsingWildcard() {
        return livingEntityWildcard;
    }

    public void setWildcard(boolean value) {
        this.livingEntityWildcard = value;
    }

    private void populateLivingEntities(List<EntityType> list) {
        list.addAll(Arrays.stream(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList()));
    }

    private void checkWildcard() {
        List<EntityType> slave = Stream.of(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList());
        List<EntityType> master = this.list;
        if(master.containsAll(slave)) {
            setWildcard(true);
        }
    }
}
