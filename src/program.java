import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import view.LoginPage;

public class program extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage app){
		// TODO Auto-generated method stub	
//		VBox vbox = new VBox(25);
		LoginPage lp = new LoginPage();
		Scene primary = lp.getScene();
		
		
		app.setTitle("Mystic Grills");
		app.setScene(primary);
		app.show();
		
	}
}
