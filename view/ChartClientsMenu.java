package view;
import controller.DaoBill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.FA;
import utilities.ProjectEffects;

public class ChartClientsMenu {

    public ChartClientsMenu(double percentage, double buttonFont) {
        bill = new DaoBill();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private double percentage;
    private DaoBill bill;
    private boolean currentBankMode = true;
    private double buttonFont;
    private MenuListManager menuListManager = new MenuListManager();
    private VBox menuList;
    ComboBox<String> chartComboBox = new ComboBox();

    private Button chartClientButtonTemplate(double width, double height, String message) {
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
            menuListManager.displayMenu();
            ProjectEffects.linearTransitionToRight(menuList, 250, width, height, width, height);
        });

        hBox.getChildren().addAll(menuCircle);
        HBox.setMargin(menuCircle, new Insets(0, ((width * 0.10) - circleRadius), 0, 0));
        HBox.setMargin(chartComboBox, new Insets(0, (width * 0.05), 0, (width * 0.05)));
        return hBox;
    }

    private HBox botBar(HBox hBox, double width, double height) {

        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox chartClientInfo(double width, double height) {

        //Hbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width * 0.8, height * 0.9);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-border-width: 4 0 4 0;-fx-border-color: #17161B;-fx-background-color: #24222A;");

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setPrefSize(width * 0.2, height * 0.9);
        centerText.setAlignment(Pos.TOP_CENTER);
        centerText.setStyle("-fx-border-width: 0 2 0 0;-fx-border-color: #FFFFFF;");
        centerText.setSpacing(15);

        StackPane stackChart = new StackPane();
        stackChart.setPrefSize(width * 0.6, height * 0.9);
        stackChart.setAlignment(Pos.CENTER);

        //Text with message
        Text text = new Text("Filtros para Clientes");
        text.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text.setFill(Color.web("#FFFFFF"));
        Button generateChart = chartClientButtonTemplate(width, height,"Generar Grafico");

        DatePicker date = new DatePicker();
        DatePicker dateTo = new DatePicker();

        generateChart.setOnMouseClicked( e -> {
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Grapefruit", 13),
                            new PieChart.Data("Oranges", 25),
                            new PieChart.Data("Plums", 10),
                            new PieChart.Data("Pears", 22),
                            new PieChart.Data("Apples", 30));
            final PieChart chart = new PieChart();
            chart.setTitle("Imported Fruits");
            chart.setLegendSide(Side.LEFT);
            stackChart.getChildren().addAll(chart);
        });
        centerText.getChildren().addAll(text, date, dateTo, generateChart);
        hbox.getChildren().addAll(centerText, stackChart);


        return hbox;
    }

    @SuppressWarnings("DuplicatedCode")
    public StackPane renderChartClientsEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(chartClientInfo(width, height));
        menu.topRightPane();

        menuList = menuListManager.display(width, height, percentage);

        BorderPane chartClientMenu;
        chartClientMenu = menu.renderMenuTemplate();
        chartClientMenu.setTop(topBar((HBox) chartClientMenu.getTop(), width, height));
        chartClientMenu.setBottom(botBar((HBox) chartClientMenu.getBottom(), width, height));
        chartClientMenu.setCenter(chartClientMenu.getCenter());


        stackPane.getChildren().addAll(chartClientMenu, menuList);
        stackPane.setAlignment(Pos.TOP_LEFT);

        return stackPane;
    }
}
