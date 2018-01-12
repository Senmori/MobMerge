package net.senmori.mobmerge.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.condition.Priority;
import net.senmori.mobmerge.condition.type.ColorableCondition;
import net.senmori.mobmerge.configuration.SettingsManager;
import net.senmori.senlib.configuration.ConfigOption;
import net.senmori.senlib.configuration.option.ChatColorOption;
import net.senmori.senlib.configuration.option.NumberOption;
import net.senmori.senlib.configuration.option.VectorOption;
import org.apache.commons.lang.WordUtils;
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
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
public class EntityMatcherOptions {
    private final EntityType entityType;
    private final String typeName;
    private final String formattedTypeName;
    private final Map<String, ConfigOption> options = Maps.newHashMap();
    private final SettingsManager settingsManager;
    private final EntityOptionManager optionManager;
    private final ConditionManager conditionManager = ConditionManager.getInstance();

    // options
    private final VectorOption RADIUS;
    private final NumberOption COUNT;
    private final ChatColorOption CHAT_COLOR;

    private List<Condition> conditions = Lists.newArrayList();

    public EntityMatcherOptions(SettingsManager settingsManager, EntityType entityType) {
        this.entityType = entityType;
        this.typeName = entityType.getName().toLowerCase();
        this.formattedTypeName = WordUtils.capitalizeFully(this.typeName.replaceAll("_", " "));
        this.settingsManager = settingsManager;
        this.optionManager = settingsManager.getEntityOptionManager();
        conditions.addAll(conditionManager.getDefaultConditions());

        RADIUS = addOption(this.formattedTypeName + " Radius", new VectorOption("radius", settingsManager.DEFAULT_SECTION.RADIUS.getValue()));
        COUNT = addOption(this.formattedTypeName + " Max Count", new NumberOption("count", settingsManager.DEFAULT_SECTION.MAX_COUNT.getValue()));
        CHAT_COLOR = addOption(this.formattedTypeName + " Chat Color", new ChatColorOption("color", settingsManager.DEFAULT_SECTION.COLOR.getValue()));
    }

    public <T extends ConfigOption> T addOption(String key, T option) {
        options.put(key, option);
        return option;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public String getFormattedTypeName() {
        return this.formattedTypeName;
    }

    public Vector getRadius() {
        return RADIUS.getValue();
    }

    public boolean setRadius(Vector vector) {
        Vector old = getRadius();
        RADIUS.setValue(vector);
        return !old.equals(getRadius());
    }

    public int getMaxCount() {
        return COUNT.getValue().intValue() < 0 ? Integer.MAX_VALUE : COUNT.getValue().intValue();
    }

    /**
     * Set the new max count.
     * @param maxCount the new max count
     * @return true if the new max count is different than the old max count
     */
    public boolean setMaxCount(int maxCount) {
        if(maxCount == getMaxCount()) return false;
        int old = getMaxCount();
        COUNT.setValue(maxCount);
        return old != getMaxCount();
    }

    public ChatColor getChatColor() {
        return CHAT_COLOR.getValue();
    }

    /**
     * Set the {@link ChatColor}.
     * @param color the color to set
     * @return true if the color is different than the old color
     */
    public boolean setChatColor(ChatColor color) {
        if(color == getChatColor()) return false;
        ChatColor old = getChatColor();
        CHAT_COLOR.setValue(color);
        return old != getChatColor();
    }

    /**
     * Get the merged count for the given entity.<br>
     * If the entity is not the correct type for this {@link EntityMatcherOptions}, this will return -1.
     * @param entity the entity to check
     * @return the current count of the entity, or -1 if the entity is not the correct type. Defaults to 1 if the entity is not merged.
     */
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

    public boolean load(FileConfiguration config) {
        String MOBS_KEY = optionManager.MOBS_SECTION.getPath();
        ConfigurationSection mobSection = config.getConfigurationSection(MOBS_KEY);
        if(mobSection == null) {
            MobMerge.LOG.warning("Expected configuration section at " + MOBS_KEY + ". Found " + config.get(MOBS_KEY).getClass().getName());
            return false;
        }
        ConfigurationSection typeSection = mobSection.getConfigurationSection(this.typeName);
        String path = MOBS_KEY + "." + this.typeName;
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
                    if(typeSection.getIntegerList(RADIUS.getPath()) != null) {
                        List<Integer> intList = typeSection.getIntegerList(RADIUS.getPath());
                        Vector result;
                        switch(intList.size()) {
                            case 0:
                                result = settingsManager.DEFAULT_SECTION.RADIUS.getValue();
                                break;
                            case 1:
                                int x = intList.get(0);
                                result = new Vector(x,x,x);
                                break;
                            case 2:
                                result = new Vector(intList.get(0), intList.get(1), intList.get(0)); // use 'x' for x & z
                                break;
                            case 3:
                            default:
                                result = new Vector(intList.get(0), intList.get(1), intList.get(2));
                        }
                        RADIUS.setValue(result);
                    } else {
                        String numberStr = typeSection.getString(RADIUS.getPath());
                        if(NumberUtils.isNumber(numberStr)) {
                            try {
                                Number num = NumberFormat.getInstance().parse(numberStr);
                                RADIUS.setValue(new Vector(num.doubleValue(), num.doubleValue(), num.doubleValue()));
                            } catch (ParseException e) {
                                if(settingsManager.VERBOSE.getValue()) {
                                    MobMerge.LOG.warning("Failed to import radius for " + typeSection.getCurrentPath() + ". Setting it to default value.");
                                }
                                RADIUS.setValue(settingsManager.DEFAULT_SECTION.RADIUS.getValue());
                            }
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
            if(typeSection.isConfigurationSection(node) && node.equals(SettingsManager.CONDITIONS_KEY)) {
                // condition node
                ConfigurationSection condNode = typeSection.getConfigurationSection(node);
                for(String key : condNode.getKeys(false)) {
                    Condition condition = null;
                    // try to parse key for a valid namespace
                    NamespacedKey nameKey = MobMerge.parseStringToKey(key.replaceAll("\"", "")); // remove double quotes
                    condition = conditionManager.getCondition(nameKey);
                    if(condition == null) {
                        if(settingsManager.VERBOSE.getValue()) {
                            MobMerge.LOG.info("Failed to find condition \'" + key + "\' in section " + condNode.getCurrentPath());
                        }
                        continue;
                    }
                    MobMerge.debug("Found condition \'" + condition.getKey().toString() + "\' with value \'" + condition.getRequiredValue().toString() + "\' for entity " + this.typeName);
                    this.conditions.add(condition.setRequiredValue(condNode.getString(key)));
                }
            }
        }
        MobMerge.debug("Loaded options for " + WordUtils.capitalizeFully(this.typeName.replaceAll("_", " ")));
        return true;
    }

    public void save(FileConfiguration config) {
        String path = optionManager.MOBS_SECTION.getPath() + "." + this.typeName;

        ConfigurationSection section = config.getConfigurationSection(path);

        for(ConfigOption opt : options.values()) {
            if(opt instanceof ChatColorOption) {
                section.set(opt.getPath(), ( (ChatColorOption) opt ).getValue().name().toLowerCase(Locale.ENGLISH));
                continue;
            }
            section.set(opt.getPath(), opt.getValue());
        }

        if(!conditions.isEmpty()) {
            ConfigurationSection condSection = section.getConfigurationSection(SettingsManager.CONDITIONS_KEY);
            if(condSection == null) {
                condSection = section.createSection(SettingsManager.CONDITIONS_KEY);
                MobMerge.debug("Created empty conditions section for " + section.getCurrentPath());
            }
            for(Condition con : this.conditions) {
                if(conditionManager.isDefaultCondition(con)) continue; // skip default conditions
                NamespacedKey conditionKey = conditionManager.getKey(con);
                if(conditionKey == null || conditionKey.toString().isEmpty()) {
                    if(settingsManager.VERBOSE.getValue()) {
                        MobMerge.LOG.warning("Unknown condition \'" + con.getClass().getName() + "\'. Could not find matching NamespacedKey.");
                    }
                    continue;
                }
                // we have to surround namespaced keys with double quotes because yaml is awesome like that!
                if(con instanceof ColorableCondition && con.getRequiredValue() != null) {
                    condSection.set("\"" + conditionKey.toString() + "\"", con.getStringValue());
                } else {
                    condSection.set("\"" + conditionKey.toString() + "\"", con.getRequiredValue());
                }
            }
        }
    }

    /**
     * Test two entities against all the conditions present.
     * @param entity the first entity to test
     * @param other the other entity to test
     * @return
     */
    public boolean test(Entity entity, Entity other) {
        if(entity.getType() != this.getEntityType() || other.getType() != this.getEntityType()) return false; // wrong entities
        for(Condition cond : getConditions()) {
            if(!cond.test(entity, other)) {
                if(settingsManager.VERBOSE.getValue()) {
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
    public Map<String, ConfigOption> getOptions() {
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
                       .filter(condition -> ! conditionManager.isDefaultCondition(condition))
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
        if(conditionManager.isDefaultCondition(condition)) return false;
        return this.conditions.remove(condition);
    }

    /**
     * Get an immutable copy of the conditions for this entity type.<br>
     * These conditions are sorted by their {@link Priority}.
     * @return an immutable copy of the conditions
     */
    public List<Condition> getConditions() {
        return ImmutableList.copyOf(conditionManager.sortConditionsByPriority(this.conditions));
    }
}
