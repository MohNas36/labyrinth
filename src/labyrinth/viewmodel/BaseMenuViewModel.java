package labyrinth.viewmodel;

import javafx.application.Platform;

public class BaseMenuViewModel implements MenuViewModel{
    @Override
    public void newGame() {
        System.out.println("New game");
    }

    @Override
    public void load() {
        System.out.println("Load");
    }

    @Override
    public void save() {
        System.out.println("Save");
    }

    @Override
    public void exit() {
        Platform.exit();
    }
}
