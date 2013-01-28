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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	private TextView startDateDisplay;
	private Button pickStartDate;
	private int syear;
	private int smonth;
	private int sday;
	static final int START_DATE_DIALOG_ID = 0;

	private TextView startTimeDisplay;
	private int shour;
	private int sminute;
	private int ssecond;
	static final int START_TIME_DIALOG_ID = 1;

	private TextView endDateDisplay;
	private Button pickEndDate;
	private int eyear;
	private int emonth;
	private int eday;
	static final int END_DATE_DIALOG_ID = 2;

	private TextView endTimeDisplay;
	private int ehour;
	private int eminute;
	private int esecond;
	static final int END_TIME_DIALOG_ID = 3;

	private TextView placeDisplay;
	private EditText title;
	private EditText description;
	private Button checkIn;
	private String uid = "default";
	private double longitude = 121.517081;
	private double latitude = 25.047924;
	private String titleContent;
	private String descriptionContent;
	private String startTime;
	private String endTime;
	JSONObject jsonResult = new JSONObject();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);

		startDateDisplay = (TextView) findViewById(R.id.startdatedisplay);
		pickStartDate = (Button) findViewById(R.id.pickstartdate);
		startTimeDisplay = (TextView) findViewById(R.id.starttimedisplay);

		endDateDisplay = (TextView) findViewById(R.id.enddatedisplay);
		pickEndDate = (Button) findViewById(R.id.pickenddate);
		endTimeDisplay = (TextView) findViewById(R.id.endtimedisplay);

		placeDisplay = (TextView) findViewById(R.id.placedisplay);
		title = (EditText) findViewById(R.id.title);
		description = (EditText) findViewById(R.id.description);
		checkIn = (Button) findViewById(R.id.checkin);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(30);
		title.setFilters(FilterArray);
		InputFilter[] FilterArray2 = new InputFilter[1];
		FilterArray2[0] = new InputFilter.LengthFilter(100);
		description.setFilters(FilterArray2);
		uid = ScheduleActivity.this.getIntent().getExtras().getString("uid");
		longitude = Double.parseDouble(ScheduleActivity.this.getIntent()
				.getExtras().getString("longitude"));
		latitude = Double.parseDouble(ScheduleActivity.this.getIntent()
				.getExtras().getString("latitude"));
		placeDisplay.setText(ScheduleActivity.this.getIntent().getExtras()
				.getString("address"));

		pickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(START_DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar calendarOfDate = Calendar.getInstance();
		syear = calendarOfDate.get(Calendar.YEAR);
		smonth = calendarOfDate.get(Calendar.MONTH);
		sday = calendarOfDate.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay1();

		// get the current time
		final Calendar calendarOfTime = Calendar.getInstance();
		shour = calendarOfTime.get(Calendar.HOUR_OF_DAY);
		sminute = calendarOfTime.get(Calendar.MINUTE);
		ssecond = calendarOfTime.get(Calendar.SECOND);

		// display the current date (this method is below)
		updateDisplay2();

		pickEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(END_DATE_DIALOG_ID);
			}
		});

		// get the current date
		final Calendar calendarOfDate2 = Calendar.getInstance();
		eyear = calendarOfDate2.get(Calendar.YEAR);
		emonth = calendarOfDate2.get(Calendar.MONTH);
		eday = calendarOfDate2.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay3();

		// get the current time
		final Calendar calendarOfTime2 = Calendar.getInstance();
		ehour = calendarOfTime2.get(Calendar.HOUR_OF_DAY);
		eminute = calendarOfTime2.get(Calendar.MINUTE);
		esecond = calendarOfTime2.get(Calendar.SECOND);

		// display the current date (this method is below)
		updateDisplay4();

		checkIn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startTime = startDateDisplay.getText().toString()
						+ startTimeDisplay.getText().toString();
				endTime = endDateDisplay.getText().toString()
						+ endTimeDisplay.getText().toString();
				titleContent = title.getText().toString();
				descriptionContent = description.getText().toString();
				SendRequest sendRequest = new SendRequest();
				jsonResult = sendRequest.schedule(uid, 4, titleContent,
						startTime, endTime, startTime, descriptionContent,
						longitude, latitude);
				Intent intent = new Intent();
				intent.setClass(ScheduleActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("schedule_result", jsonResult.toString());
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
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
		startDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(syear).append("-").append(format(smonth + 1))
				.append("-").append(format(sday)).append(" "));
	}

	// updates the time in the TextView
	private void updateDisplay2() {
		ssecond = Calendar.getInstance().get(Calendar.SECOND);
		startTimeDisplay.setText(new StringBuilder().append(format(shour))
				.append(":").append(format(sminute)).append(":")
				.append(format(ssecond)));
	}

	// updates the date in the TextView
	private void updateDisplay3() {
		endDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(eyear).append("-").append(format(emonth + 1))
				.append("-").append(format(eday)).append(" "));
	}

	// updates the time in the TextView
	private void updateDisplay4() {
		esecond = Calendar.getInstance().get(Calendar.SECOND);
		endTimeDisplay.setText(new StringBuilder().append(format(ehour))
				.append(":").append(format(eminute)).append(":")
				.append(format(esecond)));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DATE_DIALOG_ID:
			return new DatePickerDialog(this, startDateSetListener, syear,
					smonth, sday);
		case START_TIME_DIALOG_ID:
			return new TimePickerDialog(this, startTimeSetListener, shour,
					sminute, false);
		case END_DATE_DIALOG_ID:
			return new DatePickerDialog(this, endDateSetListener, eyear,
					emonth, eday);
		case END_TIME_DIALOG_ID:
			return new TimePickerDialog(this, endTimeSetListener, ehour,
					eminute, false);
		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		switch (id) {
		case START_TIME_DIALOG_ID:
			TimePickerDialog stimePicker = (TimePickerDialog) dialog;
			stimePicker.updateTime(
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar
							.getInstance().get(Calendar.MINUTE));
			break;
		case END_TIME_DIALOG_ID:
			TimePickerDialog etimePicker = (TimePickerDialog) dialog;
			etimePicker.updateTime(
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar
							.getInstance().get(Calendar.MINUTE));
			break;
		}
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			syear = years;
			smonth = monthOfYear;
			sday = dayOfMonth;
			if (years < Calendar.getInstance().get(Calendar.YEAR)
					|| monthOfYear < Calendar.getInstance().get(Calendar.MONTH)
					|| dayOfMonth < Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH)) {
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇",
						Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay1();
				showDialog(START_TIME_DIALOG_ID);
			}
		}
	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hours, int min) {
			shour = hours;
			sminute = min;
			if (syear == Calendar.getInstance().get(Calendar.YEAR)
					&& smonth == Calendar.getInstance().get(Calendar.MONTH)
					&& sday == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && hours < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else if(syear == Calendar.getInstance().get(Calendar.YEAR)
					&& smonth == Calendar.getInstance().get(Calendar.MONTH)
					&& sday == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && min < Calendar.getInstance().get(Calendar.MINUTE)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
				updateDisplay2();
			}
		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			eyear = years;
			emonth = monthOfYear;
			eday = dayOfMonth;
			if (years < Calendar.getInstance().get(Calendar.YEAR)
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

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hours, int min) {
			ehour = hours;
			eminute = min;
			if (eyear == Calendar.getInstance().get(Calendar.YEAR)
					&& emonth == Calendar.getInstance().get(Calendar.MONTH)
					&& eday == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && hours < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else if(eyear == Calendar.getInstance().get(Calendar.YEAR)
					&& emonth == Calendar.getInstance().get(Calendar.MONTH)
					&& eday == Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH) && min < Calendar.getInstance().get(Calendar.MINUTE)){
				Toast.makeText(getBaseContext(), "Oops，你輸入的時間有點古老噢！會導致搜尋不到！ 請重新選擇", Toast.LENGTH_SHORT).show();
			}
			else {
				updateDisplay4();
			}
		}
	};
}
