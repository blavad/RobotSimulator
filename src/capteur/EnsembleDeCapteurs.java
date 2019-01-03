package capteur;

import java.util.ArrayList;

import core.EnsembleObjetP;
import core.Robot;
import javafx.scene.canvas.GraphicsContext;
import tools.*;

/** Classe plurielle gerant une liste de capteurs
 * 
 * @author DHT
 *
 */
public class EnsembleDeCapteurs {
	
	/** Le robot */
	private Robot robot;
	/** L'ensemble d'objet puvant etre captes */
	private EnsembleObjetP eObjectP;
	/** La liste des capteurs S*/
	private ArrayList<Capteur> capteurs;
	
	public EnsembleDeCapteurs(Robot robot, EnsembleObjetP eOb, double ... angle) {
		this.robot = robot;
		this.eObjectP = eOb;
		this.capteurs = new ArrayList<Capteur>();
		for (double a : angle) {
			capteurs.add(new Capteur(robot, Outils.DeuxPif*a));
		}
		
	}
	
	/**
	 * Mets a jour les capteurs
	 */
	public void update() {
		for (Capteur cap: capteurs ) {
			cap.update(eObjectP);
		}
	}
	
	/** Dessine les capteurs
	 * 
	 * @param g
	 */
	public void draw(GraphicsContext g) {
		for (Capteur cap : capteurs) {
			cap.draw(g);
		}
	}
	
	/** Recupere la valeur d'un capteur de la liste
	 * 
	 */
	public Capteur getCapteur(int i){
		return capteurs.get(i);
	}
	
	public int getSize(){
		return capteurs.size();
	}

	/** Associe un robot a l'ensemble de capteurs
	 * 
	 * @param robot2 le robot
	 */
	public void setRobot(Robot robot2) {
		this.robot = robot2;
	}

}
