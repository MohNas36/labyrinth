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



    public DevPanel(StubGameViewModel stubGameViewModel , StubTileViewModel stubTileViewModel , StubCellViewModel stubCellViewModel , StubMenuViewModel stubMenuViewModel){
        this.text = new TextField();
        this.titre = new Button("Changer le titre");
        this.rotateAll = new Button("Rotate all");
        this.randomize = new Button("Randomize");
        this.mazeView = new MazeView(stubGameViewModel.getStubMazeViewModel());
        this.gameView = new GameView(stubGameViewModel);
        this.mainView = new MainView(stubGameViewModel , stubMenuViewModel );

        titre.setOnAction(event -> stubGameViewModel.setTitle(text.getText()));

        this.randomize.setOnAction(event -> stubGameViewModel.getStubMazeViewModel().randomize());
        this.rotateAll.setOnAction(event -> stubGameViewModel.getStubMazeViewModel().rotateAll(1));

        this.getChildren().addAll(mainView/*text, titre, randomize , rotateAll , */);

    }

}
