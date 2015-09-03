import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Enemy {
	protected ImageView avatar;
	
	protected double health;
	protected double shortRangeAttackStrength;
	protected double longRangeAttackStrength;
	protected double velocity;
	
	public ImageView getAvatar(){
		return avatar;
	}
	
	public double getHealth(){
		return health;
	}
	
	public double getLongRangeAttackStrength(){
		return longRangeAttackStrength;
	}
	
	public double getShortRangeAttackStrength(){
		return shortRangeAttackStrength;
	}
	
	public void move(){
		avatar.setY(avatar.getY() + velocity);
	}
	
	public void wasShot(double d){
		health -= d;
	}
}
