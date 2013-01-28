package sodar.server.event.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EventModel {
	protected final String driver = "com.mysql.jdbc.Driver";
	protected final String databaseURL = "jdbc:mysql://140.121.197.101:3306/sodar";
	protected final String user = "sodar";
	protected final String password = "sunshyboyz";
	protected Connection connection = null;
	protected ResultSet resultSet = null;
	protected PreparedStatement preparedStatement = null;

	public EventModel() {
		// 初始化Database
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(databaseURL, user,
			        password);
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

	public JSONObject createEvent(String uid, String sType, String title,
	        String start_time, String end_time, String display_time,
	        String description, String sLongitude, String sLatitude,
	        String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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

		int newEid = 0;
		
		try {
			int type = Integer.parseInt(sType);
			double longitude = Double.parseDouble(sLongitude);
			double latitude = Double.parseDouble(sLatitude);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start_t = dateFormat.parse(start_time);
			Date end_t = dateFormat.parse(end_time);
			Date display_t = dateFormat.parse(display_time);
			java.sql.Timestamp startTimestamp = new java.sql.Timestamp(start_t
			        .getTime());
			java.sql.Timestamp endTimestamp = new java.sql.Timestamp(end_t
			        .getTime());
			java.sql.Timestamp displayTimestamp = new java.sql.Timestamp(
			        display_t.getTime());
			
			Statement stmt = connection.createStatement();
			String queryMaxCid = "SELECT IFNULL(MAX(eid), 0) AS MAXEID FROM event_history";
			ResultSet rs = stmt.executeQuery(queryMaxCid);
			
			if (rs.next()) {
				newEid = rs.getInt("MAXEID") + 1;
			}

			preparedStatement = connection
			        .prepareStatement("INSERT INTO event_history(eid, uid, type, title, start_time, end_time, display_time, description, longitude, latitude) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, newEid);
			preparedStatement.setString(2, uid);
			preparedStatement.setInt(3, type);
			preparedStatement.setString(4, title);
			preparedStatement.setTimestamp(5, startTimestamp);
			preparedStatement.setTimestamp(6, endTimestamp);
			preparedStatement.setTimestamp(7, displayTimestamp);
			preparedStatement.setString(8, description);
			preparedStatement.setDouble(9, longitude);
			preparedStatement.setDouble(10, latitude);
			preparedStatement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}
		finally {
			Close();
		}

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("eid", "" + newEid);
		jsonResult.put("uid", uid);
		jsonResult.put("longitude", sLongitude);
		jsonResult.put("latitude", sLatitude);
		jsonResult.put("status", "success");

		return jsonResult;
	}

	public JSONObject getEvent(String eid, String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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
		String uid;
		String name;
		String type;
		String title;
		String start_time;
		String end_time;
		String display_time;
		String description;
		String longitude;
		String latitude;
		String phone;
		String querySQL = "SELECT e.*, m.name, m.phone FROM event_history as e INNER JOIN member as m WHERE e.uid = m.uid AND e.eid = '"
		        + eid + "'";
		Statement stmt;

		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(querySQL);
			while (rs.next()) {
				uid = rs.getString("uid");
				name = rs.getString("name");
				type = rs.getString("type");
				title = rs.getString("title");
				start_time = rs.getString("start_time");
				end_time = rs.getString("end_time");
				display_time = rs.getString("display_time");
				description = rs.getString("description");
				longitude = rs.getString("longitude");
				latitude = rs.getString("latitude");
				phone = rs.getString("phone");
				// 存入符合的資料
				jsonResult.put("eid", eid);
				jsonResult.put("uid", uid);
				jsonResult.put("name", name);
				jsonResult.put("type", type);
				jsonResult.put("title", title);
				jsonResult.put("start_time", start_time);
				jsonResult.put("end_time", end_time);
				jsonResult.put("display_time", display_time);
				jsonResult.put("description", description);
				jsonResult.put("longitude", longitude);
				jsonResult.put("latitude", latitude);
				jsonResult.put("phone", phone);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}
		finally {
			Close();
		}

		jsonResult.put("status", "success");
		return jsonResult;
	}

	public JSONObject editEvent(String eid, String uid, String sType,
	        String title, String start_time, String end_time,
	        String display_time, String description, String sLongitude,
	        String sLatitude, String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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
		String querySQL = "UPDATE event_history SET  type ='" + sType
		        + "', title = '" + title + "', start_time = '" + start_time
		        + "', end_time = '" + end_time + "', display_time = '"
		        + display_time + "', description = '" + description
		        + "', longitude = '" + sLongitude + "', latitude = '"
		        + sLatitude + "' WHERE eid='" + eid + "'";
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.execute(querySQL);
		}
		catch (SQLException e) {
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}
		finally {
			Close();
		}

		jsonResult.put("status", "success");
		return jsonResult;
	}

	public JSONObject removeEvent(String eid, String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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
		String querySQL = "DELETE FROM event_history WHERE eid = '" + eid + "'";
		String querySQL2 = "DELETE FROM event_participant WHERE eid = '" + eid
		        + "'";
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.execute(querySQL);
			stmt.execute(querySQL2);
		}
		catch (SQLException e) {

			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;

		}
		finally {
			Close();
		}

		jsonResult.put("status", "success");
		return jsonResult;
	}

	public JSONObject addParticipant(String eid, String uid, String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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
		String querySQL = "INSERT INTO event_participant VALUES('" + uid
		        + "','" + eid + "')";
		Statement stmt;

		try {
			stmt = connection.createStatement();
			stmt.execute(querySQL);
		}
		catch (SQLException e) {

			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;

		}
		finally {
			Close();
		}

		jsonResult.put("status", "success");
		return jsonResult;
	}

	public JSONObject getParticipant(String eid, String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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
		String querySQL = "SELECT C.*, MB.* FROM (SELECT uid, MAX(cid) AS MAXVAL FROM check_in_history GROUP BY uid) AS M "
		        + "INNER JOIN check_in_history AS C INNER JOIN member AS MB INNER JOIN event_participant AS EP ON C.uid = M.uid AND C.cid = M.MAXVAL AND MB.uid = C.uid AND EP.uid = MB.uid AND EP.eid = '"
		        + eid + "'";
		Map<String, String> data = new LinkedHashMap<String, String>();
		JSONArray list = new JSONArray();
		Statement stmt;

		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(querySQL);

			while (rs.next()) {
				// 存入符合的資料
				data.put("uid", rs.getString("uid"));
				data.put("name", rs.getString("name"));
				data.put("longitude", rs.getString("longitude"));
				data.put("latitude", rs.getString("latitude"));
				list.add(data);
			}
			jsonResult.put("event_participant", list);
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

	public JSONObject removeParticipant(String eid, String uid, String APIKey) {
		// 檢驗API Key是否被註冊
		try {
			Statement stmt = connection.createStatement();
			String SQL = "SELECT api_key FROM developer WHERE api_key ='"
			        + APIKey + "'";
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
		String querySQL = "DELETE FROM event_participant WHERE eid = '" + eid
		        + "' AND uid = '" + uid + "'";
		Statement stmt;

		try {
			stmt = connection.createStatement();
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
