package labyrinth.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import labyrinth.model.GameState;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

public class BaseCellViewModel implements CellViewModel{
    private Position position;
    private BaseTileViewModel ViewModel;
    private SimpleObservableValue<Boolean> active = new SimpleObservableValue<>(false);
    private ObjectProperty<GameState> state;

    public BaseCellViewModel(Position position , BaseTileViewModel viewModel , ObjectProperty<GameState> state){
        this.position = position;
        this.ViewModel = viewModel;
        this.state = state;

        if (state.get() == GameState.PLAYER_MOVE){
            active.setValue(true);
        }else {
            active.setValue(false);
        }

        state.addListener((obs , oldVal , newVal) ->{
            if(newVal == GameState.PLAYER_MOVE){
                active.setValue(true);
            }else{
                active.setValue(false);
            }
        });

    }

    public BaseCellViewModel(Position position , ObjectProperty<GameState> state){
        this(position , new BaseTileViewModel() , state);
    }


    public BaseTileViewModel getBaseTileViewModel(){
        return ViewModel;
    }

    @Override
    public Position getPosition(){
        return this.position;
    }

    @Override
    public TileViewModel getTileViewModel(){
        return ViewModel;
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return active;
    }

    @Override
    public void movePlayer() {
        System.out.println("Mouvement du joueur vers : " + position);
    }

    @Override
    public boolean isActive() {
        return active.getValue();
    }
}
