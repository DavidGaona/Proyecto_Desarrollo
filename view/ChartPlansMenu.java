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
import javafx.scene.text.TextAlignment;
import model.DataChart;
import utilities.FA;
import utilities.ProjectEffects;
import utilities.ProjectUtilities;
import view.components.AlertBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


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
        centerText.setSpacing(25);

        StackPane stackChart = new StackPane();
        stackChart.setPrefSize(width * 0.6, height * 0.9);
        stackChart.setAlignment(Pos.CENTER);

        //Text with message
        Label introLabel = new Label("Filtros para Planes");
        introLabel.setPrefSize(width * 0.15, height * 0.09);
        introLabel.setMaxWidth(width * 0.15);
        introLabel.setWrapText(true);
        introLabel.setTextAlignment(TextAlignment.CENTER);
        introLabel.getStyleClass().add("custom-chart-label");
        introLabel.setStyle(introLabel.getStyle() + "\n-fx-font-size: " + (30 - (30 * percentage)) + ";" );

        Label typeLabel = new Label("Tipo: ");
        typeLabel.setPrefSize(width * 0.15, height * 0.03);
        typeLabel.getStyleClass().add("custom-chart-label");
        typeLabel.setStyle(typeLabel.getStyle() + "\n-fx-font-size: " + (30 - (30 * percentage)) + ";" );

        Label yearLabel = new Label("Año: ");
        yearLabel.setPrefSize(width * 0.15, height * 0.03);
        yearLabel.getStyleClass().add("custom-chart-label");
        yearLabel.setStyle(yearLabel.getStyle() + "\n-fx-font-size: " + (30 - (30 * percentage)) + ";" );

        Label fromLabel = new Label("Desde: ");
        fromLabel.setPrefSize(width * 0.15, height * 0.03);
        fromLabel.getStyleClass().add("custom-chart-label");
        fromLabel.setStyle(fromLabel.getStyle() + "\n-fx-font-size: " + (30 - (30 * percentage)) + ";" );

        Label toLabel = new Label("Hasta: ");
        toLabel.setPrefSize(width * 0.15, height * 0.03);
        toLabel.getStyleClass().add("custom-chart-label");
        toLabel.setStyle(toLabel.getStyle() + "\n-fx-font-size: " + (30 - (30 * percentage)) + ";" );

        Button generateChart = chartPlanButtonTemplate(width, height, "Generar Gráfico");

        chartComboBox.setStyle(chartComboBox.getStyle() + "\n-fx-font-size: " + (20 - (20 * percentage)) + ";");

        AtomicInteger min = new AtomicInteger();
        AtomicBoolean enteredTo = new AtomicBoolean(false);
        AtomicBoolean enteredFrom = new AtomicBoolean(false);
        AtomicInteger fromSelected = new AtomicInteger();
        AtomicInteger toSelected = new AtomicInteger();
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        ComboBox<String> fromCombobox = new ComboBox<>(FXCollections.observableArrayList(months));
        fromCombobox.setPrefSize(width * 0.15, height * 0.03);
        fromCombobox.setStyle(fromCombobox.getStyle() + "\n-fx-font-size: " + (20 - (20 * percentage)) + ";");
        fromCombobox.getItems().remove(11);
        fromCombobox.getSelectionModel().select(0);

        ComboBox<String> toCombobox = new ComboBox<>(FXCollections.observableArrayList(months));
        toCombobox.setPrefSize(width * 0.15, height * 0.03);
        toCombobox.setStyle(toCombobox.getStyle() + "\n-fx-font-size: " + (20 - (20 * percentage)) + ";");
        toCombobox.getItems().remove(0);
        toCombobox.getSelectionModel().select(0);

        fromCombobox.setOnAction(e -> {
            if (enteredTo.get())
                return;
            enteredFrom.set(true);
            toCombobox.getItems().clear();
            min.set(fromCombobox.getSelectionModel().getSelectedIndex() + 1);
            fromSelected.set(fromCombobox.getSelectionModel().getSelectedIndex());
            for (int i = fromCombobox.getSelectionModel().getSelectedIndex() + 1; i < 12; i++)
                toCombobox.getItems().add(months[i]);
            toCombobox.getSelectionModel().select(toSelected.get());
            enteredFrom.set(false);
        });

        toCombobox.setOnAction(e -> {
            if (enteredFrom.get())
                return;
            enteredTo.set(true);
            fromCombobox.getItems().clear();
            toSelected.set(toCombobox.getSelectionModel().getSelectedIndex() - min.get());
            for (int i = 0; i < toCombobox.getSelectionModel().getSelectedIndex() + min.get(); i++)
                fromCombobox.getItems().add(months[i]);
            fromCombobox.getSelectionModel().select(fromSelected.get());
            enteredTo.set(false);
        });

        ComboBox<Integer> yearsComboBox = new ComboBox<>();
        yearsComboBox.setPrefSize(width * 0.15, height * 0.03);
        yearsComboBox.setStyle(yearsComboBox.getStyle() + "\n-fx-font-size: " + (20 - (20 * percentage)) + ";");
        for (int i = 21; i >= 0; i--)
            yearsComboBox.getItems().add(2000 + i);
        yearsComboBox.getSelectionModel().select(1);
        chartComboBox.setPrefSize(width * 0.15, height * 0.03);

        generateChart.setOnMouseClicked(e -> {
            ArrayList<DataChart> data;
            boolean show = true;
            switch (chartComboBox.getValue()) {
                case "Ventas por Mes":
                    if (fromCombobox.getValue() != null && toCombobox.getValue() != null && yearsComboBox.getValue() != null) {
                        String start = yearsComboBox.getValue() + "-" +
                                ProjectUtilities.monthToNumber(fromCombobox.getValue()) + "-01";
                        String end = yearsComboBox.getValue() + "-" +
                                ProjectUtilities.monthToNumber(toCombobox.getValue()) + "-01";
                        LocalDate startDate = LocalDate.parse(start);
                        LocalDate endDate = LocalDate.parse(end);
                        endDate = LocalDate.parse(end.substring(0, 7) + "-" + endDate.lengthOfMonth());
                        data = daoChart.getDataPlansPerMonths(startDate, endDate);
                        if (data != null && !data.isEmpty()) {
                            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                            for (DataChart dataPiece : data) {
                                pieChartData.add(new PieChart.Data(
                                        ProjectUtilities.monthsInSpanish(dataPiece.getValueX()),
                                        dataPiece.getValueY()));
                            }
                            final PieChart pieChart = new PieChart(pieChartData);
                            pieChart.setTitle("Ventas por Mes");
                            pieChart.setLegendSide(Side.LEFT);
                            pieChart.setMaxSize(width * 0.55, height * 0.85);
                            show = false;
                            stackChart.getChildren().clear();
                            stackChart.getChildren().addAll(pieChart);
                        } else
                            AlertBox.display("Error: ", "No se encontraron registros en esas fechas");
                    } else {
                        show = false;
                        AlertBox.display("Error: ", "Por favor seleccione un rango de fechas valido");
                    }
                    break;
                case "Número de Ventas":
                    show = false;
                    if (fromCombobox.getValue() != null && toCombobox.getValue() != null && yearsComboBox.getValue() != null) {
                        String start = yearsComboBox.getValue() + "-" +
                                ProjectUtilities.monthToNumber(fromCombobox.getValue()) + "-01";
                        String end = yearsComboBox.getValue() + "-" +
                                ProjectUtilities.monthToNumber(toCombobox.getValue()) + "-01";
                        LocalDate startDate = LocalDate.parse(start);
                        LocalDate endDate = LocalDate.parse(end);
                        endDate = LocalDate.parse(end.substring(0, 7) + "-" + endDate.lengthOfMonth());
                        data = daoChart.getDataPlansOnRange(startDate, endDate);
                        if (data != null)
                            commonBarChart(stackChart, data, "Número de Ventas", width, height);
                        else
                            AlertBox.display("Error: ", "No se encontraron registros en esas fechas");
                    }
                    break;
                case "Lineas canceladas":
                    show = false;
                    if (fromCombobox.getValue() != null && toCombobox.getValue() != null && yearsComboBox.getValue() != null) {
                        String start = yearsComboBox.getValue() + "-" +
                                ProjectUtilities.monthToNumber(fromCombobox.getValue()) + "-01";
                        String end = yearsComboBox.getValue() + "-" +
                                ProjectUtilities.monthToNumber(toCombobox.getValue()) + "-01";
                        LocalDate startDate = LocalDate.parse(start);
                        LocalDate endDate = LocalDate.parse(end);
                        endDate = LocalDate.parse(end.substring(0, 7) + "-" + endDate.lengthOfMonth());
                        data = daoChart.getCancelledClientsOnRange(startDate, endDate);
                        if (data != null)
                            commonBarChart(stackChart, data, "Número de Cancelados", width, height);
                        else
                            AlertBox.display("Error: ", "No se encontraron registros en esas fechas");
                    }
                    break;
            }
            if (show) {
                AlertBox.display("Error: ", "No se pudo generar el grafico");
            }
        });

        centerText.getChildren().addAll(introLabel, typeLabel, chartComboBox, yearLabel, yearsComboBox, fromLabel,
                fromCombobox, toLabel, toCombobox, generateChart);

        hbox.getChildren().addAll(centerText, stackChart);


        return hbox;
    }

    private void commonBarChart(StackPane stackChart, ArrayList<DataChart> data, String s, double width, double height) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setMaxSize(width * 0.55, height * 0.85);
        bc.setTitle(s);
        xAxis.setLabel("Planes");
        yAxis.setLabel("Cantidad");
        XYChart.Series series = new XYChart.Series();
        for (DataChart datum : data) {
            series.getData().add(new XYChart.Data<>(datum.getValueX(), datum.getValueY()));
        }
        bc.getData().add(series);
        bc.setBarGap(10);
        bc.setCategoryGap(10);
        bc.setLegendVisible(false);
        stackChart.getChildren().clear();
        stackChart.getChildren().addAll(bc);
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
