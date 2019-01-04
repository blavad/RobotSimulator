package core;

import java.util.ArrayList;

import action.Avancer;
import action.Executable;
import action.Pivoter;
import capteur.EnsembleDeCapteurs;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GeneticRobot extends Robot {
	
	public GeneticRobot(int label) {
		super(label);
	}

	public GeneticRobot(Plateau plateau, int label) {
		this(label);
		initCapteurs(plateau);
		initActions();
		this.brain = new GeneticBrain((this.capObjectifs.getSize() + this.capObstacles.getSize()),6,4,this.actions.size());
	}
	
	public GeneticRobot(Plateau plateau, IA brain, int label) {
		this(plateau, label);
		this.brain = brain;
	}

	private void initActions() {
		this.actions = new ArrayList<Executable>();
		actions.add(new Avancer(this, 10));
		actions.add(new Pivoter(this, 8));
		actions.add(new Pivoter(this, -8));
	}
	
	public void draw(GraphicsContext g) {
		super.draw(g);
		g.setFill(Color.BROWN);
		g.setFont(new Font("SansSerif", 15));
		g.fillText(""+(int) score+"", pos.x - rayon , pos.y - rayon);
	}
	
}
