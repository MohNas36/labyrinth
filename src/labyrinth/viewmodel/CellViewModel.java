package labyrinth.viewmodel;


import javafx.beans.property.BooleanProperty;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.ObservableValue;

public interface CellViewModel {
    Position getPosition();
    TileViewModel getTileViewModel();
    ObservableValue<Boolean> activeProperty();
    void movePlayer();
    boolean isActive();

}
