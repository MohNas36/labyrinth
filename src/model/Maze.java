package model;

import labyrinth.util.collection.Grid;

public class Maze {
    private final Grid<Tile> tiles;

    public Maze(Grid<Tile> tiles) {
        this.tiles = tiles;
    }

    public Grid<Tile> getTiles() {
        return tiles;
    }
}
