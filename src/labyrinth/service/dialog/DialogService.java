package labyrinth.service.dialog;

import labyrinth.util.observable.ObservableValue;

import java.io.File;

public interface DialogService {

    public void publishMessage(String message);

    public void publishError(String message);

    public FileRequest requestOpenFile(String request);

    public FileRequest requestSaveFile(String request);

    ObservableValue<Message> messageProperty();

    ObservableValue<FileRequest> fileRequestProperty();

}
