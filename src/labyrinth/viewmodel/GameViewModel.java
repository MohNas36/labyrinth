package labyrinth.viewmodel;


import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;

import java.util.List;

public interface GameViewModel {
   public ObservableValue<String> titleProperty();
   ObservableValue<MazeViewModel> mazeViewModelProperty();
   public MazeViewModel getMazeViewModel();
   public String getTitle();
   public int currentplayerIdProperty();
   List<PlayerViewModel> getPlayersViewModel();
   ObservableValue<GameState> stateProperty();
   void startGame();
   MenuViewModel getMenuViewModel();
   ObservableValue<Integer> currentPlayerProperty();
   int getCurrentPlayer();
   UserProfilesViewModel getUserProfilesViewModel();
}
