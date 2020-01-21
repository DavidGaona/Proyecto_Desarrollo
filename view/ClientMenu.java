package view;

import controller.DaoClient;
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
import model.Client;
import utilities.AlertBox;
import utilities.ProjectUtilities;

public class ClientMenu {

    public ClientMenu(double percentage, double buttonFont) {
        client = new DaoClient();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private TextField clientNameTextField;
    private TextField clientLastNameTextField;
    private TextField clientDocumentIdTextField;
    private TextField clientEmailTextField;
    private TextField clientDirectionTextField;
    private ComboBox<String> clientDocumentTypeComboBox;
    private ComboBox<String> clientTypeComboBox;
    private Button saveChangesButton;

    private double percentage;
    private DaoClient client;
    private boolean currentClientMode = true;
    private double buttonFont;

    private Button clientButtonTemplate(double width, double height, String message){
        Button button = new Button(message);
        button.setPrefSize(width , height);
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
        searchTextField.setPromptText("Buscar cliente por documento");
        searchTextField.setPrefSize(width * 0.296, height * 0.03); // 0.296 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");
        searchTextField.setId("STF1");
        ProjectUtilities.onlyNumericTextField(searchTextField);

        searchTextField.setOnAction(e -> {
            Client searchedClient = client.loadClient(searchTextField.getText());
            if (!searchedClient.isBlank()) {
                ProjectUtilities.resetNodeBorderColor(clientNameTextField, clientLastNameTextField, clientDocumentIdTextField, clientEmailTextField,
                        clientDirectionTextField, clientDocumentTypeComboBox, clientTypeComboBox);

                clientNameTextField.setText(searchedClient.getName());
                clientLastNameTextField.setText(searchedClient.getLastName());
                clientDocumentIdTextField.setText(searchedClient.getDocumentId());
                clientEmailTextField.setText(searchedClient.getEmail());
                clientDirectionTextField.setText(searchedClient.getDirection());
                clientDocumentTypeComboBox.valueProperty().set(ProjectUtilities.convertDocumentTypeString(searchedClient.getDocumentType()));
                clientTypeComboBox.valueProperty().set(ProjectUtilities.convertClientTypeString(searchedClient.getType()));

                saveChangesButton.setText("Modificar cliente");
                currentClientMode = false;
            }
        });

        Button newClientButton = clientButtonTemplate(width * 0.15, height * 0.03, "Nuevo cliente");
        newClientButton.setOnMouseClicked(e -> {
            clearTextFields();
            ProjectUtilities.resetNodeBorderColor(clientNameTextField, clientLastNameTextField, clientDocumentIdTextField, clientEmailTextField,
                    clientDirectionTextField, clientDocumentTypeComboBox, clientTypeComboBox);
            saveChangesButton.setText("Agregar cliente");
            currentClientMode = true;
            searchTextField.setText("");
        });

        hBox.getChildren().addAll(marginRect1, newClientButton, marginRect2, searchTextField);
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {
        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.2035);

        double optimalWidth = 0.15;
        double rect2Reduction = 0.05;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.394 - rect2Reduction * 2));

        Button clearButton = clientButtonTemplate(width * 0.15, height * 0.03,"Limpiar celdas");
        clearButton.setOnMouseClicked(e -> {
            clearTextFields();
            ProjectUtilities.resetNodeBorderColor(clientNameTextField, clientLastNameTextField, clientDocumentIdTextField, clientEmailTextField,
                    clientDirectionTextField, clientDocumentTypeComboBox, clientTypeComboBox);
        });

        saveChangesButton = clientButtonTemplate(width * 0.15, height * 0.03,"Agregar cliente");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentClientMode) {
                saveNewClient();
            } else {
                editClient();
            }
        });

        hBox.getChildren().addAll(marginRect1, clearButton, marginRect2, saveChangesButton);
        return hBox;
    }

    private void saveNewClient() {
        boolean cbCorrect = isComboBoxCorrect(clientTypeComboBox, clientDocumentTypeComboBox);
        boolean tfCorrect = isTextFieldCorrect(clientNameTextField, clientLastNameTextField, clientDocumentIdTextField,
                clientEmailTextField, clientDirectionTextField);
        if (cbCorrect && tfCorrect) {
            client.saveNewClient(
                    ProjectUtilities.clearWhiteSpaces(clientNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(clientLastNameTextField.getText()),
                    ProjectUtilities.convertDocumentType(clientDocumentTypeComboBox.getValue()),
                    ProjectUtilities.clearWhiteSpaces(clientDocumentIdTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(clientEmailTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(clientDirectionTextField.getText()),
                    ProjectUtilities.convertClientType(clientTypeComboBox.getValue()));
        }
    }

    private void editClient() {
        boolean cbCorrect = isComboBoxCorrect(clientTypeComboBox, clientDocumentTypeComboBox);
        boolean tfCorrect = isTextFieldCorrect(clientNameTextField, clientLastNameTextField, clientDocumentIdTextField,
                clientEmailTextField, clientDirectionTextField);
        if (cbCorrect && tfCorrect) {
            client.editClient(
                    ProjectUtilities.clearWhiteSpaces(clientNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(clientLastNameTextField.getText()),
                    ProjectUtilities.convertDocumentType(clientDocumentTypeComboBox.getValue()),
                    ProjectUtilities.clearWhiteSpaces(clientDocumentIdTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(clientEmailTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(clientDirectionTextField.getText()),
                    ProjectUtilities.convertClientType(clientTypeComboBox.getValue()));
        }
    }

    private boolean isTextFieldCorrect(TextField... textFields) {
        boolean correct = true;
        for (TextField textField : textFields) {
            if (textField.getText().isBlank()) {
                textField.setStyle(textField.getStyle() + "\n-fx-border-color: #ED1221;");
                //textField.getStyleClass().add("client-text-field-template-wrong");
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
                //comboBox.getStyleClass().add("client-text-field-template-wrong");
                correct = false;
            }
        }
        return correct;
    }

    private void clearTextFields() {
        clientNameTextField.setText("");
        clientNameTextField.setText("");
        clientLastNameTextField.setText("");
        clientDocumentIdTextField.setText("");
        clientEmailTextField.setText("");
        clientDirectionTextField.setText("");
        clientDocumentTypeComboBox.valueProperty().set(null);
        clientTypeComboBox.valueProperty().set(null);
        AlertBox.display("", "Celdas Limpiadas", "");
    }

    private TextField clientTextFieldTemplate() {
        TextField clientTextField = new TextField();
        clientTextField.getStyleClass().add("client-text-field-template");
        clientTextField.setStyle(clientTextField.getStyle() + "-fx-font-size: "+ (20 - (20 * percentage)) + "px;");
        clientTextField.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientTextField.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        return clientTextField;
    }

    private Text clientTextTemplate(String tittle, String color) {
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 20 - (20 * percentage)));
        clientText.setFill(Color.web(color));
        return clientText;
    }

    private Node selectedNode, lastSelectedNode;

    private void focusListener(GridPane layout, Node... nodes) {
        // Install the same listener on all of them
        for (Node textField : nodes) {
            textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {

                // Set the selectedTextField to null whenever focus is lost. This accounts for the
                // TextField losing focus to another control that is NOT a TextField
                selectedNode = null;

                if (newValue) {
                    // The new node is focused, so set the global reference
                    lastSelectedNode = textField;
                    selectedNode = textField;
                    String textFieldId = selectedNode.getId();
                    selectedNode.setStyle(textField.getStyle() + "\n-fx-border-color: #C2B8E0;");
                    for (Node node : layout.getChildren()) {
                        if (textFieldId.substring(2).equals(node.getId().substring(1))) {
                            ((Text) node).setFill(Color.web("#C2B8E0"));
                            break;
                        }
                    }
                } else {
                    String textFieldId = lastSelectedNode.getId();
                    if (lastSelectedNode != null) {
                        lastSelectedNode.setStyle(textField.getStyle() + "\n-fx-border-color: #3d3d3d;");
                        for (Node node : layout.getChildren()) {
                            if (textFieldId.substring(2).equals(node.getId().substring(1))) {
                                ((Text) node).setFill(Color.web("#948FA3"));
                                break;
                            }
                        }
                    }
                }

            });
        }
    }

    private GridPane personalInfoPane(double width, double height) {

        GridPane gridPane = new GridPane();
        //gridPane.setPrefSize(width * 0.4, height); // 0.4 ,,
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
        Text clientNameText = clientTextTemplate("Nombres:", textColor);
        clientNameText.setId("T1");

        //name text field actions
        clientNameTextField = clientTextFieldTemplate();
        clientNameTextField.setId("TF1");

        //last name text
        Text clientLastNameText = clientTextTemplate("Apellidos:", textColor);
        clientLastNameText.setId("T2");


        //name text field actions
        clientLastNameTextField = clientTextFieldTemplate();
        clientLastNameTextField.setId("TF2");

        //document id text
        Text clientDocumentIdText = clientTextTemplate("Número de documento:", textColor);
        clientDocumentIdText.setId("T3");

        //Document id text field actions
        clientDocumentIdTextField = clientTextFieldTemplate();
        clientDocumentIdTextField.setId("TF3");
        ProjectUtilities.onlyNumericTextField(clientDocumentIdTextField);

        //Email Text
        Text clientEmailText = clientTextTemplate("Email:", textColor);
        clientEmailText.setId("T4");

        //Email TextField
        clientEmailTextField = clientTextFieldTemplate();
        clientEmailTextField.setId("TF4");

        //Direction Text
        Text clientDirectionText = clientTextTemplate("Dirección:", textColor);
        clientDirectionText.setId("T5");

        //Direction TextField
        clientDirectionTextField = clientTextFieldTemplate();
        clientDirectionTextField.setId("TF5");

        //document type text
        Text clientDocumentTypeText = clientTextTemplate("Tipo de documento:", textColor);
        clientDocumentTypeText.setId("T6");

        //document type combobox
        clientDocumentTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypes));
        clientDocumentTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientDocumentTypeComboBox.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientDocumentTypeComboBox.setStyle(clientDocumentTypeComboBox.getStyle() + "-fx-font-size: "+ (20 - (20 * percentage)) + "px;");
        clientDocumentTypeComboBox.setId("CB6");

        //document type text
        Text clientTypeText = clientTextTemplate("Tipo de cliente:", textColor);
        clientTypeText.setId("T7");

        //document type combobox
        clientTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.clientTypes));
        clientTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientTypeComboBox.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientTypeComboBox.setStyle(clientDocumentTypeComboBox.getStyle() + "-fx-font-size: "+ (20 - (20 * percentage)) + "px;");
        clientTypeComboBox.setId("CB7");

        //Install listener for color highlight
        focusListener(gridPane,
                clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField,
                clientDirectionTextField, clientDocumentTypeComboBox,
                clientTypeComboBox);

        //install listener for length limit
        ProjectUtilities.addTextFieldCharacterLimit(50, clientNameTextField, clientLastNameTextField);
        ProjectUtilities.addTextFieldCharacterLimit(20, clientDocumentIdTextField);
        ProjectUtilities.addTextFieldCharacterLimit(256, clientDirectionTextField, clientEmailTextField);

        int colText = 4;
        int colTextField = 5;
        int rowStart = 0;
        //Constrains
        GridPane.setConstraints(clientNameText, colText, rowStart);
        GridPane.setHalignment(clientNameText, HPos.RIGHT);
        GridPane.setConstraints(clientNameTextField, colTextField, rowStart);

        GridPane.setConstraints(clientLastNameText, colText, rowStart + 1);
        GridPane.setHalignment(clientLastNameText, HPos.RIGHT);
        GridPane.setConstraints(clientLastNameTextField, colTextField, rowStart + 1);

        GridPane.setConstraints(clientDocumentTypeText, colText, rowStart + 2);
        GridPane.setHalignment(clientDocumentTypeText, HPos.RIGHT);
        GridPane.setConstraints(clientDocumentTypeComboBox, colTextField, rowStart + 2);

        GridPane.setConstraints(clientDocumentIdText, colText, rowStart + 3);
        GridPane.setHalignment(clientDocumentIdText, HPos.RIGHT);
        GridPane.setConstraints(clientDocumentIdTextField, colTextField, rowStart + 3);

        GridPane.setConstraints(clientEmailText, colText, rowStart + 4);
        GridPane.setHalignment(clientEmailText, HPos.RIGHT);
        GridPane.setConstraints(clientEmailTextField, colTextField, rowStart + 4);

        GridPane.setConstraints(clientDirectionText, colText, rowStart + 5);
        GridPane.setHalignment(clientDirectionText, HPos.RIGHT);
        GridPane.setConstraints(clientDirectionTextField, colTextField, rowStart + 5);

        GridPane.setConstraints(clientTypeText, colText, rowStart + 6);
        GridPane.setHalignment(clientTypeText, HPos.RIGHT);
        GridPane.setConstraints(clientTypeComboBox, colTextField, rowStart + 6);

        //Adding all nodes
        gridPane.getChildren().addAll(
                //currentImage,
                clientNameText, clientNameTextField,
                clientLastNameText, clientLastNameTextField,
                clientDocumentTypeText, clientDocumentTypeComboBox,
                clientDocumentIdText, clientDocumentIdTextField,
                clientEmailText, clientEmailTextField,
                clientDirectionText, clientDirectionTextField,
                clientTypeText, clientTypeComboBox);

        gridPane.setId("Información Personal");
        return gridPane;
    }

    public BorderPane renderClientEditMenu(double width, double height) {
        EditingMenu menu = new EditingMenu();
        BorderPane clientMenu;
        clientMenu = menu.renderMenuTemplate(width, height, percentage, personalInfoPane(width, height));
        clientMenu.setTop(topBar((HBox) clientMenu.getTop(), width, height));
        clientMenu.setBottom(botBar((HBox) clientMenu.getBottom(), width, height));
        clientMenu.setCenter(clientMenu.getCenter());
        return clientMenu;
    }
}
