import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile {
	private double power;
	private double velocity;
	private double[] direction;
	protected ImageView proj;
	
	public Projectile(Car car, Main.Side s, double initialX, double initialY, double velocity, double power){
		this.velocity = velocity;
		this.power = power;
		direction = new double[]{ 0, -1 };
		
		
		Image bullet = new Image(getClass().getClassLoader().getResourceAsStream("bullet.jpg"));
        proj = new ImageView(bullet);
        
        if(s.equals(Main.Side.Bad)){
        	proj.setRotate(180);
			direction[0] = car.carGraphic.getX() - initialX;
			direction[1] = car.carGraphic.getY() - initialY;
		}
        
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
