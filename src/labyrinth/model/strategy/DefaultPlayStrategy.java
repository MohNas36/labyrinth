package labyrinth.model.strategy;

import labyrinth.model.MazeMove;
import labyrinth.model.strategy.simulation.GameSimulation;
import labyrinth.model.strategy.simulation.PlayerSimulation;
import labyrinth.model.strategy.simulation.TileSimulation;
import labyrinth.service.serialization.GameDTO;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultPlayStrategy implements PlayStrategy {

    private GameSimulation game;
    private PlayerSimulation initialPlayer;

    private final Random random = new Random();

    @Override
    public Move getNextMove(GameDTO snapshot) {
        game = new GameSimulation(snapshot);
        initialPlayer = game.getCurrentPlayer();
        return computeNextMove();
    }

    private Move computeNextMove() {
        if (game.hasWon(initialPlayer)) {
            return null;
        }

        // TODO
        return null;
    }

    private int getScore(){
        if(game.hasWon(initialPlayer)){
            return 10000;
        }

        if(game.isGameOver()){
            return -10000;
        }

        int nbTreasuresFound = initialPlayer.getDiscardPile().size();
        int distance = game.getDistanceToTarget(initialPlayer);

        return 100 * nbTreasuresFound - distance;
    }

    private List<ScoredMove> computeMoves(){
        List<ScoredMove> moves = new ArrayList<>();

        for(MazeMove mazeMove : game.getValidMazeMoves()) {
            for (int nbRotations = 0; nbRotations < 4; nbRotations++) {

                game.modifyMaze(nbRotations, mazeMove);

                Paths<TileSimulation> paths = game.computePaths();

                for (Grid.Entry<TileSimulation> entry : game.getMaze().getTiles()) {
                    TileSimulation destinationTile = entry.value();

                    if(paths.isAccessible(destinationTile)) {
                        Position destinationPosition = destinationTile.getPosition();

                        Move move = new Move(nbRotations, mazeMove, destinationPosition);

                        game.movePlayer(destinationTile);

                        int score = getScore();
                        moves.add(new ScoredMove(move, score));

                        game.undoLastPlayerMove();
                    }
                }

                game.undoLastMazeModification();

            }
        }

        return moves;

    }

    private ScoredMove getRandomMove() {
        List<MazeMove> mazeMoves = game.getValidMazeMoves();

        if (mazeMoves.isEmpty()) {
            return null;
        }

        MazeMove mazeMove = mazeMoves.get(random.nextInt(mazeMoves.size()));
        int nbRotations = random.nextInt(4);

        game.modifyMaze(nbRotations, mazeMove);

        Paths<TileSimulation> paths = game.computePaths();
        PlayerSimulation currentPlayer = game.getCurrentPlayer();

        List<TileSimulation> bestDestinations = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;

        for (Grid.Entry<TileSimulation> entry : game.getMaze().getTiles()) {
            TileSimulation tile = entry.value();

            if (paths.isAccessible(tile)) {
                int distance = game.getDistanceToTarget(tile.getPosition(), currentPlayer);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestDestinations.clear();
                    bestDestinations.add(tile);
                } else if (distance == bestDistance) {
                    bestDestinations.add(tile);
                }
            }
        }

        if (bestDestinations.isEmpty()) {
            game.undoLastMazeModification();
            return null;
        }

        TileSimulation destinationTile = bestDestinations.get(random.nextInt(bestDestinations.size()));
        Move move = new Move(nbRotations, mazeMove, destinationTile.getPosition());

        game.movePlayer(destinationTile);

        int score = getScore();

        game.undoLastMove();

        return new ScoredMove(move, score);
    }

}