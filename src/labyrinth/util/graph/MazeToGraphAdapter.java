package labyrinth.util.graph;

import labyrinth.model.Direction;
import labyrinth.model.Maze;
import labyrinth.model.Tile;
import labyrinth.util.collection.Grid;

public class MazeToGraphAdapter {

    public static Graph<Tile> getGraph(Maze maze) {
        Graph<Tile> graph = new Graph<>();
        Grid<Tile> grid = maze.getTiles();

        for(int i = 0; i < grid.getHeight(); i++){
            for(int j = 0; j < grid.getWidth(); j++){
                Tile current = grid.get(i, j);
                graph.addNode(current);

                for(Direction d : current.getExits()){
                    int dx = i;
                    int dy = j;
                    if(d == Direction.UP){
                        dx = i-1;
                    }
                    if(d == Direction.DOWN){
                        dx = i+1;
                    }
                    if(d == Direction.LEFT){
                        dy = j-1;
                    }
                    if(d == Direction.RIGHT){
                        dy = j+1;
                    }
                    if(dx < grid.getHeight() && dy < grid.getWidth() && dx >= 0 && dy >= 0){
                        Tile next = grid.get(dx, dy);
                        if(next.getExits().contains(d.getOpposite())){
                            graph.addEdge(current, next);
                        }
                    }
                }
            }
        }
        return graph;
    }

}
