package labyrinth.view;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import labyrinth.viewmodel.PlayerViewModel;
import labyrinth.viewmodel.UserProfilesViewModel;

import java.util.List;
import java.util.OptionalInt;

public class PlayerView extends VBox {
    //pour lr playerView je vais mettre des labels pour tout ce qui affichable sous formz de texte et un iamgeView pour l'image

    private Label name;
    private ImageView imageView;
    private Button removePlayer;

    private Label deckLabel;
    private Label discardLabel;

    public PlayerView(PlayerViewModel viewModel , UserProfilesViewModel userProfilesViewModel){
        name = new Label();
        imageView = new ImageView();
        removePlayer = new Button("Remove a player");
        deckLabel = new Label();
        discardLabel = new Label();

        name.textProperty().bind(viewModel.nameProperty());

        imageView.imageProperty().bind(viewModel.pictureProperty());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        AddPlayerButton addPlayerButton = new AddPlayerButton(viewModel, userProfilesViewModel);

        addPlayerButton.disableProperty().bind(viewModel.isLockedProperty());
        removePlayer.disableProperty().bind(viewModel.isLockedProperty());

        Runnable updateButtons = () -> {
            boolean active = viewModel.isActive();

            addPlayerButton.setVisible(!active);
            addPlayerButton.setManaged(!active);

            removePlayer.setVisible(active);
            removePlayer.setManaged(active);
        };

        viewModel.activeProperty().addListener((obs, oldVal, newVal) -> updateButtons.run());

        viewModel.nextTreasureProperty().addListener((obs, oldVal, newVal) -> updateTreasureLabels(viewModel));

        viewModel.foundTreasureProperty().addListener(
                (ListChangeListener<Integer>) change -> updateTreasureLabels(viewModel)
        );
        updateButtons.run();

        addPlayerButton.disableProperty().bind(viewModel.isLockedProperty());
        removePlayer.disableProperty().bind(viewModel.isLockedProperty());

        removePlayer.setOnAction(e-> viewModel.removePlayer());

        HBox players = new HBox(8, imageView, name);

        VBox treasures = new VBox(3, deckLabel, discardLabel);
        treasures.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");

        VBox buttons = new VBox(4, addPlayerButton, removePlayer);

        updateTreasureLabels(viewModel);

        getChildren().addAll(players, treasures, buttons);
    }

    private void updateTreasureLabels(PlayerViewModel viewModel) {
        if (!viewModel.isActive()) {
            deckLabel.setText("Deck : -");
            discardLabel.setText("Défausse : -");
            return;
        }

        OptionalInt nextTreasure = viewModel.getNextTreasure();

        if (nextTreasure.isPresent()) {
            deckLabel.setText("Deck : TR" + nextTreasure.getAsInt());
        } else {
            deckLabel.setText("Deck : vide");
        }

        List<Integer> foundTreasures = viewModel.getFoundTreasures();

        if (foundTreasures.isEmpty()) {
            discardLabel.setText("Défausse : vide");
        } else {
            discardLabel.setText("Défausse : " + formatTreasures(foundTreasures));
        }
    }

    private String formatTreasures(List<Integer> treasures) {
        String result = "";

        for (int i = 0; i < treasures.size(); i++) {
            result += "TR" + treasures.get(i);

            if (i < treasures.size() - 1) {
                result += ", ";
            }
        }

        return result;
    }

}
