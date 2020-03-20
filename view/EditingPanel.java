package view;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.ProjectUtilities;

import java.util.Collections;
import java.util.ArrayList;

public class EditingPanel {

    private ArrayList<TextField> textFields = new ArrayList<>();
    private ArrayList<ComboBox<String>> comboBoxes = new ArrayList<>();
    private ArrayList<SwitchButton> switchButtons = new ArrayList<>();
    private ArrayList<Text> texts = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private GridPane tagsPane = new GridPane();
    private double percentage;
    private int maxMsgLength = 0;

    private String color = "#948FA3";
    private String title;

    EditingPanel(String title, double percentage, double width) {
        tagsPane.setPadding(new Insets(25, 10, 25, 10));
        tagsPane.setPrefWidth(width * 0.4);
        tagsPane.setVgap(25);
        tagsPane.setHgap(10);
        tagsPane.setStyle("-fx-background-color: #302E38;\n-fx-border-style: solid inside;\n" +
                "-fx-border-color: #28272F;\n-fx-border-width: 0;");
        this.title = title;
        this.percentage = percentage;
    }

    private TextField textFieldTemplate(String id) {
        TextField textField = new TextField();
        textField.getStyleClass().add("client-text-field-template");
        textField.setStyle(textField.getStyle() + "-fx-font-size: " + (20 - (20 * percentage)) + "px;");
        textField.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        textField.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        textField.setId(id);
        return textField;
    }

    private ComboBox<String> comboBoxTemplate(String id, String[] elements) {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(elements));
        comboBox.setPrefSize(350 - (350 * percentage), 40 - (40 * percentage));
        comboBox.setMinSize(350 - (350 * percentage), 40 - (40 * percentage));
        comboBox.setStyle(comboBox.getStyle() + "-fx-font-size: " + (20 - (20 * percentage)) + "px;");
        comboBox.setId(id);
        return comboBox;
    }

    private SwitchButton switchButtonTemplate(String id, boolean defaultState, String onMessage, String offMessage) {
        SwitchButton switchButton = new SwitchButton(350 - (350 * percentage), 45 - (45 * percentage),
                defaultState, onMessage, offMessage);
        switchButton.setOnMouseClicked(e -> switchButton.invertSwitchedOn());
        switchButton.setId(id);
        return switchButton;
    }

    private void addText(String id, String title, String color) {
        Text text = new Text(title);
        text.setFont(new Font("Consolas", 20 - (20 * percentage)));
        text.setFill(Color.web(color));
        text.setId(id);
        texts.add(text);
    }

    @SuppressWarnings("DuplicatedCode")
    public void addTextField(String name, String message) {
        textFields.add(textFieldTemplate(name));
        addText(name, message, color);
        names.add(name);
        comboBoxes.add(null);
        switchButtons.add(null);
        int index = texts.size() - 1;
        maxMsgLength = Math.max(maxMsgLength, message.length());

        GridPane.setConstraints(texts.get(index), 4, index);
        GridPane.setHalignment(texts.get(index), HPos.RIGHT);
        GridPane.setConstraints(textFields.get(index), 5, index);
        tagsPane.getChildren().addAll(texts.get(index), textFields.get(index));
    }

    public void addTextField(String name, String message, String color) {
        addTextField(name, message);
        getText(name).setFill(Color.web(color));
    }

    @SuppressWarnings("DuplicatedCode")
    public void addComboBox(String name, String message, String[] elements) {
        comboBoxes.add(comboBoxTemplate(name, elements));
        addText(name, message, color);
        names.add(name);
        textFields.add(null);
        switchButtons.add(null);
        int index = texts.size() - 1;
        maxMsgLength = Math.max(maxMsgLength, message.length());

        GridPane.setConstraints(texts.get(index), 4, index);
        GridPane.setHalignment(texts.get(index), HPos.RIGHT);
        GridPane.setConstraints(comboBoxes.get(index), 5, index);
        tagsPane.getChildren().addAll(texts.get(index), comboBoxes.get(index));
    }

    @SuppressWarnings("DuplicatedCode")
    public void addComboBox(String name, String message, String[] elements, int index) {
        addComboBox(name, message, elements);
        getComboBox(name).getSelectionModel().select(index);
    }

    @SuppressWarnings("DuplicatedCode")
    public void addSwitchButton(String name, String message, boolean defaultState, String onMessage, String offMessage) {
        switchButtons.add(switchButtonTemplate(name, defaultState, onMessage, offMessage));
        addText(name, message, color);
        names.add(name);
        textFields.add(null);
        comboBoxes.add(null);
        int index = texts.size() - 1;
        maxMsgLength = Math.max(maxMsgLength, message.length());

        GridPane.setConstraints(texts.get(index), 4, index);
        GridPane.setHalignment(texts.get(index), HPos.RIGHT);
        GridPane.setConstraints(switchButtons.get(index), 5, index);
        tagsPane.getChildren().addAll(texts.get(index), switchButtons.get(index));
    }

    private TextField getTextfield(String id) {
        for (int i = 0; i < names.size(); ++i) {
            if (names.get(i).equals(id))
                return textFields.get(i);
        }
        return new TextField();
    }

    public void setTextField(String id, String value) {
        getTextfield(id).setText(value);
    }

    private ComboBox<String> getComboBox(String id) {
        for (int i = 0; i < names.size(); ++i) {
            if (names.get(i).equals(id))
                return comboBoxes.get(i);
        }
        return new ComboBox<>();
    }

    public void setComboBox(String id, String value) {
        getComboBox(id).valueProperty().set(value);
    }

    private SwitchButton getSwitchButton(String id) {
        for (int i = 0; i < names.size(); ++i) {
            if (names.get(i).equals(id))
                return switchButtons.get(i);
        }
        return switchButtons.get(0);
    }

    public void setSwitchButton(String id, boolean value) {
        getSwitchButton(id).setSwitchedButton(value);
    }

    private Text getText(String id) {
        for (int i = 0; i < names.size(); ++i) {
            if (names.get(i).equals(id))
                return texts.get(i);
        }
        return new Text();
    }

    private int getIndex(String id) {
        for (int i = 0; i < names.size(); ++i) {
            if (names.get(i).equals(id))
                return i;
        }
        return -1;
    }

    public String getContent(String id) {
        if (textFields.get(getIndex(id)) != null)
            return getTextfield(id).getText();
        else if (comboBoxes.get(getIndex(id)) != null)
            return getComboBox(id).getValue();
        else
            return "";
    }

    public boolean getSwitchButtonValue(String id) {
        return getSwitchButton(id).switchedOnProperty().get();
    }

    public void swap(String name1, String name2) {
        int indexName1 = getIndex(name1);
        int indexName2 = getIndex(name2);

        Collections.swap(textFields, indexName1, indexName2);
        Collections.swap(comboBoxes, indexName1, indexName2);
        Collections.swap(switchButtons, indexName1, indexName2);
        Collections.swap(texts, indexName1, indexName2);
        Collections.swap(names, indexName1, indexName2);

        GridPane.setConstraints(texts.get(indexName1), 4 + indexName1, 0);
        GridPane.setHalignment(texts.get(indexName1), HPos.RIGHT);
        if (textFields.get(indexName1) != null)
            GridPane.setConstraints(textFields.get(indexName1), 5 + indexName1, 0);
        else if (comboBoxes.get(indexName1) != null)
            GridPane.setConstraints(comboBoxes.get(indexName1), 5 + indexName1, 0);
        else if (switchButtons.get(indexName1) != null) {
            GridPane.setConstraints(switchButtons.get(indexName1), 5 + indexName1, 0);
        }

        GridPane.setConstraints(texts.get(indexName2), 4 + indexName2, 0);
        GridPane.setHalignment(texts.get(indexName2), HPos.RIGHT);
        if (textFields.get(indexName2) != null)
            GridPane.setConstraints(textFields.get(indexName2), 5 + indexName2, 0);
        else if (comboBoxes.get(indexName2) != null)
            GridPane.setConstraints(comboBoxes.get(indexName2), 5 + indexName2, 0);
        else if (switchButtons.get(indexName2) != null) {
            GridPane.setConstraints(switchButtons.get(indexName2), 5 + indexName2, 0);
        }
    }

    public void clear() {
        clearTextFields();
        clearComboBoxes();
        resetSwitchButtons();
        resetHighLightColor();
    }

    private void clearTextFields() {
        for (TextField textField : textFields) {
            if (textField != null)
                textField.setText("");
        }
    }

    private void clearComboBoxes() {
        for (ComboBox<String> comboBox : comboBoxes) {
            if (comboBox != null)
                comboBox.valueProperty().set(null);
        }
    }

    private void resetSwitchButtons() {
        for (SwitchButton switchButton : switchButtons) {
            if (switchButton != null)
                switchButton.setToDefault();
        }
    }

    private void resetHighLightColor() {
        for (int i = 0; i < names.size(); ++i) {
            if (textFields.get(i) != null)
                textFields.get(i).setStyle(textFields.get(i).getStyle() + "\n-fx-border-color: #3d3d3d;");
            else if (comboBoxes.get(i) != null)
                comboBoxes.get(i).setStyle(comboBoxes.get(i).getStyle() + "\n-fx-border-color: #3d3d3d;");
        }
    }

    public boolean isEmpty() {
        boolean checker = false;
        for (int i = 0; i < names.size(); ++i) {
            if (textFields.get(i) != null) {
                if (textFields.get(i).getText().isBlank()) {
                    textFields.get(i).setStyle(textFields.get(i).getStyle() + "\n-fx-border-color: #ED1221;");
                    checker = true;
                }

            } else if (comboBoxes.get(i) != null) {
                if (comboBoxes.get(i).getValue() == null) {
                    comboBoxes.get(i).setStyle(comboBoxes.get(i).getStyle() + "\n-fx-border-color: #ED1221;");
                    checker = false;
                }

            }
        }
        return checker;
    }

    //Restrictions

    public void addCharacterLimit(int limit, String id) { ProjectUtilities.addTextFieldCharacterLimit(limit, getTextfield(id)); }

    public void makeFieldNumericOnly(String id) { ProjectUtilities.onlyNumericTextField(getTextfield(id)); }

    public void makeFieldFloatOnly(String id) { ProjectUtilities.onlyFloatTextField(getTextfield(id)); }

    public void addRegexConstraint(String pattern) {

    }

    public HBox sendPane(double width, double height) {
        //Hbox
        HBox hbox = new HBox();
        hbox.setPrefSize(width * 0.6, height);
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setStyle("-fx-border-width: 4;-fx-border-color: #17161B;-fx-background-color: #24222A;");

        //StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.setPrefSize(width * 0.2, height);

        //Rectangle bg
        Rectangle rect = new Rectangle();
        rect.setHeight(Math.max(height, 280.0));
        rect.setWidth(width * 0.2);
        rect.setFill(Color.web("#24222A"));

        //VBox to center the text
        VBox centerText = new VBox();
        centerText.setMaxWidth(width * 0.2);
        centerText.setAlignment(Pos.TOP_CENTER);

        //Text with message
        Text text = new Text(title);
        text.setFont(new Font("Consolas", 30));
        text.setFill(Color.web("#FFFFFF"));

        //Margin for the text
        Rectangle marginRect = new Rectangle();
        marginRect.setHeight(30);
        marginRect.setWidth(0);
        marginRect.setFill(Color.web("#24222A"));

        //Install focus listener
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < names.size(); ++i) {
            if (textFields.get(i) != null)
                nodes.add(textFields.get(i));
            else if (comboBoxes.get(i) != null)
                nodes.add(comboBoxes.get(i));
            else if (switchButtons.get(i) != null)
                nodes.add(switchButtons.get(i));
        }
        ProjectUtilities.focusListener(tagsPane, nodes);

        centerText.getChildren().addAll(marginRect, text);
        stackPane.getChildren().addAll(rect, centerText);
        hbox.getChildren().addAll(stackPane, tagsPane);

        return hbox;
    }


}
