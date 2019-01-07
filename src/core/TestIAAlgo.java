package core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tools.Debug;
import tools.Outils;

public class TestIAAlgo extends Simulation {
	/**
	 * Taux d'apprentissage
	 */
	public static final double LEARNING_RATE = 0.2;

	/**
	 * Duree de l'action en seconde
	 */
	private static final double DUREE_ACTION  = 0.05;
	
	/** 
	 * Le robot du modele de Q-learning
	 */
	private Robot robot;
	
	/** Episode en cours */
	private int episode = 1;
	
	/** Recompense recue lors de l'action */
	private int dr =0;
	private int action_en_cours, state_init, state_final;
	private long actionTime;
	
	public TestIAAlgo(GraphicsContext drawContext, String name, IA ia) {
		super(drawContext, name);
		plateau.initObjectifsPerso(1);
		if (ia instanceof QBrain) robot = new QRobot(plateau, (QBrain)ia, 0);
		else if (ia instanceof GeneticBrain) robot = new GeneticRobot(plateau, (GeneticBrain)ia, 0);
	}

	@Override
	public void handle(long currentTime) {
		super.handle(currentTime);
		
		
		robot.update(speed*interpolation);
		
		checkCollision();
		
        // On clear le canvas pour pouvoir redessiner dessus
        drawContext.clearRect(0, 0, plateau.getWidth(), plateau.getHeight());

        // On dessine le tout sur le canvas
        plateau.draw(drawContext);
        robot.draw(drawContext);
        drawNumEpisode();
        
        // On cree le nouveau plateau et le nouveau robot si l'episode est termine
        if (robot.isDead() || (speed*(lastTime-firstTime)/1.e9) > DUREE_SIMUL) {
        	Debug.log.println(robot.getNbFoundObj());
        	// Changement de plateau 
        	if (episode%4==0) {
        		this.plateau = new Plateau();
        	}
        	this.plateau.initObjectifsPerso(1);
        	// Recuperation de  l'IA precedent et creation du nouveau robot
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
	 * Verifie si une collision s'est produite et mets a jour le score en fonction
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
						robot.addFoundObj();
						// Mesure la recompense
						dr+= 800*800*(2-current_time/DUREE_SIMUL);
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
	
	public void saveIA() {
		Outils.saveQBrain((QBrain)robot.getBrain(), "res/ia/q/" + name);
    	Debug.log.println("#> Sauvegarde de la meilleure IA reussie");
	}

}
