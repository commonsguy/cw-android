/***
	Copyright (c) 2010-2011 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.commonsware.android.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DownloadDemo extends Activity {
	private DownloadManager mgr=null;
	private long lastDownload=-1L;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
																.detectAll()
																.penaltyLog()
																.build());
		
		mgr=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		registerReceiver(onComplete,
										 new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		registerReceiver(onNotificationClick,
										 new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(onComplete);
		unregisterReceiver(onNotificationClick);
	}
	
	public void startDownload(View v) {
		Uri uri=Uri.parse("http://commonsware.com/misc/test.mp4");
		
		Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
			.mkdirs();
		
		lastDownload=
			mgr.enqueue(new DownloadManager.Request(uri)
									.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
																					DownloadManager.Request.NETWORK_MOBILE)
									.setAllowedOverRoaming(false)
									.setTitle("Demo")
									.setDescription("Something useful. No, really.")
									.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
																										 "test.mp4"));
		
		v.setEnabled(false);
		findViewById(R.id.query).setEnabled(true);
	}
	
	public void queryStatus(View v) {
		Cursor c=mgr.query(new DownloadManager.Query().setFilterById(lastDownload));
		
		if (c==null) {
			Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show();
		}
		else {
			c.moveToFirst();
			
			Log.d(getClass().getName(), "COLUMN_ID: "+
						c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
			Log.d(getClass().getName(), "COLUMN_BYTES_DOWNLOADED_SO_FAR: "+
						c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
			Log.d(getClass().getName(), "COLUMN_LAST_MODIFIED_TIMESTAMP: "+
						c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
			Log.d(getClass().getName(), "COLUMN_LOCAL_URI: "+
						c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
			Log.d(getClass().getName(), "COLUMN_STATUS: "+
						c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
			Log.d(getClass().getName(), "COLUMN_REASON: "+
						c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));
			
			Toast.makeText(this, statusMessage(c), Toast.LENGTH_LONG).show();
		}
	}
	
	public void viewLog(View v) {
		startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
	}
	
	private String statusMessage(Cursor c) {
		String msg="???";
		
		switch(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
			case DownloadManager.STATUS_FAILED:
				msg="Download failed!";
				break;
			
			case DownloadManager.STATUS_PAUSED:
				msg="Download paused!";
				break;
			
			case DownloadManager.STATUS_PENDING:
				msg="Download pending!";
				break;
			
			case DownloadManager.STATUS_RUNNING:
				msg="Download in progress!";
				break;
			
			case DownloadManager.STATUS_SUCCESSFUL:
				msg="Download complete!";
				break;
			
			default:
				msg="Download is nowhere in sight";
				break;
		}
		
		return(msg);
	}
	
	BroadcastReceiver onComplete=new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			findViewById(R.id.start).setEnabled(true);
		}
	};
	
	BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
		}
	};
}
