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

        if(tile.getContent() instanceof None){
            this.content = null;
        }else{
            this.content = tile.getContent();
        }


        this.direction = tile.getOrientation();
        this.kind = tile.getKind();
    }

    public Content getContent() {
        if(content == null){
            return new None();
        }
        return content;
    }

    public Direction getOrientation() {
        return direction;
    }

    public TileKind getTileKind() {
        return kind;
    }

    public List<Integer> getPlayers() {
        return players;
    }
}
