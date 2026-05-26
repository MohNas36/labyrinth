package labyrinth.viewmodel;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.Player;
import labyrinth.model.Slot;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.persistence.UserProfile;

import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

public class BasePlayerViewModel implements PlayerViewModel{
    private int index;
    private Game game;

    private BooleanProperty isActive;
    private StringProperty name;
    private ObjectProperty<Image> picture;
    private BooleanProperty currentPlayer;
    private ObjectProperty<Integer> nextTreasure;
    private ObservableList<Integer> foundTreasures;
    private BooleanProperty isLocked;
    private SqlPersistenceService persistenceService;

    public BasePlayerViewModel(Game game, int index , SqlPersistenceService persistenceService) {
        this.game = game;
        this.index = index;
        this.persistenceService = persistenceService;

        this.isActive = new SimpleBooleanProperty(false);
        this.name = new SimpleStringProperty("Libre");
        this.picture = new SimpleObjectProperty<>(null);
        this.currentPlayer = new SimpleBooleanProperty(false);
        this.nextTreasure = new SimpleObjectProperty<>(null);
        this.foundTreasures = FXCollections.observableArrayList();
        this.isLocked = new SimpleBooleanProperty(false);

        this.isLocked = new SimpleBooleanProperty(game.getGameState() != GameState.SETUP);

        refresh();

        game.stateProperty().addListener((obs, oldVal, newVal) -> {
            isLocked.set(newVal != GameState.SETUP);
            refresh();
        });

        game.currentPlayerProperty().addListener((obs, oldVal, newVal) -> {
            refreshPlayer();
        });

        persistenceService.addUserProfileChangedHandler(playerId -> {
            Slot slot = game.getSlots().get(index);

            if (slot.getPlayer() != null
                    && slot.getPlayer().getUuid() != null
                    && slot.getPlayer().getUuid().equals(playerId)) {
                refreshName();
            }
        });

    }

    private void refresh() {
        Slot slot = game.getSlots().get(index);

        isActive.set(slot.getPlayer() != null);

        refreshName();
        refreshPlayer();
        refreshTreasure();
    }

    private void refreshPlayer() {
        Player p = game.getCurrentPlayer();

        if (p == null) {
            currentPlayer.set(false);
            return;
        }

        currentPlayer.set(index == p.getIndex());
    }

    private void refreshTreasure() {
        foundTreasures.clear();

        Slot slot = game.getSlot(index);

        if (slot == null || slot.getPlayer() == null) {
            nextTreasure.set(null);
            return;
        }

        Player p = slot.getPlayer();

        if (p.getDeck().getDrawPile().isEmpty()) {
            nextTreasure.set(null);
        } else {
            nextTreasure.set(p.getDeck().getDrawPile().getFirst());
        }

        foundTreasures.addAll(p.getDeck().getDiscardPile());
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return isActive;
    }

    @Override
    public boolean isActive() {
        return isActive.get();
    }

    @Override
    public ObservableValue<String> nameProperty() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name.get();
    }

    @Override
    public ObservableValue<Image> pictureProperty() {
        return picture;
    }

    @Override
    public Image pictureImage() {
        return picture.get();
    }

    @Override
    public ObservableValue<Boolean> currentPlayerProperty() {
        return currentPlayer;
    }

    @Override
    public boolean isCurrentPlayer() {
        return currentPlayer.get();
    }

    @Override
    public ObservableValue<Integer> nextTreasureProperty() {
        return nextTreasure;
    }

    @Override
    public OptionalInt getNextTreasure() {
        Integer value = nextTreasure.get();

        if(value == null){
            return OptionalInt.empty();
        }

        return OptionalInt.of(value);
    }

    @Override
    public ObservableList<Integer> foundTreasureProperty() {
        return foundTreasures;
    }

    @Override
    public List<Integer> getFoundTreasures() {
        return List.copyOf(foundTreasures);
    }

    @Override
    public ObservableValue<Boolean> isLockedProperty() {
        return isLocked;
    }

    @Override
    public boolean isLocked() {
        return isLocked.get();
    }

    @Override
    public void addPlayer() {
        Slot slot = game.getSlots().get(index);

        if (slot.getPlayer() != null) {
            slot.close();
        }

        slot.open();
        refresh();
    }

    @Override
    public void addPlayer(UUID uuid) {
        Slot slot = game.getSlots().get(index);

        if (slot.getPlayer() != null) {
            slot.close();
        }

        slot.open(uuid);
        refresh();
    }

    @Override
    public void addBot() {
        isActive.set(true);
        name.set("Bot " + index);
    }

    @Override
    public void removePlayer() {
        Slot slot = game.getSlots().get(index);

        if (slot.getPlayer() != null) {
            slot.close();
        }

        refresh();
    }

    private void refreshName() {
        Slot slot = game.getSlots().get(index);

        if (slot.getPlayer() == null) {
            name.set("Libre");
            return;
        }

        Player player = slot.getPlayer();

        if (player.getUuid() == null) {
            name.set("Joueur " + (index + 1));
            return;
        }

        UserProfile profile = persistenceService.getUserProfile(player.getUuid());

        if (profile == null) {
            name.set("Joueur " + (index + 1));
        } else {
            name.set(profile.name());
        }
    }
}

