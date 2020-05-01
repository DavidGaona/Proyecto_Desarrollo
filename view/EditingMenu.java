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
    private VBox midPane;
    private double height;
    private double width;
    private ScrollPane scrollPane;

    EditingMenu(double width, double height, double percentage){
        this.height = height;
        this.width = width;
        this.percentage = percentage;

        midPane = new VBox();
        midPane.setMinSize(width * 0.6, height * 0.9);
        midPane.setAlignment(Pos.TOP_LEFT);
        midPane.setStyle("-fx-border-width: 4 0 4 0;\n-fx-border-color: #17161B;\n-fx-background-color: #24222A;");
    }

    private HBox topBar() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 0, 0, 0));
        hbox.setPrefHeight(height * 0.05);
        hbox.getStyleClass().add("top-bar-color");
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox botBar() {
        HBox hbox = new HBox();
        hbox.setPrefHeight(height * 0.05);
        hbox.getStyleClass().add("bot-bar-color");
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPrefWidth(width * 0.2);
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699
        return vbox;
    }

    public void addToMidPane(HBox... hBoxes) {

        for(HBox hBox: hBoxes){
            midPane.getChildren().add(hBox);
        }

        //EditingPanel editingPanel1 = new EditingPanel("Información Del Plan", percentage, width);
        //EditingPanel editingPanel2 = new EditingPanel("Información Bancaria", percentage, width);

        //midPane.getChildren().addAll(
        //        editingPanel1.sendPane(width, height * 0.6),
        //        editingPanel2.sendPane(width, height * 0.3)
        //);

    }

    public void topRightPane()
    {
        midPane.setAlignment(Pos.TOP_RIGHT);
        midPane.setStyle(midPane.getStyle() + "-fx-background-color: #18171C;");
    }

    public void centerPane(){
        midPane.setAlignment(Pos.CENTER);
        midPane.setStyle(midPane.getStyle() + "-fx-background-color: #18171C;");
    }

    private ScrollPane centerScrollPane() {
        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #141318;\n-fx-border-color: #17161B;\n-fx-border-width: 0");
        scrollPane.setMinSize(width * 0.6, height * 0.9);

        BorderPane layout = new BorderPane();
        VBox vBoxLeft = addVBox();
        VBox vBoxRight = addVBox();
        VBox vBoxCenter = midPane;
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
        //scrollPane.setA
        return scrollPane;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public BorderPane renderMenuTemplate() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(0, 0, 0, 0));
        HBox hBoxTop = topBar();
        HBox hBoxBot = botBar();
        ScrollPane spCenter = centerScrollPane();
        spCenter.getStyleClass().add("scroll-bar:vertical");

        mainLayout.setBottom(hBoxBot);
        mainLayout.setTop(hBoxTop);
        mainLayout.setCenter(spCenter);

        return mainLayout;
    }

}
