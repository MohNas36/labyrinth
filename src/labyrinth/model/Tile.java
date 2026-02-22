package labyrinth.model;

import java.util.HashSet;
import java.util.Set;

public class Tile{
    private TileKind kind;
    private Content content;
    public Direction direction;

    public Tile(TileKind kind) {
        this(kind, new None(), Direction.UP);
    }

    public Tile(TileKind kind, Content content) {
        this.kind = kind;
        this.content = content;
        this.direction = Direction.UP;
    }

    public Tile(TileKind kind, Content content, Direction direction) {
        this.kind = kind;
        this.content = content;
        this.direction = direction;

    }

    public TileKind getKind() {
        return kind;
    }

    public Content getContent() {
        return content;
    }

    public Direction getOrientation() {
        return direction;
    }

    public Set<Direction> getExits() {
        Set<Direction> exits = new HashSet<Direction>();
        for (Direction d : kind.getExits()) {
            exits.add(d.getRotated(direction));
        }
        return exits;
    }
}
