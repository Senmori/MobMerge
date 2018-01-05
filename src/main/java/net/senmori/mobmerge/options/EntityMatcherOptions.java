package net.senmori.mobmerge.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import net.senmori.mobmerge.configuration.option.ConfigurationKey;
import net.senmori.mobmerge.configuration.option.types.ChatColorOption;
import net.senmori.mobmerge.configuration.option.types.NumberOption;
import net.senmori.mobmerge.configuration.option.types.StringListOption;
import net.senmori.mobmerge.configuration.option.types.VectorOption;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityMatcherOptions {
    private final EntityType entityType;
    private final String typeName;
    private final Map<ConfigurationKey, ConfigOption> options = Maps.newHashMap();
    private final ConfigManager configManager;
    private final EntityOptionManager optionManager;

    // options keys
    private final ConfigurationKey RADIUS_KEY = ConfigurationKey.create("Mob Radius", VectorOption.class);
    private final ConfigurationKey COUNT_KEY = ConfigurationKey.create("Mob Max Count", NumberOption.class);
    private final ConfigurationKey CHAT_COLOR_KEY = ConfigurationKey.create("Mob Chat Color", ChatColorOption.class);

    // options
    private final VectorOption RADIUS = VectorOption.newOption("radius", ConfigManager.RADIUS.getValue());
    private final NumberOption COUNT = NumberOption.newOption("count", ConfigManager.MAX_COUNT.getValue().intValue());
    private final ChatColorOption CHAT_COLOR = ChatColorOption.newOption("color", ConfigManager.DEFAULT_COLOR.getValue());

    private List<Condition> conditions = Lists.newArrayList();

    public EntityMatcherOptions(ConfigManager configManager, EntityType entityType) {
        this.entityType = entityType;
        this.typeName = entityType.getName().toLowerCase();
        addOption(RADIUS_KEY, RADIUS);
        addOption(COUNT_KEY, COUNT);
        addOption(CHAT_COLOR_KEY, CHAT_COLOR);
        this.configManager = configManager;
        this.optionManager = configManager.getEntityOptionManager();
        conditions.addAll(ConditionManager.getDefaultConditions());
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Vector getRadius() {
        return RADIUS.getValue();
    }

    public boolean setRadius(@NotNull Vector vector) {
        Vector old = getRadius();
        RADIUS.setValue(vector);
        return !old.equals(getRadius());
    }

    public int getMaxCount() {
        return COUNT.getValue().intValue() < 0 ? Integer.MAX_VALUE : COUNT.getValue().intValue();
    }

    public boolean setMaxCount(int maxCount) {
        if(maxCount < 0) return false;
        if(maxCount == getMaxCount()) return false;
        int old = getMaxCount();
        COUNT.setValue(maxCount);
        return old != getMaxCount();
    }

    public ChatColor getChatColor() {
        return CHAT_COLOR.getValue();
    }

    public boolean setChatColor(ChatColor color) {
        if(color == getChatColor()) return false;
        ChatColor old = getChatColor();
        CHAT_COLOR.setValue(color);
        return old != getChatColor();
    }

    public int getCount(Entity entity) {
        if(entity.getType() != this.getEntityType()) return -1;
        String customName = entity.getCustomName();
        if(customName != null && customName.startsWith(this.getChatColor().toString())) {
            try {
                return Integer.parseInt(ChatColor.stripColor(customName));
            } catch(NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }

    public <T extends ConfigOption> boolean addOption(ConfigurationKey key, T option) {
        return options.putIfAbsent(key, option) == null;
    }

    public boolean load(FileConfiguration config) {
        ConfigurationSection mobSection = config.getConfigurationSection(ConfigManager.MOBS_KEY.getName());
        if(mobSection == null) {
            MobMerge.LOG.warning("Expected configuration section at " + ConfigManager.MOBS_KEY.getName() + ". Found " + config.get(ConfigManager.MOBS_KEY.getName()).getClass().getName());
            return false;
        }
        ConfigurationSection typeSection = mobSection.getConfigurationSection(this.typeName);
        String path = ConfigManager.MOBS_KEY.getName() + "." + this.typeName;
        if(typeSection == null) {
            MobMerge.LOG.warning("Expected section at " + path + ". Found null");
            return false;
        }

        // we are already at "mobs.<type>"
        // e.g. mobs.zombie.<node> <-- currently at 'node'
        for(String node : typeSection.getKeys(false)) {
            if(!typeSection.isConfigurationSection(node)) {
                // it's not a 'conditions' node
                // check for radius, count, color
               if(node.equals(COUNT.getPath())) {
                   COUNT.setValue(typeSection.getInt(COUNT.getPath()));
               }
               if(node.equals(CHAT_COLOR.getPath())) {
                   CHAT_COLOR.setValue(ChatColor.valueOf(typeSection.getString(CHAT_COLOR.getPath()).toUpperCase()));
               }
            }
            if(node.equals(RADIUS.getPath())) {
                if( !(typeSection.get(RADIUS.getPath()) instanceof Vector) ) {
                    // treat it as a single integer
                    String numberStr = typeSection.getString(RADIUS.getPath());
                    if(NumberUtils.isNumber(numberStr)) {
                        try {
                            Number num = NumberFormat.getInstance().parse(numberStr);
                            RADIUS.setValue(new Vector(num.doubleValue(), num.doubleValue(), num.doubleValue()));
                        } catch (ParseException e) {
                            if(ConfigManager.VERBOSE.getValue()) {
                                MobMerge.LOG.warning("Failed to import radius for " + typeSection.getCurrentPath() + ". Setting it to default value.");
                            }
                            RADIUS.setValue(ConfigManager.RADIUS.getValue());
                        }
                    }
                } else {
                    // it's a vector
                    Vector vector = typeSection.getVector(RADIUS.getPath());
                    if(vector == null) {
                        MobMerge.LOG.warning("Expected vector at mobs." + this.typeName + "." + node + ". Found: " + typeSection.get(node).getClass().getName());
                        MobMerge.LOG.warning("Setting default radius for entity type " + this.entityType + " to " + RADIUS.getValue().toString());
                        continue;
                    }
                    RADIUS.setValue(vector);
                }
            }
            if(typeSection.isConfigurationSection(node) && node.equals(ConfigManager.CONDITIONS_KEY.getName())) {
                // condition node
                ConfigurationSection condNode = typeSection.getConfigurationSection(node);
                for(String key : condNode.getKeys(false)) {
                    Condition condition = null;
                    // try to parse key for a valid namespace
                    NamespacedKey nameKey = MobMerge.newKey(key.replaceAll("\"", "")); // remove double quotes
                    condition = ConditionManager.getCondition(nameKey);
                    if(condition == null) {
                        if(ConfigManager.VERBOSE.getValue()) {
                            MobMerge.LOG.info("Failed to find condition \'" + key + "\' in section " + condNode.getCurrentPath());
                        }
                        continue;
                    }
                    MobMerge.debug("Found condition \'" + condition.getKey().toString() + "\' with value \'" + condition.getRequiredValue().toString() + "\' for entity " + this.typeName);
                    this.conditions.add(condition.setRequiredValue(condNode.getString(key)));
                }
            }
        }
        return true;
    }

    public void save(FileConfiguration config) {
        String path = ConfigManager.MOBS_KEY.getName() + "." + this.typeName;

        ConfigurationSection section = config.getConfigurationSection(path);

        for(ConfigOption opt : options.values()) {
            section.set(opt.getPath(), opt.getValue());
        }

        if(!conditions.isEmpty()) {
            ConfigurationSection condSection = section.getConfigurationSection(ConfigManager.CONDITIONS_KEY.getName());
            if(condSection == null) {
                condSection = section.createSection(ConfigManager.CONDITIONS_KEY.getName());
                MobMerge.debug("Created empty conditions section for " + section.getCurrentPath());
            }
            for(Condition con : this.conditions) {
                if(ConditionManager.isDefaultCondition(con)) continue; // skip default conditions
                NamespacedKey conditionKey = ConditionManager.getKey(con);
                if(conditionKey == null || conditionKey.toString().isEmpty()) {
                    if(ConfigManager.VERBOSE.getValue()) {
                        MobMerge.LOG.warning("Unknown condition \'" + con.getClass().getName() + "\'. Could not find matching NamespacedKey.");
                    }
                    continue;
                }
                // we have to surround namespaced keys with double quotes because yaml is awesome like that!
                condSection.set("\"" + conditionKey.toString() + "\"", con.getStringValue());
            }
        }
    }

    public boolean test(Entity entity, Entity other) {
        for(Condition cond : getConditions()) {
            if(!cond.test(entity, other)) {
                if(ConfigManager.VERBOSE.getValue()) {
                    MobMerge.LOG.warning("Condition \'" + cond.getClass().getName() + "\' failed. Required value (" + cond.getStringValue() + ")");
                }
                return false;
            }
        }
        return true;
    }


    /**
     * Get an immutable copy of all {@link ConfigOption}s for this entity type.
     * @return an immutable copy of config options.
     */
    public Map<ConfigurationKey, ConfigOption> getOptions() {
        return ImmutableMap.copyOf(this.options);
    }

    /**
     * Add a condition.
     * @param condition the condition to add
     * @return true if the condition was successfully added.
     */
    public boolean addCondition(Condition condition) {
        return this.conditions.add(condition);
    }

    /**
     * Remove a condition with a given {@link NamespacedKey}.<br>
     * Default conditions can never be removed.
     * @param key the key of the condition that should be removed
     * @return true if the condition was successfully removed.
     */
    public boolean removeCondition(NamespacedKey key) {
        return this.conditions
                       .stream()
                       .filter(condition -> !ConditionManager.isDefaultCondition(condition))
                       .filter(condition -> condition.getKey().equals(key))
                       .findFirst()
                       .map(condition -> this.conditions.remove(condition))
                       .orElse(false);
    }

    /**
     * Remove a condition.<br>
     * Default conditions can never be removed.
     * @param condition the condition to remove
     * @return true if the condition was successfully removed.
     */
    public boolean removeCondition(Condition condition) {
        if(ConditionManager.isDefaultCondition(condition)) return false;
        return this.conditions.remove(condition);
    }

    /**
     * Get an immutable copy of the conditions for this entity type.<br>
     * These conditions are sorted by their {@link Priority}.
     * @return an immutable copy of the conditions
     */
    public List<Condition> getConditions() {
        return ImmutableList.copyOf(ConditionManager.sortConditionsByPriority(this.conditions));
    }
}
