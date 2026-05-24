package labyrinth.service;

import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.serialization.GameDTO;

public class SaveService {

    private final SqlPersistenceService sqlPersistenceService;
    private final Game game;

    public SaveService(SqlPersistenceService sqlPersistenceService , Game game) {
        this.sqlPersistenceService = sqlPersistenceService;
        this.game = game;
    }

    public void attach(Game game){
        game.stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == GameState.TURN_END || newValue == GameState.GAME_OVER){
                saveGame();
            }
        });
    }

    private void saveGame(){
        GameDTO gameDTO = new GameDTO(game);
        sqlPersistenceService.saveGame(gameDTO);
    }

}
