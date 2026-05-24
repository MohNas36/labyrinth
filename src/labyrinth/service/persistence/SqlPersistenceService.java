package labyrinth.service.persistence;

import labyrinth.service.serialization.SerializationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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


    public UUID createUserProfile(String name){
        UUID uuid = UUID.randomUUID();

        String sql = "INSERT INTO profiles (id, name) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, name);

            statement.executeUpdate();

            firePlayerInfoChanged(uuid);

            return uuid;

        } catch (SQLException e) {
            throw new SqlException("Impossible de créer le profil utilisateur", e);
        }
    }

    public List<UserProfile> getUserProfiles() {
        List<UserProfile> profiles = new ArrayList<>();

        String sql = "SELECT id, name FROM profiles";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("id"));
                String name = resultSet.getString("name");

                profiles.add(new UserProfile(uuid, name));
            }

            return profiles;

        } catch (SQLException e) {
            throw new SqlException("Impossible de récupérer les profils utilisateurs", e);
        }
    }

    public UserProfile getUserProfile(UUID userId) {
        String sql = "SELECT id, name FROM profiles WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");

                    return new UserProfile(uuid, name);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new SqlException("Impossible de récupérer le profil utilisateur", e);
        }
    }

    public void setUserName(UUID userId, String name) {
        String sql = "UPDATE profiles SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, userId.toString());

            statement.executeUpdate();

            firePlayerInfoChanged(userId);

        } catch (SQLException e) {
            throw new SqlException("Impossible de modifier le nom du profil utilisateur", e);
        }
    }

    public void deleteUserProfile(UUID userId) {
        String sql = "DELETE FROM profiles WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId.toString());

            statement.executeUpdate();

            firePlayerInfoChanged(userId);

        } catch (SQLException e) {
            throw new SqlException("Impossible de supprimer le profil utilisateur", e);
        }
    }


    public void addUserProfileChangedHandler(UserProfileChangedHandler handler) {
        handlers.add(handler);
    }

    public void removeUserProfileChangedHandler(UserProfileChangedHandler handler) {
        handlers.remove(handler);
    }

    private void firePlayerInfoChanged(UUID playerId) {
        for (UserProfileChangedHandler handler : handlers) {
            handler.handlePlayerInfoChanged(playerId);
        }
    }
}