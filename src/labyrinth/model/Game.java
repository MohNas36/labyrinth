package labyrinth.model;

import labyrinth.util.collection.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private Maze maze;
    private List<Slot> slots;
    private GameState gameState;
    private int currentPlayerIndex;

    public Game() {
        this.maze = MazeBuilder.buildStandardMaze();
        this.slots = new ArrayList<>(4);
        for(int i = 0; i < 4; i++) {
            slots.add(new Slot(this, i));
        }
    }

    public Slot getSlot(int index) {
        if(gameState == GameState.SETUP){
            return null;
        }

        for(Slot slot : slots) {
            if(slot.getIndex() == index){
                return slot;
            }
        }
        return null;
    }

    public Maze getMaze() {
        if(gameState == GameState.SETUP){
            return null;
        }
        return maze;
    }

    public Tile getTile(Player player) {
        if(gameState == GameState.SETUP){
            return null;
        }
        return maze.getTile(player);
    }

    public GameState getGameState(GameState gameState){
        return gameState;
    }

    public Player getCurrentPlayer(){
        return getSlot(currentPlayerIndex).getPlayer();
    }

    public void reset(){
        this.gameState = GameState.SETUP;
        this.maze = null;
        this.currentPlayerIndex = -1;

        for(Slot slot : slots){
            if(slot.getPlayer() != null) {
                slot.close();
            }
        }
    }

    public void init(){
        if(gameState == GameState.SETUP) {
            this.maze = MazeBuilder.buildStandardMaze();
            Random random = new Random();
            this.currentPlayerIndex = random.nextInt(0, 4);

            //placer les joueurs sur leurs case de depart
            for(int i = 0 ; i < 4 ; i++ ){
                Player player = this.getSlot(i).getPlayer();

                for(Tile tile : this.maze.getTiles()){ // demander le prof + gérer les decks
                    if(tile.getContent() instanceof StartingPosition sp && sp.index() == i){
                        tile.addPlayer(player);
                    }
                }
            }
        }
        this.gameState = GameState.MAZE_MOVE;
    }

    public void RotateExtraTile(int nbTimes){
        if(gameState == GameState.MAZE_MOVE) {
            maze.getExtraTile().getOrientation().getRotated(nbTimes);
        }
    }

    public void movePlayer(Tile tile){
        if(gameState == GameState.PLAYER_MOVE){
            Tile tileJoueur = maze.getTile(getCurrentPlayer());
            tileJoueur.removePlayer(getCurrentPlayer());
            tile.addPlayer(getCurrentPlayer());

            if(!getCurrentPlayer().getDeck().getDrawPile().isEmpty() && tile.getContent() instanceof StartingPosition pos && pos.index() == currentPlayerIndex){
                this.gameState = GameState.GAME_OVER;
            }else{
                this.gameState = GameState.TURN_END;
            }
        }
    }

    public void endTurn(){
        currentPlayerIndex = (this.currentPlayerIndex + 1) % 4;
        gameState = GameState.MAZE_MOVE;
    }

    // demander le prof pour mazeMove
}
