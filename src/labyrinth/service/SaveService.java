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


    public void attach(Game game) {
        game.stateProperty().addListener((obs, oldState, newState) -> {
            if (oldState == GameState.TURN_END && newState == GameState.MAZE_MOVE) {
                sqlPersistenceService.saveGame(new GameDTO(game));
            }
        });
    }

    private void saveGame(Game game){
        GameDTO gameDTO = new GameDTO(game);
        sqlPersistenceService.saveGame(gameDTO);
    }

}
