package view.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utilities.ProjectUtilities;

public class SearchPane {
    private HBox frame;
    private TextField searchField = new TextField();
    private Button closeButton = new Button();
    private ComboBox<String> documentType;
    private double width;
    private double height;
    private BooleanProperty isVisible = new SimpleBooleanProperty();

    public SearchPane(double width, double height, double percentage) {
        this.width = width * 0.6;

        frame = new HBox();
        frame.setMaxSize(this.width, 100);
        frame.setPrefSize(this.width, 100);
        frame.setMinSize(this.width, 100);
        frame.setStyle("-fx-background-color: #3D3946;\n -fx-background-radius: 10px");
        frame.setPadding(new Insets(10, 10, 10, (this.width * 0.05)));

        searchField.setMinSize(this.width * 0.55, height * 0.04175);
        searchField.setMaxSize(this.width * 0.55, height * 0.04175);
        searchField.setPromptText("Escriba el documento para buscar");
        searchField.getStyleClass().add("search-bar");
        searchField.setStyle(searchField.getStyle() + "-fx-font-size: " + (28 - (28 * percentage)) + ";");
        ProjectUtilities.onlyNumericTextField(searchField);

        closeButton.setMinSize(this.width * 0.15, height * 0.05175);
        closeButton.setMaxSize(this.width * 0.15, height * 0.05175);
        closeButton.setText("Cerrar");
        closeButton.getStyleClass().add("close-search-button");
        closeButton.setStyle(closeButton.getStyle() + "-fx-font-size: " + (30 - (30 * percentage)) + ";");
        closeButton.setOnAction(e -> searchField.setText(""));
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(closeButton.getStyle() + "-fx-background-color: #4422AA;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(closeButton.getStyle() + "-fx-background-color: #5639AC;"));

        documentType = new ComboBox<>(FXCollections.observableArrayList(ProjectUtilities.documentTypesAbb));
        documentType.setMaxSize(width * 0.06, height * 0.06175);
        documentType.setMinSize(width * 0.06, height * 0.06175);
        documentType.setStyle(documentType.getStyle() + "-fx-font-size: " + (20 - (20 * percentage)) + "px;\n" +
                "-fx-border-color: #615C70;\n-fx-border-width: 4;");
        documentType.valueProperty().set(ProjectUtilities.documentTypesAbb[1]);

        searchField.focusedProperty().addListener(e -> {
            if (searchField.focusedProperty().get() || documentType.focusedProperty().get())
                frame.setVisible(true);
            else
                frame.setVisible(false);
        });

        documentType.focusedProperty().addListener(e -> {
            if (searchField.focusedProperty().get() || documentType.focusedProperty().get())
                frame.setVisible(true);
            else
                frame.setVisible(false);
        });

        frame.getChildren().addAll(searchField, documentType, closeButton);
        frame.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(searchField, new Insets(0, this.width * 0.05, 0, 0));
        HBox.setMargin(closeButton, new Insets(0, 0, 0, this.width * 0.05));
        isVisible.bind(frame.visibleProperty());
    }

    public SearchPane(double width, double height, double percentage, boolean hasNoComboBox) {
        this.width = width * 0.6;

        frame = new HBox();
        frame.setMaxSize(this.width, 100);
        frame.setPrefSize(this.width, 100);
        frame.setMinSize(this.width, 100);
        frame.setStyle("-fx-background-color: #3D3946;\n -fx-background-radius: 10px");
        frame.setPadding(new Insets(10, 10, 10, (this.width * 0.05)));

        searchField.setMinSize(this.width * 0.70, height * 0.04175);
        searchField.setMaxSize(this.width * 0.70, height * 0.04175);
        searchField.setPromptText("Escriba el nombre del plan");
        searchField.getStyleClass().add("search-bar");
        searchField.setStyle(searchField.getStyle() + "-fx-font-size: " + (28 - (28 * percentage)) + ";");

        closeButton.setMinSize(this.width * 0.15, height * 0.05175);
        closeButton.setMaxSize(this.width * 0.15, height * 0.05175);
        closeButton.setText("Cerrar");
        closeButton.getStyleClass().add("close-search-button");
        closeButton.setStyle(closeButton.getStyle() + "-fx-font-size: " + (30 - (30 * percentage)) + ";");
        closeButton.setOnAction(e -> {
            searchField.setText("");
            setVisible(false);
        });
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(closeButton.getStyle() + "-fx-background-color: #4422AA;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(closeButton.getStyle() + "-fx-background-color: #5639AC;"));

        searchField.focusedProperty().addListener(e -> {
            if (searchField.focusedProperty().get())
                frame.setVisible(true);
            else
                frame.setVisible(false);
        });

        frame.getChildren().addAll(searchField, closeButton);
        frame.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(searchField, new Insets(0, this.width * 0.05, 0, 0));
        HBox.setMargin(closeButton, new Insets(0, 0, 0, this.width * 0.05));
        isVisible.bind(frame.visibleProperty());
    }

    public String getTextContent() {
        return searchField.getText();
    }

    public TextField getSearchField() {
        return searchField;
    }

    public String getDocumentType() {
        return documentType.getValue();
    }

    public void setVisible(boolean value) {
        frame.setVisible(value);
    }

    public void giveFocus() {
        documentType.requestFocus();
    }

    public void addElement(String element){
        documentType.getItems().add(element);
    }

    public BooleanProperty getIsVisible() {
        return isVisible;
    }

    public HBox showFrame() {
        frame.setVisible(false);
        return frame;
    }
}
