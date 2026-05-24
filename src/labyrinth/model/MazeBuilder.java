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

    public Maze build() {
        List<Tile> L = new ArrayList<>();
        L.addAll(freeTiles);
        Collections.shuffle(L);

        for (int i = 0; i < L.size(); i++) {
            int n = (int) (Math.random() * 4);
            Tile tile = L.get(i);
            L.set(i, new Tile(tile.getKind(), tile.getContent(), tile.getOrientation().getRotated(n)));
        }

        int i = 0;

        for (int x = 0; x < tiles.getWidth(); x++) {
            for (int y = 0; y < tiles.getHeight(); y++) {
                if (tiles.get(x, y) == null) {
                    tiles.set(x, y, L.get(i));
                    i++;
                }
            }
        }

        Tile extraTile = L.get(i);

        return new Maze(this.tiles, extraTile, this.fixRows, this.fixCols);
    }


    public static Maze buildStandardMaze(){
        MazeBuilder mb = new MazeBuilder(7, 7);
        Tile j1 = new Tile(TileKind.ELBOW, new StartingPosition(0));
        Tile j2 = new Tile(TileKind.ELBOW, new StartingPosition(1));
        Tile j3 = new Tile(TileKind.ELBOW, new StartingPosition(2));
        Tile j4 = new Tile(TileKind.ELBOW, new StartingPosition(3));

        mb.addFixedTile(0, 0, j1);
        mb.addFixedTile(6, 6, j2);
        mb.addFixedTile(0, 6, j3);
        mb.addFixedTile(6, 0, j4);

        int id = 0;
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if((i % 2 == 0 && j % 2 == 0) && !((i == 0 && j == 0) || (i == 0 && j == 6) || (i == 6 && j == 0) || (i == 6 && j == 6))){
                    mb.addFixedTile(i, j, new Tile(TileKind.T_CROSS, new Treasure(id)));
                    id++;
                }
            }
        }

        for(int i = 0; i < 13; i++){
            mb.addFreeTile(new Tile(TileKind.CORRIDOR));
        }

        int id2 = 0;
        for(int i = 0; i < 15; i++){
            if(id2 < 6){
                /*Multiplication par 10 pour pouvoir distinguer*/
                mb.addFreeTile(new Tile(TileKind.ELBOW, new Treasure(id + id2)));
                id2++;
            }else{
                mb.addFreeTile(new Tile(TileKind.ELBOW));
            }
        }

        for(int i = 0; i < 6; i++){
            /*Multiplication par 20 pour pouvoir distinguer*/
            mb.addFreeTile(new Tile(TileKind.T_CROSS, new Treasure(id + id2)));
            id2++;
        }

        return mb.build();
    }
}
