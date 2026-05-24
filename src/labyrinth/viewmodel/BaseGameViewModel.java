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
        this.playerViewModels.add(new BasePlayerViewModel(game, 0, services.getSqlPersistenceService()));
        this.playerViewModels.add(new BasePlayerViewModel(game, 1,  services.getSqlPersistenceService()));
        this.playerViewModels.add(new BasePlayerViewModel(game, 2 ,  services.getSqlPersistenceService()));
        this.playerViewModels.add(new BasePlayerViewModel(game, 3, services.getSqlPersistenceService()));
        menuViewModel = new BaseMenuViewModel(game, services, this);
        this.userProfilesViewModel = new BaseUserProfilesViewModel(game , services.getSqlPersistenceService());
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

    public void rebuildMazeViewModel() {
        mazeViewModel.set(new BaseMazeViewModel(game, this));

        state.set(game.getGameState());

        if (game.getCurrentPlayer() != null) {
            currentPlayerId.set(game.getCurrentPlayer().getIndex());
        }
    }

    public void showCurrentPlayerMessage() {
        if (game.getCurrentPlayer() != null) {
            currentPlayerId.set(game.getCurrentPlayer().getIndex());

            services.getDialogService().publishMessage(
                    "C'est au tour du joueur " + game.getCurrentPlayer().getIndex() + "."
            );
        }
    }


    //fonction qui rend le jeu dans le mode setup
    public void goToSetup() {
        mazeViewModel.set(null);
        state.set(GameState.SETUP);
        currentPlayerId.set(-1);
    }
}
