import javafx.application.Application;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Main extends Application {

    public boolean isNumeric(String inputData) {
        return inputData.matches("\\d+(\\d+)?");
    }


    private Client client;

    public HBox topBar(double width, double height, double porwid, double porhei) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(0, 0, 0, 0));
        hbox.setPrefHeight(height * 0.05 ); // 0.05
        hbox.getStyleClass().add("top-bar-color");

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.205 ); //0.205

        double optimalWidth = 0.15;
        double rect2Reduction = 0.05;
        if ( width > 1920){
            optimalWidth = 0.1;
            rect2Reduction = 0.0;
        }

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.195 - rect2Reduction)); //0.195

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Buscar cliente por documento");
        searchTextField.setPrefSize(width*0.296 , height*0.03 ); // 0.296 , 0.03
        searchTextField.getStyleClass().add("client-search-bar");

        Button newClientButton = new Button("Nuevo cliente");
        newClientButton.setPrefSize(width * optimalWidth , height*0.03); //0.10 , 0.03
        newClientButton.getStyleClass().add("client-buttons-template");

        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(marginRect1, newClientButton, marginRect2, searchTextField);
        return hbox;
    }

    public HBox botBar(double width, double height, String buttonText, double porwid, double porhei) {
        HBox hbox = new HBox();
        hbox.setPrefHeight(height*0.05 ); //0.05
        hbox.getStyleClass().add("bot-bar-color");

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.205 ); // 0.205

        double optimalWidth = 0.15;
        double rect2Reduction = 0.05;
        if ( width > 1920){
            optimalWidth = 0.1;
            rect2Reduction = 0.0;
        }

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth(width * (0.391 - rect2Reduction * 2) ); //0.391

        Button clearButton = new Button("Limpiar celdas");
        clearButton.setPrefSize(width*optimalWidth , height*0.03 ); //0.10 , 0.03
        clearButton.getStyleClass().add("client-buttons-template");

        Button saveChangesButton = new Button(buttonText);
        saveChangesButton.setPrefSize(width*optimalWidth , height*0.03 ); // 0.10 , 0.03
        saveChangesButton.getStyleClass().add("client-buttons-template");
        saveChangesButton.setOnMouseClicked(e -> {
            Node uwu = hbox.getParent().getChildrenUnmodifiable().get(1);
        });

        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(marginRect1, clearButton, marginRect2, saveChangesButton);
        return hbox;
    }

    public VBox addVBox(double width, double height, double porwid, double porhei) {
        VBox vbox = new VBox();
        vbox.setPrefWidth(width*0.2); // 0.2
        vbox.setSpacing(10); // 10
        vbox.setStyle("-fx-background-color: #18171C;"); // #336699
        return vbox;
    }

    public VBox midPane(double width, double height, double porwid, double porhei){
        VBox vbox = new VBox();
        vbox.setPrefSize(width*0.6, height*0.9 ); // 0.6 , 0.9
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setStyle("-fx-border-width: 4;\n-fx-border-color: #17161B"); //4

        GridPane delete = new GridPane();

        HBox infoHbox = centerHboxTemplate(width ,
                height*0.45 ,
                "Información Personal",
                personalInfoPane(width , height*0.45, porwid, porhei ), porwid, porhei); // , 0.45 , , 0.45
        HBox centerHbox = centerHboxTemplate(width , height*0.6 , "Información Del Plan", delete, porwid, porhei); // , 0.6
        HBox botHbox = centerHboxTemplate(width , height*0.3 , "Información Bancaria", delete, porwid, porhei); // , 0.3

        vbox.getChildren().addAll(infoHbox, centerHbox, botHbox);
        return vbox;
    }

    public HBox centerHboxTemplate(double width, double height, String message, GridPane gridPane, double porwid, double porhei){
        //Vbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width*0.6 , height ); // 0.6 , ,
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setStyle("-fx-border-width: 4;-fx-border-color: #17161B;-fx-background-color: #24222A;"); //4

        //StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.setPrefSize(width*0.2 ,height ); // 0.2 ,,

        //Rectangle bg
        Rectangle rect = new Rectangle();
        rect.setHeight(Math.max(height , 280.0)); // ,280.0
        rect.setWidth(width*0.2 ); //0.2
        rect.setFill(Color.web("#24222A"));

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setMaxWidth(width*0.2 ); // 0.2
        centerText.setAlignment(Pos.TOP_CENTER);

        //Text with message
        double percent = porhei;
        if(porwid > porhei)
        {
            percent = porwid;
        }
        Text text = new Text(message);
        text.setFont(new Font("Consolas", 30 - (30 * percent))); // 30
        text.setFill(Color.web("#FFFFFF"));

        //Margin for the text
        Rectangle marginRect = new Rectangle();
        marginRect.setHeight(30 - (30 * percent)); //30
        marginRect.setWidth(0);
        marginRect.setFill(Color.web("#24222A"));

        centerText.getChildren().addAll(marginRect, text);
        stackPane.getChildren().addAll(rect, centerText);
        hbox.getChildren().addAll(stackPane, gridPane);

        return hbox;
    }

    public TextField clientTextFieldTemplate(String tittle, String textFieldStyle, double porwid, double porhei){
        double percent = porhei;
        if(porwid > porhei)
        {
            percent = porwid;
        }
        TextField clientTextField = new TextField(tittle);
        clientTextField.setStyle(textFieldStyle);
        clientTextField.setFont(new Font("Consolas", 20 - (20 * percent))); //20
        clientTextField.setPrefSize(350 - (350 * percent), 30 - (30 * percent)); //350 , 30
        return clientTextField;
    }

    public Text clientTextTemplate(String tittle, String color, double porwid, double porhei){
        double percent = porhei;
        if(porwid > porhei)
        {
            percent = porwid;
        }
        Text clientText = new Text(tittle);
        clientText.setFont(new Font("Consolas", 20 - (20 * percent))); //20
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

    private void addTextFieldCharacterLimit(int limit, TextField... textFields){
        for (TextField textField : textFields) {
            textField.textProperty().addListener(e -> {
                if (textField.getText().length() > limit){
                    int prevPos = textField.getCaretPosition();
                    String limitedText = textField.getText().substring(0, prevPos) + textField.getText().substring(prevPos + 1);
                    textField.setText(limitedText);
                    textField.positionCaret(prevPos);
                }
            });
        }
    }

    private void addTextFieldTypeListener(TextField... textFields){
        for (TextField textField : textFields) {
            textField.onKeyTypedProperty().addListener((observableValue, eventHandler, t1) -> {

                System.out.println(textField.getId());
                switch (textField.getId()){
                    case "TF1":
                        client.setName(textField.getText());
                        System.out.println("Sirvo uwu");
                        break;
                    case "TF2":
                        client.setLastName(textField.getText());
                        break;
                    case "TF3":
                        client.setDocumentId(textField.getText());
                        break;
                    case "TF4":
                        client.setEmail(textField.getText());
                        break;
                    case "TF5":
                        client.setDirection(textField.getText());
                        break;
                }
            });
        }
    }

    public GridPane personalInfoPane(double width, double height, double porwid, double porhei)  {
        double percent = porhei;
        if(porwid > porhei)
        {
            percent = porwid;
        }
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(width*0.4 , height ); // 0.4 ,,
        //gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(25 - (25 * percent)); // 25
        gridPane.setHgap(10 - (10 * percent)); // 10
        gridPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 0;");


        String textFieldStyle = "-fx-background-color: #3D3946;\n-fx-text-fill: #FFFFFF;\n-fx-border-radius: 2;\n" +
                "-fx-border-width: 2;\n-fx-border-color: #3d3d3d;";
        String textColor = "#948FA3";

        //Image checkImage = new Image(new FileInputStream("C:\\Users\\david\\IdeaProjects\\panes\\src\\Check.png"));
        //final ImageView currentImage = new ImageView();
        //currentImage.setImage(checkImage);

        //name text
        Text clientNameText = clientTextTemplate("Nombres:", textColor , porwid, porhei);
        clientNameText.setId("T1");

        //name text field actions
        TextField clientNameTextField = clientTextFieldTemplate("", textFieldStyle , porwid, porhei);
        clientNameTextField.setId("TF1");

        //last name text
        Text clientLastNameText = clientTextTemplate("Apellidos:", textColor , porwid, porhei);
        clientLastNameText.setId("T2");

        //name text field actions
        TextField clientLastNameTextField = clientTextFieldTemplate("", textFieldStyle , porwid, porhei);
        clientLastNameTextField.setId("TF2");

        //document type text
        Text clientDocumentTypeText = clientTextTemplate("Tipo de documento:", textColor , porwid, porhei);
        clientDocumentTypeText.setId("T6");

        //document type combobox
        String documentTypes[] = { "Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería", "Pasaporte"};
        ComboBox clientDocumentTypeComboBox = new ComboBox(FXCollections.observableArrayList(documentTypes));
        clientDocumentTypeComboBox.setPrefSize(350 - (350 * percent), 40 - (40 * percent)); // 350 , 40
        clientDocumentTypeComboBox.setId("CB6");

        //document type text
        Text clientTypeText = clientTextTemplate("Tipo de cliente:", textColor , porwid, porhei);
        clientTypeText.setId("T7");

        //document type combobox
        String clientTypes[] = { "Natural", "Corporativo"};
        ComboBox clientTypeComboBox = new ComboBox(FXCollections.observableArrayList(clientTypes));
        clientTypeComboBox.setPrefSize(350 - (350 * percent), 40 - (40 * percent)); // 350 , 40
        clientTypeComboBox.setId("CB7");

        //document id text
        Text clientDocumentIdText = clientTextTemplate("Número de documento:", textColor , porwid, porhei);
        clientDocumentIdText.setId("T3");

        //Document id text field actions
        TextField clientDocumentIdTextField = clientTextFieldTemplate("", textFieldStyle , porwid, porhei);
        clientDocumentIdTextField.setId("TF3");

        clientDocumentIdTextField.setOnKeyTyped(e -> {
            if (!(isNumeric(clientDocumentIdTextField.getText()))) {
                String correctText = clientDocumentIdTextField.getText().replaceAll("[^\\d]", "");
                int prevPos = clientDocumentIdTextField.getCaretPosition();
                clientDocumentIdTextField.setText(correctText);
                clientDocumentIdTextField.positionCaret(prevPos - 1);
            }
        });

        //Email Text
        Text clientEmailText = clientTextTemplate("Email:", textColor , porwid, porhei);
        clientEmailText.setId("T4");

        //Email TextField
        TextField clientEmailTextField = clientTextFieldTemplate("", textFieldStyle , porwid, porhei);
        clientEmailTextField.setId("TF4");

        //Direction Text
        Text clientDirectionText = clientTextTemplate("Dirección:", textColor , porwid, porhei);
        clientDirectionText.setId("T5");

        //Direction TextField
        TextField clientDirectionTextField = clientTextFieldTemplate("", textFieldStyle , porwid, porhei);
        clientDirectionTextField.setId("TF5");

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

        addTextFieldTypeListener(clientNameTextField, clientLastNameTextField,
                clientDocumentIdTextField, clientEmailTextField,
                clientDirectionTextField);

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
        //gridPane.setAlignment(Pos.TOP_CENTER);
        return gridPane;
    }

    public ScrollPane centerScrollPane(double width, double height, double porwid, double porhei){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #141318;\n-fx-border-color: #17161B;\n-fx-border-width: 0");

        BorderPane layout = new BorderPane();
        VBox vBoxLeft = addVBox(width , height, porwid, porhei ); // ,,
        VBox vBoxRight = addVBox(width , height, porwid, porhei ); // ,,
        VBox vBoxCenter = midPane(width , height, porwid, porhei); // ,,
        vBoxCenter.setId("a1");

        layout.setCenter(vBoxCenter);
        layout.setLeft(vBoxLeft);
        layout.setRight(vBoxRight);

        scrollPane.setContent(layout);
        return scrollPane;
    }

    @Override
    public void start(Stage window) throws Exception {
        //resolutions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double porwid = (1920 - screenSize.getWidth())/1920;
        double porhei = (1080 - screenSize.getHeight())/1080;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = gd.getDefaultConfiguration();
        java.awt.Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        int taskBarSize = scnMax.bottom;
        int width = (int) screenSize.getWidth() ; //2560 1920 1280 1152 1024; 768 40
        int height = (int) screenSize.getHeight();//1440 1080 720 648 576; 432 40
        Scene mainMenu;
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(0, 0, 0, 0));
        HBox hBoxTop = topBar(width , height , porwid, porhei);
        HBox hBoxBot = botBar(width , height , "Agregar cliente", porwid, porhei);
        ScrollPane spCenter = centerScrollPane(width , height , porwid, porhei);

        mainLayout.setBottom(hBoxBot);
        mainLayout.setTop(hBoxTop);
        mainLayout.setCenter(spCenter);

        mainMenu = new Scene(mainLayout, width , height - taskBarSize*1.8);
        mainMenu.getStylesheets().add("styles.css");

        //mainMenu.setOn(e -> System.out.println(mainMenu.getWidth() + " x " + mainMenu.getHeight()));
        System.out.println(mainMenu.getWidth() + " x " + mainMenu.getHeight());
        System.out.println(width + " x " + height);
        System.out.println(porwid + "x" + porhei);
        window.setScene(mainMenu);
        window.setTitle("UwU");
        window.setResizable(false);
        window.show();
        Node tst = mainLayout.getChildren().get(2).lookup("a1");
        System.out.println(spCenter.getHeight());

    }

    public static void main(String[] args) {
        DbManager test = new DbManager("postgres", "postgres", "MobilePlan", "localhost");
        test.abrirConexionBD();
        Client client = new Client();
        client.setName("David");
        client.setLastName("Gaona");
        //client.setDocumentType();
        client.setDocumentId("1234");
        client.setDirection("Cra 26 #16-64");
        client.setEmail("dvg@gmail.com");
        client.setType(1);
        //test.saveClient(client);


        launch(args);
    }

}
