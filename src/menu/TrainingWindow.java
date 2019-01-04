package menu;

import core.GeneticAlgoSimulation;
import core.QAlgoSimulation;
import core.Simulation;
import core.TypeSimu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.Debug;

public class TrainingWindow extends Stage {

	/** */
	Simulation simulation;
	
	Canvas canvas;
	Slider speedSlider;
	Label speedValue, speedText;
	
	public TrainingWindow(TypeSimu type_simul) {
		this.setTitle(type_simul+" Simulation");

        // On cree les objets de base de notre fenêtre de simulation
        Group root = new Group();
        Scene scene = new Scene(root);
        this.setScene(scene);
        
        // On cree la zone de dessin (le canvas) et le graphics context pour dessiner dessus
        canvas = new Canvas(800, 500);
        GraphicsContext drawContext = canvas.getGraphicsContext2D();

		// On cree la simulation et on lui donne en parametre le canvas de dessin
        switch (type_simul) {
		case GENETIC:
        	this.simulation = new GeneticAlgoSimulation(drawContext);
			break;
		case QLEARNIG:
        	this.simulation = new QAlgoSimulation(drawContext);
			break;
		default:
        	this.simulation = new GeneticAlgoSimulation(drawContext);
			break;
        
        }
        
        // On cree le slider de vitesse
        speedText = new Label("Speed");
        speedText.setTextFill(Color.ANTIQUEWHITE);
        speedSlider = new Slider(Simulation.SPEED_MIN,Simulation.SPEED_MAX,Simulation.SPEED_INIT);
        speedValue = new Label("x"+ Double.toString(simulation.getSpeed()));
        speedValue.setTextFill(Color.ANTIQUEWHITE);
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
        	@Override
        	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				simulation.setSpeed(newValue.doubleValue());
				speedValue.setText("x"+String.format("%.2f", newValue));
			}
		});
        
        // On dispose les elements du slider de vitesse
        HBox slider = new HBox();
		slider.setSpacing(20);
		slider.getChildren().add(speedText);
		slider.getChildren().add(speedSlider);
		slider.getChildren().add(speedValue);
       
		// On dispose le canvas et le slider sur la fenetre
        root.getChildren().add(canvas);
        root.getChildren().add(slider);
        
        // On lance la simulation (se lance dans un thread separe)
        simulation.start();
        
        // Finalement, on affiche notre simulation
        this.show();
        
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  Debug.log.println("#> Fin de la simulation");
	        	  simulation.stop();
	          }
		});    
    
	}
}
