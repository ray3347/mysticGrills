package view;

import java.util.ArrayList;

import controller.OrderController;
import controller.OrderItemController;
import controller.UserController;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class RestaurantOperationView extends Stage {
	private User activeUser;
	private VBox vbox = new VBox();
	private Scene restaurantOpsScene = new Scene(vbox);
	
	// components
	private Label idLabel = new Label("");
	private Label customerLabel = new Label("");
	private Label dateLabel = new Label("");
	private Label priceLabel = new Label("");
	private Label statusLabel = new Label("");
	private Button prepareBtn = new Button("Prepare Order");
	private Button serveBtn = new Button("Serve Order");
	private Button deleteBtn = new Button("Remove Order");
	private Order activeOrder;
	private TableView<OrderItem> activeItems;

	public RestaurantOperationView(User u) {
		// TODO Auto-generated constructor stub
		this.activeUser = u;
		this.activeOrder = null;
		this.activeItems = createOrderItemTable();;
		
		showRestaurantOperationsScene();
		this.setScene(restaurantOpsScene);
	}
	
	public void showRestaurantOperationsScene() {
		vbox.getChildren().clear();
		
		OrderController orderController = new OrderController();
		TableView<Order> orderTable = createOrderTable();
		ArrayList<Order> orders = new ArrayList<Order>();
		if(activeUser.getUserRole().equals("Chef")) {
			orders = orderController.getOrderByStatus("Pending");
		}
		else if(activeUser.getUserRole().equals("Waiter")) {
			orders = orderController.getOrderByStatus("Prepared");
		}	
		orderTable.getItems().setAll(orders);
		selectOrderTableEventListener(orderTable);
		
		HBox hbox = new HBox();
		hbox.setMargin(orderTable, new Insets(20));
		hbox.setMargin(activeItems, new Insets(20));
		hbox.getChildren().addAll(orderTable, activeItems);
		
		GridPane orderForm = createOrderDetailForm();
		vbox.setMargin(hbox, new Insets(20));
		vbox.setMargin(orderForm, new Insets(20));
		vbox.getChildren().addAll(hbox, orderForm);
	}
	
	public TableView<Order> createOrderTable(){
		TableView<Order> orderTable = new TableView<Order>();
		
		TableColumn<Order, Integer> idColumn = new TableColumn<Order, Integer>("Order ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		
		TableColumn<Order, String> statusColumn = new TableColumn<Order, String>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		
		TableColumn<Order, Integer> priceColumn = new TableColumn<Order, Integer>("Total Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
		
		orderTable.getColumns().addAll(idColumn, statusColumn, priceColumn);
		
		return orderTable;
	}
	
	public void selectOrderTableEventListener(TableView<Order> table) {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				UserController userController = new UserController();
				this.activeOrder = newSelection;
				idLabel.setText(""+activeOrder.getOrderId());
				customerLabel.setText(userController.getUserById(activeOrder.getOrderUser().getUserId()).getUserName());
				dateLabel.setText(""+activeOrder.getOrderDate());
				priceLabel.setText(""+activeOrder.getOrderTotal());	
				statusLabel.setText(activeOrder.getOrderStatus());
				if(activeUser.getUserRole().equals("Chef")) {
					prepareBtn.setDisable(false);
				}
				else if(activeUser.getUserRole().equals("Waiter")) {
					serveBtn.setDisable(false);
				}		
				
				deleteBtn.setDisable(false);
				
				OrderItemController orderItemController = new OrderItemController();
				ArrayList<OrderItem> items = orderItemController.getOrderItemByOrderId(activeOrder.getOrderId());
				activeItems.getItems().setAll(items);				
			}
		});
	}
	
	public TableView<OrderItem> createOrderItemTable(){
		TableView<OrderItem> orderTable = new TableView<OrderItem>();
        
        TableColumn<OrderItem, String> nameColumn = new TableColumn<>("Name");
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menuItem"));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMenuItem().getMenuItemName()));
        
        TableColumn<OrderItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        orderTable.getColumns().addAll(nameColumn, quantityColumn);
		
		return orderTable;
	}
	
	public GridPane createOrderDetailForm() {
		GridPane orderForm = new GridPane();
		orderForm.setVgap(20);
		orderForm.setHgap(10);
		
		prepareBtn.setDisable(true);
		serveBtn.setDisable(true);
		deleteBtn.setDisable(true);
		
		orderForm.add(new Label("Order ID :"), 0, 0);
		orderForm.add(idLabel, 1, 0);
		orderForm.add(new Label("Customer Name"), 0, 1);
		orderForm.add(customerLabel, 1, 1);
		orderForm.add(new Label("Order Date"), 0, 2);
		orderForm.add(dateLabel, 1, 2);
		orderForm.add(new Label("Price"), 0, 3);
		orderForm.add(priceLabel, 1, 3);
		orderForm.add(new Label("Status") , 0, 4);
		orderForm.add(statusLabel, 1, 4);
		if(activeUser.getUserRole().equals("Chef")) {
			orderForm.add(prepareBtn, 1, 5);
		}
		else if(activeUser.getUserRole().equals("Waiter")) {
			orderForm.add(serveBtn, 1, 5);
		}
		orderForm.add(deleteBtn, 1, 6);
		
		prepareBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				OrderController orderController = new OrderController();
				try {
					boolean success = orderController.updateOrder(activeOrder, "Prepared");
					if(success) {
						showSuccessMessage("Order Preparation Successfull!").show();
						resetForm();
						showRestaurantOperationsScene();
					}
				}
				catch(Exception ex)
				{
					throw ex;
				}
			}
		});
		
		serveBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				OrderController orderController = new OrderController();
				try {
					boolean success = orderController.updateOrder(activeOrder, "Served");
					if(success) {
						showSuccessMessage("Order Has Been Served!").show();
						resetForm();
						showRestaurantOperationsScene();
					}
				}
				catch(Exception ex)
				{
					throw ex;
				}
			}
		});
		
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				OrderController orderController = new OrderController();
				OrderItemController orderItemController = new OrderItemController();
				try {
					boolean valid = orderItemController.deleteOrderItems(activeOrder);
					if(valid) {
						boolean success = orderController.deleteOrder(activeOrder);
						if(success) {
							showSuccessMessage("Order Successfully Deleted!").show();
							resetForm();
							showRestaurantOperationsScene();
						}
					}
					
				}
				catch(Exception ex)
				{
					throw ex;
				}
			}
		});
		
		return orderForm;
	}
	
	//utils
	public void resetForm() {
		this.activeOrder = null;
		this.activeItems = createOrderItemTable();
		idLabel.setText("");
		customerLabel.setText("");
		dateLabel.setText("");
		priceLabel.setText("");	
		statusLabel.setText("");
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
	
}
