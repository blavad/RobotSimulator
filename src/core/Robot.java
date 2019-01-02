package core;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import action.*;
import tools.*;
import capteur.*;

/** Classe robot
 * 
 * @author DHT
 *
 */
public class Robot implements Comparable {
	
	protected EnsembleDeCapteurs capObjectifs;
	protected EnsembleDeCapteurs capObstacles;
	protected ArrayList<Executable> actions;
	protected IA brain;
	
	protected Vect2 pos;
	protected int rayon = 20;
	protected double angle;
	protected double score;
	protected boolean isDead = false;
	protected int label;

	public Robot(int label) {
		this.pos = new Vect2(50, 50);
		this.angle = 0;
		this.score = 0;
		this.label = label;
	}
	
	public Robot(int label, boolean geneticBrain, EnsembleDeCapteurs capObj, EnsembleDeCapteurs capObs) {
		this(label);
		this.capObjectifs = capObj;
		this.capObstacles = capObs;
		initActions();
		if (geneticBrain)
			this.brain = new GeneticBrain((this.capObjectifs.getSize() + this.capObstacles.getSize()),6,4,this.actions.size());
		else 
			this.brain = new QBrain((this.capObjectifs.getSize() + this.capObstacles.getSize()),this.actions.size(), 3);
	}
	
	public Robot(Plateau plateau, boolean geneticBrain, int label) {
		this(label);
		this.capObjectifs = new EnsembleDeCapteurs(this, plateau.getObjectifs(),0.,0.125,0.25,0.375,0.5,0.625,0.75,0.875);
		this.capObstacles = new EnsembleDeCapteurs(this, plateau.getObstacles(),0.,0.125,0.875);
		initActions();
		if (geneticBrain)
			this.brain = new GeneticBrain((this.capObjectifs.getSize() + this.capObstacles.getSize()),6,4,this.actions.size());
		else 
			this.brain = new QBrain((this.capObjectifs.getSize() + this.capObstacles.getSize()),this.actions.size(), 3);
	}
	
	public Robot(Plateau plateau, IA brain, int label) {
		this(label);
		this.capObjectifs = new EnsembleDeCapteurs(this, plateau.getObjectifs(),0.,0.125,0.25,0.375,0.5,0.625,0.75,0.875);
		this.capObstacles = new EnsembleDeCapteurs(this, plateau.getObstacles(),0.,0.125,0.875);
		initActions();
		this.brain = brain;
	}
	
	private void initActions() {
		this.actions = new ArrayList<Executable>();
		actions.add(new Avancer(this, 10));
		actions.add(new Pivoter(this, 5));
		actions.add(new Pivoter(this, -5));
	}

	public int update(double dt) {

		// Recupere les informations provenant des capteurs
		capObjectifs.update();
		capObstacles.update();
		
		// Choisi l'action a execute en fonction des capteurs
		int choix = brain.output(this.capObjectifs, this.capObstacles);
		actions.get(choix).execute(dt);
		
		return choix;
	}
	
	public void draw(GraphicsContext g) {
		g.setStroke(Color.RED);
		capObstacles.draw(g);
		
		
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
	
	@Override
	public int compareTo(Object compare) {
		return (int)(((Robot)compare).getScore() - getScore());
	}
	
}
