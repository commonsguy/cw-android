/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	
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

package com.commonsware.android.notify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotifyDemo extends Activity {
	private static final int NOTIFY_ME_ID=1337;
	private int count=0;
	private NotificationManager mgr=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}
	
	public void notifyMe(View v) {
		Notification note=new Notification(R.drawable.stat_notify_chat,
																				"Status message!",
																				System.currentTimeMillis());
		PendingIntent i=PendingIntent.getActivity(this, 0,
														new Intent(this, NotifyMessage.class),
																							0);
		
		note.setLatestEventInfo(this, "Notification Title",
														"This is the notification message", i);
		note.number=++count;
		note.vibrate=new long[] {500L, 200L, 200L, 500L};
		note.flags|=Notification.FLAG_AUTO_CANCEL;
		
		mgr.notify(NOTIFY_ME_ID, note);
	}
	
	public void clearNotification(View v) {
		mgr.cancel(NOTIFY_ME_ID);
	}
}