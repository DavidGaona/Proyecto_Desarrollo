package view;

import controller.DaoBank;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Bank;
import utilities.AlertBox;
import utilities.Icons;
import utilities.ProjectEffects;
import utilities.ProjectUtilities;
import view.components.SearchPane;

public class BankMenu {

    public BankMenu(double percentage, double buttonFont) {
        bank = new DaoBank();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    EditingPanel bankInfo;

    private Button saveChangesButton;

    private double percentage;
    private DaoBank bank;
    private boolean currentBankMode = true;
    private double buttonFont;
    private MenuListAdmin menuListAdmin = new MenuListAdmin();
    private VBox menuList;

    private Button bankButtonTemplate(double width, double height, String message) {
        Button button = new Button(message);
        button.setPrefSize(width * 0.15, height * 0.03); //0.10 , 0.03
        button.setStyle("-fx-font-size: " + buttonFont);
        button.getStyleClass().add("client-buttons-template");
        return button;
    }

    private HBox topBar(HBox hBox, double width, double height) {

        double reduction;
        double circleRadius = (height * 0.045) / 2;

        Rectangle marginRect1 = new Rectangle();
        marginRect1.setHeight(0);
        marginRect1.setWidth(width * 0.10 - circleRadius); //0.1475

        Circle menuCircle = new Circle(circleRadius);
        menuCircle.setCenterX(circleRadius);
        menuCircle.setCenterY(circleRadius);
        menuCircle.setFill(Color.web("#FFFFFF"));
        menuCircle.setStroke(Color.web("#3D3D3E"));


        reduction = circleRadius;

        Rectangle marginRect2 = new Rectangle();
        marginRect2.setHeight(0);
        marginRect2.setWidth((width * 0.10) - reduction);

        Button newBankButton = bankButtonTemplate(width, height, "Nuevo banco");

        reduction += (width * 0.15);

        Rectangle marginRect3 = new Rectangle();
        marginRect3.setHeight(0);
        marginRect3.setWidth(width * 0.3 - reduction);

        Rectangle marginRect4 = new Rectangle();
        marginRect4.setHeight(0);
        marginRect4.setWidth(width * 0.004);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Buscar banco por nombre");
        searchTextField.setPrefSize(width * 0.24, height * 0.03);
        searchTextField.getStyleClass().add("bank-search-bar");
        searchTextField.setId("STF1");
        ProjectUtilities.onlyNumericTextField(searchTextField);

        saveChangesButton = bankButtonTemplate(width, height, "Agregar banco");
        saveChangesButton.setOnMouseClicked(e -> {
            if (currentBankMode)
                saveNewBank();
            else
                editBank();
        });

        searchTextField.setOnAction(e -> {
            Bank searchedBank = bank.loadBank(searchTextField.getText());
            if (searchedBank == null) {
                AlertBox.display("Error: ", "Ocurrio un error interno del sistema", "");
            }
            if (searchedBank.isNotBlank()) {
                bankInfo.clear();
                currentBankMode = false;

                bankInfo.setTextField("bankNIT", "" + searchedBank.getNIT());
                bankInfo.setTextField("bankName", searchedBank.getName());
                bankInfo.setTextField("bankAccountNumber", searchedBank.getAccountNumber());
                bankInfo.setSwitchButton("bankState", searchedBank.getState());

                saveChangesButton.setText("Modificar banco");
                bankInfo.disableTextField("bankNIT");
                bankInfo.disableTextField("bankName");
                bankInfo.disableTextField("bankAccountNumber");
            } else {
                AlertBox.display("Error: ", "Banco no encontrado", "");
            }
        });

        ProjectUtilities.focusListener("24222A", "C2B8E0", searchTextField);

        newBankButton.setOnAction(e -> {
            bankInfo.clear();
            saveChangesButton.setText("Agregar banco");
            searchTextField.setText("");
            bankInfo.enableTextField("bankNIT");
            currentBankMode = true;
            bankInfo.enableTextField("bankName");
            bankInfo.enableTextField("bankAccountNumber");
        });

        menuCircle.setOnMouseClicked(e -> {
            menuListAdmin.displayMenu();
            ProjectEffects.linearTransitionToRight(menuList, 250, width, height, width, height);
        });

        hBox.getChildren().addAll(marginRect1, menuCircle, marginRect2, newBankButton, marginRect3,
                marginRect4, searchTextField, saveChangesButton);
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {

        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private void saveNewBank() {
        String message = "No se pueden dejar campos vacios";
        if (!bankInfo.isEmpty()) {
            message = bank.saveNewBank(
                    ProjectUtilities.clearWhiteSpaces(bankInfo.getContent("bankName")),
                    ProjectUtilities.clearWhiteSpaces(bankInfo.getContent("bankAccountNumber")),
                    ProjectUtilities.clearWhiteSpaces(bankInfo.getContent("bankNIT"))
            );
            AlertBox.display("Error ", message, "");
            bankInfo.clear();
        } else {
            AlertBox.display("Error", message, "");
        }

    }

    private void editBank() {
        String message = "No se pueden dejar campos vacios";
        if (!bankInfo.isEmpty()) {
            message = bank.editBank(
                    bankInfo.getSwitchButtonValue("bankState"),
                    bankInfo.getContent("bankNIT")
                    );
            AlertBox.display("Error ", message, "");
            bankInfo.clear();
        } else {
            AlertBox.display("Error ", message, "");
        }
    }

    private void bankInfo(double width) {
        bankInfo = new EditingPanel("Información Banco", percentage, width);

        bankInfo.addTextField("bankNIT", "NIT:");
        bankInfo.addCharacterLimit(20, "bankNIT");
        bankInfo.makeFieldNumericOnly("bankNIT");

        bankInfo.addTextField("bankName", "Nombre:");
        bankInfo.addCharacterLimit(50, "bankName");

        bankInfo.addTextField("bankAccountNumber", "Número de cuenta:");
        bankInfo.addCharacterLimit(20, "bankAccountNumber");
        bankInfo.makeFieldNumericOnly("bankAccountNumber");

        bankInfo.addSwitchButton("bankState", "Estado del banco:", false,
                "Activado", "Desactivado");

    }

    @SuppressWarnings("DuplicatedCode")
    public StackPane renderBankEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();
        bankInfo(width);

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(bankInfo.sendPane(width, height * 0.1));

        menuList = menuListAdmin.display(width, height, percentage);

        SearchPane searchPane = new SearchPane(width, height, percentage);
        HBox sp = searchPane.showFrame();

        BorderPane bankMenu;
        bankMenu = menu.renderMenuTemplate();
        bankMenu.setTop(topBar((HBox) bankMenu.getTop(), width, height));
        bankMenu.setBottom(botBar((HBox) bankMenu.getBottom(), width, height));
        bankMenu.setCenter(bankMenu.getCenter());

        stackPane.getChildren().addAll(bankMenu, menuList, sp);
        stackPane.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(sp, Pos.CENTER);

        return stackPane;
    }
}
