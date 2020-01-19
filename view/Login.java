package view;

import controller.DaoClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Login {

    public Login(double percentage, double buttonFont) {
        client = new DaoClient();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private DaoClient client;
    private double percentage;
    private double buttonFont;

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

        Text text = loginTextTemplate("Número de documento");

        TextField textField = loginTextFieldTemplate();

        GridPane.setConstraints(text, 0, 0);
        GridPane.setConstraints(textField, 0, 1);
        gridPane.getChildren().addAll(text, textField);

        return gridPane;
    }

    private VBox mainLoginPane(double width, double height) {
        VBox background = new VBox();
        background.setStyle("-fx-background-color: #171A1C");
        background.setPrefSize(width, height);

        GridPane gridPane = loginGridPane(width, height);

        background.getChildren().addAll(gridPane);
        background.setAlignment(Pos.CENTER);
        return background;
    }

    public Scene renderLoginScene(double width, double height) {
        Scene loginScene;

        loginScene = new Scene(mainLoginPane(width, height), width, height);
        loginScene.getStylesheets().add("loginStyle.css");

        return loginScene;
    }
}
