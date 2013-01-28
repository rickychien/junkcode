package ntou.cs.android.ChaoNanBai;

import java.util.List;

import ntou.cs.android.ChaoNanBai.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BaiBaiActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private SensorManager sensorManager2;
	private MySurfaceView view;
	private int orientation = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager2 = (SensorManager) getSystemService(SENSOR_SERVICE);

		view = new MySurfaceView(this);
		setContentView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		List<Sensor> sensors2 = sensorManager2
				.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0) {
			sensorManager.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_GAME);
		}
		if (sensors2.size() > 0) {
			sensorManager2.registerListener(this, sensors2.get(0),
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		// Canvas canvas=view.getHolder().lockCanvas();
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			view.onValueChanged(event.values, orientation);
		} else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			orientation = view.onOrientationChanged(event.values);
		}
		// canvas=view.getHolder().lockCanvas();
	}

	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
		private Bitmap bitmap, bitmap2, bitmap3;
		private int counter = 0;
		private int x, y, m, n, a, b;

		public MySurfaceView(Context context) {
			super(context);
			getHolder().addCallback(this);
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.god);
			bitmap2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.spring);
			bitmap3 = BitmapFactory.decodeResource(getResources(),
					R.drawable.spring2);
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			onValueChanged(new float[3], orientation);

		}

		public void surfaceCreated(SurfaceHolder holder) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}

		int onOrientationChanged(float[] values) {
			if (values[0] <= 190 && values[0] >= 170) {
				return 1;
			} else {
				return 0;
			}
		}

		Canvas onValueChanged(float[] values, int orientation) {

			float result = Math.max(Math.max(values[0], values[1]),
					Math.max(values[1], values[2]));
			Canvas canvas = getHolder().lockCanvas();
			x = (getWidth()) / 2 - 60;
			y = (getHeight()) / 2;
			m = (getWidth()) / 2 + 30;
			n = (getHeight()) / 2;
			a = (getWidth()) / 2 - 110;
			b = (getHeight()) / 2;
			if (canvas != null) {
				Paint paint = new Paint();
				Paint paint1 = new Paint();
				paint.setAntiAlias(true);
				paint1.setAntiAlias(true);
				paint.setColor(Color.RED);
				paint.setTextSize(72);
				paint1.setColor(Color.BLACK);
				paint1.setTextSize(24);
				canvas.drawColor(Color.YELLOW);
				String S = Integer.toString(counter);
				canvas.drawText("朝向南方  虔誠的拜三下 ", getWidth() / 10,
						getHeight() / 9, paint1);
				canvas.drawText("將會有好事發生喔!", getWidth() / 7, getHeight() / 6,
						paint1);
				canvas.drawText(S, getWidth() / 2 - 30, getHeight() / 3, paint);
				if (result > 18 && result < 20) {
					counter++;
				}
				if (counter % 3 == 0 && counter != 0 && orientation == 1) {
					canvas.drawBitmap(bitmap, x, y, paint);
					canvas.drawBitmap(bitmap2, m, n, paint);
					canvas.drawBitmap(bitmap3, a, b, paint);
				}
				if (counter == 16) {
					counter = 0;
				}
				getHolder().unlockCanvasAndPost(canvas);

			}
			return canvas;
		}
	}
}