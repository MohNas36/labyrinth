package labyrinth.service;

import labyrinth.model.Game;

public class GameBootstrapper {

    private final GameInitialiser gameInitialiser;
    private final SaveService  saveService;

    public GameBootstrapper(Services services) {
        this.gameInitialiser = new GameInitialiser(services.getSqlPersistenceService());
        this.saveService = new SaveService(services.getSqlPersistenceService());
    }

    public Game bootstrap() {
        Game game = new Game();

        gameInitialiser.init(game);
        saveService.attach(game);

        return game;
    }

}
