package view;

import controller.DaoUser;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import utilities.AlertBox;
import utilities.ProjectUtilities;

public class Login {

    public static SimpleIntegerProperty currentWindow = new SimpleIntegerProperty(0);
    public static int currentLoggedUser = -1;

    private DaoUser user;
    private double width;
    private double height;


    public Login(double width, double height, SimpleIntegerProperty currentWindow2) {
        user = new DaoUser();
        this.width = width;
        this.height = height;
        currentWindow2.bind(currentWindow);
    }

    private TextField userIdTextField;
    private TextField passwordTextField;

    private TextField loginTextFieldTemplate(double width, double height) {
        TextField textField = new TextField();
        textField.getStyleClass().add("text-field-login");
        textField.setPromptText("Número de documento");
        textField.setPrefSize(width, height);
        textField.setMaxSize(width, height);
        return textField;
    }

    private VBox loginVBox(double width, double height) {
        double percentageWidth = (2560 - width) / 2560;
        double percentageHeight = (1440 - height) / 1440;
        double percentageLogin = Math.max(percentageWidth, percentageHeight);

        double textFFont = 30 - (30 * percentageLogin);
        double labelFont = 60 - (60 * percentageLogin);
        double buttonFontLogin = 40 - (40 * percentageLogin);

        VBox vBox = new VBox();
        vBox.setSpacing(height * 0.05);
        vBox.setPadding(new Insets(0, 0, 25, 0));
        vBox.setStyle("-fx-background-color: #22282A;\n -fx-border-width: 0 3 3 3;\n -fx-border-color: #3C4448;\n -fx-border-radius: 5;"); //2D333B
        vBox.setPrefSize(width * 0.3, height * 0.6);
        vBox.setMaxWidth(width * 0.3);
        vBox.setAlignment(Pos.TOP_CENTER);

        HBox hBox = new HBox();
        hBox.setPrefSize(width * 0.3, height * 0.1);
        hBox.setAlignment(Pos.TOP_CENTER);

        Pane leftPane = new Pane();
        leftPane.setStyle("-fx-background-color: #22282A;\n -fx-border-width: 3 0 0 0;\n -fx-border-color: #3C4448;");
        leftPane.setMaxSize(width * 0.025, height * 0.1);
        leftPane.setMinSize(width * 0.025, height * 0.1);

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(width * 0.25, height * 0.1);
        stackPane.setMaxWidth(width * 0.25);
        stackPane.setStyle("-fx-background-color: #171A1C;\n -fx-border-width: 0 3 3 3;\n -fx-border-color: #3C4448;\n -fx-border-radius: 0 0 3 3;");
        stackPane.setPadding(new Insets(20, 0, 0, 0));
        stackPane.setAlignment(Pos.TOP_CENTER);

        Pane rightPane = new HBox();
        rightPane.setStyle("-fx-background-color: #22282A;\n -fx-border-width: 3 0 0 0;\n -fx-border-color: #3C4448;");
        rightPane.setMaxSize(width * 0.025, height * 0.1);
        rightPane.setMinSize(width * 0.025, height * 0.1);

        hBox.getChildren().addAll(leftPane, stackPane, rightPane);

        Label loginLabel = new Label("INICIAR SESIÓN");
        loginLabel.getStyleClass().add("login-label");
        loginLabel.setStyle(loginLabel.getStyle() + "-fx-font-size: " + labelFont + "px;");
        stackPane.getChildren().addAll(loginLabel);

        userIdTextField = loginTextFieldTemplate(width * 0.25, height * 0.05);
        userIdTextField.setStyle(userIdTextField.getStyle() + " -fx-font-size: " + textFFont + "px; ");
        ProjectUtilities.onlyNumericTextField(userIdTextField);

        passwordTextField = new PasswordField();
        passwordTextField.setMaxSize(width * 0.25, height * 0.05);
        passwordTextField.setPrefSize(width * 0.25, height * 0.05);
        passwordTextField.getStyleClass().add("text-field-login");
        passwordTextField.setStyle(passwordTextField.getStyle() + " -fx-font-size: " + textFFont + "px; ");
        passwordTextField.setPromptText("Contraseña");
        passwordTextField.setOnAction(e -> loginAction());

        ProjectUtilities.focusListener("3C4448", "3985AB", userIdTextField, passwordTextField);

        Button loginButton = new Button("Iniciar sesión");
        loginButton.setPrefSize(width * 0.25, height * 0.05);
        loginButton.getStyleClass().add("login-button");
        loginButton.setStyle(loginButton.getStyle() + "-fx-font-size: " + buttonFontLogin + "px;");
        loginButton.setOnMouseClicked(e -> loginAction());

        vBox.getChildren().addAll(hBox, userIdTextField, passwordTextField, loginButton);

        return vBox;
    }

    private void clear(){
        userIdTextField.setText("");
        passwordTextField.setText("");
    }

    private void loginAction() {
        final int loginSuccess = user.loginUser(ProjectUtilities.clearWhiteSpaces(userIdTextField.getText()), passwordTextField.getText());

        switch (loginSuccess) {
            case 0:
                clear();
                Login.currentWindow.set(1);
                break;
            case 1:
                clear();
                Login.currentWindow.set(2);
                break;
            case 2:
                clear();
                Login.currentWindow.set(3);
                break;
            case 3:
                clear();
                Login.currentWindow.set(4);
                break;
            case -1:
                AlertBox.display("Error","No se pudo encontrar el usuario","");
                currentLoggedUser = -1;
                break;
            case -2:
                AlertBox.display("Error","Contraseña invalida","");
                currentLoggedUser = -1;
                break;
            case -3:
                AlertBox.display("Error","Cuenta desactivada","");
                currentLoggedUser = -1;
                break;
            default:
                currentLoggedUser = -1;
                break;
        }
    }

    public VBox mainLoginPane() {

        VBox background = new VBox();
        background.setStyle("-fx-background-color: #171A1C");
        background.setPrefSize(width, height);

        background.getChildren().addAll(loginVBox(width, height));
        background.setAlignment(Pos.CENTER);
        return background;
    }
}
