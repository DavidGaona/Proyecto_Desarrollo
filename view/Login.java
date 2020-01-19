package view;

import controller.DaoClient;
import controller.DaoUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.ProjectUtilities;

public class Login {

    private DaoUser user;
    private double percentage;
    private double buttonFont;

    public Login(double percentage, double buttonFont) {
        user = new DaoUser();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private TextField id_text_field;
    private PasswordField textFieldPassword;


    private TextField loginTextFieldTemplate() {
        TextField textField = new TextField();
        textField.setPrefSize(350, 40);
        return textField;
    }

    private Text loginTextTemplate(String tittle) {
        Text text = new Text(tittle);
        text.setFill(Color.web("#FFFFFF"));
        text.setFont(new Font("Consolas", 20 - (20 * percentage)));
        return text;
    }

    private GridPane loginGridPane(double width, double height) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(25);
        gridPane.setHgap(25);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.setStyle("-fx-background-color: #22282A");
        gridPane.setPrefSize(width * 0.6, height * 0.6);
        gridPane.setMaxWidth(width * 0.6);

        Text idDocumentNumber = loginTextTemplate("Número de documento");
        id_text_field = loginTextFieldTemplate();

        Text text_password = loginTextTemplate("Contraseña");
        textFieldPassword = new PasswordField();

        Button loginButton = new Button("Iniciar");
        loginButton.setOnMouseClicked(e -> loginAcction());

        GridPane.setConstraints(idDocumentNumber, 0, 0);
        GridPane.setConstraints(id_text_field, 0, 1);
        GridPane.setConstraints(text_password, 1, 0);
        GridPane.setConstraints(textFieldPassword, 1, 1);
        GridPane.setConstraints(loginButton, 1,2);
        gridPane.getChildren().addAll(idDocumentNumber, id_text_field, text_password,textFieldPassword,loginButton);


        return gridPane;
    }

    private VBox mainLoginPane(double width, double height) {

        // Solo para probar.
        //user.saveNewUser("Alexander","Gonzalez","11144186919",(short) 1,true,"holamundo");

        VBox background = new VBox();
        background.setStyle("-fx-background-color: #171A1C");
        background.setPrefSize(width, height);

        GridPane gridPane = loginGridPane(width, height);

        background.getChildren().addAll(gridPane);
        background.setAlignment(Pos.CENTER);
        return background;
    }

    private void loginAcction(){
        user.loginUser(ProjectUtilities.clearWhiteSpaces(id_text_field.getText()),textFieldPassword.getText());
    }

    public Scene renderLoginScene(double width, double height) {
        Scene loginScene;

        loginScene = new Scene(mainLoginPane(width, height), width, height);
        loginScene.getStylesheets().add("loginStyle.css");

        return loginScene;
    }
}
