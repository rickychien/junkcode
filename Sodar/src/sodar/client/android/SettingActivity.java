package sodar.client.android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

public class SettingActivity extends Activity {
	private LocalCache db;
	private String uid = "default";
	private String accessToken;
	private int open_notify = 0;
	private int notify_mode = 0;
	private int notify_range = 0;
	private int frequency = 0;
	private CheckBox checkbox;
	private Spinner[] spinner = new Spinner[3];
	private Intent intent;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initialize();
		findViews();
		setViews();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	
	public void initialize() {
		uid = this.getIntent().getExtras().getString("uid");
		accessToken = SettingActivity.this.getIntent().getExtras().getString("access_token");

		// 取得手機端資料庫記錄
		db = new LocalCache(this);

		Cursor cursor = db.getLocalCache(uid);
		int rowNum = cursor.getCount();
		cursor.moveToFirst();

		if (rowNum > 0) {
			open_notify = cursor.getInt(5);
			notify_mode = cursor.getInt(6);
			notify_range = cursor.getInt(7);
			frequency = cursor.getInt(8);
		}
	}

	public void findViews() {
		checkbox = (CheckBox) findViewById(R.id.open_notify_checkBox);
		spinner[0] = (Spinner) findViewById(R.id.notify_mode_spinner);
		spinner[1] = (Spinner) findViewById(R.id.notify_range_spinner);
		spinner[2] = (Spinner) findViewById(R.id.frequency_spinner);
	}

	public void setViews() {
		// 註冊元件Listener
		// 啟用通知功能CheckBox
		checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				intent = new Intent(SettingActivity.this, NotifyService.class);
				
				if (isChecked) {
					open_notify = 1;
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);
					startService(intent);
				}
				else {
					open_notify = 0;
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("access_token", accessToken);
					intent.putExtras(bundle);
					stopService(intent);
				}
				db.setSettingLocalCache(uid, open_notify, notify_mode, notify_range, frequency);
			}
		});

		// 通知模式Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "正常", "僅震動", "僅鈴聲" });
		// 設定下拉選單的樣式
		adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[0].setAdapter(adapter0);
		// 設定項目被選取之後的動作
		spinner[0].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				notify_mode = position;
				db.setSettingLocalCache(uid, open_notify, notify_mode, notify_range, frequency);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 通知範圍Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "5 公里", "10 公里", "30 公里",
		        "50 公里", "100 公里" });
		// 設定下拉選單的樣式
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[1].setAdapter(adapter1);
		// 設定項目被選取之後的動作
		spinner[1].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				notify_range = position;
				db.setSettingLocalCache(uid, open_notify, notify_mode, notify_range, frequency);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 更新頻率Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "1 分鐘", "5 分鐘", "10 分鐘", "20 分鐘",
		        "30 分鐘", });
		// 設定下拉選單的樣式
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[2].setAdapter(adapter2);
		// 設定項目被選取之後的動作
		spinner[2].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				frequency = position;
				db.setSettingLocalCache(uid, open_notify, notify_mode, notify_range, frequency);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 設定各個選項內容
		if (open_notify == 1) {
			checkbox.setChecked(true);
		}
		else {
			checkbox.setChecked(false);
		}

		spinner[0].setSelection(notify_mode);
		spinner[1].setSelection(notify_range);
		spinner[2].setSelection(frequency);
		
	}
}