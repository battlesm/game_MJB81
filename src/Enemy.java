import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Enemy {
	protected Rectangle avatar;
	protected Rectangle lrAttack;
	
	protected double health;
	protected double srAttackStrength;
	protected double lrAttackStrength;
	
	public Rectangle getLRAttack(){
		return lrAttack;
	}
	
	public Rectangle getAvatar(){
		return avatar;
	}
	
	public double getLRAttackStrength(){
		return lrAttackStrength;
	}
	
	public double getSRAttackStrength(){
		return srAttackStrength;
	}
	
	public void wasShot(double shotStrength){
		health -= shotStrength;
	}
	
	public void wasHit(double hitImpact){
		health -= hitImpact;
	}

}
