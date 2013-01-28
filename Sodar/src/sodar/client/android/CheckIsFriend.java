package sodar.client.android;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;

import com.facebook.android.Util;

public class CheckIsFriend {
	public String check(String accessToken, String uid){
	Bundle b = new Bundle();
		b.putString("access_token", accessToken);
		b.putString("query", "SELECT uid1 FROM friend WHERE uid1 = '" + uid + "' AND uid2 = me()");
			String myResult;
			try {
				myResult = Util.openUrl(
						"https://api.facebook.com/method/fql.query&format=json",
						"GET", b);
				final JSONArray FriendOrNotJSON = new JSONArray(myResult);
				if(FriendOrNotJSON.length()!=0){

				}
				else {
					//«DªB¤Í 
					return "notfriend";
				}
			
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "isfriend";
			
	}
		
}
