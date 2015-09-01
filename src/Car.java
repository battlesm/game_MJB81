import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car {
	private int health;
	private int hitImpact;
	private int shotImpact;
	public ImageView carGraphic;
	
	public Car(int x, int y){
		health = 100;
		hitImpact = 50;
		shotImpact = 10;
		
		Image car = new Image(getClass().getClassLoader().getResourceAsStream("Car_Top_View.png"));
        carGraphic = new ImageView(car);
        carGraphic.setX(x);
        carGraphic.setY(y);
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getShot(){
		return shotImpact;
	}
	
	public int getHit(){
		return hitImpact;
	}
	
	public void changeShotImpact(int deltaImpact){
		shotImpact += deltaImpact;
	}
	
	public void changeHitImpact(int deltaImpact){
		hitImpact += deltaImpact;
	}
	
	public void changeHealth(int deltaHealth){
		health += deltaHealth;
	}
	

}
