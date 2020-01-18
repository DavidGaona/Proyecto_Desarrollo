package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ClientMenuScene {
    private ClientMenu clientMenu;

    public ClientMenuScene(double percentage, double buttonFont ){
        clientMenu = new ClientMenu(percentage,buttonFont);
    }

    public Scene renderScene(int width, int height,int taskBarSize){
        Scene mainMenuClient;
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(0, 0, 0, 0));
        HBox hBoxTop = clientMenu.topBar(width, height);
        HBox hBoxBot = clientMenu.botBar(width, height);
        ScrollPane spCenter = clientMenu.centerScrollPane(width, height);

        mainLayout.setBottom(hBoxBot);
        mainLayout.setTop(hBoxTop);
        mainLayout.setCenter(spCenter);

        mainMenuClient = new Scene(mainLayout, width, height - taskBarSize * 1.8);
        mainMenuClient.getStylesheets().add("styles.css");

        return mainMenuClient;
    }
}
