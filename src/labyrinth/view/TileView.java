package labyrinth.view;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;
import labyrinth.viewmodel.TileViewModel;

public class TileView extends StackPane {

    private ImageView imageView;
    private Label startingPositionLabel;
    private Label treasureLabel;
    private Label playersLabel;
    private Label currentPlayerLabel;
    private final String SP_STYLE = "-fx-background-color: red; -fx-border-color: black; -fx-font-size: 12"; // rouge pour la position de depart
    private final String TREASURE_STYLE = "-fx-background-color: lightgreen; -fx-text-fill: black; -fx-border-color: black; -fx-font-size: 12";
    private final String PLAYERS_STYLE = "-fx-background-color: yellow; -fx-border-color: black; -fx-font-size: 12";// jaune pour les joueurs
    private final String CURRENT_STYLE = "-fx-background-color: white; -fx-border-color: black; -fx-font-size: 12";// vert pour le joueur courant


    public TileView(TileViewModel tileViewModel){

        imageView = new ImageView();
        startingPositionLabel = new Label();
        treasureLabel = new Label();
        playersLabel = new Label();
        currentPlayerLabel = new Label();

        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        tileViewModel.kindProperty().addListener(((observableValue, tileKind, t1) -> {updateImage(tileViewModel);}));
        tileViewModel.directionProperty().addListener((observable , val1 , val2)->{updateImage(tileViewModel);});

        tileViewModel.startingPositionProperty().addListener((obs, oldVal, newVal) -> {
            updateStartingPosition(tileViewModel);
        });

        tileViewModel.treasureProperty().addListener((obs, oldVal, newVal) -> {
            updateTreasure(tileViewModel);
        });

        tileViewModel.playersProperty().addListener((ListChangeListener<Integer>) val-> {
            updatePlayers(tileViewModel);
        });

        tileViewModel.currentPlayerProperty().addListener((obs, oldVal , newVal)->{updateCurrent(tileViewModel);});

        startingPositionLabel.setMouseTransparent(true);
        treasureLabel.setMouseTransparent(true);
        startingPositionLabel.setVisible(false);
        treasureLabel.setVisible(false);
        playersLabel.setMouseTransparent(true);
        playersLabel.setVisible(false);
        currentPlayerLabel.setMouseTransparent(true);
        currentPlayerLabel.setVisible(false);

        startingPositionLabel.setStyle(SP_STYLE);
        treasureLabel.setStyle(TREASURE_STYLE);
        playersLabel.setStyle(PLAYERS_STYLE);
        currentPlayerLabel.setStyle(CURRENT_STYLE);


        setMinHeight(40);
        setMinWidth(40);

        setAlignment(startingPositionLabel , Pos.TOP_LEFT);
        setAlignment(treasureLabel , Pos.TOP_RIGHT);
        setAlignment(playersLabel , Pos.BOTTOM_CENTER);
        setAlignment(currentPlayerLabel, Pos.CENTER);

        getChildren().addAll(imageView , startingPositionLabel , treasureLabel , playersLabel, currentPlayerLabel);

        updateImage(tileViewModel);
        updatePlayers(tileViewModel);
        updateTreasure(tileViewModel);
        updateStartingPosition(tileViewModel);
        updateCurrent(tileViewModel);
    }

    /**
     * Met à jour l'image de la tuile.
     *
     * L'image dépend du type de tuile et de son orientation.
     */
    public void updateImage(TileViewModel tileViewModel){
        TileKind kind = tileViewModel.getKind();
        Direction direction = tileViewModel.getDirection();

        Image img = ImageManager.getINSTANCE().getTileImage(kind , direction);
        imageView.setImage(img);
    }

    /**
     * Met à jour l'affichage de la position de départ.
     *
     * Si la tuile contient une StartingPosition, son indice est affiché
     * dans le coin de la tuile.
     */
    public void updateStartingPosition(TileViewModel tileViewModel){
        if(tileViewModel.getStartingPosition().isPresent()){
            startingPositionLabel.setText("SP" + String.valueOf(tileViewModel.getStartingPosition().getAsInt()));
            startingPositionLabel.setVisible(true);
        }else{
            startingPositionLabel.setText("");
        }
    }

    /**
     * Met à jour l'affichage du trésor.
     *
     * Si la tuile contient un trésor, son identifiant est affiché
     * sous forme de label vert.
     */
    public void updateTreasure(TileViewModel tileViewModel){
        if(tileViewModel.getTreasure().isPresent()){
            treasureLabel.setText("TR" + String.valueOf(tileViewModel.getTreasure().getAsInt()));
            treasureLabel.setVisible(true);
        }else{
            treasureLabel.setText("");
        }
    }

    /**
     * Met à jour la liste des joueurs présents sur la tuile.
     *
     * Les indices affichés correspondent aux indices internes du modèle,
     * donc ils commencent à 0.
     */
    public void updatePlayers(TileViewModel tileViewModel){
        if(tileViewModel.getPlayers().isEmpty()){
            playersLabel.setText("");
        }else{
            playersLabel.setText("P(" + tileViewModel.getPlayers().toString() + ")");
            playersLabel.setVisible(true);
        }
    }

    /**
     * Met à jour l'indicateur du joueur courant.
     *
     * Si le joueur courant se trouve sur cette tuile, un label blanc
     * est affiché au centre de la tuile.
     */
    public void updateCurrent(TileViewModel tileViewModel){
        if(tileViewModel.getPlayers().contains(tileViewModel.getCurrentPlayer())){
            currentPlayerLabel.setText(String.valueOf("C" + tileViewModel.getCurrentPlayer()));
            currentPlayerLabel.setVisible(true);
        }else{
            currentPlayerLabel.setVisible(false);
        }
    }

}
