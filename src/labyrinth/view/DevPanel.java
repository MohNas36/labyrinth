package labyrinth.view;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import labyrinth.viewmodel.*;

public class DevPanel extends VBox {
    private TextField text;
    private Button titre;
    private Button randomize;
    private Button rotateAll;
    private MazeView mazeView;
    private GameView gameView;
    private MainView mainView;



    public DevPanel(BaseGameViewModel BaseGameViewModel , BaseTileViewModel BaseTileViewModel , BaseCellViewModel BaseCellViewModel , BaseMenuViewModel BaseMenuViewModel){
        this.text = new TextField();
        this.titre = new Button("Changer le titre");
        this.rotateAll = new Button("Rotate all");
        this.randomize = new Button("Randomize");
        this.mazeView = new MazeView(BaseGameViewModel.getBaseMazeViewModel());
        this.gameView = new GameView(BaseGameViewModel);
        this.mainView = new MainView(BaseGameViewModel , BaseMenuViewModel );

        titre.setOnAction(event -> BaseGameViewModel.setTitle(text.getText()));

        this.randomize.setOnAction(event -> BaseGameViewModel.getBaseMazeViewModel().randomize());
        this.rotateAll.setOnAction(event -> BaseGameViewModel.getBaseMazeViewModel().rotateAll(1));

        this.getChildren().addAll(mainView/*text, titre, randomize , rotateAll , */);

    }

}
