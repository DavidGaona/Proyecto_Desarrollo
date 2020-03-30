package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchPane {
    private HBox frame;
    private TextField searchField = new TextField();
    private Button closeButton = new Button();
    private double width;
    private double height;
    private double percentage;

    public SearchPane(double width, double height, double percentage){
        this.width = width;
        this.height = height;
        this.percentage = percentage;

        frame = new HBox();
        frame.setMaxSize(width - (width * 0.4), 100);
        frame.setPrefSize(width - (width * 0.4), 100);
        frame.setMinSize(width - (width * 0.4), 100);
        frame.setStyle("-fx-background-color: #FFFFFF");
        frame.setPadding(new Insets(10, 10, 10 ,10));
        frame.setAlignment(Pos.CENTER_LEFT);
        frame.setSpacing(40);

        searchField.setMinSize(width - (width * 0.4) - (120), 80);
        searchField.setMaxSize(width - (width * 0.4) - (120), 80);
        searchField.setPromptText("Escriba el documento para buscar");
        searchField.getStyleClass().add("client-search-bar");

        closeButton.setMinSize(60, 40);
        closeButton.setMaxSize(60, 40);
        closeButton.setText("Cerrar");

        frame.getChildren().addAll(searchField, closeButton);
    }


    public HBox showFrame(){
        return frame;
    }
}
