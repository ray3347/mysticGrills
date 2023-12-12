package model;

import java.util.ArrayList;
import java.util.Date;

public class Order {
	private int orderId;
	private User orderUser;
	private ArrayList<OrderItem> orderItems;
	private String orderStatus;
	private Date orderDate;
	private int orderTotal;
	
	public Order(int orderId, User orderUser, String orderStatus, Date orderDate, int orderTotal) {
		super();
		this.orderId = orderId;
		this.orderUser = orderUser;
		this.orderDate = orderDate;
		this.orderItems = new ArrayList<>();
		this.orderStatus = orderStatus;		
		this.orderTotal = orderTotal;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public User getOrderUser() {
		return orderUser;
	}

	public void setOrderUser(User orderUser) {
		this.orderUser = orderUser;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public ArrayList<OrderItem> getOrderItems() {
		return orderItems;
	}

	public int getOrderTotal() {
		return orderTotal;
	}	
	
}
