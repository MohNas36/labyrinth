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
        this.maze = null;
        gameState = GameState.SETUP;
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

    public GameState getGameState(){
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
        if(gameState != GameState.SETUP){
            throw new IllegalStateException("Le jeu n'est pas dans SETUP");
        }

        this.maze = MazeBuilder.buildStandardMaze();
        Random random = new Random();

        //placer les joueurs sur leurs case de depart
        for(Slot slot : slots){
            slot.open();
        }

        for(Slot slot : slots ){
           Player actuel = slot.getPlayer();
           Tile start = maze.getStartingTile(actuel);
           start.addPlayer(actuel);
        }

        List<Integer> treasures = new ArrayList<>();
        for(Grid.Entry<Tile> entry : maze.getTiles()){
            Content content = entry.value().getContent();
            if(content instanceof Treasure tr){
                treasures.add(tr.id());
            }
        }

        List<Integer>[] playerTreasures = new ArrayList[slots.size()];
        // on crée une liste pour chaque joueur
        for(int i = 0 ; i < slots.size() ; i++){
            playerTreasures[i] = new ArrayList<>();
        }

        for(int i = 0 ; i < treasures.size() ; i++){
            playerTreasures[i % slots.size()].add(treasures.get(i)); // le % sert a répartir les trseors en tournant entrre les joueurs
        }

        for(int i = 0 ; i <slots.size() ; i++){
            slots.get(i).getPlayer().getDeck().init(playerTreasures[i]); // on initialise chaque deck de joueur
        }

        this.currentPlayerIndex = random.nextInt(slots.size());

        this.gameState = GameState.MAZE_MOVE;
    }

    public void RotateExtraTile(int nbTimes){
        if(gameState != GameState.MAZE_MOVE){
            throw new IllegalStateException("Le jeu n'est pas dans Maze Move");
        }
        if(gameState == GameState.MAZE_MOVE) {
            maze.getExtraTile().getOrientation().getRotated(nbTimes);
        }
    }

    public void movePlayer(Tile tile){
        if(gameState != GameState.PLAYER_MOVE){
            throw new IllegalStateException("Le jeu n'est pas dans Player Move");
        }

        Tile tileJoueur = maze.getTile(getCurrentPlayer());
        tileJoueur.removePlayer(getCurrentPlayer());
        tile.addPlayer(getCurrentPlayer());
        if(getCurrentPlayer().getDeck().getDrawPile().isEmpty() && tile.getContent() instanceof StartingPosition pos && pos.index() == currentPlayerIndex){
            this.gameState = GameState.GAME_OVER;
        }else{
            this.gameState = GameState.TURN_END;
        }

    }

    public void endTurn(){
        if(gameState != GameState.TURN_END){
            throw new IllegalStateException("Le jeu n'est pas dans END TURN");
        }

        currentPlayerIndex = (this.currentPlayerIndex + 1) % 4;
        gameState = GameState.MAZE_MOVE;
    }

    public void mazeMove(MazeMove mazeMove){
        if(gameState != GameState.MAZE_MOVE){
            throw new IllegalStateException("Le jeu n'est pas dans Maze Move");
        }

        maze.move(mazeMove);
        gameState = GameState.PLAYER_MOVE;
    }
}
