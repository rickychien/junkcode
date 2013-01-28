package sodar.client.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class SignActivity extends Activity {
	/** Called when the activity is first created. */
	public static final String APP_ID = "192393297482948";
	private static final String[] PERMISSIONS = new String[] { "user_about_me", "user_birthday", "email",
	        "user_photos", "publish_stream", "read_friendlists", "read_stream", "offline_access" };
	private ImageButton mButton1;
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	private String uid;
	private String name;
	private String sex;
	private String aboutMe;
	private String educationHistory;
	private String workHistory;

	private String accessToken;
	private ProgressDialog myDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		mButton1 = (ImageButton) findViewById(R.id.button1);
		
		// 按下按鈕後，登入
		mButton1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 先取得此service
				ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				// 在取得相關資訊
				NetworkInfo networInfo = conManager.getActiveNetworkInfo();
				// 判斷是否有網路
				if (networInfo == null || !networInfo.isAvailable()) {
					Toast.makeText(SignActivity.this, "無法取得網路服務", Toast.LENGTH_LONG).show();
				}
				
				if (mFacebook.isSessionValid()) {
					Intent intent = new Intent();
					intent.setClass(SignActivity.this, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);

				}
				else {
					mFacebook.authorize(SignActivity.this, PERMISSIONS, new LoginDialogListener());
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	public class GetUserProfilelistener implements com.facebook.android.AsyncFacebookRunner.RequestListener {
		public void onComplete(String response, Object state) {
			accessToken = mFacebook.getAccessToken();
			try {
				// 先解析接收到的fb資訊
				final JSONObject json = new JSONObject(response);
				uid = json.getString("id");
				Log.d("token", accessToken);
				JSONObject userProfile = new JSONObject();
				Bundle param = new Bundle();
				param.putString("access_token", accessToken);
				param.putString("query",
				        "SELECT name, pic_square, sex, about_me, education_history, work_history, email FROM user WHERE uid = '"
				                + uid + "'");

				try {
					String result = Util.openUrl("https://api.facebook.com/method/fql.query&format=json",
					        "GET", param);
					JSONArray userProfileArray = new JSONArray(result);
					userProfile = userProfileArray.optJSONObject(0);
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				name = userProfile.getString("name");

				if ((sex = userProfile.getString("sex")).equals("male")) {
					sex = "男";
				}
				else {
					sex = "女";
				}

				aboutMe = userProfile.getString("about_me");

				if (aboutMe.equals("null")) {
					aboutMe = "";
				}

				String eduString = userProfile.getString("education_history");
				if (!eduString.equals("null")) {
					JSONArray eduArray = new JSONArray(eduString);
					if (!eduArray.isNull(0)) {
						JSONObject eduObj = eduArray.getJSONObject(0);
						educationHistory = eduObj.getString("name");
						if (!eduObj.getJSONArray("concentrations").isNull(0)) {
							educationHistory += " " + eduObj.getJSONArray("concentrations").getString(0);
						}
					}
				}

				String workString = userProfile.getString("work_history");
				if (!workString.equals("null")) {
					JSONArray workArray = new JSONArray(workString);
					if (!workArray.isNull(0)) {
						JSONObject workObj = workArray.getJSONObject(0);
						workHistory = "在 " + workObj.getString("company_name") + " 上班";
					}
				}
			}
			catch (JSONException e) {
				Log.w("Facebook", "JSON Error in response");
				Toast.makeText(getBaseContext(), "Facebook發生了點問題，請稍後再登入", Toast.LENGTH_SHORT).show();
			}

			// 發送登入Sodar Server Request
			JSONObject jsonResult = new SendRequest().signin(uid, name, sex, aboutMe, educationHistory,
			        workHistory, accessToken);

			LocalCache db = new LocalCache(SignActivity.this);
			Cursor cursor = db.getLocalCache(uid);
			int notify = 0;

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				notify = cursor.getInt(5);
			}

			// 判斷使用者設定值並啟動通知服務
			if (notify == 1) {
				Intent intent = new Intent();
				intent = new Intent(SignActivity.this, NotifyService.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("access_token", accessToken);
				intent.putExtras(bundle);
				startService(intent);

			}

			db.close();

			// 若成功登入，連結進主畫面
			try {
				if (jsonResult.getString("status").equals("success")) {
					// 開啟MainActivity主畫面
					Intent intent = new Intent();
					intent.setClass(SignActivity.this, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);
					startActivity(intent);
					myDialog.dismiss();
					finish();
				}
				// 若失敗
				else {
					Log.e("sodar_server_error: ", jsonResult.toString());
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(SignActivity.this, "網路連線或伺服器發生錯誤，正在找一群猴子幫忙解決中", Toast.LENGTH_SHORT).show();
			}
		}

		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "Facebook has some problem...", Toast.LENGTH_SHORT);
		}
	}

	/**
	 * Listener for login dialog completion status
	 */
	private final class LoginDialogListener implements com.facebook.android.Facebook.DialogListener {

		/**
		 * Called when the dialog has completed successfully
		 */
		public void onComplete(Bundle values) {
			// get user's profiles
			mAsyncRunner.request("me/", new GetUserProfilelistener());
			Thread action = null;
			try {
				action = new Thread() {
					public void run() {
						Looper.prepare();
						myDialog = ProgressDialog.show(SignActivity.this, "Sodar", "登入中..", true);
						Looper.loop();
					}
				};
				action.start();
			}
			catch (Exception e) {

			}
			finally {

			}

		}

		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}

		public void onError(DialogError e) {
			// TODO Auto-generated method stub

		}

		public void onCancel() {
			// Process cancel message
			Log.d("FB Sample App", "LoginDialogListener.onCancel()");
		}
	}
}