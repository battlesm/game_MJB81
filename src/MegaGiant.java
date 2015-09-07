import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MegaGiant extends Enemy {
	public MegaGiant(double x, double y){
		Image warrior = new Image(getClass().getClassLoader().getResourceAsStream("mega_giant.jpg"));
        avatar = new ImageView(warrior);
        avatar.setX(x);
        avatar.setY(y);
        
		velocity = .005;
		
		health = 50 + Math.random() * 50;
		shortRangeAttackStrength = 100;
		killScore = 50;
	}
	@Override
	public void move(Car car){
		double[] direction = new double[2];
		direction[0] = car.carGraphic.getX() + 50 - avatar.getX();
		direction[1] = car.carGraphic.getY() - avatar.getY();
		avatar.setX(avatar.getX() + direction[0] * velocity);
		avatar.setY(avatar.getY() + direction[1] * velocity);
	}
}
