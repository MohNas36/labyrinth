package labyrinth.service.serialization;

import com.google.gson.*;
import labyrinth.model.Content;
import labyrinth.model.None;
import labyrinth.model.StartingPosition;
import labyrinth.model.Treasure;

import java.lang.reflect.Type;

public class ContentAdapter implements JsonSerializer<Content>, JsonDeserializer<Content> {

    @Override
    public JsonElement serialize(Content content, Type type, JsonSerializationContext context) {
        JsonObject jsonObject;

        if (content instanceof None) {
            jsonObject = context.serialize(content, None.class).getAsJsonObject();
            jsonObject.addProperty("type", "none");
        } else if (content instanceof StartingPosition) {
            jsonObject = context.serialize(content, StartingPosition.class).getAsJsonObject();
            jsonObject.addProperty("type", "startingPosition");
        } else if (content instanceof Treasure) {
            jsonObject = context.serialize(content, Treasure.class).getAsJsonObject();
            jsonObject.addProperty("type", "treasure");
        } else {
            throw new JsonParseException("Content inconnu");
        }

        return jsonObject;
    }

    @Override
    public Content deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return new None();
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement typeElement = jsonObject.get("type");

        if (typeElement != null && !typeElement.isJsonNull()) {
            String contentType = typeElement.getAsString();

            if (contentType.equals("none")) {
                return context.deserialize(jsonObject, None.class);
            } else if (contentType.equals("treasure")) {
                return context.deserialize(jsonObject, Treasure.class);
            } else if (contentType.equals("startingPosition")) {
                return context.deserialize(jsonObject, StartingPosition.class);
            }

            throw new JsonParseException("Type de contenu inconnu : " + contentType);
        }

        // Ancienne sauvegarde sans champ "type"
        if (jsonObject.has("id")) {
            return context.deserialize(jsonObject, Treasure.class);
        }

        if (jsonObject.has("index")) {
            return context.deserialize(jsonObject, StartingPosition.class);
        }

        return new None();
    }
}