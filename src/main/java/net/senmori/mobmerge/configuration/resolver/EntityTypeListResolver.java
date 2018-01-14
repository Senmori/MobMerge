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
            return getSingleNode(config, path);
        }

        List<String> types = config.getStringList(path);

        return populateList(types);
    }

    @Override
    public List<EntityType> resolve(ConfigurationSection section, String path) {
        List<EntityType> result = Lists.newArrayList();
        if(!section.contains(path)) return result; // empty list
        if(!section.isList(path)) {
            // check for just 'living'
            return getSingleNode(section, path);
        }

        List<String> types = section.getStringList(path);

        return populateList(types);
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
            if(s.startsWith(NEGATION_PATTERN.pattern())) {
                typeString = s.replaceFirst(NEGATION_PATTERN.pattern(), "");
                negated = true;
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
            } else if(!negated && type.isAlive()) {
                result.add(type);
            }
        }
        return result.stream().distinct().collect(Collectors.toList()); // remove duplicates
    }

    public List<String> getFilters(FileConfiguration config, String path) {
        if(!config.contains(path)) return Lists.newArrayList();
        if(!config.isList(path)) return Lists.newArrayList();

        return getFilters(config.getStringList(path));
    }

    public List<String>  getFilters(ConfigurationSection section, String path) {
        if(!section.contains(path)) return Lists.newArrayList();
        if(!section.isList(path)) return Lists.newArrayList();

        return getFilters(section.getStringList(path));
    }

    private List<String> getFilters(List<String> list) {
        List<String> result = Lists.newArrayList();
        for(String s : list) {
            if(EntityFilters.hasFilter(s)) {
                result.add(s);
            } else if(s.startsWith(NEGATION_PATTERN.pattern())) {
                result.add(s);
            }
        }
        return result;
    }

    private List<EntityType> getSingleNode(FileConfiguration config, String path) {
        if(!config.isList(path)) {
            // check for just 'living'
            if(config.isString(path)) {
                return getEntityListFromNode(config.getString(path));
            } else {
                MobMerge.LOG.warning("Expected " + this.getValueClass() + ", but found " + config.get(path).getClass() + ".");
                return null;
            }
        }
        return Lists.newArrayList();
    }

    private List<EntityType> getSingleNode(ConfigurationSection section, String path) {
        if(!section.isList(path)) {
            // check for just 'living'
            if(section.isString(path)) {
                return getEntityListFromNode(section.getString(path));
            } else {
                MobMerge.LOG.warning("Expected " + this.getValueClass() + ", but found " + section.get(path).getClass() + ".");
                return null;
            }
        }
        return Lists.newArrayList();
    }

    private List<EntityType> getEntityListFromNode(String node) {
        return EntityFilters.hasFilter(node) ? EntityFilters.getFilter(node).getAllowedTypes() : null;
    }
}
