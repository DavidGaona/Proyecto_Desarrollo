package messages;

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

    public static boolean display(String title, String message)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(screenSize.getWidth() * 0.30);
        window.setMinHeight(screenSize.getHeight() * 0.25);

        Label label = new Label();
        label.setText(message);

        //
        Button yesButton = new Button();
        Button noButton = new Button();
        yesButton.setText("Sí quiero salir del programa");
        noButton.setText("No quiero salir del programa");

        yesButton.setOnAction( e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction( e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,yesButton,noButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #22282A");
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Popup.css");
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();

        return answer;
    }

}
