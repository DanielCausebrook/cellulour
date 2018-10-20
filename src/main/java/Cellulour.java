import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Cellulour extends Application {
    private final int SIZE = 100;
    private final int CELL = 8;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas c = new Canvas(SIZE*CELL,SIZE*CELL);
        StackPane root = new StackPane();
        root.getChildren().add(c);
        primaryStage.setScene(new Scene(root, SIZE*CELL, SIZE*CELL));
        primaryStage.show();
        GraphicsContext gc = c.getGraphicsContext2D();

        Automata a = new Might(SIZE, SIZE);
        Timeline timeline = a.get(gc);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args){
        Application.launch();
    }
}
