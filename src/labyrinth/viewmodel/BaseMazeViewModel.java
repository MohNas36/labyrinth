package labyrinth.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Direction;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;
import labyrinth.model.Tile;
import labyrinth.model.TileKind;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;

import java.util.HashMap;
import java.util.Map;

public class BaseMazeViewModel implements MazeViewModel {

    private final Game game;
    private Grid<CellViewModel> cellViewModels;
    private BaseTileViewModel extraTile;
    private Map<MazeMove, SlotViewModel> slots;

    private final ObjectProperty<MazeMove> forbiddenMove = new SimpleObjectProperty<>(null);
    private final ObjectProperty<GameState> state = new SimpleObjectProperty<>(GameState.SETUP);

    public BaseMazeViewModel(Game game, BaseGameViewModel gameViewModel) {
        this.game = game;
        this.state.set(game.getState());

        game.stateProperty().addListener((obs, oldState, newState) -> {
            this.state.set(newState);
        });

        Grid<Tile> grid = game.getMaze().getTiles();
        this.cellViewModels = new ArrayGrid<>(grid.getWidth(), grid.getHeight());

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile = grid.get(x, y);
                this.cellViewModels.set(x, y, new BaseCellViewModel(new Position(x, y), game, tile, gameViewModel));
            }
        }

        this.extraTile = new BaseTileViewModel(game.getMaze().getExtraTile(), game);

        this.forbiddenMove.set(game.getMaze().getForbiddenMove());

        game.getMaze().forbiddenMoveProperty().addListener((obs, oldForbiddenMove, newForbiddenMove) -> {
            this.forbiddenMove.set(newForbiddenMove);
        });

        this.slots = new HashMap<>();

        for (int i = 1; i < grid.getWidth(); i += 2) {
            MazeMove topSlot = new MazeMove(Direction.UP, i);
            MazeMove rightSlot = new MazeMove(Direction.RIGHT, i);
            MazeMove bottomSlot = new MazeMove(Direction.DOWN, i);
            MazeMove leftSlot = new MazeMove(Direction.LEFT, i);

            MazeMove topAction = new MazeMove(Direction.DOWN, i);
            MazeMove rightAction = new MazeMove(Direction.LEFT, i);
            MazeMove bottomAction = new MazeMove(Direction.UP, i);
            MazeMove leftAction = new MazeMove(Direction.RIGHT, i);

            slots.put(topSlot, new BaseSlotViewModel(game, topAction, forbiddenMove, state, extraTile, gameViewModel));
            slots.put(rightSlot, new BaseSlotViewModel(game, rightAction, forbiddenMove, state, extraTile, gameViewModel));
            slots.put(bottomSlot, new BaseSlotViewModel(game, bottomAction, forbiddenMove, state, extraTile, gameViewModel));
            slots.put(leftSlot, new BaseSlotViewModel(game, leftAction, forbiddenMove, state, extraTile, gameViewModel));
        }
    }

    @Override
    public Grid<CellViewModel> getCellViewModels() {
        return cellViewModels;
    }

    public void randomize() {
        TileKind[] kinds = TileKind.values();

        for (int x = 0; x < cellViewModels.getWidth(); x++) {
            for (int y = 0; y < cellViewModels.getHeight(); y++) {
                int randomDir = (int) (Math.random() * 4);
                int randomKind = (int) (Math.random() * kinds.length);

                BaseCellViewModel currentCell = (BaseCellViewModel) cellViewModels.get(x, y);
                BaseTileViewModel currentTile = currentCell.getBaseTileViewModel();

                currentTile.rotate(randomDir);
                currentTile.setKind(kinds[randomKind]);
            }
        }
    }

    public void rotateAll(int nbTimes) {
        for (int x = 0; x < cellViewModels.getWidth(); x++) {
            for (int y = 0; y < cellViewModels.getHeight(); y++) {
                BaseCellViewModel currentCell = (BaseCellViewModel) cellViewModels.get(x, y);
                BaseTileViewModel currentTile = currentCell.getBaseTileViewModel();

                currentTile.rotate(nbTimes);
            }
        }
    }

    @Override
    public TileViewModel getExtraTileViewModel() {
        return extraTile;
    }

    @Override
    public void rotateExtraTile(int nbTimes) {
        extraTile.rotate(nbTimes);
    }

    @Override
    public Map<MazeMove, SlotViewModel> getSlotViewModels() {
        return slots;
    }

    @Override
    public ObservableValue<MazeMove> forbiddenMoveProperty() {
        return forbiddenMove;
    }

    @Override
    public MazeMove getForbiddenMove() {
        return forbiddenMove.get();
    }

    @Override
    public GameState getGameState() {
        return state.get();
    }

    @Override
    public ObservableValue<GameState> stateProperty() {
        return state;
    }
}