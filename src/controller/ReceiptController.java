package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.DBConnection;
import model.Order;
import model.Receipt;

public class ReceiptController {
	public boolean createReceipt(int orderId, int payAmount, String payType ) {
		String dbQuery = "INSERT INTO receipt (orderId, receiptPaymentAmount, receiptPaymentDate, receiptPaymentType) VALUES (?,?,?,?)";
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			LocalDateTime currentDateTime = LocalDateTime.now();
		    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
			ps.setInt(1, orderId);
			ps.setInt(2, payAmount);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, payType);
			ps.executeUpdate();
			
			return true;
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		
		return false;
	}
	
	public ArrayList<Receipt> getAllReceipts(){
		ArrayList<Receipt> recList = new ArrayList<>();
		String dbQuery = "SELECT * FROM receipt";
		try(
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(dbQuery)){
			ResultSet res = ps.executeQuery();
			
			HashMap<Receipt, Integer> hashmap = new HashMap<Receipt, Integer>();
			
			while(res.next()) {
				int receiptId = res.getInt("receiptId");
				int orderId = res.getInt("orderId");
				int receiptPaymentAmount = res.getInt("receiptPaymentAmount");
				Timestamp receiptDate = res.getTimestamp("receiptPaymentDate");
				Date receiptDateDate = new Date(receiptDate.getTime());
				String receiptPaymentMethod = res.getString("receiptPaymentType");
				
				Receipt rec = new Receipt(receiptId, null, receiptPaymentAmount, receiptDateDate, receiptPaymentMethod);
				hashmap.put(rec, orderId);
			}
			
			OrderController orderController = new OrderController();
			for(Map.Entry<Receipt, Integer> item : hashmap.entrySet()) {
				Receipt obj = item.getKey();
				Order order = orderController.getOrderByOrderId(item.getValue());
				
				obj.setReceiptOrder(order);
				recList.add(obj);
			}
		}
		catch (SQLException e) {
			
            e.printStackTrace();
        }
		return recList;
	}
}
