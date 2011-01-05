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
	 
package com.commonsware.android.rotation.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class RotationAsync extends Activity {
	private ProgressBar bar=null;
	private RotationAwareTask task=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		bar=(ProgressBar)findViewById(R.id.progress);
		
		task=(RotationAwareTask)getLastNonConfigurationInstance();
		
		if (task==null) {
			task=new RotationAwareTask(this);
			task.execute();
		}
		else {
			task.attach(this);
			updateProgress(task.getProgress());
		
			if (task.getProgress()>=100) {
				markAsDone();
			}
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		task.detach();
		
		return(task);
	}
	
	void updateProgress(int progress) {
		bar.setProgress(progress);
	}
	
	void markAsDone() {
		findViewById(R.id.completed).setVisibility(View.VISIBLE);
	}
	
	static class RotationAwareTask extends AsyncTask<Void, Void, Void> {
		RotationAsync activity=null;
		int progress=0;
		
		RotationAwareTask(RotationAsync activity) {
			attach(activity);
		}
		
		@Override
		protected Void doInBackground(Void... unused) {
			for (int i=0;i<20;i++) {
				SystemClock.sleep(500);
				publishProgress();
			}
			
			return(null);
		}
		
		@Override
		protected void onProgressUpdate(Void... unused) {
			if (activity==null) {
				Log.w("RotationAsync", "onProgressUpdate() skipped -- no activity");
			}
			else {
				progress+=5;
				activity.updateProgress(progress);
			}
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			if (activity==null) {
				Log.w("RotationAsync", "onPostExecute() skipped -- no activity");
			}
			else {
				activity.markAsDone();
			}
		}
		
		void detach() {
			activity=null;
		}
		
		void attach(RotationAsync activity) {
			this.activity=activity;
		}
		
		int getProgress() {
			return(progress);
		}
	}
}
