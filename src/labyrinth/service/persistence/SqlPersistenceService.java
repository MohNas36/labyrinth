package labyrinth.service.persistence;

import labyrinth.service.serialization.SerializationService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SqlPersistenceService {

    private Connection connection;
    private SerializationService serializationService;
    private List<UserProfileChangedHandler> handlers;

    public SqlPersistenceService(SerializationService serializationService) {
        this.serializationService = serializationService;
        this.handlers = new ArrayList<>();
    }
}