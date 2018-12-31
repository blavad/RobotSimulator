package core;

import java.awt.Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Obstacle extends ObjetPlateau{

	private int width, height;
	
	public Obstacle(int x, int y, int w, int h) {
		super(x,y,w,h);
		this.width = w;
		this.height = h;
	}
	
	public void draw(GraphicsContext g) {
		//g.drawImage(new Image(getClass().getResource("/obstacle.png").toExternalForm()), (int)pos.x, (int)pos.y, width, height);
		g.setFill(Color.BLACK);
		g.fillRect((int)pos.x, (int)pos.y, width, height);
	}
	
	public int getWidth() { 
		return width;
	}
	public int getHeight() { 
		return height;
	}
}
