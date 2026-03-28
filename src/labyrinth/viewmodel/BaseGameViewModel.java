package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;

import java.util.ArrayList;
import java.util.List;

public class BaseGameViewModel implements GameViewModel {
    private MazeViewModel mazeViewModel;
    private Property<String> title = new SimpleObjectProperty<>("Labyrinthe");
    private ObjectProperty<Integer> currentPlayerId = new SimpleObjectProperty(0);
    private List<PlayerViewModel> playerViewModels;
    private ObjectProperty<GameState> state = new SimpleObjectProperty<>(GameState.SETUP);

    public BaseGameViewModel(){
        this.playerViewModels = new ArrayList<>();
        mazeViewModel = new BaseMazeViewModel(state);
        this.playerViewModels.add(new BasePlayerViewModel(0 , state));
        this.playerViewModels.add(new BasePlayerViewModel(1, state));
        this.playerViewModels.add(new BasePlayerViewModel(2, state));
        this.playerViewModels.add(new BasePlayerViewModel(3, state));
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
            this.state.set(GameState.MAZE_MOVE);
        }
    }
}
