package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import utilities.ProjectUtilities;
import view.components.AlertBox;

import java.util.concurrent.ThreadLocalRandom;


public class Start {

    private double percentage;
    private double buttonFont;

    public Start(double percentage, double buttonFont){
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    public VBox show(double width, double height){
        VBox vBox = new VBox();
        vBox.setPrefSize(width, height);
        vBox.setStyle("-fx-background-color: #171A1C");
        vBox.setSpacing(50);
        vBox.setAlignment(Pos.CENTER);

        VBox center = new VBox();
        center.setMinSize(width * 0.25, height * 0.35);
        center.setMaxSize(width * 0.25, height * 0.35);
        center.setStyle("-fx-background-color: #22282A;\n -fx-border-color: #3C4448;\n -fx-border-width: 2");
        center.setSpacing(50);
        center.setPadding(new Insets(50, 0, 50, 0));
        center.setAlignment(Pos.TOP_CENTER);

        Label label = new Label("Introdusca su número de celular");
        label.setMinSize(500, 120);
        label.setMaxSize(500, 120);
        label.setWrapText(true);

        TextField textField = new TextField();
        textField.setMinSize(350, 75);
        textField.setMaxSize(350, 75);
        ProjectUtilities.onlyNumericTextField(textField);
        textField.setOnAction(e -> {
            if (textField.getText().isBlank())
                AlertBox.display("Error", "introdusca un número de celular valido:");
            AlertBox.display("Consulta", "Su consumo de datos es de: " +
                    ThreadLocalRandom.current().nextInt(0, 10000) + " Megas");
        });

        Button button = new Button("Consultar");
        button.setMinSize(350, 75);
        button.setMaxSize(350, 75);
        button.setOnAction(e -> {
            if (textField.getText().isBlank())
                AlertBox.display("Error", "introdusca un número de celular valido:");
            AlertBox.display("Consulta", "Su consumo de datos es de: " +
                    ThreadLocalRandom.current().nextInt(0, 10000) + " Megas");
        });

        center.getChildren().addAll(label, textField, button);
        vBox.getChildren().add(center);
        return vBox;

    }

}
