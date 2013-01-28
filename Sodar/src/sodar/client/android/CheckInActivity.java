package sodar.client.android;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CheckInActivity extends Activity {
	private Button checkIn;
	private EditText description;
	private String descriptionContent;
	private String uid = "default";
	private double longitude;
	private double latitude;
	JSONObject jsonResult = new JSONObject();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkin);

		checkIn = (Button) findViewById(R.id.checkin);
		description = (EditText) findViewById(R.id.description);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(100);
		description.setFilters(FilterArray);
		// 取得登入後Facebook回傳的access token
		uid = CheckInActivity.this.getIntent().getExtras().getString("uid");
		longitude = Double.parseDouble(CheckInActivity.this.getIntent().getExtras().getString("longitude"));
		latitude = Double.parseDouble(CheckInActivity.this.getIntent().getExtras().getString("latitude"));

		checkIn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				descriptionContent = description.getText().toString();
				SendRequest sendRequest = new SendRequest();
				jsonResult = sendRequest.checkin(uid, longitude, latitude, descriptionContent);
				Intent intent = new Intent();
				intent.setClass(CheckInActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("checkin_result", jsonResult.toString());
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
