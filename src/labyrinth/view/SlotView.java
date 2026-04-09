package labyrinth.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import labyrinth.viewmodel.SlotViewModel;
import labyrinth.viewmodel.TileViewModel;

public class SlotView extends StackPane {

    private TileView tuile;
    private final SlotViewModel viewModel;
    private final ImageView forbiddenImage;

    public SlotView(SlotViewModel SlotViewModel , TileViewModel extraTileViewModel){
        this.tuile = new TileView(extraTileViewModel);
        forbiddenImage = new ImageView(new Image(getClass().getResourceAsStream("/img/forbidden.png")));
        this.viewModel = SlotViewModel;

        forbiddenImage.setManaged(false);
        forbiddenImage.setPreserveRatio(true);
        forbiddenImage.fitWidthProperty().bind(widthProperty());
        forbiddenImage.fitHeightProperty().bind(heightProperty());
        forbiddenImage.visibleProperty().bind(viewModel.forbiddenProperty());
        forbiddenImage.setMouseTransparent(true);

        tuile.setVisible(false);

        setOnMouseEntered(e-> {
            viewModel.magnetize();
            tuile.setVisible(true);
        });
        setOnMouseExited(e -> {
            viewModel.demagnetize();
            tuile.setVisible(false);
        });

        disableProperty().bind(viewModel.forbiddenProperty());


        setOnMouseClicked( e -> {
            if(!viewModel.forbiddenProperty().getValue()){
                viewModel.mazeMove(); // on déplace juste si le coup n'est pas interdit
            }
        });


        getChildren().addAll(tuile , forbiddenImage);
    }

}
