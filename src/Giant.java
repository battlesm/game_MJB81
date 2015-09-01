import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Giant extends Enemy{
	public Giant(int x, int y){
		avatar = new Rectangle(x, y, 100, 100);
		avatar.setFill(Color.RED);
		lrAttack = new Rectangle(20, 20);
		lrAttack.setFill(Color.GRAY);
		
		health = Math.random() * 100;
		lrAttackStrength = Math.random() * 50;
		srAttackStrength = 50;
	}
}
