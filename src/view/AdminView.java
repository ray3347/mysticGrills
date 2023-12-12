package view;

import java.util.ArrayList;

import com.mysql.cj.xdevapi.Table;

import controller.MenuItemController;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.MenuItem;
import model.User;

public class AdminView extends Stage {
	private User activeUser;
	
	private MenuBar menuBar;
	private VBox vbox = new VBox();
	private Scene adminScene = new Scene(vbox);
	
	// User Management Components
	private Label idLabel = new Label("");
	private Label nameLabel = new Label("");
	private Label emailLabel = new Label("");
	private Label roleLabel = new Label("");
	private TextField roleField = new TextField();
	Button changeRoleBtn = new Button("Change Role");
	Button deleteUser = new Button("Delete User");
	private User selectedUser;
	
	// Menu Management Components
	private Label menuIdLabel = new Label("");
	private TextField menuNameTextField = new TextField();
	private TextField menuDescriptionTextField = new TextField();
	private TextField menuPriceTextField = new TextField();
	Button addButton = new Button("Add Item");
	Button updateButton = new Button("Update Item");
	Button deleteButton = new Button("Delete Item");
	private MenuItem selectedItem;

	public AdminView(User u) {
		// TODO Auto-generated constructor stub
		this.activeUser = u;
		this.selectedUser = null;
		
		menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		javafx.scene.control.MenuItem userManagement = new javafx.scene.control.MenuItem("User Management Menu");
		javafx.scene.control.MenuItem menuItemManagement = new javafx.scene.control.MenuItem("Menu Item Management");
		
		userManagement.setOnAction(e ->{
			resetUserForm();
			showUserManagementScene();
		});
		
		menuItemManagement.setOnAction(e ->{
			resetMenuItemForm();
			showMenuItemManagementScene();
		});

		menu.getItems().addAll(userManagement, menuItemManagement);
		menuBar.getMenus().addAll(menu);
		
		showUserManagementScene();
		this.setScene(adminScene);
	}
	
	// User Management Module
	public void showUserManagementScene() {
		vbox.getChildren().clear();
		
		UserController userController = new UserController();
		
		TableView<User> userTable = createUserTable();
		ArrayList<User> allUsers = userController.getAllUsers();
		
		userTable.getItems().setAll(allUsers);
		
		selectUserTableEventListener(userTable);
		
		GridPane userForm = createUserForm();
		
		vbox.setMargin(userTable, new Insets(20));
		vbox.setMargin(userForm, new Insets(20));
		vbox.getChildren().addAll(menuBar, userTable, userForm);		
	}
	
	public TableView<User> createUserTable(){
		TableView<User> userTable = new TableView<User>();
		
		TableColumn<User, Integer> idColumn = new TableColumn<User, Integer>("User ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
		
		TableColumn<User, String> nameColumn = new TableColumn<User, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
		
		TableColumn<User, String> emailColumn = new TableColumn<User, String>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
		
		TableColumn<User, String> roleColumn = new TableColumn<User, String>("Role");
		roleColumn.setCellValueFactory(new PropertyValueFactory<>("userRole"));
		
		userTable.getColumns().addAll(idColumn, nameColumn, emailColumn, roleColumn);
		
		return userTable;
	}
	
	public void selectUserTableEventListener(TableView<User> table) {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				this.selectedUser = newSelection;
				idLabel.setText(""+selectedUser.getUserId());
				nameLabel.setText(selectedUser.getUserName());
				emailLabel.setText(selectedUser.getUserEmail());
				roleField.setText(selectedUser.getUserRole());	
				changeRoleBtn.setDisable(false);
				deleteUser.setDisable(false);
			}
		});
	}
	
	public GridPane createUserForm() {
		GridPane userForm = new GridPane();
		userForm.setVgap(20);
		userForm.setHgap(10);
		
		changeRoleBtn.setDisable(true);
		deleteUser.setDisable(true);
		
		userForm.add(new Label("User ID :"), 0, 0);
		userForm.add(idLabel, 1, 0);
		userForm.add(new Label("Name :"), 0, 1);
		userForm.add(nameLabel, 1, 1);
		userForm.add(new Label("Email :"), 0, 2);
		userForm.add(emailLabel, 1, 2);
		userForm.add(new Label("Role [Customer | Cashier | Admin | Waiter | Chef] [Case Sensitive]:"), 0, 3);
		userForm.add(roleField, 1, 3);
		userForm.add(changeRoleBtn, 1, 4);
		userForm.add(deleteUser, 1, 5);
		
		changeRoleBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean val = validateRole(roleField.getText());
				if(val) {
					try {
						UserController userController = new UserController();
						selectedUser.setUserRole(roleField.getText());
						boolean success = userController.updateUser(selectedUser);
						if(success) {
							showSuccessMessage("User Role Successfully Changed!").show();
							resetUserForm();
							showUserManagementScene();
						}
					}
					catch(Exception ex) {
						throw ex;
					}
				}
				else {
					
				}
			}
		});
		
		deleteUser.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					UserController userController = new UserController();
					boolean success = userController.deleteUser(selectedUser);
					if(success) {
						showSuccessMessage("User Successfully Deleted!").show();
						resetUserForm();
						showUserManagementScene();
					}
				}
				catch(Exception ex) {
					throw ex;
				}
			}
		});
		
		return userForm;
	}
	
	// Menu Item Management Module
	public void showMenuItemManagementScene() {
		vbox.getChildren().clear();
		
		Button addNew = new Button("Add New Menu Item");
		addNew.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				showNewMenuItemWindow().show();
			}
		});
		
		TableView<MenuItem> itemTable = createMenuItemTable();
		MenuItemController menuItemController = new MenuItemController();
		ArrayList<MenuItem> items = menuItemController.getAllMenuItems();
		itemTable.getItems().setAll(items);
		
		selectMenuItemTableEventListener(itemTable);
		
		GridPane itemForm = createMenuItemForm(false);
		
		vbox.setMargin(addNew, new Insets(20));
		vbox.setMargin(itemTable, new Insets(20));
		vbox.setMargin(itemForm, new Insets(20));
		vbox.getChildren().addAll(menuBar, addNew, itemTable, itemForm);
		
	}
	
	public TableView<MenuItem> createMenuItemTable(){
		TableView<MenuItem> itemTable = new TableView<MenuItem>();
		
		TableColumn<MenuItem, Integer> idColumn = new TableColumn<MenuItem, Integer>("Item ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, Integer>("menuItemId"));
		
		TableColumn<MenuItem, String> nameColumn = new TableColumn<MenuItem, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("menuItemName"));
		
		TableColumn<MenuItem, String> descColumn = new TableColumn<MenuItem, String>("Description");
		descColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("menuItemDescription"));
		
		TableColumn<MenuItem, Integer> priceColumn = new TableColumn<MenuItem, Integer>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, Integer>("menuItemPrice"));
		
		itemTable.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn);
		
		return itemTable;
	}
	
	public GridPane createMenuItemForm(boolean isAddFeature) {
		GridPane itemForm = new GridPane();
		itemForm.setVgap(20);
		itemForm.setHgap(10);
		
		
		Label err = new Label("Price must be greater than 0!");
		err.setVisible(false);
		
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
		
		itemForm.add(new Label("Item Name"), 0, 1);
		itemForm.add(menuNameTextField, 1, 1);
		itemForm.add(new Label("Description"), 0, 2);
		itemForm.add(menuDescriptionTextField, 1, 2);
		itemForm.add(new Label("Price"), 0, 3);
		itemForm.add(menuPriceTextField, 1, 3);
		if(isAddFeature) {
			itemForm.add(addButton, 1, 4);
			itemForm.add(err,1, 5);
		}else {
			itemForm.add(new Label("Item ID"), 0, 0);
			itemForm.add(menuIdLabel, 1, 0);
			itemForm.add(updateButton, 1, 4);
			itemForm.add(deleteButton, 1, 5);
			itemForm.add(err,1, 6);
		}
		
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean val = validateMenuItemForm();
				if(val) {
					err.setVisible(false);
					try {
						MenuItemController menuItemController = new MenuItemController();
						MenuItem newItem = new MenuItem(0, menuNameTextField.getText(), menuDescriptionTextField.getText(), Integer.parseInt(menuPriceTextField.getText()));
						boolean success = menuItemController.createMenuItem(newItem);
						if(success) {
							showSuccessMessage("New Item Successfully Added!").show();
							resetMenuItemForm();
							showMenuItemManagementScene();
						}
					}
					catch(Exception ex) {
						throw ex;
					}
					
				}
				else {
					err.setVisible(true);
				}
			}
		});
		
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean val = validateMenuItemForm();
				if(val) {
					err.setVisible(false);
					try {
						MenuItemController menuItemController = new MenuItemController();
						selectedItem.setMenuItemName(menuNameTextField.getText());
						selectedItem.setMenuItemDescription(menuDescriptionTextField.getText());
						selectedItem.setMenuItemPrice(Integer.parseInt(menuPriceTextField.getText()));
						
						boolean success = menuItemController.updateMenuItem(selectedItem);
						if(success) {
							showSuccessMessage("Item Data Successfully Updated!").show();
							resetMenuItemForm();
							showMenuItemManagementScene();
						}
					}
					catch(Exception ex) {
						throw ex;
					}
				}
				else {
					err.setVisible(true);
				}
			}
		});
		
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					MenuItemController menuItemController = new MenuItemController();
					boolean success = menuItemController.deleteMenuItem(selectedItem);
					if(success) {
						showSuccessMessage("Item Data Successfully Deleted!").show();
						resetMenuItemForm();
						showMenuItemManagementScene();
					}
				}
				catch(Exception ex) {
					throw ex;
				}
			}
		});
		
		return itemForm;
	}
	
	public void selectMenuItemTableEventListener(TableView<MenuItem> table) {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				this.selectedItem = newSelection;
				menuIdLabel.setText(""+selectedItem.getMenuItemId());
				menuNameTextField.setText(selectedItem.getMenuItemName());
				menuDescriptionTextField.setText(selectedItem.getMenuItemDescription());
				menuPriceTextField.setText(""+selectedItem.getMenuItemPrice());	
				updateButton.setDisable(false);
				deleteButton.setDisable(false);
			}
		});
	}
	
	public Stage showNewMenuItemWindow() {
		Stage newStage = new Stage();
		VBox smallBox = new VBox();
		Scene addScene = new Scene(smallBox);
		
		GridPane itemForm = createMenuItemForm(true);	
		
		smallBox.setMargin(itemForm, new Insets(20));
		smallBox.getChildren().addAll(itemForm);
		
		newStage.setTitle("Add Menu Item");
		newStage.setScene(addScene);
		return newStage;
	}
	
	
	// util functions
	public boolean validateRole(String role) {
		if(role.equals("Customer") || role.equals("Cashier") || role.equals("Admin") || role.equals("Waiter") || role.equals("Chef")) {
			return true;
		}
		return false;
	}
	
	public Stage showSuccessMessage(String args) {
		Stage newStage = new Stage();
		VBox smallBox = new VBox();
		Scene message = new Scene(smallBox);
		
		Label msg = new Label(args);
		
		smallBox.setMargin(msg, new Insets(20));
		smallBox.getChildren().addAll(msg);
		newStage.setScene(message);
		
		return newStage;
	}
	
	public void resetUserForm() {
		this.selectedUser = null;
		idLabel.setText("");
		nameLabel.setText("");
		emailLabel.setText("");
		roleField.setText("");	
	}
	
	public boolean validateMenuItemForm() {
		if(menuNameTextField.getText()=="") {
			return false;
		}
		if(menuDescriptionTextField.getText()=="") {
			return false;
		}
		if(menuPriceTextField.getText()=="") {
			return false;
		}else if(Integer.parseInt(menuPriceTextField.getText())< 1) {
			return false;
		}
		return true;
	}
	
	public void resetMenuItemForm() {
		this.selectedItem = null;
		menuIdLabel.setText("");
		menuNameTextField.setText("");
		menuDescriptionTextField.setText("");
		menuPriceTextField.setText("");	
	}
}
