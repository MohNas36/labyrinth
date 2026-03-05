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

        /*
        ArrayList<Treasure> treasures = new ArrayList<>();
        ArrayList<StartingPosition> s_positions = new ArrayList<>();
        for(int i = 0; i < tiles.getHeight(); i++){
            for(int j = 0; j < tiles.getWidth(); j++){
                Tile t1 = tiles.get(i, j);
                if(t1.getContent() instanceof Treasure t){
                    t.id() == 0;
                }
                if(t1.getContent() == new StartingPosition()){
                    s_positions.add(t1.getContent());
                }
            }
        }

        for(int i = 0; i < treasures.size(); i++){
            Treasure t1 = treasures.get(i);
            for(int k = i; k < treasures.size(); k++){
                if(t1.equals(treasures.get(k))){
                    throw new IllegalArgumentException("Il y a un tresor sur plusieurs tuiles");
                }
            }
        }

        for(int i = 0; i < s_positions.size(); i++){
            StartingPosition p = s_positions.get(i);
            for(int k = i; k < s_positions.size(); k++){
                if(p.equals(s_positions.get(k))){
                    throw new IllegalArgumentException("Il y a une StartingPosition sur plusieurs tuiles");
                }
            }
        }
         */

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
}
