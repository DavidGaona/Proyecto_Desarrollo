package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class ConfirmBox {

    private static boolean answer;

    public static boolean display(String title, String message, String yesAnswerMessage, String noAnswerMessage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double percentageWidth = (1360 - screenSize.getWidth()) / 1360;
        double percentageHeight = (768 - screenSize.getHeight()) / 768;
        double percentage = Math.max(percentageWidth, percentageHeight);

        //Window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(screenSize.getWidth() * 0.30);
        window.setHeight(screenSize.getHeight() * 0.30);

        //Message label
        Label label = new Label();
        double fontLabel = 20 - (20 * percentage);
        label.setText(message);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: " + fontLabel + "px");

        //Yes/No buttons
        Button yesButton = new Button();
        Button noButton = new Button();
        double fontButton = 16 - (16 * percentage);
        yesButton.setPrefSize(screenSize.getWidth() * 0.12, screenSize.getHeight() * 0.05);
        noButton.setPrefSize(screenSize.getWidth() * 0.12, screenSize.getHeight() * 0.05);
        yesButton.setText(yesAnswerMessage);
        noButton.setText(noAnswerMessage);
        yesButton.setStyle("-fx-font-size: " + fontButton + "px");
        noButton.setStyle("-fx-font-size: " + fontButton + "px");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        //Main layout
        VBox layout = new VBox();
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #22282A;" + "-fx-border-color: black;");
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(20.0);

        //Popup scene
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Popup.css");

        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.setX((screenSize.getWidth() / 2) - ((screenSize.getWidth() * 0.30) / 2));
        window.setY((screenSize.getHeight() / 2) - ((screenSize.getHeight() * 0.25) / 2));
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }

}
