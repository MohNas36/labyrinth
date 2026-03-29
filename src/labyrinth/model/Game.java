package labyrinth.model;

import labyrinth.util.collection.Grid;
import labyrinth.util.graph.Graph;
import labyrinth.util.graph.MazeToGraphAdapter;
import labyrinth.util.graph.Paths;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private SimpleObservableValue<Maze> maze;
    private List<Slot> slots;
    private SimpleObservableValue<GameState> gameState;
    private SimpleObservableValue<Paths<Tile>> paths;
    private SimpleObservableValue<Integer> currentPlayerIndex;
    private SimpleObservableValue<Player> currentPlayer;

    public Game() {
        this.maze = new SimpleObservableValue<>(null);
        gameState = new SimpleObservableValue<>(GameState.SETUP);
        this.slots = new ArrayList<>(4);
        for(int i = 0; i < 4; i++) {
            slots.add(new Slot(this, i));
        }
        currentPlayerIndex = new SimpleObservableValue<>(0);
        currentPlayer = new SimpleObservableValue<>(new Player(currentPlayerIndex.getValue()));
        paths = new SimpleObservableValue<Paths<Tile>>(null);
    }

    public Slot getSlot(int index) {
        if(gameState.getValue() == GameState.SETUP){
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
        if(gameState.getValue() == GameState.SETUP){
            return null;
        }
        return maze.getValue();
    }

    public Tile getTile(Player player) {
        if(gameState.getValue() == GameState.SETUP){
            return null;
        }
        return maze.getValue().getTile(player);
    }

    public GameState getGameState(){
        return gameState.getValue();
    }

    public Player getCurrentPlayer(){
        if(gameState.getValue() == GameState.SETUP){
            return null;
        }
        return currentPlayer.getValue();
    }

    public void reset(){
        gameState.setValue(GameState.SETUP);
        maze.setValue(null);
        this.currentPlayerIndex.setValue(-1);

        for(Slot slot : slots){
            if(slot.getPlayer() != null) {
                slot.close();
            }
        }
        this.paths = null;
    }

    public void init(){
        if(gameState.getValue() != GameState.SETUP){
            throw new IllegalStateException("Le jeu n'est pas dans SETUP");
        }

        this.maze.setValue(MazeBuilder.buildStandardMaze());
        Random random = new Random();

        //placer les joueurs sur leurs case de depart
        for(Slot slot : slots){
            slot.open();
        }

        for(Slot slot : slots ){
           Player actuel = slot.getPlayer();
           Tile start = maze.getValue().getStartingTile(actuel);
           start.addPlayer(actuel);
        }

        List<Integer> treasures = new ArrayList<>();
        for(Grid.Entry<Tile> entry : maze.getValue().getTiles()){
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

        this.currentPlayerIndex.setValue(random.nextInt(slots.size()));
        this.currentPlayer.setValue(slots.get(currentPlayerIndex.getValue()).getPlayer());

        this.gameState.setValue(GameState.MAZE_MOVE);
        updatePaths();
    }

    public void rotateExtraTile(int nbTimes){
        if(gameState.getValue() != GameState.MAZE_MOVE){
            throw new IllegalStateException("Le jeu n'est pas dans Maze Move");
        }
        maze.getValue().getExtraTile().rotate(nbTimes);
    }

    public void movePlayer(Tile tile){
        if(gameState.getValue() != GameState.PLAYER_MOVE){
            throw new IllegalStateException("Le jeu n'est pas dans Player Move");
        }

        if(paths == null || paths.getValue() == null || tile == null || !paths.getValue().isAccessible(tile)){
            throw new IllegalStateException("La tuile selectionne n'est pas accessible");
        }

        Tile tileJoueur = maze.getValue().getTile(getCurrentPlayer());
        tileJoueur.removePlayer(getCurrentPlayer());
        tile.addPlayer(getCurrentPlayer());
        if(getCurrentPlayer().getDeck().getDrawPile().isEmpty() && tile.getContent() instanceof StartingPosition pos && pos.index() == currentPlayerIndex.getValue()){
            this.gameState.setValue(GameState.GAME_OVER);
        }else{
            this.gameState.setValue(GameState.TURN_END);
        }

    }

    public void endTurn(){
        if(gameState.getValue() != GameState.TURN_END){
            throw new IllegalStateException("Le jeu n'est pas dans END TURN");
        }

        currentPlayerIndex.setValue((this.currentPlayerIndex.getValue() + 1) % 4);
        currentPlayer.setValue(slots.get(currentPlayerIndex.getValue()).getPlayer());
        gameState.setValue(GameState.MAZE_MOVE);
        updatePaths();
    }

    public void mazeMove(MazeMove mazeMove){
        if(gameState.getValue() != GameState.MAZE_MOVE){
            throw new IllegalStateException("Le jeu n'est pas dans Maze Move");
        }

        maze.getValue().move(mazeMove);
        gameState.setValue(GameState.PLAYER_MOVE);
        updatePaths();
    }

    private void updatePaths(){
        if(maze == null || maze.getValue() == null || currentPlayerIndex.getValue() == -1 || getCurrentPlayer() == null){
            paths.setValue(null);
            return;
        }

        Graph<Tile> graph = new MazeToGraphAdapter().getGraph(maze.getValue());
        Tile start = maze.getValue().getTile(getCurrentPlayer());

        /*if(start == null){
            paths.setValue(null);
            return;
        }*/

        paths.setValue(new Paths<>(graph, start));
    }

    public Paths<Tile> getPaths(){
        if(gameState.getValue() == GameState.SETUP){
            paths = null;
        }
        return paths.getValue();
    }

    public ObservableValue<GameState> stateProperty(){
        return gameState.asReadOnly();
    }

    public GameState getState(){
        return gameState.getValue();
    }

    public ObservableValue<Player> currentPlayerProperty(){
        return currentPlayer.asReadOnly();
    }
}
