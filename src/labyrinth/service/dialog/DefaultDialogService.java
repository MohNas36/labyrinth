package labyrinth.service.dialog;

import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

public class DefaultDialogService implements DialogService {

    private final SimpleObservableValue<Message> message;
    private final SimpleObservableValue<FileRequest> fileRequest;
    private SimpleObservableValue<ProfilesManagerRequest> profilesManagerRequest;

    public DefaultDialogService() {
        this.message = new SimpleObservableValue<>(null);
        this.fileRequest = new SimpleObservableValue<>(null);
        this.profilesManagerRequest = new SimpleObservableValue<>(null);
    }

    @Override
    public void publishMessage(String message) {
        this.message.setValue(new Message(message, false));
    }

    @Override
    public void publishError(String message) {
        this.message.setValue(new Message(message, true));
    }

    @Override
    public FileRequest requestOpenFile(String request) {
        FileRequest fileRequest = new FileRequest(request, Mode.OPEN);
        this.fileRequest.setValue(fileRequest);
        return fileRequest;
    }

    @Override
    public FileRequest requestSaveFile(String request) {
        FileRequest fileRequest = new FileRequest(request, Mode.SAVE);
        this.fileRequest.setValue(fileRequest);
        return fileRequest;
    }

    @Override
    public ObservableValue<Message> messageProperty() {
        return message.asReadOnly();
    }

    @Override
    public ObservableValue<FileRequest> fileRequestProperty() {
        return fileRequest.asReadOnly();
    }

    @Override
    public void requestShowProfilesManager() {
        profilesManagerRequest.setValue(new ProfilesManagerRequest());
    }

    @Override
    public ObservableValue<ProfilesManagerRequest> profilesManagerRequestProperty() {
        return profilesManagerRequest.asReadOnly();
    }
}
