package view;

import java.util.ArrayList;

import controller.OrderController;
import controller.OrderItemController;
import controller.ReceiptController;
import controller.UserController;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Receipt;
import model.User;

public class CashierView extends Stage {
	private User activeUser;

	private MenuBar menuBar;
	private VBox vbox = new VBox();
	private Scene cashierScene = new Scene(vbox);
	
	// Process Payment Components
	private Order activeOrder;
	private TableView<OrderItem> activeItems;
	private Label idLabel = new Label("");
	private Label customerLabel = new Label("");
	private Label priceLabel = new Label("");
	private TextField paymentAmountField = new TextField();
	private TextField paymentMethodField = new TextField();
	
	// Receipt Management Component
	private Receipt activeReceipt;
	private TableView<OrderItem> receiptActiveItems;
	
	public CashierView(User u) {
		// TODO Auto-generated constructor stub
		this.activeUser = u;
		this.activeOrder = null;
		this.activeItems = createOrderItemTable();
		
		this.activeReceipt = null;
		this.receiptActiveItems = createOrderItemTable();
		
		menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		javafx.scene.control.MenuItem receiptManagement = new javafx.scene.control.MenuItem("Receipt Management");
		javafx.scene.control.MenuItem processPayment = new javafx.scene.control.MenuItem("Process Payment");
		receiptManagement.setOnAction(e->{
			showReceiptManagementScene();
		});
		processPayment.setOnAction(e->{
			resetCheckoutForm();
			showProcessPaymentScene();
		});
		
		menu.getItems().addAll(receiptManagement, processPayment);
		menuBar.getMenus().add(menu);
		
		showReceiptManagementScene();
		this.setScene(cashierScene);
	}
	
	// Process Payment Module
	public void showProcessPaymentScene() {
		vbox.getChildren().clear();
		
		OrderController orderController = new OrderController();
		TableView<Order> orderTable = createOrderTable();
		ArrayList<Order> orders = orderController.getAllUnpaidOrder();
		orderTable.getItems().setAll(orders);
		
		selectOrderTableEventListener(orderTable);
		
		HBox hbox = new HBox();
		hbox.setMargin(orderTable, new Insets(20));
		hbox.setMargin(activeItems, new Insets(20));
		hbox.getChildren().addAll(orderTable, activeItems);
		
		GridPane checkoutForm = createCheckoutForm();
		vbox.setMargin(hbox, new Insets(20));
		vbox.setMargin(checkoutForm, new Insets(20));
		vbox.getChildren().addAll(menuBar, hbox, checkoutForm);
		
	}
	
	public TableView<Order> createOrderTable(){
		TableView<Order> orderTable = new TableView<Order>();
		
		TableColumn<Order, Integer> idColumn = new TableColumn<Order, Integer>("Order ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		
		TableColumn<Order, String> customerColumn = new TableColumn<Order, String>("Customer");
		customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderUser().getUserName()));
		
		TableColumn<Order, String> statusColumn = new TableColumn<Order, String>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		
		TableColumn<Order, Integer> priceColumn = new TableColumn<Order, Integer>("Total Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
		
		orderTable.getColumns().addAll(idColumn, customerColumn, statusColumn, priceColumn);
		
		return orderTable;
	}
	
	public void selectOrderTableEventListener(TableView<Order> table) {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				UserController userController = new UserController();
				this.activeOrder = newSelection;
				idLabel.setText(""+activeOrder.getOrderId());
				customerLabel.setText(userController.getUserById(activeOrder.getOrderUser().getUserId()).getUserName());
				priceLabel.setText(""+activeOrder.getOrderTotal());
				paymentAmountField.setText("");
				paymentMethodField.setText("");
				
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
        
        TableColumn<OrderItem, Integer> priceColumn = new TableColumn<OrderItem, Integer>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMenuItem().getMenuItemPrice()).asObject());
        
        TableColumn<OrderItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        orderTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn);
		
		return orderTable;
	}
	
	public GridPane createCheckoutForm() {
		GridPane checkoutForm = new GridPane();
		checkoutForm.setVgap(20);
		checkoutForm.setHgap(10);
		
		Button payButton = new Button("Process Payment");
		Label err = new Label("Payment Amount must be greater than Total Price!");
		err.setVisible(false);
		
		checkoutForm.add(new Label("Order ID :"), 0, 0);
		checkoutForm.add(idLabel, 1, 0);
		checkoutForm.add(new Label("Customer :"), 0, 1);
		checkoutForm.add(customerLabel, 1, 1);
		checkoutForm.add(new Label("Total Price :"), 0, 2);
		checkoutForm.add(priceLabel, 1, 2);
		checkoutForm.add(new Label("Payment Amount :"), 0, 3);
		checkoutForm.add(paymentAmountField, 1, 3);
		checkoutForm.add(new Label("Payment Method [Cash | Debit | Card] :"), 0, 4);
		checkoutForm.add(paymentMethodField, 1, 4);
		checkoutForm.add(payButton, 1, 5);
		checkoutForm.add(err, 1, 6);
		
		payButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean val = validateForm();
				if(val) {
					err.setVisible(false);
					OrderController orderController = new OrderController();
					boolean success = orderController.updateOrder(activeOrder, "Paid");
					if(success) {
						ReceiptController receiptController = new ReceiptController();
						boolean recSuccess = receiptController.createReceipt(activeOrder.getOrderId(), Integer.parseInt(paymentAmountField.getText()), paymentMethodField.getText());
						if(recSuccess) {
							showSuccessMessage("Order Successfully Paid! Thank You for your Patronage!").show();
							resetCheckoutForm();
							showProcessPaymentScene();
						}
					}
				}
				else {
					err.setVisible(true);
				}
			}
		});
		
		return checkoutForm;
	}
	
	// Receipt Management
	public void showReceiptManagementScene() {
		vbox.getChildren().clear();
		
		TableView<Receipt> receiptTable = createReceiptTable();
		ReceiptController receiptController = new ReceiptController();
		ArrayList<Receipt> receipts = receiptController.getAllReceipts();
		receiptTable.getItems().setAll(receipts);
		
		selectReceiptTableEventListener(receiptTable);
		
		vbox.setMargin(receiptTable, new Insets(20));
		vbox.setMargin(receiptActiveItems, new Insets(20));
		vbox.getChildren().addAll(menuBar, receiptTable, receiptActiveItems);		
	}
	
	public TableView<Receipt> createReceiptTable(){
		TableView<Receipt> receiptTable = new TableView<Receipt>();
		receiptTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<Receipt, Integer> idColumn = new TableColumn<Receipt, Integer>("Receipt ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<Receipt, Integer>("receiptId"));
		
		TableColumn<Receipt, Integer> orderColumn = new TableColumn<Receipt, Integer>("Order ID");
		orderColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReceiptOrder().getOrderId()).asObject());
		
		TableColumn<Receipt, Integer> payAmountColumn = new TableColumn<Receipt, Integer>("Payment Amount");
		payAmountColumn.setCellValueFactory(new PropertyValueFactory<Receipt, Integer>("receiptPaymentAmount"));
		
		TableColumn<Receipt, String> customerColumn = new TableColumn<Receipt, String>("Payment Date");
		customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReceiptPaymentDate().toString()));
		
		TableColumn<Receipt, String> methodColumn = new TableColumn<Receipt, String>("Payment Type");
		methodColumn.setCellValueFactory(new PropertyValueFactory<Receipt, String>("receiptPaymentType"));
		
		receiptTable.getColumns().addAll(idColumn, orderColumn, payAmountColumn, customerColumn, methodColumn);
		
		return receiptTable;
	}
	
	public void selectReceiptTableEventListener(TableView<Receipt> table) {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
			if(newSelection !=null) {
				UserController userController = new UserController();
				this.activeReceipt = newSelection;
//				idLabel.setText(""+activeOrder.getOrderId());
//				customerLabel.setText(userController.getUserById(activeOrder.getOrderUser().getUserId()).getUserName());
//				priceLabel.setText(""+activeOrder.getOrderTotal());
//				paymentAmountField.setText("");
//				paymentMethodField.setText("");
//				
				OrderItemController orderItemController = new OrderItemController();
				ArrayList<OrderItem> items = orderItemController.getOrderItemByOrderId(activeReceipt.getReceiptOrder().getOrderId());
				receiptActiveItems.getItems().setAll(items);				
			}
		});
	}
	
	
	
	// utils
	public boolean validateForm() {
		if(paymentAmountField.getText()=="") {
			return false;
		}
		if(paymentMethodField.getText()=="") {
			return false;
		}
		if(Integer.parseInt(paymentAmountField.getText()) < activeOrder.getOrderTotal()) {
			return false;
		}
		if(paymentAmountField.getText().equals("Cash") && paymentAmountField.getText().equals("Debit") && paymentAmountField.getText().equals("Credit") ) {
			return false;
		}
		return true;
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
	
	public void resetCheckoutForm() {
		this.activeOrder = null;
		this.activeItems = createOrderItemTable();
		idLabel.setText("");
		customerLabel.setText("");
		priceLabel.setText("");
		paymentAmountField.setText("");
		paymentMethodField.setText("");
	}
	
}
