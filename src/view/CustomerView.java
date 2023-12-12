package view;

import java.util.ArrayList;

import controller.MenuItemController;
import controller.OrderController;
import controller.OrderItemController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class CustomerView extends Stage {
	private User activeUser;
	private MenuItem selectedItem;
	private ArrayList<OrderItem> orderItems;
	
	private int selectedOrderId;
	private ArrayList<OrderItem> selectedOrderItems;
	private OrderItem activeOrderItem;
	
	private MenuBar menuBar;
	private VBox vboxViewMenuItems = new VBox();
	private Scene viewMenuItems = new Scene(vboxViewMenuItems);
	private Label idLabel = new Label("");
	private Label nameLabel = new Label("");
	private Label descLabel = new Label("");
	private Label priceLabel = new Label("");
	private TextField quantityField = new TextField();
	private TableView<OrderItem> orderItemTable;
	
	private TableView<Order> orderTable;
	private TableView<OrderItem> selectedOrderItemTable;
	
	public CustomerView(User u) {
		this.activeUser = u;
		this.selectedItem = null;
		this.orderItems = new ArrayList<OrderItem>();
		this.selectedOrderItems = new ArrayList<OrderItem>();
		
		menuBar = new MenuBar();
		Menu viewMenu = new Menu("Menu");
		javafx.scene.control.MenuItem viewMenuItem = new javafx.scene.control.MenuItem("View Menu Items");
		viewMenuItem.setOnAction(e->{
			resetForm();
			showViewMenuItemsScene();
		});
		javafx.scene.control.MenuItem orderedMenu = new javafx.scene.control.MenuItem("View Your Orders");
		orderedMenu.setOnAction(e->{
			resetForm();
			showViewOrderedMenuItems();
		});
		viewMenu.getItems().addAll(viewMenuItem, orderedMenu);
		menuBar.getMenus().addAll(viewMenu);
		
		showViewMenuItemsScene();
		this.setScene(viewMenuItems);
	}
	
	
	// View Menu Items Page -> Add Order / Feature
	public void showViewMenuItemsScene() {
		vboxViewMenuItems.getChildren().clear();
		MenuItemController menuItemController = new MenuItemController();
		ArrayList<MenuItem> menuItems = menuItemController.getAllMenuItems();
		
		TableView<MenuItem> menuTable = createMenuItemTable();
		menuTable.getItems().setAll(menuItems);
		GridPane addOrderForm = createAddOrderForm();
		
		selectMenuItemTableEventListener(menuTable);
		
		GridPane ordersGrid = createOrdersGrid();
		
		HBox hbox = new HBox();
		hbox.setMargin(addOrderForm, new Insets(20));
		hbox.setMargin(ordersGrid, new Insets(20));
		hbox.getChildren().addAll(addOrderForm, ordersGrid);
		
		vboxViewMenuItems.setMargin(menuTable, new Insets(20));
		vboxViewMenuItems.getChildren().addAll(menuBar, menuTable, hbox);
		
	}
	
	public TableView<MenuItem> createMenuItemTable(){
		TableView<MenuItem> menuTable = new TableView<>();
		menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<MenuItem, Integer> idColumn = new TableColumn<>("Item ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemId"));
        
        TableColumn<MenuItem, String> nameColumn = new TableColumn<>("Item Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));
        
        TableColumn<MenuItem, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));
        
        TableColumn<MenuItem, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));
        
        menuTable.getColumns().add(idColumn);
        menuTable.getColumns().add(nameColumn);
        menuTable.getColumns().add(descColumn);
        menuTable.getColumns().add(priceColumn);
        
		return menuTable;
	}
	
	public void selectMenuItemTableEventListener(TableView<MenuItem> table){
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				int activeId = newSelection.getMenuItemId();
				String activeName = newSelection.getMenuItemName();
				String activeDesc = newSelection.getMenuItemDescription();
				int activePrice = newSelection.getMenuItemPrice();
				
				idLabel.setText(""+activeId);
				nameLabel.setText(activeName);
				descLabel.setText(activeDesc);
				priceLabel.setText(""+activePrice);
				
				MenuItem menuItem = new MenuItem(activeId,activeName,activeDesc,activePrice);
				this.selectedItem = menuItem;
			}
		});
	}
	
	public GridPane createAddOrderForm() {
		GridPane addOrderForm = new GridPane();
		addOrderForm.setVgap(20);
		addOrderForm.setHgap(10);
		
		Button addOrder = new Button("Add to Order");
		Label err = new Label("Quantity must be more than 0");
		err.setVisible(false);
		
		addOrderForm.add(new Label("Item ID : "), 0, 0);
		addOrderForm.add(idLabel, 1, 0);
		addOrderForm.add(new Label("Item Name : "), 0, 1);
		addOrderForm.add(nameLabel, 1, 1);
		addOrderForm.add(new Label("Description : "), 0, 2);
		addOrderForm.add(descLabel, 1, 2);
		addOrderForm.add(new Label("Price : "), 0, 3);
		addOrderForm.add(priceLabel, 1, 3);
		addOrderForm.add(new Label("Quantity : "), 0, 4);
		addOrderForm.add(quantityField, 1, 4);
		addOrderForm.add(addOrder, 1, 5);
		addOrderForm.add(err, 1, 6);
		
		addOrder.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean val = validateQuantity(Integer.parseInt(quantityField.getText()));
				if(val) {
					try {
						err.setVisible(false);
						
						OrderController orderController = new OrderController();
						OrderItem item = new OrderItem(orderController.getNewOrderId(), selectedItem, Integer.parseInt(quantityField.getText()));
						int flag = 0;
						for (OrderItem orderItem : orderItems) {
							if(orderItem.getMenuItem().getMenuItemId() == item.getMenuItem().getMenuItemId()) {
								orderItem.setQuantity(orderItem.getQuantity() + item.getQuantity());
								flag = 1;
							}
						}
						if(flag == 0) {
							orderItems.add(item);	
						}
						orderItemTable.getItems().setAll(orderItems);						
						quantityField.setText("");
						
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
		
		return addOrderForm;
	}
	
	public GridPane createOrdersGrid() {
		GridPane orderGrid = new GridPane();
		
		orderItemTable = createOrderItemTable();
		orderItemTable.getItems().setAll(orderItems);
		Button orderBtn = new Button("Order");
		
		orderGrid.add(orderItemTable, 0, 0);
		orderGrid.add(orderBtn, 0, 1);
		
		orderBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					OrderController orderController = new OrderController();
					
					int finalPrice = 0;
					for (OrderItem orderItem : orderItems) {
						finalPrice += orderItem.getQuantity()*orderItem.getMenuItem().getMenuItemPrice();
					}
					
					boolean success = orderController.createOrder(activeUser, finalPrice);
					if(success) {
						OrderItemController orderItemController = new OrderItemController();
						boolean submitOrderItems = orderItemController.createOrderItem(orderItems);
						if(submitOrderItems) {
							showOrderSuccessMessage("Items Successfully Ordered!").show();
							resetForm();
							showViewMenuItemsScene();
						}
						
					}
				}
				catch(Exception ex) {
					throw ex;
				}
			}
		});
		
		return orderGrid;
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
	
	public void selectOrderTableEventListener(TableView<Order> table){
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				int orderId = newSelection.getOrderId();
				this.selectedOrderId = orderId;
				
				OrderItemController orderItemController = new OrderItemController();
				selectedOrderItems = orderItemController.getOrderItemByOrderId(selectedOrderId);
				
				selectedOrderItemTable.getItems().setAll(selectedOrderItems);
			}
		});
	}
	
	public void selectOrderItemTableEventListener(TableView<OrderItem> table){
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				activeOrderItem = newSelection;
				nameLabel.setText(newSelection.getMenuItem().getMenuItemName());
				descLabel.setText(""+newSelection.getMenuItem().getMenuItemPrice());
				quantityField.setText(""+newSelection.getQuantity());
				
			}
		});
	}
	
	public boolean validateQuantity(int quantity) {
		if(quantity > 0) {
			return true;
		}
		return false;
	}
	
	
	public Stage showOrderSuccessMessage(String args) {
		Stage newStage = new Stage();
		VBox smallBox = new VBox();
		Scene message = new Scene(smallBox);
		
		Label msg = new Label(args);
		
		smallBox.setMargin(msg, new Insets(20));
		smallBox.getChildren().addAll(msg);
		newStage.setScene(message);
		
		return newStage;
	}
	
	// View Ordered Menu Items - Update Ordered Menu Items	
	public void showViewOrderedMenuItems() {
		
		vboxViewMenuItems.getChildren().clear();
		OrderController orderController = new OrderController();
		ArrayList<Order> customerOrders = orderController.getOrdersByCustomer(activeUser.getUserId());
		
		orderTable = createOrderTable();
		orderTable.getItems().setAll(customerOrders);
		
		selectedOrderItemTable = createOrderItemTable();
		
		selectOrderTableEventListener(orderTable);
		selectOrderItemTableEventListener(selectedOrderItemTable);
		
		HBox hbox = new HBox();
		hbox.setMargin(orderTable, new Insets(20));
		hbox.setMargin(selectedOrderItemTable, new Insets(20));
		hbox.getChildren().addAll(orderTable, selectedOrderItemTable);
		
		GridPane orderForm = createOrderForm();
		vboxViewMenuItems.setMargin(hbox, new Insets(20));
		vboxViewMenuItems.setMargin(orderForm, new Insets(20));
		vboxViewMenuItems.getChildren().addAll(menuBar, hbox, orderForm);
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
	
	
	public GridPane createOrderForm() {
		GridPane orderForm = new GridPane();
		orderForm.setVgap(20);
		orderForm.setHgap(10);
		
		Button updateButton = new Button("Update Order Item");
		
		Label err = new Label("Quantity must be greater than 0");
		err.setVisible(false);
		
		orderForm.add(new Label("Item Name"), 0, 0);
		orderForm.add(nameLabel, 1, 0);
		orderForm.add(new Label("Item Price"), 0, 1);
		orderForm.add(descLabel, 1, 1);
		orderForm.add(new Label("Quantity"), 0, 2);
		orderForm.add(quantityField, 1, 2);
		orderForm.add(updateButton, 1, 3);
		orderForm.add(err, 1, 4);
		
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean val = validateQuantity(Integer.parseInt(quantityField.getText()));
				if(val){
					try {
						err.setVisible(false);
						OrderItemController orderItemController = new OrderItemController();
						boolean success = orderItemController.updateOrderItemQuantity(activeOrderItem, Integer.parseInt(quantityField.getText()));
						if(success) {
							showOrderSuccessMessage("Order Successfully Updated!").show();
							resetForm();
							showViewOrderedMenuItems();
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
		
		return orderForm;
	}
	
	public void resetForm() {
		idLabel.setText("");
		nameLabel.setText("");
		descLabel.setText("");
		priceLabel.setText("");
		quantityField.setText("");
		this.selectedItem = null;
		this.orderItems.clear();
		this.selectedOrderItems.clear();
		this.orderItemTable = null;
		this.orderTable = null;
		this.selectedOrderItemTable = null;
		this.selectedOrderId = 0;
		
	}
}
