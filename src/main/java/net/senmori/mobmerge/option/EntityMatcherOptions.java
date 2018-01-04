package net.senmori.mobmerge.option;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.ConfigManager;
import net.senmori.mobmerge.configuration.option.ConfigOption;
import net.senmori.mobmerge.configuration.option.ConfigurationKey;
import net.senmori.mobmerge.configuration.option.types.ChatColorOption;
import net.senmori.mobmerge.configuration.option.types.NumberOption;
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
import java.util.function.BiPredicate;

public class EntityMatcherOptions implements BiPredicate<Entity, Entity> {
    private final EntityType entityType;
    private final String typeName;
    private final Map<ConfigurationKey, ConfigOption> options = Maps.newHashMap();
    private final ConfigManager configManager;
    private final EntityOptionManager optionManager;

    // option keys
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
    }

    public Vector getRadius() {
        return RADIUS.getValue();
    }

    public int getMaxCount() {
        return COUNT.getValue().intValue() < 0 ? Integer.MAX_VALUE : COUNT.getValue().intValue();
    }

    public ChatColor getChatColor() {
        return CHAT_COLOR.getValue();
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
                    NamespacedKey nameKey = MobMerge.newKey(key);
                    MobMerge.debug("Found key in conditions " + key);
                    condition = ConditionManager.getCondition(nameKey);
                    if(condition == null) {
                        if(ConfigManager.VERBOSE.getValue()) {
                            MobMerge.LOG.info("Failed to find condition \'" + key + "\' in section " + condNode.getCurrentPath());
                        }
                        continue;
                    }
                    MobMerge.debug("Found condition \'" + condition.getKey().toString() + "\' with value \'" + condition.getRequiredValue().toString() + "\' for entity " + this.typeName);
                    condition = condition.setRequiredValue(condNode.getString(key)); // adjust condition as needed
                    this.conditions.add(condition);
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
                condSection.set(conditionKey.getKey(), con.getRequiredValue().toString().toLowerCase());
            }
        }
    }

    @Override
    public boolean test(Entity entity, Entity other) {
        for(Condition defaultCondition : ConditionManager.getDefaultConditions()) {
            if(!defaultCondition.test(entity, other)) {
                return false;
            }
        }
       for(Condition condition : getConditions()) {
           if(!condition.test(entity, other)) {
               return false;
           }
       }
       return true;
    }


    public Map<ConfigurationKey, ConfigOption> getOptions() {
        return ImmutableMap.copyOf(this.options);
    }

    public List<Condition> getConditions() {
        return ImmutableList.copyOf(this.conditions);
    }
}
