package labyrinth.model;

import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.util.HashSet;
import java.util.Set;

public class MazeBuilder {
    private ArrayGrid<Tile> tiles;
    private Set<Tile> freeTiles;
    private final Set<Integer> fixRows;
    private final Set<Integer> fixCols;

    public MazeBuilder(int width, int height) {
        this.tiles = new ArrayGrid<Tile>(width, height);
        this.freeTiles = new HashSet<Tile>();
        this.fixCols = new HashSet<Integer>();
        this.fixRows = new HashSet<Integer>();
    }

    public MazeBuilder addFixedTile(int x, int y, Tile tile) {
        this.tiles.set(x, y, tile);
        this.fixRows.add(x);
        this.fixCols.add(y);
        return this;
    }

    public MazeBuilder addFreeTile(Tile tile) {
        this.freeTiles.add(tile);
        return this;
    }

    public Maze build(){

        Maze m = new Maze(this.tiles, null, this.fixRows, this.fixCols);
        return m;
    }
}
