package net.senmori.mobmerge.configuration.option;

import com.google.common.collect.Lists;
import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.SectionOption;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MobsSection extends SectionOption {

    public final EntityTypeOption DEFAULT_MOBS = addOption("Default Mobs", new EntityTypeOption("default"));

    public final BooleanOption CUSTOM_NAME_VISIBLE = addOption("Custom Name Visible:", new BooleanOption("custom-name-visible", true));

    public MobsSection(String key) {
        super(key, key);
    }

    @Override
    public boolean load(ConfigurationSection section) {
        if(!section.contains(DEFAULT_MOBS.getPath())) return false;
        if(!section.isList(DEFAULT_MOBS.getPath())) return false;

        List<String> types = section.getStringList(getPath());

        List<EntityType> result = Lists.newArrayList();
        for(String s : types) {
            if(s.equalsIgnoreCase("living")) {
                result.addAll(Arrays.stream(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList()));
                DEFAULT_MOBS.setWildcard(true);
            }
            EntityType type = EntityType.fromName(s.toLowerCase());
            if(type != null) {
                result.add(type);
            }
        }
        DEFAULT_MOBS.setList(result.stream().distinct().collect(Collectors.toList())); // filter duplicates

        return true;
    }

    @Override
    public boolean save(ConfigurationSection section) {
        section.set(DEFAULT_MOBS.getPath(), DEFAULT_MOBS.getStringList());
        return true;
    }
}
