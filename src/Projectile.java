import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile {
	private double power;
	private double velocity;
	private double[] direction;
	protected ImageView proj;
	
	public Projectile(Car car, Side s, double initialX, double initialY, double velocity, double power){
		this.velocity = velocity;
		this.power = power;
		direction = new double[]{ 0, -1 };
		
		
		Image bullet = new Image(getClass().getClassLoader().getResourceAsStream("bullet.jpg"));
        
        
        if(s.equals(Side.BAD)){
        	bullet = new Image(getClass().getClassLoader().getResourceAsStream("enemyProjectile.bmp"));
			direction[0] = car.carGraphic.getX() + 50 - initialX;
			direction[1] = car.carGraphic.getY() - initialY;
		}
        
        proj = new ImageView(bullet);
        proj.setX(initialX);
        proj.setY(initialY);
	}
	
	public void move(){
		proj.setX(proj.getX() + direction[0] * velocity);
		proj.setY(proj.getY() + direction[1] * velocity);
	}
	
	public double getDamage(){
		return this.power;
	}

}
