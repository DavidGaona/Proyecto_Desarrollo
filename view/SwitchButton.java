package view;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class SwitchButton extends StackPane {

    private BooleanProperty switchedOn = new SimpleBooleanProperty(true);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.125));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.125));
    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public void invertSwitchedOn(){
        switchedOn.set(!switchedOn.get());
    }

    public void setSwitchedButton(boolean state){
        switchedOn.set(state);
    }

    public SwitchButton(double width, double height) {
        setMinSize(width, height);

        Rectangle background = new Rectangle(width, height);
        background.setArcWidth(height);
        background.setArcHeight(height);
        background.setFill(Color.web("#5639AC"));
        background.setStroke(Color.web("#3D3D3E"));

        Circle trigger = new Circle(height/2);
        trigger.setCenterX(height/2);
        trigger.setCenterY(height/2);
        trigger.setFill(Color.web("#FFFFFF"));
        trigger.setStroke(Color.web("#3D3D3E"));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        Text message = new Text("Activado");
        message.setFont(new Font("Consolas", 25));
        message.setFill(Color.web("#FFFFFF"));

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger, message);
        setAlignment(trigger, Pos.CENTER_RIGHT);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            translateAnimation.setToX(isOn ? 0 : -(width) + height);
            fillAnimation.setFromValue(isOn ? Color.web("#3D3946") : Color.web("#5639AC"));
            fillAnimation.setToValue(isOn ? Color.web("#5639AC") : Color.web("#3D3946"));
            message.setText(isOn ? "Activado" : "Desactivado");
            animation.play();
        });
    }
}