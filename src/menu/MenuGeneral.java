package menu;

import core.TypeSimu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.Debug;

public class MenuGeneral {

	private Button button_training, button_simul, button_test;
	
	public MenuGeneral(Stage primaryStage) {
		primaryStage.setTitle("Robot Simulator Menu");
		primaryStage.setResizable(false);
	
		// On cree les objets de base de notre menu
		
		// Bouton Q-learning
		button_training = new Button();
		button_training.setText("Q-learning");
		button_training.setPrefSize(200, 60);
	
		button_training.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Debug.log.println("#> Lancement entrainement Q-learning");
				new TrainingWindow(TypeSimu.QLEARNIG);
			}
		});
	
		// Bonton Genetic algo
		button_simul = new Button();
		button_simul.setText("Genetic Algorithm");
		button_simul.setPrefSize(200, 60);
		button_simul.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Debug.log.println("#> Lancement entrainement algo genetique");
				new TrainingWindow(TypeSimu.GENETIC);
			
			}
		});
		
		// Bonton Genetic algo
		button_test = new Button();
		button_test.setText("Test IA");
		button_test.setPrefSize(200, 60);
		button_test.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Debug.log.println("#> Lancement test IA");
				new Fenetre();
			}
		});
	
		// On cree et ajoute les elements de la fenetre au layout
		VBox menu_layout = new VBox();
		menu_layout.setSpacing(40);
		menu_layout.setPadding(new Insets(40));
		menu_layout.getChildren().add(button_training);
		menu_layout.getChildren().add(button_simul);
		menu_layout.getChildren().add(button_test);
	
		// On cree la scene et l'ajoute a la fenetre
	    Scene scene = new Scene(menu_layout);
	    scene.getStylesheets().add(getClass().getResource("/style_menu.css").toExternalForm());
	
	    primaryStage.setScene(scene);
		primaryStage.show();
	}
}
