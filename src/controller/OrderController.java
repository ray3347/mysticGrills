package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.DBConnection;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class OrderController {
	public int getNewOrderId() {
		String dbQuery = "SELECT MAX(orderId) AS orderId FROM `order`";
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				int newId = res.getInt("orderId");
				return newId+1;
			}
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		return 1;
		
	}
	
	public boolean createOrder(User orderUser, int finalPrice) {
		int isTrue = 0;
		String dbQuery = "INSERT INTO `order` (userId, orderStatus, orderDate, orderTotal) VALUES (?,?,?,?)";
		
		try (
		Connection connection = DBConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(dbQuery)){
			
			LocalDateTime currentDateTime = LocalDateTime.now();
		    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
			ps.setInt(1, orderUser.getUserId());
			ps.setString(2, "Pending");
			ps.setTimestamp(3, timestamp);
			ps.setInt(4, finalPrice);
		
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			
		    e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Order> getOrdersByCustomer(int userId){
		ArrayList<Order> orders = new ArrayList<>();
		
		String dbQuery = "SELECT * FROM `order` WHERE userId LIKE ?";
		
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, userId);
			ResultSet res = ps.executeQuery();		
			
			UserController userController = new UserController();
			while(res.next()) {				
				int orderId = res.getInt("orderId");
//				int userIdDB = res.getInt("userId");
				String orderStatus = res.getString("orderStatus");
				Timestamp orderDate = res.getTimestamp("orderDate");
				Date orderDateDate = new Date(orderDate.getTime());
				int orderTotal = res.getInt("orderTotal");
//				User u = userController.getUserById(res.getInt("userId"));
				
				Order order = new Order(orderId, null, orderStatus, orderDateDate, orderTotal);				
				orders.add(order);
			}
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return orders;
	}
	
	public boolean updateOrder(Order order, String status) {
		String dbQuery = "UPDATE `order` SET orderStatus = ? WHERE orderId LIKE ?";
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setString(1, status);
			ps.setInt(2, order.getOrderId());
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		return false;
	}
	
	public boolean deleteOrder(Order order) {
		String dbQuery = "DELETE from `order` WHERE orderId LIKE ?";
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, order.getOrderId());
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		return false;
	}
	
	public ArrayList<Order> getOrderByStatus(String status){
		ArrayList<Order> orderList = new ArrayList<Order>();
		String dbQuery = "SELECT * FROM `order` WHERE orderStatus LIKE ?";
		
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setString(1, status);
			ResultSet res = ps.executeQuery();
			
			HashMap<Order, Integer> hashmap = new HashMap<Order, Integer>();
			
			while(res.next()) {
				int orderId = res.getInt("orderId");
				int userId = res.getInt("userId");
				String orderStatus = res.getString("orderStatus");
				Date orderDate = new Date(res.getTimestamp("orderDate").getTime());
				int orderTotal = res.getInt("orderTotal");
				
				Order order = new Order(orderId, null, orderStatus, orderDate, orderTotal);		
				hashmap.put(order, userId);
			}		
			
			UserController userController = new UserController();
			for(Map.Entry<Order, Integer> item : hashmap.entrySet()) {
				Order obj = item.getKey();
				User u = userController.getUserById(item.getValue());
				obj.setOrderUser(u);
				orderList.add(obj);
			}
			
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return orderList;
	}
	
	public ArrayList<Order> getAllUnpaidOrder(){
		ArrayList<Order> orderList = new ArrayList<Order>();
		String dbQuery = "SELECT * FROM `order` WHERE orderStatus LIKE 'Served'";
		
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ResultSet res = ps.executeQuery();
			
			HashMap<Order, Integer> hashmap = new HashMap<Order, Integer>();
			
			while(res.next()) {
				int orderId = res.getInt("orderId");
				int userId = res.getInt("userId");
				String orderStatus = res.getString("orderStatus");
				Date orderDate = new Date(res.getTimestamp("orderDate").getTime());
				int orderTotal = res.getInt("orderTotal");
				
				Order order = new Order(orderId, null, orderStatus, orderDate, orderTotal);		
				hashmap.put(order, userId);
			}		
			
			UserController userController = new UserController();
			for(Map.Entry<Order, Integer> item : hashmap.entrySet()) {
				Order obj = item.getKey();
				User u = userController.getUserById(item.getValue());
				obj.setOrderUser(u);
				orderList.add(obj);
			}
			
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return orderList;
	}
	
	public Order getOrderByOrderId(int id) {
		String dbQuery = "SELECT * FROM `order` WHERE orderId LIKE ?";
		
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, id);
			ResultSet res = ps.executeQuery();
			
			HashMap<Order, Integer> hashmap = new HashMap<Order, Integer>();
			
			while(res.next()) {
				int orderId = res.getInt("orderId");
				int userId = res.getInt("userId");
				String orderStatus = res.getString("orderStatus");
				Timestamp orderDate = res.getTimestamp("orderDate");
				Date orderDateDate = new Date(orderDate.getTime());
				int orderTotal = res.getInt("orderTotal");
				
				Order order = new Order(orderId, null, orderStatus, orderDateDate, orderTotal);
				
				hashmap.put(order, userId);
			}	
			
			UserController userController = new UserController();
			for(Map.Entry<Order, Integer> item : hashmap.entrySet()) {
				Order order = item.getKey();
				User u = userController.getUserById(item.getValue());
				
				order.setOrderUser(u);
				return order;
			}
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return null;
	}
}
