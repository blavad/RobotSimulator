package core;

import java.io.Serializable;

import capteur.EnsembleDeCapteurs;
import tools.Debug;
import tools.Matrix;
import tools.Outils;

/*
 * IA du robot utilisant la methode de Q-learning
 */
public class QBrain extends IA implements Serializable {

	/**
	 * Matrice Q (Quality)
	 */
	private Matrix Q;
	
	/**
	 * Importance sur le long terme
	 */
	private double lambda = 1.;
	
	/**
	 * Part de d'exploration (prise d'action aleatoire )
	 */
	private double eps = 0.99;

	/** Constructeur
	 * 
	 * @param nb_capt le nombre de capteurs
	 * @param nb_act le nombre d'actions possibles
	 * @param nb_state_per_capt le nombre d'etat possible par capteur
	 */
	public QBrain(int nb_capt, int nb_act, int nb_state_per_capt) {
		this.Q = new Matrix((int)Math.pow(nb_state_per_capt, nb_capt), nb_act);
		this.Q.nulle();
		Debug.log.println(this);
	}

	@Override
	public int output(EnsembleDeCapteurs... listCapteur) {
		int s = 0;
		int fact = 1;
		for (EnsembleDeCapteurs lcap : listCapteur) {
			for (int i = 0;  i<lcap.getSize();i++) {
				double val = lcap.getCapteur(i).getValue();
				int s_capt =0;
				if (val < 80)
					s_capt =0;
				else if (val < 200)
					s_capt =1;
				else 
					s_capt =2;
				s+=fact*s_capt;
				fact*=QRobot.NUM_STATE_PER_CAPT;
			}
		}
		if (Math.random()<eps)
			return Outils.RAND.nextInt(Q.getNbCol());
		else
			return new Matrix(Q.copy()[s]).argmax();
	}
	
	/** Mets a jour la matrice Q 
	 * 
	 * @param s_init l'etat initial
	 * @param act l'action prise
	 * @param s_final l'etat final
	 * @param rec la recompense
	 */
	public void update(int s_init, int act, int s_final, double rec) {
		double value;
		int act2 = 0;
		// Mise a jour de la matrcie Q
		Q.get(s_final, new Matrix(Q.copy()[s_final]).argmax());
		value = (1-QAlgoSimulation.LEARNING_RATE)*Q.get(s_init, act) + QAlgoSimulation.LEARNING_RATE * (rec+ lambda*Q.get(s_final, act2));
		Q.set(s_init, act, value);
		lambda *= 0.999;
		eps*=.9999;
	}
	

	public String toString(){
		return "Q Matrix (" + Q.getNbLin() +"x"+Q.getNbCol()+ ")";
	}

}
