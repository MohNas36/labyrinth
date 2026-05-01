package labyrinth.service.serialization;

import com.google.gson.*;
import labyrinth.model.Content;
import labyrinth.model.None;
import labyrinth.model.StartingPosition;
import labyrinth.model.Treasure;

import java.lang.reflect.Type;

public class ContentAdapter implements JsonSerializer<Content>, JsonDeserializer<Content> {
    @Override
    public Content deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String contentType = jsonObject.get("type").getAsString();

        if(contentType.equals("none")) {
            return jsonDeserializationContext.deserialize(jsonElement, None.class);
        }else if(contentType.equals("treasure")) {
            return jsonDeserializationContext.deserialize(jsonElement, Treasure.class);
        }else if(contentType.equals("startingPosition")) {
            return jsonDeserializationContext.deserialize(jsonElement, StartingPosition.class);
        }

        return null;
    }

    @Override
    public JsonElement serialize(Content content, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        if(content instanceof None){
            jsonObject.addProperty("type", "none");
            jsonObject = jsonSerializationContext.serialize(content, None.class).getAsJsonObject();
        }else if(content instanceof StartingPosition){
            jsonObject.addProperty("type", "startingPosition");
            jsonObject = jsonSerializationContext.serialize(content, StartingPosition.class).getAsJsonObject();
        }else if(content instanceof Treasure){
            jsonObject.addProperty("type", "treasure");
            jsonObject = jsonSerializationContext.serialize(content, Treasure.class).getAsJsonObject();
        }

        return jsonObject;
    }
}
