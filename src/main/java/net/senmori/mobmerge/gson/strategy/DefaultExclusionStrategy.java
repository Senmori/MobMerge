package net.senmori.mobmerge.gson.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import net.senmori.mobmerge.annotation.Excluded;

/**
 * This exclusion strategy is used to prevent the serialization/deserialization of default conditions.<br>
 * This is so users may not alter the core behavior of this plugin.
 */
public class DefaultExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return aClass.isAnnotationPresent(Excluded.class);
    }
}
