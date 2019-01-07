package core;

import java.util.Collections;

import capteur.Capteur;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tools.Debug;
import tools.Outils;
import tools.Vect2;

public class GeneticAlgoSimulation extends Simulation {

	/** La population d'individus du modele genetique */
	private Population population;

	/** Generation en cours */
	private int generation = 1;

	
	public GeneticAlgoSimulation(GraphicsContext drawContext, String name) {
		super(drawContext, name);
		plateau.initObjectifsPerso(Population.STD_SIZE);
		population = new Population(plateau, Population.STD_SIZE, 0.03);
	}
	
	@Override
	public void handle(long currentTime) {
		super.handle(currentTime);

        // On met a jour la population
        population.update(speed*interpolation);
        checkCollision();
        
        // On clear le canvas pour pouvoir redessiner dessus
        drawContext.clearRect(0, 0, plateau.getWidth(), plateau.getHeight());

        // On dessine le jeu en cours sur le canvas
        plateau.draw(drawContext);
        
        population.draw(drawContext);
        
        // On dessine les informations
        drawInfo();
        
        // On cree le nouveau plateau et la nouvelle population 
        if (population.allDead() || (speed*(lastTime-firstTime)/1.e9) > DUREE_SIMUL) {
        	// Sauvegarde les donnees dans un fichier texte
        	Outils.saveGResults(population.getRobots(), "donnees/" + name + ".txt", plateau.getObjectifs().getObPX().size());
        	
        	// On creer un nouveay plateau
        	if (generation%4==0) {
        		this.plateau = new Plateau();
        	}
        	this.plateau.initObjectifsPerso(Population.STD_SIZE);
        	this.population = population.nextGeneration(this.plateau);
    		generation++;
    		if (isFinished()) {
            	Debug.log.println("#> Fin de la simulation");
            	this.saveIA();
            	this.stop();
            }
    		firstTime = System.nanoTime();
        }
	}
	
	@SuppressWarnings("unchecked")
	public void saveIA() {
		// Tri la population par ordre decroissant de score
		Collections.sort(population.getRobots());
		Outils.saveGBrain((GeneticBrain)population.getRobot(0).getBrain(), "res/ia/genetic/" + name);
    	Debug.log.println("#> Sauvegarde de la meilleure IA reussie");
	}
	
	private void checkCollision() {
		// regarde les collisions entre le robot et les objectis 
		if (plateau.getObjectifs() != null) {
			for (ObjetPlateau ob : plateau.getObjectifs().getObPX()) {
				for (int rob = 0; rob < population.getSize(); rob++) {
					double d = Outils.norme2AB(((Objectif)ob).getPos(), population.getRobot(rob).getPos());
					if (d < Math.pow(population.getRobot(rob).getRayon() + ((Objectif)ob).getRayon(), 2)) {
						if (! ((Objectif)ob).isActive(rob)) {
							((Objectif)ob).activate(rob);
							population.getRobot(rob).addScore((int)(DUREE_SIMUL-(speed*(lastTime-firstTime)/1.e9)));
							population.getRobot(rob).addFoundObj();
						}
					}
				}
			}
		}
		// regarde les collisions entre le robot et les obstacles 
		if (plateau.getObstacles() != null) {
			for (ObjetPlateau ob : plateau.getObstacles().getObPX()) {
				for (int rob = 0; rob < population.getSize(); rob++) {
					if (intersects(population.getRobot(rob), (Obstacle)ob)) {
						if (!population.getRobot(rob).isDead()) {
							population.getRobot(rob).dead();
							population.getRobot(rob).addScore(-(int)(DUREE_SIMUL-(speed*(lastTime-firstTime)/1.e9)));
						}
					}
				}
			}
		}
		
		
	}
	
	
	private void drawInfo() {
		 drawContext.setFill(Color.WHITE);
		 drawContext.setFill(Color.WHITE);
		 drawContext.setFont(new Font("SansSerif", 20));
		 drawContext.fillText("Generation "+ generation , 0.80*plateau.getWidth(), 0.99*plateau.getHeight());
		 
		 float[] res1 = obRecup();
		 String res = "Objectifs recuperes \n  ";
		 for (int i = 0; i <= plateau.getObjectifs().getObPX().size(); i++) {
			 res += i + " : " + (100 * res1[i] / population.getSize()) + "%\n  ";
		 }
		 drawContext.setFill(Color.BLACK);
		 drawContext.setFont(new Font("SansSerif", 12));
		 drawContext.fillText(res , 0.83*plateau.getWidth(), 0.08*plateau.getHeight());
	}
	
	private float[] obRecup() {
		float[] res = new float[plateau.getObjectifs().getObPX().size() + 1];
		for (Robot r : population.getRobots()) {
			res[r.getNbFoundObj()] += 1;
		}
		return res;
	}

	@Override
	public boolean isFinished() {
		return generation>MAX_EPISODE;
	}
	
	public int getGeneration() { return this.generation; }
	

}
