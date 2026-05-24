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


    private void handleProfilesManagerRequest(ProfilesManagerRequest request) {
        profilesManagerWindow.show();
        profilesManagerWindow.toFront();
    }
}
