package labyrinth.view;

import javafx.scene.layout.*;
import labyrinth.model.Direction;
import labyrinth.model.MazeMove;
import labyrinth.util.collection.Grid;
import labyrinth.viewmodel.CellViewModel;
import labyrinth.viewmodel.MazeViewModel;
import labyrinth.viewmodel.SlotViewModel;

import java.util.Map;


public class BoardView extends StackPane {

    private final GridPane gridPane;

    public BoardView(MazeViewModel mazeViewModel){

        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        ColumnConstraints columnConstraints4 = new ColumnConstraints();
        ColumnConstraints columnConstraints5 = new ColumnConstraints();
        ColumnConstraints columnConstraints6 = new ColumnConstraints();
        ColumnConstraints columnConstraints7 = new ColumnConstraints();
        ColumnConstraints columnConstraints8 = new ColumnConstraints();
        ColumnConstraints columnConstraints9 = new ColumnConstraints();

        RowConstraints rowConstraints1 = new RowConstraints();
        RowConstraints rowConstraints2 = new RowConstraints();
        RowConstraints rowConstraints3 = new RowConstraints();
        RowConstraints rowConstraints4 = new RowConstraints();
        RowConstraints rowConstraints5 = new RowConstraints();
        RowConstraints rowConstraints6 = new RowConstraints();
        RowConstraints rowConstraints7 = new RowConstraints();
        RowConstraints rowConstraints8 = new RowConstraints();
        RowConstraints rowConstraints9 = new RowConstraints();


        columnConstraints1.setHgrow(Priority.ALWAYS);
        columnConstraints2.setHgrow(Priority.ALWAYS);
        columnConstraints3.setHgrow(Priority.ALWAYS);
        columnConstraints4.setHgrow(Priority.ALWAYS);
        columnConstraints5.setHgrow(Priority.ALWAYS);
        columnConstraints6.setHgrow(Priority.ALWAYS);
        columnConstraints7.setHgrow(Priority.ALWAYS);
        columnConstraints8.setHgrow(Priority.ALWAYS);
        columnConstraints9.setHgrow(Priority.ALWAYS);

        rowConstraints1.setVgrow(Priority.ALWAYS);
        rowConstraints2.setVgrow(Priority.ALWAYS);
        rowConstraints3.setVgrow(Priority.ALWAYS);
        rowConstraints4.setVgrow(Priority.ALWAYS);
        rowConstraints5.setVgrow(Priority.ALWAYS);
        rowConstraints6.setVgrow(Priority.ALWAYS);
        rowConstraints7.setVgrow(Priority.ALWAYS);
        rowConstraints8.setVgrow(Priority.ALWAYS);
        rowConstraints9.setVgrow(Priority.ALWAYS);


        columnConstraints1.setPercentWidth(100.0 / 9);
        columnConstraints2.setPercentWidth(100.0 / 9);
        columnConstraints3.setPercentWidth(100.0 / 9);
        columnConstraints4.setPercentWidth(100.0 / 9);
        columnConstraints5.setPercentWidth(100.0 / 9);
        columnConstraints6.setPercentWidth(100.0 / 9);
        columnConstraints7.setPercentWidth(100.0 / 9);

        rowConstraints1.setPercentHeight(100.0 / 9);
        rowConstraints2.setPercentHeight(100.0 / 9);
        rowConstraints3.setPercentHeight(100.0 / 9);
        rowConstraints4.setPercentHeight(100.0 / 9);
        rowConstraints5.setPercentHeight(100.0 / 9);
        rowConstraints6.setPercentHeight(100.0 / 9);
        rowConstraints7.setPercentHeight(100.0 / 9);


        gridPane = new GridPane();
        Grid<CellViewModel> cells = mazeViewModel.getCellViewModels();

        widthProperty().addListener((observable , ancien , nouveau) ->{update();} );
        heightProperty().addListener((observable , ancien , nouveau) ->{update();} );

        for(int x = 0 ; x < cells.getWidth() ; x++){
            for(int y = 0; y < cells.getHeight() ; y++){
                CellViewModel cellViewModel = cells.get(x , y);
                CellView cellView = new CellView(cellViewModel);

                gridPane.add(cellView , x + 1 , y + 1);

            }
        }

        Map<MazeMove , SlotViewModel> slots = mazeViewModel.getSlotViewModels();

        for(Map.Entry<MazeMove , SlotViewModel> entry : slots.entrySet()){

            MazeMove mazeMove = entry.getKey();
            SlotViewModel viewModel = entry.getValue();

            SlotView slotView = new SlotView(viewModel , mazeViewModel.getExtraTileViewModel());

            if(mazeMove.direction() == Direction.UP){
                gridPane.add(slotView , mazeMove.index() + 1, 0);
            }
            if(mazeMove.direction() == Direction.RIGHT){
                gridPane.add(slotView, 8, mazeMove.index() + 1);
            }
            if(mazeMove.direction() == Direction.DOWN){
                gridPane.add(slotView , mazeMove.index() + 1, 8);
            }
            if(mazeMove.direction() == Direction.LEFT){
                gridPane.add(slotView ,0,  mazeMove.index() + 1);
            }
        }

        gridPane.getColumnConstraints().addAll(columnConstraints1 , columnConstraints2, columnConstraints3, columnConstraints4 , columnConstraints5 , columnConstraints6 , columnConstraints7);
        gridPane.getRowConstraints().addAll(rowConstraints1, rowConstraints2 , rowConstraints3 , rowConstraints4 , rowConstraints5 , rowConstraints6 , rowConstraints7);

        getChildren().add(gridPane);

    }

    private void update(){
        double taille = Math.min(getWidth() , getHeight());
        gridPane.setPrefSize(taille , taille);
    }
}
