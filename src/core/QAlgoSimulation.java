package core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tools.Debug;
import tools.Outils;
import tools.Vect2;

public class QAlgoSimulation extends Simulation {
	
	/**
	 * 
	 */
	public static final double LEARNING_RATE = 0.2;

	/**
	 * Duree de l'action en seconde
	 */
	private static final double DUREE_ACTION  = 0.05;
	
	/** 
	 * Le robot du modele de Q-learning
	 */
	private QRobot robot;
	
	/** Episode en cours */
	private int episode = 1;
	
	/** Recompense recue lors de l'action */
	private int dr =0;
	private int action_en_cours, state_init, state_final;
	private long actionTime;
	
	public QAlgoSimulation(GraphicsContext drawContext) {
		super(drawContext);
		plateau.initObjectifsPerso(1);
		robot = new QRobot(plateau, 0);
	}


	@Override
	public void handle(long currentTime) {
		super.handle(currentTime);
		
		// Si le robot a fini l'action precedente, on mets a jour son etat et on realise une autre action
		if (!robot.isEnAction()) {
			dr=0;
			state_init = robot.getState();
			// choix de l'action et execution de l'action
			action_en_cours = robot.update(speed*interpolation);
			robot.setEnAction(true);
			actionTime = System.nanoTime();
			//Debug.log.println("Action "+action_en_cours);
		}
		else {
			
			if ((speed*(currentTime - actionTime) / 1E9)>DUREE_ACTION) {
				dr += robot.getDistanceScore();
				state_final = robot.getState();
				// Mise a jour de la matrice Q
				((QBrain)robot.getBrain()).update(state_init, action_en_cours, state_final, dr);
				robot.setEnAction(false);
			}
			else {
				robot.update(speed*interpolation, action_en_cours);
			}
		}
		
		checkCollision();

		
        // On clear le canvas pour pouvoir redessiner dessus
        drawContext.clearRect(0, 0, plateau.getWidth(), plateau.getHeight());

        // On dessine le tout sur le canvas
        plateau.draw(drawContext);
        robot.draw(drawContext);
        drawNumEpisode();
        
        // On cree le nouveau plateau et le nouveau robot
        if (robot.isDead() || (speed*(lastTime-firstTime)/1.e9) > DUREE_SIMUL) {
        	Debug.log.println(robot.getScore());
        	if (episode%4==0) {
        		this.plateau = new Plateau();
        	}
        	this.plateau.initObjectifsPerso(1);
        	IA brain = this.robot.getBrain();
        	this.robot = new QRobot(plateau, brain, 0);
    		episode++;
    		if (isFinished()) {
            	Debug.log.println("#> Fin de la simulation");
            	this.stop();
            }
    		firstTime = System.nanoTime();
        }

	}

	/**
	 * Verifie si une collision s'est produite et mets a jour le score
	 */
	private void checkCollision() {

		int current_time = (int)(DUREE_SIMUL-(speed*(lastTime-firstTime)/1.e9));
		// regarde les collisions entre le robot et les objectis 
		if (plateau.getObjectifs() != null) {
			for (ObjetPlateau ob : plateau.getObjectifs().getObPX()) {
				double d = Outils.norme2AB(((Objectif)ob).getPos(), robot.getPos());
				if (d < Math.pow(robot.getRayon() + ((Objectif)ob).getRayon(), 2)) {
					if (! ((Objectif)ob).isActive(0)) {
						((Objectif)ob).activate(0);
						// Mesure la recompense
						dr+= 800*800*(2-current_time/DUREE_SIMUL);
						robot.addFoundObj();
					}
				}
			}
		}
	
		// regarde les collisions entre le robot et les obstacles 
		if (plateau.getObstacles() != null) {
			for (ObjetPlateau ob : plateau.getObstacles().getObPX()) {
				if (intersects(robot, (Obstacle)ob)) {
					if (!robot.isDead()) {
						robot.dead();
						dr-=10000*10000*(2-current_time/DUREE_SIMUL);
					}
				}
			}
		}
	}
	
	private boolean intersects(Robot circle, Obstacle rect) {
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
	
	private void drawNumEpisode() {
		 drawContext.setFill(Color.WHITE);
		 drawContext.fillRect(0.7*plateau.getWidth(), 0.9*plateau.getHeight(), 0.29*plateau.getWidth(), 0.1*plateau.getHeight());
		 drawContext.setFill(Color.BLUE);
		 drawContext.setFont(new Font("SansSerif", 30));
		 drawContext.fillText("Episode "+ episode , 0.7*plateau.getWidth(), 0.99*plateau.getHeight());
	}
	
	@Override
	public boolean isFinished() {
		return episode>MAX_EPISODE;
	}

}
