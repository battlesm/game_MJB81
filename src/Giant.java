import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Giant extends Enemy{
	private double Health;
	private double Attack;
	public Rectangle giantGif;
	
	public Giant(int x, int y){
		Health = Math.random() * 100;
		Attack = Math.random() * 50;
		giantGif = new Rectangle(x, y, 100, 100);
		giantGif.setFill(Color.RED);
	}
	
	@Override
	public Rectangle Attack(){
		Rectangle attack = new Rectangle(20, 20);
		attack.setFill(Color.GRAY);
		return attack;
	}

}
