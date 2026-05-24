package labyrinth.service;

import labyrinth.service.dialog.DialogService;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.SerializationService;

public class Services {

    private final SerializationService serializationService;
    private final DialogService dialogService;
    private final SqlPersistenceService sqlPersistenceService;

    public Services(SerializationService serializationService, DialogService dialogService, SqlPersistenceService sqlPersistenceService) {
        this.serializationService = serializationService;
        this.dialogService = dialogService;
        this.sqlPersistenceService = sqlPersistenceService;

    }

    public DialogService getDialogService() {
        return dialogService;
    }

    public SerializationService getSerializationService() {
        return serializationService;
    }

    public SqlPersistenceService getSqlPersistenceService() {
        return sqlPersistenceService;
    }
}
