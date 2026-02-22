package labyrinth.model;

import labyrinth.util.collection.ArrayGrid;

import java.util.*;

public class MazeBuilder {
    private ArrayGrid<Tile> tiles;
    private Set<Tile> freeTiles;
    private final Set<Integer> fixRows;
    private final Set<Integer> fixCols;

    public MazeBuilder(int width, int height) {
        this.tiles = new ArrayGrid<Tile>(width, height);
        this.freeTiles = new HashSet<Tile>();
        this.fixCols = new HashSet<Integer>();
        this.fixRows = new HashSet<Integer>();
    }

    public MazeBuilder addFixedTile(int x, int y, Tile tile) {
        this.tiles.set(x, y, tile);
        this.fixRows.add(x);
        this.fixCols.add(y);
        return this;
    }

    public MazeBuilder addFreeTile(Tile tile) {
        this.freeTiles.add(tile);
        return this;
    }

    public Maze build(){
        List<Tile> L = new ArrayList<>();
        L.addAll(freeTiles);
        Collections.shuffle(L);

        for(int i = 0; i < L.size(); i++) {
            int n = (int) (Math.random() * 4);
            Tile tile = L.get(i);
            L.set(i, new Tile(tile.getKind(), tile.getContent(), tile.getOrientation().getRotated(n)));
        }

        int i = 0;
        for(int x = 0; x < tiles.getWidth(); x++){
            for(int y = 0; y < tiles.getHeight(); y++){
                if(tiles.get(x, y) == null){
                    tiles.set(x, y, L.get(i));
                    i++;
                }
            }
        }

        Tile extraTile = L.remove(L.size() - 1);


        return new Maze(this.tiles, extraTile, this.fixRows, this.fixCols);
    }


}
