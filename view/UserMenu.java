package view;

import controller.DaoUser;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.User;
import utilities.Icons;
import utilities.ProjectUtilities;
import utilities.ProjectEffects;
import view.components.SearchPane;

public class UserMenu {

    public UserMenu(double percentage, double buttonFont) {
        user = new DaoUser();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    EditingPanel personalInfo;

    private Button saveChangesButton;
    private SearchPane searchPane;

    private double percentage;
    private DaoUser user;
    private boolean currentUserMode = true;
    private double buttonFont;
    private MenuListAdmin menuListAdmin = new MenuListAdmin();
    private VBox menuList;
    private int currentSelectedUser;

    private Button userButtonTemplate(double width, double height, String message) {
        Button button = new Button(message);
        button.setPrefSize(width * 0.15, height * 0.03); //0.10 , 0.03
        button.setStyle("-fx-font-size: " + buttonFont);
        button.getStyleClass().add("client-buttons-template");
        return button;
    }

    private HBox topBar(HBox hBox, double width, double height) {

        double reduction;
        double circleRadius = (height * 0.045) / 2;

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.10 - circleRadius);

        Circle menuCircle = new Circle(circleRadius);
        menuCircle.setCenterX(circleRadius);
        menuCircle.setCenterY(circleRadius);
        menuCircle.setFill(Color.web("#FFFFFF"));
        menuCircle.setStroke(Color.web("#3D3D3E"));

        reduction = circleRadius;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth((width * 0.10) - reduction);

        Button newUserButton = userButtonTemplate(width, height, "Nuevo usuario");

        reduction += (width * 0.15);

        Rectangle marginRect3 = new Rectangle();
        marginRect3.setHeight(0);
        marginRect3.setWidth(width * 0.3 - reduction);

        Icons icons = new Icons();
        icons.searchIcon(percentage);
        icons.getSearchIcon().setOnAction(e -> {
            searchPane.setVisible(true);
            searchPane.getSearchField().requestFocus();
        });

        Rectangle marginRect4 = new Rectangle();
        marginRect4.setHeight(0);
        marginRect4.setWidth(width * 0.004);

        searchPane.getSearchField().setOnAction(e -> {
            User searchedUser = user.loadUser(searchPane.getTextContent(), searchPane.getDocumentType());
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
                searchPane.getSearchField().setText("");
            }
        });

        newUserButton.setOnMouseClicked(e -> {
            personalInfo.clear();
            saveChangesButton.setText("Agregar usuario");
            currentUserMode = true;
            currentSelectedUser = -1;
        });

        menuCircle.setOnMouseClicked(e -> {
            menuListAdmin.displayMenu();
            ProjectEffects.linearTransitionToRight(menuList, width, height, width, height);
        });

        hBox.getChildren().addAll(marginRect1, menuCircle, marginRect2, newUserButton, icons.getSearchIcon(), marginRect3,
                marginRect4);
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
            user.saveNewUser(
                    currentSelectedUser,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userLastName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userDocumentNumber")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("userDocumentType")),
                    ProjectUtilities.convertUserType(personalInfo.getContent("userType")),
                    personalInfo.getSwitchButtonValue("userState"));
            personalInfo.clear();
        }
    }

    private void editUser() {
        if (!personalInfo.isEmpty()) {
            user.editUser(
                    currentSelectedUser,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userLastName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userDocumentNumber")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("userDocumentType")),
                    ProjectUtilities.convertUserType(personalInfo.getContent("userType")),
                    personalInfo.getSwitchButtonValue("userState"),
                    !personalInfo.getSwitchButtonValue("userPasswordReset"));
            personalInfo.clear();
        }
    }

    private void focuss(){

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
    public StackPane renderUserEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();
        personalInfo(width);

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(personalInfo.sendPane(width, height * 0.1));

        menuList = menuListAdmin.display(width, height, percentage);

        searchPane = new SearchPane(width, height, percentage);
        HBox sp = searchPane.showFrame();

        BorderPane userMenu;
        userMenu = menu.renderMenuTemplate();
        userMenu.setTop(topBar((HBox) userMenu.getTop(), width, height));
        userMenu.setBottom(botBar((HBox) userMenu.getBottom(), width, height));
        userMenu.setCenter(userMenu.getCenter());

        stackPane.getChildren().addAll(userMenu, menuList, sp);
        stackPane.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(sp, Pos.CENTER);
        return stackPane;
    }

}
