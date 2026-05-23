package labyrinth.util.graph;

import labyrinth.model.Direction;
import labyrinth.model.Maze;
import labyrinth.model.Tile;
import labyrinth.util.collection.Grid;

public class MazeToGraphAdapter {

    public static Graph<Tile> getGraph(Maze maze) {
        Graph<Tile> graph = new Graph<>();
        Grid<Tile> grid = maze.getTiles();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile current = grid.get(x, y);
                graph.addNode(current);

                for (Direction direction : current.getExits()) {
                    int nextX = x;
                    int nextY = y;

                    if (direction == Direction.UP) {
                        nextY = y - 1;
                    } else if (direction == Direction.DOWN) {
                        nextY = y + 1;
                    } else if (direction == Direction.LEFT) {
                        nextX = x - 1;
                    } else if (direction == Direction.RIGHT) {
                        nextX = x + 1;
                    }

                    if (nextX >= 0 && nextX < grid.getWidth()
                            && nextY >= 0 && nextY < grid.getHeight()) {

                        Tile next = grid.get(nextX, nextY);

                        if (next.getExits().contains(direction.getOpposite())) {
                            graph.addEdge(current, next);
                        }
                    }
                }
            }
        }

        return graph;
    }
}
