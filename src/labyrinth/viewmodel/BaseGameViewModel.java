package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.service.Services;

import java.util.ArrayList;
import java.util.List;

public class BaseGameViewModel implements GameViewModel {
    private ObjectProperty<MazeViewModel> mazeViewModel = new SimpleObjectProperty<>();
    private Property<String> title = new SimpleObjectProperty<>("Labyrinthe");
    private ObjectProperty<Integer> currentPlayerId = new SimpleObjectProperty(0);
    private List<PlayerViewModel> playerViewModels;
    private ObjectProperty<GameState> state = new SimpleObjectProperty<>(GameState.SETUP);
    private MenuViewModel menuViewModel;
    private Services services;
    private final Game game;
    private final UserProfilesViewModel userProfilesViewModel;

    public BaseGameViewModel(Game game, Services services) {
        this.game = game;
        this.services = services;
        this.playerViewModels = new ArrayList<>();
        if (game.getGameState() == GameState.SETUP) {
            mazeViewModel.set(null);
        } else {
            mazeViewModel.set(new BaseMazeViewModel(game, this));
        }
        this.playerViewModels.add(new BasePlayerViewModel(game, 0, services.getPersistenceService()));
        this.playerViewModels.add(new BasePlayerViewModel(game, 1,  services.getPersistenceService()));
        this.playerViewModels.add(new BasePlayerViewModel(game, 2 ,  services.getPersistenceService()));
        this.playerViewModels.add(new BasePlayerViewModel(game, 3, services.getPersistenceService()));
        menuViewModel = new BaseMenuViewModel(game, services, this);
        this.userProfilesViewModel = new BaseUserProfilesViewModel(game , services.getPersistenceService());
    }

    public void setTitle(String title){
        this.title.setValue(title);
    }

    public BaseMazeViewModel getBaseMazeViewModel() {
        return (BaseMazeViewModel) this.mazeViewModel.get();
    }

    @Override
    public ObservableValue<String> titleProperty(){
        return title;
    }

    @Override
    public MazeViewModel getMazeViewModel() {
        return mazeViewModel.get();
    }

    @Override
    public ObservableValue<MazeViewModel> mazeViewModelProperty() {
        return mazeViewModel;
    }

    @Override
    public String getTitle(){
        return title.getValue();
    }

    @Override
    public int currentplayerIdProperty() {
        return this.currentPlayerId.get();
    }

    @Override
    public List<PlayerViewModel> getPlayersViewModel(){
        return this.playerViewModels;
    }

    @Override
    public ObservableValue<GameState> stateProperty() {
        return this.state;
    }

    @Override
    public void startGame() {
        if (game.getGameState() == GameState.SETUP) {
            game.init();
            mazeViewModel.set(new BaseMazeViewModel(game, this));
        }

        this.state.set(game.getGameState());

        if (game.getCurrentPlayer() != null) {
            this.currentPlayerId.set(game.getCurrentPlayer().getIndex());
        }

        game.startTurn();
        rebuildMazeViewModel();
        refreshPlayers();

        if(game.getCurrentPlayer() != null) {
            this.currentPlayerId.set(game.getCurrentPlayer().getIndex());
        }

        if (game.getGameState() == GameState.GAME_OVER) {
            showWinnerMessage();
        }
    }

    @Override
    public MenuViewModel getMenuViewModel() {
        return menuViewModel;
    }

    @Override
    public ObservableValue<Integer> currentPlayerProperty() {
        return currentPlayerId;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayerId.get();
    }

    @Override
    public UserProfilesViewModel getUserProfilesViewModel() {
        return userProfilesViewModel;
    }

    /**
     * Reconstruit le ViewModel du labyrinthe.
     *
     * Cette méthode est appelée après un changement important du plateau
     * pour forcer le rafraîchissement de l'affichage.
     */
    public void rebuildMazeViewModel() {
        mazeViewModel.set(new BaseMazeViewModel(game, this));

        state.set(game.getGameState());

        if (game.getCurrentPlayer() != null) {
            currentPlayerId.set(game.getCurrentPlayer().getIndex());
        }
    }

    /**
     * Affiche un message indiquant le joueur courant.
     */
    public void showCurrentPlayerMessage() {
        if (game.getCurrentPlayer() != null) {
            currentPlayerId.set(game.getCurrentPlayer().getIndex());

            services.getDialogService().publishMessage(
                    "C'est au tour de " + getPlayerLabel(game.getCurrentPlayer()) + "."
            );
        }
    }


    /**
     * Replace l'interface en mode configuration.
     *
     * Le labyrinthe est retiré de l'affichage et le joueur courant est réinitialisé.
     */
    public void goToSetup() {
        mazeViewModel.set(null);
        state.set(GameState.SETUP);
        currentPlayerId.set(-1);
    }

    /**
     * Construit le nom affichable d'un joueur.
     *
     * Les bots sont affichés comme "Bot X" et les joueurs humains
     * comme "Joueur X".
     */
    private String getPlayerLabel(labyrinth.model.Player player) {
        if (player.isBot()) {
            return "Bot " + (player.getIndex() + 1);
        }

        return "Joueur " + (player.getIndex() + 1);
    }

    /**
     * Rafraîchit tous les ViewModels des joueurs.
     *
     * Cette méthode est utile après un déplacement, une sauvegarde chargée
     * ou un tour automatique joué par un bot.
     */
    public void refreshPlayers() {
        for (PlayerViewModel playerViewModel : playerViewModels) {
            if (playerViewModel instanceof BasePlayerViewModel basePlayerViewModel) {
                basePlayerViewModel.refresh();
            }
        }
    }

    /**
     * Affiche le message de victoire.
     *
     * Le message est affiché seulement si le jeu est en GAME_OVER
     * et qu'un joueur courant existe.
     */
    public void showWinnerMessage() {
        if (game.getGameState() == GameState.GAME_OVER && game.getCurrentPlayer() != null) {
            services.getDialogService().publishMessage(
                    getPlayerLabel(game.getCurrentPlayer()) + " a gagné la partie !"
            );
        }
    }
}
