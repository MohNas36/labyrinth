package labyrinth.model;

import labyrinth.service.serialization.MazeDTO;
import labyrinth.service.serialization.TileDTO;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Maze {
    private  Grid<SimpleObservableValue<Tile>> tiles;
    private Set<Integer> fixedRows;
    private Set<Integer> fixedColumns;
    private SimpleObservableValue<Tile> extraTile;
    private SimpleObservableValue<MazeMove> forbiddenMove = new SimpleObservableValue<>(null);


    public Maze(Grid<Tile> tiles, Tile extraTile, Set<Integer> fixedRows, Set<Integer> fixedColumns) {
        if(tiles == null || extraTile == null || fixedRows == null || fixedColumns == null) {
            throw new NullPointerException("Au moins un des parametres du labyrinthe est null");
        }

        for(int x = 0; x < tiles.getHeight(); x++){
            for(int y = 0; y < tiles.getWidth(); y++){
                if(tiles.get(x, y) == null){
                    throw new NullPointerException("La grille n'est pas complete");
                }
            }
        }

        for(int i = 0; i < tiles.getHeight(); i++){
            for(int j = 0; j < tiles.getWidth(); j++){
                Tile t1 = tiles.get(i, j);

                for(int k = i + 1; k < tiles.getWidth(); k++){
                    for(int l = j + 1; l < tiles.getHeight(); l++){
                        Tile t2 = tiles.get(k, l);
                        if(t1.equals(t2)){
                            throw new IllegalArgumentException("Il existe une tuile identique dans la grille");
                        }
                    }
                }
            }
        }

        ArrayList<Treasure> treasures = new ArrayList<>();
        ArrayList<StartingPosition> s_positions = new ArrayList<>();

        for(int i = 0; i < tiles.getHeight(); i++){
            for(int j = 0; j < tiles.getWidth(); j++){
                Tile t1 = tiles.get(i, j);
                Content c = t1.getContent();

                if(c instanceof Treasure tr){
                    treasures.add(tr);
                }

                if(c instanceof StartingPosition s_pos){
                    s_positions.add(s_pos);
                }
            }
        }

        for(int i = 0; i < treasures.size(); i++){
            Treasure t1 = treasures.get(i);
            for(int k = i + 1; k < treasures.size(); k++){
                Treasure t2 = treasures.get(k);
                if(t1.id() == t2.id()){
                    throw new IllegalArgumentException("Il y a un tresor sur plusieurs tuiles");
                }
            }
        }

        for(int i = 0; i < s_positions.size(); i++){
            StartingPosition s_pos = s_positions.get(i);
            for(int k = i + 1; k < s_positions.size(); k++){
                StartingPosition s_pos2 = s_positions.get(k);
                if(s_pos.index() == s_pos2.index()){
                    throw new IllegalArgumentException("Il y a une StartingPosition sur plusieurs tuiles");
                }
            }
        }


        this.tiles = tiles.map(SimpleObservableValue :: new);
        this.fixedRows = fixedRows;
        this.fixedColumns = fixedColumns;
        this.extraTile = new SimpleObservableValue<>(extraTile);
    }

    public Grid<ObservableValue<Tile>> getObservableTiles(){
        return tiles.map(SimpleObservableValue :: asReadOnly);
    }

    public Grid<Tile> getTiles() {
        return tiles.map(SimpleObservableValue :: getValue );
    }

    public Position getPosition(Tile tile) {
        for(Grid.Entry<SimpleObservableValue<Tile>> entry : tiles){
            if(entry.value().getValue().equals(tile)){
                return entry.position();
            }
        }
        return null;
    }

    public Set<Integer> getFixedRows() {
        return fixedRows;
    }

    public Set<Integer> getFixedColumns() {
        return fixedColumns;
    }

    public ObservableValue<Tile> extraTileProperty(){
        return extraTile.asReadOnly();
    }

    public Tile getExtraTile() {
        return extraTile.getValue();
    }

    public Tile getTile(Player p){
        for(Grid.Entry<SimpleObservableValue<Tile>> entry : tiles){
            if(entry.value().getValue().getPlayers().contains(p)){
                return entry.value().getValue();
            }
        }
        return null;
    }

    public Tile getStartingTile(Player p){
        for(Grid.Entry<SimpleObservableValue<Tile>> entry : tiles){
            if(entry.value().getValue().getContent() instanceof StartingPosition s && (p.getIndex() == s.index())) {
                return entry.value().getValue();
            }
        }
        return null;
    }

    public ObservableValue<MazeMove> forbiddenMoveProperty(){
        return forbiddenMove.asReadOnly();
    }

    public MazeMove getForbiddenMove(){
        if(this.forbiddenMove.getValue() == null){
            return null;
        }
        return this.forbiddenMove.getValue().getOpposite();
    }

    public void move(MazeMove mazeMove){

        if(mazeMove == null){
            throw new IllegalArgumentException("Le déplacement est null");
        }

        Direction direction = mazeMove.direction();
        int index = mazeMove.index();

        if(index < 0 || index >= tiles.getWidth()){
            throw new IllegalArgumentException("Index hors bornes");
        }

        if((direction == Direction.LEFT || direction == Direction.RIGHT) && fixedRows.contains(index)){
            throw new IllegalArgumentException("Ligne fixe");
        }

        if((direction == Direction.UP || direction == Direction.DOWN) && fixedColumns.contains(index)){
            throw new IllegalArgumentException("Colonne fixe");
        }

        if(getForbiddenMove() != null && getForbiddenMove().equals(mazeMove)){
            throw new IllegalArgumentException("Déplacement interdit");
        }

        Tile expelled = this.extraTile.getValue();
        SimpleObservableValue<Tile> oldExtra = this.extraTile;

        switch (direction) {
            case RIGHT ->
                {
                expelled = this.tiles.get(index, tiles.getWidth() - 1).getValue();
                    for (int i = tiles.getWidth() - 1; i > 0; i--) {
                        this.tiles.set(index, i, this.tiles.get(index, i + 1));
                    }
                    this.tiles.set(index, 0, oldExtra);
                    this.extraTile.setValue(expelled);
                }
            case LEFT -> {
                expelled = this.tiles.get(index, 0).getValue();
                for (int i = 0; i < tiles.getWidth() - 1; i++) {
                    this.tiles.set(index, i, this.tiles.get(index, i - 1));
                }
                this.tiles.set(index, tiles.getWidth() - 1, oldExtra);
                this.extraTile.setValue(expelled);
            }
            case DOWN -> {
                expelled = this.tiles.get(tiles.getHeight() - 1, index).getValue();
                for (int i = tiles.getHeight() - 1; i > 0; i--) {
                    this.tiles.set(i, index, tiles.get(i - 1, index));
                }
                this.tiles.set(0, index, oldExtra);
                this.extraTile.setValue(expelled);
            }
            case UP -> {
                expelled = this.tiles.get(0, index).getValue();
                for (int i = 0; i < tiles.getHeight() - 1; i++) {
                    this.tiles.set(i, index, this.tiles.get(i + 1, index));
                }
                this.tiles.set(this.tiles.getHeight() - 1, index, oldExtra);
                this.extraTile.setValue(expelled);
            }
            default -> {
                throw new IllegalArgumentException("Direction Invalide");
            }
        }
        if(expelled.getPlayers() != null ){
            for(Player p : expelled.getPlayers()){
                this.extraTile.getValue().removePlayer(p);
                oldExtra.getValue().addPlayer(p);
            }
        }
    }

    public static Maze from(MazeDTO mazeDTO, List<Player> players){
        Grid<TileDTO> grid = mazeDTO.getTiles();

        Grid<Tile> tiles = new ArrayGrid<>(grid.getWidth(), grid.getHeight());

        for(int x = 0; x < grid.getWidth(); x++){
            for(int y = 0; y < grid.getHeight(); y++){
                Tile tile = Tile.from(grid.get(x, y), players);
                tiles.set(x, y, tile);
            }
        }

        Tile extraTile = Tile.from(mazeDTO.getExtraTile(), players);

        Maze maze = new Maze(tiles, extraTile, new HashSet<>(mazeDTO.getFixedRows()), new HashSet<>(mazeDTO.getFixedColumns()));

        if(mazeDTO.getForbiddenMove() != null){
            maze.forbiddenMove.setValue(mazeDTO.getForbiddenMove().getOpposite());
        }

        return maze;
    }

}
