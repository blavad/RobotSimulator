package menu;

import java.util.Optional;

import core.GeneticAlgoSimulation;
import core.IA;
import core.QAlgoSimulation;
import core.Simulation;
import core.TestIAAlgo;
import core.TypeSimu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.Debug;
import tools.Outils;

/** Fenetre de simulation
 * 
 * @author DHT
 *
 */
public class TrainingWindow extends Stage {

	/** La simulation liee a la fenetre */
	Simulation simulation;
	/** Le canva de dessin */
	Canvas canvas;
	/** Le slider de vitesse pour gerer la vitesse de la simulation */
	Slider speedSlider;
	Label speedValue, speedText;
	
	private Button buttonNext;
	
	public TrainingWindow(TypeSimu type_simul, IA ia) {

		String simuName = "";
		if (type_simul == TypeSimu.GENETIC || type_simul == TypeSimu.QLEARNIG) {
			
			
			TextInputDialog inDialog = new TextInputDialog("name");
			inDialog.setTitle("Simulation");
			//inDialog.setHeaderText("Simulation name");
			inDialog.setContentText("Name :");
			Optional<String> textIn = inDialog.showAndWait();
			//--- Get response value (traditional way)
			if (textIn.isPresent()) {
				simuName = textIn.get();
			}
			else simuName = "NameAuto";
		}
		
		this.setTitle(type_simul + " " + simuName);

        // On cree les objets de base de notre fenetre de simulation
        Group root = new Group();
        Scene scene = new Scene(root);
        this.setScene(scene);
        
        // On cree la zone de dessin (le canvas) et le graphics context pour dessiner dessus
        canvas = new Canvas(800, 500);
        GraphicsContext drawContext = canvas.getGraphicsContext2D();

		// On cree la simulation et on lui donne en parametre le canvas de dessin
        switch (type_simul) {
		case GENETIC:
        	this.simulation = new GeneticAlgoSimulation(drawContext, simuName);
			break;
		case QLEARNIG:
        	this.simulation = new QAlgoSimulation(drawContext, simuName);
			break;
		case TESTIA:
        	this.simulation = new TestIAAlgo(drawContext, ia);
			break;
		default:
        	this.simulation = new GeneticAlgoSimulation(drawContext, simuName);
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
	        	  simulation.saveIA();
	        	  simulation.stop();
	          }
		});    
    
	}
}
