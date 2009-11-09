/* Copyright (c) 2008-2009 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.commonsware.android.constants;

import android.content.*;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Provider extends ContentProvider {
	private static final String DATABASE_NAME="constants.db";
	private static final int CONSTANTS=1;
	private static final int CONSTANT_ID=2;
	private static final UriMatcher MATCHER;
	private static HashMap<String, String> CONSTANTS_LIST_PROJECTION;
	
	public static final class Constants implements BaseColumns {
		public static final Uri CONTENT_URI
				 =Uri.parse("content://com.commonsware.android.constants.Provider/constants");
		public static final String DEFAULT_SORT_ORDER="title";
		public static final String TITLE="title";
		public static final String VALUE="value";
	}

	static {
		MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
		MATCHER.addURI("com.commonsware.android.constants.Provider", "constants", CONSTANTS);
		MATCHER.addURI("com.commonsware.android.constants.Provider", "constants/#", CONSTANT_ID);

		CONSTANTS_LIST_PROJECTION=new HashMap<String, String>();
		CONSTANTS_LIST_PROJECTION.put(Provider.Constants._ID, Provider.Constants._ID);
		CONSTANTS_LIST_PROJECTION.put(Provider.Constants.TITLE, Provider.Constants.TITLE);
		CONSTANTS_LIST_PROJECTION.put(Provider.Constants.VALUE, Provider.Constants.VALUE);
	}

	public String getDbName() {
		return(DATABASE_NAME);
	}
	
	public int getDbVersion() {
		return(1);
	}
	
	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Cursor c=db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='constants'", null);
			
			try {
				if (c.getCount()==0) {
					db.execSQL("CREATE TABLE constants (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL);");
					
					ContentValues cv=new ContentValues();
					
					cv.put(Constants.TITLE, "Gravity, Death Star I");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_DEATH_STAR_I);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Earth");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_EARTH);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Jupiter");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_JUPITER);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Mars");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_MARS);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Mercury");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_MERCURY);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Moon");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_MOON);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Neptune");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_NEPTUNE);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Pluto");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_PLUTO);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Saturn");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_SATURN);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Sun");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_SUN);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, The Island");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_THE_ISLAND);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Uranus");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_URANUS);
					db.insert("constants", getNullColumnHack(), cv);
					
					cv.put(Constants.TITLE, "Gravity, Venus");
					cv.put(Constants.VALUE, SensorManager.GRAVITY_VENUS);
					db.insert("constants", getNullColumnHack(), cv);
				}
			}
			finally {
				c.close();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			android.util.Log.w("Constants", "Upgrading database, which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS constants");
			onCreate(db);
		}
	}
	
	private SQLiteDatabase db;
	
	@Override
	public boolean onCreate() {
		db=(new DatabaseHelper(getContext())).getWritableDatabase();
		
		return (db == null) ? false : true;
	}
	
	@Override
	public Cursor query(Uri url, String[] projection, String selection,
												String[] selectionArgs, String sort) {
		SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

		qb.setTables(getTableName());
		
		if (isCollectionUri(url)) {
			qb.setProjectionMap(getDefaultProjection());
		}
		else {
			qb.appendWhere(getIdColumnName()+"="+url.getPathSegments().get(1));
		}
		
		String orderBy;
		
		if (TextUtils.isEmpty(sort)) {
			orderBy=getDefaultSortOrder();
		} else {
			orderBy=sort;
		}

		Cursor c=qb.query(db, projection, selection, selectionArgs,
											null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	@Override
	public String getType(Uri url) {
		if (isCollectionUri(url)) {
			return(getCollectionType());
		}
		
		return(getSingleType());
	}

	@Override
	public Uri insert(Uri url, ContentValues initialValues) {
		long rowID;
		ContentValues values;
		
		if (initialValues!=null) {
			values=new ContentValues(initialValues);
		} else {
			values=new ContentValues();
		}

		if (!isCollectionUri(url)) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		
		for (String colName : getRequiredColumns()) {
			if (values.containsKey(colName) == false) {
				throw new IllegalArgumentException("Missing column: "+colName);
			}
		}

		populateDefaultValues(values);

		rowID=db.insert(getTableName(), getNullColumnHack(), values);
		if (rowID > 0) {
			Uri uri=ContentUris.withAppendedId(getContentUri(), rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}

		throw new SQLException("Failed to insert row into " + url);
	}

	@Override
	public int delete(Uri url, String where, String[] whereArgs) {
		int count;
		long rowId=0;
		
		if (isCollectionUri(url)) {
			count=db.delete(getTableName(), where, whereArgs);
		}
		else {
			String segment=url.getPathSegments().get(1);
			rowId=Long.parseLong(segment);
			count=db
					.delete(getTableName(), getIdColumnName()+"="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	@Override
	public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
		int count;
		
		if (isCollectionUri(url)) {
			count=db.update(getTableName(), values, where, whereArgs);
		}
		else {
			String segment=url.getPathSegments().get(1);
			count=db
					.update(getTableName(), values, getIdColumnName()+"="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		}
	
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}
	
	private boolean isCollectionUri(Uri url) {
		return(MATCHER.match(url)==CONSTANTS);
	}
	
	private HashMap<String, String> getDefaultProjection() {
		return(CONSTANTS_LIST_PROJECTION);
	}
	
	private String getTableName() {
		return("constants");
	}
	
	private String getIdColumnName() {
		return("_id");
	}
	
	private String getDefaultSortOrder() {
		return("title");
	}
	
	private String getCollectionType() {
		return("vnd.android.cursor.dir/vnd.commonsware.constant");
	}
	
	private String getSingleType() {
		return("vnd.android.cursor.item/vnd.commonsware.constant");
	}
	
	private String[] getRequiredColumns() {
		return(new String[] {"title"});
	}
	
	private void populateDefaultValues(ContentValues values) {
		Long now=Long.valueOf(System.currentTimeMillis());
		Resources r=Resources.getSystem();

		if (values.containsKey(Provider.Constants.VALUE) == false) {
			values.put(Provider.Constants.VALUE, 0.0f);
		}
	}
	
	private String getNullColumnHack() {
		return("title");
	}
	
	private Uri getContentUri() {
		return(Provider.Constants.CONTENT_URI);
	}
}