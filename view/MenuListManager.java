package view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import view.components.ConfirmBox;
import utilities.FA;
import utilities.ProjectEffects;

public class MenuListManager extends MenuList {

    public VBox display(double width, double height, double percentage) {

        Button closeMenu = new Button();
        closeMenu.setText("\u21A9");
        closeMenu.setStyle(closeMenu.getStyle() + "-fx-font-size: " + (80 - (80 * percentage)) + ";");

        Label profile = new Label();
        profile.setFont(Font.loadFont(FA.getFont(), (80 - (80 * percentage))));
        profile.setText(FA.USER);

        Label planLabel = labelGenerator("Crear/Editar Planes " + FA.EDIT, width, height, percentage);
        Label statsClientsLabel = labelGenerator("Estadísticas de Clientes " + FA.BAR_CHART, width, height, percentage);
        Label statsPlansLabel = labelGenerator("Estadísticas de Planes " + FA.AREA_CHART, width, height, percentage);
        Label changePasswordLabel = labelGenerator("Cambiar Contraseña " + FA.KEY, width, height, percentage);
        Label logOutLabel = labelGenerator("Cerrar Sesión " + FA.SIGN_OUT, width, height, percentage);

        planLabel.setAlignment(Pos.CENTER);
        statsClientsLabel.setAlignment(Pos.CENTER);
        statsPlansLabel.setAlignment(Pos.CENTER);
        changePasswordLabel.setAlignment(Pos.CENTER);
        logOutLabel.setAlignment(Pos.CENTER);

        layout.setPrefSize(width * 0.2 + 2, height);
        layout.setMaxSize(width * 0.2 + 2, height);
        layout.getChildren().addAll(profile, separator2(width), separator(width), planLabel,
                separator(width), statsPlansLabel, separator(width), statsClientsLabel, separator(width), changePasswordLabel,
                separator(width), logOutLabel, separator2(width), closeMenu);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-width: 0 10 0 0;" + "-fx-border-color: linear-gradient(to right, #212828, #24222A);" + "-fx-background-color: #212828");
        layout.setPadding(new Insets(20, 0, 20, 0));
        layout.setVisible(false);
        layout.getStylesheets().add("menuListStyle.css");

        //effect closeMenu
        closeMenu.setOnMouseEntered(e -> ProjectEffects.fadeTransition(closeMenu, 700, 1));
        closeMenu.setOnMouseExited(e -> ProjectEffects.stopFadeTransition());

        planLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(2));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();
        });

        statsClientsLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(7));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();
        });

        statsPlansLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(8));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();
        });

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
