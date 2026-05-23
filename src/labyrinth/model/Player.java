package labyrinth.model;

import labyrinth.service.serialization.PlayerDTO;

import java.util.UUID;

public class Player {
    private int index;
    private Deck deck;
    private UUID uuid;

    public Player(int index) {
        this.index = index;
        this.deck = new Deck();
    }


    public UUID getUuid() {
        return uuid;
    }

    public void uodateFrom(PlayerDTO player) {
        this.uuid = player.getUuid();
        deck.updateFrom(player.getDeck());
    }

    public int getIndex() {
        return index;
    }

    public Deck getDeck() {
        return deck;
    }

}
