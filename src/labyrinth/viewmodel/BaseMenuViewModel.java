package labyrinth.viewmodel;

import javafx.application.Platform;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.service.Services;
import labyrinth.service.dialog.FileRequest;
import labyrinth.service.serialization.GameDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class BaseMenuViewModel implements MenuViewModel{

    private final Game game;
    private final Services services;
    private final BaseGameViewModel gameViewModel;

    public BaseMenuViewModel(Game game, Services services, BaseGameViewModel gameViewModel) {
        this.game = game;
        this.services = services;
        this.gameViewModel = gameViewModel;
    }

    @Override
    public void newGame() {
        game.reset();
        game.init();

        services.getDialogService().publishMessage("Nouvelle partie. Joueur " + game.getCurrentPlayer().getIndex() + " commence");
    }

    @Override
    public void load() {
        FileRequest request = services.getDialogService()
                .requestOpenFile("Choisir une sauvegarde à charger");

        request.getFile().thenAccept(file -> {
            if (file == null) {
                return;
            }

            try {
                String data = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                GameDTO gameDTO = services.getSerializationService().deserialize(data);

                game.updateFrom(gameDTO);
                gameViewModel.rebuildMazeViewModel();

                services.getDialogService().publishMessage("Partie chargée.");
            } catch (Exception e) {
                e.printStackTrace();
                services.getDialogService().publishError("Impossible de charger cette sauvegarde : " + e.getMessage());
            }
        });
    }

    @Override
    public void save() {
        if(game.getGameState() == GameState.SETUP){
            services.getDialogService().publishError("Impossible de sauvegarder avant de commencer");
            return;
        }

        FileRequest request = services.getDialogService().requestSaveFile("Choisir où sauvegarder la partie");
        request.getFile().thenAccept(file -> {
            if (file == null) {
                return;
            }

            try {
                GameDTO gameDTO = new GameDTO(game);
                String data = services.getSerializationService().serialize(gameDTO);

                Files.writeString(file.toPath(), data, StandardCharsets.UTF_8);

                services.getDialogService().publishMessage("Partie sauvegardée");
            } catch (Exception e) {
                e.printStackTrace();
                services.getDialogService().publishError("Impossible de sauvegarder la partie : " + e.getMessage());
            }
        });
    }

    @Override
    public void showAbout() {
        services.getDialogService().publishMessage(
                "Labyrinth\nProjet JavaFX - TP sérialisation et services"
        );
    }

    @Override
    public void showProfilesManager() {
        System.out.println("BaseMenuViewModel : showProfilesManager appelé");
        services.getDialogService().requestShowProfilesManager();
    }

    @Override
    public void exit() {
        Platform.exit();
    }
}
