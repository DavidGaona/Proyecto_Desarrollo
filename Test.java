import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    public static boolean isNumeric(String inputData) {
        return inputData.matches("[+]?\\d+(\\d+)?");
    }

    public static HBox addHBox(double width, double height, String color) {
        HBox hbox = new HBox();
        hbox.setPrefHeight(height*0.05);
        hbox.setSpacing(10);
        String style = String.format("-fx-background-color: #%s;", color);
        hbox.setStyle(style);

        return hbox;
    }

    public static VBox addVBox(double width, double height) {
        VBox vbox = new VBox();
        vbox.setPrefWidth(width*0.2);
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699

        return vbox;
    }

    public static VBox midPane(double width, double height){
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

    public static HBox centerHboxTemplate(double width, double height, String message){
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
        rect.setHeight(height);
        rect.setWidth(width*0.2);
        rect.setFill(Color.web("#24222A"));

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setAlignment(Pos.TOP_CENTER);
        
        //Text with message
        Text text = new Text(message);
        text.setFont(new Font("Consolas", 30));
        text.setFill(Color.web("#FFFFFF"));
        //text.setStyle("-fx-font-style: italic;\n-fx-font-size: 30");

        //
        Rectangle marginRect = new Rectangle();
        marginRect.setHeight(30);
        marginRect.setWidth(width*0.2);
        marginRect.setFill(Color.web("#24222A"));

        centerText.getChildren().addAll(marginRect, text);
        stackPane.getChildren().addAll(rect, centerText);
        hbox.getChildren().addAll(stackPane);

        return hbox;
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
    public static GridPane addGridPane(double width, double height) throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(width,height);
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
            if (isNumeric(textFields.get(2).getText()) || textFields.get(2).getText().isBlank()) {
                textFields.get(2).setStyle(textFieldStyle + "\n-fx-border-color: #C2B8E0;");
                texts.get(2).setFill(Color.web("#C2B8E0"));
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

    public static ScrollPane centerScrollPane(double width, double height){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #141318;\n-fx-border-color: #17161B;\n-fx-border-width: 0");

        BorderPane layout = new BorderPane();
        VBox vBoxLeft = addVBox(width, height);
        VBox vBoxRight = addVBox(width, height);
        VBox vBoxCenter = midPane(width, height);

        layout.setCenter(vBoxCenter); //
        layout.setLeft(vBoxLeft);
        layout.setRight(vBoxRight);

        scrollPane.setContent(layout);
        return scrollPane;
    }

    @Override
    public void start(Stage window) throws Exception {
        //resolutions
        int width = 1920; //1920 1280 1152
        int height = 1080;//1080 720 648

        Scene mainMenu;
        BorderPane layoutScroll = new BorderPane();
        HBox hBoxTop = addHBox(width, height, "2E293D"); //18171C
        HBox hBoxBot = addHBox(width, height, "24222A");
        ScrollPane spCenter = centerScrollPane(width, height);

        layoutScroll.setBottom(hBoxBot);
        layoutScroll.setTop(hBoxTop);
        layoutScroll.setCenter(spCenter);

        mainMenu = new Scene(layoutScroll, width, height);

        window.setScene(mainMenu);
        window.setTitle("UwU");
        window.setResizable(false);
        window.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
