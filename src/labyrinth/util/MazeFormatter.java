package labyrinth.util;


import labyrinth.model.Maze;
import labyrinth.model.Tile;
import labyrinth.util.collection.Grid;






public class MazeFormatter {

    //public String Format(Tile tile){



    public String Format(Maze maze){
        int nbLignes = maze.getFixedRows().size();
        String res = "";

        for(int i = 0 ; i < nbLignes - 1; i++){
            if( i > 0){
                res = res + "\n";
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

                //String repr = format(tile); // fonction format(Tile) pas enocre finaliser
                //parts[x] = repr.split("\n");
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
}
