package labyrinth.service.persistence;

import labyrinth.service.serialization.SerializationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlPersistenceService {

    private Connection connection;
    private SerializationService serializationService;
    private List<UserProfileChangedHandler> handlers;

    public SqlPersistenceService(SerializationService serializationService) {
        this.serializationService = serializationService;
        this.handlers = new ArrayList<>();

        try {
            Path directory = Path.of(System.getProperty("user.home"), ".labyrinth");
            Files.createDirectories(directory);

            Path database = directory.resolve("labyrinth.db");

            this.connection = DriverManager.getConnection("jdbc:sqlite:" + database);

            String createProfilesTable = """
                    CREATE TABLE IF NOT EXISTS profiles (
                        id TEXT PRIMARY KEY,
                        name TEXT
                    )
                    """;

            String createGamesTable = """
                    CREATE TABLE IF NOT EXISTS games (
                        id TEXT PRIMARY KEY,
                        timestamp INTEGER,
                        data TEXT
                    )
                    """;

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createProfilesTable);
                statement.executeUpdate(createGamesTable);
            }

        } catch (IOException | SQLException e) {
            throw new SqlException("Erreur lors de l'initialisation de la base de données", e);
        }
    }
}