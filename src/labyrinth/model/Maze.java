package labyrinth.model;

import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;

import java.util.Set;

public class Maze {
    private final Grid<Tile> tiles;
    private Set<Integer> fixedRows;
    private Set<Integer> fixedColumns;
    private Tile extraTile;

    public Maze(Grid<Tile> tiles, Tile extraTile, Set<Integer> fixedRows, Set<Integer> fixedColumns) {
        this.tiles = tiles;
        this.fixedRows = fixedRows;
        this.fixedColumns = fixedColumns;
        this.extraTile = extraTile;
    }

    public Grid<Tile> getTiles() {
        return tiles;
    }

    public Position getPosition(Tile tile) {
        for(Grid.Entry<Tile> entry : tiles){
            if(entry.value().equals(tile)){
                return entry.position();
            }
        }
        return null;
    }

    public Set<Integer> getFixedRows() {
        return fixedRows;
    }

    public Set<Integer> getFixedColumns() {
        return fixedColumns;
    }

    public Tile getExtraTile() {
        return extraTile;
    }
}
