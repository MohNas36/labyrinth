package labyrinth.model;

import labyrinth.service.serialization.PlayerDTO;

import java.util.UUID;

public class Player {
    private int index;
    private Deck deck;
    private UUID uuid;

    public Player(int index , UUID uuid) {
        this.index = index;
        this.deck = new Deck();
        this.uuid = uuid;
    }
    public Player(int index) {
        this(index , null);
    }


    public UUID getUuid() {
        return uuid;
    }

    public void updateFrom(PlayerDTO player) {
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
