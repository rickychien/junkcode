package sodar.client.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PickLocationActivity extends MapActivity {
	private MapView mapView;
	private EditText searchBox;
	private Button search;
	private Button confirm;
	private String uid = "default";
	private double longitude;
	private double latitude;
	private String address;
	private String type;
	private int createEventRequestCode = 1;
	private int scheduleRequestCode = 3;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picklocation);
        
        mapView = (MapView)findViewById(R.id.mapview);
        searchBox = (EditText)findViewById(R.id.search_box);
        search = (Button)findViewById(R.id.search_button);
        confirm = (Button)findViewById(R.id.comfirm);
        
        uid = PickLocationActivity.this.getIntent().getExtras().getString("uid");
        longitude = Double.parseDouble(PickLocationActivity.this.getIntent().getExtras().getString("longitude"));
		latitude = Double.parseDouble(PickLocationActivity.this.getIntent().getExtras().getString("latitude"));
        getAddress(longitude, latitude);
        Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
		type = PickLocationActivity.this.getIntent().getExtras().getString("type");
		
		mapView.setBuiltInZoomControls(true);
        mapView.getController().setCenter(new GeoPoint((int)(latitude * 1000000), (int)(longitude * 1000000)));
        mapView.getController().setZoom(15);
        
		Drawable marker=getResources().getDrawable(R.drawable.pushpin);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		
		final SitesOverlay site = new SitesOverlay(marker);
		mapView.getOverlays().add(site);
        
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	GeoPoint getGeoPoint = getGeoByAddress(searchBox.getText().toString());
            	mapView.getController().setCenter( getGeoPoint );
            	site.setNewPlace(getGeoPoint);
            	longitude = getGeoPoint.getLongitudeE6() /1E6;
	            latitude = getGeoPoint.getLatitudeE6() / 1E6;
            }
        });
        
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Bundle bundle = new Bundle();
            	bundle.putString("uid", uid);
            	bundle.putString("longitude", Double.toString(longitude));
            	bundle.putString("latitude", Double.toString(latitude));
            	bundle.putString("address", address);
            	
            	if(type.equals("0")) {
            		Intent intent = new Intent();
            		intent.setClass(PickLocationActivity.this, ScheduleActivity.class);
            		intent.putExtras(bundle);
            		startActivityForResult(intent, scheduleRequestCode);
            	}
            	else if(type.equals("1")) {
            		Intent intent2 = new Intent();
            		intent2.setClass(PickLocationActivity.this, CreateEventActivity.class);
            		intent2.putExtras(bundle);
            		startActivityForResult(intent2, createEventRequestCode);
            	}
            	else if(type.equals("2")) {
            		Intent intent3 = new Intent();
            		intent3.setClass(PickLocationActivity.this, EditScheduleActivity.class);
            		intent3.putExtras(bundle);
            		setResult(RESULT_OK, intent3);
            		finish();
            	}
            	else if(type.equals("3")) {
            		Intent intent4 = new Intent();
            		intent4.setClass(PickLocationActivity.this, EditEventActivity.class);
            		intent4.putExtras(bundle);
            		setResult(RESULT_OK, intent4);
            		finish();
            	}
            }
        });
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 確認回傳的requestCode和resultCode
		if (requestCode == this.scheduleRequestCode && resultCode == RESULT_OK) {
			// 抓取ScheduleActivity傳過來的數值
			setResult(RESULT_OK, data);
			finish();
		}
		else if (requestCode == this.createEventRequestCode && resultCode == RESULT_OK) {
			// 抓取CreateEventActivity傳過來的數值
			setResult(RESULT_OK, data);
			finish();
		}
    }
    
    @Override
	public void onResume() {
		super.onResume();
	}		
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	public void getAddress(double longitude, double latitude) {
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
        	List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
            	Address add = addresses.get(0);
            	address = add.getAddressLine(0) + ", " + add.getLocality();
            }
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	private GeoPoint getGeoByAddress(String searchAddress) {
		GeoPoint geoPoint = null; 
	    try {
	    	if( !searchAddress.equals("") ) { 
	    		Geocoder geocoder = new Geocoder(PickLocationActivity.this, Locale.getDefault());
	    		List<Address> lstAddress = geocoder.getFromLocationName(searchAddress, 1);
	    		if( !lstAddress.isEmpty() ) { 
	    			Address location = lstAddress.get(0); 
	    			double geoLatitude = location.getLatitude()*1E6; 
	    			double geoLongitude = location.getLongitude()*1E6; 
	    			getAddress(location.getLongitude(), location.getLatitude());
	    			geoPoint = new GeoPoint((int) geoLatitude, (int) geoLongitude); 
	    		} 
	    	} 
	    } 
	    catch (Exception e) {  
	      e.printStackTrace();  
	    } 
	    return geoPoint; 
	}
	
	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items=new ArrayList<OverlayItem>();
		private Drawable marker=null;
		private OverlayItem inDrag=null;
		private ImageView dragImage=null;
		private int xDragImageOffset=0;
		private int yDragImageOffset=0;
		private int xDragTouchOffset=0;
		private int yDragTouchOffset=0;
		
		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker=marker;
			
			dragImage=(ImageView)findViewById(R.id.drag);
			xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
			yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();
			
			items.add(0, new OverlayItem(new GeoPoint((int)(latitude * 1000000), (int)(longitude * 1000000)), "", ""));
			populate();
		}
		
		private void setNewPlace( GeoPoint geoPoint ) {
			items.set(0, new OverlayItem(geoPoint, "", ""));
			populate();
		}
		
		@Override
		protected OverlayItem createItem(int i) {
			return items.get(i);
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);
		}
 		
		@Override
		public int size() {
			return(items.size());
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			final int action=event.getAction();
			final int x=(int)event.getX();
			final int y=(int)event.getY();
			boolean result=false;
			
			if (action==MotionEvent.ACTION_DOWN) {
				for (OverlayItem item : items) {
					Point p=new Point(0,0);
					
					mapView.getProjection().toPixels(item.getPoint(), p);
					
					if (hitTest(item, marker, x-p.x, y-p.y)) {
						result=true;
						inDrag=item;
						items.remove(inDrag);
						populate();

						xDragTouchOffset=0;
						yDragTouchOffset=0;
						
						setDragImagePosition(p.x, p.y);
						dragImage.setVisibility(View.VISIBLE);

						xDragTouchOffset=x-p.x;
						yDragTouchOffset=y-p.y;
						
						break;
					}
				}
			}
			else if (action==MotionEvent.ACTION_MOVE && inDrag!=null) {
				setDragImagePosition(x, y);
				result=true;
			}
			else if (action==MotionEvent.ACTION_UP && inDrag!=null) {
				dragImage.setVisibility(View.GONE);
				
				GeoPoint pt=mapView.getProjection().fromPixels(x-xDragTouchOffset,y-yDragTouchOffset);
				OverlayItem toDrop=new OverlayItem(pt, inDrag.getTitle(), inDrag.getSnippet());
				
				items.add(toDrop);
				populate();
				
				GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
	            longitude = p.getLongitudeE6() /1E6;
	            latitude = p.getLatitudeE6() / 1E6;
	            
	            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
	            try {
	            	List<Address> addresses = geoCoder.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);
	                if (addresses != null && addresses.size() > 0) {
	                	Address add = addresses.get(0);
	                	address = add.getAddressLine(0) + ", " + add.getLocality();
	                }
	                Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
	            }
	            catch (IOException e) {
	            	e.printStackTrace();
	            }

				inDrag=null;
				result=true;
			}
			
			return(result || super.onTouchEvent(event, mapView));
		}
		
		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)dragImage.getLayoutParams();			
			lp.setMargins(x-xDragImageOffset-xDragTouchOffset, y-yDragImageOffset-yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
