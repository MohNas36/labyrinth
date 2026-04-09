package labyrinth.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import labyrinth.viewmodel.PlayerViewModel;

public class PlayerView extends VBox {
    //pour lr playerView je vais mettre des labels pour tout ce qui affichable sous formz de texte et un iamgeView pour l'image

    private Label name;
    private ImageView imageView;
    private Button addPlayer;
    private Button addBot;
    private Button removePlayer;

    public PlayerView(PlayerViewModel viewModel){
        name = new Label();
        imageView = new ImageView();
        addPlayer = new Button("Add a player");
        addBot = new Button("Add a bot");
        removePlayer = new Button("Remove a player");

        name.textProperty().bind(viewModel.nameProperty());

        imageView.imageProperty().bind(viewModel.pictureProperty());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        addPlayer.setOnAction(e -> viewModel.addPlayer());
        addBot.setOnAction(e -> viewModel.addBot());
        removePlayer.setOnAction(e-> viewModel.removePlayer());

        HBox players = new HBox(imageView , name); // un composant pour les joueurs
        VBox buttons = new VBox(addPlayer , addBot ,removePlayer);// un composant pour les buttons

        getChildren().addAll(players , buttons);
    }

}
