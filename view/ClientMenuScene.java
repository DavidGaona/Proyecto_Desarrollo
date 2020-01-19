package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ClientMenuScene {

    private EditMenu editMenu;

    public ClientMenuScene(double percentage, double buttonFont) {
        editMenu = new EditMenu(percentage, buttonFont);
    }

    public Scene renderScene(double width, double height) {
        Scene mainMenuClient;
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(0, 0, 0, 0));
        HBox hBoxTop = editMenu.topBar(width, height);
        HBox hBoxBot = editMenu.botBar(width, height);
        ScrollPane spCenter = editMenu.centerScrollPane(width, height);

        mainLayout.setBottom(hBoxBot);
        mainLayout.setTop(hBoxTop);
        mainLayout.setCenter(spCenter);

        mainMenuClient = new Scene(mainLayout, width, height);
        mainMenuClient.getStylesheets().add("styles.css");

        return mainMenuClient;
    }
}
