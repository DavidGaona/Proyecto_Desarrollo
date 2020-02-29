import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ConfirmBox;
import view.ClientMenu;
import view.Login;
import view.UserMenu;
import view.UserPasswordChange;

import java.awt.*;

public class Main extends Application {
    public static SimpleIntegerProperty currentWindow = new SimpleIntegerProperty(0);

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
        double height = screenSize.getHeight() - scnMax.bottom * 1.685;//1440 1080 720 648 576; 432 40

        Login login = new Login(width, height, currentWindow);
        Scene rootScene = new Scene(login.mainLoginPane());
        rootScene.getStylesheets().add("loginStyle.css");
        currentWindow.addListener((obs, oldState, newState) -> {
            switch (currentWindow.get()) {
                case 0:
                    //User Login scene
                    window.setScene(rootScene);
                    break;
                case 1:
                    //Default user menu
                    ClientMenu client = new ClientMenu(percentage, buttonFont);
                    Scene clientMenuScene = new Scene(client.renderClientEditMenu(width, height), width, height);
                    clientMenuScene.getStylesheets().add("styles.css");
                    window.setScene(clientMenuScene);
                    break;
                case 2:
                    //ToDo manager

                    break;
                case 3:
                    //Admin user menu
                    UserMenu user = new UserMenu(percentage, buttonFont);
                    Scene userMenuScene = new Scene(user.renderUserEditMenu(width, height), width, height);
                    userMenuScene.getStylesheets().add("styles.css");
                    window.setScene(userMenuScene);
                    break;
                case 4:
                    //User Password Reset scene
                    UserPasswordChange userPasswordChange = new UserPasswordChange();
                    Scene passwordChangeScene = new Scene(userPasswordChange.BackGroundPane(width, height), width, height);
                    passwordChangeScene.getStylesheets().add("loginStyle.css");
                    window.setScene(passwordChangeScene);
                    break;
            }

        });

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(window);
        });
        GraphicsDevice gdd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int widthh = gdd.getDisplayMode().getWidth();
        int heightt = gdd.getDisplayMode().getHeight();
        System.out.println(width + "  " + height);
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int taskBarHeight = scrnSize.height - winSize.height;
        System.out.println(taskBarHeight);
        System.out.println(widthh + "  " + (heightt - taskBarHeight));
        window.setTitle("Mobile plans solution");
        window.setResizable(false);
        window.setScene(rootScene);
        window.show();
    }

    private void closeProgram(Stage window) {
        if (ConfirmBox.display("Cerrar Programa", "¿ Quieres cerrar el programa ?", "Si quiero cerrar", "No quiero cerrar")) {
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
