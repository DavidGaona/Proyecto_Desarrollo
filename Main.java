import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import messages.AlertBox;
import messages.ConfirmBox;
import model.Client;
import connection.DbManager;
import view.DaoClient;

import javax.swing.text.Utilities;
import java.awt.*;


public class Main extends Application {

    private DaoClient client = new DaoClient();
    private double percentage;
    private boolean currentClientMode = true;

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
        if (width > 1920) {
            optimalWidth = 0.1;
            rect2Reduction = 0.0;
        }

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.195 - rect2Reduction)); //0.195

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Buscar cliente por documento");
        searchTextField.setPrefSize(width * 0.296, height * 0.03); // 0.296 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");
        searchTextField.setId("STF1");
        onlyNumericTextField(searchTextField);

        searchTextField.setOnAction(e -> {
            Client searchedClient = client.loadClient(searchTextField.getText());
            if (!searchedClient.isBlank()) {
                clientNameTextField.setText(searchedClient.getName());
                clientLastNameTextField.setText(searchedClient.getLastName());
                clientDocumentIdTextField.setText(searchedClient.getDocumentId());
                clientEmailTextField.setText(searchedClient.getEmail());
                clientDirectionTextField.setText(searchedClient.getDirection());
                clientDocumentTypeComboBox.valueProperty().set(ProjectUtilities.convertDocumentTypeString(searchedClient.getDocumentType()));
                clientTypeComboBox.valueProperty().set(ProjectUtilities.convertClientTypeString(searchedClient.getType()));
                ;
                saveChangesButton.setText("Modificar cliente");
                currentClientMode = false;
            }
        });

        Button newClientButton = new Button("Nuevo cliente");
        newClientButton.setPrefSize(width * optimalWidth, height * 0.03); //0.10 , 0.03
        newClientButton.getStyleClass().add("client-buttons-template");
        newClientButton.setOnMouseClicked(e -> {
            clearTextFields();
            saveChangesButton.setText("Agregar cliente");
            currentClientMode = true;
            searchTextField.setText("");
        });

        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(marginRect1, newClientButton, marginRect2, searchTextField);
        return hbox;
    }

    private void onlyNumericTextField(TextField searchTextField) {
        searchTextField.setOnKeyTyped(e -> {
            if (!(ProjectUtilities.isNumeric(searchTextField.getText()))) {
                String correctText = searchTextField.getText().replaceAll("[^\\d]", "");
                int prevPos = searchTextField.getCaretPosition();
                searchTextField.setText(correctText);
                searchTextField.positionCaret(prevPos - 1);
            }
        });
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
        if (width > 1920) {
            optimalWidth = 0.1;
            rect2Reduction = 0.0;
        }

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.391 - rect2Reduction * 2)); //0.391

        Button clearButton = new Button("Limpiar celdas");
        clearButton.setPrefSize(width * optimalWidth, height * 0.03); //0.10 , 0.03
        clearButton.getStyleClass().add("client-buttons-template");
        clearButton.setOnMouseClicked(e -> clearTextFields());

        saveChangesButton = new Button("Agregar cliente");
        saveChangesButton.setPrefSize(width * optimalWidth, height * 0.03); // 0.10 , 0.03
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
        if (isTextFieldCorrect(clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField, clientDirectionTextField)) {
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
        if (isTextFieldCorrect(clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField, clientDirectionTextField)) {
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

        HBox infoHbox = centerHboxTemplate(width, height * 0.45, "Información Personal", personalInfoPane(width, height * 0.45));
        HBox centerHbox = centerHboxTemplate(width, height * 0.6, "Información Del Plan", delete);
        HBox botHbox = centerHboxTemplate(width, height * 0.3, "Información Bancaria", delete);

        vbox.getChildren().addAll(infoHbox, centerHbox, botHbox);
        return vbox;
    }

    public HBox centerHboxTemplate(double width, double height, String message, GridPane gridPane) {
        //Vbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width * 0.6, height);
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setStyle("-fx-border-width: 4;-fx-border-color: #17161B;-fx-background-color: #24222A;");

        //StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.setPrefSize(width * 0.2, height);

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

    public TextField clientTextFieldTemplate(String tittle, String textFieldStyle) {
        TextField clientTextField = new TextField(tittle);
        clientTextField.setStyle(textFieldStyle);
        clientTextField.setFont(new Font("Consolas", 20 - (20 * percentage))); //20
        clientTextField.setPrefSize(350 - (350 * percentage), 30 - (30 * percentage)); //350 , 30
        return clientTextField;
    }

    public Text clientTextTemplate(String tittle, String color) {
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 20 - (20 * percentage))); //20
        clientText.setFill(Color.web(color));
        return clientText;
    }

    private Node selectedNode, lastSelectedNode;

    private void focusListener(GridPane layout, String nodeStyle, String nodeColor, Node... nodes) {
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
                    selectedNode.setStyle(nodeStyle + "\n-fx-border-color: #C2B8E0;");
                    for (Node node : layout.getChildren()) {
                        if (textFieldId.substring(2).equals(node.getId().substring(1))) {
                            ((Text) node).setFill(Color.web(nodeColor));
                            break;
                        }
                    }
                } else {
                    String textFieldId = lastSelectedNode.getId();
                    if (lastSelectedNode != null) {
                        lastSelectedNode.setStyle(nodeStyle);
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

    private void addTextFieldCharacterLimit(int limit, TextField... textFields) {
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

    private void clearTextFields() {
        clientNameTextField.setText("");
        clientNameTextField.setText("");
        clientLastNameTextField.setText("");
        clientDocumentIdTextField.setText("");
        clientEmailTextField.setText("");
        clientDirectionTextField.setText("");
        clientDocumentTypeComboBox.valueProperty().set(null);
        clientTypeComboBox.valueProperty().set(null);
        AlertBox.display("","Datos del Cliente Limpiados");
    }

    private TextField clientNameTextField;
    private TextField clientLastNameTextField;
    private TextField clientDocumentIdTextField;
    private TextField clientEmailTextField;
    private TextField clientDirectionTextField;
    private ComboBox<String> clientDocumentTypeComboBox;
    private ComboBox<String> clientTypeComboBox;

    public GridPane personalInfoPane(double width, double height) {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(width * 0.4, height); // 0.4 ,,
        //gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25);
        gridPane.setHgap(10); // 10
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 0;");


        String textFieldStyle = "-fx-background-color: #3D3946;\n-fx-text-fill: #FFFFFF;\n-fx-border-radius: 2;\n" +
                "-fx-border-width: 2;\n-fx-border-color: #3d3d3d;";
        String textColor = "#948FA3";

        //Image checkImage = new Image(new FileInputStream("C:\\Users\\david\\IdeaProjects\\panes\\src\\Check.png"));
        //final ImageView currentImage = new ImageView();
        //currentImage.setImage(checkImage);

        //name text
        Text clientNameText = clientTextTemplate("Nombres:", textColor);
        clientNameText.setId("T1");

        //name text field actions
        clientNameTextField = clientTextFieldTemplate("", textFieldStyle);
        clientNameTextField.setId("TF1");

        //last name text
        Text clientLastNameText = clientTextTemplate("Apellidos:", textColor);
        clientLastNameText.setId("T2");

        //name text field actions
        clientLastNameTextField = clientTextFieldTemplate("", textFieldStyle);
        clientLastNameTextField.setId("TF2");

        //document id text
        Text clientDocumentIdText = clientTextTemplate("Número de documento:", textColor);
        clientDocumentIdText.setId("T3");

        //Document id text field actions
        clientDocumentIdTextField = clientTextFieldTemplate("", textFieldStyle);
        clientDocumentIdTextField.setId("TF3");
        onlyNumericTextField(clientDocumentIdTextField);

        //Email Text
        Text clientEmailText = clientTextTemplate("Email:", textColor);
        clientEmailText.setId("T4");

        //Email TextField
        clientEmailTextField = clientTextFieldTemplate("", textFieldStyle);
        clientEmailTextField.setId("TF4");

        //Direction Text
        Text clientDirectionText = clientTextTemplate("Dirección:", textColor);
        clientDirectionText.setId("T5");

        //Direction TextField
        clientDirectionTextField = clientTextFieldTemplate("", textFieldStyle);
        clientDirectionTextField.setId("TF5");

        //document type text
        Text clientDocumentTypeText = clientTextTemplate("Tipo de documento:", textColor);
        clientDocumentTypeText.setId("T6");

        //document type combobox
        clientDocumentTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypes));
        clientDocumentTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientDocumentTypeComboBox.setId("CB6");
        //clientDocumentTypeComboBox.setOnAction(e -> client.setType(ProjectUtilities.convertDocumentType(clientDocumentTypeComboBox.getValue())));

        //document type text
        Text clientTypeText = clientTextTemplate("Tipo de cliente:", textColor);
        clientTypeText.setId("T7");

        //document type combobox
        clientTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.clientTypes));
        clientTypeComboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        clientTypeComboBox.setId("CB7");
        //clientTypeComboBox.setOnAction(e -> client.setType(ProjectUtilities.convertClientType(clientTypeComboBox.getValue())));

        //Install listener for color highlight
        focusListener(gridPane, textFieldStyle, "#C2B8E0",
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
        int rowStart = 1;
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

        Rectangle emptyRect = new Rectangle();
        emptyRect.setWidth(0.0);
        emptyRect.setHeight(0.0);
        GridPane.setConstraints(emptyRect, colText, rowStart + 7);
        GridPane.setHalignment(emptyRect, HPos.RIGHT);
        GridPane.setConstraints(emptyRect, colTextField, rowStart + 7);

        //Adding all nodes
        gridPane.getChildren().addAll(
                //currentImage,
                clientNameText, clientNameTextField,
                clientLastNameText, clientLastNameTextField,
                clientDocumentTypeText, clientDocumentTypeComboBox,
                clientDocumentIdText, clientDocumentIdTextField,
                clientEmailText, clientEmailTextField,
                clientDirectionText, clientDirectionTextField,
                clientTypeText, clientTypeComboBox,
                emptyRect);

        return gridPane;
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

    @Override
    public void start(Stage window) {
        //resolutions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double percentageWidth = (1920 - screenSize.getWidth()) / 1920;
        double percentageHeight = (1080 - screenSize.getHeight()) / 1080;
        percentage = Math.max(percentageWidth, percentageHeight);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = gd.getDefaultConfiguration();
        java.awt.Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        int taskBarSize = scnMax.bottom;
        int width = (int) screenSize.getWidth(); //2560 1920 1280 1152 1024; 768 40
        int height = (int) screenSize.getHeight();//1440 1080 720 648 576; 432 40

        Scene mainMenu;
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(0, 0, 0, 0));
        HBox hBoxTop = topBar(width, height);
        HBox hBoxBot = botBar(width, height);
        ScrollPane spCenter = centerScrollPane(width, height);

        mainLayout.setBottom(hBoxBot);
        mainLayout.setTop(hBoxTop);
        mainLayout.setCenter(spCenter);

        mainMenu = new Scene(mainLayout, width, height - taskBarSize * 1.8);
        mainMenu.getStylesheets().add("styles.css");

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(window);
        });

        window.setScene(mainMenu);
        window.setTitle("UwU");
        window.setResizable(false);
        window.show();

    }

    private void closeProgram(Stage window) {
        Boolean answer = ConfirmBox.display("Cerrar Programa", "¿ Seguro que quieres cerrar el programa ?");
        if (answer) {
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
