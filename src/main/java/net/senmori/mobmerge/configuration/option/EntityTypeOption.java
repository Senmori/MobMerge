package net.senmori.mobmerge.configuration.option;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.senmori.mobmerge.configuration.resolver.EntityTypeListResolver;
import net.senmori.mobmerge.filter.EntityFilters;
import net.senmori.mobmerge.filter.filters.LivingEntityFilter;
import net.senmori.senlib.configuration.option.ListOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EntityTypeOption extends ListOption<EntityType> {
    private static final Pattern NEGATION_PATTERN = Pattern.compile("-");
    private static final EntityTypeListResolver resolver = new EntityTypeListResolver();

    private List<String> filters = Lists.newArrayList();
    public EntityTypeOption(String key) {
        super(key, Lists.newArrayList());
        setResolver(resolver);
    }

    public EntityTypeOption(String key, List<EntityType> defaultTypes) {
        super(key, defaultTypes);
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

                boolean negated = false;
                if(str.startsWith(NEGATION_PATTERN.pattern())) {
                    // it's negated
                    str = str.replaceFirst(NEGATION_PATTERN.pattern(), "");
                    negated = true;
                }
                if(EntityFilters.getInstance().hasFilter(str)) {
                    if(negated) {
                        this.list.removeAll(EntityFilters.getInstance().getFilter(str).getAllowedTypes());
                    } else {
                        this.list.addAll(EntityFilters.getInstance().getFilter(str).getAllowedTypes());
                    }
                } else {
                    EntityType type = EntityType.fromName(str.toLowerCase());
                    if (type != null && type.isAlive()) {
                        this.list.add(type);
                    }
                }
            }
        });
        this.list = this.list.stream().distinct().collect(Collectors.toList()); // filter duplicates
    }

    @Override
    public boolean load(FileConfiguration config) {
        setList(resolver.resolve(config, getPath()));

        List<String> filter = resolver.getFilters(config, getPath());
        if(filter != null) {
            this.filters = filter;
        }
        return this.currentValue != null;
    }

    @Override
    public boolean save(FileConfiguration config) {
        config.set(getPath(), getStringList());
        return true;
    }

    public List<String> getStringList() {
        List<String> result = Lists.newArrayList();
        List<EntityType> values = getValue();

        if(filters != null) {
            for(String filter : filters) {
                String sub = filter;
                if(filter.startsWith(NEGATION_PATTERN.pattern())) {
                    sub = filter.replaceFirst(NEGATION_PATTERN.pattern(), "");
                }
                if(EntityFilters.getInstance().hasFilter(sub)) {
                    values.removeAll(EntityFilters.getInstance().getFilter(sub).getAllowedTypes());
                } else {
                    EntityType toRemove = EntityType.fromName(sub);
                    if(toRemove != null) {
                        values.remove(toRemove);
                    }
                }
                result.add(filter);
            }
        }

        for(EntityType type : values) {
            if(type != null && type.isAlive()) {
                result.add(type.getName());
            }
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    public EntityTypeListResolver getResolver() {
        return resolver;
    }

    public List<String> getFilters() {
        return ImmutableList.copyOf(filters);
    }

    public void addFilter(String filterName) {
        this.filters.add(filterName);
    }

    public void removeFilter(String filter) {
        this.filters.remove(filter);
    }

    public void setFilters(List<String> filters) {
        if(filters == null || filters.isEmpty()) {
            this.filters = Lists.newArrayList(); // reset filters
            return;
        }
        this.filters.clear();
        this.filters.addAll(filters);
    }

    private void populateLivingEntities(List<EntityType> list) {
        list.addAll(new LivingEntityFilter().getAllowedTypes());
    }
}
