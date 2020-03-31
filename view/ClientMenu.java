package view;

import controller.DaoClient;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Client;
import utilities.AlertBox;
import utilities.ProjectUtilities;
import view.components.SignOut;

public class ClientMenu {

    public ClientMenu(double percentage, double buttonFont) {
        client = new DaoClient();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    EditingPanel personalInfo;

    private ComboBox<String> clientDocumentTypeAbbComboBox;
    private Button saveChangesButton;
    private int currentClient = -1;

    private double percentage;
    private DaoClient client;
    private boolean currentClientMode = true;
    private double buttonFont;
    private SignOut signOut = new SignOut();

    private Button clientButtonTemplate(double width, double height, String message) {
        Button button = new Button(message);
        button.setPrefSize(width, height);
        button.getStyleClass().add("client-buttons-template");
        button.setStyle("-fx-font-size: " + buttonFont);

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
        searchTextField.setPromptText("Buscar cliente por documento");
        searchTextField.setPrefSize(width * 0.24, height * 0.03); // 0.24 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");
        searchTextField.setId("STF1");
        ProjectUtilities.onlyNumericTextField(searchTextField);

        clientDocumentTypeAbbComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypesAbb));
        clientDocumentTypeAbbComboBox.setPrefSize(width * 0.052, height * 0.045);
        clientDocumentTypeAbbComboBox.setMinSize(width * 0.052, height * 0.045);
        clientDocumentTypeAbbComboBox.setStyle(clientDocumentTypeAbbComboBox.getStyle() + "-fx-font-size: " + (18 - (18 * percentage)) + "px;");
        clientDocumentTypeAbbComboBox.valueProperty().set(ProjectUtilities.documentTypesAbb[1]);

        searchTextField.setOnAction(e -> {
            Client searchedClient = client.loadClient(searchTextField.getText(), clientDocumentTypeAbbComboBox.getValue());
            if (!searchedClient.isBlank()) {
                personalInfo.clear();

                currentClient = searchedClient.getId();
                personalInfo.setTextField("clientName", searchedClient.getName());
                personalInfo.setTextField("clientLastName", searchedClient.getLastName());
                personalInfo.setTextField("clientDocumentNumber", searchedClient.getDocumentId());
                personalInfo.setTextField("clientEmail", searchedClient.getEmail());
                personalInfo.setTextField("clientAddress", searchedClient.getDirection());
                personalInfo.setComboBox("clientDocumentType", ProjectUtilities.convertDocumentTypeString(searchedClient.getDocumentType()));
                personalInfo.setComboBox("clientType", ProjectUtilities.convertDocumentTypeString(searchedClient.getType()));

                saveChangesButton.setText("Modificar cliente");
                currentClientMode = false;
            }
        });

        ProjectUtilities.focusListener("24222A", "C2B8E0", searchTextField);

        Button newClientButton = clientButtonTemplate(width * 0.15, height * 0.03, "Nuevo cliente");
        newClientButton.setOnMouseClicked(e -> {
            personalInfo.clear();
            saveChangesButton.setText("Agregar cliente");
            currentClientMode = true;
            searchTextField.setText("");
        });

        logOut.setOnMouseClicked(e -> signOut.display());

        hBox.getChildren().addAll(marginRect1, newClientButton, marginRect2,
                clientDocumentTypeAbbComboBox, marginRect4, searchTextField, marginRect3, logOut);
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {
        saveChangesButton = clientButtonTemplate(width * 0.15, height * 0.03, "Agregar cliente");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentClientMode)
                saveNewClient();
            else
                editClient();
        });

        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(saveChangesButton);
        return hBox;
    }

    private void saveNewClient() {
        String message = "No se pueden dejar campos vacios";
        if (!personalInfo.isEmpty()) {
            message = client.saveNewClient(
                    -1,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientLastName")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("clientDocumentType")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientDocumentNumber")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientEmail")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientAddress")),
                    ProjectUtilities.convertClientType(personalInfo.getContent("clientType")),
                    Login.currentLoggedUser);
            personalInfo.clear();
        }
        AlertBox.display("Error", message, "");
    }

    private void editClient() {
        String message = "No se pueden dejar campos vacios";
        if (!personalInfo.isEmpty()) {
            message = client.editClient(
                    currentClient,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientLastName")),
                    ProjectUtilities.convertDocumentType(personalInfo.getContent("clientDocumentType")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientDocumentNumber")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientEmail")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientAddress")),
                    ProjectUtilities.convertClientType(personalInfo.getContent("clientType")));
            personalInfo.clear();
        }
        AlertBox.display("Error", message, "");
    }

    private void personalInfo(double width) {
        personalInfo = new EditingPanel("Información Personal", percentage, width);

        personalInfo.addTextField("clientName", "Nombres:");
        personalInfo.addCharacterLimit(50, "clientName");

        personalInfo.addTextField("clientLastName", "Apellido:");
        personalInfo.addCharacterLimit(50, "clientLastName");

        personalInfo.addTextField("clientDocumentNumber", "Número de documento:");
        personalInfo.addCharacterLimit(20, "clientDocumentNumber");
        personalInfo.makeFieldNumericOnly("clientDocumentNumber");

        personalInfo.addTextField("clientEmail", "Email:");
        personalInfo.addCharacterLimit(256, "clientEmail");

        personalInfo.addTextField("clientAddress", "Dirección:");
        personalInfo.addCharacterLimit(256, "clientAddress");

        personalInfo.addComboBox("clientDocumentType", "Tipo de documento:", ProjectUtilities.documentTypes);

        personalInfo.addComboBox("clientType", "Tipo de cliente:", ProjectUtilities.clientTypes);

    }

    public StackPane renderClientEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();
        personalInfo(width);
        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(personalInfo.sendPane(width, height * 0.1));
        BorderPane clientMenu;
        clientMenu = menu.renderMenuTemplate();
        clientMenu.setTop(topBar((HBox) clientMenu.getTop(), width, height));
        clientMenu.setBottom(botBar((HBox) clientMenu.getBottom(), width, height));
        clientMenu.setCenter(clientMenu.getCenter());
        stackPane.getChildren().addAll(clientMenu);
        stackPane.setAlignment(Pos.TOP_LEFT);
        return stackPane;
    }
}
