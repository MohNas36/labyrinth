package labyrinth.viewmodel;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;

public class BaseSlotViewModel implements SlotViewModel{

    private MazeMove mazeMove;
    private Property<Boolean> magnetized = new SimpleObjectProperty<>(false);
    private BooleanProperty forbiddenMove = new SimpleBooleanProperty(false);
    private ObjectProperty<MazeMove> forbiddenMoveProperty;
    private BooleanProperty active = new SimpleBooleanProperty();

    public BaseSlotViewModel(MazeMove mazeMove , ObjectProperty<MazeMove> forbiddenMoveProperty , ObjectProperty<GameState> state){
        this.mazeMove = mazeMove;
        this.forbiddenMoveProperty = forbiddenMoveProperty;

        forbiddenMoveProperty.addListener(((observable, oldMove , NewMove) ->{
            forbiddenMove.set(mazeMove.equals(NewMove));
            if(forbiddenMove.get()){
                demagnetize();
            }
        }));

        if(state.get() == GameState.MAZE_MOVE){
            active.set(true);
        }else{
            active.set(false);
        }

        state.addListener((obs , oldVal , newVal)-> {
            if(newVal == GameState.MAZE_MOVE){
                active.set(true);
            }else{
                active.set(false);
            }
        });

        forbiddenMove.set(mazeMove.equals(forbiddenMoveProperty.get()));
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
        forbiddenMoveProperty.setValue(mazeMove.getOpposite());
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return active;
    }
}
