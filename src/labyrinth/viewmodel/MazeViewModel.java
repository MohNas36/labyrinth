package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;
import labyrinth.util.collection.Grid;

import java.util.Map;

public interface MazeViewModel {

    Grid<CellViewModel> getCellViewModels();
    TileViewModel getExtraTileViewModel();
    void rotateExtraTile(int nbTimes);
    Map<MazeMove , SlotViewModel> getSlotViewModels();
    ObjectProperty<MazeMove> forbiddenMoveProperty();
    MazeMove getForbiddenMove();
    GameState getGameState();
    ObservableValue<GameState> stateProperty();

}
