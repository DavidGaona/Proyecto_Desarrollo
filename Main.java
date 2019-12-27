import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.text.StyledEditorKit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {

    public static boolean isNumeric(String inputData) {
        return inputData.matches("[+]?\\d+(\\d+)?");
    }

    public static HBox addHBox(int width, int height, String color) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 15, (int)height*0.05, 15));
        hbox.setSpacing(10);
        String style = String.format("-fx-background-color: #%s;", color);
        hbox.setStyle(style);

        return hbox;
    }

    public static VBox addVBox(int width, int height) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(15, (int)width*0.15, 15, 0));
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699

        return vbox;
    }

    public static TextField clientTextFieldTemplate(String tittle, String textFieldStyle){
        TextField clientTextField = new TextField(tittle);
        clientTextField.setStyle(textFieldStyle);
        clientTextField.setFont(new Font("Consolas", 20));
        clientTextField.setPrefSize(350, 30);
        return clientTextField;
    }

    public static Text clientTextTemplate(String tittle, String color){
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 20));;
        clientText.setFill(Color.web(color));
        return clientText;
    }

    private TextField selectedTextField, lastSelectedTextField;
    private void installListener(GridPane layout, String textFieldStyle, String textColor, TextField... textFields) {
        // Install the same listener on all of them
        for (TextField textField : textFields) {
            textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {

                // Set the selectedTextField to null whenever focus is lost. This accounts for the
                // TextField losing focus to another control that is NOT a TextField
                selectedTextField = null;

                if (newValue) {
                    // The new textfield is focused, so set the global reference
                    lastSelectedTextField = textField;
                    selectedTextField = textField;
                    selectedTextField.setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
                    for (Node node : layout.getChildren()){
                        if (selectedTextField.getId().substring(2).equals(node.getId().substring(1))){
                            ((Text) node).setFill(Color.web(textColor));
                            break;
                        }
                    }
                } else {
                    if (lastSelectedTextField != null){
                        lastSelectedTextField.setStyle(textFieldStyle);
                        for (Node node : layout.getChildren()){
                            if (lastSelectedTextField.getId().substring(2).equals(node.getId().substring(1))){
                                ((Text) node).setFill(Color.web("#948FA3"));
                                break;
                            }
                        }
                    }
                }
            });
        }
    }


    public GridPane addGridPane(int width, int height) throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 5;");


        String textFieldStyle = "-fx-background-color: #3D3946;\n-fx-text-fill: #FFFFFF;\n-fx-border-radius: 2;\n" +
                "-fx-border-width: 2;\n-fx-border-color: #3d3d3d;";
        String textColor = "#948FA3";

        Image checkImage = new Image(new FileInputStream("C:\\Users\\david\\IdeaProjects\\panes\\src\\Check.png"));
        final ImageView currentImage = new ImageView();
        currentImage.setImage(checkImage);

        //name text
        Text clientNameText = clientTextTemplate("Nombres:", textColor);
        clientNameText.setId("T1");

        //name text field actions
        TextField clientNameTextField = clientTextFieldTemplate("", textFieldStyle);
        clientNameTextField.setId("TF1");

        //last name text
        Text clientLastNameText = clientTextTemplate("Apellidos:", textColor);
        clientLastNameText.setId("T2");

        //name text field actions
        TextField clientLastNameTextField = clientTextFieldTemplate("", textFieldStyle);
        clientLastNameTextField.setId("TF2");

        //ID document text
        Text clientDocumentIdText = clientTextTemplate("Documento de identidad:", textColor);
        clientDocumentIdText.setId("T3");

        //name text field actions
        TextField clientDocumentIdTextField = clientTextFieldTemplate("", textFieldStyle);
        clientDocumentIdTextField.setId("TF3");
        clientDocumentIdTextField.setOnKeyTyped(e -> {
            if (isNumeric(clientDocumentIdTextField.getText()) || clientDocumentIdTextField.getText().isBlank()) {
                clientDocumentIdTextField.setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
                clientDocumentIdText.setFill(Color.web("#C2B8E0"));
            } else {
                clientDocumentIdTextField.setStyle(textFieldStyle + "\n-fx-border-color: #FE0000;");
                clientDocumentIdText.setFill(Color.web("#FE0000"));
            }
        });

        //Email Text
        Text clientEmailText = clientTextTemplate("Email", textColor);
        clientEmailText.setId("T4");

        //Email TextField
        TextField clientEmailTextField = clientTextFieldTemplate("", textFieldStyle);
        clientEmailTextField.setId("TF4");

        //Direction Text
        Text clientDirectionText = clientTextTemplate("Dirección:", textColor);
        clientDirectionText.setId("T5");

        //Direction TextField
        TextField clientDirectionTextField = clientTextFieldTemplate("", textFieldStyle);
        clientDirectionTextField.setId("TF5");

        //Install listener
        installListener(gridPane, textFieldStyle, "#C2B8E0",
                clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField,
                clientDirectionTextField);

        int colText = 40;
        int colTextField = 41;
        int rowStart = 1;
        //Constrains
        GridPane.setConstraints(clientNameText, colText, rowStart);
        GridPane.setHalignment(clientNameText, HPos.RIGHT);
        GridPane.setConstraints(clientNameTextField, colTextField, rowStart);
        GridPane.setConstraints(clientLastNameText, colText, rowStart + 1);
        GridPane.setHalignment(clientLastNameText, HPos.RIGHT);
        GridPane.setConstraints(clientLastNameTextField, colTextField, rowStart + 1);
        GridPane.setConstraints(clientDocumentIdText, colText, rowStart + 2);
        GridPane.setHalignment(clientDocumentIdText, HPos.RIGHT);
        GridPane.setConstraints(clientDocumentIdTextField, colTextField, rowStart + 2);
        GridPane.setConstraints(clientEmailText, colText, rowStart + 3);
        GridPane.setHalignment(clientEmailText, HPos.RIGHT);
        GridPane.setConstraints(clientEmailTextField, colTextField, rowStart + 3);
        GridPane.setConstraints(clientDirectionText, colText, rowStart + 4);
        GridPane.setHalignment(clientDirectionText, HPos.RIGHT);
        GridPane.setConstraints(clientDirectionTextField, colTextField, rowStart + 4);


        //Adding all nodes
        gridPane.getChildren().addAll(
                //currentImage,
                clientNameText, clientNameTextField,
                clientLastNameText, clientLastNameTextField,
                clientDocumentIdText, clientDocumentIdTextField,
                clientEmailText, clientEmailTextField,
                clientDirectionText, clientDirectionTextField);
        //gridPane.setAlignment(Pos.TOP_CENTER);
        return gridPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        int width = 1920;  //1920 1280
        int height = 1080; //1080 720
        Scene mainMenu;
        BorderPane layout = new BorderPane();
        HBox hBoxTop = addHBox(width, height, "2E293D"); //18171C
        HBox hBoxBot = addHBox(width, height, "24222A");
        GridPane gridPane = addGridPane(width, height);
        VBox vBoxLeft = addVBox(width, height);
        VBox vBoxRight = addVBox(width, height);
        layout.setTop(hBoxTop);
        layout.setCenter(gridPane);
        layout.setLeft(vBoxLeft);
        layout.setRight(vBoxRight);
        layout.setBottom(hBoxBot);
        mainMenu = new Scene(layout, width, height);

        window.setScene(mainMenu);
        window.setTitle("UwU");
        window.setResizable(false);
        window.show();

    }
}
