package menu;

import javafx.application.Application;
import javafx.stage.Stage;

/** Classe principale lancant l'application javaFX avec le menu principal
 * 
 * @author DHT
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		new MenuGeneral(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
