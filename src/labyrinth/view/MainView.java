package labyrinth.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import labyrinth.model.GameState;
import labyrinth.viewmodel.GameViewModel;
import labyrinth.viewmodel.MenuViewModel;

public class MainView extends BorderPane {

    private GameViewModel viewModel;
    private Button startGame;
    private GameView gameView;
    private MenuView menuView;
    private PlayersView setupPlayersView;

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

    public void updateView() {

        if (viewModel.stateProperty().getValue() == GameState.SETUP) {
            if (setupPlayersView == null) {
                setupPlayersView = new PlayersView(viewModel);
            }

            StackPane centerPane = new StackPane(startGame);
            centerPane.setAlignment(Pos.CENTER);

            setCenter(centerPane);
            setRight(setupPlayersView);

        } else {
            if (gameView == null) {
                gameView = new GameView(viewModel);
            }

            setCenter(gameView);
            setRight(null);
        }
    }
}
