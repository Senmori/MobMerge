package net.senmori.mobmerge.gson.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.senmori.mobmerge.MobMerge;
import net.senmori.mobmerge.condition.Condition;
import net.senmori.mobmerge.condition.ConditionManager;
import net.senmori.senlib.gson.JsonTypeAdapter;
import net.senmori.senlib.gson.JsonUtils;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

public class ConditionTypeAdapter extends JsonTypeAdapter<Condition> {
    private static final ConditionManager conditionManager = ConditionManager.getInstance();
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
        Condition condition = conditionManager.getCondition(key);
        if(condition != null) {
            return condition.setRequiredValue(value);
        }
        throw new JsonSyntaxException("Unknown condition \'" + name + "\'");
    }
}
