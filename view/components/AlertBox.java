package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class AlertBox {

    public static void display(String title, String message) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double percentageWidth = (1360 - screenSize.getWidth()) / 1360;
        double percentageHeight = (768 - screenSize.getHeight()) / 768;
        double percentage = Math.max(percentageWidth, percentageHeight);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(screenSize.getWidth() * 0.30);
        window.setHeight(screenSize.getHeight() * 0.25);

        Label label = new Label();
        double fontLabel = 20 - (20 * percentage);
        label.setText(message);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-font-size: " + fontLabel + "px;");

        Button closeButton = new Button();
        double fontButton = 16 - (16 * percentage);
        closeButton.setText("Cerrar");
        closeButton.setOnAction(e -> window.close());
        closeButton.setPrefSize(screenSize.getWidth() * 0.12, screenSize.getHeight() * 0.025);
        closeButton.setStyle("-fx-font-size: " + fontButton + "px");

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #22282A;" + "-fx-border-color: black;");
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(20);
        layout.getChildren().addAll(label, closeButton);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Popup.css");

        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.setX((screenSize.getWidth() / 2) - ((screenSize.getWidth() * 0.3) / 2));
        window.setY((screenSize.getHeight() / 2) - ((screenSize.getHeight() * 0.25) / 2));
        window.setResizable(false);
        window.showAndWait();

    }
}
