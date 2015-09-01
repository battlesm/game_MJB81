import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Enemy {
	private int Health;
	
	public Rectangle Attack(){
		return null;
	}
	
	public void wasShot(){
		Health -= 10;
	}
	
	public void wasHit(){
		Health -= 50;
	}

}
