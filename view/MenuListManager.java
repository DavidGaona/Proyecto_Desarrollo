package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import utilities.ConfirmBox;

import java.awt.*;

public class MenuListManager extends MenuList{

    public VBox display() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        double percentageWidth = (1360 - width) / 1360;
        double percentageHeight = (768 - height) / 768;
        percentage = Math.max(percentageWidth, percentageHeight);

        Circle profile = new Circle((height * 0.2)/2);
        profile.setCenterX((height * 0.2)/2);
        profile.setCenterY((height * 0.2)/2);
        profile.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
        profile.setStroke(Color.web("#3D3D3E"));

        Label planLabel = labelGenerator("Crear/Editar Planes", width, height);
        Label statsClients = labelGenerator("Estadísticas de Clientes",width, height);
        Label statsPlans = labelGenerator("Estadísticas de Planes",width, height);
        planLabel.setAlignment(Pos.CENTER);
        statsClients.setAlignment(Pos.CENTER);
        statsPlans.setAlignment(Pos.CENTER);

        Label changePassword = labelGenerator("Cambiar Contraseña", width, height);
        Label logOut = labelGenerator("Cerrar Sesión", width, height);
        changePassword.setAlignment(Pos.CENTER);
        logOut.setAlignment(Pos.CENTER);

        Button closeMenu = new Button();
        closeMenu.setText("Cerrar");
        closeMenu.setPrefSize(width * 0.2058875 , height * 0.05);


        changePassword.setOnMouseClicked(e -> {
            boolean answer = ConfirmBox.display("Cambiar Contraseña", "¿Desea Cambiar la Contraseña?", "Si", "No");
            if(answer) {
                Login.currentWindow.set(4);
            }
        });

        logOut.setOnMouseClicked( e ->{
            boolean answer = ConfirmBox.display("Cerrar sesión", "¿Quieres cerrar sesión?", "Sí quiero cerrar", "No quiero cerrar");
            if(answer) {
                Login.currentWindow.set(0);
                Login.currentLoggedUser = -1;
            }
        });

        layout.setPrefSize(width * 0.2058875, height * 0.912);
        layout.setMaxSize(width * 0.2058875, height * 0.912);
        layout.getChildren().addAll( profile, separator2(width), separator(width), planLabel,
                separator(width), statsPlans, separator(width), statsClients, separator(width), changePassword,
                separator(width), logOut, separator2(width), closeMenu);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #212828");
        layout.setPadding(new Insets(20, 0, 20, 0));
        layout.setVisible(false);
        layout.getStylesheets().add("menuListStyle.css");

        closeMenu.setOnMouseClicked( e ->{
            layout.setVisible(false);
        });

        return layout;

    }

}
