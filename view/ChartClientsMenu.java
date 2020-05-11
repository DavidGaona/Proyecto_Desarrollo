package view;

import controller.DaoBill;
import controller.DaoChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.DataChart;
import model.TableClient;
import utilities.FA;
import utilities.ProjectEffects;
import view.components.AlertBox;

import java.util.ArrayList;

public class ChartClientsMenu {

    private DaoChart daoChart;

    public ChartClientsMenu(double percentage, double buttonFont) {
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
    private ComboBox<String> chartComboBox = new ComboBox();
    private StackPane stackChart;

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

        stackChart = new StackPane();
        stackChart.setPrefSize(width * 0.6, height * 0.9);
        stackChart.setAlignment(Pos.CENTER);

        //Text with message
        Text text = new Text("Filtros para Clientes");
        text.setFont(new Font("Consolas", 30 - (30 * percentage)));
        text.setFill(Color.web("#FFFFFF"));
        Button generateChart = chartClientButtonTemplate(width, height, "Generar Grafico");

        String[] options = {"Tipos de clientes", "Clientes antiguos", "Mejores clientes"};
        ComboBox<String> optionsCombobox = new ComboBox<>(FXCollections.observableArrayList(options));
        optionsCombobox.setPrefSize(width * 0.15, height * 0.03);

        generateChart.setOnMouseClicked(e -> {
            if (optionsCombobox.getValue().equals("Tipos de clientes")){
                ArrayList<DataChart> data = daoChart.getDataAboutClientsNC(true);
                if (data != null) {
                    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                    for (DataChart dataPiece : data) {
                        pieChartData.add(new PieChart.Data(dataPiece.getValueX(), dataPiece.getValueY()));
                    }
                    final PieChart chart = new PieChart(pieChartData);
                    chart.setTitle("Tipos de Cliente");
                    chart.setLegendSide(Side.LEFT);

                    stackChart.getChildren().clear();
                    stackChart.getChildren().addAll(chart);
                    return;
                }
            } else if (optionsCombobox.getValue().equals("Clientes antiguos")){
                ArrayList<TableClient> data = daoChart.getOldestClients(10);
                if (data != null){
                    showOldestClients(data, width * 0.55, height * 0.8);
                    return;
                }
            } else if(optionsCombobox.getValue().equals("Mejores clientes")){
                
            }
            AlertBox.display("Error: ", "No se pudo generar el gráfico");

        });

        centerText.getChildren().addAll(text, optionsCombobox, generateChart);
        hbox.getChildren().addAll(centerText, stackChart);


        return hbox;
    }

    private void showOldestClients(ArrayList<TableClient> data, double width, double height){
        TableView<TableClient> oldClientTableView = new TableView<>();
        oldClientTableView.setMinSize(width, height);
        oldClientTableView.setMaxSize(width, height);

        TableColumn<TableClient, String> titleColumn = new TableColumn<>("Tabla de seleccionados");
        titleColumn.setMinWidth(width);

        TableColumn<TableClient, String> nameColumn = new TableColumn<>("Nombre Cliente");
        nameColumn.setMinWidth(width * 0.25);
        nameColumn.setMaxWidth(width * 0.25);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<TableClient, String> documentColumn = new TableColumn<>("Número de documento");
        documentColumn.setMinWidth(width * 0.25);
        documentColumn.setMaxWidth(width * 0.25);
        documentColumn.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));

        TableColumn<TableClient, Long> numberColumn = new TableColumn<>("Número de linea");
        numberColumn.setMinWidth(width * 0.25);
        numberColumn.setMaxWidth(width * 0.25);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<TableClient, String> dateColumn = new TableColumn<>("Fecha de adquisición");
        dateColumn.setMinWidth(width * 0.25);
        dateColumn.setMaxWidth(width * 0.25);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        titleColumn.getColumns().addAll(nameColumn, documentColumn, numberColumn, dateColumn);

        oldClientTableView.getColumns().addAll(titleColumn);

        stackChart.getChildren().clear();
        stackChart.getChildren().addAll(oldClientTableView);

        for (TableClient datum: data)
            oldClientTableView.getItems().add(datum);
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
