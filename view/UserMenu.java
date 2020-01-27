package view;

import controller.DaoUser;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.User;
import utilities.AlertBox;
import utilities.ProjectUtilities;

public class UserMenu {

    public UserMenu(double percentage, double buttonFont) {
        user = new DaoUser();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private TextField userNameTextField;
    private TextField userLastNameTextField;
    private TextField userDocumentIdTextField;
    private ComboBox<String> userDocumentTypeComboBox;
    private ComboBox<String> userTypeComboBox;
    private Button saveChangesButton;
    private SwitchButton userStateButton;

    private double percentage;
    private DaoUser user;
    private boolean currentUserMode = true;
    private double buttonFont;

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

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Buscar usuario por documento");
        searchTextField.setPrefSize(width * 0.296, height * 0.03); // 0.296 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");
        searchTextField.setId("STF1");
        ProjectUtilities.onlyNumericTextField(searchTextField);

        searchTextField.setOnAction(e -> {
            User searchedUser = user.loadUser(searchTextField.getText());
            if (!searchedUser.isBlank()) {
                ProjectUtilities.resetNodeBorderColor(userNameTextField, userLastNameTextField,
                        userDocumentIdTextField, userDocumentTypeComboBox, userTypeComboBox);

                userNameTextField.setText(searchedUser.getName());
                userLastNameTextField.setText(searchedUser.getLastName());
                userDocumentIdTextField.setText(searchedUser.getDocumentIdNumber());
                userDocumentTypeComboBox.valueProperty().set(ProjectUtilities.convertDocumentTypeString(searchedUser.getDocumentType()));
                userTypeComboBox.valueProperty().set(ProjectUtilities.convertUserTypeString(searchedUser.getType()));
                userStateButton.setSwitchedButton(searchedUser.getState());
                saveChangesButton.setText("Modificar usuario");
                currentUserMode = false;
            }
        });

        ProjectUtilities.focusListener("24222A", "C2B8E0", searchTextField);

        Button newUserButton = userButtonTemplate(width, height, "Nuevo usuario");
        newUserButton.setOnMouseClicked(e -> {
            clearFields();
            ProjectUtilities.resetNodeBorderColor(userNameTextField, userLastNameTextField,
                    userDocumentIdTextField, userDocumentTypeComboBox, userTypeComboBox);
            saveChangesButton.setText("Agregar usuario");
            currentUserMode = true;
            searchTextField.setText("");
            userStateButton.setSwitchedButton(true);
        });

        hBox.getChildren().addAll(marginRect1, newUserButton, marginRect2, searchTextField);
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {
        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.2035);

        double rect2Reduction = 0.05;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.394 - rect2Reduction * 2));

        Button clearButton = userButtonTemplate(width, height, "Limpiar celdas");
        clearButton.setOnMouseClicked(e -> {
            clearFields();
            ProjectUtilities.resetNodeBorderColor(userNameTextField, userLastNameTextField,
                    userDocumentIdTextField, userDocumentTypeComboBox, userTypeComboBox);
            userStateButton.setSwitchedButton(true);
            Login.currentWindow.set(Login.currentWindow.get() + 1);
        });

        saveChangesButton = userButtonTemplate(width, height, "Agregar usuario");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentUserMode) {
                saveNewUser();
            } else {
                editUser();
            }
        });

        hBox.getChildren().addAll(marginRect1, clearButton, marginRect2, saveChangesButton);
        return hBox;
    }

    private void saveNewUser() {
        boolean cbCorrect = isComboBoxCorrect(userTypeComboBox, userDocumentTypeComboBox);
        boolean tfCorrect = isTextFieldCorrect(userNameTextField, userLastNameTextField, userDocumentIdTextField);
        if (cbCorrect && tfCorrect) {
            user.saveNewUser(
                    ProjectUtilities.clearWhiteSpaces(userNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(userLastNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(userDocumentIdTextField.getText()),
                    ProjectUtilities.convertDocumentType(userDocumentTypeComboBox.getValue()),
                    ProjectUtilities.convertUserType(userTypeComboBox.getValue()),
                    userStateButton.switchedOnProperty().get());
        }
    }

    private void editUser() {
        boolean cbCorrect = isComboBoxCorrect(userTypeComboBox, userDocumentTypeComboBox);
        boolean tfCorrect = isTextFieldCorrect(userNameTextField, userLastNameTextField, userDocumentIdTextField);
        if (cbCorrect && tfCorrect) {
            user.editUser(
                    ProjectUtilities.clearWhiteSpaces(userNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(userLastNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(userDocumentIdTextField.getText()),
                    ProjectUtilities.convertDocumentType(userDocumentTypeComboBox.getValue()),
                    ProjectUtilities.convertUserType(userTypeComboBox.getValue()),
                    userStateButton.switchedOnProperty().get());
        }
    }

    private boolean isTextFieldCorrect(TextField... textFields) {
        boolean correct = true;
        for (TextField textField : textFields) {
            if (textField.getText().isBlank()) {
                textField.setStyle(textField.getStyle() + "\n-fx-border-color: #ED1221;");
                //textField.getStyleClass().add("user-text-field-template-wrong");
                correct = false;
            }
        }
        return correct;
    }

    @SafeVarargs
    private boolean isComboBoxCorrect(ComboBox<String>... comboBoxes) {
        boolean correct = true;
        for (ComboBox<String> comboBox : comboBoxes) {
            if (comboBox.getValue() == null) {
                comboBox.setStyle(comboBox.getStyle() + "\n-fx-border-color: #ED1221;");
                //comboBox.getStyleClass().add("user-text-field-template-wrong");
                correct = false;
            }
        }
        return correct;
    }

    private void clearFields() {
        userNameTextField.setText("");
        userNameTextField.setText("");
        userLastNameTextField.setText("");
        userDocumentIdTextField.setText("");
        userDocumentTypeComboBox.valueProperty().set(null);
        userTypeComboBox.valueProperty().set(null);
        AlertBox.display("", "Celdas Limpiadas", "");
    }

    private TextField userTextFieldTemplate() {
        TextField userTextField = new TextField();
        userTextField.getStyleClass().add("client-text-field-template");
        userTextField.setStyle(userTextField.getStyle() + "-fx-font-size: " + (20 - (20 * percentage)) + "px; ");
        userTextField.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        userTextField.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        return userTextField;
    }

    private Text userTextTemplate(String tittle, String color) {
        Text userText = new Text(tittle);
        userText.setFont(new Font("Consolas", 20 - (20 * percentage)));
        userText.setFill(Color.web(color));
        return userText;
    }

    private GridPane personalInfoPane(double width) {

        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(width * 0.4);
        gridPane.setPadding(new Insets(25, 10, 25, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10); // 10
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 0;");


        String textColor = "#948FA3";

        //Image checkImage = new Image(new FileInputStream("C:\\Users\\david\\IdeaProjects\\panes\\src\\Check.png"));
        //final ImageView currentImage = new ImageView();
        //currentImage.setImage(checkImage);

        //name text
        Text userNameText = userTextTemplate("Nombres:", textColor);
        userNameText.setId("T1");

        //name text field actions
        userNameTextField = userTextFieldTemplate();
        userNameTextField.setId("TF1");

        //last name text
        Text userLastNameText = userTextTemplate("Apellidos:", textColor);
        userLastNameText.setId("T2");


        //name text field actions
        userLastNameTextField = userTextFieldTemplate();
        userLastNameTextField.setId("TF2");

        //document id text
        Text userDocumentIdText = userTextTemplate("Número de documento:", textColor);
        userDocumentIdText.setId("T3");

        //Document id text field actions
        userDocumentIdTextField = userTextFieldTemplate();
        userDocumentIdTextField.setId("TF3");
        ProjectUtilities.onlyNumericTextField(userDocumentIdTextField);

        //document type text
        Text userDocumentTypeText = userTextTemplate("Tipo de documento:", textColor);
        userDocumentTypeText.setId("T4");

        //document type combobox
        userDocumentTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypes));
        userDocumentTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        userDocumentTypeComboBox.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        userDocumentTypeComboBox.setStyle(userDocumentTypeComboBox.getStyle() + "-fx-font-size: " + (20 - (20 * percentage)) + "px;");
        userDocumentTypeComboBox.setId("CB4");

        //document type text
        Text userTypeText = userTextTemplate("Tipo de usuario:", textColor);
        userTypeText.setId("T5");

        //document type combobox
        userTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.userTypes));
        userTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        userTypeComboBox.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        userTypeComboBox.setStyle(userDocumentTypeComboBox.getStyle() + "-fx-font-size: " + (20 - (20 * percentage)) + "px;");
        userTypeComboBox.setId("CB5");

        //User state text
        Text userStateText = userTextTemplate("Estado del usuario:", textColor);
        userStateText.setId("T6");

        //User state button
        userStateButton = new SwitchButton(350 - (350 * percentage));
        userStateButton.setOnMouseClicked(e -> userStateButton.invertSwitchedOn());
        userStateButton.setId("UB6");

        //Install listener for color highlight
        ProjectUtilities.focusListener(gridPane,
                userNameTextField, userLastNameTextField,
                userDocumentIdTextField, userDocumentTypeComboBox,
                userTypeComboBox);

        //install listener for length limit
        ProjectUtilities.addTextFieldCharacterLimit(50, userNameTextField, userLastNameTextField);
        ProjectUtilities.addTextFieldCharacterLimit(20, userDocumentIdTextField);

        int colText = 4;
        int colTextField = 5;
        int rowStart = 0;
        //Constrains
        GridPane.setConstraints(userNameText, colText, rowStart);
        GridPane.setHalignment(userNameText, HPos.RIGHT);
        GridPane.setConstraints(userNameTextField, colTextField, rowStart);

        GridPane.setConstraints(userLastNameText, colText, rowStart + 1);
        GridPane.setHalignment(userLastNameText, HPos.RIGHT);
        GridPane.setConstraints(userLastNameTextField, colTextField, rowStart + 1);

        GridPane.setConstraints(userDocumentTypeText, colText, rowStart + 2);
        GridPane.setHalignment(userDocumentTypeText, HPos.RIGHT);
        GridPane.setConstraints(userDocumentTypeComboBox, colTextField, rowStart + 2);

        GridPane.setConstraints(userDocumentIdText, colText, rowStart + 3);
        GridPane.setHalignment(userDocumentIdText, HPos.RIGHT);
        GridPane.setConstraints(userDocumentIdTextField, colTextField, rowStart + 3);

        GridPane.setConstraints(userTypeText, colText, rowStart + 4);
        GridPane.setHalignment(userTypeText, HPos.RIGHT);
        GridPane.setConstraints(userTypeComboBox, colTextField, rowStart + 4);

        GridPane.setConstraints(userStateText, colText, rowStart + 5);
        GridPane.setHalignment(userStateText, HPos.RIGHT);
        GridPane.setConstraints(userStateButton, colTextField, rowStart + 5);

        //Adding all nodes
        gridPane.getChildren().addAll(
                userNameText, userNameTextField,
                userLastNameText, userLastNameTextField,
                userDocumentTypeText, userDocumentTypeComboBox,
                userDocumentIdText, userDocumentIdTextField,
                userTypeText, userTypeComboBox,
                userStateText, userStateButton);

        gridPane.setId("Información Personal");
        return gridPane;
    }

    public BorderPane renderUserEditMenu(double width, double height) {
        EditingMenu menu = new EditingMenu();
        BorderPane userMenu;
        userMenu = menu.renderMenuTemplate(width, height, percentage, personalInfoPane(width));
        userMenu.setTop(topBar((HBox) userMenu.getTop(), width, height));
        userMenu.setBottom(botBar((HBox) userMenu.getBottom(), width, height));
        userMenu.setCenter(userMenu.getCenter());
        return userMenu;
    }

}
