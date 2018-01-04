package net.senmori.mobmerge.gson.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.mobmerge.gson.JsonUtils;
import net.senmori.mobmerge.gson.util.JsonTypeAdapter;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

public class ConditionAdapter extends JsonTypeAdapter<Condition> {
    @Override
    public JsonElement serialize(Condition condition, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("name", condition.getKey().toString());
        json.addProperty("value", condition.getStringValue());
        return json;
    }

    @Override
    public Condition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String name = JsonUtils.getString(jsonElement, "name");
        String value = JsonUtils.getString(jsonElement, "value");
        NamespacedKey key = MobMerge.parseStringToKey(name);
        Class<? extends Condition> conditionClass = ConditionManager.getConditionClass(key);
        if(conditionClass != null) {
            try {
                Condition condition = conditionClass.newInstance();
                condition.setRequiredValue(value);
                return condition;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new JsonSyntaxException("Unknown condition \'" + name + "\'");
        }
        return null;
    }
}
