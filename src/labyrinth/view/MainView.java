package labyrinth.view;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import labyrinth.model.GameState;
import labyrinth.viewmodel.GameViewModel;
import labyrinth.viewmodel.MenuViewModel;

public class MainView extends BorderPane {

    private GameViewModel viewModel;
    private Button startGame;
    private GameView gameView;
    private MenuView menuView;

    public MainView(GameViewModel viewModel , MenuViewModel menuViewModel){
        this.viewModel = viewModel;
        this.startGame = new Button("Start game");
        this.gameView = null;
        this.menuView = new MenuView(menuViewModel);

        setTop(menuView);

        startGame.setOnAction(e -> {
            viewModel.startGame();

        });
        viewModel.stateProperty().addListener((obs , oldVal , newVal) -> updateView());

        updateView();

    }

    public void updateView(){

        if(viewModel.stateProperty().getValue() == GameState.SETUP){
            setCenter(startGame);
        }else{
            if(gameView == null){
                gameView = new GameView(viewModel);
            }
            setCenter(gameView);
        }

    }

}
