package view;

import controller.DaoPlan;
import controller.DaoUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PlanTable;
import utilities.AlertBox;
import utilities.ProjectUtilities;

import java.util.ArrayList;

public class ManagerMenu {

    private EditingPanel basicPlanInfo, planExtras, createExtra;
    private ArrayList<EditingPanel> aligner = new ArrayList<>();
    private double percentage;
    private DaoPlan plan;
    private double buttonFont;

    public ManagerMenu(double percentage, double buttonFont) {
        plan = new DaoPlan();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private HBox topBar(HBox hBox, double width, double height) {

        hBox.setAlignment(Pos.CENTER);
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

        createExtra.addTextField("extraQuantity", "Minutos:");
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

                if (message.equals("Operación realizada con exito")) {
                    PlanTable planTable = new PlanTable(
                            createExtra.getContent("extraName"),
                            Integer.parseInt(ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraQuantity"))),
                            true,
                            1
                    );
                    planExtras.loadTable(planTable);
                    createExtra.clear();
                }

                AlertBox.display("Error ", message, "");

            } else if (!createExtra.isEmpty() && createExtra.getContent("extraType").equals("App")){
                message = plan.saveApp(
                        ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraName")),
                        ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraQuantity"))
                );

                if (message.equals("Operación realizada con exito")) {
                    PlanTable planTable = new PlanTable(
                            createExtra.getContent("extraName"),
                            Integer.parseInt(ProjectUtilities.clearWhiteSpaces(createExtra.getContent("extraQuantity"))),
                            true,
                            1
                    );
                    planExtras.loadTable(planTable);
                    createExtra.clear();
                }
                AlertBox.display("Exito ", message, "");

            }
            else {
                AlertBox.display("Error", message, "");
            }
        });
    }

    private void createExistingExtra(double width, double height) {
        planExtras = new EditingPanel("Poner Extras", percentage, width);

        ObservableList<PlanTable> test = FXCollections.observableArrayList();
        test.add(new PlanTable("Minutos Estados Unidos", 100, true, 1));
        test.add(new PlanTable("Minutos Colombia", 10, false, 1));
        test.add(new PlanTable("Megas Discord", 5000, true, 1));
        test.add(new PlanTable("Megas WhatsApp", 500000, false, 1));

        planExtras.createTables(width, height, test);
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
    public BorderPane renderPlanEditingMenu(double width, double height) {
        basicPlanInfo(width);
        createExtra(width);
        createExistingExtra(width, height);

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(
                basicPlanInfo.sendPane(width, height * 0.1),
                createExtra.sendPane(width, height * 0.0),
                planExtras.sendTable(width, height * 0.0)
        );
        BorderPane planMenu;
        planMenu = menu.renderMenuTemplate();
        planMenu.setTop(topBar((HBox) planMenu.getTop(), width, height));
        planMenu.setBottom(botBar((HBox) planMenu.getBottom(), width, height));
        planMenu.setCenter(planMenu.getCenter());
        return planMenu;
    }
}
