package labyrinth.viewmodel;

import javafx.beans.property.BooleanProperty;
import labyrinth.util.collection.Position;

public interface CellViewModel {

    Position getPosition();
    TileViewModel getTileViewModel();
    BooleanProperty activeProperty();
    void movePlayer();

}
