package view;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.AlertBox;
import model.Client;
import controller.DaoClient;
import utilities.ProjectUtilities;

public class EditingMenu {

    private double percentage;


    private HBox topBar(double height) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 0, 0, 0));
        hbox.setPrefHeight(height * 0.05);
        hbox.getStyleClass().add("top-bar-color");
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox botBar(double height) {
        HBox hbox = new HBox();
        hbox.setPrefHeight(height * 0.05);
        hbox.getStyleClass().add("bot-bar-color");
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private VBox addVBox(double width) {
        VBox vbox = new VBox();
        vbox.setPrefWidth(width * 0.2);
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699
        return vbox;
    }

    private VBox midPane(double width, double height, GridPane... gridPanes) {
        VBox vbox = new VBox();
        vbox.setPrefSize(width * 0.6, height * 0.9);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setStyle("-fx-border-width: 4;\n-fx-border-color: #17161B");


        for(GridPane gridPane: gridPanes){
            vbox.getChildren().add(centerHBoxTemplate(width, gridPane.getPrefHeight(), gridPane.getId(), gridPane));
        }

        GridPane delete = new GridPane();

        HBox centerHbox = centerHBoxTemplate(width, height * 0.6, "Información Del Plan", delete);
        HBox botHbox = centerHBoxTemplate(width, height * 0.3, "Información Bancaria", delete);

        vbox.getChildren().addAll(centerHbox, botHbox);
        return vbox;
    }
    private VBox addToMidPane(double width, double height, HBox... hBoxes) {
        VBox vbox = new VBox();
        vbox.setPrefSize(width * 0.6, height * 0.9);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setStyle("-fx-border-width: 4;\n-fx-border-color: #17161B");


        for(HBox hBox: hBoxes){
            //vbox.getChildren().add(centerHBoxTemplate(width, gridPane.getPrefHeight(), gridPane.getId(), gridPane));
        }

        return vbox;
    }


    private HBox centerHBoxTemplate(double width, double height, String message, GridPane gridPane) {
        //Vbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width * 0.6, height);
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setStyle("-fx-border-width: 4;-fx-border-color: #17161B;-fx-background-color: #24222A;");

        //StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.setPrefSize(width * 0.2, height);

        //Rectangle bg
        Rectangle rect = new Rectangle();
        rect.setHeight(Math.max(height, 280.0));
        rect.setWidth(width * 0.2);
        rect.setFill(Color.web("#24222A"));

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setMaxWidth(width * 0.2);
        centerText.setAlignment(Pos.TOP_CENTER);

        //Text with message
        Text text = new Text(message);
        text.setFont(new Font("Consolas", 30 - (30 * percentage))); // 30
        text.setFill(Color.web("#FFFFFF"));

        //Margin for the text
        Rectangle marginRect = new Rectangle();
        marginRect.setHeight(30);
        marginRect.setWidth(0);
        marginRect.setFill(Color.web("#24222A"));

        centerText.getChildren().addAll(marginRect, text);
        stackPane.getChildren().addAll(rect, centerText);
        hbox.getChildren().addAll(stackPane, gridPane);

        //ReSize
        hbox.maxHeightProperty().bind(gridPane.heightProperty());
        //hbox.maxWidthProperty().bind(gridPane.widthProperty());

        return hbox;
    }

    private ScrollPane centerScrollPane(double width, double height, GridPane... gridPanes) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #141318;\n-fx-border-color: #17161B;\n-fx-border-width: 0");

        BorderPane layout = new BorderPane();
        VBox vBoxLeft = addVBox(width);
        VBox vBoxRight = addVBox(width);
        VBox vBoxCenter = midPane(width, height, gridPanes);
        vBoxCenter.setId("a1");

        layout.setCenter(vBoxCenter);
        layout.setLeft(vBoxLeft);
        layout.setRight(vBoxRight);

        scrollPane.setContent(layout);
        layout.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 3; // *6 to make the scrolling a bit faster
            double widthSpeed = scrollPane.getContent().getBoundsInLocal().getWidth();
            double value = scrollPane.getVvalue();
            scrollPane.setVvalue(value + -deltaY / widthSpeed);
        });
        return scrollPane;
    }

    public BorderPane renderMenuTemplate(double width, double height, double percentage, GridPane... gridPanes) {
        BorderPane mainLayout = new BorderPane();
        this.percentage = percentage;
        mainLayout.setPadding(new Insets(0, 0, 0, 0));
        HBox hBoxTop = topBar(height);
        HBox hBoxBot = botBar(height);
        ScrollPane spCenter = centerScrollPane(width, height, gridPanes);
        spCenter.getStyleClass().add("scroll-bar:vertical");

        mainLayout.setBottom(hBoxBot);
        mainLayout.setTop(hBoxTop);
        mainLayout.setCenter(spCenter);

        return mainLayout;
    }

}
