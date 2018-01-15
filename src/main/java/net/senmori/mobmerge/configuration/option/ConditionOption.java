package net.senmori.mobmerge.configuration.option;

import net.senmori.senlib.configuration.option.BooleanOption;

public class ConditionOption extends BooleanOption {
    public ConditionOption(String key, Boolean defaultValue) {
        super(key, defaultValue);
    }

    public String getConditionName() {
        return this.getPath();
    }

    public boolean isEnabled() {
        return this.getValue();
    }

    public void setEnabled(boolean value) {
        this.setValue(value);
    }
}
