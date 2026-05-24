package labyrinth;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import labyrinth.model.Game;
import labyrinth.service.GameBootstrapper;
import labyrinth.service.Services;
import labyrinth.service.dialog.DefaultDialogService;
import labyrinth.service.dialog.DialogService;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.JsonSerializationService;
import labyrinth.service.serialization.SerializationService;
import labyrinth.view.DialogHandler;
import labyrinth.view.MainView;
import labyrinth.view.ProfilesManagerWindow;
import labyrinth.viewmodel.*;

public class Labyrinth extends Application {

    @Override
    public void start(Stage primaryStage) {
        SerializationService serializationService = new JsonSerializationService();
        DialogService dialogService = new DefaultDialogService();
        SqlPersistenceService sqlPersistenceService = new SqlPersistenceService(serializationService);

        Services services = new Services(serializationService, dialogService,  sqlPersistenceService);



        GameBootstrapper gameBootstrapper = new GameBootstrapper(services);
        Game game = gameBootstrapper.bootstrap();

        BaseGameViewModel viewModel = new BaseGameViewModel(game, services);
        ProfilesManagerWindow profilesManagerWindow = new ProfilesManagerWindow(viewModel.getUserProfilesViewModel());

        new DialogHandler(services.getDialogService(), primaryStage,  profilesManagerWindow);

        primaryStage.titleProperty().bind(viewModel.titleProperty());
        BorderPane root = new MainView(viewModel, viewModel.getMenuViewModel());

        Scene scene = new Scene(root, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
        if (game.getCurrentPlayer() != null) {
            services.getDialogService().publishMessage(
                    "La partie commence. Le joueur " + game.getCurrentPlayer().getIndex() + " commence."
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

