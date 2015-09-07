// This entire file is part of my masterpiece.
// MATTHEW BATTLES
import java.util.List;

import javax.swing.JOptionPane;

import javafx.scene.text.Text;

public class InteractionHandler {

	private static final double ENEMY_EDGE = 800;
	private static final String HEALTH_HEADER = "Health: ";

	public void handleCollision(List<Enemy> activeEnemies, Car myCar, Game myGame, Text myCarHealthText, boolean enemiesFrozen, boolean invincible) {
		if(!activeEnemies.isEmpty()){
    		for(int k = activeEnemies.size() - 1; k > -1; k--){
    			Enemy e = activeEnemies.get(k);
    			double doesFireWeapon = Math.random();
    			if(!enemiesFrozen){
    				e.move(myCar);
    			}
    			
    			if(doesFireWeapon > .99 && !enemiesFrozen){
    				myGame.fireEnemyWeapon(e);
    			}
    	        if (e.avatar.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())) {
    	        	if(!invincible){
    	        		changeHealth(myGame, myCar, myCarHealthText, -e.shortRangeAttackStrength);
    	        	}
    	        	
    	            myGame.killEnemy(e);
    	            break;
    	        }
    	        if(e.avatar.getY() >= ENEMY_EDGE){
    	        	myGame.removeEnemy(e);
    	        }
    		}
    	}
	}
	

	public void handleShot(List<Projectile> activeProjectiles, List<Enemy> activeEnemies, Car myCar, Game myGame, Text myCarHealthText, boolean invincible) {
		if(!activeProjectiles.isEmpty()){
			project:
    		for(int i = activeProjectiles.size() - 1; i > -1; i--){
    			Projectile p = activeProjectiles.get(i);
        		p.move();
        		
        		if(p.proj.getY() <= 0 || p.proj.getY() > 900){
        			myGame.removeProjectile(p);
        			break;
        		}
        		if(!activeEnemies.isEmpty()){
        			for(int j = activeEnemies.size() - 1; j > -1; j--){
            			Enemy e = activeEnemies.get(j);
            			if (p.proj.getBoundsInParent().intersects(e.avatar.getBoundsInParent())) {
            	            e.wasShot(p.getDamage());
            	            
            	            if(e.getHealth() <= 0){
            	            	myGame.killEnemy(e);   
            	            }
            	            
            	            myGame.removeProjectile(p);
            	            break project;
            	        }
            			if(p.proj.getBoundsInParent().intersects(myCar.carGraphic.getBoundsInParent())){
            				if(!invincible){
            					changeHealth(myGame, myCar, myCarHealthText, -p.getDamage());
            				}
            				
            				myGame.removeProjectile(p);
            				break project;
            			}
            		}
        		}
        	}
    	}
	}
	
	private void changeHealth(Game myGame, Car myCar, Text myCarHealthText, double points){
    	myCar.changeHealth(points);
    	myCarHealthText.setText(HEALTH_HEADER + myCar.getHealth());
    	if(myCar.getHealth() <= 0){
    		// End Game
    		if (JOptionPane.showConfirmDialog(null, "You Lose! Try Again?", "End Game", 0) == 0){
    			myGame.initializeLevel1();
    		}
    		else{
    			myGame.initializeMainScreen();
    		}
    	}
    }
}