package view;

import controller.DaoBank;

import controller.DaoBill;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.Bank;
import utilities.AlertBox;
import utilities.FA;
import utilities.ProjectEffects;
import utilities.ProjectUtilities;
import view.components.SearchPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BillingMenu {

    public BillingMenu(double percentage, double buttonFont) {
        bill = new DaoBill();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private double percentage;
    private DaoBill bill;
    private boolean currentBankMode = true;
    private double buttonFont;
    private MenuListAdmin menuListAdmin = new MenuListAdmin();
    private VBox menuList;
    ComboBox<String> searchComboBox = new ComboBox();

    private Button billingButtonTemplate(double width, double height, String message) {
        Button button = new Button(message);
        button.setPrefSize(width * 0.15, height * 0.03); //0.10 , 0.03
        button.setStyle("-fx-font-size: " + buttonFont);
        button.getStyleClass().add("client-buttons-template");
        return button;
    }

    private HBox topBar(HBox hBox, double width, double height) {

        double circleRadius = (height * 0.045) / 2;
        hBox.setPadding(new Insets(0, 0, 0, ((width * 0.10) - circleRadius)));

        Label menuCircle = new Label();
        menuCircle.setFont(Font.loadFont(FA.getFont(), (40 - (40 * percentage))));
        menuCircle.setText(FA.COGS);
        menuCircle.setStyle(
                menuCircle.getStyle() + "-fx-text-fill: #FFFFFF;\n" + "-fx-font-weight: bold;\n" +
                        "-fx-background-color: #2E293D;\n"
        );

        menuCircle.setOnMouseClicked(e -> {
            menuListAdmin.displayMenu();
            ProjectEffects.linearTransitionToRight(menuList, 250, width, height, width, height);
        });

        hBox.getChildren().addAll(menuCircle);
        HBox.setMargin(menuCircle, new Insets(0, ((width * 0.10) - circleRadius), 0, 0));
        HBox.setMargin(searchComboBox, new Insets(0, (width * 0.05), 0, (width * 0.05)));
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {

        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox billingInfo(double width, double height) {

        //Hbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width * 0.3, height * 0.2);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-border-width: 4 0 4 0;-fx-border-color: #17161B;-fx-background-color: #24222A;");

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setMaxWidth(width * 0.2);
        centerText.setAlignment(Pos.CENTER);
        centerText.setSpacing(15);

        //Text with message
        Text text = new Text("Facturación");
        text.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text.setFill(Color.web("#FFFFFF"));

        Button generateBillButton = billingButtonTemplate(width, height, "Generar Factura");
        Button downloadBills = billingButtonTemplate(width, height, "Descargar Facturas");

        generateBillButton.setOnMouseClicked( e -> {
            AlertBox.display("Mensaje: ",bill.generateBills(),"");
        });
        downloadBills.setOnMouseClicked( e -> {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Guardar en la carpeta especificada: ");

            File selectedFile = directoryChooser.showDialog(null);
            if (selectedFile != null) {
                AlertBox.display("Mensaje: ", bill.getAllBills(selectedFile.getAbsolutePath()),"");
            }

        });

        centerText.getChildren().addAll( text , generateBillButton, downloadBills);
        hbox.getChildren().addAll(centerText);


        return hbox;
    }

    @SuppressWarnings("DuplicatedCode")
    public StackPane renderBillingEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(billingInfo(width, height));
        menu.centerPane();

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

