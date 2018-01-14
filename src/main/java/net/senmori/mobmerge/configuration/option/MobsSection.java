package net.senmori.mobmerge.configuration.option;

import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.SectionOption;
import org.bukkit.configuration.ConfigurationSection;

public class MobsSection extends SectionOption {

    public final EntityTypeOption DEFAULT_MOBS = addOption("Default Mobs", new EntityTypeOption("default"));

    public final BooleanOption CUSTOM_NAME_VISIBLE = addOption("Custom Name Visible", new BooleanOption("custom-name-visible", true));

    public MobsSection(String key) {
        super(key);
    }

    @Override
    public boolean load(ConfigurationSection section) {
        DEFAULT_MOBS.setList(DEFAULT_MOBS.getResolver().resolve(section, DEFAULT_MOBS.getPath()));
        DEFAULT_MOBS.setFilters(DEFAULT_MOBS.getResolver().getFilters(section, DEFAULT_MOBS.getPath()));
        return true;
    }

    @Override
    public boolean save(ConfigurationSection section) {
        section.set(DEFAULT_MOBS.getPath(), DEFAULT_MOBS.getStringList());
        return true;
    }
}
