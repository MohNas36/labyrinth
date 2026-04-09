package labyrinth.service.serialization;

import labyrinth.model.Player;

public class PlayerDTO {

    private int index;
    private DeckDTO deck;

    public PlayerDTO(Player player){
        this.index = player.getIndex();
        this.deck = new DeckDTO(player.getDeck());
    }

    public DeckDTO getDeck() {
        return deck;
    }

    public int getIndex() {
        return index;
    }
}
