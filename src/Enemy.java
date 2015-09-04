import javafx.scene.image.ImageView;

public abstract class Enemy {
	protected ImageView avatar;
	
	protected double health;
	protected int killScore;
	protected double shortRangeAttackStrength;
	protected double longRangeAttackStrength;
	
	protected double velocity;
	
	public ImageView getAvatar(){
		return avatar;
	}
	
	public double getHealth(){
		return health;
	}
	
	public int getKillScore(){
		return killScore;
	}
	
	public double getLongRangeAttackStrength(){
		return longRangeAttackStrength;
	}
	
	public double getShortRangeAttackStrength(){
		return shortRangeAttackStrength;
	}
	
	public void move(Car car){
		avatar.setY(avatar.getY() + velocity);
	}
	
	public void wasShot(double d){
		health -= d;
	}
}
