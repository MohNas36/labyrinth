package labyrinth.model;

public class Player {
    private int index;
    private Deck deck;

    public Player(int index) {
        this.index = index;
        this.deck = new Deck();
    }

    public int getIndex() {
        return index;
    }

    public Deck getDeck() {
        return deck;
    }
}
