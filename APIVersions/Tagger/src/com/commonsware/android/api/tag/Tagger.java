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

package com.commonsware.android.api.tag;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Date;

public class Tagger extends Activity {
	private static final String LOG_KEY="Tagger";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TextView view=(TextView)findViewById(R.id.test);
		
		setTag(view, R.id.test, new Date());
		
		view.setText(getTag(view, R.id.test).toString());
	}
	
	public void setTag(View v, int key, Object value) {
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.DONUT) {
			v.setTag(key, value);
		}
		else {
			HashMap<Integer, Object> meta=(HashMap<Integer, Object>)v.getTag();
			
			if (meta==null) {
				meta=new HashMap<Integer, Object>();
			}
			
			meta.put(key, value);
		}
	}
	
	public Object getTag(View v, int key) {
		Object result=null;
		
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.DONUT) {
			result=v.getTag(key);
		}
		else {
			HashMap<Integer, Object> meta=(HashMap<Integer, Object>)v.getTag();
			
			if (meta==null) {
				meta=new HashMap<Integer, Object>();
			}
			
			result=meta.get(key);
		}
		
		return(result);
	}
}
