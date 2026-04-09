package labyrinth.service.serialization;

import labyrinth.model.Deck;

import java.util.List;

public class DeckDTO {

    private List<Integer> drawPile;
    private List<Integer> discardPile;

    public DeckDTO(Deck deck){
        this.discardPile = deck.getDiscardPile();
        this.drawPile = deck.getDrawPile();
    }

    public List<Integer> getDiscardPile() {
        return discardPile;
    }

    public List<Integer> getDrawPile() {
        return drawPile;
    }
}
