package labyrinth;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import labyrinth.model.Game;
import labyrinth.view.MainView;
import labyrinth.viewmodel.*;

public class Labyrinth extends Application {

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        BaseGameViewModel viewModel = new BaseGameViewModel(game);
        BaseMenuViewModel menuViewModel = new BaseMenuViewModel();
        /*StubTileViewModel tileViewModel = new StubTileViewModel();
        StubCellViewModel cellViewModel = new StubCellViewModel(new Position(0, 0));*/

        primaryStage.titleProperty().bind(viewModel.titleProperty());
        BorderPane root = new MainView(viewModel, menuViewModel);

        /*DevPanel devPanel = new DevPanel(viewModel/*, tileViewModel, cellViewModel, menuViewModel);*/

        Scene scene = new Scene(root, 800, 800);

        viewModel.setTitle("Test Labyrinth");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

