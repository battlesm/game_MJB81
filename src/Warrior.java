import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Warrior extends Enemy {
	public Warrior(double x, double y){
		Image warrior = new Image(getClass().getClassLoader().getResourceAsStream("warrior.jpg"));
        avatar = new ImageView(warrior);
        avatar.setX(x);
        avatar.setY(y);
        
		velocity = 1;
		
		health = Math.random() * 50;
		longRangeAttackStrength = 5 + Math.random() * 5;
		shortRangeAttackStrength = 10;
		killScore = 10;
	}
}
