package labyrinth.model;

import labyrinth.model.strategy.DefaultPlayStrategy;
import labyrinth.model.strategy.Move;
import labyrinth.model.strategy.PlayStrategy;
import labyrinth.model.strategy.StubPlayStrategy;
import labyrinth.service.serialization.GameDTO;
import labyrinth.service.serialization.PlayerDTO;
import labyrinth.util.collection.Grid;
import labyrinth.util.graph.Graph;
import labyrinth.util.graph.MazeToGraphAdapter;
import labyrinth.util.graph.Paths;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Game {
    private SimpleObservableValue<Maze> maze;
    private List<Slot> slots;
    private SimpleObservableValue<GameState> gameState;
    private SimpleObservableValue<Paths<Tile>> paths;
    private SimpleObservableValue<Integer> currentPlayerIndex;
    private SimpleObservableValue<Player> currentPlayer;
    private UUID uuid;

    public static final PlayStrategy STRATEGY = new StubPlayStrategy();

    public Game() {
        this.maze = new SimpleObservableValue<>(null);
        gameState = new SimpleObservableValue<>(GameState.SETUP);
        this.slots = new ArrayList<>(4);
        this.uuid = UUID.randomUUID();

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
        this.uuid = UUID.randomUUID();

        gameState.setValue(GameState.SETUP);
        maze.setValue(null);
        this.currentPlayerIndex.setValue(-1);

        for(Slot slot : slots){
            if(slot.getPlayer() != null) {
                slot.close();
            }
        }

        this.paths.setValue(null);
    }

    public void startTurn() {
        Player current = getCurrentPlayer();

        if (current == null) {
            return;
        }

        if (!current.isBot()) {
            return;
        }

        if (gameState.getValue() != GameState.MAZE_MOVE) {
            return;
        }

        Move move = STRATEGY.getNextMove(new GameDTO(this));

        if (move == null) {
            return;
        }

        rotateExtraTile(move.nbRotations());
        mazeMove(move.mazeMove());

        Tile destination = maze.getValue().getTiles().get(move.destination());
        movePlayer(destination);

        if (gameState.getValue() == GameState.TURN_END) {
            endTurn();
        }
    }

    public void init(){
        if(gameState.getValue() != GameState.SETUP){
            throw new IllegalStateException("Le jeu n'est pas dans SETUP");
        }

        this.maze.setValue(MazeBuilder.buildStandardMaze());
        Random random = new Random();

        System.out.println("=== AVANT CREATION JOUEURS MANQUANTS ===");
        for (Slot slot : slots) {
            Player p = slot.getPlayer();

            System.out.println(
                    "slot " + slot.getIndex()
                            + " joueur=" + (p != null)
                            + " bot=" + (p != null && p.isBot())
            );
        }

        //placer les joueurs sur leurs case de depart
        for (Slot slot : slots) {
            if (slot.getPlayer() == null) {
                slot.open();
            }
        }

        // placer uniquement les joueurs déjà présents sur leurs cases de départ
        for (Slot slot : slots) {
            Player actuel = slot.getPlayer();

            if (actuel != null) {
                Tile start = maze.getValue().getStartingTile(actuel);
                start.addPlayer(actuel);
            }
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

        for (int i = 0; i < slots.size(); i++) {
            Player player = slots.get(i).getPlayer();

            if (player != null) {
                player.getDeck().init(playerTreasures[i]);
            }
        }

        List<Integer> activeIndexes = new ArrayList<>();

        for (Slot slot : slots) {
            if (slot.getPlayer() != null) {
                activeIndexes.add(slot.getIndex());
            }
        }

        if (activeIndexes.isEmpty()) {
            throw new IllegalStateException("Aucun joueur dans la partie");
        }

        int randomIndex = activeIndexes.get(random.nextInt(activeIndexes.size()));

        this.currentPlayerIndex.setValue(randomIndex);
        this.currentPlayer.setValue(slots.get(randomIndex).getPlayer());

        System.out.println("=== AVANT DEBUT PARTIE ===");
        for (Slot slot : slots) {
            Player p = slot.getPlayer();

            System.out.println(
                    "slot " + slot.getIndex()
                            + " joueur=" + (p != null)
                            + " bot=" + (p != null && p.isBot())
            );
        }

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

        Player player = getCurrentPlayer();
        Deck deck = player.getDeck();

        if(tile.getContent() instanceof Treasure treasure && !deck.getDrawPile().isEmpty() && deck.getDrawPile().getFirst() == treasure.id()) {
            deck.discard();
        }

        if(deck.getDrawPile().isEmpty() && tile.getContent() instanceof StartingPosition pos && pos.index() == currentPlayerIndex.getValue()) {
            this.gameState.setValue(GameState.GAME_OVER);
        } else {
            this.gameState.setValue(GameState.TURN_END);
        }
    }

    public void endTurn(){
        if(gameState.getValue() != GameState.TURN_END){
            throw new IllegalStateException("Le jeu n'est pas dans END TURN");
        }

        int index = currentPlayerIndex.getValue();

        do {
            index = (index + 1) % slots.size();
        } while (slots.get(index).getPlayer() == null);

        currentPlayerIndex.setValue(index);
        currentPlayer.setValue(slots.get(index).getPlayer());

        gameState.setValue(GameState.MAZE_MOVE);
        updatePaths();

        startTurn();
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
            return null;
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

    public void updateFrom(GameDTO gameDTO){
        this.uuid = gameDTO.getUuid();
        gameState.setValue(GameState.SETUP);

        if(paths == null){
            paths = new SimpleObservableValue<>(null);
        }

        List<Player> players = new ArrayList<>();

        for(int i = 0; i < slots.size(); i++){
            PlayerDTO playerDTO = gameDTO.getPlayers().get(i);
            Slot slot = slots.get(i);

            if(playerDTO == null){
                if(slot.getPlayer() != null){
                    slot.close();
                }
                players.add(null);
            }else{
                if(slot.getPlayer() == null){
                    slot.open();
                }
                slot.getPlayer().updateFrom(playerDTO);
                players.add(slot.getPlayer());
            }
        }

        maze.setValue(Maze.from(gameDTO.getMaze(), players));

        currentPlayerIndex.setValue(gameDTO.getCurrentPlayerIndex());

        if (gameDTO.getCurrentPlayerIndex() != null && gameDTO.getCurrentPlayerIndex() >= 0) {
            this.currentPlayer.setValue(players.get(gameDTO.getCurrentPlayerIndex()));
        } else {
            this.currentPlayer.setValue(null);
        }

        this.gameState.setValue(gameDTO.getState());

        updatePaths();
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public UUID getUuid() {
        return uuid;
    }
}
