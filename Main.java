import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;

public class Main extends Application {

    public static boolean isNumeric(String inputData) {
        return inputData.matches("[+]?\\d+(\\d+)?");
    }

    public static HBox addHBox(int width, int height) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #3C3F41;"); // #336699

        Button buttonCurrent = new Button("Current");
        buttonCurrent.setPrefSize((int)(width*0.05), (int)(height*0.03));
        buttonCurrent.setStyle("-fx-background-color: #FFFFFF;");

        Button buttonProjected = new Button("Projected");
        buttonProjected.setPrefSize((int)(width*0.05), (int)(height*0.03));
        hbox.getChildren().addAll(buttonCurrent, buttonProjected);
        buttonProjected.setStyle("-fx-background-color: #FFFFFF;");

        return hbox;
    }

    public static TextField clientTextFieldTemplate(String tittle, String textFieldStyle){
        TextField clientTextField = new TextField(tittle);
        clientTextField.setStyle(textFieldStyle);
        clientTextField.setFont(new Font("Consolas", 20));
        clientTextField.setPrefSize(300, 30);
        return clientTextField;
    }

    public static Text clientTextTemplate(String tittle, String color){
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 30));;
        clientText.setFill(Color.web(color));
        return clientText;
    }

    static int lastUpdated;
    public static GridPane addGridPane(){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: #212121;");
        ArrayList<TextField> textFields = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();

        String textFieldStyle = "-fx-background-color: #3D3946;\n-fx-text-fill: #FFFFFF;";
        String textColor = "#747474";

        //name text
        texts.add(clientTextTemplate("Nombres:", textColor));
        //name text field actions
        textFields.add(clientTextFieldTemplate("", textFieldStyle));
        textFields.get(0).setOnMouseClicked(e -> {
            textFields.get(0).setStyle(textFieldStyle + "\n-fx-border-color: #FFFFFF;");
            texts.get(0).setFill(Color.web("#FFFFFF"));
            if (lastUpdated != 0){
                textFields.get(lastUpdated).setStyle(textFieldStyle);
                //texts.get(lastUpdated).setFill(Color.web("#747474"));
                lastUpdated = 0;
            }
        });

        //last name text
        Text lastNameText = clientTextTemplate("Apellidos:", textColor);

        //name text field actions
        TextField lastNameTextField =  clientTextFieldTemplate("", textFieldStyle);
        textFields.add(lastNameTextField);
        lastNameTextField.setOnMouseClicked(e -> {
            lastNameTextField.setStyle(textFieldStyle + "\n-fx-border-color: #FFFFFF;");
            lastNameText.setFill(Color.web("#FFFFFF"));
            lastUpdated = 1;
        });
        textFields.get(0).setOnMouseReleased(e -> {
            lastNameTextField.setStyle(textFieldStyle);
            lastNameText.setFill(Color.web("#747474"));
        });//C792EA

        Text documentIDText = clientTextTemplate("Documento de identidad:", textColor);

        //name text field actions
        TextField documentIDTextField =  clientTextFieldTemplate("", textFieldStyle);
        textFields.add(documentIDTextField);
        documentIDTextField.setOnMouseClicked(e -> {
            documentIDTextField.setStyle(textFieldStyle + "\n-fx-border-color: #FFFFFF;");
            documentIDText.setFill(Color.web("#FFFFFF"));
            lastUpdated = 2;
        });
        documentIDTextField.setOnMouseReleased(e -> {
            documentIDTextField.setStyle(textFieldStyle);
            documentIDText.setFill(Color.web("#747474"));
        });
        documentIDTextField.setOnKeyTyped(e -> {
            if (isNumeric(documentIDTextField.getText())){
                documentIDTextField.setStyle(textFieldStyle + "\n-fx-border-color: #46E004;");
                documentIDText.setFill(Color.web("#46E004"));
            } else {
                documentIDTextField.setStyle(textFieldStyle + "\n-fx-border-color: #FE0000;");
                documentIDText.setFill(Color.web("#FE0000"));
            }
        });


        //Constrains
        GridPane.setConstraints(texts.get(0), 0, 0);
        GridPane.setHalignment(texts.get(0), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(0), 1, 0);
        GridPane.setConstraints(lastNameText, 0, 1);
        GridPane.setHalignment(lastNameText, HPos.RIGHT);
        GridPane.setConstraints(lastNameTextField, 1, 1);
        GridPane.setConstraints(documentIDText, 0, 2);
        GridPane.setHalignment(documentIDText, HPos.RIGHT);
        GridPane.setConstraints(documentIDTextField, 1, 2);

        //Adding all nodes
        gridPane.getChildren().addAll(texts.get(0), textFields.get(0), lastNameText, lastNameTextField, documentIDText, documentIDTextField);

        return gridPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        int width = 1920;
        int height = 1080;
        Scene mainMenu;
        BorderPane layout = new BorderPane();
        HBox hbox = addHBox(width, height);
        GridPane gridPane = addGridPane();
        layout.setTop(hbox);
        layout.setCenter(gridPane);
        mainMenu = new Scene(layout, width, height);

        window.setScene(mainMenu);
        window.setTitle("UwU");
        window.setResizable(false);
        window.show();

    }
}
