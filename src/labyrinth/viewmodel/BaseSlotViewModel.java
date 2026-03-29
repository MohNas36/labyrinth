package labyrinth.viewmodel;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;

public class BaseSlotViewModel implements SlotViewModel{

    private Game game;
    private MazeMove mazeMove;
    private Property<Boolean> magnetized = new SimpleObjectProperty<>(false);
    private BooleanProperty forbiddenMove = new SimpleBooleanProperty(false);
    private ObjectProperty<MazeMove> forbiddenMoveProperty;
    private BooleanProperty active = new SimpleBooleanProperty();
    private TileViewModel extraTileView;

    public BaseSlotViewModel(Game game, MazeMove mazeMove , ObjectProperty<MazeMove> forbiddenMoveProperty , ObjectProperty<GameState> state, BaseTileViewModel extraTileView ){
        this.game = game;
        this.mazeMove = mazeMove;
        this.forbiddenMoveProperty = forbiddenMoveProperty;
        this.extraTileView = extraTileView;
        this.forbiddenMove.set(mazeMove.equals(forbiddenMoveProperty.get()));

        forbiddenMoveProperty.addListener(((observable, oldMove , NewMove) ->{
            forbiddenMove.set(mazeMove.equals(NewMove));
            if(forbiddenMove.get()){
                demagnetize();
            }
        }));

        active.set(state.get() == GameState.MAZE_MOVE);

        state.addListener((obs , oldVal , newVal)-> {
            active.set(newVal == GameState.MAZE_MOVE);
            if(newVal != GameState.MAZE_MOVE){
                demagnetize();
            }
        });
    }

    @Override
    public MazeMove getMazeMove(){
        return this.mazeMove;
    }

    @Override
    public void magnetize(){
        if(!forbiddenMove.get()){
            magnetized.setValue(true);
        }
    }

    @Override
    public void demagnetize(){
        magnetized.setValue(false);
    }

    @Override
    public ObservableValue<Boolean> magnetizedProperty(){
        return magnetized;
    }

    @Override
    public ObservableValue<Boolean> forbiddenProperty(){
        return this.forbiddenMove;
    }

    @Override
    public void mazeMove(){
        game.mazeMove(mazeMove);
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return active;
    }

    @Override
    public Boolean isActive() {
        return active.get();
    }

    @Override
    public Boolean isForbidden() {
        return forbiddenMove.get();
    }

    @Override
    public Boolean isMagnetized() {
        return magnetized.getValue();
    }

    @Override
    public TileViewModel getExtraTileViewModel() {
        return extraTileView ;
    }
}
