package labyrinth.view;

import labyrinth.viewmodel.CellViewModel;

public class CellView extends TileView{

    public CellView(CellViewModel viewModel){
        super(viewModel.getTileViewModel());

        setOnMouseClicked(e -> {
            if(viewModel.activeProperty().getValue()){
                viewModel.movePlayer();
            }
        });
    }

}
