package capteur;

import java.util.ArrayList;

import core.EnsembleObjetP;
import core.Robot;
import javafx.scene.canvas.GraphicsContext;
import tools.*;

public class EnsembleDeCapteurs {
	
	private Robot robot;
	private EnsembleObjetP eObjectP;
	private ArrayList<Capteur> capteurs;
	
	public EnsembleDeCapteurs(Robot robot, EnsembleObjetP eOb, double ... angle) {
		this.robot = robot;
		this.eObjectP = eOb;
		this.capteurs = new ArrayList<Capteur>();
		for (double a : angle) {
			capteurs.add(new Capteur(robot, Outils.DeuxPif*a));
		}
		
	}
	
	public void update() {
		for (Capteur cap: capteurs ) {
			cap.update(eObjectP);
		}
	}
	
	public void draw(GraphicsContext g) {
		for (Capteur cap : capteurs) {
			cap.draw(g);
		}
	}
	
	public Capteur getCapteur(int i){
		return capteurs.get(i);
	}
	
	public int getSize(){
		return capteurs.size();
	}

	public void setRobot(Robot robot2) {
		this.robot = robot2;
	}

}
