package core;

import javafx.scene.canvas.GraphicsContext;
import tools.Outils;

/** Classe du plateau 
 * 
 * @author DHT
 *
 */
public class Plateau {

	private int width;
	private int height;
	/** Ensemble des obstacles du plateau */
	private EnsembleObjetP obstacles;
	/** Ensemble des objectifs du plateau */
	private EnsembleObjetP objectifs;
	
	public Plateau() {
		this.width = 800;
		this.height = 500;
		this.obstacles = new EnsembleObjetP();
		this.objectifs = new EnsembleObjetP();		
		
		// initialise le plateau préconstruit
		initPlateau();
		
		// initialise un plateau aléatoire
		//initRandomPlateau();
		
	}
	
	/** 
	 * Initialise un plateau de virages
	 */
	public void initPlateau() {
		// On place les obstacles
		int epaisseur = 20;
		int delta = 50;
		int espace = 150;
		obstacles.add(new Obstacle(0,0,width,epaisseur));
		obstacles.add(new Obstacle(0,height - epaisseur,width,epaisseur));
		obstacles.add(new Obstacle(0,0,epaisseur,height));
		obstacles.add(new Obstacle(width - epaisseur,0,epaisseur,height));
		
		obstacles.add(new Obstacle(0,100,width-espace,epaisseur));
		obstacles.add(new Obstacle(espace,200,width-espace,epaisseur));
		obstacles.add(new Obstacle(0,300,width-espace,epaisseur));
		obstacles.add(new Obstacle(espace,400,width-espace,epaisseur));
		
		// On place les objectifs
		
		objectifs.add(new Objectif(400,50));
		objectifs.add(new Objectif(150,50));
		objectifs.add(new Objectif(300,50));
		objectifs.add(new Objectif(50,50));
		objectifs.add(new Objectif(400,50));
		objectifs.add(new Objectif(600,50));
		objectifs.add(new Objectif(700,70));
		objectifs.add(new Objectif(730,100));
		objectifs.add(new Objectif(730,130));
		objectifs.add(new Objectif(100,150));
		objectifs.add(new Objectif(200,150));
		objectifs.add(new Objectif(400,150));
		objectifs.add(new Objectif(500,150));
		objectifs.add(new Objectif(600,150));
		objectifs.add(new Objectif(700,150));
		objectifs.add(new Objectif(70,200));
		objectifs.add(new Objectif(100,250));
		objectifs.add(new Objectif(500,250));
		objectifs.add(new Objectif(300,250));
		objectifs.add(new Objectif(600,250));
		objectifs.add(new Objectif(700,270));
		objectifs.add(new Objectif(730,300));
		objectifs.add(new Objectif(730,330));
		objectifs.add(new Objectif(100,350));
		objectifs.add(new Objectif(200,350));
		objectifs.add(new Objectif(700,350));
		objectifs.add(new Objectif(500,350));
		objectifs.add(new Objectif(300,350));
		objectifs.add(new Objectif(100,350));
		objectifs.add(new Objectif(70,400));
		objectifs.add(new Objectif(100,450));
		objectifs.add(new Objectif(500,450));
		objectifs.add(new Objectif(300,450));
		objectifs.add(new Objectif(600,450));
		
	}
	
	/**
	 * Initialise un plateau aléatoire
	 */
	public void initRandomPlateau() {
		int epaisseur = 20;
		obstacles.add(new Obstacle(0,0,width,epaisseur));
		obstacles.add(new Obstacle(0,height - epaisseur,width,epaisseur));
		obstacles.add(new Obstacle(0,0,epaisseur,height));
		obstacles.add(new Obstacle(width - epaisseur,0,epaisseur,height));
		
		int nb_mur = 4;//Outils.RAND.nextInt(4);
		int nb_obj = Outils.RAND.nextInt(42)+8;
		
		for (int mur =0; mur <nb_mur; mur++) {
			if (Outils.RAND.nextBoolean())
				obstacles.add(new Obstacle(Outils.RAND.nextInt(this.width),Outils.RAND.nextInt(this.height),50+Outils.RAND.nextInt(this.width/4),epaisseur));
			else
				obstacles.add(new Obstacle(Outils.RAND.nextInt(this.width),Outils.RAND.nextInt(this.height),epaisseur,50+Outils.RAND.nextInt(this.height/4)));
		}
		for (int obj= 0; obj < nb_obj; obj++) {
			objectifs.add(new Objectif(
					2 * epaisseur + 1 * Objectif.RAYON + Outils.RAND.nextInt(this.width - 4 * epaisseur - 2 * Objectif.RAYON) ,
					2 * epaisseur + 1 * Objectif.RAYON + Outils.RAND.nextInt(this.height- 4 * epaisseur - 2* Objectif.RAYON)));
		}
	}
	
	public void draw(GraphicsContext g) {
		for(ObjetPlateau ob : obstacles.getObPX()) {
			ob.draw(g);
		}
		
		for(ObjetPlateau ob : objectifs.getObPX()) {
			ob.draw(g);
		}
	}
	
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
	
	public EnsembleObjetP getObjectifs() {
		return this.objectifs;
	}
	public EnsembleObjetP getObstacles() {
		return this.obstacles;
	}

	public void initObjectifsPerso(int taille_population) {
		for (ObjetPlateau o : objectifs.getObPX()) {
			((Objectif)o).init(taille_population);
		}
	}
	
}
