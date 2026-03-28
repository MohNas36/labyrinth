package labyrinth.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;

import java.util.List;
import java.util.OptionalInt;

public class BaseTileViewModel implements TileViewModel{
    private final Property<TileKind> kind;
    private final Property<Direction> direction;
    private final Property<Integer> startingPosition;
    private final Property<Integer> treasure;
    private final ObservableList<Integer> players;
    private final Property<Integer> currentPlayer;
    public BaseTileViewModel(TileKind kind , Direction direction , Integer sp , Integer treasure , List<Integer> playersList , Integer current){
        this.kind = new SimpleObjectProperty<>(kind);
        this.direction = new SimpleObjectProperty<>(direction);
        this.startingPosition = new SimpleObjectProperty<>(sp);
        this.treasure = new SimpleObjectProperty<>(treasure);
        players = FXCollections.observableArrayList();
        if(playersList != null){
            this.players.addAll(playersList);
        }
        currentPlayer = new SimpleObjectProperty<>(current);
    }

    public BaseTileViewModel(){
        this(TileKind.CORRIDOR , Direction.UP , null , null , null, null);
    }

    public void setKind(TileKind kind) {
        this.kind.setValue(kind);
    }

    public void setDirection(Direction direction){
        this.direction.setValue(direction);
    }

    public void rotate(int nbTimes){
        setDirection(getDirection().getRotated(nbTimes));
    }

    @Override
    public ObservableValue<TileKind> kindProperty(){
        return kind;
    }

    @Override
    public TileKind getKind(){
        return kind.getValue();
    }

    @Override
    public ObservableValue<Direction> directionProperty(){
        return direction;
    }

    @Override
    public Direction getDirection(){
        return direction.getValue();
    }

    @Override
    public ObservableValue<Integer> startingPositionProperty() {
        return startingPosition;
    }

    @Override
    public OptionalInt getStartingPosition() {
        Integer value = startingPosition.getValue();

        if(value == null){
            return OptionalInt.empty();
        }

        return OptionalInt.of(value);
    }

    @Override
    public ObservableValue<Integer> treasureProperty() {
        return treasure;
    }

    @Override
    public OptionalInt getTreasure() {
        Integer value = treasure.getValue();

        if(value == null){
            return OptionalInt.empty();
        }

        return OptionalInt.of(value);
    }

    @Override
    public ObservableList<Integer> playersProperty() {
        return players;
    }

    @Override
    public List<Integer> getPlayers() {
        return List.copyOf(players);
    }

    @Override
    public ObservableValue<Integer> currentPlayerProperty() {
        return currentPlayer;
    }

    @Override
    public int getCurrentPlayer() {

        if(currentPlayer.getValue() == null){
            return -1;
        }

        return currentPlayer.getValue();
    }
}
