package labyrinth.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import labyrinth.viewmodel.MenuViewModel;

public class MenuView extends MenuBar {

    public MenuView(MenuViewModel viewModel){
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        MenuItem newItem = new MenuItem("New");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem aboutItem = new MenuItem("About");

        newItem.setOnAction(e -> viewModel.newGame());
        loadItem.setOnAction(e -> viewModel.load());
        saveItem.setOnAction(e -> viewModel.save());
        exitItem.setOnAction(e -> viewModel.exit());
        aboutItem.setOnAction(e -> viewModel.showAbout());


        fileMenu.getItems().addAll(newItem, loadItem, saveItem, exitItem);
        helpMenu.getItems().addAll(aboutItem);

        getMenus().addAll(fileMenu, helpMenu);

    }
}
