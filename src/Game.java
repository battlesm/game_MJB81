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
    private static final int MAX_ENEMIES = 10;
	private static final int SPEED_INCREMENT = 10;
	private static final String TITLE = "Mad Max: Conquer Fury Road";
    private static final String SCORE_HEADER = "Score: ";
    private static final String HEALTH_HEADER = "Health: ";
    private static final int LANE_SHIFT_DISTANCE = 300;
    private static final int RIGHT_LANE_X = 700;
    private static final int MIDDLE_LANE_X = 400;
    private static final int LEFT_LANE_X = 100;

    private Group root;
    
    private Screen screen;
    
    private List<Enemy> enemies;
    private List<Projectile> projectiles;

    private ImageView myBackground;
    private ImageView myBackground1;
    
    private Car myCar;
    private int CAR_SPEED = 300;

    private Text myScoreText;
    private Text myCarHealthText;
    
    private int currentScore;
    
    private boolean enemiesFrozen;
    private boolean invincible;

    private Scene myScene;
    
    public String getTitle(){
    	return TITLE;
    }
    
    public Scene init (int width, int height) {
        // Create a scene graph to organize the scene
        root = new Group();
        enemies = new ArrayList<Enemy>();
        projectiles = new ArrayList<Projectile>();
        
        enemiesFrozen = false;
        invincible = false;
        
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        
        initializeMainScreen();
        
        // Respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return myScene;
    }

	private void initializeMainScreen() {
		screen = Screen.MAIN_SCREEN;
		
        Image main = new Image(getClass().getClassLoader().getResourceAsStream("main_screen.jpg"));
        setBackground(main);
	}

	private void initializeLevel1() {
		root.getChildren().clear();
		screen = Screen.LEVEL_1;
		enemies.clear();
		projectiles.clear();
		
		myCar = new Car(400, 700);
        
        Image road = new Image(getClass().getClassLoader().getResourceAsStream("road.gif"));
        setBackground(road);
        this.currentScore = 0;
        this.initializeHeaders();

        // order added to the group is the order in which they are drawn
        
        
        root.getChildren().add(myCar.carGraphic);
        root.getChildren().add(myScoreText);
        root.getChildren().add(myCarHealthText);
	}

	private void setBackground(Image back) {
		myBackground = new ImageView(back);
        myBackground1 = new ImageView(back);
        
        myBackground.setX(0);
        myBackground.setY(0);
        
        myBackground1.setX(0);
        myBackground1.setY(-900);
        
        root.getChildren().add(myBackground);
        root.getChildren().add(myBackground1);
	}

	private void initializeHeaders() {
		
        myScoreText = new Text(SCORE_HEADER + currentScore);
        myScoreText.setX(700);
        myScoreText.setY(100);
        
        myCarHealthText = new Text(HEALTH_HEADER + myCar.getHealth());
        myCarHealthText.setX(700);
        myCarHealthText.setY(125);
	}
    
    public void step (double elapsedTime) {
    	if(screen == Screen.LEVEL_1){
    		if(!enemiesFrozen){
    			createEnemyLevel1();
    		}
    		
        	handleShot();
        	handleCollision();
        	
        	this.hasDrivenSegment(myBackground);
        	this.hasDrivenSegment(myBackground1);
        	
        	this.updatePosition(elapsedTime);
    	}
    	if(screen == Screen.LEVEL_2){
    		if(!enemiesFrozen){
    			createEnemyLevel2();
    		}
    		
    		handleShot();
    		handleCollision();
    		
    		this.hasDrivenSegment(myBackground);
        	this.hasDrivenSegment(myBackground1);
        	
    		this.updatePosition(elapsedTime);
    	}
    }

	private void createEnemyLevel2() {
		// TODO Auto-generated method stub
		double makeEnemy = Math.random();
    	if(makeEnemy > .99 && enemies.size() < MAX_ENEMIES){
    		
    		double lane = Math.random();
    		double position = 100 + Math.random() * 250;
    		
    		if(lane < .3){
    			lane = LEFT_LANE_X;
    		}
    		else if(lane < .6){
    			lane = RIGHT_LANE_X;
    		}
    		else{
    			lane = MIDDLE_LANE_X;
    		}
    		MegaGiant mg = new MegaGiant(lane, position);
    		spawnEnemy(mg);
    	}
		
	}

	private void handleCollision() {
		if(!enemies.isEmpty()){
    		for(int k = enemies.size() - 1; k > -1; k--){
    			Enemy e = enemies.get(k);
    			double doesFireWeapon = Math.random();
    			if(!enemiesFrozen){
    				e.move(myCar);
    			}
    			
    			if(doesFireWeapon > .99 && !enemiesFrozen){
    				fireEnemyWeapon(e);
    			}
    	        if (e.avatar.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())) {
    	            changeHealth(-e.shortRangeAttackStrength);
    	            killEnemy(e);
    	            break;
    	        }
    	        if(e.avatar.getY() >= 800){
    	        	removeEnemy(e);
    	        }
    		}
    	}
	}

	private void handleShot() {
		if(!projectiles.isEmpty()){
			project:
    		for(int i = projectiles.size() - 1; i > -1; i--){
    			
    			Projectile p = projectiles.get(i);
        		p.move();
        		if(p.proj.getY() <= 0 || p.proj.getY() > 900){
        			removeProjectile(p);
        			break;
        		}
        		if(!enemies.isEmpty()){
        			for(int j = enemies.size() - 1; j > -1; j--){
            			Enemy e = enemies.get(j);
            			if (p.proj.getBoundsInParent().intersects(e.avatar.getBoundsInParent())) {
            	            e.wasShot(p.getDamage());
            	            
            	            if(e.getHealth() <= 0){
            	            	killEnemy(e);   
            	            }
            	            removeProjectile(p);
            	            break project;
            	        }
            			if(p.proj.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())){
            				changeHealth(-p.getDamage());
            				removeProjectile(p);
            				break project;
            			}
            		}
        		}
        		
        	}
    	}
	}

	private void createEnemyLevel1() {
		double makeEnemy = Math.random();
    	if(makeEnemy > .99 && enemies.size() < MAX_ENEMIES){
    		double type = Math.random();
    		double lane = Math.random();
    		double position = 100 + Math.random() * 400;
    		
    		if(lane < .3){
    			lane = LEFT_LANE_X;
    		}
    		else if(lane < .6){
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
		removeEnemy(e);
		changeScore(e.getKillScore());
	}

	private void removeEnemy(Enemy e) {
		root.getChildren().remove(e.avatar);
		enemies.remove(e);
	}
	
	private void fireEnemyWeapon(Enemy e){
		Projectile p = new Projectile(myCar, Side.BAD, e.avatar.getX() + 50, e.avatar.getY() + 130, .01, e.getLongRangeAttackStrength());
		projectiles.add(p);
		root.getChildren().add(p.proj);
	}

	private void updatePosition(double elapsedTime) {
		myBackground.setY(myBackground.getY() + CAR_SPEED * elapsedTime);
    	myBackground1.setY(myBackground1.getY() + CAR_SPEED * elapsedTime);
	}


    // What to do each time a key is pressed
	private void handleKeyInput (KeyCode code) {
		if(screen != Screen.MAIN_SCREEN){
			switch (code) {
			case RIGHT: // shift right
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
			case F:
				enemiesFrozen = !enemiesFrozen;
				break;
			case I:
				invincible = !invincible;
				break;
			case DIGIT2:
				screen = Screen.LEVEL_2;
				initializeLevel2();
			default:
			}
		}
	}

	private void firePrimaryWeapon() {
		Projectile shot = new Projectile(myCar, Side.GOOD, myCar.carGraphic.getX() + 50, myCar.carGraphic.getY() - 50, 10, myCar.getShotImpact());
		root.getChildren().add(shot.proj);
		projectiles.add(shot);
	}

    // What to do each time a mouse button is pressed
    private void handleMouseInput (double x, double y) {
    	if (screen == Screen.MAIN_SCREEN){
    		screen = Screen.LEVEL_1;
    		initializeLevel1();
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
    	if(currentScore >= 100 && screen != Screen.LEVEL_2){
    		screen = Screen.LEVEL_2;
    		initializeLevel2();		
    	}
    	else if(currentScore >= 2000){
    		if (JOptionPane.showConfirmDialog(null, "Congratulations! You win! Play Again?", "Winner!", 0) == 0){
    			initializeLevel1();
    		}
    		else{
    			initializeMainScreen();
    		}
    	}
    }
    
    private void initializeLevel2() {
		root.getChildren().clear();
		
		enemies.clear();
		projectiles.clear();
        
        Image desert = new Image(getClass().getClassLoader().getResourceAsStream("desert.jpg"));
        setBackground(desert);
        
        this.initializeHeaders();
        root.getChildren().add(myCarHealthText);
        root.getChildren().add(myScoreText);
		root.getChildren().add(myCar.carGraphic);
		
		
		MegaGiant mg = new MegaGiant(350, 50);
		spawnEnemy(mg);
		
	}

	private void changeHealth(double points){
    	myCar.changeHealth(points);
    	myCarHealthText.setText(HEALTH_HEADER + myCar.getHealth());
    	if(myCar.getHealth() <= 0){
    		// end game
    		if (JOptionPane.showConfirmDialog(null, "You Lose! Try Again?", "End Game", 0) == 0){
    			initializeLevel1();
    		}
    		else{
    			initializeMainScreen();
    		}
    	}
    }
}