package labyrinth.service.serialization;

import labyrinth.model.*;

import java.util.ArrayList;
import java.util.List;

public class TileDTO {

    private List<Integer> players;
    private Direction direction;
    private TileKind kind;
    private Content content;

    public TileDTO(Tile tile){
        this.players = new ArrayList<>();
        for(Player p : tile.getPlayers()){
            this.players.add(p.getIndex());
        }


        this.content = tile.getContent();
        this.direction = tile.getOrientation();
        this.kind = tile.getKind();
    }

    public Content getContent() {
        return content;
    }

    public Direction getOrientation() {
        return direction;
    }

    public TileKind getKind() {
        return kind;
    }
}
