package view;

import controller.DaoPlan;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Extras;
import model.Plan;
import utilities.AlertBox;
import utilities.Icons;
import utilities.ProjectEffects;
import utilities.ProjectUtilities;
import view.components.SearchPane;

import java.util.ArrayList;

public class ManagerMenu {

    private EditingPanel basicPlanInfo, planExtras, createExtra;
    private SearchPane searchPane;
    private ArrayList<EditingPanel> aligner = new ArrayList<>();
    private double percentage;
    private DaoPlan plan;
    private double buttonFont;
    private Button saveChangesButton;
    private MenuListManager menuListManager = new MenuListManager();
    private VBox menuList;
    private boolean currentUserMode = true;

    public ManagerMenu(double percentage, double buttonFont) {
        plan = new DaoPlan();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private Button ManagerMenuButtonTemplate(double width, double height, String message) {
        Button button = new Button(message);
        button.setPrefSize(width * 0.15, height * 0.03);
        button.setStyle("-fx-font-size: " + buttonFont);
        button.getStyleClass().add("client-buttons-template");
        return button;
    }

    private HBox topBar(HBox hBox, double width, double height) {
        double circleRadius = (height * 0.045) / 2;
        hBox.setPadding(new Insets(0, 0, 0, ((width * 0.10) - circleRadius)));

        Circle menuCircle = new Circle(circleRadius);
        menuCircle.setCenterX(circleRadius);
        menuCircle.setCenterY(circleRadius);
        menuCircle.setFill(Color.web("#FFFFFF"));
        menuCircle.setStroke(Color.web("#3D3D3E"));

        Button newBankButton = ManagerMenuButtonTemplate(width, height, "Nuevo Plan");

        Icons icons = new Icons();
        icons.searchIcon(percentage);
        icons.getSearchIcon().setOnAction(e -> searchPane.setVisible(true));

        searchPane.getSearchField().setOnAction(e -> {
            Plan searchedPlan = plan.loadPlan(searchPane.getTextContent());
            if (searchedPlan.isNotBlank()) {
                planExtras.clearTables();
                basicPlanInfo.setTextField("planName", searchedPlan.getPlanName());
                basicPlanInfo.setTextField("planCost", searchedPlan.getPlanCost() + "");
                basicPlanInfo.setTextField("planMinutes", searchedPlan.getPlanMinutes() + "");
                basicPlanInfo.setTextField("planDataCap", searchedPlan.getPlanData() + "");
                basicPlanInfo.setTextField("planTextMessage", searchedPlan.getPlanTextMsn() + "");
                createExistingExtra(width, height, plan.loadPlanExtras(searchedPlan.getId()));
                saveChangesButton.setText("Modificar Plan");
                currentUserMode = false;
                searchPane.getSearchField().setText("");
                searchPane.setVisible(false);
            } else
                AlertBox.display("Error: ", "Usuario no encontrado", "");
        });

        saveChangesButton = ManagerMenuButtonTemplate(width, height, "Agregar Plan");
        saveChangesButton.setOnAction(e -> {
            String message = "No se pueden dejar campos vacios";
            if (!basicPlanInfo.isEmpty()) {
                message = plan.saveNewPlan(
                        ProjectUtilities.clearWhiteSpaces(basicPlanInfo.getContent("planName")),
                        ProjectUtilities.clearWhiteSpaces(basicPlanInfo.getContent("planCost")),
                        ProjectUtilities.clearWhiteSpaces(basicPlanInfo.getContent("planMinutes")),
                        ProjectUtilities.clearWhiteSpaces(basicPlanInfo.getContent("planDataCap")),
                        ProjectUtilities.clearWhiteSpaces(basicPlanInfo.getContent("planTextMessage")),
                        planExtras.getTableData()
                );
                if (message.equals("Plan registrado con exito")) {
                    basicPlanInfo.clear();
                }
                AlertBox.display("Éxito", message, "");
            } else {
                AlertBox.display("Error", message, "");
            }
        });

        newBankButton.setOnAction(e -> {
            basicPlanInfo.clear();
            createExtra.clear();
            saveChangesButton.setText("Agregar Plan");
        });

        menuCircle.setOnMouseClicked(e -> {
            menuListManager.displayMenu();
            ProjectEffects.linearTransitionToRight(menuList, 250, width, height, width, height);
        });


        hBox.getChildren().addAll(menuCircle, newBankButton, icons.getSearchIcon(), saveChangesButton);
        HBox.setMargin(menuCircle, new Insets(0, ((width * 0.10) - circleRadius), 0, 0));
        HBox.setMargin(icons.getSearchIcon(), new Insets(0, (width * 0.1355), 0, (width * 0.135)));
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {

        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private void basicPlanInfo(double width) {
        basicPlanInfo = new EditingPanel("Información del Plan", percentage, width);

        basicPlanInfo.addTextField("planName", "Nombre del plan:");
        basicPlanInfo.addCharacterLimit(100, "planName");

        basicPlanInfo.addTextField("planCost", "Costo:");
        basicPlanInfo.makeFieldFloatOnly("planCost");
        basicPlanInfo.addCharacterLimit(10, "planCost");

        basicPlanInfo.addTextField("planMinutes", "Minutos:");
        basicPlanInfo.makeFieldNumericOnly("planMinutes");
        basicPlanInfo.addCharacterLimit(10, "planMinutes");

        basicPlanInfo.addTextField("planDataCap", "Gigas del plan:");
        basicPlanInfo.makeFieldFloatOnly("planDataCap");
        basicPlanInfo.addCharacterLimit(10, "planDataCap");

        basicPlanInfo.addTextField("planTextMessage", "Número de mensajes:");
        basicPlanInfo.makeFieldNumericOnly("planTextMessage");
        basicPlanInfo.addCharacterLimit(10, "planTextMessage");

        aligner.add(basicPlanInfo);
    }

    private void createExtra(double width) {
        createExtra = new EditingPanel("Añadir Extra", percentage, width);

        String[] extraTypes = {"Voz", "App"};
        createExtra.addComboBox("extraType", "Tipo:", extraTypes, 0);

        createExtra.addTextField("extraName", "Nombre:");
        createExtra.addCharacterLimit(100, "extraName");

        createExtra.addTextField("extraQuantity", "Cantidad:");
        createExtra.makeFieldNumericOnly("extraQuantity");
        createExtra.addCharacterLimit(15, "extraQuantity");
        createExtra.makeFieldFloatOnly("extraQuantity");

        createExtra.addButton("Guardar");
        saveExtra();

        aligner.add(createExtra);
    }

    private void saveExtra(){
        createExtra.getAddButton().setOnAction(e -> {
            String message = "No se pueden dejar campos vacios";
            if (!createExtra.isEmpty() && createExtra.getContent("extraType").equals("Voz")) {
                message = plan.saveNewVoiceMins(
                        ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraName")),
                        ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraQuantity"))
                );

                wasSuccessful(message);
            } else if (!createExtra.isEmpty() && createExtra.getContent("extraType").equals("App")){
                message = plan.saveApp(
                        ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraName")),
                        ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraQuantity"))
                );

                wasSuccessful(message);
            }
            else {
                AlertBox.display("Error", message, "");
            }
        });
    }

    private void wasSuccessful(String message) {
        if (message.equals("Operación realizada con exito")) {
            Extras extras = new Extras(
                    createExtra.getContent("extraName"),
                    Integer.parseInt(ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraQuantity"))),
                    true,
                    (createExtra.getContent("extraType").equals("Voz")) ? 0 : 1
            );
            planExtras.loadTable(extras);
            createExtra.clear();
        }
        AlertBox.display("Éxito ", message, "");
    }

    private void createExistingExtra(double width, double height) {
        planExtras = new EditingPanel("Poner Extras", percentage, width);
        planExtras.createTables(width, height, plan.listExtras());
    }

    private void createExistingExtra(double width, double height, ObservableList<Extras> extras) {
        planExtras.loadTable(extras);
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

    @SuppressWarnings("DuplicatedCode")
    public StackPane renderPlanEditingMenu(double width, double height) {
        StackPane stackPane = new StackPane();
        menuList = menuListManager.display(width, height, percentage);
        basicPlanInfo(width);
        createExtra(width);
        createExistingExtra(width, height);

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(
                basicPlanInfo.sendPane(width, height * 0.1),
                planExtras.sendTable(width, height * 0.0),
                createExtra.sendPane(width, height * 0.0)
        );
        BorderPane planMenu;

        searchPane = new SearchPane(width, height, percentage, true);
        HBox sp = searchPane.showFrame();

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

        planMenu = menu.renderMenuTemplate();
        planMenu.setTop(topBar((HBox) planMenu.getTop(), width, height));
        planMenu.setBottom(botBar((HBox) planMenu.getBottom(), width, height));
        planMenu.setCenter(planMenu.getCenter());

        stackPane.getChildren().addAll(planMenu, menuList, screenFilter, sp); //userMenu, menuList,
        stackPane.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(screenFilter, Pos.CENTER_LEFT);
        StackPane.setAlignment(sp, Pos.CENTER);
        return stackPane;
    }
}
