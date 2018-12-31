package core;

import capteur.*;

/** Classe abstraite IA
 * 
 * @author DHT
 *
 */
public abstract class IA {
	
	/** Sortie de l'IA
	 * 
	 * @param listCapteur l'ensemble des capteurs
	 * @return le numero de l'action a executer
	 */
	public abstract int output(EnsembleDeCapteurs ... listCapteur);
	
}
