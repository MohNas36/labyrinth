package labyrinth.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import labyrinth.model.GameState;
import labyrinth.util.collection.Position;

public class BaseCellViewModel implements CellViewModel{
    private Position position;
    private BaseTileViewModel ViewModel;
    private BooleanProperty active = new SimpleBooleanProperty(false);
    private ObjectProperty<GameState> state;

    public BaseCellViewModel(Position position , BaseTileViewModel viewModel , ObjectProperty<GameState> state){
        this.position = position;
        this.ViewModel = viewModel;
        this.state = state;

        if (state.get() == GameState.PLAYER_MOVE){
            active.set(true);
        }else {
            active.set(false);
        }

        state.addListener((obs , oldVal , newVal) ->{
            if(newVal == GameState.PLAYER_MOVE){
                active.set(true);
            }else{
                active.set(false);
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
    public BooleanProperty activeProperty() {
        return active;
    }

    @Override
    public void movePlayer() {
        System.out.println("Mouvement du joueur vers : " + position);
    }
}
