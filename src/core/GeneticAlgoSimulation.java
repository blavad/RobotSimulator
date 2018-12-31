package core;

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

	
	public GeneticAlgoSimulation(GraphicsContext drawContext) {
		super(drawContext);
		plateau.initObjectifsPerso(Population.STD_SIZE);
		population = new Population(plateau, Population.STD_SIZE, 0.15);
	}
	
	@Override
	public void handle(long currentTime) {
		super.handle(currentTime);

        // On met à jour la population
        population.update(speed*interpolation);
        checkCollision();
		
        // On clear le canvas pour pouvoir redessiner dessus
        drawContext.clearRect(0, 0, plateau.getWidth(), plateau.getHeight());

        // On dessine le jeu en cours sur le canvas
        plateau.draw(drawContext);
        
        population.draw(drawContext);
        
        // On dessine le numero de la generation
        drawNumGeneration();
        
        // On cree le nouveau plateau et la nouvelle population 
        if (population.allDead() || (speed*(lastTime-firstTime)/1.e9) > DUREE_SIMUL) {
        	if (generation%50==0) {
        		this.plateau = new Plateau();
        	}
        	this.plateau.initObjectifsPerso(Population.STD_SIZE);
        	this.population = population.nextGeneration(this.plateau);
    		generation++;
    		if (isFinished()) {
            	Debug.log.println("#> Fin de la simulation");
            	this.stop();
            }
    		firstTime = System.nanoTime();
        }
	}
	
	private void drawNumGeneration() {
		 drawContext.setFill(Color.WHITE);
		 drawContext.fillRect(0.7*plateau.getWidth(), 0.9*plateau.getHeight(), 0.29*plateau.getWidth(), 0.1*plateau.getHeight());
		 drawContext.setFill(Color.BLUE);
		 drawContext.setFont(new Font("SansSerif", 30));
		 drawContext.fillText("Generation "+ generation , 0.7*plateau.getWidth(), 0.99*plateau.getHeight());
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
							population.getRobot(rob).addScore(1);
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
							population.getRobot(rob).addScore(-1);
						}
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

	@Override
	public boolean isFinished() {
		return generation>MAX_EPISODE;
	}
	

}