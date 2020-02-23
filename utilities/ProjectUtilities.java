package utilities;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ProjectUtilities {

    public static boolean isNumeric(String inputData) {
        return inputData.matches("\\d+(\\d+)?");
    }

    public static final String[] documentTypes = {"Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería", "Pasaporte"};
    public static final String[] clientTypes = {"Natural", "Corporativo"};
    public static final String[] userTypes = {"Operador", "Gerente", "Administrador"};
    public static final String[] documentTypesAbb = {"TI", "CC", "PA", "CE"};

    public static short convertDocumentType(String documentType) {
        if (documentTypes[0].equals(documentType) || documentTypesAbb[1].equals(documentType)) {
            return (short) 0;
        } else if (documentTypes[1].equals(documentType) || documentTypesAbb[0].equals(documentType)) {
            return (short) 1;
        } else if (documentTypes[2].equals(documentType) || documentTypesAbb[3].equals(documentType)) {
            return (short) 2;
        } else if (documentTypes[3].equals(documentType) || documentTypesAbb[2].equals(documentType)) {
            return (short) 3;
        }
        return (short) -1;
    }


    public static String clearWhiteSpaces(String inputString) {
        StringBuilder newString = new StringBuilder();
        boolean whiteSpace = false;
        boolean firstTime = true;
        for (int i = 0; i < inputString.length(); i++) {
            if (firstTime) {
                if (!(inputString.charAt(i) == ' ')) {
                    firstTime = false;
                    newString.append(inputString.charAt(i));
                }
            } else if (!(inputString.charAt(i) == ' ')) {
                if (whiteSpace) {
                    newString.append(" ").append(inputString.charAt(i));
                } else {
                    newString.append(inputString.charAt(i));
                }
                whiteSpace = false;
            } else {
                if (!whiteSpace) {
                    whiteSpace = true;
                }
            }
        }
        return newString.toString();
    }

    public static String convertDocumentTypeString(short documentType) {
        if (documentType == (short) 0) {
            return documentTypes[0];
        } else if (documentType == (short) 1) {
            return documentTypes[1];
        } else if (documentType == (short) 2) {
            return documentTypes[2];
        } else if (documentType == (short) 3) {
            return documentTypes[3];
        }
        return documentTypes[0];
    }

    public static short convertClientType(String type) {
        if (clientTypes[0].equals(type)) {
            return (short) 0;
        } else if (clientTypes[1].equals(type)) {
            return (short) 1;
        }
        return (short) -1;
    }

    public static String convertClientTypeString(short type) {
        if (type == (short) 0) {
            return clientTypes[0];
        } else if (type == (short) 1) {
            return clientTypes[1];
        }
        return clientTypes[0];
    }

    public static short convertUserType(String type) {
        if (userTypes[0].equals(type)) {
            return (short) 0;
        } else if (userTypes[1].equals(type)) {
            return (short) 1;
        } else if (userTypes[2].equals(type)){
            return (short) 2;
        }
        return (short) -1;
    }

    public static String convertUserTypeString(short type) {
        if (type == (short) 0) {
            return userTypes[0];
        } else if (type == (short) 1) {
            return userTypes[1];
        } else if (type == (short) 2){
            return userTypes[2];
        }
        return clientTypes[0];
    }

    public static void onlyNumericTextField(TextField searchTextField) {
        searchTextField.setOnKeyTyped(e -> {
            if (!(ProjectUtilities.isNumeric(searchTextField.getText()))) {
                String correctText = searchTextField.getText().replaceAll("[^\\d]", "");
                int prevPos = searchTextField.getCaretPosition();
                searchTextField.setText(correctText);
                searchTextField.positionCaret(prevPos - 1);
            }
        });

    }

    public static void resetNodeBorderColor(Node... nodes) {
        for (Node node : nodes) {
            node.setStyle(node.getStyle() + "\n-fx-border-color: #3d3d3d;");
        }
    }

    public static void addTextFieldCharacterLimit(int limit, TextField... textFields) {
        for (TextField textField : textFields) {
            textField.textProperty().addListener(e -> {
                if (textField.getText().length() > limit) {
                    int prevPos = textField.getCaretPosition();
                    String limitedText = textField.getText().substring(0, prevPos) + textField.getText().substring(prevPos + 1);
                    textField.setText(limitedText);
                    textField.positionCaret(prevPos);
                }
            });
        }
    }

    private static Node selectedNode, lastSelectedNode;
    public static void focusListener(GridPane layout, Node... nodes) {
        // Install the same listener on all of them
        for (Node textField : nodes) {
            textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {

                // Set the selectedTextField to null whenever focus is lost. This accounts for the
                // TextField losing focus to another control that is NOT a TextField
                selectedNode = null;

                if (newValue) {
                    // The new node is focused, so set the global reference
                    lastSelectedNode = textField;
                    selectedNode = textField;
                    String textFieldId = selectedNode.getId();
                    selectedNode.setStyle(textField.getStyle() + "\n-fx-border-color: #C2B8E0;");
                    for (Node node : layout.getChildren()) {
                        if (textFieldId.substring(2).equals(node.getId().substring(1))) {
                            ((Text) node).setFill(Color.web("#C2B8E0"));
                            break;
                        }
                    }
                } else {
                    String textFieldId = lastSelectedNode.getId();
                    if (lastSelectedNode != null) {
                        lastSelectedNode.setStyle(textField.getStyle() + "\n-fx-border-color: #3d3d3d;");
                        for (Node node : layout.getChildren()) {
                            if (textFieldId.substring(2).equals(node.getId().substring(1))) {
                                ((Text) node).setFill(Color.web("#948FA3"));
                                break;
                            }
                        }
                    }
                }

            });
        }
    }

    public static void focusListener(String oldColor, String newColor, Node... nodes) {
        for (Node node : nodes) {
            node.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
                selectedNode = null;
                if (newValue) {
                    lastSelectedNode = node;
                    selectedNode = node;
                    selectedNode.setStyle(node.getStyle() + "\n-fx-border-color: #" + newColor + ";");
                } else {
                    if (lastSelectedNode != null) {
                        lastSelectedNode.setStyle(node.getStyle() + "\n-fx-border-color: #" + oldColor + ";");
                    }
                }

            });
        }
    }

}
