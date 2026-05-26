package labyrinth.service.serialization;

import labyrinth.model.Maze;
import labyrinth.model.MazeMove;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.util.ArrayList;
import java.util.List;

public class MazeDTO {

    private final List<Integer> fixedRows;
    private final List<Integer> fixedColumns;
    private Grid<TileDTO> tiles;
    private final TileDTO extraTile;
    private final MazeMove forbiddenMove;

    public MazeDTO(Maze maze){
        this.tiles = new ArrayGrid<>(maze.getTiles().getWidth(), maze.getTiles().getHeight());
        fixedColumns = new ArrayList<>(maze.getFixedColumns());
        fixedRows = new ArrayList<>(maze.getFixedRows());

        for(int x = 0; x < maze.getTiles().getWidth() ; x++){
            for(int y = 0 ; y < maze.getTiles().getHeight() ; y++){
                tiles.set(x , y , new TileDTO(maze.getTiles().get(x , y)));
            }
        }

        extraTile = new TileDTO(maze.getExtraTile());
        forbiddenMove = maze.getForbiddenMove();
    }

    public Grid<TileDTO> getTiles() {
        return tiles;
    }

    public MazeMove getForbiddenMove() {
        return forbiddenMove;
    }

    public List<Integer> getFixColumns() {
        return fixedColumns;
    }

    public List<Integer> getFixRows() {
        return fixedRows;
    }

    public TileDTO getExtraTile() {
        return extraTile;
    }
}
