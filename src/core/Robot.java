package core;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import action.*;
import tools.*;
import capteur.*;

/** Classe robot
 * 
 * @author DHT
 *
 */
public class Robot implements Comparable {
	
	/** Les capteurs d'objectif du robot */
	protected EnsembleDeCapteurs capObjectifs;
	/** Les capteurs d'obstacle du robot */
	protected EnsembleDeCapteurs capObstacles;
	/** Les actions executable par le robot */
	protected ArrayList<Executable> actions;
	/** Le cerveau du robot */
	protected IA brain;
	
	/** Position du robot sur la carte*/
	protected Vect2 pos;
	/** Rayon du robot */
	protected int rayon = 10;
	/** Angle du robot */
	protected double angle;
	/** Score du robot : fonction de recompense percu par celui-ci*/
	protected double score;
	/** Nombre d'objectif atteint*/
	protected int obj_found=0;
	/** Etat du robot (mort ou vivant)*/
	protected boolean isDead = false;
	/** Label associant le robot a son objectif */
	protected int label;
	
	public Robot(int label) {
		this.pos = new Vect2(50, 50);
		this.angle = 0;
		this.score = 0;
		this.label = label;
	}
	
	public Robot(Plateau plateau, IA brain, int label) {
		this(label);
		this.capObjectifs = new EnsembleDeCapteurs(this, plateau.getObjectifs(),0.,0.125,0.25,0.375,0.5,0.625,0.75,0.875);
		this.capObstacles = new EnsembleDeCapteurs(this, plateau.getObstacles(),0.,0.125,0.25,0.375,0.5,0.625,0.75,0.875);
		initActions();
		this.brain = brain;
	}
	
	private void initActions() {
		this.actions = new ArrayList<Executable>();
		actions.add(new Avancer(this, 10));
		actions.add(new Pivoter(this, 5));
		actions.add(new Pivoter(this, -5));
	}

	/** Fait prendre une decision au robot et mets a ses parametres a jour
	 * 
	 * @param dt l'interpolation liee a la boucle 
	 * @return l'action executee
	 */
	public int update(double dt) {

		// Recupere les informations provenant des capteurs
		capObjectifs.update();
		capObstacles.update();
		
		// Choisi l'action a execute en fonction des capteurs
		int choix = brain.output(this.capObjectifs, this.capObstacles);
		actions.get(choix).execute(dt);
		
		return choix;
	}
	
	/** Dessine le robot sur le canva de dessin
	 * 
	 * @param g le canva de dessin
	 */
	public void draw(GraphicsContext g) {
		/*g.setStroke(Color.RED);
		capObstacles.draw(g);*/
		
		
		g.setStroke(Color.GREEN);
		capObjectifs.draw(g);
		
		Color c = isDead() ? new Color(1., 0., 0., 1.) : new Color(0., 0., 1., 1.);
		g.setFill(c);
		g.fillOval(pos.x - rayon, pos.y - rayon, rayon*2, rayon*2);
		g.setFill(new Color(1., 1., 0., 0.9));
		g.fillOval(pos.x - rayon/2 + Math.floor(Math.cos(angle)* 1f/2f * rayon) , pos.y - rayon/2 + Math.floor(Math.sin(angle)* 1f/2f * rayon), rayon, rayon);
	}

	public void setPosition(double x, double y) {
		this.pos.x = x;
		this.pos.y = y;
	}

	public void dead() {this.isDead = true;}
	
	public void addScore(int s) {this.score += s;}
	
	public boolean isDead() {return this.isDead;}
	
	public void setAngle(double angle) {this.angle = angle;}

	public Vect2 getPos() {return this.pos;}
	
	public int getRayon() {return this.rayon;}
	
	public double getAngle() {return this.angle;}

	public double getScore() {return this.score;}
	
	public IA getBrain() {return this.brain;}
	
	public int getLabel() {return this.label;}
	
	public void addFoundObj() {
		this.obj_found++;
	}
	
	public int getNbFoundObj() {
		return this.obj_found;
	}
	
	@Override
	public int compareTo(Object compare) {
		return (int)(((Robot)compare).getScore() - getScore());
	}
	
}
