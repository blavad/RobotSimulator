package core;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tools.*;

public abstract class Simulation extends AnimationTimer {

	/** Vitesse maximale de la simulation */
	public static final double SPEED_MAX = 15;
	
	/** Vitesse minimale de la simulation */
	public static final double SPEED_MIN = 0.05;
	
	/** Vitesse standard de la simulation */
	public static final double SPEED_INIT = 1.0;
	
	/** Duree de chaque simulation en secondes */
	public static final double DUREE_SIMUL = 50.;
	
	/** Nombre de generation maximale avant arret de la phase d'entrainement */
	public static final int MAX_EPISODE = 1000;
	
	/** Le contexte sur lequel on affiche les elements de decor*/
	protected GraphicsContext drawContext;
	
	/** Le plateau avec tous les obstacles et objectifs */
	protected Plateau plateau;
	
	/** Parametres temporaires  */
	protected double interpolation;
	protected long lastTime;
	protected long firstTime;
    
    /** Vitesse d'execution de la simulation */
	protected double speed;

	public Simulation(GraphicsContext drawContext) {
		this.drawContext = drawContext;
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
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double newSpeed) {
		this.speed = newSpeed;
	}

	public abstract boolean isFinished();
	
	
}
