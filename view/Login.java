package view;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Login {

    /* ToDo  */
    /*private double percentage;
    private double buttonFont;

    public Login(double percentage, double buttonFont) {

        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private TextField userIdTextField;
    private PasswordField passwordTextfield;


    private TextField userTextFieldTemplate(String tittle) {
        TextField clientTextField = new TextField(tittle);
        clientTextField.getStyleClass().add("client-text-field-template");
        clientTextField.setFont(new Font("Consolas", 20 - (20 * percentage))); //20
        clientTextField.setPrefSize(350 - (350 * percentage), 30 - (30 * percentage)); //350 , 30
        return clientTextField;
    }

    private Text userTextTemplate(String tittle, String color) {
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 20 - (20 * percentage))); //20
        clientText.setFill(Color.web(color));
        return clientText;
    }


    public GridPane loginPane(double width, double height){
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(width * 0.2, height); // 0.4 ,,
        //gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10); // 10
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 0;");


        String textColor = "#948FA3";

        //Id text
        Text clientNameText = userTextTemplate("ID: ", textColor);
        clientNameText.setId("L1");
        gridPane.add(clientNameText,0,1);

        //Id text field actions
        userIdTextField = userTextFieldTemplate("");
        userIdTextField.setId("LF1");
        gridPane.add(userIdTextField,1,1);

        //Password text
        Text passwordText = userTextTemplate("Contraseña: ", textColor);
        passwordText.setId("L2");
        gridPane.add(passwordText,0,3);

        // Add Password Field
        passwordTextfield = new PasswordField();
        gridPane.add(passwordTextfield, 1, 3);

        return gridPane;

    }*/

}
