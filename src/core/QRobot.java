package core;

import java.util.ArrayList;

import action.Avancer;
import action.Executable;
import action.Pivoter;
import capteur.Capteur;
import capteur.EnsembleDeCapteurs;
import javafx.scene.canvas.GraphicsContext;

/** Classe du robot utilisant la methode du Q-learning
 * 
 * @author DHT
 *
 */
public class QRobot extends Robot {
	
	/** Nombre d'etat pas capteur (Proche, Moyennement eloigne et eloigne)*/
	public static final int NUM_STATE_PER_CAPT = 3;
	
	/** Etat du robot (=etat global des capteurs) */
	int state =0;

	/**
	 * Vrai si le robot est en train d'executer une action (= changement d'etat en cours)
	 */
	private boolean enAction = false;
	
	public QRobot(int label) {
		super(label);
	}

	public QRobot(Plateau plateau, int label) {
		this(label);
		this.capObjectifs = new EnsembleDeCapteurs(this, plateau.getObjectifs(),0.,0.01,0.05,0.99,0.95,0.125,0.875,0.25,0.75);
		this.capObstacles = new EnsembleDeCapteurs(this, plateau.getObstacles(),0.,0.125,0.875);
		initActions();
		this.brain = new QBrain((this.capObjectifs.getSize() + this.capObstacles.getSize()),this.actions.size(),NUM_STATE_PER_CAPT);
	}
	
	public QRobot(Plateau plateau, IA brain, int label) {
		this(plateau, label);
		this.brain = brain;
	}
	
	/**
	 * Initialise les actions du robot
	 */
	private void initActions() {
		this.actions = new ArrayList<Executable>();
		actions.add(new Avancer(this, 10));
		actions.add(new Pivoter(this, -6.));
		actions.add(new Pivoter(this, 6.));
	}

	/** Mets a jour les parametres du robot
	 * 
	 * @param dt
	 * @param action l'action a executer
	 */
	public void update(double dt,int action) {
		// Recupere les informations provenant des capteurs
		capObjectifs.update();
		capObstacles.update();
		
		// Choisi l'action a execute en fonction des capteurs
		actions.get(action).execute(dt);
	}
	
	public void draw(GraphicsContext g) {
		super.draw(g);
	}
	
	/** Recupere l'etat du robot, celui-ci est compris entre 0 et NUM_STATE_PER_CAPT^nb_capteur
	 * 
	 * @return l'etat du robot
	 */
	public int getState() {
		int log =0;
		int s = 0;
		int fact = 1, fact_log = 1;
		for (int i = 0;  i< capObjectifs.getSize();i++) {
			double val = capObjectifs.getCapteur(i).getValue();
			int s_capt =0;
			if (val < 80)
				s_capt =0;
			else if (val < 200)
				s_capt =1;
			else 
				s_capt =2;
			s+=fact*s_capt;
			log+=fact_log*s_capt;
			fact_log*=10;
			fact*=NUM_STATE_PER_CAPT;
		}
		for (int i = 0;  i< capObstacles.getSize();i++) {
			double val = capObstacles.getCapteur(i).getValue();
			int s_capt =0;
			if (val < 80)
				s_capt =0;
			else if (val < 200)
				s_capt =1;
			else 
				s_capt =2;
			s+=fact*s_capt;
			log+=fact_log*s_capt;
			fact_log*=10;
			fact*=NUM_STATE_PER_CAPT;
		}	
		//Debug.log.println(log);
		return s;
	}

	public boolean isEnAction() {
		return this.enAction;
	}
	
	/**
	 * 
	 * @return la fonction de recompense percu par le robot
	 */
	public double getDistanceScore() {
		// rate représente l'importance de chaque partie
		double rate = .5;
		return 1.*(rate*Math.pow((Capteur.DEFAULT_VAL-this.capObjectifs.getCapteur(0).getValue()),2)-(1-rate)*(Math.pow(Capteur.DEFAULT_VAL-this.capObstacles.getCapteur(0).getValue(),2)));
	}

	/** Met le robot dans l'etat ACTION_EN_COURS ou desactive cet etat
	 * 
	 * @param b 
	 */
	public void setEnAction(boolean b) {
		this.enAction = b;
	}

}
