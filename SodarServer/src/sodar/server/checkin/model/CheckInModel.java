package sodar.server.checkin.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONObject;

public class CheckInModel {
	protected final String driver = "com.mysql.jdbc.Driver";
	protected final String databaseURL = "jdbc:mysql://140.121.197.101:3306/sodar";
	protected final String user = "sodar";
	protected final String password = "sunshyboyz";
	protected Connection connection = null;
	protected ResultSet resultSet = null;
	protected PreparedStatement preparedStatement = null;

	public CheckInModel() {
		// 初始化Database
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(databaseURL, user, password);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void Close() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
		}
		catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}

	public JSONObject checkIn(String uid, String sLongitude, String sLatitude, String description,
	        String APIKey) {
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

		int newCid = 0;

		try {
			Statement stmt = connection.createStatement();
			String queryMaxCid = "SELECT IFNULL(MAX(cid), 0) AS MAXCID FROM check_in_history";
			ResultSet rs = stmt.executeQuery(queryMaxCid);

			if (rs.next()) {
				newCid = rs.getInt("MAXCID") + 1;
			}

			preparedStatement = connection
			        .prepareStatement("INSERT INTO check_in_history(cid, uid, longitude, latitude, create_time, display_time, description) VALUES(?, ?, ?, ?, NOW(), NOW(), ?)");
			preparedStatement.setInt(1, newCid);
			preparedStatement.setString(2, uid);
			preparedStatement.setDouble(3, Double.parseDouble(sLongitude));
			preparedStatement.setDouble(4, Double.parseDouble(sLatitude));
			preparedStatement.setString(5, description);
			preparedStatement.executeUpdate();
		}
		catch (Exception e) {
			System.out.print(e.getMessage());
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}

		JSONObject jsonResult = new JSONObject();

		jsonResult.put("cid", "" + newCid);
		jsonResult.put("uid", uid);
		jsonResult.put("longitude", sLongitude);
		jsonResult.put("latitude", sLatitude);
		jsonResult.put("description", description);
		jsonResult.put("status", "success");

		return jsonResult;
	}

	public JSONObject getCheckIn(String uid, String APIKey) {
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
		try {
			Statement stmt;
			stmt = connection.createStatement();
			String SQL = "SELECT C.* FROM (SELECT uid, MAX(cid) AS MAXVAL FROM check_in_history GROUP BY uid) AS M "
			        + "INNER JOIN check_in_history AS C ON C.uid = M.uid AND C.cid = M.MAXVAL AND C.uid = '"
			        + uid + "'";
			ResultSet rs = stmt.executeQuery(SQL);

			while (rs.next()) {
				// 存入符合的資料
				jsonResult.put("uid", rs.getString("uid"));
				jsonResult.put("longitude", rs.getString("longitude"));
				jsonResult.put("latitude", rs.getString("latitude"));
				jsonResult.put("create_time", rs.getString("create_time"));
				jsonResult.put("display_time", rs.getString("display_time"));
				jsonResult.put("description", rs.getString("description"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		jsonResult.put("status", "success");
		return jsonResult;
	}
}
