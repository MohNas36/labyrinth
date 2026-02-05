package labyrinth.model;

import java.util.HashSet;
import java.util.Set;

public class Tile implements Content{
    private TileKind kind;
    private Content content;
    public Direction direction;

    public Tile(TileKind kind) {
        this.kind = kind;
    }

    public Tile(TileKind kind, Content content) {
        this.kind = kind;
        this.content = content;
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
        return null;
    }
}
