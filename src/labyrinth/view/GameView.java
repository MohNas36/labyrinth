package labyrinth.view;

import javafx.scene.layout.BorderPane;
import labyrinth.viewmodel.GameViewModel;

public class GameView extends BorderPane {

    private MazeView mazeView;
    private PlayersView playersView;

    public GameView (GameViewModel gameViewModel){
        this.mazeView = new MazeView(gameViewModel.getMazeViewModel());
        this.playersView = new PlayersView(gameViewModel);

        setCenter(mazeView);
        setRight(playersView);
    }

}
