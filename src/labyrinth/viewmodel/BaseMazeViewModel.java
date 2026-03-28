package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Direction;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;
import labyrinth.model.TileKind;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseMazeViewModel implements MazeViewModel{
    private Grid<CellViewModel> cellViewModels;
    private BaseTileViewModel ExtraTile;
    private Map<MazeMove, SlotViewModel> slots;
    private ObjectProperty<MazeMove> forbiddenMove = new SimpleObjectProperty<>(null);
    private ObjectProperty<GameState> state;

    public BaseMazeViewModel(ObjectProperty<GameState> state){
        this.state = state;
        cellViewModels = new ArrayGrid<>(7 , 7);

        for(int x = 0 ; x < 7 ; x++){
            for (int y = 0 ; y < 7 ; y++){
                cellViewModels.set(x , y , new BaseCellViewModel(new Position( x , y ) , state));
            }
        }
/*
        cellViewModels.set(0 , 0 , new BaseCellViewModel(new Position(0 , 0) , new BaseTileViewModel(TileKind.ELBOW , Direction.RIGHT , 0 ,null , null , null ) , state));
        cellViewModels.set(0 , 6 , new BaseCellViewModel(new Position(0 , 6) , new BaseTileViewModel(TileKind.ELBOW , Direction.UP , 1 , null, null, null) , state));
        cellViewModels.set(6 , 0 , new BaseCellViewModel(new Position(6 , 0) , new BaseTileViewModel(TileKind.ELBOW , Direction.DOWN , 2 , null, null, null), state));
        cellViewModels.set(6, 6 , new BaseCellViewModel(new Position(6 , 6) , new BaseTileViewModel(TileKind.ELBOW , Direction.LEFT , 3 , null, null, null), state));

        cellViewModels.set(2 , 3 , new BaseCellViewModel(new Position(2 , 3) , new BaseTileViewModel(TileKind.T_CROSS , Direction.UP , null , 0, null, null ), state));
        cellViewModels.set(4 , 4 , new BaseCellViewModel(new Position(4, 4) , new BaseTileViewModel(TileKind.T_CROSS , Direction.UP , null , 1 ,null, null), state));
        cellViewModels.set(5 , 5 , new BaseCellViewModel(new Position(5 , 5) , new BaseTileViewModel(TileKind.T_CROSS , Direction.UP , null , 2 , null, null), state));
        cellViewModels.set(6  , 3 , new BaseCellViewModel(new Position(6 , 3) , new BaseTileViewModel(TileKind.T_CROSS , Direction.UP , null , 3 , null, null), state));

        ArrayList<Integer> listJoueurs = new ArrayList<>();
        listJoueurs.add(1);
        listJoueurs.add(2);
        listJoueurs.add(3);

        cellViewModels.set(1 , 1 , new BaseCellViewModel(new Position(1 , 1) , new BaseTileViewModel(TileKind.T_CROSS , Direction.DOWN , null, null , listJoueurs, 2), state));

*/

        ExtraTile = new BaseTileViewModel();

        slots = new HashMap<>();
        for(int i = 1 ; i < 7 ; i += 2){
            MazeMove up = new MazeMove(Direction.UP , i);
            MazeMove right = new MazeMove(Direction.RIGHT , i);
            MazeMove down = new MazeMove(Direction.DOWN , i);
            MazeMove left = new MazeMove(Direction.LEFT , i);

            slots.put(up , new BaseSlotViewModel(up , forbiddenMove , state , ExtraTile));
            slots.put(right , new BaseSlotViewModel(right, forbiddenMove , state , ExtraTile));
            slots.put(down, new BaseSlotViewModel(down, forbiddenMove, state , ExtraTile));
            slots.put(left , new BaseSlotViewModel(left , forbiddenMove, state , ExtraTile));

        }
    }

    @Override
    public Grid<CellViewModel> getCellViewModels(){
        return cellViewModels;
    }

    public void randomize(){
        TileKind[] kinds = TileKind.values();
        for(int x = 0; x < cellViewModels.getWidth(); x++){
            for(int y = 0 ; y < cellViewModels.getHeight(); y++){
                int randomDir = (int) (Math.random() * 4);
                int randomKind = (int) (Math.random() * 3);
                BaseCellViewModel currentCell = (BaseCellViewModel) cellViewModels.get(x , y);
                BaseTileViewModel currentTile = currentCell.getBaseTileViewModel();
                currentTile.rotate(randomDir);
                currentTile.setKind(kinds[randomKind]);
            }
        }
    }

    public void rotateAll(int nbTimes){
        for (int x = 0 ; x < cellViewModels.getWidth(); x++){
            for(int y = 0 ; y < cellViewModels.getHeight(); y++){
                BaseCellViewModel currentCell = (BaseCellViewModel) cellViewModels.get(x , y);
                BaseTileViewModel currentTile = currentCell.getBaseTileViewModel();
                currentTile.rotate(nbTimes);
            }
        }
    }

    @Override
    public TileViewModel getExtraTileViewModel(){
        return this.ExtraTile;
    }

    @Override
    public void rotateExtraTile(int nbTimes){
        this.ExtraTile.rotate(nbTimes);
    }

    @Override
    public Map<MazeMove , SlotViewModel> getSlotViewModels(){
        return slots;
    }

    @Override
    public ObjectProperty<MazeMove> forbiddenMoveProperty(){
        return this.forbiddenMove;
    }

    @Override
    public MazeMove getForbiddenMove() {
        return forbiddenMove.get();
    }

    @Override
    public GameState getGameState() {
        return state.get();
    }

    @Override
    public ObservableValue<GameState> stateProperty() {
        return state;
    }

}
