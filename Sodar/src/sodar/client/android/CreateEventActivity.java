package sodar.client.android;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

public class CreateEventActivity extends Activity {
	private TextView startDateDisplay;
	private Button pickStartDate;
	private int sYear;
	private int sMonth;
	private int sDay;
	static final int START_DATE_DIALOG_ID = 0;
	
	private TextView startTimeDisplay;
	private int sHour;
	private int sMin;
	static final int START_TIME_DIALOG_ID = 1;
	
	private TextView endDateDisplay;
	private Button pickEndDate;
	private int eYear;
	private int eMonth;
	private int eDay;
	static final int END_DATE_DIALOG_ID = 2;
	
	private TextView endTimeDisplay;
	private int eHour;
	private int eMin;
	static final int END_TIME_DIALOG_ID = 3;
	
	private int second;
	
	
	private String uid;
	private int event_type;
	private double longitude;
	private double latitude;
	
	private TextView placeDisplay;
	private EditText titleView;
	private EditText descriptionView;
	private String title;
	private String description;
	
	JSONObject jsonResult = new JSONObject();

	private Button confirmButton;
	private ConfirmButtonListener confirmButtonListener = new ConfirmButtonListener();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);
		
		placeDisplay = (TextView) findViewById(R.id.placedisplay);
		startDateDisplay = (TextView) findViewById(R.id.startdatedisplay);
		pickStartDate = (Button) findViewById(R.id.pickstartdate);
		startTimeDisplay = (TextView) findViewById(R.id.starttimedisplay);
		//pickStartTime = (Button) findViewById(R.id.pickstarttime);

		endDateDisplay = (TextView) findViewById(R.id.enddatedisplay);
		pickEndDate = (Button) findViewById(R.id.pickenddate);
		endTimeDisplay = (TextView) findViewById(R.id.endtimedisplay);
		//pickEndTime = (Button) findViewById(R.id.pickendtime);

		confirmButton = (Button) findViewById(R.id.create);
		confirmButton.setOnClickListener(confirmButtonListener);
		titleView = (EditText) findViewById(R.id.title);
		descriptionView = (EditText) findViewById(R.id.description);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(30);
		titleView.setFilters(FilterArray);
		InputFilter[] FilterArray2 = new InputFilter[1];
		FilterArray2[0] = new InputFilter.LengthFilter(100);
		descriptionView.setFilters(FilterArray2);
		uid = CreateEventActivity.this.getIntent().getExtras().getString("uid");
		longitude = Double.parseDouble(CreateEventActivity.this.getIntent().getExtras().getString("longitude"));
		latitude = Double.parseDouble(CreateEventActivity.this.getIntent().getExtras().getString("latitude"));
		placeDisplay.setText(CreateEventActivity.this.getIntent().getExtras().getString("address"));
		
	
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] {"緊急", "共同", "聚會"});
		// 設定下拉選單的樣式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		// 設定項目被選取之後的動作
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				event_type = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		pickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(START_DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		sYear = c.get(Calendar.YEAR);
		sMonth = c.get(Calendar.MONTH);
		sDay = c.get(Calendar.DAY_OF_MONTH);
		eYear = c.get(Calendar.YEAR);
		eMonth = c.get(Calendar.MONTH);
		eDay = c.get(Calendar.DAY_OF_MONTH);
		second = c.get(Calendar.SECOND);
		
		// display the current date (this method is below)
		updateDisplay1();

		// get the current time
		final Calendar t = Calendar.getInstance();
		sHour = t.get(Calendar.HOUR_OF_DAY);
		sMin = t.get(Calendar.MINUTE);
		eHour = t.get(Calendar.HOUR_OF_DAY);
		eMin = t.get(Calendar.MINUTE);
		
		
		// display the current time (this method is below)
		updateDisplay2();

		pickEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(END_DATE_DIALOG_ID);
			}
		});
		
		updateDisplay3();	
		updateDisplay4();
	}

	public class ConfirmButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			title = titleView.getText().toString();
			description = descriptionView.getText().toString();
			
			String start_time = new String(startDateDisplay.getText().toString()
					+ startTimeDisplay.getText().toString());
			
			String end_time = new String (endDateDisplay.getText().toString()
			+ endTimeDisplay.getText().toString());
			
			String display_time = new String(startDateDisplay.getText().toString()
			+ startTimeDisplay.getText().toString());
			
			SendRequest sendRequest = new SendRequest();
			jsonResult = sendRequest.createEvent(uid, event_type + 1, title, start_time, end_time, display_time, description, longitude, latitude);
			
			Intent intent = new Intent();
			intent.setClass(CreateEventActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("create_event_result", jsonResult.toString());
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			
		}

	}
	//讓日期顯示為雙位數
	private String format(int x){
		String s =""+x;
		if(s.length()==1){
			s = "0"+x;
		}
		return s;
	}
	// updates the date in the TextView
	private void updateDisplay1() {
		startDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(format(sYear)).append("-").append(format(sMonth + 1) ).append("-")
				.append(format(sDay)).append(" "));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, sYear, sMonth,
					sDay);
		case START_TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, sHour, sMin,
					false);
		case END_DATE_DIALOG_ID:
			return new DatePickerDialog(this, nDateSetListener, eYear, eMonth,
					eDay);
		case END_TIME_DIALOG_ID:
			return new TimePickerDialog(this, nTimeSetListener, eHour, eMin,
					false);
		}
		return null;
	}
	
	
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args){
	 switch(id)
	 {
	  case START_TIME_DIALOG_ID:
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
			sYear = year;
			sMonth = monthOfYear;
			sDay = dayOfMonth;
			if (year < Calendar.getInstance().get(Calendar.YEAR)||monthOfYear < Calendar.getInstance().get(Calendar.MONTH)||dayOfMonth < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
			updateDisplay1();
			showDialog(START_TIME_DIALOG_ID);
			}
		}
	};

	// updates the date in the TextView
	private void updateDisplay2() {
		second = Calendar.getInstance().get(Calendar.SECOND);
		startTimeDisplay.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(format(sHour)).append(":").append(format(sMin)).append(":").append(format(second)));
	}

	// the callback received when the user "sets" the date in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int min) {
			// TODO Auto-generated method stub
			sHour = hour;
			sMin = min;
			if (sYear == Calendar.getInstance().get(Calendar.YEAR)
					&& sMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& sDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && hour < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else if(sYear == Calendar.getInstance().get(Calendar.YEAR)
					&& sMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& sDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && min < Calendar.getInstance().get(Calendar.MINUTE)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
				updateDisplay2();
			}
		}
	};

	private void updateDisplay3() {
		endDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(format(eYear)).append("-").append(format(eMonth + 1)).append("-")
				.append(format(eDay)).append(" "));
	}

	private DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			eYear = year;
			eMonth = monthOfYear;
			eDay = dayOfMonth;
			if (year < Calendar.getInstance().get(Calendar.YEAR)||monthOfYear < Calendar.getInstance().get(Calendar.MONTH)||dayOfMonth < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
			updateDisplay3();
			showDialog(END_TIME_DIALOG_ID);
			}
		}
	};

	private void updateDisplay4() {
		second = Calendar.getInstance().get(Calendar.SECOND);
		endTimeDisplay.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(format(eHour)).append(":").append(format(eMin)).append(":").append(format(second)));
	}

	private TimePickerDialog.OnTimeSetListener nTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int min) {
			// TODO Auto-generated method stub
			eHour = hour;
			eMin = min;
			if (eYear == Calendar.getInstance().get(Calendar.YEAR)
					&& eMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& eDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && hour < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else if(eYear == Calendar.getInstance().get(Calendar.YEAR)
					&& eMonth == Calendar.getInstance().get(Calendar.MONTH)
					&& eDay == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && min < Calendar.getInstance().get(Calendar.MINUTE)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
				updateDisplay4();
			}
		}
	};
}
