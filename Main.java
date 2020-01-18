import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import messages.AlertBox;
import messages.ConfirmBox;
import model.Client;
import connection.DbManager;
import view.DaoClient;

import javax.swing.text.Utilities;
import java.awt.*;


public class Main extends Application {

    private DaoClient client = new DaoClient();
    private int currentState = 0;

    @Override
    public void start(Stage window) {
        //resolutions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double percentageWidth = (1920 - screenSize.getWidth()) / 1920;
        double percentageHeight = (1080 - screenSize.getHeight()) / 1080;
        double percentage = Math.max(percentageWidth, percentageHeight);
        double buttonFont = 22 - (22 * percentage);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = gd.getDefaultConfiguration();
        java.awt.Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        int taskBarSize = scnMax.bottom;
        int width = (int) screenSize.getWidth(); //2560 1920 1280 1152 1024; 768 40
        int height = (int) screenSize.getHeight();//1440 1080 720 648 576; 432 40

        ClientMenu clientMenu = new ClientMenu(client, percentage, buttonFont);
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

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(window);
        });

        window.setScene(mainMenuClient);
        window.setTitle("UwU");
        window.setResizable(false);
        window.show();
    }

    private void closeProgram(Stage window) {
        if (ConfirmBox.display("Cerrar Programa", "¿ Quieres cerrar el programa ?")) {
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
