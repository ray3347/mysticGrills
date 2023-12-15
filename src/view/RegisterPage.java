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

public class RegisterPage extends Stage {
	private VBox vbox = new VBox();
	private Scene registerScene = new Scene(vbox, 500, 300);
	
	private TextField nameField = new TextField();
	private TextField emailField = new TextField();
	private PasswordField passwordField = new PasswordField();
	private PasswordField confirmPassField = new PasswordField();
	
	public RegisterPage() {
		this.setScene(createRegisterPage());
	}
	
	public Scene createRegisterPage() {
		GridPane registerForm = createRegisterForm();
		VBox.setMargin(registerForm, new Insets(20));
		vbox.getChildren().addAll(registerForm);
		
		return registerScene;
	}
	
	public GridPane createRegisterForm() {
		GridPane registerForm = new GridPane();
		registerForm.setVgap(20);
        registerForm.setHgap(10);
        
        Button registerButton = new Button("Register");
        Button backToLoginBtn = new Button("Have an Account? Login here");
        
        Label errMsg = new Label("");
        errMsg.setVisible(false);
        
        registerForm.add(new Label("Username: "), 0, 0);
        registerForm.add(nameField, 1, 0);
        registerForm.add(new Label("Email: "), 0, 1);
        registerForm.add(emailField, 1, 1);
        registerForm.add(new Label("Password [must contain more than 6 characters]: "), 0, 2);
        registerForm.add(passwordField, 1, 2);
        registerForm.add(new Label("Confirm Password: "), 0, 3);
        registerForm.add(confirmPassField, 1, 3);
        registerForm.add(registerButton, 1, 4);
        registerForm.add(backToLoginBtn, 1, 5);
        registerForm.add(errMsg, 1, 6);
        
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String isValid = validateForm();
				if(isValid.equals("")) {
					errMsg.setVisible(false);
					UserController userController = new UserController();
					boolean isUserCreated = userController.createUser("Customer", nameField.getText(), emailField.getText(), passwordField.getText());
					if(isUserCreated) {
						new LoginPage().show();
					}
				}else {
					errMsg.setText(isValid);
					errMsg.setVisible(true);
				}
				
			}
		});
        
        backToLoginBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new LoginPage().show();
			}
        	
		});
        
		return registerForm;
	}
	
	public String validateForm() {
		if(passwordField.getText().length() < 6) {
			return "Password must contain more than 6 characters";
		}
		if(!confirmPassField.getText().equals(passwordField.getText())) {
			return "Confirmation Password must be the same as Password";
		}	
		
		return "";
	}
}
