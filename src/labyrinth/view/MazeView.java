package labyrinth.view;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import labyrinth.viewmodel.MazeViewModel;
import labyrinth.viewmodel.SlotViewModel;

public class MazeView extends StackPane {

    private BoardView boardView;
    private TileView extraTile;
    private int magnetizedCount = 0;

    public MazeView(MazeViewModel mazeViewModel){
        boardView = new BoardView(mazeViewModel);
        extraTile = new TileView(mazeViewModel.getExtraTileViewModel());
        TileView secondTile = new TileView(mazeViewModel.getExtraTileViewModel());

        extraTile.setPrefSize(80, 80);
        extraTile.setMaxSize(80 , 80);

        secondTile.setPrefSize(80 , 80);
        secondTile.setMaxSize(80, 80);


        StackPane stackPane1 = new StackPane();
        stackPane1.getChildren().addAll(boardView,  extraTile);
        stackPane1.setAlignment(extraTile , Pos.BOTTOM_LEFT);

        Pane mouse = new Pane();
        mouse.getChildren().addAll(secondTile);
        mouse.setMouseTransparent(true);


        setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();

            secondTile.relocate(x , y);
        });

        setOnMouseExited(event ->secondTile.setVisible(false));

        setFocusTraversable(true);

        setOnMouseEntered(e->{
            requestFocus();
            secondTile.setVisible(true);
        });

        setOnKeyPressed(e-> {
            if(e.getCode() == KeyCode.A) {
                mazeViewModel.rotateExtraTile(1);
            }
            if(e.getCode() == KeyCode.E) {
                mazeViewModel.rotateExtraTile(3);
            }
        });

        setOnScroll(event -> mazeViewModel.rotateExtraTile(1));

        //gérer les magnetize
        for(SlotViewModel slot : mazeViewModel.getSlotViewModels().values()){
            slot.magnetizedProperty().addListener((obs , oldValue , newValue) -> {
                if(!oldValue && newValue){
                    magnetizedCount++;
                } else if (oldValue && !newValue) {
                    magnetizedCount--;
                }

                secondTile.setVisible(magnetizedCount == 0);
            });
        }

        getChildren().addAll(stackPane1 , mouse);
    }
}
