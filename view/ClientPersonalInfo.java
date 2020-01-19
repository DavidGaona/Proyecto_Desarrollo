package view;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.AlertBox;
import utilities.ProjectUtilities;

public class ClientPersonalInfo {
    public static TextField clientNameTextField;
    public static TextField clientLastNameTextField;
    public static TextField clientDocumentIdTextField;
    public static TextField clientEmailTextField;
    public static TextField clientDirectionTextField;
    public static ComboBox<String> clientDocumentTypeComboBox;
    public static ComboBox<String> clientTypeComboBox;
    public static double percentage;

    public static void clearTextFields() {
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

    public static TextField clientTextFieldTemplate(String tittle) {
        TextField clientTextField = new TextField(tittle);
        clientTextField.getStyleClass().add("client-text-field-template");
        clientTextField.setFont(new Font("Consolas", 20 - (20 * percentage)));
        clientTextField.setPrefSize(350 - (350 * percentage), 30 - (30 * percentage));
        return clientTextField;
    }

    private static Text clientTextTemplate(String tittle, String color) {
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 20 - (20 * percentage)));
        clientText.setFill(Color.web(color));
        return clientText;
    }

    private static Node selectedNode, lastSelectedNode;

    private static void focusListener(GridPane layout, Node... nodes) {
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

    private static void addTextFieldCharacterLimit(int limit, TextField... textFields) {
        for (TextField textField : textFields) {
            textField.textProperty().addListener(e -> {
                if (textField.getText().length() > limit) {
                    int prevPos = textField.getCaretPosition();
                    String limitedText = textField.getText().substring(0, prevPos) + textField.getText().substring(prevPos + 1);
                    textField.setText(limitedText);
                    textField.positionCaret(prevPos);
                }
            });
        }
    }

    public static GridPane personalInfoPane(double width, double height) {

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
        clientNameTextField = clientTextFieldTemplate("");
        clientNameTextField.setId("TF1");


        //last name text
        Text clientLastNameText = clientTextTemplate("Apellidos:", textColor);
        clientLastNameText.setId("T2");


        //name text field actions
        clientLastNameTextField = clientTextFieldTemplate("");
        clientLastNameTextField.setId("TF2");

        //document id text
        Text clientDocumentIdText = clientTextTemplate("Número de documento:", textColor);
        clientDocumentIdText.setId("T3");

        //Document id text field actions
        clientDocumentIdTextField = clientTextFieldTemplate("");
        clientDocumentIdTextField.setId("TF3");
        ProjectUtilities.onlyNumericTextField(clientDocumentIdTextField);

        //Email Text
        Text clientEmailText = clientTextTemplate("Email:", textColor);
        clientEmailText.setId("T4");

        //Email TextField
        clientEmailTextField = clientTextFieldTemplate("");
        clientEmailTextField.setId("TF4");

        //Direction Text
        Text clientDirectionText = clientTextTemplate("Dirección:", textColor);
        clientDirectionText.setId("T5");

        //Direction TextField
        clientDirectionTextField = clientTextFieldTemplate("");
        clientDirectionTextField.setId("TF5");

        //document type text
        Text clientDocumentTypeText = clientTextTemplate("Tipo de documento:", textColor);
        clientDocumentTypeText.setId("T6");

        //document type combobox
        clientDocumentTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypes));
        clientDocumentTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientDocumentTypeComboBox.setId("CB6");
        //clientDocumentTypeComboBox.setOnAction(e -> client.setType(utilities.ProjectUtilities.convertDocumentType(clientDocumentTypeComboBox.getValue())));

        //document type text
        Text clientTypeText = clientTextTemplate("Tipo de cliente:", textColor);
        clientTypeText.setId("T7");

        //document type combobox
        clientTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.clientTypes));
        clientTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientTypeComboBox.setId("CB7");
        //clientTypeComboBox.setOnAction(e -> client.setType(utilities.ProjectUtilities.convertClientType(clientTypeComboBox.getValue())));

        //Install listener for color highlight
        focusListener(gridPane,
                clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField,
                clientDirectionTextField, clientDocumentTypeComboBox,
                clientTypeComboBox);

        //install listener for length limit
        addTextFieldCharacterLimit(50, clientNameTextField, clientLastNameTextField);
        addTextFieldCharacterLimit(20, clientDocumentIdTextField);
        addTextFieldCharacterLimit(256, clientDirectionTextField, clientEmailTextField);

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

        return gridPane;
    }
}
