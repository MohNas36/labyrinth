package labyrinth.view;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import labyrinth.service.dialog.*;

import java.io.File;

public class DialogHandler {

    public Window owner;
    private final ProfilesManagerWindow profilesManagerWindow;

    public DialogHandler(DialogService dialogService, Window owner, ProfilesManagerWindow profilesManagerWindow) {
        this.owner = owner;
        this.profilesManagerWindow = profilesManagerWindow;

        dialogService.fileRequestProperty().addListener(observable -> {
            FileRequest fileRequest = dialogService.fileRequestProperty().getValue();
            if(fileRequest != null) {
                handleFileRequest(fileRequest);
            }
        });

        dialogService.messageProperty().addListener(observable -> {
            Message message = dialogService.messageProperty().getValue();
            if(message != null) {
                handleMessage(message);
            }
        });

        dialogService.profilesManagerRequestProperty().addListener(observable -> {
            ProfilesManagerRequest request = dialogService.profilesManagerRequestProperty().getValue();

            if(request != null) {
                handleProfilesManagerRequest(request);
            }
        });
    }

    /**
     * Traite une demande de fichier.
     *
     * Selon le mode de la demande, on ouvre soit un dialogue d'ouverture,
     * soit un dialogue de sauvegarde. Le fichier choisi est ensuite donné
     * au FileRequest.
     */
    private void handleFileRequest(FileRequest fileRequest) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileRequest.getRequest());

        File file;

        if(fileRequest.getMode() == Mode.OPEN){
            file = fileChooser.showOpenDialog(owner);
        }else{
            file = fileChooser.showSaveDialog(owner);
        }

        fileRequest.setFile(file);
    }

    /**
     * Affiche un message dans une fenêtre JavaFX.
     *
     * Le type de l'alerte dépend du fait que le message soit une erreur
     * ou un simple message d'information.
     */
    private void handleMessage(Message message) {
        Alert.AlertType type;

        if(message.isError()){
            type = Alert.AlertType.ERROR;
        }else{
            type = Alert.AlertType.INFORMATION;
        }

        Alert alert = new Alert(type);
        alert.initOwner(owner);
        alert.setHeaderText(null);
        alert.setContentText(message.getMessage());
        alert.showAndWait();
    }

    /**
     * Affiche la fenêtre de gestion des profils.
     *
     * Si la fenêtre est déjà ouverte, elle est simplement remise au premier plan.
     */
    private void handleProfilesManagerRequest(ProfilesManagerRequest request) {
        profilesManagerWindow.show();
        profilesManagerWindow.toFront();
    }
}
