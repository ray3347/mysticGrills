package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.DBConnection;
import model.MenuItem;
import model.Order;
import model.OrderItem;

public class OrderItemController {
	public boolean createOrderItem(ArrayList<OrderItem> orderItems) {
		int isTrue = 0;
		String dbQuery = "INSERT INTO orderItem (orderId, menuItemId, quantity) VALUES (?,?,?)";
		for (OrderItem orderItem : orderItems) {
			try (
					Connection connection = DBConnection.getInstance().getConnection();
					PreparedStatement ps = connection.prepareStatement(dbQuery)){
				ps.setInt(1, orderItem.getOrderId());
				ps.setInt(2, orderItem.getMenuItem().getMenuItemId());
				ps.setInt(3, orderItem.getQuantity());
				
				ps.executeUpdate();
				isTrue++;
//				finalPrice += orderItem.getQuantity()*orderItem.getMenuItem().getMenuItemPrice();
			}
			catch (SQLException e) {
				
	            e.printStackTrace();
	        }
		}
		if(isTrue == orderItems.size()) {
			return true;
		}
		return false;
	}
	
	public ArrayList<OrderItem> getOrderItemByOrderId(int orderId){
		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
		
		String dbQuery = "SELECT * FROM orderItem WHERE orderId LIKE ?";
		
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, orderId);
			ResultSet res = ps.executeQuery();
			
//			ArrayList<Integer> menuItemIndex = new ArrayList<Integer>();
			HashMap<OrderItem, Integer> hash = new HashMap<>();
			MenuItemController menuItemController = new MenuItemController();
			while(res.next()) {
				
				int orderIdDB = res.getInt("orderId");
				int menuItemId = res.getInt("menuItemId");
				
				
//				String innerQuery = "SELECT * FROM menuitem WHERE menuItemId LIKE ?";
//				MenuItem menuItem = menuItemController.getMenuItemById(menuItemId);
//				try(PreparedStatement ps2 = connection.prepareStatement(innerQuery)){
//					ps2.setInt(1, menuItemId);
//					ResultSet innerRes = ps2.executeQuery();
//					
//					while(innerRes.next()) {
//						int menuItemId2 = innerRes.getInt("menuItemId");
//						String itemName = innerRes.getString("menuItemName");
//						String menuItemDescription = res.getString("menuItemDescription");
//						int menuItemPrice = res.getInt("menuItemPrice");
//						
//						menuItem = new MenuItem(menuItemId2, itemName, menuItemDescription, menuItemPrice);
//						break;
//					}					
//				}catch (SQLException e) {
//					
//		            e.printStackTrace();
//		        }
				
				int quantity = res.getInt("quantity");
				
				OrderItem orderItem = new OrderItem(orderIdDB, null, quantity);
				hash.put(orderItem, menuItemId);
				
			}
			
			for(Map.Entry<OrderItem, Integer> item : hash.entrySet()) {
				OrderItem key = item.getKey();
				MenuItem val = menuItemController.getMenuItemById(item.getValue());
				
				item.getKey().setMenuItem(val);
				orderItems.add(item.getKey());
			}
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return orderItems;
	}
	
	public boolean updateOrderItemQuantity(OrderItem item, int quantity) {
		String dbQuery = "UPDATE orderitem SET quantity = ? WHERE orderId = ? AND menuItemId = ?";
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
//			ps.setInt(1, orderId);
//			ResultSet res = ps.executeQuery();
//			
//			MenuItemController menuItemController = new MenuItemController();
//			while(res.next()) {
//				int orderIdDB = res.getInt("orderId");
//				int menuItemId = res.getInt("menuItemId");
//				MenuItem menuItem = menuItemController.getMenuItemById(menuItemId);
//				int quantity = res.getInt("quantity");
//				
//				OrderItem orderItem = new OrderItem(orderIdDB, menuItem, quantity);
//				orderItems.add(orderItem);
//			}
			ps.setInt(1, quantity);
			ps.setInt(2, item.getOrderId());
			ps.setInt(3, item.getMenuItem().getMenuItemId());
			
			ps.executeUpdate();
			
			int finalPrice = 0;
			ArrayList<OrderItem> items = getOrderItemByOrderId(item.getOrderId());
			for (OrderItem orderItem : items) {
				finalPrice += orderItem.getQuantity() * orderItem.getMenuItem().getMenuItemPrice();
			}
			
			String innerQuery = "UPDATE `order` SET orderTotal = ? WHERE orderId = ?";
			try(
					Connection connection2 = DBConnection.getInstance().getConnection();
					PreparedStatement ps2 = connection2.prepareStatement(innerQuery)){
				ps2.setInt(1, finalPrice);
				ps2.setInt(2, item.getOrderId());
				ps2.executeUpdate();
				
				return true;
			}
			catch (SQLException e) {
				
	            e.printStackTrace();
	        }
			
			
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return false;
	}
	
	public boolean deleteOrderItems(Order order) {
		String dbQuery = "DELETE from orderItem WHERE orderId LIKE ?";
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
}
