package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;

import java.util.List;
import java.util.OptionalInt;

public interface TileViewModel {
    ObservableValue<TileKind> kindProperty();
    TileKind getKind();
    ObservableValue<Direction> directionProperty();
    Direction getDirection();
    ObservableValue<Integer> startingPositionProperty();
    OptionalInt getStartingPosition();
    ObservableValue<Integer> treasureProperty();
    OptionalInt getTreasure();
    ObservableList<Integer> playersProperty();
    List<Integer> getPlayers();
    ObservableValue<Integer> currentPlayerProperty();
    int getCurrentPlayer();
}
