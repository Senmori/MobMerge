package net.senmori.mobmerge.configuration.option;

import com.google.common.collect.Sets;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.SectionOption;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

public class ConditionSection extends SectionOption {

    private ConditionManager conditionManager = ConditionManager.getInstance();

    private Set<Condition> enabledConditions = Sets.newHashSet(); // all the enabled conditions
    private Set<Condition> totalConditions = Sets.newHashSet(); // all conditions in the 'conditions' section
    public ConditionSection(String key) {
        super(key);

        addOption("Slime Size", new BooleanOption("slime-size", true));
        addOption("Horse Color", new BooleanOption("horse-color", true));
        addOption("Zombie Villager Profession", new BooleanOption("zombie-villager-profession", true));
        addOption("Llama Color", new BooleanOption("llama-color", true));
        addOption("Parrot Color", new BooleanOption("parrot-color", true));
    }

    @Override
    public boolean load(ConfigurationSection section) {
        boolean foundAllConditions = true;
        for(String node : section.getKeys(false)) {

        }
        return foundAllConditions;
    }

    @Override
    public boolean save(ConfigurationSection section) {
        boolean noErrors = true;
        return noErrors;
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
