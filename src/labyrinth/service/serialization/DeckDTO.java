package labyrinth.service.serialization;

import labyrinth.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class DeckDTO {

    private List<Integer> drawPile;
    private List<Integer> discardPile;

    public DeckDTO(Deck deck){
        this.discardPile = new ArrayList<>(deck.getDiscardPile());
        this.drawPile = new ArrayList<>(deck.getDrawPile());
    }

    public List<Integer> getDiscardPile() {
        return discardPile;
    }

    public List<Integer> getDrawPile() {
        return drawPile;
    }
}
