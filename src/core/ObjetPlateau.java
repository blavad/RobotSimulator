package core;

import javafx.scene.canvas.GraphicsContext;

import tools.*;

public abstract class ObjetPlateau {
	protected Vect2 pos, dim;
	
	public ObjetPlateau(int x, int y, int w, int h) {
		this.pos = new Vect2(x,y);
		this.dim = new Vect2(w,h);
	}

	public abstract void draw(GraphicsContext g);
	
	public Vect2 getPos() {
		return this.pos;
	}
	
	public Vect2 getDim() {
		return this.dim;
	}
}
