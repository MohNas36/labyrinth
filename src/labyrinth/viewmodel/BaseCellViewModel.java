package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.Tile;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

public class BaseCellViewModel implements CellViewModel{
    private Game game;
    private Tile tile;
    private Position position;
    private BaseTileViewModel ViewModel;
    private SimpleObservableValue<Boolean> active = new SimpleObservableValue<>(false);
    private ObjectProperty<GameState> state;

    public BaseCellViewModel(Position position, Game game, Tile tile) {
        this.game = game;
        this.tile = tile;
        this.position = position;
        this.state = new SimpleObjectProperty<>(game.getState());
        this.ViewModel = new BaseTileViewModel(tile, game);

        if (state.get() == GameState.PLAYER_MOVE){
            active.setValue(true);
        }else {
            active.setValue(false);
        }

        game.stateProperty().addListener((obs , oldVal , newVal) ->{
            if(newVal == GameState.PLAYER_MOVE){
                active.setValue(true);
            }else{
                active.setValue(false);
            }
        });

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
