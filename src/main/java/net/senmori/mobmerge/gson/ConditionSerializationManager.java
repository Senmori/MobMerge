package net.senmori.mobmerge.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.gson.serializers.ConditionAdapter;
import net.senmori.mobmerge.gson.strategy.DefaultExclusionStrategy;

/**
 * This class handles the serialization/deserialization of conditions
 */
public final class ConditionSerializationManager {
    private static final ConditionSerializationManager INSTANCE = new ConditionSerializationManager();

    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = null;

    public static ConditionSerializationManager getInstance() {
        return INSTANCE;
    }

    public ConditionSerializationManager() {
    }

    public GsonBuilder getGsonBuilder() {
        return new GsonBuilder().setExclusionStrategies(new DefaultExclusionStrategy())
                .registerTypeAdapter(Condition.class, new ConditionAdapter())
                .setPrettyPrinting()
                .setLenient(); // lenient to allow for comments in the future?
    }

    public Gson getGson() {
        if(gson == null) {
            gson = getGsonBuilder().create();
        }
        return gson;
    }
}
