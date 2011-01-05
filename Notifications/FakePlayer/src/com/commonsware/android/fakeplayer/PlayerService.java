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

package com.commonsware.android.fakeplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class PlayerService extends Service {
	public static final String EXTRA_PLAYLIST="EXTRA_PLAYLIST";
	public static final String EXTRA_SHUFFLE="EXTRA_SHUFFLE";
	private boolean isPlaying=false;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String playlist=intent.getStringExtra(EXTRA_PLAYLIST);
		boolean useShuffle=intent.getBooleanExtra(EXTRA_SHUFFLE, false);

		play(playlist, useShuffle);			
		
		return(START_NOT_STICKY);
	}
	
	@Override
	public void onDestroy() {
		stop();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return(null);
	}
	
	private void play(String playlist, boolean useShuffle) {
		if (!isPlaying) {
			Log.w(getClass().getName(), "Got to play()!");
			isPlaying=true;

			Notification note=new Notification(R.drawable.stat_notify_chat,
																					"Can you hear the music?",
																					System.currentTimeMillis());
			Intent i=new Intent(this, FakePlayer.class);
		
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
								 Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
			PendingIntent pi=PendingIntent.getActivity(this, 0,
																									i, 0);
			
			note.setLatestEventInfo(this, "Fake Player",
															"Now Playing: \"Ummmm, Nothing\"",
															pi);
			note.flags|=Notification.FLAG_NO_CLEAR;

			startForeground(1337, note);
		}
	}
	
	private void stop() {
		if (isPlaying) {
			Log.w(getClass().getName(), "Got to stop()!");
			isPlaying=false;
			stopForeground(true);
		}
	}
}
