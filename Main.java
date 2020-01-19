import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utilities.ConfirmBox;
import view.ClientMenu;
import view.ClientMenuScene;

import java.awt.*;


public class Main extends Application {

    private Dimension screenSize;
    private double percentageWidth;
    private double percentageHeight;
    private double percentage;
    private double buttonFont;
    private GraphicsDevice gd;
    private GraphicsConfiguration graphicsConfiguration;
    private java.awt.Insets scnMax;
    private int taskBarSize;
    private int width;
    private int height;

    @Override
    public void start(Stage window) {
        //resolutions
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        percentageWidth = (1920 - screenSize.getWidth()) / 1920;
        percentageHeight = (1080 - screenSize.getHeight()) / 1080;
        percentage = Math.max(percentageWidth, percentageHeight);
        buttonFont = 22 - (22 * percentage);
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        graphicsConfiguration = gd.getDefaultConfiguration();
        scnMax = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        taskBarSize = scnMax.bottom;
        width = (int) screenSize.getWidth(); //2560 1920 1280 1152 1024; 768 40
        height = (int) screenSize.getHeight();//1440 1080 720 648 576; 432 40

        ClientMenuScene mainMenuClient = new ClientMenuScene(percentage,buttonFont);

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(window);
        });
        window.setScene(mainMenuClient.renderScene(width,height,taskBarSize));
        window.setTitle("Mobile plans solution");
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
