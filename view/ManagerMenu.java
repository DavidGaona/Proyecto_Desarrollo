package view;

import controller.DaoUser;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManagerMenu {

    EditingPanel basicPlanInfo, planExtras, createExtra;
    private double percentage;
    private DaoUser user;
    private double buttonFont;
    private int maxMsgLength = 0;

    public ManagerMenu(double percentage, double buttonFont) {
        user = new DaoUser();
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

    private void basicPlanInfo(double width){
        basicPlanInfo = new EditingPanel("Información Básica del Plan", percentage, width);

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
    }

    private void createExtra(double width){
        createExtra = new EditingPanel("Añadir Extra", percentage, width);

        String[] extraTypes = {"Voz", "App"};
        createExtra.addComboBox("extraType", "Tipo:", extraTypes, 0);

        createExtra.addTextField("extraName", "Nombre:");
        createExtra.addCharacterLimit(10, "extraName");

        createExtra.addTextField("extraQuantity", "Minutos:");
        createExtra.makeFieldNumericOnly("extraQuantity");
        createExtra.addCharacterLimit(15, "extraQuantity");
        createExtra.makeFieldFloatOnly("extraQuantity");

    }

    @SuppressWarnings("DuplicatedCode")
    public BorderPane renderPlanEditingMenu(double width, double height) {
        basicPlanInfo(width);
        createExtra(width);
        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(
                basicPlanInfo.sendPane(width, height*0.1),
                createExtra.sendPane(width, height*0.1)
        );
        BorderPane planMenu;
        planMenu = menu.renderMenuTemplate();
        planMenu.setTop(topBar((HBox) planMenu.getTop(), width, height));
        planMenu.setBottom(botBar((HBox) planMenu.getBottom(), width, height));
        planMenu.setCenter(planMenu.getCenter());
        return planMenu;
    }
}
