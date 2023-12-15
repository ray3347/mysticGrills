package view;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class LoginPage extends Stage {
	private VBox vbox = new VBox();
	private Scene loginScene = new Scene(vbox, 500, 300);
	
	private TextField emailField = new TextField();
	private PasswordField passwordField = new PasswordField();
	
	public LoginPage() {
		this.setScene(createLoginPage());
	}
	
	public Scene createLoginPage() {
		GridPane loginForm = createLoginForm();
		VBox.setMargin(loginForm, new Insets(20));
		vbox.getChildren().addAll(loginForm);
		
		
		return loginScene;
	}
	
	public GridPane createLoginForm() {
		GridPane loginForm = new GridPane();
		loginForm.setVgap(20);
        loginForm.setHgap(10);
		
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register an Account");
        Label err = new Label("User Not Found, Please Check Your Login Credentials");
        err.setVisible(false);
        
        loginForm.add(new Label("Email: "), 0, 0);
        loginForm.add(emailField, 1, 0);
        loginForm.add(new Label("Password: "), 0, 1);
        loginForm.add(passwordField, 1, 1);
        loginForm.add(loginButton, 1, 2);
        loginForm.add(registerButton, 1, 3);
        loginForm.add(err, 0, 3);
        
        
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					UserController userController = new UserController();
					
					User u = userController.authenticateUser(emailField.getText(), passwordField.getText());
					if(u != null) {
						 err.setVisible(false);
						 if(u.getUserRole().equals("Customer")) {
							 new CustomerView(u).show();
						 }
						 else if(u.getUserRole().equals("Admin")) {
							 new AdminView(u).show();
						 }
						 else if(u.getUserRole().equals("Chef") || u.getUserRole().equals("Waiter")) {
							 new RestaurantOperationView(u).show();
						 }
						 else if(u.getUserRole().equals("Cashier")) {
							 new CashierView(u).show();
						 }
					}
					else {
						err.setVisible(true);
					}
				}
				catch(Exception ex) {
					throw ex;
				}
				
			}
		});
        
        registerButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					new RegisterPage().show();
				}
				catch(Exception ex) {
					throw ex;
				}
			}
        	
		});
		
		return loginForm;
	}
	
}
