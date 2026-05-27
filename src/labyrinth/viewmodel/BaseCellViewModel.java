package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.Tile;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

public class BaseCellViewModel implements CellViewModel{
    private Game game;
    private BaseGameViewModel gameViewModel;
    private Tile tile;
    private Position position;
    private BaseTileViewModel ViewModel;
    private SimpleObservableValue<Boolean> active = new SimpleObservableValue<>(false);
    private ObjectProperty<GameState> state;

    public BaseCellViewModel(Position position, Game game, Tile tile, BaseGameViewModel gameViewModel) {
        this.game = game;
        this.gameViewModel = gameViewModel;
        this.tile = tile;
        this.position = position;
        this.state = new SimpleObjectProperty<>(game.getState());
        this.ViewModel = new BaseTileViewModel(tile, game);

        if (state.get() == GameState.PLAYER_MOVE){
            active.setValue(true);
        }else {
            active.setValue(false);
        }

        game.stateProperty().addListener((obs , oldVal , newVal) ->{
            if(newVal == GameState.PLAYER_MOVE){
                active.setValue(true);
            }else{
                active.setValue(false);
            }
        });

    }

    public BaseTileViewModel getBaseTileViewModel(){
        return ViewModel;
    }

    @Override
    public Position getPosition(){
        return this.position;
    }

    @Override
    public TileViewModel getTileViewModel(){
        return ViewModel;
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return active;
    }

    @Override
    public void movePlayer() {
        Tile start = game.getMaze().getTile(game.getCurrentPlayer());
        Tile target = game.getMaze().getTiles().get(position.x(), position.y());

        System.out.println("Joueur courant : " + game.getCurrentPlayer().getIndex());
        System.out.println("Position depart : " + game.getMaze().getPosition(start));
        System.out.println("Position cible : " + position);

        System.out.println("Sorties depart : " + start.getExits());
        System.out.println("Sorties cible : " + target.getExits());

        System.out.println("Accessible ? " + game.getPaths().isAccessible(target));
        System.out.println("Chemin : " + game.getPaths().getPath(target));

        game.movePlayer(target);

        if (game.getGameState() == GameState.TURN_END) {
            game.endTurn();
            gameViewModel.rebuildMazeViewModel();
            gameViewModel.showCurrentPlayerMessage();
            gameViewModel.refreshPlayers();
        }

        System.out.println("Mouvement du joueur vers : " + position);
    }

    @Override
    public boolean isActive() {
        return active.getValue();
    }
}
