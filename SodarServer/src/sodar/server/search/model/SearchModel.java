package sodar.server.search.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;

public class SearchModel {
	protected final String driver = "com.mysql.jdbc.Driver";
	protected final String databaseURL = "jdbc:mysql://140.121.197.101:3306/sodar";
	protected final String user = "sodar";
	protected final String password = "sunshyboyz";
	protected Connection connection = null;
	protected ResultSet resultSet = null;

	public SearchModel() {
		// 初始化Database
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(databaseURL, user, password);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 處理FQL取得的資料
	public static class FqlUser {
		@Facebook(value = "uid2")
		String uid;

		@Override
		public String toString() {
			return String.format("%s", uid);
		}
	}

	public JSONObject search(String uidMe, String textQuery, String clientLongitude, String clientLatitude,
	        String accessToken, String APIKey) {
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

		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);

		// 搜尋Facebook好友，先取出好友名單
		String query = "SELECT uid2 FROM friend WHERE uid1 = me()";
		List<FqlUser> users = facebookClient.executeQuery(query, FqlUser.class);

		JSONObject jsonResult = new JSONObject();
		String querySQL = "SELECT f.*, MB.name FROM (SELECT uid, MAX(cid) as maxval FROM check_in_history GROUP BY uid)" +
				" AS x INNER JOIN check_in_history AS f INNER JOIN member AS MB ON f.uid = x.uid AND f.cid = x.maxval AND x.uid = MB.uid AND x.uid IN" +
				"(SELECT uid FROM member WHERE name LIKE '%" + textQuery + "%')";

		// 搜尋符合使用者名稱
		try {
			Map<String, String> data = new LinkedHashMap<String, String>();
			JSONArray list = new JSONArray();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(querySQL);
			int interaction = 0;
			
			while (rs.next()) {
				String uidHit = rs.getString("uid");

				// 計算使用者(點擊者)與被點擊者的互動程度
				try {
					querySQL = "SELECT * FROM interaction WHERE uid_me='" + uidMe + "' AND uid_hit='"
					        + uidHit + "'";
					Statement stmt1 = connection.createStatement();
					ResultSet rs1 = stmt1.executeQuery(querySQL);
					Timestamp now = new Timestamp(new Date().getTime()); // 現在時間
					Timestamp hitTime = null; // 點擊時間
					int hits = 0; // 點擊次數
					interaction = 0;

					// 如果使用者有對應到的被點擊者
					if (rs1.next()) {
						hitTime = rs1.getTimestamp("hit_time");
						hits = rs1.getInt("hits");
						long diffDay = ((now.getTime() - hitTime.getTime()) / 1000);

						// 互動演算法公式(最高機密)
						interaction = (int) (((double) hits * 1 / (1 + diffDay)) * 10000000);
					}

					// 自己的互動權值最高
					if (uidMe.equals(uidHit)) {
						interaction = Integer.MAX_VALUE;
					}

					rs1.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				data.put("uid", uidHit);
				data.put("name", rs.getString("name"));
				data.put("longitude", rs.getString("longitude"));
				data.put("latitude", rs.getString("latitude"));
				data.put("create_time", rs.getString("create_time"));
				data.put("display_time", rs.getString("display_time"));
				data.put("description", rs.getString("description"));
				data.put("interaction", "" + interaction);
				list.add(data);
			}

			jsonResult.put("check_in", list);
		}
		catch (Exception e) {
			e.printStackTrace();
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}

		// 搜尋符合事件標題名稱
		querySQL = "SELECT * FROM event_history WHERE title LIKE '%" + textQuery
		        + "%' AND CURDATE() <= DATE(end_time)";

		try {
			Map<String, String> data = new LinkedHashMap<String, String>();
			JSONArray list = new JSONArray();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(querySQL);

			while (rs.next()) {
				String eid = rs.getString("eid");

				// 計算事件參與者人數
				int participantCount = 0;
				int friendCount = 0;
				int joined = 0;

				try {
					String SQL = "SELECT uid FROM event_participant WHERE eid = '" + eid + "'";
					Statement stmt1 = connection.createStatement();
					ResultSet rs1 = stmt1.executeQuery(SQL);

					// 取出所有參與者
					while (rs1.next()) {
						String partUid = rs1.getString("uid");
						// 計算參與者人數
						participantCount++;

						// 取出好友名單，計算參與者中朋友人數
						for (FqlUser fbFriend : users) {
							if (fbFriend.uid.equals(partUid)) {
								friendCount++;
							}
						}

						// 自己參與，設定回傳是否參與變數
						if (partUid.equals(uidMe)) {
							joined = 1;
						}
					}
					rs1.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				data.put("eid", eid);
				data.put("uid", rs.getString("uid"));
				data.put("type", rs.getString("type"));
				data.put("title", rs.getString("title"));
				data.put("start_time", rs.getString("start_time"));
				data.put("end_time", rs.getString("end_time"));
				data.put("display_time", rs.getString("display_time"));
				data.put("description", rs.getString("description"));
				data.put("longitude", rs.getString("longitude"));
				data.put("latitude", rs.getString("latitude"));
				data.put("participants_count", "" + participantCount);
				data.put("friends_count", "" + friendCount);
				data.put("joined", "" + joined);
				list.add(data);
			}

			jsonResult.put("event", list);
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

	public JSONObject search(String uidMe, String sRange, String sRelationship, String sEventType,
	        String sEventTimeRange, String sClientLongitude, String sClientLatitude, String accessToken,
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

		JSONObject jsonResult = new JSONObject();
		String querySQL;
		int range;
		int relationship;
		int eventType;
		int eventTimeRange;
		double clientLongitude;
		double clientLatitude;
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);

		// 搜尋Facebook好友，先取出好友名單
		String query = "SELECT uid2 FROM friend WHERE uid1 = me()";
		List<FqlUser> users = facebookClient.executeQuery(query, FqlUser.class);

		Map<String, String> checkinData = new LinkedHashMap<String, String>();
		JSONArray checkinList = new JSONArray();

		// 向資料庫取得資料
		try {
			range = Integer.parseInt(sRange);
			relationship = Integer.parseInt(sRelationship);
			eventType = Integer.parseInt(sEventType);
			eventTimeRange = Integer.parseInt(sEventTimeRange);
			clientLongitude = Double.parseDouble(sClientLongitude);
			clientLatitude = Double.parseDouble(sClientLatitude);
			String cid;
			String uidHit;
			String name;
			String phone;
			String lastLoginTime;
			String longitude;
			String latitude;
			String create_time;
			String display_time;
			String description;
			double R = 6378.1; /* 地球半徑(KM) */
			double latitude1;
			double latitude2;
			double longitude1;
			double longitude2;
			double distance;
			int interaction = 0; /* 互動權值 */

			// 取得所有使用者的Check-In History
			querySQL = "SELECT C.*, MB.* FROM (SELECT uid, MAX(cid) AS MAXVAL FROM check_in_history GROUP BY uid) AS M "
			        + "INNER JOIN check_in_history AS C INNER JOIN member AS MB ON C.uid = M.uid AND C.cid = M.MAXVAL AND MB.uid = C.uid";

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(querySQL);

			// 取得每筆資料，並比對
			while (rs.next()) {
				cid = rs.getString("cid");
				uidHit = rs.getString("uid");
				name = rs.getString("name");
				phone = rs.getString("phone");
				lastLoginTime = rs.getString("last_login_time");
				longitude = rs.getString("longitude");
				latitude = rs.getString("latitude");
				create_time = rs.getString("create_time");
				display_time = rs.getString("display_time");
				description = rs.getString("description");

				// 比對符合的使用者，與Facebook做好友資料比對
				boolean found = false;

				if (relationship == 1) {
					for (FqlUser fbFriend : users) {
						if (fbFriend.uid.equals(uidHit)) {
							found = true;
						}
					}

					// 搜尋到自己
					if (uidMe.equals(uidHit)) {
						found = true;
					}

					if (found == false) {
						continue;
					}
				}

				// 比對符合的範圍的使用者，計算在地球中的範圍
				latitude1 = (Math.PI / 180) * Double.parseDouble(latitude);
				latitude2 = (Math.PI / 180) * clientLatitude;
				longitude1 = (Math.PI / 180) * Double.parseDouble(longitude);
				longitude2 = (Math.PI / 180) * clientLongitude;

				distance = Math.acos(Math.sin(latitude1) * Math.sin(latitude2) + Math.cos(latitude1)
				        * Math.cos(latitude2) * Math.cos(longitude2 - longitude1))
				        * R;

				if (distance > range) {
					continue;
				}

				// 計算使用者(點擊者)與被點擊者的互動程度
				try {
					querySQL = "SELECT * FROM interaction WHERE uid_me='" + uidMe + "' AND uid_hit='"
					        + uidHit + "'";
					Statement stmt1 = connection.createStatement();
					ResultSet rs1 = stmt1.executeQuery(querySQL);
					Timestamp now = new Timestamp(new Date().getTime()); // 現在時間
					Timestamp hitTime = null; // 點擊時間
					int hits = 0; // 點擊次數
					interaction = 0;

					// 如果使用者有對應到的被點擊者
					if (rs1.next()) {
						hitTime = rs1.getTimestamp("hit_time");
						hits = rs1.getInt("hits");
						long diffDay = ((now.getTime() - hitTime.getTime()) / 1000);

						// 互動演算法公式(最高機密)
						interaction = (int) (((double) hits * 1 / (1 + diffDay)) * 10000000);
					}

					// 自己的互動權值最高
					if (uidMe.equals(uidHit)) {
						interaction = Integer.MAX_VALUE;
					}

					rs1.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				// 存入符合的資料
				checkinData.put("cid", cid);
				checkinData.put("uid", uidHit);
				checkinData.put("name", name);
				checkinData.put("phone", phone);
				checkinData.put("last_login_time", lastLoginTime);
				checkinData.put("longitude", longitude);
				checkinData.put("latitude", latitude);
				checkinData.put("create_time", create_time);
				checkinData.put("display_time", display_time);
				checkinData.put("description", description);
				checkinData.put("interaction", "" + interaction);
				checkinList.add(checkinData);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}

		jsonResult.put("check_in", checkinList);

		Map<String, String> eventData = new LinkedHashMap<String, String>();
		JSONArray eventList = new JSONArray();

		// event_type 判斷使用者是否搜尋 event
		try {
			String eid;
			String uid;
			String type;
			String title;
			String start_time;
			String end_time;
			String display_time;
			String description;
			String longitude;
			String latitude;
			double R = 6378.1; /* 地球半徑(KM) */
			double latitude1;
			double latitude2;
			double longitude1;
			double longitude2;
			double distance;

			// 轉換eventTimeRange成timeInterval
			String[] timeInterval = { "1 DAY", "1 WEEK", "2 WEEK", "1 MONTH", "3 MONTH" };

			// event_type = 0 所有事件
			if (eventType == 0) {
				querySQL = "SELECT * FROM event_history WHERE DATE(start_time) <= DATE_ADD(CURDATE(), INTERVAL "
				        + timeInterval[eventTimeRange] + " ) && CURDATE() <= DATE(end_time)";
			}
			// event_type > 0 其餘事件類別
			else {
				querySQL = "SELECT * FROM event_history WHERE type='" + eventType
				        + "' && DATE(start_time) <= DATE_ADD(CURDATE(), INTERVAL "
				        + timeInterval[eventTimeRange] + " ) && CURDATE() <= DATE(end_time)";
			}

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(querySQL);

			// 取得每筆資料比對
			while (rs.next()) {
				eid = rs.getString("eid");
				uid = rs.getString("uid");
				type = rs.getString("type");
				title = rs.getString("title");
				start_time = rs.getString("start_time");
				end_time = rs.getString("end_time");
				display_time = rs.getString("display_time");
				description = rs.getString("description");
				longitude = rs.getString("longitude");
				latitude = rs.getString("latitude");

				// 篩選 事件發起人也是朋友
				if (relationship == 1) {
					boolean found = false;

					for (FqlUser fbFriend : users) {
						if (fbFriend.uid.equals(uid)) {
							found = true;
						}
					}

					if (uidMe.equals(uid)) {
						found = true;
					}

					if (found == false) {
						continue;
					}
				}

				// 比對符合的範圍的使用者，與GoogleMap做資料比對
				latitude1 = (Math.PI / 180) * Double.parseDouble(latitude);
				latitude2 = (Math.PI / 180) * clientLatitude;
				longitude1 = (Math.PI / 180) * Double.parseDouble(longitude);
				longitude2 = (Math.PI / 180) * clientLongitude;

				distance = Math.acos(Math.sin(latitude1) * Math.sin(latitude2) + Math.cos(latitude1)
				        * Math.cos(latitude2) * Math.cos(longitude2 - longitude1))
				        * R;

				if (distance > range) {
					continue;
				}

				// 計算事件參與者人數
				int participantCount = 0;
				int friendCount = 0;
				int joined = 0;

				try {
					String SQL = "SELECT uid FROM event_participant WHERE eid = '" + eid + "'";
					Statement stmt1 = connection.createStatement();
					ResultSet rs1 = stmt1.executeQuery(SQL);

					// 取出所有參與者
					while (rs1.next()) {
						String partUid = rs1.getString("uid");
						// 計算參與者人數
						participantCount++;

						// 取出好友名單，計算參與者中朋友人數
						for (FqlUser fbFriend : users) {
							if (fbFriend.uid.equals(partUid)) {
								friendCount++;
							}
						}

						// 自己參與，設定回傳是否參與變數
						if (partUid.equals(uidMe)) {
							joined = 1;
						}
					}
					rs1.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				// 存入符合的資料
				eventData.put("eid", eid);
				eventData.put("uid", uid);
				eventData.put("type", type);
				eventData.put("title", title);
				eventData.put("start_time", start_time);
				eventData.put("end_time", end_time);
				eventData.put("display_time", display_time);
				eventData.put("description", description);
				eventData.put("longitude", longitude);
				eventData.put("latitude", latitude);
				eventData.put("participants_count", "" + participantCount);
				eventData.put("friends_count", "" + friendCount);
				eventData.put("joined", "" + joined);
				eventList.add(eventData);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			JSONObject errorResult = new JSONObject();
			errorResult.put("status", "error: " + e.getMessage());
			return errorResult;
		}
		jsonResult.put("event", eventList);

		jsonResult.put("status", "success");
		return jsonResult;
	}
}
