package net.senmori.mobmerge.configuration.option;

import com.google.common.collect.Sets;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.configuration.resolver.BooleanResolver;
import net.senmori.senlib.configuration.ConfigOption;
import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.SectionOption;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

public class ConditionSection extends SectionOption {
    private static final BooleanResolver boolResolver = new BooleanResolver();

    private ConditionManager conditionManager = ConditionManager.getInstance();

    private Set<Condition> enabledConditions = Sets.newHashSet(); // all the enabled conditions
    private Set<Condition> totalConditions = Sets.newHashSet(); // all conditions in the 'conditions' section
    public ConditionSection(String key) {
        super(key);

        addOption("Slime Size", new ConditionOption("slime-size", true));
        addOption("Horse Color", new ConditionOption("horse-color", true));
        addOption("Zombie Villager Profession", new ConditionOption("zombie-villager-profession", true));
        addOption("Llama Color", new ConditionOption("llama-color", true));
        addOption("Parrot Color", new ConditionOption("parrot-color", true));
    }

    @Override
    public boolean load(ConfigurationSection section) {
        for(String node : section.getKeys(false)) {

            ConfigOption option = getOptionByPath(node);

            if(option != null && option instanceof ConditionOption) {
                ConditionOption bool = (ConditionOption)option;
                MobMerge.debug("Condition Option: " + bool.getPath() + " -> " + bool.getValue());
                Boolean result = boolResolver.resolve(section, node);

                if(result == null) {
                    if(MobMerge.getInstance().getSettingsManager().VERBOSE.getValue()) {
                        MobMerge.LOG.warning("Failed to load \'" + this.getPath() + "\' option: \'" + option.getPath() + "\'. Expected true/false. Got \'" + section.getString(node) + "\'");
                    }
                    continue;
                }
                bool.setValue(result);
            }
        }

        // load all conditions
        for(ConfigOption option : getOptions().values()) {
            if(option instanceof ConditionOption) {
                ConditionOption condOption = (ConditionOption)option;

                boolean enabled = condOption.isEnabled();

                Condition condition = conditionManager.getCondition(condOption.getConditionName());
                if(enabled && condition != null && !conditionManager.isDefaultCondition(condition)) {
                    enabledConditions.add(condition);
                }

                if(condition != null) {
                    totalConditions.add(condition);
                }
            }
        }
        return true;
    }

    @Override
    public boolean save(ConfigurationSection section) {
        getOptions().values().forEach(option -> {
            if(option.hasResolver()) {
                option.getResolver().save(section, option.getPath(), option.getValue());
            } else if (option instanceof BooleanOption) {
                boolResolver.save(section, option.getPath(), ( (BooleanOption) option ).getValue());
            } else {
                section.set(option.getPath(), option.getValue());
            }
        });
        return true;
    }

    /**
     * Get all the enabled conditions.
     */
    public Set<Condition> getEnabledConditions() {
        return Sets.newHashSet(enabledConditions);
    }

    /**
     * Get all the conditions listed in the 'conditions' section; regardless of if they are enabled or not
     */
    public Set<Condition> getAllConditions() {
        return Sets.newHashSet(totalConditions);
    }
}
