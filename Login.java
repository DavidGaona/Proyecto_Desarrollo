import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.DaoClient;

public class Login {

    public Login(DaoClient client, double percentage, double buttonFont) {
        this.client = client;
        this.percentage = percentage;
        this.buttonFont = buttonFont;
    }

    private DaoClient client;
    private double percentage;
    private double buttonFont;

    private GridPane loginGridPane(double width, double height){
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #22282A");


        return gridPane;
    }

    private StackPane mainLoginPane(double width, double height){
        StackPane background = new StackPane();
        background.setStyle("-fx-background-color: #171A1C");

        GridPane gridPane = loginGridPane(width, height);

        return background;
    }

    public Scene loginScene(double width, double height){
        Scene loginScene;

        loginScene = new Scene(mainLoginPane(width, height));

        return loginScene;
    }
}
