package labyrinth.model;

import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

import java.util.UUID;

public class Slot {
    private int index;
    private SimpleObservableValue<Player> p;
    private Game game;

    public Slot(Game game, int index){
        this.game = game;
        this.index = index;
        this.p = new SimpleObservableValue<>(null);
    }

    public int getIndex(){
        return index;
    }

    public Player getPlayer(){
        return p.getValue();
    }

    public ObservableValue<Player> playerProperty(){
        return p.asReadOnly();
    }

    public void open() {
        open(null, false);
    }

    public void open(UUID uuid) {
        open(uuid, false);
    }

    public void open(boolean isBot) {
        open(null, isBot);
    }

    public void open(UUID uuid, boolean isBot) {
        if (game.getGameState() != GameState.SETUP) {
            throw new IllegalStateException("Le jeu n'est pas dans SETUP");
        }

        if (p.getValue() != null) {
            throw new IllegalStateException("Le slot contient déjà un joueur");
        }

        this.p.setValue(new Player(index, uuid, isBot));
    }

    public void close(){
        if(game.getGameState() != GameState.SETUP){
            throw new IllegalStateException("Le jeu n'est pas dans SETUP");
        }
        if(p.getValue() == null){
            throw new IllegalStateException("Le slot ne contient pas de joueur");
        }
        this.p.setValue(null);
    }
}
