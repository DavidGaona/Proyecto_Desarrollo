import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.text.StyledEditorKit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Test extends Application {

    public boolean isNumeric(String inputData) {
        return inputData.matches("\\d+(\\d+)?");
    }

    public HBox addHBox(double width, double height, String color) {
        HBox hbox = new HBox();
        hbox.setPrefHeight(height*0.05);
        hbox.setSpacing(10);
        String style = String.format("-fx-background-color: #%s;", color);
        hbox.setStyle(style);

        return hbox;
    }

    public VBox addVBox(double width, double height) {
        VBox vbox = new VBox();
        vbox.setPrefWidth(width*0.2);
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699

        return vbox;
    }

    public VBox midPane(double width, double height){
        VBox vbox = new VBox();
        vbox.setPrefSize(width*0.6, height*0.9);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setStyle("-fx-border-width: 4;\n-fx-border-color: #17161B");

        HBox infoHbox = centerHboxTemplate(width, height*0.4, "Información Personal");
        HBox centerHbox = centerHboxTemplate(width, height*0.6, "Información Del Plan");
        HBox botHbox = centerHboxTemplate(width, height*0.3, "Información Bancaria");

        vbox.getChildren().addAll(infoHbox, centerHbox, botHbox);
        return vbox;
    }

    public HBox centerHboxTemplate(double width, double height, String message){
        //Vbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width*0.6, height);
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setStyle("-fx-border-width: 4;-fx-border-color: #17161B;-fx-background-color: #302E38;");

        //StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.setPrefSize(width*0.2,height);

        //Rectangle bg
        Rectangle rect = new Rectangle();
        rect.setHeight(Math.max(height, 280.0));
        rect.setWidth(width*0.2);
        rect.setFill(Color.web("#24222A"));

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setMaxWidth(width*0.2);
        centerText.setAlignment(Pos.TOP_CENTER);
        
        //Text with message
        Text text = new Text(message);
        text.setFont(new Font("Consolas", 18));
        text.setFill(Color.web("#FFFFFF"));
        //text.setStyle("-fx-font-style: italic;\n-fx-font-size: 30");

        //Margin for the text
        Rectangle marginRect = new Rectangle();
        marginRect.setHeight(30);
        marginRect.setWidth(0);
        marginRect.setFill(Color.web("#24222A"));

        //TextFields
        GridPane gridPane = addGridPane(width, height);

        centerText.getChildren().addAll(marginRect, text);
        stackPane.getChildren().addAll(rect, centerText);
        hbox.getChildren().addAll(stackPane, gridPane);

        return hbox;
    }

    public TextField clientTextFieldTemplate(String tittle, String textFieldStyle){
        TextField clientTextField = new TextField(tittle);
        clientTextField.setStyle(textFieldStyle);
        clientTextField.setFont(new Font("Consolas", 15));
        clientTextField.setPrefSize(350, 30);
        return clientTextField;
    }

    public Text clientTextTemplate(String tittle, String color){
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 15));
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
                    String textFieldId = selectedTextField.getId();
                    selectedTextField.setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
                    for (Node node : layout.getChildren()){
                        if (textFieldId.substring(2).equals(node.getId().substring(1))){
                            ((Text) node).setFill(Color.web(textColor));
                            break;
                        }
                    }
                } else {
                    String textFieldId = lastSelectedTextField.getId();
                    if (lastSelectedTextField != null){
                        lastSelectedTextField.setStyle(textFieldStyle);
                        for (Node node : layout.getChildren()){
                            if (textFieldId.substring(2).equals(node.getId().substring(1))){
                                ((Text) node).setFill(Color.web("#948FA3"));
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void addTextFieldCharacterLimit(int limit, TextField... textFields){
        for (TextField textField : textFields) {
            textField.textProperty().addListener(e -> {
                if (textField.getText().length() > limit){
                    int prevPos = textField.getCaretPosition();
                    String limitedText = textField.getText().substring(0, prevPos) + textField.getText().substring(prevPos + 1);
                    textField.setText(limitedText);
                    textField.positionCaret(prevPos);
                }
            });
        }
    }

    public GridPane addGridPane(double width, double height)  {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(width*0.4, height);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 0;");


        String textFieldStyle = "-fx-background-color: #3D3946;\n-fx-text-fill: #FFFFFF;\n-fx-border-radius: 2;\n" +
                "-fx-border-width: 2;\n-fx-border-color: #3d3d3d;";
        String textColor = "#948FA3";

        //Image checkImage = new Image(new FileInputStream("C:\\Users\\david\\IdeaProjects\\panes\\src\\Check.png"));
        //final ImageView currentImage = new ImageView();
        //currentImage.setImage(checkImage);

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

        //Document id text field actions
        TextField clientDocumentIdTextField = clientTextFieldTemplate("", textFieldStyle);
        clientDocumentIdTextField.setId("TF3");

        clientDocumentIdTextField.setOnKeyTyped(e -> {
            if (!(isNumeric(clientDocumentIdTextField.getText()))) {
                String correctText = clientDocumentIdTextField.getText().replaceAll("[^\\d]", "");
                int prevPos = clientDocumentIdTextField.getCaretPosition();
                clientDocumentIdTextField.setText(correctText);
                clientDocumentIdTextField.positionCaret(prevPos - 1);
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

        //Install listener for color highlight
        installListener(gridPane, textFieldStyle, "#C2B8E0",
                clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField,
                clientDirectionTextField);

        //install listener for length limit
        addTextFieldCharacterLimit(10, clientNameTextField, clientLastNameTextField);
        addTextFieldCharacterLimit(20, clientDocumentIdTextField);
        addTextFieldCharacterLimit(256, clientDirectionTextField, clientEmailTextField);

        int colText = 4;
        int colTextField = 5;
        int rowStart = 0;
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

    public ScrollPane centerScrollPane(double width, double height){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #141318;\n-fx-border-color: #17161B;\n-fx-border-width: 0");

        BorderPane layout = new BorderPane();
        VBox vBoxLeft = addVBox(width, height);
        VBox vBoxRight = addVBox(width, height);
        VBox vBoxCenter = midPane(width, height);
        vBoxCenter.setId("a1");

        layout.setCenter(vBoxCenter);
        layout.setLeft(vBoxLeft);
        layout.setRight(vBoxRight);

        scrollPane.setContent(layout);
        return scrollPane;
    }

    @Override
    public void start(Stage window) throws Exception {
        //resolutions
        int width = 1024; //2560 1920 1280 1152 1024
        int height = 576;//1440 1080 720 648 576;

        Scene mainMenu;
        BorderPane layoutScroll = new BorderPane();
        HBox hBoxTop = addHBox(width, height, "2E293D"); //18171C
        HBox hBoxBot = addHBox(width, height, "24222A");
        ScrollPane spCenter = centerScrollPane(width, height);

        layoutScroll.setBottom(hBoxBot);
        layoutScroll.setTop(hBoxTop);
        layoutScroll.setCenter(spCenter);

        mainMenu = new Scene(layoutScroll, width, height);

        //mainMenu.setOn(e -> System.out.println(mainMenu.getWidth() + " x " + mainMenu.getHeight()));
        System.out.println(mainMenu.getWidth() + " x " + mainMenu.getHeight());
        window.setScene(mainMenu);
        window.setTitle("UwU");
        window.setResizable(false);

        window.show();
        Node tst = layoutScroll.getChildren().get(2).lookup("a1");
        System.out.println(spCenter.getHeight());

    }

    public static void main(String[] args) {
        launch(args);
    }

}
