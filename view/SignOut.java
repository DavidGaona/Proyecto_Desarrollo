package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import utilities.ConfirmBox;

import java.awt.*;

public class SignOut {
    private double percentage;
    public boolean isShowAble = true;

    private Rectangle separator(double width){
        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(2);
        marginRect1.setWidth(width * 0.1);
        marginRect1.setFill(Color.web("#22282A"));
        return marginRect1;
    }

    private Label labelGenerator(String message, double width, double height){
        double fontLabel = 13 - (13 * percentage);
        javafx.scene.control.Label label = new javafx.scene.control.Label();
        label.setText(message);
        label.setPrefWidth(width * 0.1);
        label.setPrefHeight(height * 0.035);
        label.setStyle("-fx-font-size: " + fontLabel + "px;");

        label.setOnMouseEntered( e -> label.setStyle(label.getStyle() + "-fx-background-color: #171A1C;"));
        label.setOnMouseExited( e -> label.setStyle(label.getStyle() + "-fx-background-color: #22282A;"));

        return label;
    }


    public void display() {
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        double percentageWidth = (1360 - width) / 1360;
        double percentageHeight = (768 - height) / 768;
        percentage = Math.max(percentageWidth, percentageHeight);

        Stage ownerStage = new Stage();
        Stage window = new Stage();

        ownerStage.initStyle(StageStyle.UTILITY);
        ownerStage.setWidth(0); //408
        ownerStage.setHeight(0); //340
        ownerStage.setX(-10);
        ownerStage.setY(-10);
        ownerStage.setOpacity(0);

        window.initOwner(ownerStage);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setWidth(width * 0.10); //408
        window.setHeight(height * 0.25); //340

        Label label1 = labelGenerator("Cambiar Contraseña", width, height);
        label1.setAlignment(Pos.CENTER);
        Label label2 = labelGenerator("Cerrar Sesión",width, height);
        label2.setAlignment(Pos.CENTER);

        label1.setOnMouseClicked(e -> {

        });

        label2.setOnMouseClicked( e ->{
            boolean answer = ConfirmBox.display("Cerrar sesión", "¿ Quieres cerrar sesión?");
            if(answer)
            {
                Login.currentWindow.set(Login.currentWindow.get() + 1);
            }
        });

        window.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                window.close();
                ownerStage.close();
            }
        });

        VBox layout = new VBox();
        layout.getChildren().addAll( label1, separator(width), label2);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #22282A");
        layout.setPadding(new Insets(20, 0, 20, 0));

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Popup.css");

        window.setScene(scene);
        window.setX(width - width * 0.15);
        window.setY(height * 0.097);
        window.setResizable(false);
        ownerStage.show();
        window.show();

    }
}

