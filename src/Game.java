import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Game {
    private static final String TITLE = "Mad Max: Conquer Fury Road";
    private static final String SCORE_HEADER = "Score: ";
    private static final String HEALTH_HEADER = "Health: ";
    private static final int LANE_SHIFT_DISTANCE = 300;
    private static final int RIGHT_LANE_X = 700;
    private static final int MIDDLE_LANE_X = 400;
    private static final int LEFT_LANE_X = 100;

    private Group root;
    private List<Enemy> enemies;
    private List<Projectile> projectiles;
    private List<PowerUp> powerUps;

    private ImageView myBackground;
    private ImageView myBackground1;
    
    private Car myCar;
    private int CAR_SPEED = 300;
    
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
        enemies = new ArrayList<Enemy>();
        projectiles = new ArrayList<Projectile>();
        powerUps = new ArrayList<PowerUp>();
        
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
        
        this.initializeHeaders();
        
        Giant poop = new Giant(400, 300);
        enemies.add(poop);
        
        // order added to the group is the order in which they are drawn
        
        root.getChildren().add(myBackground);
        root.getChildren().add(myBackground1);
        root.getChildren().add(myCar.carGraphic);
        root.getChildren().add(myScoreText);
        root.getChildren().add(myCarHealthText);
        root.getChildren().add(poop.avatar);
        // Respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return myScene;
    }

	private void initializeHeaders() {
		this.currentScore = 0;
        myScoreText = new Text(SCORE_HEADER + currentScore);
        myScoreText.setX(700);
        myScoreText.setY(100);
        
        myCarHealthText = new Text(HEALTH_HEADER + myCar.getHealth());
        myCarHealthText.setX(700);
        myCarHealthText.setY(125);
	}
    
    public void step (double elapsedTime) {
    	
    	double makeEnemy = Math.random();
    	if(makeEnemy > .99){
    		double type = Math.random();
    		double lane = Math.random();
    		double position = 100 + Math.random() * 400;
    		
    		if(lane < .25){
    			lane = LEFT_LANE_X;
    		}
    		else if(lane < .5){
    			lane = RIGHT_LANE_X;
    		}
    		else{
    			lane = MIDDLE_LANE_X;
    		}
    		
    		if(type < .25){
    			Giant g = new Giant(lane, position);
    			spawnEnemy(g);
    		}
    		else{
    			Warrior w = new Warrior(lane, position);
    			spawnEnemy(w);
    		}
    	}
    	
    	if(!projectiles.isEmpty()){
    		for(int i = projectiles.size() - 1; i > -1; i--){
    			Projectile p = projectiles.get(i);
        		p.move();
        		if(p.proj.getY() <= 0 || p.proj.getY() > 900){
        			removeProjectile(p);
        		}
        		if(!enemies.isEmpty()){
        			for(int j = enemies.size() - 1; j > -1; j--){
            			Enemy e = enemies.get(j);
            			if (p.proj.getBoundsInParent().intersects(e.avatar.getBoundsInParent())) {
            	            e.wasShot(p.getDamage());
            	            removeProjectile(p);
            	            if(e.getHealth() <= 0){
            	            	killEnemy(e);   
            	            }
            	        }
            			if(p.proj.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())){
            				changeHealth(-p.getDamage());
            				removeProjectile(p);
            			}
            		}
        		}
        	}
    	}
    	if(!enemies.isEmpty()){
    		for(int k = enemies.size() - 1; k > -1; k--){
    			Enemy e = enemies.get(k);
    			double doesFireWeapon = Math.random();
    			e.move();
    			if(doesFireWeapon > .99){
    				fireEnemyWeapon(e);
    			}
    	        if (e.avatar.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())) {
    	            changeHealth(-e.shortRangeAttackStrength);
    	            killEnemy(e);
    	        }
    		}
    	}
    	
    	this.hasDrivenSegment(myBackground);
    	this.hasDrivenSegment(myBackground1);
    	
    	this.updatePosition(elapsedTime);
    }

	private void removeProjectile(Projectile p) {
		root.getChildren().remove(p.proj);
		projectiles.remove(p);
	}

	private void spawnEnemy(Enemy e) {
		root.getChildren().add(e.avatar);
		enemies.add(e);
	}

	private void killEnemy(Enemy e) {
		root.getChildren().remove(e.avatar);
		enemies.remove(e);
	}
	
	private void fireEnemyWeapon(Enemy e){
		Projectile p = new Projectile(myCar, Main.Side.Bad, e.avatar.getX() + 50, e.avatar.getY() + 100, .1, e.getLongRangeAttackStrength(  ));
		projectiles.add(p);
		root.getChildren().add(p.proj);
	}

	private void updatePosition(double elapsedTime) {
		myBackground.setY(myBackground.getY() + CAR_SPEED * elapsedTime);
    	myBackground1.setY(myBackground1.getY() + CAR_SPEED * elapsedTime);
	}


    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case RIGHT: // shift right
            	if(myCar.carGraphic.getX() != RIGHT_LANE_X){
            		myCar.carGraphic.setX(myCar.carGraphic.getX() + LANE_SHIFT_DISTANCE);
            	}
                break;
            case LEFT: // shift left
            	if(myCar.carGraphic.getX() != LEFT_LANE_X){
                    myCar.carGraphic.setX(myCar.carGraphic.getX() - LANE_SHIFT_DISTANCE);
            	}
                break;
            case SPACE: // fire weapon
            	Projectile shot = new Projectile(myCar, Main.Side.Good, myCar.carGraphic.getX() + 50, myCar.carGraphic.getY() - 50, 10, myCar.getShotImpact());
            	root.getChildren().add(shot.proj);
            	projectiles.add(shot);
            	break;
            default:
                // do nothing
        }
    }

    // What to do each time a mouse button is pressed
    private void handleMouseInput (double x, double y) {
        if (myCar.carGraphic.contains(x, y)){
        	
        }
    }
    
    private void hasDrivenSegment(ImageView background){
    	if(background.getY() >= 900){
    		background.setY(-900);
    		this.changeScore(10);
    	}
    }
    
    private void changeScore(int points){
    	currentScore += points;
    	myScoreText.setText(SCORE_HEADER + currentScore);
    }
    
    private void changeHealth(double points){
    	myCar.changeHealth(points);
    	myCarHealthText.setText(HEALTH_HEADER + myCar.getHealth());
    	if(myCar.getHealth() <= 0){
    		// end game
    		JOptionPane.showConfirmDialog(null, "You Lose!", "End Game", 0);
    		System.exit(0);
    	}
    }
}
