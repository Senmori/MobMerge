package net.senmori.mobmerge.configuration.option;

import net.senmori.senlib.annotation.RequiresReload;
import net.senmori.senlib.configuration.option.ChatColorOption;
import net.senmori.senlib.configuration.option.NumberOption;
import net.senmori.senlib.configuration.option.SectionOption;
import net.senmori.senlib.configuration.option.StringOption;
import net.senmori.senlib.configuration.option.VectorOption;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public class DefaultSection extends SectionOption {

    public final VectorOption RADIUS = addOption("Radius", new VectorOption("radius", new Vector(5, 5, 5)));

    @RequiresReload(description = "This option requires a reload because of how BukkitTasks work.")
    public final NumberOption INTERVAL = addOption("Interval", new NumberOption("interval", 5));
    public final NumberOption MAX_COUNT = addOption("Max Count", new NumberOption("count", 256));
    public final ChatColorOption COLOR = addOption("Chat Color", new ChatColorOption("color", ChatColor.RED));
    public final StringOption ENTITY_TAG = addOption("Entity Tag", new StringOption("tag", "mergedEntity"));

    public DefaultSection(String key) {
        super(key);
    }

    @Override
    public boolean load(ConfigurationSection section) {
        RADIUS.setValue(RADIUS.getResolver().resolve(section, RADIUS.getPath()));
        return true;
    }

    @Override
    public boolean save(ConfigurationSection configurationSection) {
        getOptions().values().forEach(opt -> {
            if(!(opt instanceof SectionOption) && opt.hasResolver()) {
                opt.getResolver().save(section, opt.getPath(), opt.getValue());
            } else {
                section.set(opt.getPath(), opt.getValue());
            }
        });
        return true;
    }
}
