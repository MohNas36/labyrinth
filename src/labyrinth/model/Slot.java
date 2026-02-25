package labyrinth.model;

public class Slot {
    private int index;
    private Player p;
    private Game game;

    public Slot(Game game, int index){
        this.game = game;
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public Player getPlayer(){
        return p;
    }
}
