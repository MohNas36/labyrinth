package labyrinth.model.strategy;

import labyrinth.model.strategy.simulation.GameSimulation;
import labyrinth.model.strategy.simulation.PlayerSimulation;
import labyrinth.service.serialization.GameDTO;

public class DefaultPlayStrategy implements PlayStrategy {

    private GameSimulation game;
    private PlayerSimulation initialPlayer;

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

}