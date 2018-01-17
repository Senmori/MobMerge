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
            if(MobMerge.getInstance().getSettingsManager().VERBOSE.getValue()) {
                MobMerge.LOG.warning("Failed to load entity filters");
            }
            return null;
        }

        List<String> types = config.getStringList(path);

        return populateList(types);
    }

    @Override
    public List<EntityType> resolve(ConfigurationSection section, String path) {
        List<EntityType> result = Lists.newArrayList();
        if(!section.contains(path)) return result; // empty list
        if(!section.isList(path)) {
            if(MobMerge.getInstance().getSettingsManager().VERBOSE.getValue()) {
                MobMerge.LOG.warning("Failed to load entity filters");
            }
            return null;
        }

        List<String> types = section.getStringList(path);

        return populateList(types);
    }

    private List<EntityType> populateList(List<String> list) {
        Set<EntityType> result = Sets.newHashSet();
        for(String s : list) {
            if(EntityFilters.getInstance().hasFilter(s)) {
                result.addAll(EntityFilters.getInstance().getFilter(s).getAllowedTypes());
                continue; // found the filter; keep going
            }
            boolean negated = false;
            String typeString = s;
            if(s.startsWith(NEGATION_PATTERN.pattern())) {
                typeString = s.replaceFirst(NEGATION_PATTERN.pattern(), "");
                negated = true;
            }
            if(negated && EntityFilters.getInstance().hasFilter(typeString)) {
                result.removeAll(EntityFilters.getInstance().getFilter(typeString).getAllowedTypes());
                continue; // removed those entities
            }
            EntityType type = EntityType.fromName(typeString);
            if(type == null) {
                continue; // it's a filter
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
            if(EntityFilters.getInstance().hasFilter(s)) {
                result.add(s);
            } else if(s.startsWith(NEGATION_PATTERN.pattern())) {
                String temp = s.replaceFirst(NEGATION_PATTERN.pattern(), "");
                if(EntityFilters.getInstance().hasFilter(temp) || EntityType.fromName(temp) != null) {
                    result.add(s);
                } else {
                    if(MobMerge.getInstance().getSettingsManager().VERBOSE.getValue()) {
                        MobMerge.LOG.warning("Invalid filter: " + s);
                    }
                }
            }
        }
        return result;
    }
}
