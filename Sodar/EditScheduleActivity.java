package sodar.client.android;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditScheduleActivity extends Activity {
	private int pickPlaceRequestCode = 1;
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
	private Button changePlace;
	private EditText titleView;
	private EditText descriptionView;
	private Button confirm;
	private Button delete;
	
	private String uid;
	private String eid;
	private String address;
	private String longitude;
	private String latitude;

	JSONObject jsonResult = new JSONObject();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_schedule);

		startDateDisplay = (TextView) findViewById(R.id.startdatedisplay);
		pickStartDate = (Button) findViewById(R.id.pickstartdate);
		startTimeDisplay = (TextView) findViewById(R.id.starttimedisplay);

		endDateDisplay = (TextView) findViewById(R.id.enddatedisplay);
		pickEndDate = (Button) findViewById(R.id.pickenddate);
		endTimeDisplay = (TextView) findViewById(R.id.endtimedisplay);
		
		placeDisplay = (TextView) findViewById(R.id.placedisplay);
		changePlace = (Button) findViewById(R.id.pickplace);
		titleView = (EditText) findViewById(R.id.title);
		descriptionView = (EditText) findViewById(R.id.description);
		confirm = (Button) findViewById(R.id.confirm);
		delete = (Button) findViewById(R.id.delete);
		
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
		
		changePlace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(EditScheduleActivity.this, PickLocationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("longitude", longitude);
				bundle.putString("latitude", latitude);
				bundle.putString("type", "2");
				intent.putExtras(bundle);
				startActivityForResult(intent, pickPlaceRequestCode);
			}
		});
		
		// 刪除事件
		delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog alertDialog = getAlertDialog("Sodar","確定刪除？");
				alertDialog.show();
			}
		});
		
		confirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String title = titleView.getText().toString();
				String description = descriptionView.getText().toString();
				String start_time = new String(startDateDisplay.getText().toString()+ " " + startTimeDisplay.getText().toString());
				String end_time = new String(endDateDisplay.getText().toString() + " " + endTimeDisplay.getText().toString());
				String display_time = new String(startDateDisplay.getText().toString()+ " " + startTimeDisplay.getText().toString());
				
				SendRequest sendRequest = new SendRequest();
				JSONObject jsonResult = sendRequest.editSchedule(eid, uid, 4, title, start_time, end_time, display_time, description, longitude, latitude);

				try {
					Toast.makeText(EditScheduleActivity.this, jsonResult.getString("status"), Toast.LENGTH_SHORT).show();
					if (jsonResult.getString("status").equals("success")) {
						// 返回事件畫面
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("title", title);
						bundle.putString("start_time", start_time);
						bundle.putString("end_time", end_time);
						bundle.putString("description", description);
						bundle.putString("address", address);
						bundle.putString("type", "0");
						intent.putExtras(bundle);
						setResult(RESULT_OK, intent);
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		initialize();
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

	public void initialize() {
		String orig_startTime;
		String orig_endTime;
		String orig_description;
		String orig_title;
		
		uid = EditScheduleActivity.this.getIntent().getExtras().getString("uid");
		eid = EditScheduleActivity.this.getIntent().getExtras().getString("eid");
		
		orig_title = EditScheduleActivity.this.getIntent().getExtras().getString("orig_title");
		orig_startTime = EditScheduleActivity.this.getIntent().getExtras().getString("orig_startTime");
		orig_endTime = EditScheduleActivity.this.getIntent().getExtras().getString("orig_endTime");
		orig_description = EditScheduleActivity.this.getIntent().getExtras().getString("orig_description");
		longitude = EditScheduleActivity.this.getIntent().getExtras().getString("longitude");
		latitude = EditScheduleActivity.this.getIntent().getExtras().getString("latitude");
		address = EditScheduleActivity.this.getIntent().getExtras().getString("address");
		
		confirm.setText("確定修改");
		titleView.setText(orig_title);
		descriptionView.setText(orig_description);
		placeDisplay.setText(address);
		String S[] = orig_startTime.split("\\s+");
		startDateDisplay.setText(S[0]);
		startTimeDisplay.setText(S[1]);
		String E[] = orig_endTime.split("\\s+");
		endDateDisplay.setText(E[0]);
		endTimeDisplay.setText(E[1]);
	}
	
	private AlertDialog getAlertDialog(String title, String message){
        //產生一個Builder物件
        Builder builder = new AlertDialog.Builder(EditScheduleActivity.this);
        //設定Dialog的標題
        builder.setTitle(title);
        //設定Dialog的內容
        builder.setMessage(message);
        //設定Positive按鈕資料
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	SendRequest sendRequest = new SendRequest();
            	jsonResult = sendRequest.removeSchedule(eid);
            	
            	Intent intent = new Intent();
				intent.setClass(EditScheduleActivity.this, ShowScheduleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("longitude", longitude);
				bundle.putString("latitude", latitude);
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
	private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			syear = years;
			smonth = monthOfYear;
			sday = dayOfMonth;
			updateDisplay1();
			showDialog(START_TIME_DIALOG_ID);
		}
	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hours, int min) {
			shour = hours;
			sminute = min;
			updateDisplay2();
		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			eyear = years;
			emonth = monthOfYear;
			eday = dayOfMonth;
			updateDisplay3();
			showDialog(END_TIME_DIALOG_ID);
		}
	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hours, int min) {
			ehour = hours;
			eminute = min;
			updateDisplay4();
		}
	};
}
