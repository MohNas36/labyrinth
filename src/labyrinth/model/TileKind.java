package labyrinth.model;

import java.util.Collections;
import java.util.Set;

public enum TileKind {
    CORRIDOR,
    ELBOW,
    T_CROSS;

    private final Set<Direction> exits;


    TileKind(Direction... exits){
        this.exits = Set.of(exits);
    }

    public Set<Direction> getExits(){
        Set<Direction> exits = Collections.unmodifiableSet(this.exits);
        return exits;
    }
}
