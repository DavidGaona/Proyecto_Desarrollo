package utilities;

import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

public class Icons {
    private Button searchIcon;

    public void searchIcon(double percentage) {
        searchIcon = new Button();
        double radius = 12;
        searchIcon.setShape(new Circle(radius));
        searchIcon.setText("\uD83D\uDD0D");
        searchIcon.setStyle(
                searchIcon.getStyle() + "-fx-font-size: " + ((radius * 2) - ((radius * 2) * percentage)) + ";\n" +
                        "-fx-text-fill: #FFFFFF;\n" + "-fx-font-family: Lucida Sans Unicode;\n" + "-fx-font-weight: bold;\n" +
                        "-fx-background-color: #2E293D;\n"
        );

        searchIcon.setOnMouseEntered(e -> searchIcon.setStyle(searchIcon.getStyle() + "-fx-background-color: #4422AA;"));
        searchIcon.setOnMouseExited(e -> searchIcon.setStyle(searchIcon.getStyle() + "-fx-background-color: #2E293D;"));
    }

    public Button getSearchIcon() {
        return searchIcon;
    }
}
