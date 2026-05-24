package labyrinth.service;

import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.GameDTO;

public class SaveService {

    private final SqlPersistenceService sqlPersistenceService;


    public SaveService(SqlPersistenceService sqlPersistenceService) {
        this.sqlPersistenceService = sqlPersistenceService;
    }

    public void attach(Game game){
        game.stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == GameState.TURN_END || newValue == GameState.GAME_OVER){
                saveGame(game);
            }
        });
    }

    private void saveGame(Game game){
        GameDTO gameDTO = new GameDTO(game);
        sqlPersistenceService.saveGame(gameDTO);
    }

}
