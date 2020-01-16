package messages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class ConfirmBox {

    static boolean answer;

    public static boolean display(String title, String message) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(screenSize.getWidth() * 0.30);
        window.setHeight(screenSize.getHeight() * 0.25);

        //Message label
        Label label = new Label();
        label.setText(message);

        //Yes/No buttons
        Button yesButton = new Button();
        Button noButton = new Button();
        yesButton.setPrefSize(screenSize.getWidth() * 0.12, screenSize.getHeight() * 0.05);
        noButton.setPrefSize(screenSize.getWidth() * 0.12, screenSize.getHeight() * 0.05);
        yesButton.setText("Sí quiero cerrar");
        noButton.setText("No quiero cerrar");

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
        layout.setStyle("-fx-background-color: #22282A");
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(20.0);

        //Popup scene
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Popup.css");

        window.setScene(scene);
        window.setX((screenSize.getWidth()/2) - ((screenSize.getWidth() * 0.30)/2));
        window.setY((screenSize.getHeight()/2) - ((screenSize.getHeight() * 0.25)/2));
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }

}
