package net.senmori.mobmerge.configuration.option;

import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.senlib.configuration.option.SectionOption;
import org.bukkit.configuration.ConfigurationSection;

public class VerificationSection extends SectionOption {

    private final ConditionManager conditionManager = ConditionManager.getInstance();

    protected VerificationSection(String key) {
        super(key);
    }

    @Override
    public boolean load(ConfigurationSection section) {

        for(String node : section.getKeys(false)) {

        }

        return true;
    }

    @Override
    public boolean save(ConfigurationSection section) {
        return true;
    }
}
