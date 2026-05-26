package labyrinth.service;

import labyrinth.model.Game;

public class GameBootstrapper {

    private final GameInitialiser gameInitialiser;
    private final SaveService  saveService;

    public GameBootstrapper(Services services) {
        this.gameInitialiser = new GameInitialiser(services.getPersistenceService());
        this.saveService = new SaveService(services.getPersistenceService());
    }

    public Game bootstrap() {
        Game game = new Game();

        gameInitialiser.init(game);
        saveService.attach(game);

        return game;
    }

}
