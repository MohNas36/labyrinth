package labyrinth.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Maze maze;
    private List<Slot> slots;

    public Game() {
        this.maze = MazeBuilder.buildStandardMaze();
        this.slots = new ArrayList<>(4);
        for(int i = 0; i < 4; i++) {
            slots.add(new Slot(this, i));
        }
    }

    public Slot getSlot(int index) {
        for(Slot slot : slots) {
            if(slot.getIndex() == index){
                return slot;
            }
        }
        return null;
    }

    public Maze getMaze() {
        return maze;
    }

    public Tile getTile(Player player) {
        return maze.getTile(player);
    }
}
