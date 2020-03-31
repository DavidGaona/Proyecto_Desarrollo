package view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import utilities.ConfirmBox;
import utilities.ProjectEffects;
import utilities.ProjectUtilities;

import java.util.concurrent.TimeUnit;

public class MenuListAdmin extends MenuList {


    public VBox display(double width, double height, double percentage) {

        Button closeMenu = new Button();
        closeMenu.setText("\u21A9");
        closeMenu.setStyle(closeMenu.getStyle() + "-fx-font-size: " + (80 - (80 * percentage)) + ";");

        Circle profile = new Circle((height * 0.2) / 2);
        profile.setCenterX((height * 0.2) / 2);
        profile.setCenterY((height * 0.2) / 2);
        profile.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
        profile.setStroke(Color.web("#3D3D3E"));

        Label userLabel = labelGenerator("Crear/Editar Usuario", width, height, percentage);
        Label controlLabel = labelGenerator("Control", width, height, percentage);
        Label listUsersLabel = labelGenerator("Listar Usuarios", width, height, percentage);
        Label statsUsers = labelGenerator("Estadísticas de Usuarios", width, height, percentage);
        Label bankLabel = labelGenerator("Crear/Editar Banco", width, height, percentage);
        Label changePasswordLabel = labelGenerator("Cambiar Contraseña", width, height, percentage);
        Label logOutLabel = labelGenerator("Cerrar Sesión", width, height, percentage);

        userLabel.setAlignment(Pos.CENTER);
        controlLabel.setAlignment(Pos.CENTER);
        listUsersLabel.setAlignment(Pos.CENTER);
        statsUsers.setAlignment(Pos.CENTER);
        bankLabel.setAlignment(Pos.CENTER);
        changePasswordLabel.setAlignment(Pos.CENTER);
        logOutLabel.setAlignment(Pos.CENTER);

        layout.setPrefSize(width * 0.2 + 2, height); // height * 0.912
        layout.setMaxSize(width * 0.2 + 2, height); // height * 0.912
        layout.getChildren().addAll(
                profile, separator2(width), controlLabel, separator(width), listUsersLabel,
                separator(width), bankLabel, separator(width), userLabel, separator(width), statsUsers, separator(width), changePasswordLabel,
                separator(width), logOutLabel, separator2(width), closeMenu
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-width: 0 10 0 0;" + "-fx-border-color: linear-gradient(to right, #212828, #24222A);" + "-fx-background-color: #212828");
        layout.setPadding(new Insets(20, 0, 20, 0));
        layout.setVisible(false);
        layout.getStylesheets().add("menuListStyle.css");

        //effect closeMenu
        closeMenu.setOnMouseEntered(e -> ProjectEffects.fadeTransition(closeMenu,700));
        closeMenu.setOnMouseExited(e -> ProjectEffects.stopFadeTransition());

        userLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(3));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();


        });

        bankLabel.setOnMouseClicked( e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(5));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();
        });

        changePasswordLabel.setOnMouseClicked(e -> {
            boolean answer = ConfirmBox.display("Cambiar Contraseña", "¿Desea Cambiar la Contraseña?", "Si", "No");
            if (answer) {
                Login.currentWindow.set(3);
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
