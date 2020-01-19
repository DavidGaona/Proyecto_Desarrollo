import javafx.application.Application;
import javafx.stage.Stage;
import utilities.ConfirmBox;
import view.ClientMenuScene;
import view.Login;

import java.awt.*;


public class Main extends Application {

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
        double width = screenSize.getWidth();  //2560 1920 1280 1152 1024; 768 40
        double height = screenSize.getHeight() - scnMax.bottom * 1.7;//1440 1080 720 648 576; 432 40

        ClientMenuScene mainMenuClient = new ClientMenuScene(percentage, buttonFont);
        Login login = new Login(percentage, buttonFont);

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(window);
        });

        //window.setScene(mainMenuClient.renderScene(width, height));
        window.setScene(login.renderLoginScene(width, height)); //1.8 si no te queda
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
