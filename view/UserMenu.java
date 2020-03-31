package view;

import controller.DaoUser;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.User;
import utilities.Icons;
import utilities.ProjectUtilities;
import utilities.ProjectEffects;
import utilities.AlertBox;
import view.components.SearchPane;

public class UserMenu {

    public UserMenu(double percentage, double buttonFont) {
        user = new DaoUser();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private EditingPanel personalInfo;
    private SearchPane searchPane;
    private Button saveChangesButton;

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
        hBox.setPadding(new Insets(0, 0, 0, ((width * 0.10) - circleRadius)));

        Circle menuCircle = new Circle(circleRadius);
        menuCircle.setCenterX(circleRadius);
        menuCircle.setCenterY(circleRadius);
        menuCircle.setFill(Color.web("#FFFFFF"));
        menuCircle.setStroke(Color.web("#3D3D3E"));

        Button newUserButton = userButtonTemplate(width, height, "Nuevo usuario");

        Icons icons = new Icons();
        icons.searchIcon(percentage);
        icons.getSearchIcon().setOnAction(e -> {
            searchPane.setVisible(true);
            searchPane.giveFocus();
        });

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
                searchPane.setVisible(false);
            } else
                AlertBox.display("Error: ", "Usuario no encontrado", "");
        });

        newUserButton.setOnMouseClicked(e -> {
            personalInfo.clear();
            saveChangesButton.setText("Agregar usuario");
            currentUserMode = true;
            currentSelectedUser = -1;
        });

        menuCircle.setOnMouseClicked(e -> {
            menuListAdmin.displayMenu();
            ProjectEffects.linearTransitionToRight(menuList, 250, width, height, width, height);
        });

        saveChangesButton = userButtonTemplate(width, height, "Agregar usuario");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentUserMode)
                saveNewUser();
            else
                editUser();
        });

        hBox.getChildren().addAll(menuCircle, newUserButton, icons.getSearchIcon(), saveChangesButton);
        HBox.setMargin(menuCircle, new Insets(0, ((width * 0.10) - circleRadius), 0, 0));
        HBox.setMargin(icons.getSearchIcon(), new Insets(0, (width * 0.1355), 0, (width * 0.135)));

        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private void saveNewUser() {
        String message = "No se pueden dejar campos vacios";
        if (!personalInfo.isEmpty()) {
            message = user.saveNewUser(
                    currentSelectedUser,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userLastName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userDocumentNumber")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("userDocumentType")),
                    ProjectUtilities.convertUserType(personalInfo.getContent("userType")),
                    personalInfo.getSwitchButtonValue("userState"));
            AlertBox.display("Error ", message, "");
            personalInfo.clear();
        } else {
            AlertBox.display("Error", message, "");
        }

    }

    private void editUser() {
        String message = "No se pueden dejar campos vacios";
        if (!personalInfo.isEmpty()) {
            message = user.editUser(
                    currentSelectedUser,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userLastName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("userDocumentNumber")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("userDocumentType")),
                    ProjectUtilities.convertUserType(personalInfo.getContent("userType")),
                    personalInfo.getSwitchButtonValue("userState"),
                    !personalInfo.getSwitchButtonValue("userPasswordReset"));
            AlertBox.display("Error ", message, "");
            personalInfo.clear();
        } else {
            AlertBox.display("Error ", message, "");
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
    public StackPane renderUserEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();
        personalInfo(width);

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(personalInfo.sendPane(width, height * 0.1));
        menu.centerPane();

        menuList = menuListAdmin.display(width, height, percentage);

        searchPane = new SearchPane(width, height, percentage);
        HBox sp = searchPane.showFrame();

        BorderPane userMenu;
        userMenu = menu.renderMenuTemplate();
        userMenu.setTop(topBar((HBox) userMenu.getTop(), width, height));
        userMenu.setBottom(botBar((HBox) userMenu.getBottom(), width, height));
        userMenu.setCenter(userMenu.getCenter());

        Rectangle r1 = new Rectangle(0, 0, width, height * 0.9);
        r1.setOnMouseClicked(e -> searchPane.setVisible(false));
        r1.setOnTouchPressed(e -> searchPane.setVisible(false));
        r1.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 3;
            double widthSpeed = menu.getScrollPane().getContent().getBoundsInLocal().getWidth();
            double value = menu.getScrollPane().getVvalue();
            menu.getScrollPane().setVvalue(value + -deltaY / widthSpeed);
        });
        r1.setFill(Color.rgb(0, 0, 0, 0.25));
        r1.visibleProperty().bind(searchPane.getIsVisible());

        stackPane.getChildren().addAll(userMenu, menuList, r1, sp);
        stackPane.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(r1, Pos.CENTER_LEFT);
        StackPane.setAlignment(sp, Pos.CENTER);
        return stackPane;
    }

}
