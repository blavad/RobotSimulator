package capteur;

import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import core.EnsembleObjetP;
import core.Objectif;
import core.ObjetPlateau;
import core.Robot;
import javafx.scene.canvas.GraphicsContext;
import tools.Debug;

/** Classe de capteur d'objet
 * 
 * @author DHT
 *
 */
public class Capteur {
	
	/** Valeur par default du capteur */
	public static final double DEFAULT_VAL = 800;
	/** Robot d'appartenance du capteur */
	private Robot robot;
	/** Angle relatif du capteur (par rapport a l'angle du robot)*/
	private double angleRelatif;
	/** Distance percu par le capteur */
	private double distance;
	
	public Capteur(Robot robot, double angleRelatif) {
		this.robot = robot;
		this.angleRelatif = angleRelatif;
	}
	
	/** Mets a jour le capteur selon les objetx potentiellement captables
	 * 
	 * @param obP l'ensemble d'objet potentiellement captes
	 */
	public void update(EnsembleObjetP obP) {
		
		double dist = DEFAULT_VAL;
		
		// Recuperation de l'angle du capteur
		double angle = robot.getAngle()+ angleRelatif;
		double facteur_cos_angle = 1./Math.cos(angle), facteur_sin_angle = 1./Math.sin(angle);
		for (ObjetPlateau o : obP.getObPX()) {
			if (o instanceof Objectif && ((Objectif)o).isActive(robot.getLabel())) {
				continue;
			}
			double dx = o.getPos().x - robot.getPos().x;
			double dy = o.getPos().y- robot.getPos().y;
			// Rajustement selon la position des obstacles par rapport au robot
			if (dx<0) dx+=o.getDim().x;
			if (dy<0) dy+=o.getDim().y;
			double r1 = dx*facteur_cos_angle, r2 = dy*facteur_sin_angle;
			if (r1>=0) {
				double dy_capt = r1*Math.sin(angle);
				if (r1<dist && robot.getPos().y+dy_capt>o.getPos().y &&  robot.getPos().y+dy_capt<o.getPos().y+o.getDim().y)
					dist = r1;
			}
			if (r2>=0) {
				double dx_capt = r2*Math.cos(angle);
				if (r2<dist && robot.getPos().x+dx_capt>o.getPos().x &&  robot.getPos().x+dx_capt<o.getPos().x+o.getDim().x)
					dist = r2;
			}
		}
		this.distance = dist;
	}

	/** Getter de la valeur du capteur
	 * 
	 * @return la valeur du capteur
	 */
	public double getValue(){
		return this.distance;
	}

	/** Dessine le capteur si la distance n'est pas celle par default
	 *  
	 * @param g le canva de dessin
	 */
	public void draw(GraphicsContext g) {
		double x_inter, y_inter;
		x_inter = robot.getPos().x + distance*Math.cos(robot.getAngle()+angleRelatif);
		y_inter = robot.getPos().y + distance*Math.sin(robot.getAngle()+angleRelatif);
		//Color c =(distance==DEFAULT_VAL)? Color.AQUA : Color.DARKRED;
		//g.setStroke(c);
		if (distance != DEFAULT_VAL) {
			g.strokeLine(robot.getPos().x,robot.getPos().y, x_inter, y_inter);
		}
	}
	
}
