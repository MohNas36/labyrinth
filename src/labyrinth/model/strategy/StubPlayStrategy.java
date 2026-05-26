package labyrinth.model.strategy;

import labyrinth.model.MazeMove;
import labyrinth.model.strategy.simulation.GameSimulation;
import labyrinth.model.strategy.simulation.TileSimulation;
import labyrinth.service.serialization.GameDTO;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.Paths;

public class StubPlayStrategy implements PlayStrategy {
    @Override
    public Move getNextMove(GameDTO game) {
        GameSimulation gameSimulation = new GameSimulation(game);

        MazeMove mazeMove = gameSimulation.getValidMazeMoves().getFirst();

        gameSimulation.modifyMaze(0, mazeMove);

        TileSimulation currentTile = gameSimulation.getCurrentPlayerTile();
        Paths<TileSimulation> paths = gameSimulation.computePaths();

        Position destination = currentTile.getPosition();

        for (Grid.Entry<TileSimulation> entry : gameSimulation.getMaze().getTiles()) {
            TileSimulation tile = entry.value();
            if(paths.isAccessible(tile)){
                destination = tile.getPosition();
                break;
            }
        }

        return new Move(0, mazeMove, destination);
    }
}
