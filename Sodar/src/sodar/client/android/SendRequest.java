package sodar.client.android;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class SendRequest {
	private String APIKey = "8095a7a1b6de064e8862d34219828df1";
	private String serverHttp = "http://140.121.197.101:88/SodarServer";
	//private String serverHttp = "http://10.0.2.2:8080/SodarServer";
	public JSONObject signin(String uid, String name, String sex, String aboutMe, String educationHistory, String workHistory, String accessToken) {
		HttpPost http_post = new HttpPost(serverHttp + "/SignIn");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("sex", sex));
			params.add(new BasicNameValuePair("aboutMe", aboutMe));
			params.add(new BasicNameValuePair("educationHistory", educationHistory));
			params.add(new BasicNameValuePair("workHistory", workHistory));
			params.add(new BasicNameValuePair("api_key", APIKey));
			http_post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse resultStatus = new DefaultHttpClient().execute(http_post);

			// 若狀態碼為 200 OK
			if (resultStatus.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK
			        && resultStatus != null) {
				// 在這裡把result這個字符串個給JSONObject。解讀裡面的內容。
				jsonResult = new JSONObject(EntityUtils.toString(resultStatus.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject search(String uid_me, String queryText, double clientLongitude, double clientLatitude,
	        String accessToken) {
		HttpPost httppost = new HttpPost(serverHttp + "/Search");
		JSONObject jsonResult = new JSONObject();

		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("uid_me", uid_me));
			params.add(new BasicNameValuePair("search_text", queryText));
			params.add(new BasicNameValuePair("client_longitude", String.valueOf(clientLongitude)));
			params.add(new BasicNameValuePair("client_latitude", String.valueOf(clientLatitude)));
			params.add(new BasicNameValuePair("access_token", accessToken));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				// 在這裡把result這個字符串個給JSONObject。解讀裡面的內容。
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonResult;
	}

	public JSONObject search(String uid_me, int range, int relationship, int eventType, int eventTimeRange,
	        double clientLongitude, double clientLatitude, String accessToken) {
		HttpPost httppost = new HttpPost(serverHttp + "/Search");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("uid_me", uid_me));
			params.add(new BasicNameValuePair("search_range", "" + range));
			params.add(new BasicNameValuePair("relationship", "" + relationship));
			params.add(new BasicNameValuePair("event_type", "" + eventType));
			params.add(new BasicNameValuePair("event_time_range", "" + eventTimeRange));
			params.add(new BasicNameValuePair("client_longitude", String.valueOf(clientLongitude)));
			params.add(new BasicNameValuePair("client_latitude", String.valueOf(clientLatitude)));
			params.add(new BasicNameValuePair("access_token", accessToken));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				// 在這裡把result這個字符串個給JSONObject。解讀裡面的內容。
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject checkin(String uid, double longitude, double latitude, String description) {
		HttpPost httppost = new HttpPost(serverHttp + "/CheckIn");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("longitude", "" + longitude));
			params.add(new BasicNameValuePair("latitude", "" + latitude));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}
	public JSONObject getcheckin(String uid) {
		HttpPost httppost = new HttpPost(serverHttp + "/CheckIn/getCheckin");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}
	public JSONObject schedule(String uid, int type, String title, String start_time, String end_time,
	        String display_time, String description, double longitude, double latitude) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/publishPlan");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(9);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("event_type", "" + type));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("start_time", start_time));
			params.add(new BasicNameValuePair("end_time", end_time));
			params.add(new BasicNameValuePair("display_time", display_time));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("longitude", "" + longitude));
			params.add(new BasicNameValuePair("latitude", "" + latitude));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject createEvent(String uid, int type, String title, String start_time, String end_time,
	        String display_time, String description, double longitude, double latitude) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/createEvent");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(9);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("event_type", "" + type));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("start_time", start_time));
			params.add(new BasicNameValuePair("end_time", end_time));
			params.add(new BasicNameValuePair("display_time", display_time));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("longitude", "" + longitude));
			params.add(new BasicNameValuePair("latitude", "" + latitude));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject getEvent(String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/getEvent");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject getEventParticipant(String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/getParticipant");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject addParticipant(String uid, String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/addParticipant");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject removeParticipant(String uid, String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/removeParticipant");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject editEvent(String eid, String uid, int type, String title, String start_time,
	        String end_time, String display_time, String description, String longitude, String latitude) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/editEvent");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("event_type", "" + type));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("start_time", start_time));
			params.add(new BasicNameValuePair("end_time", end_time));
			params.add(new BasicNameValuePair("display_time", display_time));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("longitude", longitude));
			params.add(new BasicNameValuePair("latitude", latitude));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject removeEvent(String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/removeEvent");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject getSchedule(String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/getPlan");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject removeSchedule(String eid) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/removePlan");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}

	public JSONObject editSchedule(String eid, String uid, int type, String title, String start_time,
	        String end_time, String display_time, String description, String longitude, String latitude) {
		HttpPost httppost = new HttpPost(serverHttp + "/Event/editPlan");
		JSONObject jsonResult = new JSONObject();
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(9);
			params.add(new BasicNameValuePair("eid", eid));
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("event_type", "" + type));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("start_time", start_time));
			params.add(new BasicNameValuePair("end_time", end_time));
			params.add(new BasicNameValuePair("display_time", display_time));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("longitude", longitude));
			params.add(new BasicNameValuePair("latitude", latitude));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				jsonResult = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;
	}
	
	public JSONObject getUserProfile(String uid_me,String uid_hit) {
		JSONObject userProfile = new JSONObject();
		
		
		// 向Sodar server取使用者資訊
		HttpPost httppost = new HttpPost(serverHttp + "/User/getUserProfile");
		
		
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("uid_me", uid_me));
			params.add(new BasicNameValuePair("uid_hit", uid_hit));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				// 在這裡把result這個字符串個給JSONObject。解讀裡面的內容。
				userProfile = new JSONObject(EntityUtils.toString(response.getEntity()));
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
	            userProfile.put("phone", "");
            }
            catch (JSONException e1) {
	            e1.printStackTrace();
            }
		}
		
		return userProfile;
	}
	
	public JSONObject editUserProfile(String uid, String name, String phone) {
		JSONObject userProfile = new JSONObject();
		
		// 向Sodar server取使用者資訊
		HttpPost httppost = new HttpPost(serverHttp + "/User/editUserProfile");
		
		try {
			// NameValuePair實現請求參數的封裝
			List<NameValuePair> params = new ArrayList<NameValuePair>(4);
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("phone", phone));
			params.add(new BasicNameValuePair("api_key", APIKey));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			// 發送請求並等待響應
			HttpResponse response = new DefaultHttpClient().execute(httppost);

			// 若狀態碼為 200 OK
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK && response != null) {
				// 在這裡把result這個字符串個給JSONObject。解讀裡面的內容。
				userProfile = new JSONObject(EntityUtils.toString(response.getEntity()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return userProfile;
	}
}
