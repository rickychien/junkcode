package sodar.server.user.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONObject;

public class UserProfileModel {
	protected final String driver = "com.mysql.jdbc.Driver";
	protected final String databaseURL = "jdbc:mysql://140.121.197.101:3306/sodar";
	protected final String user = "sodar";
	protected final String password = "sunshyboyz";
	protected Connection connection = null;
	protected ResultSet resultSet = null;

	public UserProfileModel() {
		// 初始化Database
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(databaseURL, user, password);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject getUserProfile(String uidMe, String uidHit, String APIKey) {
		// 檢驗API Key是否被註冊
        try {
        	Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='" + APIKey + "'";
			ResultSet rs = stmt.executeQuery(SQL);
    		
    		if (!rs.next()) {
    			JSONObject errorResult = new JSONObject();
    			errorResult.put("status", "error: Invalid API Key");
    			return errorResult;
    		}
        }
        catch (SQLException e) {
	        e.printStackTrace();
	        JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
        }
		
		JSONObject jsonResult = new JSONObject();
		
		// 搜尋符合使用者名稱
		try {
			Statement stmt = connection.createStatement();
			// 新增
			String insertHit = "INSERT INTO interaction (uid_me, uid_hit, hits, hit_time) VALUES('" + uidMe + "', '" + uidHit + "', 1, NOW()) ON DUPLICATE KEY UPDATE hits=hits+1; ";
			stmt.execute(insertHit);
			
			String querySQL = "SELECT * FROM member WHERE uid = '" + uidHit + "'";
			ResultSet rs = stmt.executeQuery(querySQL);
			
			if (rs.next()) {
				jsonResult.put("uid", uidHit);
				jsonResult.put("name", rs.getString("name"));
				jsonResult.put("phone", rs.getString("phone"));
				jsonResult.put("sex", rs.getString("sex"));
				jsonResult.put("aboutMe", rs.getString("aboutMe"));
				jsonResult.put("educationHistory", rs.getString("educationHistory"));
				jsonResult.put("workHistory", rs.getString("workHistory"));
				jsonResult.put("last_login_time", rs.getString("last_login_time"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}

		jsonResult.put("status", "success");
		return jsonResult;
	}

	public JSONObject editUserProfile(String uid, String name, String phone, String APIKey) {
		// 檢驗API Key是否被註冊
        try {
        	Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='" + APIKey + "'";
			ResultSet rs = stmt.executeQuery(SQL);
    		
    		if (!rs.next()) {
    			JSONObject errorResult = new JSONObject();
    			errorResult.put("status", "error: Invalid API Key");
    			return errorResult;
    		}
        }
        catch (SQLException e) {
	        e.printStackTrace();
	        JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
        }
        
		JSONObject jsonResult = new JSONObject();
		String querySQL = "UPDATE member SET phone = '" + phone + "' WHERE uid='" + uid + "'";
        
		try {
        	Statement stmt = connection.createStatement();
			stmt.execute(querySQL);	
        }
        catch (SQLException e) {
	        e.printStackTrace();
	        JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
        }
        
		jsonResult.put("status", "success");
		return jsonResult;
	}
}
