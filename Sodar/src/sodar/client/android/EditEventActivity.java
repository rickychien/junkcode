package sodar.client.android;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditEventActivity extends Activity {
	private int pickPlaceRequestCode = 1;
	private String uid;
	private String eid;
	private int event_type;
	private String title;
	
	private TextView placeDisplay;
	private Button pickPlace;
	private Spinner spinner;
	private String description;
	private String address;
	private String longitude;
	private String latitude;
	private EditText titleView;
	private EditText descriptionView;

	private TextView mDateDisplay;
	private Button mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int nYear;
	private int nMonth;
	private int nDay;
	private int csecond;
	static final int DATE_DIALOG_ID = 0;

	private TextView mTimeDisplay;
	private int mHour;
	private int mMin;
	private int nHour;
	private int nMin;
	static final int TIME_DIALOG_ID = 1;

	private TextView nDateDisplay;
	private Button nPickDate;
	static final int END_DATE_DIALOG_ID = 2;

	private TextView nTimeDisplay;
	static final int END_TIME_DIALOG_ID = 3;

	private Button confirmButton;
	private ConfirmButtonListener confirmButtonListener = new ConfirmButtonListener();
	private DeleteButtonListener deleteButtonListener = new DeleteButtonListener();
	private Button deleteButton;
	
	private JSONObject jsonResult = new JSONObject();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_event);
		spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "緊急", "共同", "聚會" });
		// 設定下拉選單的樣式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		// 設定項目被選取之後的動作
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				event_type = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		placeDisplay = (TextView) findViewById(R.id.placedisplay);
		pickPlace = (Button) findViewById(R.id.pickplace);
		mDateDisplay = (TextView) findViewById(R.id.startdatedisplay);
		mPickDate = (Button) findViewById(R.id.pickstartdate);
		mTimeDisplay = (TextView) findViewById(R.id.starttimedisplay);

		nDateDisplay = (TextView) findViewById(R.id.enddatedisplay);
		nPickDate = (Button) findViewById(R.id.pickenddate);
		nTimeDisplay = (TextView) findViewById(R.id.endtimedisplay);

		confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(confirmButtonListener);
		deleteButton = (Button) findViewById(R.id.delete);
		deleteButton.setOnClickListener(deleteButtonListener);
		titleView = (EditText) findViewById(R.id.title);
		descriptionView = (EditText) findViewById(R.id.description);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(30);
		titleView.setFilters(FilterArray);
		InputFilter[] FilterArray2 = new InputFilter[1];
		FilterArray2[0] = new InputFilter.LengthFilter(100);
		descriptionView.setFilters(FilterArray2);
		pickPlace.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(EditEventActivity.this, PickLocationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("longitude", longitude);
				bundle.putString("latitude", latitude);
				bundle.putString("type", "3");
				intent.putExtras(bundle);
				startActivityForResult(intent, pickPlaceRequestCode);
			}
		});

		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		nYear = c.get(Calendar.YEAR);
		nMonth = c.get(Calendar.MONTH);
		nDay = c.get(Calendar.DAY_OF_MONTH);
		csecond = c.get(Calendar.SECOND);
		
		// display the current date (this method is below)


		// get the current Time
		final Calendar t = Calendar.getInstance();
		mHour = t.get(Calendar.HOUR_OF_DAY);
		mMin = t.get(Calendar.MINUTE);
		nHour = t.get(Calendar.HOUR_OF_DAY);
		nMin = t.get(Calendar.MINUTE);
		// display the current date (this method is below)

		nPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(END_DATE_DIALOG_ID);
			}
		});


		initialize();

	}

	public void initialize() {
		String orig_startTime;
		String orig_endTime;
		String orig_description;
		int orig_event_type;
		uid = EditEventActivity.this.getIntent().getExtras().getString("uid");
		eid = EditEventActivity.this.getIntent().getExtras().getString("eid");
		longitude = EditEventActivity.this.getIntent().getExtras().getString("longitude");
		latitude = EditEventActivity.this.getIntent().getExtras().getString("latitude");
		address = EditEventActivity.this.getIntent().getExtras().getString("address");
		// 取出此事件的資料(直接從activity傳)
		title = EditEventActivity.this.getIntent().getExtras()
				.getString("orig_title");
		orig_event_type = Integer.parseInt(EditEventActivity.this.getIntent().getExtras()
				.getString("orig_event_type"));
		orig_startTime = EditEventActivity.this.getIntent().getExtras()
				.getString("orig_startTime");
		orig_endTime = EditEventActivity.this.getIntent().getExtras()
				.getString("orig_endTime");
		orig_description = EditEventActivity.this.getIntent().getExtras()
				.getString("orig_description");
		placeDisplay.setText(address);
		titleView.setText(title);
		spinner.setSelection(orig_event_type - 1, true);
		descriptionView.setText(orig_description);
		String S[] = orig_startTime.split("\\s+");
		mDateDisplay.setText(S[0]+" ");
		mTimeDisplay.setText(S[1]);
		String E[] = orig_endTime.split("\\s+");
		nDateDisplay.setText(E[0]+" ");
		nTimeDisplay.setText(E[1]);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 確認回傳的requestCode和resultCode
		if (requestCode == this.pickPlaceRequestCode && resultCode == RESULT_OK) {
			Bundle bundle = new Bundle();
			bundle = data.getExtras();
			longitude = bundle.getString("longitude");
			latitude = bundle.getString("latitude");
			placeDisplay.setText(bundle.getString("address"));
		}
	}

	public class ConfirmButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			title = titleView.getText().toString();
			description = descriptionView.getText().toString();
			String start_time = new String(mDateDisplay.getText().toString() + mTimeDisplay.getText().toString() );
			String end_time = new String(nDateDisplay.getText().toString() + nTimeDisplay.getText().toString() );
			String display_time = new String(mDateDisplay.getText().toString() + mTimeDisplay.getText().toString() );
			
			SendRequest sendRequest = new SendRequest();
			jsonResult = sendRequest.editEvent(eid, uid, event_type + 1, title, start_time, end_time, display_time, description, longitude, latitude);
			
			Intent intent = new Intent();
			intent.setClass(EditEventActivity.this, ShowEventActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("event_type", Integer.toString(event_type + 1));
			bundle.putString("title", title);
			bundle.putString("start_time", start_time);
			bundle.putString("end_time", end_time);
			bundle.putString("description", description);
			bundle.putString("type", "0");
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	public class DeleteButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog alertDialog = getAlertDialog("Sodar","確定刪除？");
			alertDialog.show();
		}
	}
	
	private AlertDialog getAlertDialog(String title, String message){
        //產生一個Builder物件
        Builder builder = new AlertDialog.Builder(EditEventActivity.this);
        //設定Dialog的標題
        builder.setTitle(title);
        //設定Dialog的內容
        builder.setMessage(message);
        //設定Positive按鈕資料
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	SendRequest sendRequest = new SendRequest();
            	jsonResult = sendRequest.removeEvent(eid);
            	
            	Intent intent = new Intent();
				intent.setClass(EditEventActivity.this, ShowEventActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("longitude", "" + longitude);
				bundle.putString("latitude", "" + latitude);
				bundle.putString("type", "1");
				bundle.putString("showevent_result", jsonResult.toString());
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
            }
        });
        
        //設定Negative按鈕資料
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //利用Builder物件建立AlertDialog
        return builder.create();
    }

	// 讓日期顯示為雙位數
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1) {
			s = "0" + x;
		}
		return s;
	}

	// updates the date in the TextView
	private void updateDisplay1() {
		mDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(format(mYear)).append("-").append(format(mMonth + 1))
				.append("-").append(format(mDay)).append(" "));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMin,
					false);
		case END_DATE_DIALOG_ID:
			return new DatePickerDialog(this, nDateSetListener, nYear, nMonth,
					mDay);
		case END_TIME_DIALOG_ID:
			return new TimePickerDialog(this, nTimeSetListener, nHour, nMin,
					false);
		}
		return null;
	}
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args){
		 switch(id)
		 {
		  case TIME_DIALOG_ID:
		    TimePickerDialog stimePicker = 
		                               (TimePickerDialog)dialog;
		    stimePicker.updateTime(
		        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
		        Calendar.getInstance().get(Calendar.MINUTE)
		                     );
		    break;
		  case END_TIME_DIALOG_ID:
			    TimePickerDialog etimePicker = 
			                               (TimePickerDialog)dialog;
			    etimePicker.updateTime(
			        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
			        Calendar.getInstance().get(Calendar.MINUTE)
			                     );
			    break;
		 }
		}
	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			if (year < Calendar.getInstance().get(Calendar.YEAR)
					|| monthOfYear < Calendar.getInstance().get(Calendar.MONTH)
					|| dayOfMonth < Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH)) {
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇",
						Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay1();
				showDialog(TIME_DIALOG_ID);
			}
		}
	};

	// updates the date in the TextView
	private void updateDisplay2() {
		csecond = Calendar.getInstance().get(Calendar.SECOND);
		mTimeDisplay.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(format(mHour)).append(":").append(format(mMin)).append(":").append(format(csecond)));
	}

	// the callback received when the user "sets" the date in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int min) {
			// TODO Auto-generated method stub
			mHour = hour;
			mMin = min;
			if (mYear == Calendar.getInstance().get(Calendar.YEAR)
					&& mMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& mDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) &&hour < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else if(mYear == Calendar.getInstance().get(Calendar.YEAR)
					&& mMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& mDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && min < Calendar.getInstance().get(Calendar.MINUTE)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
				updateDisplay2();
			}
		}
	};

	private void updateDisplay3() {
		nDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(format(nYear)).append("-").append(format(nMonth + 1))
				.append("-").append(format(nDay)).append(" "));
	}

	private DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			nYear = year;
			nMonth = monthOfYear;
			nDay = dayOfMonth;
			if (year < Calendar.getInstance().get(Calendar.YEAR)
					|| monthOfYear < Calendar.getInstance().get(Calendar.MONTH)
					|| dayOfMonth < Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH)) {
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇",
						Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay3();
				showDialog(END_TIME_DIALOG_ID);
			}
		}
	};

	private void updateDisplay4() {
		csecond = Calendar.getInstance().get(Calendar.SECOND);
		nTimeDisplay.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(format(nHour)).append(":").append(format(nMin)).append(":").append(format(csecond)));
	}

	private TimePickerDialog.OnTimeSetListener nTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int min) {
			// TODO Auto-generated method stub
			nHour = hour;
			nMin = min;
			if (nYear == Calendar.getInstance().get(Calendar.YEAR)
					&& nMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& nDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && hour < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else if(nYear == Calendar.getInstance().get(Calendar.YEAR)
					&& nMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& nDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && min < Calendar.getInstance().get(Calendar.MINUTE)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
				updateDisplay4();
			}
		}
	};
}
