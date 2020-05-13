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

public class MenuListAdmin extends MenuList {


    public VBox display(double width, double height, double percentage) {

        Button closeMenu = new Button();
        closeMenu.setText("\u21A9");
        closeMenu.setStyle(closeMenu.getStyle() + "-fx-font-size: " + (80 - (80 * percentage)) + ";");

        Label profile = new Label();
        profile.setFont(Font.loadFont(FA.getFont(), (80 - (80 * percentage))));
        profile.setText(FA.USER_SHIELD);

        Label userLabel = labelGenerator("Crear/Editar Usuario " + FA.USER_PLUS, width, height, percentage);
        Label billingLabel = labelGenerator("Facturación " + FA.COG, width, height, percentage);
        Label historyUsers = labelGenerator("Historial de Actividad " + FA.USERS, width, height, percentage);
        Label bankLabel = labelGenerator("Crear/Editar Banco " + FA.BANK, width, height, percentage);
        Label changePasswordLabel = labelGenerator("Cambiar Contraseña " + FA.KEY, width, height, percentage);
        Label logOutLabel = labelGenerator("Cerrar Sesión " + FA.SIGN_OUT, width, height, percentage);

        userLabel.setAlignment(Pos.CENTER);
        billingLabel.setAlignment(Pos.CENTER);
        historyUsers.setAlignment(Pos.CENTER);
        bankLabel.setAlignment(Pos.CENTER);
        changePasswordLabel.setAlignment(Pos.CENTER);
        logOutLabel.setAlignment(Pos.CENTER);

        layout.setPrefSize(width * 0.2 + 2, height);
        layout.setMaxSize(width * 0.2 + 2, height);
        layout.getChildren().addAll(
                profile, separator2(width), billingLabel, separator(width),
                bankLabel, separator(width), historyUsers, separator(width), userLabel, separator(width), changePasswordLabel,
                separator(width), logOutLabel, separator2(width), closeMenu
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-border-width: 0 10 0 0;" + "-fx-border-color: linear-gradient(to right, #212828, #24222A);" + "-fx-background-color: #212828");
        layout.setPadding(new Insets(20, 0, 20, 0));
        layout.setVisible(false);
        layout.getStylesheets().add("menuListStyle.css");

        //effect closeMenu
        closeMenu.setOnMouseEntered(e -> ProjectEffects.fadeTransition(closeMenu, 700, 1));
        closeMenu.setOnMouseExited(e -> ProjectEffects.stopFadeTransition());

        billingLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(6));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();
        });

        userLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(3));
            ProjectEffects.linearTransitionToRight(layout, 250, -width, height, -width, height);
            p.play();

        });

        bankLabel.setOnMouseClicked(e -> {
            PauseTransition p = new PauseTransition(Duration.millis(250));
            p.setOnFinished(ex -> Login.currentWindow.set(5));
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
