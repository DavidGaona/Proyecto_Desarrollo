package utilities;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class ProjectEffects {

    private static FadeTransition ft = new FadeTransition();

    public static void linearTransitionToRight(Node node, double initialWidth, double initialHeight, double toWidth, double toHeight){
        double constant = 0.102;
        Path path = new Path();
        path.getElements().add(new MoveTo(-initialWidth * constant,initialHeight/2));
        path.getElements().add(new CubicCurveTo(toWidth * constant, toHeight/2, toWidth * constant, toHeight/2,
                                                toWidth * constant, toHeight/2));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(250));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }

    public static void fadeTransition(Node node){
        ft.setNode(node);
        ft.setDuration(new Duration(700));
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setAutoReverse(true);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.play();
    }

    public static void stopFadeTransition(){
        ft.stop();
        ft.setDuration(new Duration(7));
        ft.setCycleCount(2);
        ft.play();
    }
}
