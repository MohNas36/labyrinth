package labyrinth.service;

import labyrinth.model.Game;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.GameDTO;

public class GameInitialiser {

    private final SqlPersistenceService sqlPersistenceService;

    public GameInitialiser(SqlPersistenceService sqlPersistenceService) {
        this.sqlPersistenceService = sqlPersistenceService;
    }

    public void init(Game game) {
        GameDTO gameDTO = sqlPersistenceService.loadLatestGame();
        if(gameDTO == null) {
            game.init();
        }else {
            game.updateFrom(gameDTO);
        }

    }

}
