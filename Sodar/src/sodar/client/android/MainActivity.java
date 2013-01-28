package sodar.client.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class MainActivity extends MapActivity {
	private final String APP_ID = "192393297482948";
	private Facebook mFacebook = new Facebook(APP_ID);
	private Button displayAll;
	private Button displayFriend;
	private Button displayEvent;
	private Button changeMode;
	private Button displayAllForList;
	private Button displayFriendForList;
	private Button displayEventForList;
	private Button changeModeForList;
	private ImageButton arrowLeft;
	private ImageButton arrowRight;
	private MapView mapView;
	private MapController mapController;
	private MyLocationOverlay myOverlay;
	private MarkerOverlay markOverlay;
	private MarkerOverlay2 markOverlay2;
	private ListView listResult;
	private SimpleAdapter sAdapter;
	private List<Drawable> eventPhotoForMap = new ArrayList<Drawable>();
	private List<Bitmap> eventPhotoForList = new ArrayList<Bitmap>();
	private JSONObject jsonResult = new JSONObject();
	private List<Map<String, String>> checkinResult = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> eventResult = new ArrayList<Map<String, String>>();
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private HashMap<String, Bitmap> userPicList = new HashMap<String, Bitmap>();
	private View mapMain;
	private View listMain;
	private String uid = "default";
	private String accessToken;
	private String searchText = null;
	private String checkInResultToString;
	private String scheduleResultToString;
	private String createEventResultToString;
	private String showEventResultToString;
	private String showScheduleResultToString;
	private LocalCache db;
	private int range = 0;
	private int searchRange = 1;
	private int[] selectRange = { 5, 10, 30, 50, 100 };
	private int relationship = 0;
	private int eventType = 0;
	private int eventTimeRange = 0;
	private int mainMode = 0;
	private int searchRequestCode = 0;
	private int createEventRequestCode = 1;
	private int checkInRequestCode = 2;
	private int scheduleRequestCode = 3;
	private int showEventRequestCode = 4;
	private int showScheduleRequestCode = 5;
	private int userActivityRequestCode = 6;
	private Double longitude = 121.45982;
	private Double latitude = 25.08608;
	private MyLocationListener myLocationListener;
	private MyLocationListener2 myLocationListener2;
	private LocationManager locationManager;
	private int useWhatLocation = 0;
	private ProgressDialog myDialog;
	private int position = 0;

	// private ScrollView sView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		mapMain = layoutInflater.inflate(R.layout.mapmain, null);
		listMain = layoutInflater.inflate(R.layout.listmain, null);

		setContentView(mapMain);

		displayAll = (Button) findViewById(R.id.display_all);
		displayFriend = (Button) findViewById(R.id.display_friend);
		displayEvent = (Button) findViewById(R.id.display_event);
		changeMode = (Button) findViewById(R.id.change_mode);

		mapView = (MapView) findViewById(R.id.mapview);
		arrowLeft = (ImageButton) findViewById(R.id.arrow_left);
		arrowRight = (ImageButton) findViewById(R.id.arrow_right);
		initialize();
		getJsonObject();
	}

	public void initialize() {
		// 取得登入後Facebook回傳的access token
		uid = MainActivity.this.getIntent().getExtras().getString("uid");
		accessToken = MainActivity.this.getIntent().getExtras().getString("access_token");

		// 初始化GPS服務
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		myLocationListener = new MyLocationListener();
		myLocationListener2 = new MyLocationListener2();		
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
		} else {
			Toast.makeText(MainActivity.this, "網路連線或GPS定位有問題，請重新連線", Toast.LENGTH_LONG).show();
		}

		eventPhotoForMap.add(getResources().getDrawable(R.drawable.murgent));
		eventPhotoForMap.add(getResources().getDrawable(R.drawable.mrequest));
		eventPhotoForMap.add(getResources().getDrawable(R.drawable.mmeeting));
		eventPhotoForMap.add(getResources().getDrawable(R.drawable.mschedule));

		eventPhotoForList.add(BitmapFactory.decodeResource(getResources(),R.drawable.murgent));
		eventPhotoForList.add(BitmapFactory.decodeResource(getResources(),R.drawable.mrequest));
		eventPhotoForList.add(BitmapFactory.decodeResource(getResources(),R.drawable.mmeeting));
		eventPhotoForList.add(BitmapFactory.decodeResource(getResources(),R.drawable.mschedule));
	}

	public void getJsonObject() {
		// 取得手機端資料庫記錄
		db = new LocalCache(this);

		Cursor cursor = db.getLocalCache(uid);
		int rowNum = cursor.getCount();
		cursor.moveToFirst();

		if (rowNum > 0) {
			range = cursor.getInt(1);
			relationship = cursor.getInt(2);
			eventType = cursor.getInt(3);
			eventTimeRange = cursor.getInt(4);
		}

		db.close();

		searchRange = selectRange[range];

		SendRequest sendRequest = new SendRequest();
		
		if (searchText != null) {
			jsonResult = sendRequest.search(uid, searchText, longitude, latitude, accessToken);
			mainMode = 1;
			setContentView(listMain);
			setListMain();
		}
		else {
			jsonResult = sendRequest.search(uid, searchRange, relationship, eventType, eventTimeRange, longitude, latitude, accessToken);
		}
		
		searchText = null;
		
		list.clear();
		checkinResult = getJSONCheckinContent(jsonResult);
		eventResult = getJSONEventContent(jsonResult);
		if(checkinResult.size() == 0 && eventResult.size() == 0) {
			HashMap<String, Object> userItem = new HashMap<String, Object>();
			userItem.put("info", "		   查無此結果");
			list.add(userItem);
		}
		
		for (int i = 0; i < checkinResult.size(); i++) {
			Bitmap userPic;
			HashMap<String, Object> userItem = new HashMap<String, Object>();
			if(userPicList.containsKey(checkinResult.get(i).get("uid"))) {
				userPic = userPicList.get(checkinResult.get(i).get("uid"));
			} else {
				userPic = GetUserPicture(checkinResult.get(i).get("uid"));
				userPicList.put(checkinResult.get(i).get("uid"), userPic);
			}
			if (userPic != null) {
				userItem.put("pic", userPic);
			}
			userItem.put("cid", checkinResult.get(i).get("cid"));
			userItem.put("info", checkinResult.get(i).get("name"));
			userItem.put("uid", checkinResult.get(i).get("uid"));
			userItem.put("longitude", checkinResult.get(i).get("longitude"));
			userItem.put("latitude", checkinResult.get(i).get("latitude"));
			userItem.put("start_time", checkinResult.get(i).get("create_time"));
			userItem.put("time_text", "發佈於");
			list.add(userItem);
		}

		for (int i = 0; i < eventResult.size(); i++) {
			HashMap<String, Object> eventItem = new HashMap<String, Object>();
			eventItem.put("eid", eventResult.get(i).get("eid"));
			if (eventResult.get(i).get("type").equals("1"))
				eventItem.put("pic", eventPhotoForList.get(0));
			else if (eventResult.get(i).get("type").equals("2"))
				eventItem.put("pic", eventPhotoForList.get(1));
			else if (eventResult.get(i).get("type").equals("3"))
				eventItem.put("pic", eventPhotoForList.get(2));
			else if (eventResult.get(i).get("type").equals("4"))
				eventItem.put("pic", eventPhotoForList.get(3));
			eventItem.put("info", eventResult.get(i).get("title"));
			eventItem.put("type", eventResult.get(i).get("type"));
			eventItem.put("start_time", eventResult.get(i).get("start_time"));
			eventItem.put("time_text", "開始於");
			if (!eventResult.get(i).get("type").equals("4"))
				eventItem.put("participants_count", "共 "
						+ eventResult.get(i).get("participants_count")
						+ " 人參與");
			list.add(eventItem);
		}
	}

	private static Comparator<Map<String, String>> CHECKIN_COMPARATOR = new Comparator<Map<String, String>>() {
		public int compare(Map<String, String> o1, Map<String, String> o2) {
			return Integer.parseInt(o2.get("interaction"))
					- Integer.parseInt(o1.get("interaction"));
		}
	};

	private static Comparator<Map<String, String>> EVENT_COMPARATOR = new Comparator<Map<String, String>>() {
		public int compare(Map<String, String> o1, Map<String, String> o2) {
			int join = Integer.parseInt(o1.get("joined"))
					- Integer.parseInt(o2.get("joined"));
			int friend_count = Integer.parseInt(o1.get("friends_count"))
					- Integer.parseInt(o2.get("friends_count"));
			int participants_count = Integer.parseInt(o1
					.get("participants_count"))
					- Integer.parseInt(o2.get("participants_count"));

			if (join > 0) {
				return -1;
			} else if (join < 0) {
				return 1;
			} else if (join == 0 && friend_count > 0) {
				return -1;
			} else if (join == 0 && friend_count < 0) {
				return 1;
			} else if (join == 0 && friend_count == 0 && participants_count > 0) {
				return -1;
			} else if (join == 0 && friend_count == 0 && participants_count < 0) {
				return 1;
			} else {
				return -1;
			}
		}
	};

	public List<Map<String, String>> getJSONCheckinContent(JSONObject jsonObject) {
		List<Map<String, String>> checkinResult = new ArrayList<Map<String, String>>();
		try {
			JSONArray checkinArray = jsonObject.getJSONArray("check_in");

			for (int i = 0; i < checkinArray.length(); i++) {

				// 取得jsonArray的物件轉成JSONObject
				JSONObject jsonItem = checkinArray.getJSONObject(i);

				// 將jsonItem內的name和type塞到Map中
				Map<String, String> item = new HashMap<String, String>();
				item.put("uid", jsonItem.getString("uid"));
				item.put("name", jsonItem.getString("name"));
				item.put("longitude", jsonItem.getString("longitude"));
				item.put("latitude", jsonItem.getString("latitude"));
				String create_time[] = jsonItem.getString("create_time").split(
						"\\.");
				item.put("create_time", create_time[0]);
				item.put("interaction", jsonItem.getString("interaction"));

				// 將item存入List
				checkinResult.add(item);
			}
			Collections.sort(checkinResult, CHECKIN_COMPARATOR);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return checkinResult;
	}

	public List<Map<String, String>> getJSONEventContent(JSONObject jsonObject) {
		List<Map<String, String>> eventResult = new ArrayList<Map<String, String>>();
		try {
			JSONArray eventArray = jsonObject.getJSONArray("event");

			for (int i = 0; i < eventArray.length(); i++) {

				// 取得jsonArray的物件轉成JSONObject
				JSONObject jsonItem = eventArray.getJSONObject(i);

				// 將jsonItem內的name和type塞到Map中
				Map<String, String> item = new HashMap<String, String>();
				item.put("eid", jsonItem.getString("eid"));
				item.put("title", jsonItem.getString("title"));
				item.put("type", jsonItem.getString("type"));
				item.put("longitude", jsonItem.getString("longitude"));
				item.put("latitude", jsonItem.getString("latitude"));
				String start_time[] = jsonItem.getString("start_time").split(
						"\\.");
				item.put("start_time", start_time[0]);
				item.put("joined", jsonItem.getString("joined"));
				item.put("friends_count", jsonItem.getString("friends_count"));
				item.put("participants_count",
						jsonItem.getString("participants_count"));

				// 將item存入List
				eventResult.add(item);
			}
			Collections.sort(eventResult, EVENT_COMPARATOR);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventResult;
	}

	public int doubleToInt(double temp) {
		return (int) (temp * 1000000);
	}

	public void checkGetLocation() {
		new AlertDialog.Builder(MainActivity.this).setTitle("Sodar").setMessage("您尚未開啟定位服務，要前往設定頁面啟動定位服務嗎？")
		.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "未開啟定位服務，無法使用本軟體", Toast.LENGTH_SHORT).show();
			}
		}).show();
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
				checkGetLocation();
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

	class MarkerOverlay extends BalloonItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		public MarkerOverlay(Drawable defaultMarker) {
			super(boundCenter(defaultMarker), mapView);
			items.clear();
			Bitmap userPic;
			for (int i = 0; i < checkinResult.size(); i++) {
				OverlayItem temp = new OverlayItem(new GeoPoint(doubleToInt(Double.parseDouble(checkinResult.get(i).get("latitude"))),
						doubleToInt(Double.parseDouble(checkinResult.get(i).get("longitude")))), checkinResult.get(i).get("name"), checkinResult.get(i).get("uid"));
				if(userPicList.containsKey(checkinResult.get(i).get("uid"))) {
					userPic = userPicList.get(checkinResult.get(i).get("uid"));
				} else {
					userPic = GetUserPicture(checkinResult.get(i).get("uid"));
					userPicList.put(checkinResult.get(i).get("uid"), userPic);
				}
				Drawable drawable = new BitmapDrawable(userPic);
				drawable.setBounds(-drawable.getIntrinsicWidth(), -drawable.getIntrinsicHeight(), 0, 0);
				boundCenterBottom(drawable);
				temp.setMarker(drawable);
				items.add(temp);
			}
			populate();
		}

		public void doPopulate() {
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return items.get(i);
		}

		@Override
		public int size() {
			return items.size();
		}

		@Override
		protected boolean onBalloonTap(int index, OverlayItem item) {
			String uid = items.get(index).getSnippet();
			String longitude = checkinResult.get(index).get("longitude");
			String latitude = checkinResult.get(index).get("latitude");

			Intent intent = new Intent();
			intent.setClass(MainActivity.this, UserActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("uid_me", MainActivity.this.uid);
			bundle.putString("longitude", longitude);
			bundle.putString("latitude", latitude);
			bundle.putString("access_token", accessToken);
			intent.putExtras(bundle);
			startActivityForResult(intent, userActivityRequestCode);

			return true;
		}
	}

	class MarkerOverlay2 extends BalloonItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();

		public MarkerOverlay2(Drawable defaultMarker) {
			super(boundCenter(defaultMarker), mapView);
			items.clear();
			for (int i = 0; i < eventResult.size(); i++) {
				OverlayItem temp = new OverlayItem(new GeoPoint(
						doubleToInt(Double.parseDouble(eventResult.get(i).get("latitude"))), doubleToInt(Double.parseDouble(eventResult.get(i).get("longitude")))), eventResult.get(i).get("title"), eventResult.get(i).get("eid") + " " + eventResult.get(i).get("type"));
				Drawable drawable = eventPhotoForMap.get(Integer
						.parseInt(eventResult.get(i).get("type")) - 1);
				drawable.setBounds(-drawable.getIntrinsicWidth(),
						-drawable.getIntrinsicHeight(), 0, 0);
				boundCenterBottom(drawable);
				temp.setMarker(drawable);
				items.add(temp);
			}
			populate();
		}

		public void doPopulate() {
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return items.get(i);
		}

		@Override
		public int size() {
			return items.size();
		}

		@Override
		protected boolean onBalloonTap(int index, OverlayItem item) {
			String temp[] = items.get(index).getSnippet().split("\\s+");
			String eid = temp[0];
			String eventType = temp[1];

			if (eventType.equals("4")) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ShowScheduleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("eid", eid);
				bundle.putString("access_token", accessToken);
				intent.putExtras(bundle);
				startActivityForResult(intent, showScheduleRequestCode);
			} else {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ShowEventActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("eid", eid);
				bundle.putString("access_token", accessToken);
				intent.putExtras(bundle);
				startActivityForResult(intent, showEventRequestCode);
			}
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "搜尋");
		menu.add(0, 1, 1, "發起事件");
		menu.add(0, 2, 2, "Check In");
		menu.add(0, 3, 3, "設定");
		menu.add(0, 4, 4, "重整");
		menu.add(0, 5, 5, "登出");

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// 依據itemId來判斷使用者點選哪一個item
		switch (item.getItemId()) {
		// 搜尋
		case 0:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SearchActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("access_token", accessToken);
			bundle.putString("longitude", "" + longitude);
			bundle.putString("latitude", "" + latitude);
			intent.putExtras(bundle);
			startActivityForResult(intent, searchRequestCode);
			break;

		// 發起事件
		case 1:
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, PickLocationActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("uid", uid);
			bundle2.putString("longitude", "" + longitude);
			bundle2.putString("latitude", "" + latitude);
			bundle2.putString("type", "1");
			intent2.putExtras(bundle2);
			startActivityForResult(intent2, createEventRequestCode);
			break;

		// CheckIn
		case 2:
			AlertDialog alertDialog = getAlertDialog("Check-In", "請選擇");
			alertDialog.show();
			break;

		// 設定
		case 3:
			Intent intent3 = new Intent();
			intent3.setClass(MainActivity.this, SettingActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putString("uid", uid);
			bundle3.putString("access_token", accessToken);
			intent3.putExtras(bundle3);
			startActivity(intent3);
			break;

		// 重整
		case 4:
			Thread action = null;
			action = new Thread() {
				public void run() {
					Looper.prepare();
					myDialog = ProgressDialog.show(MainActivity.this,"Sodar", "重整中..", true);
						Looper.loop();
					}
			};
			action.start();
			
			getJsonObject();
			mapView.setBuiltInZoomControls(true);
			if(useWhatLocation != 1) {
				mapView.getController().animateTo(new GeoPoint((int) (latitude * 1000000), (int) (longitude * 1000000)));
			}
			useWhatLocation = 0;
			mapView.getController().setZoom(15);

			if (mainMode == 0) {
				setMapMain();
			} else {
				setListMain();
			}
			myDialog.dismiss();
			break;

		// 登出
		case 5:
			AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
			asyncRunner.logout(this.getBaseContext(),
					new LogoutRequestListener());
			Intent intent4 = new Intent();
			intent4.setClass(MainActivity.this, SignActivity.class);
			Bundle bundle4 = new Bundle();
			intent4.putExtras(bundle4);
			startActivity(intent4);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class LogoutRequestListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub

		}

	}

	private AlertDialog getAlertDialog(String title, String message) {
		// 產生一個Builder物件
		Builder builder = new AlertDialog.Builder(MainActivity.this);
		// 設定Dialog的標題
		builder.setTitle(title);
		// 設定Dialog的內容
		builder.setMessage(message);
		// 設定Positive按鈕資料
		builder.setPositiveButton("Check-In",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								CheckInActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("uid", uid);
						bundle.putString("longitude", "" + longitude);
						bundle.putString("latitude", "" + latitude);
						intent.putExtras(bundle);
						startActivityForResult(intent, checkInRequestCode);
					}
				});
		// 設定Negative按鈕資料
		builder.setNegativeButton("個人行程",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								PickLocationActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("uid", uid);
						bundle.putString("longitude", "" + longitude);
						bundle.putString("latitude", "" + latitude);
						bundle.putString("type", "0");
						intent.putExtras(bundle);
						startActivityForResult(intent, scheduleRequestCode);
					}
				});
		// 利用Builder物件建立AlertDialog
		return builder.create();
	}

	private void setMapMain() {
		final List<Overlay> overlays = mapView.getOverlays();
		overlays.clear();
		myOverlay = new MyLocationOverlay(this, mapView);
		myOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.animateTo(myOverlay.getMyLocation());
			}
		});
		overlays.add(myOverlay);

		Drawable marker = eventPhotoForMap.get(0);
		markOverlay = new MarkerOverlay(marker);
		overlays.add(markOverlay);
		mapView.invalidate();

		Drawable marker2 = eventPhotoForMap.get(0);
		markOverlay2 = new MarkerOverlay2(marker2);
		overlays.add(markOverlay2);
		mapView.invalidate();

		displayAll.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				overlays.clear();
				markOverlay.doPopulate();
				markOverlay2.doPopulate();
				overlays.add(markOverlay);
				overlays.add(markOverlay2);
				mapView.invalidate();
			}
		});

		displayFriend.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				overlays.clear();
				markOverlay.doPopulate();
				overlays.add(markOverlay);
				mapView.invalidate();
			}
		});

		displayEvent.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				overlays.clear();
				markOverlay2.doPopulate();
				overlays.add(markOverlay2);
				mapView.invalidate();
			}
		});

		changeMode.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainMode = 1;
				setContentView(listMain);
				setListMain();
			}
		});

		arrowLeft.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});

		arrowRight.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
	}

	private void setListMain() {
		listResult = (ListView) findViewById(R.id.listview);
		displayAllForList = (Button) findViewById(R.id.display_all_list);
		displayFriendForList = (Button) findViewById(R.id.display_friend_list);
		displayEventForList = (Button) findViewById(R.id.display_event_list);
		changeModeForList = (Button) findViewById(R.id.change_mode_list);

		sAdapter = new SimpleAdapter(this, list, R.layout.listitem,
				new String[] { "pic", "info", "participants_count",
						"start_time", "time_text" }, new int[] {
						R.id.imageView, R.id.information,
						R.id.participants_count, R.id.start_time, R.id.text });

		sAdapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				} else {
					return false;
				}
			}
		});

		listResult.setAdapter(sAdapter);
		listResult.setTextFilterEnabled(true);
		listResult.setSelection(position);
		position = 0;
		
		listResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (list.get(position).get("uid") != null) {
					String uid = list.get(position).get("uid").toString();
					String longitude = list.get(position).get("longitude").toString();
					String latitude = list.get(position).get("latitude").toString();

					Intent intent = new Intent();
					intent.setClass(MainActivity.this, UserActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("uid_me", MainActivity.this.uid);
					bundle.putString("longitude", longitude);
					bundle.putString("latitude", latitude);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);
					startActivityForResult(intent, userActivityRequestCode);
				} else if (list.get(position).get("type") != null && list.get(position).get("type").toString().equals("4")) {
					String eid = list.get(position).get("eid").toString();

					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShowScheduleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("eid", eid);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);
					startActivityForResult(intent, showScheduleRequestCode);
				} else if (list.get(position).get("type") != null) {
					String eid = list.get(position).get("eid").toString();

					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShowEventActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("eid", eid);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);
					startActivityForResult(intent, showEventRequestCode);
				}
			}

		});

		displayAllForList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				list.clear();

				if(checkinResult.size() == 0 && eventResult.size() == 0) {
					HashMap<String, Object> userItem = new HashMap<String, Object>();
					userItem.put("info", "		   查無此結果");
					list.add(userItem);
				}
				
				for (int i = 0; i < checkinResult.size(); i++) {
					Bitmap userPic;
					HashMap<String, Object> userItem = new HashMap<String, Object>();
					if(userPicList.containsKey(checkinResult.get(i).get("uid"))) {
						userPic = userPicList.get(checkinResult.get(i).get("uid"));
					} else {
						userPic = GetUserPicture(checkinResult.get(i).get("uid"));
						userPicList.put(checkinResult.get(i).get("uid"), userPic);
					}
					if (userPic != null) {
						userItem.put("pic", userPic);
					}
					userItem.put("cid", checkinResult.get(i).get("cid"));
					userItem.put("info", checkinResult.get(i).get("name"));
					userItem.put("uid", checkinResult.get(i).get("uid"));
					userItem.put("longitude", checkinResult.get(i).get("longitude"));
					userItem.put("latitude", checkinResult.get(i).get("latitude"));
					userItem.put("start_time", checkinResult.get(i).get("create_time"));
					userItem.put("time_text", "發佈於");
					list.add(userItem);
				}
				for (int i = 0; i < eventResult.size(); i++) {
					HashMap<String, Object> eventItem = new HashMap<String, Object>();
					eventItem.put("eid", eventResult.get(i).get("eid"));
					if (eventResult.get(i).get("type").equals("1"))
						eventItem.put("pic", eventPhotoForList.get(0));
					else if (eventResult.get(i).get("type").equals("2"))
						eventItem.put("pic", eventPhotoForList.get(1));
					else if (eventResult.get(i).get("type").equals("3"))
						eventItem.put("pic", eventPhotoForList.get(2));
					else if (eventResult.get(i).get("type").equals("4"))
						eventItem.put("pic", eventPhotoForList.get(3));
					eventItem.put("info", eventResult.get(i).get("title"));
					eventItem.put("type", eventResult.get(i).get("type"));
					eventItem.put("start_time", eventResult.get(i).get("start_time"));
					eventItem.put("time_text", "開始於");
					if (!eventResult.get(i).get("type").equals("4"))
						eventItem.put("participants_count", "共 "
								+ eventResult.get(i).get("participants_count")
								+ " 人參與");
					list.add(eventItem);
				}
				sAdapter.notifyDataSetChanged();
			}
		});

		displayFriendForList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				list.clear();
				
				if(checkinResult.size() == 0) {
					HashMap<String, Object> userItem = new HashMap<String, Object>();
					userItem.put("info", "		   查無此結果");
					list.add(userItem);
				}
				
				for (int i = 0; i < checkinResult.size(); i++) {
					Bitmap userPic;
					HashMap<String, Object> userItem = new HashMap<String, Object>();
					if(userPicList.containsKey(checkinResult.get(i).get("uid"))) {
						userPic = userPicList.get(checkinResult.get(i).get("uid"));
					} else {
						userPic = GetUserPicture(checkinResult.get(i).get("uid"));
						userPicList.put(checkinResult.get(i).get("uid"), userPic);
					}
					if (userPic != null) {
						userItem.put("pic", userPic);
					}
					userItem.put("cid", checkinResult.get(i).get("cid"));
					userItem.put("info", checkinResult.get(i).get("name"));
					userItem.put("uid", checkinResult.get(i).get("uid"));
					userItem.put("longitude", checkinResult.get(i).get("longitude"));
					userItem.put("latitude", checkinResult.get(i).get("latitude"));
					userItem.put("start_time", checkinResult.get(i).get("create_time"));
					userItem.put("time_text", "發佈於");
					list.add(userItem);
				}
				sAdapter.notifyDataSetChanged();
			}
		});

		displayEventForList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				list.clear();
				
				if(eventResult.size() == 0) {
					HashMap<String, Object> userItem = new HashMap<String, Object>();
					userItem.put("info", "		   查無此結果");
					list.add(userItem);
				}
				
				for (int i = 0; i < eventResult.size(); i++) {
					HashMap<String, Object> eventItem = new HashMap<String, Object>();
					eventItem.put("eid", eventResult.get(i).get("eid"));
					if (eventResult.get(i).get("type").equals("1"))
						eventItem.put("pic", eventPhotoForList.get(0));
					else if (eventResult.get(i).get("type").equals("2"))
						eventItem.put("pic", eventPhotoForList.get(1));
					else if (eventResult.get(i).get("type").equals("3"))
						eventItem.put("pic", eventPhotoForList.get(2));
					else if (eventResult.get(i).get("type").equals("4"))
						eventItem.put("pic", eventPhotoForList.get(3));
					eventItem.put("info", eventResult.get(i).get("title"));
					eventItem.put("type", eventResult.get(i).get("type"));
					eventItem.put("start_time", eventResult.get(i).get("start_time"));
					eventItem.put("time_text", "開始於");
					if (!eventResult.get(i).get("type").equals("4"))
						eventItem.put("participants_count", "共 "
								+ eventResult.get(i).get("participants_count")
								+ " 人參與");
					list.add(eventItem);
				}
				sAdapter.notifyDataSetChanged();
			}
		});

		changeModeForList.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainMode = 0;
				setContentView(mapMain);
				setMapMain();
			}
		});
	}

	// 取得Facebook大頭貼
	public Bitmap GetUserPicture(String uid) {
		String pictureURL = null;
		Bitmap userPic;

		try {
			pictureURL = "http://graph.facebook.com/" + uid + "/picture?type=square";
			userPic = BitmapFactory.decodeStream(new URL(pictureURL).openStream());
			return userPic;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, myLocationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, myLocationListener2);

		mapView.setBuiltInZoomControls(true);
		if(useWhatLocation != 1) {
			mapView.getController().animateTo(new GeoPoint((int) (latitude * 1000000), (int) (longitude * 1000000)));
		}
		useWhatLocation = 0;
		mapView.getController().setZoom(15);

		if (mainMode == 0) {
			setMapMain();
		} else {
			setListMain();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(myLocationListener);
	}

	@Override
	protected void onStop() {
		myOverlay.disableMyLocation();
		super.onStop();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 確認回傳的requestCode和resultCode
		if (requestCode == this.searchRequestCode && resultCode == RESULT_OK) {
			searchText = data.getExtras().getString("search_text");
			getJsonObject();
		} else if (requestCode == this.createEventRequestCode && resultCode == RESULT_OK) {
			// 抓取CreateEventActivity傳過來的數值
			createEventResultToString = data.getExtras().getString("create_event_result");
			try {
				JSONObject jsonObject = new JSONObject(createEventResultToString);
				createEventResultToString = jsonObject.getString("status");
				if(createEventResultToString.equals("success")) {
					getJsonObject();
					String eid = jsonObject.getString("eid");
					for(int i = 0 ; i < list.size() ; i++) {
						if(list.get(i).get("eid") != null) {
							if(list.get(i).get("eid").toString().equals(eid)) {
								position = i;
								break;
							}
						}
					}
					String eventLongitude = jsonObject.getString("longitude");
					String eventLatitude = jsonObject.getString("latitude");
					useWhatLocation = 1;
					mapView.getController().animateTo(new GeoPoint((int) (Double.parseDouble(eventLatitude) * 1000000), (int) (Double.parseDouble(eventLongitude) * 1000000)));
				} else {
					Toast.makeText(getBaseContext(), "網路連線或伺服器發生錯誤，正在找一群猴子幫忙解決中", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (requestCode == this.checkInRequestCode && resultCode == RESULT_OK) {
			// 抓取CheckInActivity傳過來的數值
			checkInResultToString = data.getExtras().getString("checkin_result");
			try {
				JSONObject jsonObject = new JSONObject(checkInResultToString);
				checkInResultToString = jsonObject.getString("status");
				if(checkInResultToString.equals("success")) {
					getJsonObject();
					String cid = jsonObject.getString("cid");
					for(int i = 0 ; i < list.size() ; i++) {
						if(list.get(i).get("cid") != null) {
							if(list.get(i).get("cid").toString().equals(cid)) {
								position = i;
								break;
							}
						}
					}
					String checkInLongitude = jsonObject.getString("longitude");
					String checkInLatitude = jsonObject.getString("latitude");
					useWhatLocation = 1;
					mapView.getController().animateTo(new GeoPoint((int) (Double.parseDouble(checkInLatitude) * 1000000), (int) (Double.parseDouble(checkInLongitude) * 1000000)));
				} else {
					Toast.makeText(getBaseContext(), "網路連線或伺服器發生錯誤，正在找一群猴子幫忙解決中", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (requestCode == this.scheduleRequestCode && resultCode == RESULT_OK) {
			// 抓取ScheduleActivity傳過來的數值
			scheduleResultToString = data.getExtras().getString("schedule_result");
			try {
				JSONObject jsonObject = new JSONObject(scheduleResultToString);
				scheduleResultToString = jsonObject.getString("status");
				if(scheduleResultToString.equals("success")) {
					getJsonObject();
					String eid = jsonObject.getString("eid");
					for(int i = 0 ; i < list.size() ; i++) {
						if(list.get(i).get("eid") != null) {
							if(list.get(i).get("eid").toString().equals(eid)) {
								position = i;
								break;
							}
						}
					}
					String scheduleLongitude = jsonObject.getString("longitude");
					String scheduleLatitude = jsonObject.getString("latitude");
					useWhatLocation = 1;
					mapView.getController().animateTo(new GeoPoint((int) (Double.parseDouble(scheduleLatitude) * 1000000), (int) (Double.parseDouble(scheduleLongitude) * 1000000)));
				} else {
						Toast.makeText(getBaseContext(), "網路連線或伺服器發生錯誤，正在找一群猴子幫忙解決中", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (requestCode == this.showEventRequestCode && resultCode == RESULT_OK) {
			// 抓取ShowEventActivity傳過來的數值
			if(data.getExtras().containsKey("showevent_result")) {
				showEventResultToString = data.getExtras().getString("showevent_result");
				if(showEventResultToString.equals("{\"status\":\"success\"}")) {
					getJsonObject();
				}
			} else {
				useWhatLocation = 1;
				mapView.getController().animateTo(new GeoPoint((int) (data.getExtras().getDouble("latitude") * 1000000), (int) (data.getExtras().getDouble("longitude") * 1000000)));
				mainMode = 0;
				setContentView(mapMain);
				setMapMain();
			}
			
		} else if (requestCode == this.showScheduleRequestCode && resultCode == RESULT_OK) {
			// 抓取ShowScheduleActivity傳過來的數值
			if(data.getExtras().containsKey("showschedule_result")) {
				showScheduleResultToString = data.getExtras().getString("showschedule_result");
				if(showScheduleResultToString.equals("{\"status\":\"success\"}")) {
					getJsonObject();
				}
			} else {
				useWhatLocation = 1;
				mapView.getController().animateTo(new GeoPoint((int) (data.getExtras().getDouble("latitude") * 1000000), (int) (data.getExtras().getDouble("longitude") * 1000000)));
				mainMode = 0;
				setContentView(mapMain);
				setMapMain();
			}
		}  else if(requestCode == this.userActivityRequestCode && resultCode == RESULT_OK) {
				useWhatLocation = 1;
				mapView.getController().animateTo(new GeoPoint((int) (data.getExtras().getDouble("latitude") * 1000000), (int) (data.getExtras().getDouble("longitude") * 1000000)));
				mainMode = 0;
				setContentView(mapMain);
				setMapMain();
		}  else {
			Log.e("ActivityResult", "request code = " + requestCode + ", result code = " + resultCode);
		}
	}
}
