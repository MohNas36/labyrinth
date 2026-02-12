import labyrinth.model.*;
import labyrinth.util.MazeFormatter;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Grid<Tile> grid = new ArrayGrid<>(3, 2);
        grid.set(0, 0, new Tile(TileKind.ELBOW, new StartingPosition(0), Direction.RIGHT));
        grid.set(1, 0, new Tile(TileKind.CORRIDOR, new None(), Direction.LEFT));
        grid.set(2, 0, new Tile(TileKind.ELBOW, new StartingPosition(1), Direction.DOWN));
        grid.set(0, 1, new Tile(TileKind.T_CROSS, new Treasure(0)));
        grid.set(1, 1, new Tile(TileKind.CORRIDOR, new Treasure(23)));
        grid.set(2, 1, new Tile(TileKind.T_CROSS, new None(), Direction.LEFT));
        Tile tile = new Tile(TileKind.T_CROSS, new None());
        Maze maze = new Maze(grid, tile, Set.of(), Set.of());
        MazeFormatter formatter = new MazeFormatter();
        String s = formatter.Format(maze);
        System.out.println(s);
    }
}
