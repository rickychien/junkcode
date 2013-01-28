package sodar.client.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class NotifyService extends Service {
	private Handler handler = new Handler();
	private LocalCache db;
	private String uid = "default";
	private String accessToken;
	private Cursor cursor;
	private double longitude = 121.45982;
	private double latitude = 25.08608;
	private SendRequest sendRequest = new SendRequest();
	private JSONArray friendList;
	private ArrayList<JSONObject> oriFriends = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> newFriends = new ArrayList<JSONObject>();
	private int notifyMode = 0;
	private int searchRange = 2;
	private int relationship = 1; // 通知好友 搜尋"僅限朋友"
	private int eventType = 0; // 通知事件 搜尋"無"
	private int eventTimeRange = 0; // 不搜尋事件 此參數不影響
	private int frequency = 0;
	private int[] selectRange = { 5, 10, 30, 50, 100 };
	private int[] time = { 5 * 1000, 5 * 60 * 1000, 10 * 60 * 1000, 20 * 60 * 1000, 30 * 60 * 1000 };
	private boolean first = true;
	private MyLocationListener myLocationListener;
	private MyLocationListener2 myLocationListener2;
	private LocationManager locationManager;
	int a = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		uid = intent.getExtras().getString("uid");
		accessToken = intent.getExtras().getString("access_token");
		
		// 初始化GPS服務
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		myLocationListener = new MyLocationListener();
		myLocationListener2 = new MyLocationListener2();
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
		}
		else {
			Toast.makeText(NotifyService.this, "網路連線或GPS定位有問題，請重新連線", Toast.LENGTH_LONG).show();
		}

		db = new LocalCache(this);

		cursor = db.getLocalCache(uid);
		int rowNum = cursor.getCount();

		cursor.moveToFirst();

		if (rowNum > 0) {
			notifyMode = cursor.getInt(6);
			searchRange = cursor.getInt(7);
			frequency = cursor.getInt(8);
		}
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, myLocationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, myLocationListener2);

		// 隔一段時間呼叫Notify Thread
		handler.postDelayed(notify, time[frequency]);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 關閉資料庫
		db.close();
		handler.removeCallbacks(notify);
	}
	
	class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
			if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
	
	class MyLocationListener2 implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	private Runnable notify = new Runnable() {
		@SuppressWarnings("unchecked")
		public void run() {
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			myLocationListener = new MyLocationListener();
			myLocationListener2 = new MyLocationListener2();
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (location != null) {
				longitude = location.getLongitude();
				latitude = location.getLatitude();
			}
			else {
				Toast.makeText(NotifyService.this, "網路連線或GPS定位有問題，請重新連線", Toast.LENGTH_LONG).show();
			}

			String names = "";
			ArrayList<JSONObject> friendResult = new ArrayList<JSONObject>();

			// 取得資料表列數
			int rowNum = cursor.getCount();
			// 將指標移至第一筆資料
			cursor.moveToFirst();
			if (rowNum > 0) {
				notifyMode = cursor.getInt(6);
				searchRange = cursor.getInt(7);
				frequency = cursor.getInt(8);
			}

			JSONObject jsonResult = sendRequest.search(uid, selectRange[searchRange], relationship,
			        eventType, eventTimeRange, longitude, latitude, accessToken);

			// 尋找新出現在附近的使用者
			try {
				friendList = jsonResult.getJSONArray("check_in");

				// 第一次進入Notify，取得第一批friends資料暫存
				if (first) {
					for (int i = 0; i < friendList.length(); i++) {
						oriFriends.add((JSONObject) friendList.get(i));
					}

					first = false;
				}
				// 第二次以後把取得的friends資料放入map做比較，把新"加入"的朋友找出來(不管退出的)
				else {
					newFriends.clear();
					friendResult.clear();

					for (int i = 0; i < friendList.length(); i++) {
						newFriends.add((JSONObject) friendList.get(i));
					}

					// 尋找新加入的朋友
					for (JSONObject newOne : newFriends) {
						String newUid = newOne.getString("uid");
						boolean isNew = true;

						for (JSONObject oriOne : oriFriends) {
							String oriUid = oriOne.getString("uid");
							if (oriUid.equals(newUid)) {
								isNew = false;
							}
						}

						if (isNew == true && !uid.equals(newUid)) {
							friendResult.add(newOne);

							if (names.equals("")) {
								names += newOne.getString("name");
							}
							else {
								names += ", " + newOne.getString("name");
							}
						}
					}

					oriFriends = (ArrayList<JSONObject>) newFriends.clone();
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}

			// 取得Notification服務
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// 設定當按下這個通知之後要執行的activity
			Intent notifyIntent = new Intent(NotifyService.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("access_token", accessToken);
			notifyIntent.putExtras(bundle);
			notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent appIntent = PendingIntent.getActivity(NotifyService.this, 0, notifyIntent, 0);
			Notification notification = new Notification();
			// 設定出現在狀態列的圖示
			notification.icon = R.drawable.icon;
			// 顯示在狀態列的文字
			notification.tickerText = "有朋友出現在你的附近";
			// 會有通知：正常、鈴聲、振動、light
			switch (notifyMode) {
				case 0:
					notification.defaults = Notification.DEFAULT_ALL;
					break;
				case 1:
					notification.defaults = Notification.DEFAULT_VIBRATE;
					break;
				case 2:
					notification.defaults = Notification.DEFAULT_SOUND;
					break;
			}

			// 設定通知的標題、內容
			if (friendResult.size() > 0) {
				notification.setLatestEventInfo(NotifyService.this, names + "正在靠近你", "共有"
				        + friendResult.size() + "位朋友出現在附近，查看哪些朋友", appIntent);
				// 送出Notification
				notificationManager.notify(0, notification);
			}

			// 隔一段時間呼叫自己(Notify Thread)
			handler.postDelayed(this, time[frequency]);
		}
	};

}
