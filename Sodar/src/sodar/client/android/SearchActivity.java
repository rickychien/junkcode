package sodar.client.android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends Activity {
	private LocalCache db;
	private String uid = "default";
	private int range = 0;
	private int relationship = 0;
	private int eventType = 0;
	private int eventTimeRange = 0;
	private Spinner[] spinner = new Spinner[4];
	private ImageButton searchBarButton;
	private Button searchButton;
	private EditText searchEditText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
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
		uid = SearchActivity.this.getIntent().getExtras().getString("uid");

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
	}

	public void findViews() {
		spinner[0] = (Spinner) findViewById(R.id.search_range_spinner);
		spinner[1] = (Spinner) findViewById(R.id.relationship_spinner);
		spinner[2] = (Spinner) findViewById(R.id.event_type_spinner);
		spinner[3] = (Spinner) findViewById(R.id.event_time_range_spinner);
		searchBarButton = (ImageButton) findViewById(R.id.image_button1);
		searchButton = (Button) findViewById(R.id.button2);
		searchEditText = (EditText) findViewById(R.id.editText1);
	}

	public void setViews() {
		// 搜尋範圍Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		        new String[] { "5 公里", "10 公里", "30 公里", "50 公里", "100 公里" });
		// 設定下拉選單的樣式
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[0].setAdapter(adapter1);
		// 設定項目被選取之後的動作
		spinner[0].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				range = position;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 搜尋關係Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		        new String[] { "所有人", "僅限朋友" });
		// 設定下拉選單的樣式
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[1].setAdapter(adapter2);
		// 設定項目被選取之後的動作
		spinner[1].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				relationship = position;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 事件類別Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		        new String[] { "所有事件", "緊急", "共同", "聚會", "個人行程" });
		// 設定下拉選單的樣式
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[2].setAdapter(adapter3);
		// 設定項目被選取之後的動作
		spinner[2].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				eventType = position;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 事件時間Spinner
		// 建立一個ArrayAdapter物件，並放置下拉選單的內容
		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		        new String[] { "1 天內", "1 周內", "2 周內", "1 個月內", "3 個月內" });
		// 設定下拉選單的樣式
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner[3].setAdapter(adapter4);
		// 設定項目被選取之後的動作
		spinner[3].setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				eventTimeRange = position;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// 搜尋Bar Button
		searchBarButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				String searchText = searchEditText.getText().toString();
				if(searchText == null) {
					finish();
				}
				else {
					Intent intent = new Intent();
					intent.setClass(SearchActivity.this, MainActivity.class);
					Bundle data = new Bundle();
					data.putString("search_text", searchText);
					intent.putExtras(data);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});

		// 搜尋Button
		searchButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				if (!db.setSearchLocalCache(uid, range, relationship, eventType, eventTimeRange)) {
					Toast.makeText(SearchActivity.this, "資料庫出了點問題，請重新搜尋", Toast.LENGTH_SHORT).show();
				}
				else {
					Intent intent = new Intent();
					intent.setClass(SearchActivity.this, MainActivity.class);
					intent.putExtras(new Bundle());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});

		// 設定各個選項內容
		spinner[0].setSelection(range);
		spinner[1].setSelection(relationship);
		spinner[2].setSelection(eventType);
		spinner[3].setSelection(eventTimeRange);
	}
}