package labyrinth.util;


import labyrinth.model.*;
import labyrinth.util.collection.Grid;


public class MazeFormatter {
    public String format(Tile tile){

        boolean up = tile.getExits().contains(Direction.UP);
        boolean right = tile.getExits().contains(Direction.RIGHT);
        boolean down = tile.getExits().contains(Direction.DOWN);
        boolean left = tile.getExits().contains(Direction.LEFT);

        StringBuilder ligne1 = new StringBuilder().append("+").append("---").append((up) ? "   " : "---" ).append("---").append("+" + "\n");
        String ligne2 = ("|         |\n");
        StringBuilder ligne3 = new StringBuilder().append((left) ? " " : "|").append("    ").append(getContentToChar(tile.getContent())).append("    ").append((right) ? " " : "|\n");
        String ligne4 = ("|         |\n");
        StringBuilder ligne5 = new StringBuilder().append("+").append("---").append((down) ? "   " : "---" ).append("---").append("+" + "\n");

        return ligne1 + ligne2 + ligne3 + ligne4 + ligne5;
    }



    public String Format(Maze maze){
        int nbLignes = maze.getFixedRows().size();
        String res = "";

        for(int i = 0 ; i < nbLignes - 1; i++){
            if( i > 0){
                res = res + FormatRow(maze , i);
            }
        }
        return res;
    }

    public String FormatRow(Maze maze , int y){
        Grid<Tile> grid = maze.getTiles();
        int nbColomns = maze.getFixedColumns().size();
        String[][] parts = new String[nbColomns][];

        for (Grid.Entry<Tile> entry : grid) {


            if (entry.position().y() == y) {
                int x = entry.position().x();
                Tile tile = entry.value();

                String repr = format(tile); // fonction format(Tile) pas enocre finaliser
                parts[x] = repr.split("\n");
            }
        }

        int N = parts[0].length;
        String res = "";
        for (int i = 0 ; i < N-1 ; i++){
            if(i > 0){
                res += "\n";
            }
            for(int j = 0 ; j < nbColomns ; j++){
                if(j > 0){
                    res += " ";
                }
                res += parts[j][i];
            }
        }

        return res;
    }

    private char getContentToChar(Content c){
        if(c instanceof None) return ' ';
        if( c instanceof StartingPosition sp) return (char) ('0' + sp.index());
        if( c instanceof Treasure tr) return (char) ('A' + tr.id());
        return '?' ;
    }
}
