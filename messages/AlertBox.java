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

public class AlertBox {

    public static void display(String title, String message, String message2) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(screenSize.getWidth() * 0.30);
        window.setHeight(screenSize.getHeight() * 0.25);

        Label label = new Label();
        label.setText(message);
        Label label2 = new Label();
        label2.setText(message2);

        Button closeButton = new Button();
        closeButton.setText("Cerrar");
        closeButton.setOnAction(e -> window.close());
        closeButton.setPrefSize(screenSize.getWidth() * 0.12, screenSize.getHeight() * 0.025);

        VBox layout = new VBox();
        if(message2 == "") {
            layout.getChildren().addAll(label, closeButton);
        }
        else{
            layout.getChildren().addAll(label,label2, closeButton);
        }
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #22282A");
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(20);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Popup.css");

        window.setScene(scene);
        window.setX((screenSize.getWidth()/2) - ((screenSize.getWidth() * 0.3)/2));
        window.setY((screenSize.getHeight()/2) - ((screenSize.getHeight() * 0.25)/2));
        window.setResizable(false);
        window.showAndWait();

    }
}
