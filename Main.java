import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    static int lastUpdated;
    public static GridPane addGridPane(int width, int height) throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 5;");

        //0 = name, 1 = last name, 2 = id document, 3 = Email, 4 = Direction
        ArrayList<TextField> textFields = new ArrayList<>();
        ArrayList<Text> texts = new ArrayList<>();

        String textFieldStyle = "-fx-background-color: #3D3946;\n-fx-text-fill: #FFFFFF;\n-fx-border-radius: 2;\n" +
                "-fx-border-width: 2;\n-fx-border-color: #3d3d3d;";
        String textColor = "#948FA3";

        Image checkImage = new Image(new FileInputStream("C:\\Users\\david\\IdeaProjects\\panes\\src\\Check.png"));
        final ImageView currentImage = new ImageView();
        currentImage.setImage(checkImage);



        //name text
        texts.add(clientTextTemplate("Nombres:", textColor));
        //name text field actions
        textFields.add(clientTextFieldTemplate("", textFieldStyle));
        textFields.get(0).setOnMouseClicked(e -> {
            textFields.get(0).setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
            texts.get(0).setFill(Color.web("#C2B8E0"));
            if (lastUpdated != 0){
                textFields.get(lastUpdated).setStyle(textFieldStyle);
                texts.get(lastUpdated).setFill(Color.web(textColor));
                lastUpdated = 0;
            }

            if (!textFields.get(0).getText().isBlank()){

            }
        });



        //last name text
        texts.add(clientTextTemplate("Apellidos:", textColor));

        //name text field actions
        textFields.add(clientTextFieldTemplate("", textFieldStyle));
        textFields.get(1).setOnMouseClicked(e -> {
            textFields.get(1).setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
            texts.get(1).setFill(Color.web("#C2B8E0"));
            if (lastUpdated != 1){
                textFields.get(lastUpdated).setStyle(textFieldStyle);
                texts.get(lastUpdated).setFill(Color.web(textColor));
                lastUpdated = 1;
            }
        });

        //ID document text
        texts.add(clientTextTemplate("Documento de identidad:", textColor));

        //name text field actions
        textFields.add(clientTextFieldTemplate("", textFieldStyle));
        textFields.get(2).setOnMouseClicked(e -> {
            textFields.get(2).setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
            texts.get(2).setFill(Color.web("#C2B8E0"));
            if (lastUpdated != 2){
                textFields.get(lastUpdated).setStyle(textFieldStyle);
                texts.get(lastUpdated).setFill(Color.web(textColor));
                lastUpdated = 2;
            }
        });
        textFields.get(2).setOnKeyTyped(e -> {
            if (isNumeric(textFields.get(2).getText())) {
                textFields.get(2).setStyle(textFieldStyle);
                texts.get(2).setFill(Color.web(textColor));
            } else {
                textFields.get(2).setStyle(textFieldStyle + "\n-fx-border-color: #FE0000;");
                texts.get(2).setFill(Color.web("#FE0000"));
            }
        });

        //Email Text
        texts.add(clientTextTemplate("Email:", textColor));

        //Email TextField
        textFields.add(clientTextFieldTemplate("", textFieldStyle));
        textFields.get(3).setOnMouseClicked(e -> {
            textFields.get(3).setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
            texts.get(3).setFill(Color.web("#C2B8E0"));
            if (lastUpdated != 3){
                textFields.get(lastUpdated).setStyle(textFieldStyle);
                texts.get(lastUpdated).setFill(Color.web(textColor));
                lastUpdated = 3;
            }
        });

        //Direction Text
        texts.add(clientTextTemplate("Dirección:", textColor));

        //Direction TextField
        textFields.add(clientTextFieldTemplate("", textFieldStyle));
        textFields.get(4).setOnMouseClicked(e -> {
            textFields.get(4).setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
            texts.get(4).setFill(Color.web("#C2B8E0"));
            if (lastUpdated != 4){
                textFields.get(lastUpdated).setStyle(textFieldStyle);
                texts.get(lastUpdated).setFill(Color.web(textColor));
                lastUpdated = 4;
            }
        });


        int colText = 40;
        int colTextField = 41;
        int rowStart = 1;
        //Constrains
        GridPane.setConstraints(texts.get(0), colText, rowStart);
        GridPane.setHalignment(texts.get(0), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(0), colTextField, rowStart);
        GridPane.setConstraints(texts.get(1), colText, rowStart + 1);
        GridPane.setHalignment(texts.get(1), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(1), colTextField, rowStart + 1);
        GridPane.setConstraints(texts.get(2), colText, rowStart + 2);
        GridPane.setHalignment(texts.get(2), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(2), colTextField, rowStart + 2);
        GridPane.setConstraints(texts.get(3), colText, rowStart + 3);
        GridPane.setHalignment(texts.get(3), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(3), colTextField, rowStart + 3);
        GridPane.setConstraints(texts.get(4), colText, rowStart + 4);
        GridPane.setHalignment(texts.get(4), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(4), colTextField, rowStart + 4);

        //Adding all nodes
        gridPane.getChildren().addAll(
                //currentImage,
                texts.get(0), textFields.get(0),
                texts.get(1), textFields.get(1),
                texts.get(2), textFields.get(2),
                texts.get(3), textFields.get(3),
                texts.get(4), textFields.get(4));
        //gridPane.setAlignment(Pos.TOP_CENTER);
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
