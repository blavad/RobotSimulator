package core;

import javafx.scene.canvas.GraphicsContext;
import tools.Outils;

public class Plateau {

	private int width;
	private int height;
	private EnsembleObjetP obstacles;
	private EnsembleObjetP objectifs;
	
	public Plateau() {
		this.width = 800;
		this.height = 500;
		this.obstacles = new EnsembleObjetP();
		this.objectifs = new EnsembleObjetP();		
		
		initRandomPlateau();
	}
	
	public void initPlateau() {
		// On place les obstacles
		int epaisseur = 20;
		obstacles.add(new Obstacle(0,0,width,epaisseur));
		obstacles.add(new Obstacle(0,height - epaisseur,width,epaisseur));
		obstacles.add(new Obstacle(0,0,epaisseur,height));
		obstacles.add(new Obstacle(width - epaisseur,0,epaisseur,height));
		
		obstacles.add(new Obstacle(0,100,200,epaisseur));
		obstacles.add(new Obstacle(300,350,150,epaisseur));
		obstacles.add(new Obstacle(100,200,400,epaisseur));
		
		obstacles.add(new Obstacle(200,350,epaisseur,100));
		

		obstacles.add(new Obstacle(500,100,epaisseur,200));
		obstacles.add(new Obstacle(200,350,epaisseur,100));
		
		// On place les objectifs
		objectifs.add(new Objectif(100,150));
		objectifs.add(new Objectif(100,400));
		objectifs.add(new Objectif(400,150));
		objectifs.add(new Objectif(500,400));
		objectifs.add(new Objectif(700,100));
		objectifs.add(new Objectif(300,300));
		objectifs.add(new Objectif(600,200));
		objectifs.add(new Objectif(150,50));
		objectifs.add(new Objectif(200,200));
		objectifs.add(new Objectif(500,300));
		objectifs.add(new Objectif(600,300));
		
	}
	
	public void initRandomPlateau() {
		int epaisseur = 20;
		obstacles.add(new Obstacle(0,0,width,epaisseur));
		obstacles.add(new Obstacle(0,height - epaisseur,width,epaisseur));
		obstacles.add(new Obstacle(0,0,epaisseur,height));
		obstacles.add(new Obstacle(width - epaisseur,0,epaisseur,height));
		
		int nb_mur = 0;//Outils.RAND.nextInt(4);
		int nb_obj = 15;//Outils.RAND.nextInt(30)+3;
		
		for (int mur =0; mur <nb_mur; mur++) {
			if (Outils.RAND.nextBoolean())
				obstacles.add(new Obstacle(Outils.RAND.nextInt(this.width),Outils.RAND.nextInt(this.height),50+Outils.RAND.nextInt(this.width/4),epaisseur));
			else
				obstacles.add(new Obstacle(Outils.RAND.nextInt(this.width),Outils.RAND.nextInt(this.height),epaisseur,50+Outils.RAND.nextInt(this.height/4)));
		}
		
		for (int obj= 0; obj <nb_obj; obj++) {
			objectifs.add(new Objectif(Outils.RAND.nextInt(this.width-2*epaisseur-4*(new Objectif(0, 0)).getRayon())+2*(new Objectif(0, 0)).getRayon(),Outils.RAND.nextInt(this.height-2*epaisseur-4*(new Objectif(0, 0)).getRayon())+2*(new Objectif(0, 0)).getRayon()));
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
