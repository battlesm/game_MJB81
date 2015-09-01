import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car {
	private int Health;
	public ImageView carGraphic;
	
	public Car(){
		Health = 100;
		Image car = new Image(getClass().getClassLoader().getResourceAsStream("Car_Top_View.png"));
        carGraphic = new ImageView(car);
	}
	
	public int getHealth(){
		return Health;
	}
	
	public void changeHealth(int deltaHealth){
		Health += deltaHealth;
	}
	

}
