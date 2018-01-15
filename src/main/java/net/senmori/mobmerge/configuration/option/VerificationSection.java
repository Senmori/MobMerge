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

/**
 * This section determines which conditions are run on all entities.
 */
public class VerificationSection extends SectionOption {
    private static final BooleanResolver boolResolver = new BooleanResolver();

    private final ConditionManager conditionManager = ConditionManager.getInstance();
    private final Set<Condition> defaultConditions = Sets.newHashSet();

    protected VerificationSection(String key) {
        super(key);
        addOption("Not Leashed Condition", new ConditionOption("not-leashed", true));
        addOption("Not Tamed Condition", new ConditionOption("not-tamed", true));
        addOption("Valid Entity Condition", new ConditionOption("valid-entity", true));
        addOption("Valid Entity Type Condition", new ConditionOption("entity-type", true));
    }

    @Override
    public boolean load(ConfigurationSection section) {
        for(String node : section.getKeys(false)) {

            ConfigOption option = getOptionByPath(node);
            if(option != null && option instanceof ConditionOption) {

                ConditionOption bool = (ConditionOption)option;
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

        // load default conditions now
        for(ConfigOption option : getOptions().values()) {
            if(option instanceof ConditionOption) {
                ConditionOption condOption = (ConditionOption)option;
                Condition condition = conditionManager.getCondition(condOption.getConditionName());
                if(condition == null) {
                    if(MobMerge.getInstance().getSettingsManager().VERBOSE.getValue() || MobMerge.isDebugMode()) {
                        MobMerge.LOG.warning("Failed to find condition with name \'" + condOption.getConditionName() + "\'");
                    }
                    continue;
                }
                if(conditionManager.isDefaultCondition(condition)) {
                    defaultConditions.add(condition);
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
            } else if (option instanceof BooleanOption){
                boolResolver.save(section, option.getPath(), ( (BooleanOption) option ).getValue());
            } else {
                section.set(option.getPath(), option.getValue());
            }
        });
        return true;
    }
}
