package labyrinth.model;

import labyrinth.service.serialization.TileDTO;
import labyrinth.util.observable.ObservableList;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableList;
import labyrinth.util.observable.SimpleObservableValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tile{
    private final TileKind kind;
    private final Content content;
    private final SimpleObservableValue<Direction> direction;
    private final SimpleObservableList<Player> players = new SimpleObservableList<>();

    public Tile(TileKind kind) {
        this(kind, new None(), Direction.UP);
    }

    public Tile(TileKind kind, Content content) {
        this(kind, content, Direction.UP);
    }

    public Tile(TileKind kind, Content content, Direction direction) {
        this.kind = kind;
        this.content = content;
        this.direction = new SimpleObservableValue<>(direction);
    }

    public TileKind getKind() {
        return kind;
    }

    public Content getContent() {
        return content;
    }

    public ObservableValue<Direction> orientationProperty(){
        return direction.asReadOnly();
    }

    public Direction getOrientation() {
        return direction.getValue();
    }

    public Set<Direction> getExits() {
        Set<Direction> exits = new HashSet<Direction>();
        for (Direction d : kind.getExits()) {
            exits.add(d.getRotated(direction.getValue()));
        }
        return exits;
    }

    public ObservableList<Player> playersProperty(){
        return players.asReadOnly();
    }

    public List<Player> getPlayers() {
        return players.asReadOnly();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public void rotate(int nbTimes){
        this.direction.setValue(direction.getValue().getRotated(nbTimes));
    }

    public static Tile from(TileDTO tileDTO, List<Player> players) {
        Tile tile = new Tile(tileDTO.getKind(), tileDTO.getContent(), tileDTO.getOrientation());

        if(tileDTO.getPlayers() != null) {
            for(Integer playerIndex : tileDTO.getPlayers()) {
                Player player = players.get(playerIndex);

                if(player != null){
                    tile.addPlayer(player);
                }
            }
        }

        return tile;
    }
}
