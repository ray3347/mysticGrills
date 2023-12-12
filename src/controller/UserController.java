package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.DBConnection;
import model.User;

public class UserController {

	public boolean createUser(String userRole, String userName, String userEmail, String userPassword) {
		String dbQuery = "INSERT INTO user (userName, userEmail, userPassword, userRole) VALUES (?,?,?,?)";
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setString(1, userName);
			ps.setString(2, userEmail);
			ps.setString(3, userPassword);
			ps.setString(4, userRole);
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public User authenticateUser(String email, String password) {
		
		String dbQuery = "SELECT * FROM user WHERE userEmail LIKE ? AND userPassword LIKE ?";
		try (
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
				ps.setString(1, email);
				ps.setString(2, password);
				ResultSet res = ps.executeQuery();
				
				while(res.next()) {
					int userId = res.getInt("userId");
					String userName = res.getString("userName");
					String userEmail = res.getString("userEmail");
					String userPassword = res.getString("userPassword");
					String userRole = res.getString("userRole");
					
					User u = new User(userId, userRole, userName, userEmail, userPassword);
					return u;
				}
				
		}catch (SQLException e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public ArrayList<User> getAllUsers(){
		
		ArrayList<User> userList = new ArrayList<User>();
		String dbQuery = "SELECT * FROM user";
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				int userId = res.getInt("userId");
				String userName = res.getString("userName");
				String userEmail = res.getString("userEmail");
				String userPassword = res.getString("userPassword");
				String userRole = res.getString("userRole");
				
				User u = new User(userId, userRole, userName, userEmail, userPassword);
				userList.add(u);
			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
		
		return userList;
	}
	
	public User getUserById(int id) {
		
		String dbQuery = "SELECT * FROM user WHERE userId LIKE ?";
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, id);
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				int userId = res.getInt("userId");
				String userName = res.getString("userName");
				String userEmail = res.getString("userEmail");
				String userPassword = res.getString("userPassword");
				String userRole = res.getString("userRole");
				
				User u = new User(userId, userRole, userName, userEmail, userPassword);
				return u;
			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public boolean updateUser(User user) {
		String dbQuery = "UPDATE user SET userRole = ? WHERE userId = ?";
		
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setString(1, user.getUserRole());
			ps.setInt(2, user.getUserId());
			ps.executeUpdate();
			
			return true;
//			ResultSet res = ps.executeQuery();
			
//			while(res.next()) {
//				int userId = res.getInt("userId");
//				String userName = res.getString("userName");
//				String userEmail = res.getString("userEmail");
//				String userPassword = res.getString("userPassword");
//				String userRole = res.getString("userRole");
//				
//				User u = new User(userId, userRole, userName, userEmail, userPassword);
//				return u;
//			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
		
		return false;
	}
	
	public boolean deleteUser(User user) {
		String dbQuery = "DELETE from WHERE userId = ?";
		
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ps.setInt(1, user.getUserId());
			ps.executeUpdate();
			
			return true;
//			ResultSet res = ps.executeQuery();
			
//			while(res.next()) {
//				int userId = res.getInt("userId");
//				String userName = res.getString("userName");
//				String userEmail = res.getString("userEmail");
//				String userPassword = res.getString("userPassword");
//				String userRole = res.getString("userRole");
//				
//				User u = new User(userId, userRole, userName, userEmail, userPassword);
//				return u;
//			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
		
		return false;
	}
}
