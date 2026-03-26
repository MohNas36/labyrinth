package labyrinth;

import labyrinth.model.*;
import labyrinth.util.collection.Grid;

import java.util.Scanner;

public class LabyrinthCLI {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Entrer le nombre de joueurs que vous souhaiter (max 4)");
        int i = s.nextInt();

        Game game = new Game();
        game.init();
        Grid<Tile> grid = game.getMaze().getTiles();

        while(game.getGameState() != GameState.GAME_OVER){
            System.out.println(game.getGameState());

            Scanner s2 = new Scanner(System.in);
            System.out.println("Entrer le nombre de rotation à la tuile courante");
            int rotation = s2.nextInt();
            game.rotateExtraTile(rotation);
            System.out.println(game.getMaze().getExtraTile().getOrientation().toString());

            Scanner s3 = new Scanner(System.in);
            Scanner s4 = new Scanner(System.in);
            System.out.println("Entrer l'indice du  deplacement au labyrinth");
            int indice = s3.nextInt();
            System.out.println("Entrer le direction du deplacement au labyrinth, 1.UP 2.RIGHT 3.DOWN 4.LEFT");
            int direction = s4.nextInt();
            MazeMove mazeMove = null;
            if(direction == 1){
                mazeMove = new MazeMove(Direction.UP, indice);
            }
            else if(direction == 2){
                mazeMove = new MazeMove(Direction.RIGHT, indice);
            }
            else if(direction == 3){
                mazeMove = new MazeMove(Direction.DOWN, indice);
            }
            else if(direction == 4){
                mazeMove = new MazeMove(Direction.LEFT, indice);
            }

            game.mazeMove(mazeMove);

            System.out.println(game.getGameState());

            Scanner s5 = new Scanner(System.in);
            Scanner s6 = new Scanner(System.in);
            System.out.println("Entrer le deplacement du joueur x et y (que (1,1) pour l'instant)");
            int dx = s5.nextInt();
            int dy = s6.nextInt();

            game.movePlayer(grid.get(dx, dy));
            game.endTurn();

            System.out.println(game.getGameState());

        }

        /*Player p1 = new Player(0);
        Player p2 = new Player(1);
        Player p3 = new Player(2);
        Player p4 = new Player(3);

        Tile test = grid.get(3, 3);
        test.addPlayer(p1);
        test.addPlayer(p2);
        test.addPlayer(p3);
        test.addPlayer(p4);

        System.out.println(test.getPlayers().toString());
        System.out.println(game.getGameState().toString());*/
    }

}
