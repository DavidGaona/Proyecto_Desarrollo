package view;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.AlertBox;
import model.Client;
import controller.DaoClient;
import utilities.ProjectUtilities;

public class EditMenu {

    public EditMenu(double percentage, double buttonFont) {
        client = new DaoClient();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private DaoClient client;
    private double percentage;
    private boolean currentClientMode = true;
    private double buttonFont;

    public HBox topBar(double width, double height) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 0, 0, 0));
        hbox.setPrefHeight(height * 0.05);
        hbox.getStyleClass().add("top-bar-color");

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.205);

        double optimalWidth = 0.15;
        double rect2Reduction = 0.05;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.195 - rect2Reduction)); //0.195

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Buscar cliente por documento");
        searchTextField.setPrefSize(width * 0.296, height * 0.03); // 0.296 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");
        searchTextField.setId("STF1");
        ProjectUtilities.onlyNumericTextField(searchTextField);

        searchTextField.setOnAction(e -> {
            Client searchedClient = client.loadClient(searchTextField.getText());
            if (!searchedClient.isBlank()) {
                resetNodeBorderColor(ClientPersonalInfo.clientNameTextField, ClientPersonalInfo.clientLastNameTextField,
                        ClientPersonalInfo.clientDocumentIdTextField, ClientPersonalInfo.clientEmailTextField,
                        ClientPersonalInfo.clientDirectionTextField, ClientPersonalInfo.clientDocumentTypeComboBox,
                        ClientPersonalInfo.clientTypeComboBox);

                ClientPersonalInfo.clientNameTextField.setText(searchedClient.getName());
                ClientPersonalInfo.clientLastNameTextField.setText(searchedClient.getLastName());
                ClientPersonalInfo.clientDocumentIdTextField.setText(searchedClient.getDocumentId());
                ClientPersonalInfo.clientEmailTextField.setText(searchedClient.getEmail());
                ClientPersonalInfo.clientDirectionTextField.setText(searchedClient.getDirection());
                ClientPersonalInfo.clientDocumentTypeComboBox.valueProperty().set(ProjectUtilities.convertDocumentTypeString(searchedClient.getDocumentType()));
                ClientPersonalInfo.clientTypeComboBox.valueProperty().set(ProjectUtilities.convertClientTypeString(searchedClient.getType()));

                saveChangesButton.setText("Modificar cliente");
                currentClientMode = false;
            }
        });

        Button newClientButton = new Button("Nuevo cliente");
        newClientButton.setPrefSize(width * optimalWidth, height * 0.03); //0.10 , 0.03
        newClientButton.setStyle("-fx-font-size: " + buttonFont);
        newClientButton.getStyleClass().add("client-buttons-template");
        newClientButton.setOnMouseClicked(e -> {
            ClientPersonalInfo.clearTextFields();
            resetNodeBorderColor(ClientPersonalInfo.clientNameTextField, ClientPersonalInfo.clientLastNameTextField,
                    ClientPersonalInfo.clientDocumentIdTextField, ClientPersonalInfo.clientEmailTextField,
                    ClientPersonalInfo.clientDirectionTextField, ClientPersonalInfo.clientDocumentTypeComboBox,
                    ClientPersonalInfo.clientTypeComboBox);
            saveChangesButton.setText("Agregar cliente");
            currentClientMode = true;
            searchTextField.setText("");
        });

        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(marginRect1, newClientButton, marginRect2, searchTextField);
        return hbox;
    }

    private void resetNodeBorderColor(Node... nodes) {
        for (Node node : nodes) {
            node.setStyle(node.getStyle() + "\n-fx-border-color: #3d3d3d;");
        }
    }

    private Button saveChangesButton;

    public HBox botBar(double width, double height) {
        HBox hbox = new HBox();
        hbox.setPrefHeight(height * 0.05);
        hbox.getStyleClass().add("bot-bar-color");

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.205);

        double optimalWidth = 0.15;
        double rect2Reduction = 0.05;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.391 - rect2Reduction * 2)); //0.391

        Button clearButton = new Button("Limpiar celdas");
        clearButton.setPrefSize(width * optimalWidth, height * 0.03); //0.10 , 0.03
        clearButton.setStyle("-fx-font-size: " + buttonFont);
        clearButton.getStyleClass().add("client-buttons-template");
        clearButton.setOnMouseClicked(e -> {
            ClientPersonalInfo.clearTextFields();
            resetNodeBorderColor(ClientPersonalInfo.clientNameTextField, ClientPersonalInfo.clientLastNameTextField,
                    ClientPersonalInfo.clientDocumentIdTextField, ClientPersonalInfo.clientEmailTextField,
                    ClientPersonalInfo.clientDirectionTextField, ClientPersonalInfo.clientDocumentTypeComboBox,
                    ClientPersonalInfo.clientTypeComboBox);
        });

        saveChangesButton = new Button("Agregar cliente");
        saveChangesButton.setPrefSize(width * optimalWidth, height * 0.03); // 0.10 , 0.03
        saveChangesButton.setStyle("-fx-font-size: " + buttonFont);
        saveChangesButton.getStyleClass().add("client-buttons-template");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentClientMode) {
                saveNewClient();
            } else {
                editClient();
            }
        });

        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(marginRect1, clearButton, marginRect2, saveChangesButton);
        return hbox;
    }

    private void saveNewClient() {
        boolean cbCorrect = isComboBoxCorrect(ClientPersonalInfo.clientTypeComboBox, ClientPersonalInfo.clientDocumentTypeComboBox);
        boolean tfCorrect = isTextFieldCorrect(ClientPersonalInfo.clientNameTextField, ClientPersonalInfo.clientLastNameTextField,
                ClientPersonalInfo.clientDocumentIdTextField, ClientPersonalInfo.clientEmailTextField, ClientPersonalInfo.clientDirectionTextField);
        if (cbCorrect && tfCorrect) {
            client.saveNewClient(
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientLastNameTextField.getText()),
                    ProjectUtilities.convertDocumentType(ClientPersonalInfo.clientDocumentTypeComboBox.getValue()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientDocumentIdTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientEmailTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientDirectionTextField.getText()),
                    ProjectUtilities.convertClientType(ClientPersonalInfo.clientTypeComboBox.getValue()));
            saveChangesButton.setText("Modificar cliente");
            currentClientMode = false;

        }
    }

    private void editClient() {
        boolean cbCorrect = isComboBoxCorrect(ClientPersonalInfo.clientTypeComboBox, ClientPersonalInfo.clientDocumentTypeComboBox);
        boolean tfCorrect = isTextFieldCorrect(ClientPersonalInfo.clientNameTextField, ClientPersonalInfo.clientLastNameTextField,
                ClientPersonalInfo.clientDocumentIdTextField, ClientPersonalInfo.clientEmailTextField, ClientPersonalInfo.clientDirectionTextField);
        if (cbCorrect && tfCorrect) {
            client.editClient(
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientNameTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientLastNameTextField.getText()),
                    ProjectUtilities.convertDocumentType(ClientPersonalInfo.clientDocumentTypeComboBox.getValue()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientDocumentIdTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientEmailTextField.getText()),
                    ProjectUtilities.clearWhiteSpaces(ClientPersonalInfo.clientDirectionTextField.getText()),
                    ProjectUtilities.convertClientType(ClientPersonalInfo.clientTypeComboBox.getValue()));
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

    public VBox addVBox(double width) {
        VBox vbox = new VBox();
        vbox.setPrefWidth(width * 0.2);
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699
        return vbox;
    }

    public VBox midPane(double width, double height) {
        VBox vbox = new VBox();
        vbox.setPrefSize(width * 0.6, height * 0.9);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setStyle("-fx-border-width: 4;\n-fx-border-color: #17161B");

        GridPane delete = new GridPane();

        ClientPersonalInfo.percentage = percentage;
        HBox infoHbox = centerHboxTemplate(width, height * 0.45, "Información Personal",
                ClientPersonalInfo.personalInfoPane(width, height * 0.45));

        HBox centerHbox = centerHboxTemplate(width, height * 0.6, "Información Del Plan", delete);

        HBox botHbox = centerHboxTemplate(width, height * 0.3, "Información Bancaria", delete);

        vbox.getChildren().addAll(infoHbox, centerHbox, botHbox);
        return vbox;
    }

    public HBox centerHboxTemplate(double width, double height, String message, GridPane gridPane) {
        //Vbox
        HBox hbox = new HBox();
        //hbox.setPrefSize(width * 0.6, height);
        hbox.setPrefWidth(width * 0.6);
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setStyle("-fx-border-width: 4;-fx-border-color: #17161B;-fx-background-color: #24222A;");

        //StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        //stackPane.setPrefSize(width * 0.2, height);

        //Rectangle bg
        Rectangle rect = new Rectangle();
        rect.setHeight(Math.max(height, 280.0));
        rect.setWidth(width * 0.2);
        rect.setFill(Color.web("#24222A"));

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setMaxWidth(width * 0.2);
        centerText.setAlignment(Pos.TOP_CENTER);

        //Text with message
        Text text = new Text(message);
        text.setFont(new Font("Consolas", 30 - (30 * percentage))); // 30
        text.setFill(Color.web("#FFFFFF"));

        //Margin for the text
        Rectangle marginRect = new Rectangle();
        marginRect.setHeight(30);
        marginRect.setWidth(0);
        marginRect.setFill(Color.web("#24222A"));

        centerText.getChildren().addAll(marginRect, text);
        stackPane.getChildren().addAll(rect, centerText);
        hbox.getChildren().addAll(stackPane, gridPane);

        //ReSize
        hbox.maxHeightProperty().bind(gridPane.heightProperty());
        //hbox.maxWidthProperty().bind(gridPane.widthProperty());

        return hbox;
    }

    public ScrollPane centerScrollPane(double width, double height) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #141318;\n-fx-border-color: #17161B;\n-fx-border-width: 0");

        BorderPane layout = new BorderPane();
        VBox vBoxLeft = addVBox(width);
        VBox vBoxRight = addVBox(width);
        VBox vBoxCenter = midPane(width, height);
        vBoxCenter.setId("a1");

        layout.setCenter(vBoxCenter);
        layout.setLeft(vBoxLeft);
        layout.setRight(vBoxRight);

        scrollPane.setContent(layout);
        layout.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 3; // *6 to make the scrolling a bit faster
            double widthSpeed = scrollPane.getContent().getBoundsInLocal().getWidth();
            double value = scrollPane.getVvalue();
            scrollPane.setVvalue(value + -deltaY / widthSpeed);
        });
        return scrollPane;
    }


}
