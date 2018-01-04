package net.senmori.mobmerge.gson;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.type.MatchDyeColorCondition;
import net.senmori.mobmerge.gson.util.InheritanceAdapter;
import org.apache.commons.lang3.Validate;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;
import java.util.Map;

public final class ConditionSerializationManager {
    private static final ConditionSerializationManager INSTANCE = new ConditionSerializationManager();

    private final Map<NamespacedKey, Condition.Serializer> nameToSerializerMap = Maps.newHashMap();
    private final Map<Class<? extends Condition>, Condition.Serializer> classToSerializerMap = Maps.newHashMap();

    public static ConditionSerializationManager getInstance() {
        return INSTANCE;
    }

    public ConditionSerializationManager() {
        registerSerializer(new MatchDyeColorCondition.Serializer());
    }

    public <T extends Condition> void registerSerializer(Condition.Serializer<? extends T> serializer) {
        Validate.isTrue(!nameToSerializerMap.containsKey(serializer.getName()), "Can\'t re-register duplicate Serializer \'" + serializer.getName() + "\'");
        Validate.isTrue(!classToSerializerMap.containsKey(serializer.getConditionClass()), "Can\'t re-register duplicate condition class \'" + serializer.getConditionClass().getName() + "\'");

        nameToSerializerMap.put(serializer.getName(), serializer);
        classToSerializerMap.put(serializer.getConditionClass(), serializer);
    }

    public Condition.Serializer<?> getSerializerForName(NamespacedKey name) {
        Condition.Serializer serializer = nameToSerializerMap.get(name);
        Validate.notNull(serializer, "Unknown condition \'" + name + "\'");
        return serializer;
    }

    public <T extends Condition> Condition.Serializer<T> getSerializerFor(T conditionClass) {
        Condition.Serializer<T> serializer = classToSerializerMap.get(conditionClass);
        Validate.notNull(serializer, "Unknown condition class \'" + conditionClass.getClass().getName() + "\'");
        return serializer;
    }

    public static class Serializer extends InheritanceAdapter<Condition> {
        public Serializer() {}

        @Override
        public JsonElement serialize(Condition condition, Type type, JsonSerializationContext context) {
            Condition.Serializer serializer = ConditionSerializationManager.getInstance().getSerializerFor(condition);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("condition", serializer.getName().toString());
            serializer.serialize(jsonObject, condition, context);
            return jsonObject;
        }

        @Override
        public Condition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "condition");
            String name = JsonUtils.getString(json, "condition");
            NamespacedKey key = MobMerge.parseStringToKey(name);
            Condition.Serializer serializer;
            try {
                serializer = ConditionSerializationManager.getInstance().getSerializerForName(key);
            } catch(IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown condition \'" + key + "\'");
            }
            return serializer.deserialize(json, context);
        }
    }
}
