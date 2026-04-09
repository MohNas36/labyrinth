package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import labyrinth.model.MazeMove;

public interface SlotViewModel {
    MazeMove getMazeMove();
    ObservableValue<Boolean> magnetizedProperty();
    ObservableValue<Boolean> forbiddenProperty();
    void magnetize();
    void demagnetize();
    void mazeMove();
    ObservableValue<Boolean> activeProperty();
    Boolean isActive();
    Boolean isForbidden();
    Boolean isMagnetized();
    TileViewModel getExtraTileViewModel();
}
