package view;

import controller.DaoBank;
import controller.DaoClient;
import controller.DaoPlan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Client;
import model.Plan;
import view.components.AlertBox;
import utilities.Icons;
import utilities.ProjectUtilities;
import view.components.SearchPane;
import view.components.SignOut;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientMenu {

    public ClientMenu(double percentage, double buttonFont) {
        client = new DaoClient();
        plan = new DaoPlan();
        bank = new DaoBank();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    EditingPanel personalInfo, newLine, changePlan, payPlan;


    private Button saveChangesButton;
    private ArrayList<EditingPanel> aligner = new ArrayList<>();
    private int currentClient = -1;
    private SearchPane searchPane;
    private DaoPlan plan;
    private DaoClient client;
    private DaoBank bank;
    private double percentage;
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

        double circleRadius = (height * 0.045) / 2;
        hBox.setPadding(new Insets(0, 0, 0, ((width * 0.10) - circleRadius)));

        Circle logOut = new Circle(circleRadius);
        logOut.setCenterX(circleRadius);
        logOut.setCenterY(circleRadius);
        logOut.setFill(Color.web("#FFFFFF"));
        logOut.setStroke(Color.web("#3D3D3E"));

        Icons icons = new Icons();
        icons.searchIcon(percentage);
        icons.getSearchIcon().setOnAction(e -> {
            searchPane.setVisible(true);
            searchPane.giveFocus();
        });

        searchPane.addElement("NIT");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        logOut.setEffect(shadow);

        saveChangesButton = clientButtonTemplate(width * 0.15, height * 0.03, "Agregar cliente");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentClientMode)
                saveNewClient();
            else
                editClient();
        });

        searchPane.getSearchField().setOnAction(e -> {
            Client searchedClient = client.loadClient(searchPane.getTextContent(), searchPane.getDocumentType());
            if (searchedClient.isNotBlank()) {
                personalInfo.clear();

                currentClient = searchedClient.getId();
                personalInfo.setTextField("clientName", searchedClient.getName());
                personalInfo.setTextField("clientLastName", searchedClient.getLastName());
                personalInfo.setTextField("clientDocumentNumber", searchedClient.getDocumentId());
                personalInfo.setTextField("clientEmail", searchedClient.getEmail());
                personalInfo.setTextField("clientAddress", searchedClient.getDirection());
                personalInfo.setComboBox("clientDocumentType", ProjectUtilities.convertDocumentTypeString(searchedClient.getDocumentType()));
                personalInfo.setComboBox("clientType", ProjectUtilities.convertClientTypeString(searchedClient.getType()));

                searchPane.getSearchField().setText("");
                searchPane.setVisible(false);
                saveChangesButton.setText("Modificar cliente");
                currentClientMode = false;
                ArrayList<Long> numbers = client.loadPhoneNumbers(currentClient);
                if (!numbers.isEmpty()) {
                    payPlan.setComboBox("phoneNumbers", numbers);
                    changePlan.setComboBox("phoneNumbers", numbers);
                }
            } else
                AlertBox.display("Error: ", "Cliente no encontrado");
        });

        Button newClientButton = clientButtonTemplate(width * 0.15, height * 0.03, "Nuevo cliente");
        newClientButton.setOnMouseClicked(e -> {
            personalInfo.clear();
            saveChangesButton.setText("Agregar cliente");
            currentClientMode = true;
            searchPane.getSearchField().setText("");
        });

        logOut.setOnMouseClicked(e -> signOut.display());

        hBox.getChildren().addAll(logOut, newClientButton, icons.getSearchIcon(), saveChangesButton);
        HBox.setMargin(logOut, new Insets(0, ((width * 0.10) - circleRadius), 0, 0));
        HBox.setMargin(icons.getSearchIcon(), new Insets(0, (width * 0.1355), 0, (width * 0.135)));
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {

        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll();
        return hBox;
    }

    private void saveNewClient() {
        String message = "No se pueden dejar campos vacios";
        if (!personalInfo.isEmpty()) {
            message = client.saveNewClient(
                    -1,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientLastName")),
                    (personalInfo.getContent("clientDocumentType").equals("NIT") ? (short) 4 :
                            ProjectUtilities.convertDocumentType(personalInfo.getContent("clientDocumentType"))),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientDocumentNumber")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientEmail")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientAddress")),
                    ProjectUtilities.convertClientType(personalInfo.getContent("clientType")),
                    Login.currentLoggedUser);
            personalInfo.clear();
        }
        AlertBox.display("Error", message);
    }

    private void editClient() {
        String message = "No se pueden dejar campos vacios";
        if (!personalInfo.isEmpty()) {
            message = client.editClient(
                    currentClient,
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientName")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientLastName")),
                    (personalInfo.getContent("clientDocumentType").equals("NIT") ? (short) 4 :
                            ProjectUtilities.convertDocumentType(personalInfo.getContent("clientDocumentType"))),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientDocumentNumber")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientEmail")),
                    ProjectUtilities.clearWhiteSpaces(personalInfo.getContent("clientAddress")),
                    ProjectUtilities.convertClientType(personalInfo.getContent("clientType")));
            personalInfo.clear();
        }
        AlertBox.display("Error", message);
    }

    private void getSelectedPlan(EditingPanel panel) {
        Plan selectedPlan = plan.loadPlan(panel.getContent("planName"));
        panel.setTextField("planCost", selectedPlan.getPlanCost() + "");
        panel.setTextField("planMinutes", selectedPlan.getPlanMinutes() + "");
        panel.setTextField("planDataCap", selectedPlan.getPlanData() + "");
        panel.setTextField("planTextMessage", selectedPlan.getPlanTextMsn() + "");
    }

    private void getSelectedPlan(EditingPanel panel, String planName) {
        Plan selectedPlan = plan.loadPlan(planName);
        panel.setComboBox("planName", planName);
        panel.setTextField("planCost", selectedPlan.getPlanCost() + "");
        panel.setTextField("planMinutes", selectedPlan.getPlanMinutes() + "");
        panel.setTextField("planDataCap", selectedPlan.getPlanData() + "");
        panel.setTextField("planTextMessage", selectedPlan.getPlanTextMsn() + "");
    }

    private void saveNewLine() {
        String response = client.addNewClientLine(currentClient, newLine.getContent("planName"));
        if (response.equals("Plan y número agredado con éxito")) {
            AlertBox.display("Éxito: ", response);
            ArrayList<Long> numbers = client.loadPhoneNumbers(currentClient);
            if (!numbers.isEmpty()) {
                payPlan.setComboBox("phoneNumbers", numbers);
                changePlan.setComboBox("phoneNumbers", numbers);
            }
        } else
            AlertBox.display("Error: ", response);
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
        personalInfo.addElements("clientDocumentType", "NIT");

        personalInfo.addComboBox("clientType", "Tipo de cliente:", ProjectUtilities.clientTypes);

        aligner.add(personalInfo);
    }

    private void changePlan(double width) {
        changePlan = new EditingPanel("Cambiar plan", percentage, width);

        String[] placeHolder = {""};
        changePlan.addComboBox("phoneNumbers", "Numero de Celular:", placeHolder);
        changePlan.limitVisibleRows("phoneNumbers", 6);
        changePlan.getComboBox("phoneNumbers").setOnAction(e -> {
            String planName = client.getPhonePlan(Long.parseLong(changePlan.getContent("phoneNumbers")));
            getSelectedPlan(changePlan, planName);
        });

        changePlan.addComboBox("planName", "Plan:", placeHolder);
        changePlan.limitVisibleRows("planName", 6);

        changePlan.addTextField("planCost", "Costo:");
        changePlan.disableTextField("planCost");

        changePlan.addTextField("planMinutes", "Minutos:");
        changePlan.disableTextField("planMinutes");

        changePlan.addTextField("planDataCap", "Gigas del plan:");
        changePlan.disableTextField("planDataCap");

        changePlan.addTextField("planTextMessage", "Número de mensajes:");
        changePlan.disableTextField("planTextMessage");

        changePlan.getComboBox("planName").setOnAction(e -> getSelectedPlan(changePlan));

        changePlan.addButton("Cambiar plan");
        changePlan.getAddButton().setOnAction(e -> {
            if (currentClientMode)
                AlertBox.display("Error: ", "Seleccione un cliente primero");
            else {
                String response = client.queueNewPlan(
                        currentClient,
                        Long.parseLong(changePlan.getContent("phoneNumbers")),
                        changePlan.getContent("planName")
                );
                if (response.equals("Plan pendiente por cambio, sera cambiado en su proximo pago"))
                    AlertBox.display("Éxito: ", response);
                else
                    AlertBox.display("Error: ", "Seleccione un cliente primero");
            }
        });

        aligner.add(changePlan);
    }

    private void newLine(double width) {
        newLine = new EditingPanel("Añadir linea", percentage, width);

        String[] placeHolder = {""};
        newLine.addComboBox("planName", "Plan:", placeHolder);
        newLine.limitVisibleRows("planName", 6);

        newLine.addTextField("planCost", "Costo:");
        newLine.disableTextField("planCost");

        newLine.addTextField("planMinutes", "Minutos:");
        newLine.disableTextField("planMinutes");

        newLine.addTextField("planDataCap", "Gigas del plan:");
        newLine.disableTextField("planDataCap");

        newLine.addTextField("planTextMessage", "Número de mensajes:");
        newLine.disableTextField("planTextMessage");

        newLine.getComboBox("planName").setOnAction(e -> getSelectedPlan(newLine));

        newLine.addButton("Añadir linea");
        newLine.getAddButton().setOnAction(e -> {
            if (currentClientMode)
                AlertBox.display("Error: ", "Seleccione un cliente primero");
            else
                saveNewLine();
        });

        aligner.add(newLine);
    }

    private void payPlan(double width) {
        payPlan = new EditingPanel("Pago de plan", percentage, width);

        String[] placeHolder = {""};
        payPlan.addComboBox("phoneNumbers", "Numero de Celular:", placeHolder);
        payPlan.limitVisibleRows("phoneNumbers", 6);

        payPlan.addComboBox("banks", "Banco:", placeHolder);
        payPlan.limitVisibleRows("banks", 3);

        payPlan.addTextField("cost", "Valor a pagar:");
        payPlan.disableTextField("cost");
        payPlan.getComboBox("phoneNumbers").setOnAction(e -> payPlan.setTextField("cost", client.getValueToPay(Long.parseLong(payPlan.getContent("phoneNumbers"))) + ""));

        payPlan.addButton("Pagar plan");
        payPlan.getAddButton().setOnAction(e -> {
            String response = "No tiene facturas por pagar";
            if (!payPlan.getContent("cost").equals("0.0") && !payPlan.getContent("cost").isBlank()){
                response = client.payPlan(
                        Long.parseLong(payPlan.getContent("phoneNumbers")),
                        currentClient,
                        payPlan.getContent("banks")
                );
            }
            if (currentClient == -1)
                response = "Seleccione un cliente primero";
            if (response.equals("Factura pagada con éxito")) {
                AlertBox.display("Éxito: ", response);
            } else
                AlertBox.display("Error: ", response);
        });

        aligner.add(payPlan);
    }

    public void align() {
        double longestTextSize = 0.0;
        for (EditingPanel editingPanel : aligner) {
            if (editingPanel.getLongestText() > longestTextSize) {
                longestTextSize = editingPanel.getLongestText();
            }
        }

        for (EditingPanel editingPanel : aligner) {
            editingPanel.align(longestTextSize);
        }
    }

    public StackPane renderClientEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();
        personalInfo(width);
        changePlan(width);
        newLine(width);
        payPlan(width);
        changePlan.setComboBoxString("planName", plan.loadPlans());
        newLine.setComboBoxString("planName", plan.loadPlans());
        payPlan.setComboBoxString("banks", new ArrayList<>(Arrays.asList(bank.loadAllBanks())));

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(
                personalInfo.sendPane(width, height * 0.1),
                changePlan.sendPane(width, 0),
                newLine.sendPane(width, 0),
                payPlan.sendPane(width, 0)
        );

        searchPane = new SearchPane(width, height, percentage);
        HBox sp = searchPane.showFrame();

        BorderPane clientMenu;
        clientMenu = menu.renderMenuTemplate();
        clientMenu.setTop(topBar((HBox) clientMenu.getTop(), width, height));
        clientMenu.setBottom(botBar((HBox) clientMenu.getBottom(), width, height));
        clientMenu.setCenter(clientMenu.getCenter());

        Rectangle screenFilter = new Rectangle(0, 0, width, height * 0.9);
        screenFilter.setOnMouseClicked(e -> searchPane.setVisible(false));
        screenFilter.setOnTouchPressed(e -> searchPane.setVisible(false));
        screenFilter.setOnScroll(e -> {
            double deltaY = e.getDeltaY() * 3;
            double widthSpeed = menu.getScrollPane().getContent().getBoundsInLocal().getWidth();
            double value = menu.getScrollPane().getVvalue();
            menu.getScrollPane().setVvalue(value + -deltaY / widthSpeed);
        });
        screenFilter.setFill(Color.rgb(0, 0, 0, 0.25));
        screenFilter.visibleProperty().bind(searchPane.getIsVisible());


        stackPane.getChildren().addAll(clientMenu, screenFilter, sp);
        stackPane.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(screenFilter, Pos.CENTER_LEFT);
        StackPane.setAlignment(sp, Pos.CENTER);
        return stackPane;
    }
}
