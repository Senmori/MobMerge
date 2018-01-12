package net.senmori.mobmerge.gson;

import com.google.gson.GsonBuilder;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.gson.adapter.ConditionTypeAdapter;
import net.senmori.senlib.gson.strategy.DefaultExclusionStrategy;

public final class MobMergeGson {

    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .serializeNulls()
                .setExclusionStrategies(new DefaultExclusionStrategy())
                .registerTypeAdapter(Condition.class, new ConditionTypeAdapter());
    }
}
