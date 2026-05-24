package labyrinth.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import labyrinth.viewmodel.PlayerViewModel;
import labyrinth.viewmodel.UserProfilesViewModel;

public class PlayerView extends VBox {
    //pour lr playerView je vais mettre des labels pour tout ce qui affichable sous formz de texte et un iamgeView pour l'image

    private Label name;
    private ImageView imageView;
    private Button addPlayer;
    private Button addBot;
    private Button removePlayer;

    public PlayerView(PlayerViewModel viewModel , UserProfilesViewModel userProfilesViewModel){
        name = new Label();
        imageView = new ImageView();
        removePlayer = new Button("Remove a player");

        name.textProperty().bind(viewModel.nameProperty());

        imageView.imageProperty().bind(viewModel.pictureProperty());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        AddPlayerButton addPlayerButton = new AddPlayerButton(viewModel, userProfilesViewModel);

        removePlayer.setOnAction(e-> viewModel.removePlayer());

        HBox players = new HBox(imageView , name); // un composant pour les joueurs
        VBox buttons = new VBox(addPlayerButton, removePlayer);// un composant pour les buttons

        getChildren().addAll(players , buttons);
    }

}
