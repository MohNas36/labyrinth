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
    private MazeViewModel mazeViewModel;
    private Property<String> title = new SimpleObjectProperty<>("Labyrinthe");
    private ObjectProperty<Integer> currentPlayerId = new SimpleObjectProperty(0);
    private List<PlayerViewModel> playerViewModels;
    private ObjectProperty<GameState> state = new SimpleObjectProperty<>(GameState.SETUP);
    private MenuViewModel menuViewModel;
    private Services services;
    private final Game game;

    public BaseGameViewModel(Game game, Services services) {
        this.game = game;
        this.services = services;
        this.playerViewModels = new ArrayList<>();
        mazeViewModel = new BaseMazeViewModel(game);
        this.playerViewModels.add(new BasePlayerViewModel(game, 0));
        this.playerViewModels.add(new BasePlayerViewModel(game, 1));
        this.playerViewModels.add(new BasePlayerViewModel(game, 2));
        this.playerViewModels.add(new BasePlayerViewModel(game, 3));
        menuViewModel = new BaseMenuViewModel();
    }

    public void setTitle(String title){
        this.title.setValue(title);
    }

    public BaseMazeViewModel getBaseMazeViewModel(){
        return (BaseMazeViewModel) this.mazeViewModel;
    }

    @Override
    public ObservableValue<String> titleProperty(){
        return title;
    }

    @Override
    public MazeViewModel getMazeViewModel(){
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
        if (state.get() == GameState.SETUP) {
            game.init();
            this.state.set(game.getState());
            this.currentPlayerId.set(game.getCurrentPlayer().getIndex());

            services.getDialogService().publishMessage("Debut de la partie. Le joueur " + game.getCurrentPlayer().getIndex() + " commence");
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
}
