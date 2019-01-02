package core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Objectif extends ObjetPlateau{
	
	private int rayon = 10;
	public boolean[] activate;
	
	
	public Objectif(int x, int y) {
		super(x,y, 10, 10);
	}
	
	public void draw(GraphicsContext g) {
		for (int i = 0; i < activate.length;i++) {
			if (!isActive(i)) {
				g.setFill(new Color(0., 1., 0., 1.));
				g.fillOval((int)pos.x - rayon, (int)pos.y - rayon, rayon*2, rayon*2);
			}
		}
	}
	
	public void init(int taille_population) {
		this.activate = new boolean[taille_population];
		for (int i =0; i< taille_population; i++) {
			this.activate[i] = false;
		}
	}
	
	public int getRayon() {
		 return this.rayon;
	}
	public boolean isActive(int i) {
		return this.activate[i];
	}
	public void activate(int i) {
		this.activate[i] = true;
	}
	
	public void deactivate(int i) {
		this.activate[i] = false;
	}
	
	public void deactivateAll() {
		for (int i = 0; i < activate.length;i++)
			deactivate(i);
	}
}
