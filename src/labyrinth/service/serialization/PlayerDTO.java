package labyrinth.service.serialization;

import labyrinth.model.Player;

import java.util.UUID;

public class PlayerDTO {

    private int index;
    private boolean isBot;
    private DeckDTO deck;
    private UUID uuid;

    public PlayerDTO(Player player){
        this.index = player.getIndex();
        this.isBot = player.isBot();
        this.deck = new DeckDTO(player.getDeck());
        this.uuid = player.getUuid();
    }

    public DeckDTO getDeck() {
        return deck;
    }

    public boolean isBot() {
        return isBot;
    }

    public int getIndex() {
        return index;
    }
    public UUID getUuid() {return uuid;}
}
