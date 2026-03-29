package labyrinth.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import labyrinth.model.*;

import java.util.List;
import java.util.OptionalInt;

public class BaseTileViewModel implements TileViewModel{
    private final Game game;
    private final Tile tile;

    private final Property<TileKind> kind;
    private final Property<Direction> direction;
    private final Property<Integer> startingPosition;
    private final Property<Integer> treasure;
    private final ObservableList<Integer> players;
    private final Property<Integer> currentPlayer;

    public BaseTileViewModel(Tile tile, Game game) {
        this.game = game;
        this.tile = tile;

        this.kind = new SimpleObjectProperty<>(this.tile.getKind());
        this.direction = new SimpleObjectProperty<>(this.tile.getOrientation());
        this.startingPosition = new SimpleObjectProperty<>(getStartPos(tile));
        this.treasure = new SimpleObjectProperty<>(getTrsr(tile));
        players = FXCollections.observableArrayList();
        currentPlayer = new SimpleObjectProperty<>(game.getCurrentPlayer().getIndex());

        players.clear();
        for(Player p : tile.getPlayers()) {
            players.add(p.getIndex());
        }

        tile.orientationProperty().addListener((observable, oldValue, newValue) -> {
            this.direction.setValue(newValue);
        });

        tile.playersProperty().addListener(observable -> {
            players.clear();
            for(Player p : tile.getPlayers()) {
                players.add(p.getIndex());
            }
        });

        game.currentPlayerProperty().addListener((obs, oldVal, newVal) -> {
            this.currentPlayer.setValue(game.getCurrentPlayer().getIndex());
        });

    }

    private Integer getStartPos(Tile tile) {
        if(tile.getContent() instanceof StartingPosition sp){
            return sp.index();
        }
        return null;
    }

    private Integer getTrsr(Tile tile) {
        if(tile.getContent() instanceof Treasure tr){
            return tr.id();
        }
        return null;
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
