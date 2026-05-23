package labyrinth.service.persistence;

import java.util.UUID;

public interface UserProfileChangedHandler {

    void handlePlayerInfoChanged(UUID playerId);
}
