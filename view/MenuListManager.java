package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import utilities.ConfirmBox;
import utilities.ProjectEffects;

public class MenuListManager extends MenuList {

    public VBox display(double width, double height, double percentage) {

        Button closeMenu = new Button();
        closeMenu.setText("\u21A9");
        closeMenu.setStyle(closeMenu.getStyle() + "-fx-font-size: " + (80 - (80 * percentage)) + ";");

        Circle profile = new Circle((height * 0.2) / 2);
        profile.setCenterX((height * 0.2) / 2);
        profile.setCenterY((height * 0.2) / 2);
        profile.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
        profile.setStroke(Color.web("#3D3D3E"));

        Label planLabel = labelGenerator("Crear/Editar Planes", width, height, percentage);
        Label statsClientsLabel = labelGenerator("Estadísticas de Clientes", width, height, percentage);
        Label statsPlansLabel = labelGenerator("Estadísticas de Planes", width, height, percentage);
        Label changePasswordLabel = labelGenerator("Cambiar Contraseña", width, height, percentage);
        Label logOutLabel = labelGenerator("Cerrar Sesión", width, height, percentage);

        planLabel.setAlignment(Pos.CENTER);
        statsClientsLabel.setAlignment(Pos.CENTER);
        statsPlansLabel.setAlignment(Pos.CENTER);
        changePasswordLabel.setAlignment(Pos.CENTER);
        logOutLabel.setAlignment(Pos.CENTER);

        layout.setPrefSize(width * 0.2 + 2, height); // height * 0.912
        layout.setMaxSize(width * 0.2 + 2, height); // height * 0.912
        layout.getChildren().addAll(profile, separator2(width), separator(width), planLabel,
                separator(width), statsPlansLabel, separator(width), statsClientsLabel, separator(width), changePasswordLabel,
                separator(width), logOutLabel, separator2(width), closeMenu);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-width: 0 10 0 0;" + "-fx-border-color: linear-gradient(to right, #212828, #24222A);" + "-fx-background-color: #212828");
        layout.setPadding(new Insets(20, 0, 20, 0));
        layout.setVisible(false);
        layout.getStylesheets().add("menuListStyle.css");

        //effect closeMenu
        closeMenu.setOnMouseEntered(e -> ProjectEffects.fadeTransition(closeMenu,700));
        closeMenu.setOnMouseExited(e -> ProjectEffects.stopFadeTransition());

        changePasswordLabel.setOnMouseClicked(e -> {
            boolean answer = ConfirmBox.display("Cambiar Contraseña", "¿Desea Cambiar la Contraseña?", "Si", "No");
            if (answer) {
                Login.currentWindow.set(4);
            }
        });

        logOutLabel.setOnMouseClicked(e -> {
            boolean answer = ConfirmBox.display("Cerrar sesión", "¿Quieres cerrar sesión?", "Sí quiero cerrar", "No quiero cerrar");
            if (answer) {
                Login.currentWindow.set(0);
                Login.currentLoggedUser = -1;
            }
        });

        closeMenu.setOnMouseClicked(e -> ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height));

        return layout;

    }

}
