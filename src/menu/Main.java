package menu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.Debug;

/** Classe principale lancant l'application javaFX avec le menu principal
 * 
 * @author DHT
 *
 */
public class Main extends Application {

	/** Les boutons du menu */
	Button button_training, button_simul;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Robot Simulator Menu");
		primaryStage.setResizable(false);
		
		// Creation du bouton pour l'entrainement par l'algorithme de Q-learning
		button_training = new Button();
		button_training.setText("Q-learning");
		button_training.setPrefSize(200, 60);
		button_training.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Debug.log.println("#> Lancement entrainement Q-learning");
				TrainingWindow training_window = new TrainingWindow("Q-Learning");
			}
		});
		
		// Creation du bouton pour l'entrainement par l'algorithme genetique
		button_simul = new Button();
		button_simul.setText("Genetic Algorithm");
		button_simul.setPrefSize(200, 60);
		button_simul.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Debug.log.println("#> Lancement entrainement algo genetique");
				TrainingWindow training_window = new TrainingWindow("Genetic Algorithm");
			
			}
		});
		
		// On cree et ajoute les elements de la fenetre au layout
		VBox menu_layout = new VBox();
		menu_layout.setSpacing(40);
		menu_layout.setPadding(new Insets(40));
		menu_layout.getChildren().add(button_training);
		menu_layout.getChildren().add(button_simul);
		
		// On cree la scene et l'ajoute a la fenetre
        Scene scene = new Scene(menu_layout);
        // On applique un style a notre scene depuis une feuille de style .css
        scene.getStylesheets().add(getClass().getResource("/style_menu.css").toExternalForm());
		
        primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
