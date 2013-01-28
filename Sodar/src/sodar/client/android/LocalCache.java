package sodar.client.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalCache extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "localcache.db"; // 資料庫名稱
	private static final int DATABASE_VERSION = 2; // 資料庫版本
	private SQLiteDatabase db;
	private Cursor cursorResult;
	
	public LocalCache(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}

	public Cursor getLocalCache(String uid) {
		cursorResult = db.rawQuery("SELECT * FROM localcache WHERE uid = '" + uid + "'", null);

		if (cursorResult.getCount() == 0) {
			cursorResult.close();
			cursorResult = db.rawQuery("SELECT * FROM localcache WHERE uid = 'default'", null);
		}
		
		return cursorResult;
	}

	public boolean setSearchLocalCache(String uid, int range, int relationship, int eventType, int eventTimeRange) {
		cursorResult = db.rawQuery("SELECT * FROM localcache WHERE uid = '" + uid + "'", null);

		ContentValues data = new ContentValues();
		data.put("search_range", range);
		data.put("relationship", relationship);
		data.put("event_type", eventType);
		data.put("event_time_range", eventTimeRange);

		// 使用者資料不存在就插入新資料
		if (cursorResult.getCount() == 0) {
			data.put("uid", uid);
			data.put("open_notify", 1);
			data.put("notify_mode", 0);
			data.put("notify_range", 3);
			data.put("frequency", 0);

			if (db.insert("localcache", null, data) == -1) {
				return false;
			}
		}
		// 存在則update資料
		else {
			try {
				db.update("localcache", data, "uid = '" + uid + "'", null);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public boolean setSettingLocalCache(String uid, int open_notify, int notify_mode, int notify_range, int frequency) {
		cursorResult = db.rawQuery("SELECT * FROM localcache WHERE uid = '" + uid + "'", null);
		
		ContentValues data = new ContentValues();
		data.put("open_notify", open_notify);
		data.put("notify_mode", notify_mode);
		data.put("notify_range", notify_range);
		data.put("frequency", frequency);

		// 使用者資料不存在就插入新資料
		if (cursorResult.getCount() == 0) {
			data.put("uid", uid);
			data.put("search_range", 2);
			data.put("relationship", 1);
			data.put("event_type", 0);
			data.put("event_time_range", 3);

			if (db.insert("localcache", null, data) == -1) {
				return false;
			}
		}
		// 存在則update資料
		else {
			try {
				db.update("localcache", data, "uid = '" + uid + "'", null);
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public void close() {
		cursorResult.close();
		db.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE = "CREATE TABLE localcache (" + "uid VARCHAR PRIMARY KEY, "
		        + "search_range INTEGER NOT NULL, " + "relationship INTEGER NOT NULL, " + "event_type INTEGER NOT NULL, "
		        + "event_time_range INTEGER NOT NULL, " + "open_notify INTEGER NOT NULL, " + "notify_mode INTEGER NOT NULL, "
		        + "notify_range INTEGER NOT NULL, " + "frequency INTEGER NOT NULL );";

		// 建立資料表，詳情請參考SQL語法
		try {
			db.execSQL(DATABASE_CREATE_TABLE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// 新建資料庫時會新增預設資料
		ContentValues data = new ContentValues();
		data.put("uid", "default");
		data.put("search_range", 2);
		data.put("relationship", 1);
		data.put("event_type", 0);
		data.put("event_time_range", 3);
		data.put("open_notify", 0);
		data.put("notify_mode", 0);
		data.put("notify_range", 3);
		data.put("frequency", 0);
		db.insert("localcache", null, data);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
		db.execSQL("DROP TABLE IF EXISTS localcache"); // 刪除舊有的資料表
		onCreate(db);
	}
}
