package labyrinth.service;

import labyrinth.model.Game;

public class GameBootstrapper {

    private final GameInitialiser gameInitialiser;
    private final SaveService  saveService;

    public GameBootstrapper(Services services) {
        this.gameInitialiser = new GameInitialiser(services.getPersistenceService());
        this.saveService = new SaveService(services.getPersistenceService());
    }

    /**
     * Crée et prépare une instance de Game.
     *
     * La partie est d'abord créée, puis initialisée avec la dernière sauvegarde
     * si elle existe. Ensuite, on attache la sauvegarde automatique.
     */
    public Game bootstrap() {
        Game game = new Game();

        gameInitialiser.init(game);
        saveService.attach(game);

        return game;
    }

}
