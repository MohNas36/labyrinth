package labyrinth.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Integer> drawPile;
    private List<Integer> discardPile;

    public Deck() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    public List<Integer> getDrawPile() {
        return drawPile;
    }

    public List<Integer> getDiscardPile() {
        return discardPile;
    }

    public void init(List<Integer> treasures){
        this.drawPile.clear();
        this.drawPile.addAll(treasures);
        this.discardPile.clear();
    }

    public void discard(){
        int carte = this.drawPile.removeFirst();
        this.discardPile.add(carte);

    }
}
