package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.List;
import java.util.OptionalInt;

public interface PlayerViewModel {

    int getIndex();

    ObservableValue<Boolean> activeProperty();

    boolean isActive();

    ObservableValue<String> nameProperty();

    String getName();

    ObservableValue<Image> pictureProperty();

    Image pictureImage();

    ObservableValue<Boolean> currentPlayerProperty();

    boolean isCurrentPlayer();

    ObservableValue<Integer> nextTreasureProperty();

    OptionalInt getNextTreasure();

    ObservableList<Integer> foundTreasureProperty();

    List<Integer> getFoundTreasures();

    ObservableValue<Boolean> isLockedProperty();

    boolean isLocked();

    void addPlayer();

    void addBot();

    void removePlayer();


}


