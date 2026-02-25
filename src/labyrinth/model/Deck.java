package labyrinth.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    List<Integer> drawPile;
    List<Integer> discardPile;

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
}
