package view;

import controller.DaoUser;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.User;
import utilities.ProjectUtilities;

public class UserMenu {

    public UserMenu(double percentage, double buttonFont) {
        user = new DaoUser();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    EditingPanel personalInfo;

    private ComboBox<String> userDocumentTypeAbbComboBox;
    private Button saveChangesButton;

    private double percentage;
    private DaoUser user;
    private boolean currentUserMode = true;
    private double buttonFont;
    private SignOut signOut = new SignOut();
    private int currentSelectedUser;

    private Button userButtonTemplate(double width, double height, String message) {
        Button button = new Button(message);
        button.setPrefSize(width * 0.15, height * 0.03); //0.10 , 0.03
        button.setStyle("-fx-font-size: " + buttonFont);
        button.getStyleClass().add("client-buttons-template");
        return button;
    }

    private HBox topBar(HBox hBox, double width, double height) {

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.2035);

        double rect2Reduction = 0.05;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.198 - rect2Reduction)); //0.195

        Rectangle marginRect3 = new Rectangle();
        marginRect3.setHeight(0);
        marginRect3.setWidth(width * 0.10125 - (height * 0.045) / 2); //0.1475

        Rectangle marginRect4 = new Rectangle();
        marginRect4.setHeight(0);
        marginRect4.setWidth(width * 0.004);

        Circle logOut = new Circle((height * 0.045) / 2);
        logOut.setCenterX((height * 0.045) / 2);
        logOut.setCenterY((height * 0.045) / 2);
        logOut.setFill(Color.web("#FFFFFF"));
        logOut.setStroke(Color.web("#3D3D3E"));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        logOut.setEffect(shadow);

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Buscar usuario por documento");
        searchTextField.setPrefSize(width * 0.24, height * 0.03); // 0.24 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");
        searchTextField.setId("STF1");
        ProjectUtilities.onlyNumericTextField(searchTextField);

        userDocumentTypeAbbComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypesAbb));
        userDocumentTypeAbbComboBox.setPrefSize(width * 0.052, height * 0.045);
        userDocumentTypeAbbComboBox.setMinSize(width * 0.052, height * 0.045);
        userDocumentTypeAbbComboBox.setStyle(userDocumentTypeAbbComboBox.getStyle() + "-fx-font-size: " + (18 - (18 * percentage)) + "px;");
        userDocumentTypeAbbComboBox.valueProperty().set(ProjectUtilities.documentTypesAbb[1]);

        searchTextField.setOnAction(e -> {
            User searchedUser = user.loadUser(searchTextField.getText(), userDocumentTypeAbbComboBox.getValue());
            if (searchedUser.isNotBlank()) {
                personalInfo.clear();

                currentSelectedUser = searchedUser.getId();
                personalInfo.setTextField("userName", searchedUser.getName());
                personalInfo.setTextField("userLastName", searchedUser.getLastName());
                personalInfo.setTextField("userDocumentNumber", searchedUser.getDocumentIdNumber());
                personalInfo.setComboBox("userDocumentType", ProjectUtilities.convertDocumentTypeString(searchedUser.getDocumentType()));
                personalInfo.setComboBox("userType", ProjectUtilities.convertUserTypeString(searchedUser.getType()));
                personalInfo.setSwitchButton("userState", searchedUser.getState());
                personalInfo.setSwitchButton("userPasswordReset", !searchedUser.isPasswordReset());

                saveChangesButton.setText("Modificar usuario");
                currentUserMode = false;
                //currentUser = userDocumentIdTextField.getText();
            }
        });

        ProjectUtilities.focusListener("24222A", "C2B8E0", searchTextField);

        Button newUserButton = userButtonTemplate(width, height, "Nuevo usuario");
        newUserButton.setOnMouseClicked(e -> {
            personalInfo.clear();
            saveChangesButton.setText("Agregar usuario");
            currentUserMode = true;
            searchTextField.setText("");
            currentSelectedUser = -1;
        });

        logOut.setOnMouseClicked(e -> {
            if (signOut.isShowAble) {
                signOut.display();
                signOut.isShowAble = false;
            } else {
                signOut.isShowAble = true;
            }
        });

        hBox.getChildren().addAll(marginRect1, newUserButton, marginRect2,
                userDocumentTypeAbbComboBox, marginRect4, searchTextField, marginRect3, logOut);
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {
        saveChangesButton = userButtonTemplate(width, height, "Agregar usuario");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentUserMode)
                saveNewUser();
            else
                editUser();
        });

        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(saveChangesButton);
        return hBox;
    }

    private void saveNewUser() {
        if (!personalInfo.isEmpty()) {
            if (user.saveNewUser(
                    currentSelectedUser,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userLastName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userDocumentNumber")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("userDocumentType")),
                    ProjectUtilities.convertUserType(personalInfo.getContent("userType")),
                    personalInfo.getSwitchButtonValue("userState"))
                    == 1){
                personalInfo.clear();
            }
        }
    }

    private void editUser() {
        if (!personalInfo.isEmpty()) {
            if (user.editUser(
                    currentSelectedUser,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userLastName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userDocumentNumber")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("userDocumentType")),
                    ProjectUtilities.convertUserType(personalInfo.getContent("userType")),
                    personalInfo.getSwitchButtonValue("userState"),
                    !personalInfo.getSwitchButtonValue("userPasswordReset"))
                    == 1){
                personalInfo.clear();
            }
        }
    }

    private void personalInfo(double width) {
        personalInfo = new EditingPanel("Información Personal", percentage, width);

        personalInfo.addTextField("userName", "Nombres:");
        personalInfo.addCharacterLimit(50, "userName");

        personalInfo.addTextField("userLastName", "Apellidos:");
        personalInfo.addCharacterLimit(50, "userLastName");

        personalInfo.addTextField("userDocumentNumber", "Número de documento:");
        personalInfo.addCharacterLimit(20, "userDocumentNumber");
        personalInfo.makeFieldNumericOnly("userDocumentNumber");

        personalInfo.addComboBox("userDocumentType", "Tipo de documento:", ProjectUtilities.documentTypes);

        personalInfo.addComboBox("userType", "Tipo de usuario:", ProjectUtilities.userTypes);

        personalInfo.addSwitchButton("userState", "Estado del usuario:", false,
                "Activado", "Desactivado");

        personalInfo.addSwitchButton("userPasswordReset", "Resetear contraseña:", true,
                "Cambio pendiende", "Cambio realizado");

    }

    @SuppressWarnings("DuplicatedCode")
    public BorderPane renderUserEditMenu(double width, double height) {
        personalInfo(width);
        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(personalInfo.sendPane(width, height*0.1));
        BorderPane userMenu;
        userMenu = menu.renderMenuTemplate();
        userMenu.setTop(topBar((HBox) userMenu.getTop(), width, height));
        userMenu.setBottom(botBar((HBox) userMenu.getBottom(), width, height));
        userMenu.setCenter(userMenu.getCenter());
        return userMenu;
    }

}
