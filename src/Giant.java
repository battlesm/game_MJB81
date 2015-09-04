import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Giant extends Enemy{
	public Giant(double x, double y){
		Image giant = new Image(getClass().getClassLoader().getResourceAsStream("DC_-_Fighter_Sprite.gif"));
        avatar = new ImageView(giant);
        avatar.setX(x);
        avatar.setY(y);

		velocity = .6;
		
		health = Math.random() * 100;
		longRangeAttackStrength = Math.random() * 5;
		shortRangeAttackStrength = 50;
		killScore = 20;
	}
}
