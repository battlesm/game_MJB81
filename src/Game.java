import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Game {
	private static final int BEAT_LEVEL_2 = 2000;
	private static final int BEAT_LEVEL_1 = 1000;
	private static final String TITLE = "Mad Max: Conquer Fury Road";
    private static final String SCORE_HEADER = "Score: ";
    private static final String HEALTH_HEADER = "Health: ";
	
    private static final double GIANT_PROBABILITY = .25;
	private static final double ENEMY_PROBABILITY = .01;
	private static final int CAR_Y = 700;
	private static final int MAX_ENEMIES = 10;
	private static final int SPEED_INCREMENT = 10;
    private static final int LANE_SHIFT_DISTANCE = 300;
    private static final int RIGHT_LANE_X = 700;
    private static final int MIDDLE_LANE_X = 400;
    private static final int LEFT_LANE_X = 100;
    
	private Group root;
	private Scene myScene;
	private Car myCar;
	
	private Screen currentScreen;
	
	private InteractionHandler myInteractions;
	
	private List<Enemy> activeEnemies;
	private List<Projectile> activeProjectiles;
	
	private boolean enemiesFrozen;
	private boolean invincible;
	
	
	private int CAR_SPEED = 300;
	private int currentScore;
	
	private Text myScoreText;
	private Text myCarHealthText;
	
	private ImageView bottomBackground;
	private ImageView topBackground;

	public String getTitle(){
    	return TITLE;
    }
    
    public Scene init (int width, int height) {
        // Create a scene graph to organize the scene
        root = new Group();
        activeEnemies = new ArrayList<Enemy>();
        activeProjectiles = new ArrayList<Projectile>();
        
        myInteractions = new InteractionHandler();
        
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        
        initializeMainScreen();
        
        // Respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return myScene;
    }

	public void initializeMainScreen() {
		currentScreen = Screen.MAIN_SCREEN;
		enemiesFrozen = false;
        invincible = false;
        Image main = new Image(getClass().getClassLoader().getResourceAsStream("main_screen.jpg"));
        setBackground(main);
	}

	private void initializeHeaders() {
		
        myScoreText = new Text(SCORE_HEADER + currentScore);
        myScoreText.setX(RIGHT_LANE_X);
        myScoreText.setY(100);
        
        myCarHealthText = new Text(HEALTH_HEADER + myCar.getHealth());
        myCarHealthText.setX(RIGHT_LANE_X);
        myCarHealthText.setY(125);
        
        root.getChildren().add(myScoreText);
        root.getChildren().add(myCarHealthText);
	}
	
	public void initializeLevel1() {
		clearScreen();
        
        Image road = new Image(getClass().getClassLoader().getResourceAsStream("road.gif"));
        setBackground(road);
        
        myCar = new Car(MIDDLE_LANE_X, CAR_Y);
        root.getChildren().add(myCar.carGraphic);
        
        this.currentScore = 0;
        this.initializeHeaders();
        
        currentScreen = Screen.LEVEL_1;
	}

	private void initializeLevel2() {
		clearScreen();
		
		myCar.changeHealth(50);
		if(myCar.getHealth() > 100){
			myCar.changeHealth((myCar.getHealth() - 100) * -1);
		}

		Image desert = new Image(getClass().getClassLoader().getResourceAsStream("desert.jpg"));
		setBackground(desert);

		root.getChildren().add(myCar.carGraphic);
		this.initializeHeaders();
		
		currentScreen = Screen.LEVEL_2;
	}	
	
	private void setBackground(Image background) {
		bottomBackground = new ImageView(background);
		topBackground = new ImageView(background);

		bottomBackground.setX(0);
		bottomBackground.setY(0);

		topBackground.setX(0);
		topBackground.setY(-900);

		root.getChildren().add(bottomBackground);
		root.getChildren().add(topBackground);
	}
    
    public void step(double elapsedTime) {
    	if(currentScreen == Screen.MAIN_SCREEN){
    		return;
    	}
    	else if(currentScreen == Screen.LEVEL_1 && !enemiesFrozen){
    			createEnemyLevel1();
    	}
    	else if(currentScreen == Screen.LEVEL_2 && !enemiesFrozen){
    		createEnemyLevel2();
    	}
    	
        myInteractions.handleShot(activeProjectiles, activeEnemies, myCar, this, myCarHealthText, invincible);
        myInteractions.handleCollision(activeEnemies, myCar, this, myCarHealthText, enemiesFrozen, invincible);
        	
        this.updatePosition(elapsedTime);
    }

	private void createEnemyLevel2() {
    	if(Math.random() < ENEMY_PROBABILITY && activeEnemies.size() < MAX_ENEMIES){
    		double lane = chooseLaneSpawn(Math.random());
    		double position = 100 + Math.random() * 250;
    		
    		MegaGiant mg = new MegaGiant(lane, position);
    		spawnEnemy(mg);
    	}
	}
	
	


	private void createEnemyLevel1() {
		double makeEnemy = Math.random();
    	if(makeEnemy < ENEMY_PROBABILITY && activeEnemies.size() < MAX_ENEMIES){
    		double type = Math.random();
    		double position = 100 + Math.random() * 400;
    		
    		double lane = chooseLaneSpawn(Math.random());
    		
    		if(type < GIANT_PROBABILITY){
    			Giant g = new Giant(lane, position);
    			spawnEnemy(g);
    		}
    		else{
    			Warrior w = new Warrior(lane, position);
    			spawnEnemy(w);
    		}
    	}
	}


	private double chooseLaneSpawn(double lane) {
		if(lane < .3){
			return LEFT_LANE_X;
		}
		else if(lane < .6){
			return RIGHT_LANE_X;
		}
		else{
			return MIDDLE_LANE_X;
		}
	}


	


	private void spawnEnemy(Enemy e) {
		root.getChildren().add(e.avatar);
		activeEnemies.add(e);
	}


	public void killEnemy(Enemy e) {
		removeEnemy(e);
		changeScore(e.getKillScore());
	}


	public void removeEnemy(Enemy e) {
		root.getChildren().remove(e.avatar);
		activeEnemies.remove(e);
	}
	
	public void removeProjectile(Projectile p) {
		root.getChildren().remove(p.proj);
		activeProjectiles.remove(p);
	}
	

	public void fireEnemyWeapon(Enemy e){
		Projectile p = new Projectile(myCar, Side.BAD, e.avatar.getX() + 50, e.avatar.getY() + 130, .01, e.getLongRangeAttackStrength());
		activeProjectiles.add(p);
		root.getChildren().add(p.proj);
	}


	private void updatePosition(double elapsedTime) {
		this.hasDrivenSegment(bottomBackground);
    	this.hasDrivenSegment(topBackground);
    	
		bottomBackground.setY(bottomBackground.getY() + CAR_SPEED * elapsedTime);
    	topBackground.setY(topBackground.getY() + CAR_SPEED * elapsedTime);
	}


	private void firePrimaryWeapon() {
		Projectile shot = new Projectile(myCar, Side.GOOD, myCar.carGraphic.getX() + 50, myCar.carGraphic.getY() - 50, 10, myCar.getShotImpact());
		root.getChildren().add(shot.proj);
		activeProjectiles.add(shot);
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
    	if(currentScore >= BEAT_LEVEL_1 && currentScreen != Screen.LEVEL_2){
    		initializeLevel2();		
    	}
    	else if(currentScore >= BEAT_LEVEL_2){
    		if (JOptionPane.showConfirmDialog(null, "Congratulations! You win! Play Again?", "Winner!", 0) == 0){
    			initializeLevel1();
    		}
    		else{
    			initializeMainScreen();
    		}
    	}
    }
    
	private void clearScreen() {
		root.getChildren().clear();
		activeEnemies.clear();
		activeProjectiles.clear();
	}
	
	private void handleMouseInput (double x, double y) {
		if (currentScreen == Screen.MAIN_SCREEN){
			initializeLevel1();
		}
	}
	
	private void handleKeyInput (KeyCode code) {
		if(currentScreen != Screen.MAIN_SCREEN){
			switch (code) {
			case RIGHT:
				if(myCar.carGraphic.getX() != RIGHT_LANE_X){
					myCar.carGraphic.setX(myCar.carGraphic.getX() + LANE_SHIFT_DISTANCE);
				}
				break;
			case LEFT:
				if(myCar.carGraphic.getX() != LEFT_LANE_X){
					myCar.carGraphic.setX(myCar.carGraphic.getX() - LANE_SHIFT_DISTANCE);
				}
				break;
			case UP:
				if(CAR_SPEED < 600){
					CAR_SPEED += SPEED_INCREMENT;
				}
				break;
			case DOWN:
				if(CAR_SPEED > 50){
					CAR_SPEED -= SPEED_INCREMENT;
				}
				break;
			case SPACE:
				firePrimaryWeapon();
				break;
			case Q:
				initializeMainScreen();
				break;
			case F:
				enemiesFrozen = !enemiesFrozen;
				break;
			case I:
				invincible = !invincible;
				break;
			case DIGIT1:
				initializeLevel1();
				break;
			case DIGIT2:
				initializeLevel2();
				break;
			default:
			}
		}
	}
}