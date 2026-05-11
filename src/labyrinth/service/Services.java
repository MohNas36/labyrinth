package labyrinth.service;

import labyrinth.service.dialog.DialogService;
import labyrinth.service.serialization.SerializationService;

public class Services {

    private final SerializationService serializationService;
    private final DialogService dialogService;

    public Services(SerializationService serializationService, DialogService dialogService) {
        this.serializationService = serializationService;
        this.dialogService = dialogService;
    }

    public DialogService getDialogService() {
        return dialogService;
    }

    public SerializationService getSerializationService() {
        return serializationService;
    }
}
