package labyrinth.service.serialization;

import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.Player;
import labyrinth.model.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameDTO {

    private final int currentPlayerIndex;
    private final GameState gameState;
    private final List<PlayerDTO> players;
    private final MazeDTO maze;
    private UUID uuid;

    public GameDTO(Game game){
        this.currentPlayerIndex = game.getCurrentPlayer().getIndex();
        this.gameState = game.getGameState();
        this.uuid = game.getUuid();

        this.players = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            this.players.add(null);
        }
        for(int x = 0 ; x < game.getMaze().getTiles().getWidth() ; x++){
            for(int y = 0 ; y < game.getMaze().getTiles().getHeight() ; y++){
                Tile t = game.getMaze().getTiles().get(x , y);

                for(Player p : t.getPlayers()){
                    this.players.set(p.getIndex(), new PlayerDTO(p));
                }
            }
        }

        this.maze = new MazeDTO(game.getMaze());

    }

    public GameState getState() {
        return gameState;
    }

    public Integer getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public MazeDTO getMaze() {
        return maze;
    }

    public UUID getUuid() {
        return uuid;
    }
}
