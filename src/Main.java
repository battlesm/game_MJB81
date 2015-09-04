import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	    public static final int HEIGHT = 900;
	    public static final int WIDTH = 900;
	    public static final int FRAMES_PER_SECOND = 60;
	    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	    private Game myGame;

	    @Override
	    public void start (Stage s) {
	        myGame = new Game();
	        s.setTitle(myGame.getTitle());

	        Scene scene = myGame.init(WIDTH, HEIGHT);
	        s.setScene(scene);
	        
	        s.show();
	        
	        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
	                                      e -> myGame.step(SECOND_DELAY));
	        Timeline animation = new Timeline();
	        animation.setCycleCount(Timeline.INDEFINITE);
	        animation.getKeyFrames().add(frame);
	        animation.play();
	        
	        s.setResizable(false);
	    }
	    
	    public static void main (String[] args) {
	        launch(args);
	    }
	}