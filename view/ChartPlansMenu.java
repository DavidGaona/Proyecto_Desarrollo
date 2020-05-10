package view;

import controller.DaoBill;
import controller.DaoChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.DataChart;
import utilities.FA;
import utilities.ProjectEffects;
import utilities.ProjectUtilities;
import view.components.AlertBox;
import view.components.SearchPane;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class ChartPlansMenu {

    private DaoChart daoChart;

    public ChartPlansMenu(double percentage, double buttonFont) {
        bill = new DaoBill();
        this.percentage = percentage;
        this.buttonFont = buttonFont;
        this.daoChart = new DaoChart();
    }

    private double percentage;
    private DaoBill bill;
    private boolean currentBankMode = true;
    private double buttonFont;
    private MenuListManager menuListManager = new MenuListManager();
    private VBox menuList;
    ComboBox<String> chartComboBox = new ComboBox();

    private Button chartPlanButtonTemplate(double width, double height, String message) {
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

    private HBox chartPlanInfo(double width, double height) {

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
        Text text = new Text("Filtros para Planes");
        text.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text.setFill(Color.web("#FFFFFF"));

        Text text2 = new Text("Tipo: ");
        text2.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text2.setFill(Color.web("#FFFFFF"));

        Text text3 = new Text("Desde: ");
        text3.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text3.setFill(Color.web("#FFFFFF"));

        Text text4 = new Text("Hasta: ");
        text4.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text4.setFill(Color.web("#FFFFFF"));

        Button generateChart = chartPlanButtonTemplate(width, height, "Generar Gráfico");

        DatePicker date = new DatePicker();
        DatePicker dateTo = new DatePicker();


        generateChart.setOnMouseClicked(e -> {
            LocalDate from = date.getValue();
            LocalDate to = dateTo.getValue();
            ArrayList<DataChart> data = new ArrayList<DataChart>();
            boolean show = true;

            if (chartComboBox.getValue().equals("Ventas por Mes")) {
                if(from != null && to != null && 12>(ChronoUnit.MONTHS.between(YearMonth.from(from),YearMonth.from(to)))){
                    data = daoChart.getDataPlansPerMonths(from, to);
                }else{
                    show = false;
                    AlertBox.display("Error: ","Por favor seleccione un rango de fechas valido");
                }
                if (data != null && !data.isEmpty()) {
                    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                    for (DataChart dataPiece : data) {
                        pieChartData.add(new PieChart.Data(dataPiece.getValueX(), dataPiece.getValueY()));
                    }
                    final PieChart chart = new PieChart(pieChartData);
                    chart.setTitle("Ventas por Mes");
                    chart.setLegendSide(Side.LEFT);
                    show = false;
                    stackChart.getChildren().clear();
                    stackChart.getChildren().addAll(chart);
                }
            } else if(chartComboBox.getValue().equals("Número de Ventas")) {
                if (from != null && to != null) {
                    data = daoChart.getDataPlansOnRange(from,to);
                }else{
                    show = false;
                    AlertBox.display("Error: ","Por favor seleccione un rango de fechas valido");
                }
                if(data != null && !data.isEmpty()){
                    final CategoryAxis xAxis = new CategoryAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    xAxis.setLabel("Plan");

                    final LineChart<String, Number> lineChart =
                            new LineChart<String, Number>(xAxis, yAxis);

                    lineChart.setTitle("Venta de planes desde "+from.toString()+" hasta "+to.toString());

                    XYChart.Series series = new XYChart.Series();
                    series.setName("Ventas");

                    for (DataChart dataPiece : data) {
                        series.getData().add(new XYChart.Data(dataPiece.getValueX(), dataPiece.getValueY()));
                    }
                    show = false;
                    lineChart.getData().add(series);
                    stackChart.getChildren().clear();
                    stackChart.getChildren().addAll(lineChart);
                }

            }else{
                /* ToDo */
            }
            if(show){
                AlertBox.display("Error: ", "No se pudo generar el grafico");
            }
        });

        centerText.getChildren().addAll(text, text2, chartComboBox, text3, date, text4, dateTo, generateChart);

        hbox.getChildren().addAll(centerText, stackChart);


        return hbox;
    }

    @SuppressWarnings("DuplicatedCode")
    public StackPane renderChartPlansEditMenu(double width, double height) {
        StackPane stackPane = new StackPane();

        EditingMenu menu = new EditingMenu(width, height, percentage);
        menu.addToMidPane(chartPlanInfo(width, height));
        menu.centerPane();

        menuList = menuListManager.display(width, height, percentage);

        BorderPane chartPlanMenu;
        chartPlanMenu = menu.renderMenuTemplate();
        chartPlanMenu.setTop(topBar((HBox) chartPlanMenu.getTop(), width, height));
        chartPlanMenu.setBottom(botBar((HBox) chartPlanMenu.getBottom(), width, height));
        chartPlanMenu.setCenter(chartPlanMenu.getCenter());

        ProjectUtilities.loadComboBox(chartComboBox, ProjectUtilities.chartPlan);
        chartComboBox.setValue(ProjectUtilities.chartPlan[0]);

        stackPane.getChildren().addAll(chartPlanMenu, menuList);
        stackPane.setAlignment(Pos.TOP_LEFT);

        return stackPane;
    }
}