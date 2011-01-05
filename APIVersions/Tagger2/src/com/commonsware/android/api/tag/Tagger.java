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

package com.commonsware.android.api.tag;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Date;

public class Tagger extends Activity {
	private static final String LOG_KEY="Tagger";
	private static Method _setTag=null;
	private static Method _getTag=null;
	
	static {
		int sdk=new Integer(Build.VERSION.SDK).intValue();
		
		if (sdk>=4) {
			try {
				_setTag=View.class.getMethod("setTag",
																		 new Class[] {Integer.TYPE,
																									 Object.class});
				_getTag=View.class.getMethod("getTag",
																		 new Class[] {Integer.TYPE});
			}
			catch (Throwable t) {
				Log.e(LOG_KEY, "Could not initialize 1.6 accessors", t);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TextView view=(TextView)findViewById(R.id.test);
		
		setTag(view, R.id.test, new Date());
		
		view.setText(getTag(view, R.id.test).toString());
	}
	
	public void setTag(View v, int key, Object value) {
		if (_setTag!=null) {
			try {
				_setTag.invoke(v, key, value);
			}
			catch (Throwable t) {
				Log.e(LOG_KEY, "Could not use 1.6 setTag()", t);
			}
		}
		else {
			HashMap<Integer, Object> meta=(HashMap<Integer, Object>)v.getTag();
			
			if (meta==null) {
				meta=new HashMap<Integer, Object>();
				v.setTag(meta);
			}
			
			meta.put(key, value);
		}
	}
	
	public Object getTag(View v, int key) {
		Object result=null;
		
		if (_getTag!=null) {
			try {
				result=_getTag.invoke(v, key);
			}
			catch (Throwable t) {
				Log.e(LOG_KEY, "Could not use 1.6 getTag()", t);
			}
		}
		else {
			HashMap<Integer, Object> meta=(HashMap<Integer, Object>)v.getTag();
			
			if (meta==null) {
				meta=new HashMap<Integer, Object>();
				v.setTag(meta);
			}
			
			result=meta.get(key);
		}
		
		return(result);
	}
}
