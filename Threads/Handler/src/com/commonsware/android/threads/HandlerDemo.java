/***
	Copyright (c) 2008-2009 CommonsWare, LLC
	
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

package com.commonsware.android.threads;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class HandlerDemo extends Activity {
	ProgressBar bar;
	Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			bar.incrementProgressBy(5);
		}
	};
	boolean isRunning=false;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		bar=(ProgressBar)findViewById(R.id.progress);
	}
	
	public void onStart() {
		super.onStart();
		bar.setProgress(0);
		
		Thread background=new Thread(new Runnable() {
			public void run() {
				try {
					for (int i=0;i<20 && isRunning;i++) {
						Thread.sleep(1000);
						handler.sendMessage(handler.obtainMessage());
					}
				}
				catch (Throwable t) {
					// just end the background thread
				}
			}
		});
		
		isRunning=true;
		background.start();
	}
	
	public void onStop() {
		super.onStop();
		isRunning=false;
	}
}