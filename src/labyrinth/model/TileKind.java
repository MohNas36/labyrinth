package labyrinth.model;

import java.util.HashSet;
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
        Set<Direction> exits = new HashSet<Direction>();
        if(this.ordinal() == 0){
            exits.add(Direction.UP);
            exits.add(Direction.DOWN);
        }

        if(this.ordinal() == 1){
            exits.add(Direction.UP);
            exits.add(Direction.RIGHT);
        }

        if(this.ordinal() == 2){
            exits.add(Direction.UP);
            exits.add(Direction.RIGHT);
            exits.add(Direction.LEFT);
        }
        return exits;
    }
}
