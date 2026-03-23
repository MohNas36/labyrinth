package labyrinth.model;

import labyrinth.util.collection.Grid;
import labyrinth.util.*;
import labyrinth.util.collection.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Maze {
    private final Grid<Tile> tiles;
    private Set<Integer> fixedRows;
    private Set<Integer> fixedColumns;
    private Tile extraTile;
    private MazeMove mazeMove;

    public Maze(Grid<Tile> tiles, Tile extraTile, Set<Integer> fixedRows, Set<Integer> fixedColumns) {
        if(tiles == null || extraTile == null || fixedRows == null || fixedColumns == null) {
            throw new NullPointerException("Au moins un des parametres du labyrinthe est null");
        }
        Require.noNullElement((Collection<?>) tiles, "La grille n'est pas complete");

        for(int i = 0; i < tiles.getHeight(); i++){
            for(int j = 0; j < tiles.getWidth(); j++){
                Tile t1 = tiles.get(i, j);

                for(int k = i; k < tiles.getWidth(); k++){
                    for(int l = j; l < tiles.getHeight(); l++){
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
            for(int k = i; k < treasures.size(); k++){
                Treasure t2 = treasures.get(k);
                if(t1.id() == t2.id()){
                    throw new IllegalArgumentException("Il y a un tresor sur plusieurs tuiles");
                }
            }
        }

        for(int i = 0; i < s_positions.size(); i++){
            StartingPosition s_pos = s_positions.get(i);
            for(int k = i; k < s_positions.size(); k++){
                StartingPosition s_pos2 = s_positions.get(k);
                if(s_pos.index() == s_pos2.index()){
                    throw new IllegalArgumentException("Il y a une StartingPosition sur plusieurs tuiles");
                }
            }
        }


        this.tiles = tiles;
        this.fixedRows = fixedRows;
        this.fixedColumns = fixedColumns;
        this.extraTile = extraTile;
    }

    public Grid<Tile> getTiles() {
        return tiles;
    }

    public Position getPosition(Tile tile) {
        for(Grid.Entry<Tile> entry : tiles){
            if(entry.value().equals(tile)){
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

    public Tile getExtraTile() {
        return extraTile;
    }

    public Tile getTile(Player p){
        for(Grid.Entry<Tile> entry : tiles){
            if(entry.value().getPlayers().contains(p)){
                return entry.value();
            }
        }
        return null;
    }

    public Tile getStartingTile(Player p){
        for(Grid.Entry<Tile> entry : tiles){
            if(entry.value().getContent().equals(new StartingPosition(p.getIndex()))){
                return entry.value();
            }
        }
        return null;
    }

    public MazeMove getForbiddenMove(){
        if(this.mazeMove == null){
            return null;
        }
        return this.mazeMove.getOpposite();
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

        Tile expelled = this.extraTile;
        Tile oldExtra = this.extraTile;

        switch (direction) {
            case RIGHT ->
                {
                expelled = this.tiles.get(index, tiles.getWidth() - 1);
                    for (int i = tiles.getWidth() - 1; i > 0; i--) {
                        this.tiles.set(index, i, this.tiles.get(index, i + 1));
                    }
                    this.tiles.set(index, 0, oldExtra);
                    this.extraTile = expelled;
                }
            case LEFT -> {
                expelled = this.tiles.get(index, 0);
                for (int i = 0; i < tiles.getWidth() - 1; i++) {
                    this.tiles.set(index, i, this.tiles.get(index, i - 1));
                }
                this.tiles.set(index, tiles.getWidth() - 1, oldExtra);
                this.extraTile = expelled;
            }
            case DOWN -> {
                expelled = this.tiles.get(tiles.getHeight() - 1, index);
                for (int i = tiles.getHeight() - 1; i > 0; i--) {
                    this.tiles.set(i, index, tiles.get(i - 1, index));
                }
                this.tiles.set(0, index, oldExtra);
                this.extraTile = expelled;
            }
            case UP -> {
                expelled = this.tiles.get(0, index);
                for (int i = 0; i < tiles.getHeight() - 1; i++) {
                    this.tiles.set(i, index, this.tiles.get(i + 1, index));
                }
                this.tiles.set(this.tiles.getHeight() - 1, index, oldExtra);
                this.extraTile = expelled;
            }
            default -> {
                throw new IllegalArgumentException("Direction Invalide");
            }
        }
        if(expelled.getPlayers() != null ){
            for(Player p : expelled.getPlayers()){
                this.extraTile.removePlayer(p);
                oldExtra.addPlayer(p);
            }
        }
    }
}
