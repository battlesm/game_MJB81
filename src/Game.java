import javax.swing.JOptionPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Game {
    public static final String TITLE = "Mad Max: Conquer Fury Road";
    public static final int LANE_SHIFT_DISTANCE = 300;

    
    private Group root;

    private ImageView myBackground;
    private ImageView myBackground1;
    
    private Car myCar;
    private int CAR_SPEED = 300;

    private Rectangle myEnemy;
    
    private Text myScoreText;
    private Text myCarHealthText;
    
    private int currentScore;

    private Scene myScene;
    
    public String getTitle(){
    	return TITLE;
    }
    
    public Scene init (int width, int height) {
        // Create a scene graph to organize the scene
        root = new Group();
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        
        // Make some shapes and set their properties
        
        myCar = new Car(400, 700);
        
        Image road = new Image(getClass().getClassLoader().getResourceAsStream("road.gif"));
        myBackground = new ImageView(road);
        myBackground1 = new ImageView(road);
        
        myBackground.setX(0);
        myBackground.setY(0);
        
        myBackground1.setX(0);
        myBackground1.setY(-900);
        
        this.currentScore = 0;
        myScoreText = new Text("Score: " + currentScore);
        myScoreText.setX(700);
        myScoreText.setY(100);
        
        myCarHealthText = new Text("Health: " + myCar.getHealth());
        myCarHealthText.setX(700);
        myCarHealthText.setY(125);
        
        
        myEnemy = new Rectangle(100, 100, 100, 100);
        myEnemy.setFill(Color.RED);
//        myBottomBlock = new Rectangle(width / 2 - 25, height / 2 + 50, 50, 50);
//        myBottomBlock.setFill(Color.BISQUE);
        // order added to the group is the order in which they are drawn
        
        root.getChildren().add(myBackground);
        root.getChildren().add(myBackground1);
        root.getChildren().add(myCar.carGraphic);
        root.getChildren().add(myEnemy);
        root.getChildren().add(myScoreText);
        root.getChildren().add(myCarHealthText);

//        root.getChildren().add(myTopBlock);
//        root.getChildren().add(myBottomBlock);
        // Respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return myScene;
    }
    
    public void step (double elapsedTime) {
    	if(myCar.getHealth() <= 0){
    		// end game
    		////JOptionPane.showMessageDialog("You lose!");
    	}
    	
    	for(int i = 0; i < root.getChildren().size(); i++){
    		// Checks if given node is an enemy
//    		if(root.getChildren().get(i).getClass() != ){
//    			
//    		}
    	}
    	
    	if(myBackground.getY() >= 900){
    		myBackground.setY(-900);
    		this.updateScore(10);
    	}
    	
    	if(myBackground1.getY() >= 900){
    		myBackground1.setY(-900);
    		this.updateScore(10);
    	}

    	myBackground.setY(myBackground.getY() + CAR_SPEED * elapsedTime);
    	myBackground1.setY(myBackground1.getY() + CAR_SPEED * elapsedTime);
    	
    	myEnemy.setY(myEnemy.getY() + CAR_SPEED * elapsedTime);
    	
    	
        
//        myTopBlock.setRotate(myBottomBlock.getRotate() - 1);
//        myBottomBlock.setRotate(myBottomBlock.getRotate() + 1);
        
        // check for collisions
        // with shapes, can check precisely
//        Shape intersect = Shape.intersect(myTopBlock, myBottomBlock);
//        if (intersect.getBoundsInLocal().getWidth() != -1) {
//            myTopBlock.setFill(Color.MAROON);
//        }
//        else {
//            myTopBlock.setFill(Color.RED);
//        }
        // with images can only check bounding box
        if (myEnemy.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())) {
        	//myCar.changeHealth();
            
        }
//        else {
//            myBottomBlock.setFill(Color.BISQUE);
//        }
    }


    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case RIGHT:
            	if(myCar.carGraphic.getX() != 700){
            		myCar.carGraphic.setX(myCar.carGraphic.getX() + LANE_SHIFT_DISTANCE);
            	}
                break;
            case LEFT:
            	if(myCar.carGraphic.getX() != 100){
                    myCar.carGraphic.setX(myCar.carGraphic.getX() - LANE_SHIFT_DISTANCE);
            	}
                break;
            default:
                // do nothing
        }
    }

    // What to do each time a mouse button is pressed
    private void handleMouseInput (double x, double y) {
        if (myCar.carGraphic.contains(x, y)) {

        }
    }
    
    private void updateScore(int points){
    	currentScore += points;
    	myScoreText.setText("Score: " + currentScore);
    }

}
