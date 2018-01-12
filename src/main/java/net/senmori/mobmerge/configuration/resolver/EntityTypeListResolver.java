package net.senmori.mobmerge.configuration.resolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.filter.EntityFilters;
import net.senmori.senlib.configuration.resolver.types.ListResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityTypeListResolver extends ListResolver<EntityType> {
    public static final Pattern NEGATION_PATTERN = Pattern.compile("-");
    public EntityTypeListResolver() {
        super(List.class);
    }

    @Override
    public List<EntityType> resolve(FileConfiguration config, String path) {
        List<EntityType> result = Lists.newArrayList();
        if(!config.contains(path)) return null; // empty list
        if(!config.isList(path)) {
            // check for just 'living'
            if(config.isString(path)) {
                String str = config.getString(path);
                if(str.equals("living")) {
                    result.addAll(Stream.of(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList()));
                } else {
                    MobMerge.LOG.warning("Expected " + this.getValueClass() + ", but found " + config.get(path).getClass() + ".");
                    return result;
                }
            } else {
                MobMerge.LOG.warning("Expected " + this.getValueClass() + ", but found " + config.get(path).getClass() + ".");
                return result;
            }
        }

        List<String> types = config.getStringList(path);

        result = populateList(types);
        return null;
    }

    @Override
    public List<EntityType> resolve(ConfigurationSection section, String path) {
        List<EntityType> result = Lists.newArrayList();
        if(!section.contains(path)) return result; // empty list
        if(!section.isList(path)) {
            // check for just 'living'
            if(section.isString(path)) {
                String str = section.getString(path);
                if(EntityFilters.hasFilter(str)) {
                    result.addAll(EntityFilters.getFilter(str).getAllowedTypes());
                } else {
                    MobMerge.LOG.warning("Expected " + this.getValueClass() + ", but found " + section.get(path).getClass() + ".");
                    return result;
                }
            } else {
                MobMerge.LOG.warning("Expected " + this.getValueClass() + ", but found " + section.get(path).getClass() + ".");
                return result;
            }
        }

        List<String> types = section.getStringList(path);
        result = populateList(types);
        return result;
    }

    private List<EntityType> populateList(List<String> list) {
        Set<EntityType> result = Sets.newHashSet();
        for(String s : list) {
            if(EntityFilters.hasFilter(s)) {
                result.addAll(EntityFilters.getFilter(s).getAllowedTypes());
                continue; // found the filter; keep going
            }
            boolean negated = false;
            String typeString = s;
            if(NEGATION_PATTERN.matcher(s).matches()) {
                negated = true;
                typeString = NEGATION_PATTERN.matcher(s).replaceFirst("");
            }
            if(negated && EntityFilters.hasFilter(typeString)) {
                result.removeAll(EntityFilters.getFilter(typeString).getAllowedTypes());
                continue; // removed those entities
            }
            EntityType type = EntityType.fromName(typeString);
            if(type == null) {
                continue;
            }
            if(negated && result.contains(type)) {
                result.remove(type);
            } else if(type.isAlive()) {
                result.add(type);
            }
        }
        return result.stream().distinct().collect(Collectors.toList()); // remove duplicates
    }
}
