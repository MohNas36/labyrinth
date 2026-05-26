package labyrinth.model;

import labyrinth.service.serialization.PlayerDTO;

import java.util.UUID;

public class Player {
    private int index;
    private Deck deck;
    private boolean isBot;
    private UUID uuid;

    public Player(int index , UUID uuid, boolean isBot) {
        this.index = index;
        this.deck = new Deck();
        this.isBot = isBot;
        this.uuid = uuid;
    }

    public Player(int index) {
        this(index , null, false);
    }


    public UUID getUuid() {
        return uuid;
    }

    public void updateFrom(PlayerDTO player) {
        this.uuid = player.getUuid();
        this.isBot = player.isBot();
        deck.updateFrom(player.getDeck());
    }

    public int getIndex() {
        return index;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isBot() {
        return isBot;
    }

}
