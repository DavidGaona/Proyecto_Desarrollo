package view;

import controller.DaoUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utilities.AlertBox;
import utilities.ProjectUtilities;



public class UserPasswordChange {


    private TextField textFieldTemplate(double width, double height, String tittle) {
        TextField textField = new PasswordField();
        textField.setPromptText(tittle);
        textField.setPrefSize(width, height);
        textField.setMaxSize(width, height);
        textField.getStyleClass().add("text-field-login");

        return textField;
    }

    private VBox passwordChangePane(double width, double height) {
        double percentageWidth = (2560 - width) / 2560;
        double percentageHeight = (1440 - height) / 1440;
        double percentageChange = Math.max(percentageWidth, percentageHeight);

        double textFFont = 30 - (30 * percentageChange);
        double labelFont = 60 - (60 * percentageChange);
        double buttonFontChange = 40 - (40 * percentageChange);

        VBox background = new VBox();
        background.setStyle("-fx-background-color: #171A1C");
        background.setPadding(new Insets(25, 0, 25, 0));
        background.setSpacing(50 - (50 * percentageChange));
        background.setPrefSize(width * 0.3, height * 0.6);
        background.setMaxWidth(width * 0.3);
        background.setAlignment(Pos.TOP_CENTER);

        background.setStyle("-fx-background-color: #22282A;\n -fx-border-width: 3;\n -fx-border-color: #3C4448;\n -fx-border-radius: 5;");

        Label tittleLabel = new Label("Cambiar contraseña");
        tittleLabel.getStyleClass().add("login-label");
        tittleLabel.setStyle(tittleLabel.getStyle() + "-fx-font-size: " + labelFont + "px;");

        TextField currentPasswordTextField = textFieldTemplate(width * 0.25, height * 0.05, "Contraseña actual");
        TextField newPasswordTextField = textFieldTemplate(width * 0.25, height * 0.05, "Contraseña nueva");
        TextField confirmPasswordTextField = textFieldTemplate(width * 0.25, height * 0.05, "Confirmar nueva contraseña");
        currentPasswordTextField.setStyle(currentPasswordTextField.getStyle() + "-fx-font-size: " + textFFont + "px;");
        newPasswordTextField.setStyle(newPasswordTextField.getStyle() + "-fx-font-size: " + textFFont + "px;");
        confirmPasswordTextField.setStyle(confirmPasswordTextField.getStyle() + "-fx-font-size: " + textFFont + "px;");

        confirmPasswordTextField.setOnKeyTyped(e -> {
            if (newPasswordTextField.getText().equals(confirmPasswordTextField.getText())) {
                if (!confirmPasswordTextField.getText().isEmpty())
                    confirmPasswordTextField.setStyle(confirmPasswordTextField.getStyle() + "-fx-border-color: #008000;");
                else
                    confirmPasswordTextField.setStyle(confirmPasswordTextField.getStyle() + "-fx-border-color: #3985AB;");
            } else {

                confirmPasswordTextField.setStyle(confirmPasswordTextField.getStyle() + "-fx-border-color: #FF0000;");
            }
        });
        ProjectUtilities.addTextFieldCharacterLimit(50, currentPasswordTextField, newPasswordTextField, confirmPasswordTextField);

        Rectangle styleLine = new Rectangle();
        styleLine.setFill(Color.web("#171A1C"));
        styleLine.setHeight(1);
        styleLine.setWidth(width * 0.3);

        Button changePasswordButton = new Button("Cambiar contraseña");
        changePasswordButton.setPrefSize(width * 0.25, height * 0.05);
        changePasswordButton.getStyleClass().add("login-button");
        changePasswordButton.setStyle(changePasswordButton.getStyle() + "-fx-font-size: "+ buttonFontChange + "px;");

        changePasswordButton.setOnMouseClicked(e -> {
            DaoUser user = new DaoUser();
            if (user.checkPassword(Login.currentUser, currentPasswordTextField.getText())) {
                if (newPasswordTextField.getText().equals(confirmPasswordTextField.getText())) {
                    user.changePassword(Login.currentUser, confirmPasswordTextField.getText());
                } else {
                    AlertBox.display("Error", "Las contraseñas no coinciden", "");
                    confirmPasswordTextField.setStyle(confirmPasswordTextField.getStyle() + "-fx-border-color: #FF0000;");
                }
            } else {
                AlertBox.display("Error", "Contraseña incorrecta", "");
            }
        });

        ProjectUtilities.focusListener("3C4448", "3985AB", currentPasswordTextField, newPasswordTextField, confirmPasswordTextField);

        background.getChildren().addAll(
                tittleLabel, currentPasswordTextField,
                styleLine, newPasswordTextField,
                confirmPasswordTextField, changePasswordButton
        );

        return background;
    }

    private boolean passwordMeetsStandard(String password){
        return (password.length() > 7);
    }

    public VBox BackGroundPane(double width, double height) {

        VBox background = new VBox();
        background.setStyle("-fx-background-color: #171A1C");
        background.setPrefSize(width, height);

        background.getChildren().add(passwordChangePane(width, height));
        background.setAlignment(Pos.CENTER);
        return background;
    }
}
