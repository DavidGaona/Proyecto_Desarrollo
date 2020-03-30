package utilities;

import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

public class Icons {

    public static Button searchIcon(double percentage) {
        Button searchIcon = new Button();
        double radius = 14;
        searchIcon.setShape(new Circle(radius));
        searchIcon.setText("\uD83D\uDD0D");
        searchIcon.setStyle(
                searchIcon.getStyle() + "-fx-font-size: " + ((radius * 2) - ((radius * 2) * percentage)) + ";\n" +
                "-fx-text-fill: #FFFFFF;\n" + "-fx-background-color: #2E293D;"
        );

        searchIcon.setOnMouseEntered(e -> searchIcon.setStyle(searchIcon.getStyle() + "-fx-background-color: #4422AA;"));
        searchIcon.setOnMouseExited(e -> searchIcon.setStyle(searchIcon.getStyle() + "-fx-background-color: #2E293D;"));
        return searchIcon;
    }
}
