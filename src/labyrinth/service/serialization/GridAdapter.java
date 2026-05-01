package labyrinth.service.serialization;

import com.google.gson.*;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.lang.reflect.Type;

public class GridAdapter implements JsonSerializer<Grid<TileDTO>>, JsonDeserializer<Grid<TileDTO>> {

    @Override
    public JsonElement serialize(Grid<TileDTO> entries, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        for(int i = 0; i < entries.getHeight(); i++) {
            JsonArray jsonRow = new JsonArray();

            for(int j = 0; j < entries.getWidth(); j++) {

                TileDTO tileDTO = entries.get(j, i);
                jsonRow.add(jsonSerializationContext.serialize(tileDTO));

            }

            jsonArray.add(jsonRow);

        }

        return jsonArray;
    }

    @Override
    public Grid<TileDTO> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray jsonGrid = jsonElement.getAsJsonArray();

        int height = jsonGrid.size();
        int width = jsonGrid.get(0).getAsJsonArray().size();

        Grid<TileDTO> grid = new ArrayGrid<>(width, height);

        for(int i = 0; i < height; i++) {
            JsonArray jsonRow = jsonGrid.get(i).getAsJsonArray();

            for(int j = 0; j < width; j++) {
                TileDTO tileDTO = jsonDeserializationContext.deserialize(jsonRow.get(j), TileDTO.class);
                grid.set(j, i, tileDTO);
            }
        }

        return grid;
    }
}
