package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.DBConnection;
import model.MenuItem;

public class MenuItemController {
	public ArrayList<MenuItem> getAllMenuItems(){
		ArrayList<MenuItem> items = new ArrayList<MenuItem>();
		
		String dbQuery = "SELECT * FROM menuitem";
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				int menuItemId = res.getInt("menuItemId");
				String menuItemName = res.getString("menuItemName");
				String menuItemDescription = res.getString("menuItemDescription");
				int menuItemPrice = res.getInt("menuItemPrice");
				
				MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice);
				items.add(menuItem);
			}
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return items;
	}
	
	public MenuItem getMenuItemById(int menuItemId) {
		String dbQuery = "SELECT * FROM menuitem WHERE menuItemId LIKE ?";
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, menuItemId);
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				int menuItemIdDB = res.getInt("menuItemId");
				String menuItemName = res.getString("menuItemName");
				String menuItemDescription = res.getString("menuItemDescription");
				int menuItemPrice = res.getInt("menuItemPrice");
				
				MenuItem menuItem = new MenuItem(menuItemIdDB, menuItemName, menuItemDescription, menuItemPrice);
				return menuItem;
			}
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public boolean createMenuItem(MenuItem item) {
		String dbQuery = "INSERT INTO menuitem (menuItemName, menuItemDescription, menuItemPrice) VALUES (?,?,?)";
		
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setString(1, item.getMenuItemName());
			ps.setString(2, item.getMenuItemDescription());
			ps.setInt(3, item.getMenuItemPrice());
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return false;
	}
	
	public boolean updateMenuItem(MenuItem item) {
		String dbQuery = "UPDATE menuitem SET menuItemName = ?, menuItemDescription = ?, menuItemPrice = ? WHERE menuItemId = ?";
		
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setString(1, item.getMenuItemName());
			ps.setString(2, item.getMenuItemDescription());
			ps.setInt(3, item.getMenuItemPrice());
			ps.setInt(4, item.getMenuItemId());
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return false;
	}
	
	public boolean deleteMenuItem(MenuItem item) {
		String dbQuery = "DELETE FROM menuitem WHERE menuItemId LIKE ?";
		
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){;
			ps.setInt(1, item.getMenuItemId());
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		
		return false;
	}
}
