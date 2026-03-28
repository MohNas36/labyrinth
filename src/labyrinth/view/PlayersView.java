package labyrinth.view;

import javafx.scene.layout.VBox;
import labyrinth.viewmodel.GameViewModel;
import labyrinth.viewmodel.PlayerViewModel;

public class PlayersView extends VBox {

    public PlayersView(GameViewModel viewModel){
        setSpacing(20);

        for(PlayerViewModel playerViewModel : viewModel.getPlayersViewModel()){
            getChildren().add(new PlayerView(playerViewModel));
        }
    }

}
