package core;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tools.*;

/** Classe abstraite gerant les aspects generaux de toutes simulations
 * 
 * @author DHT
 *
 */
public abstract class Simulation extends AnimationTimer {

	/** Vitesse maximale de la simulation */
	public static final double SPEED_MAX = 15;
	
	/** Vitesse minimale de la simulation */
	public static final double SPEED_MIN = 0.05;
	
	/** Vitesse standard de la simulation */
	public static final double SPEED_INIT = 1.0;
	
	/** Duree de chaque simulation en secondes */
	public static final double DUREE_SIMUL = 90.;
	
	/** Nombre de generation maximale avant arret de la phase d'entrainement */
	public static final int MAX_EPISODE = 10000;
	
	/** Le contexte sur lequel on affiche les elements de decor*/
	protected GraphicsContext drawContext;
	
	/** Le plateau avec tous les obstacles et objectifs */
	protected Plateau plateau;
	
	/** Nom de la simulation */
	protected String name;
	
	/** Parametres temporaires  */
	protected double interpolation;
	protected long lastTime;
	protected long firstTime;
    
    /** Vitesse d'execution de la simulation */
	protected double speed;

	public Simulation(GraphicsContext drawContext, String name) {
		this.drawContext = drawContext;
		this.name = name;
		plateau = new Plateau();
		this.speed = SPEED_INIT;
		
        this.lastTime = System.nanoTime();
        this.firstTime = System.nanoTime();
	}

	@Override
	public void handle(long currentTime) {
		 // On calcul l'interpolation (temps entre deux frames)
        interpolation = (float) ((currentTime - lastTime) / 1E9);
        lastTime = System.nanoTime();
	}
	
	protected boolean intersects(Robot circle, Obstacle rect) {
			Vect2 circleDistance = new Vect2();
		    circleDistance.x = Math.abs(circle.getPos().x - rect.getPos().x - (float)rect.getWidth()/2);
		    circleDistance.y = Math.abs(circle.getPos().y - rect.getPos().y - (float)rect.getHeight()/2);
		    
		    if (circleDistance.x > (rect.getWidth()/2 + circle.getRayon())) { return false; }
		    if (circleDistance.y > (rect.getHeight()/2 + circle.getRayon())) { return false; }
		 
		    if (circleDistance.x <= (rect.getWidth()/2)) { return true; }
		    if (circleDistance.y <= (rect.getHeight()/2)) { return true; }
		 
		    float cornerDistance_sq = (float) (Math.pow(circleDistance.x - rect.getWidth()/2, 2) + Math.pow(circleDistance.y - rect.getHeight()/2, 2));
		    return (cornerDistance_sq <= (circle.getRayon() * circle.getRayon()));
	}

	
	public String getName() { return this.name; }
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double newSpeed) {
		this.speed = newSpeed;
	}

	public abstract boolean isFinished();
	public abstract void saveIA();
	
}
