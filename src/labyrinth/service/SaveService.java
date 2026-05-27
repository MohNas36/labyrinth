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


    /**
     * Attache le service de sauvegarde automatique à une partie.
     *
     * On écoute les changements d'état du jeu. Quand un tour vient de se finir
     * et qu'un nouveau tour commence, on sauvegarde la partie dans la base.
     *
     * Cela permet de reprendre automatiquement la dernière partie jouée
     * au prochain lancement de l'application.
     */
    public void attach(Game game) {
        game.stateProperty().addListener((obs, oldState, newState) -> {
            if (oldState == GameState.TURN_END && newState == GameState.MAZE_MOVE) {
                sqlPersistenceService.saveGame(new GameDTO(game));
            }
        });
    }

    /**
     * Sauvegarde explicitement l'état courant du jeu.
     *
     * La partie est d'abord convertie en GameDTO, puis envoyée au service
     * de persistance. Cette méthode garde la logique de conversion au même endroit.
     */
    private void saveGame(Game game){
        GameDTO gameDTO = new GameDTO(game);
        sqlPersistenceService.saveGame(gameDTO);
    }

}
