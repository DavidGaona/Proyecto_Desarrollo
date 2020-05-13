import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.components.ConfirmBox;
import view.*;

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
        System.out.println(width+" "+height);
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
                    clientMenuScene.getStylesheets().addAll("styles.css", "searchPaneStyle.css");
                    window.setScene(clientMenuScene);
                    client.align();
                    break;
                case 2:
                    //Manager plan info menu
                    ManagerMenu manager = new ManagerMenu(percentage, buttonFont);
                    Scene managerMenuScene = new Scene(manager.renderPlanEditingMenu(width, height), width, height);
                    managerMenuScene.getStylesheets().addAll("styles.css", "searchPaneStyle.css");
                    window.setScene(managerMenuScene);
                    manager.align();
                    break;
                case 3:
                    //Admin user menu
                    UserMenu user = new UserMenu(percentage, buttonFont);
                    Scene userMenuScene = new Scene(user.renderUserEditMenu(width, height), width, height);
                    userMenuScene.getStylesheets().addAll("styles.css", "searchPaneStyle.css");
                    window.setScene(userMenuScene);
                    break;
                case 4:
                    //User Password Reset scene
                    UserPasswordChange userPasswordChange = new UserPasswordChange();
                    Scene passwordChangeScene = new Scene(userPasswordChange.BackGroundPane(width, height), width, height);
                    passwordChangeScene.getStylesheets().add("loginStyle.css");
                    window.setScene(passwordChangeScene);
                    break;
                case 5:
                    //Admin bank menu
                    BankMenu bank = new BankMenu(percentage, buttonFont);
                    Scene bankMenuScene = new Scene(bank.renderBankEditMenu(width, height), width, height);
                    bankMenuScene.getStylesheets().addAll("styles.css", "searchPaneStyle.css");
                    window.setScene(bankMenuScene);
                    break;
                case 6:
                    //Admin bill menu
                    BillingMenu bill = new BillingMenu(percentage, buttonFont);
                    Scene billingMenuScene = new Scene(bill.renderBillingEditMenu(width, height), width, height);
                    billingMenuScene.getStylesheets().addAll("styles.css", "searchPaneStyle.css");
                    window.setScene(billingMenuScene);
                    break;

                case 7:
                    //Manager stats Client menu
                    ChartClientsMenu chartClient = new ChartClientsMenu(percentage, buttonFont);
                    Scene chartClientMenuScene = new Scene(chartClient.renderChartClientsEditMenu(width, height), width, height);
                    chartClientMenuScene.getStylesheets().addAll( "searchPaneStyle.css", "graphics.css", "charts.css");
                    window.setScene(chartClientMenuScene);
                    break;

                case 8:
                    //Manager stats Plan menu
                    ChartPlansMenu chartPlan = new ChartPlansMenu(percentage, buttonFont);
                    Scene chartPlanMenuScene = new Scene(chartPlan.renderChartPlansEditMenu(width, height), width, height);
                    chartPlanMenuScene.getStylesheets().addAll("styles.css", "graphics.css", "searchPaneStyle.css");
                    window.setScene(chartPlanMenuScene);
                    break;
            }

        });

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(window);
        });
        window.setTitle("Mobile solutions");
        window.setResizable(false);
        window.setScene(rootScene);
        //window.setMaximized(true);
        window.show();
    }

    private void closeProgram(Stage window) {
        if (ConfirmBox.display("Cerrar Programa", "¿ Quieres cerrar el programa ?",
                "Si quiero cerrar", "No quiero cerrar")) {
            window.close();
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
