package labyrinth.service;

import labyrinth.model.Game;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.GameDTO;

public class GameInitialiser {

    private final SqlPersistenceService sqlPersistenceService;

    public GameInitialiser(SqlPersistenceService sqlPersistenceService) {
        this.sqlPersistenceService = sqlPersistenceService;
    }

    /**
     * Initialise la partie au démarrage de l'application.
     *
     * Si une dernière partie sauvegardée existe, on recharge cette partie
     * dans le modèle courant.
     *
     * Sinon, on initialise une nouvelle partie normale.
     */
    public void init(Game game) {
        GameDTO gameDTO = sqlPersistenceService.loadLatestGame();

        if (gameDTO != null) {
            game.updateFrom(gameDTO);
        } else {
            game.init();
        }
    }

}
