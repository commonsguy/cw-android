/* Copyright (c) 2008-2011 -- CommonsWare, LLC

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
	 
package com.commonsware.android.preffrags;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class FragmentsDemo extends Activity {
	private static final int EDIT_ID = Menu.FIRST+2;
	
	private TextView checkbox=null;
	private TextView ringtone=null;
	private TextView checkbox2=null;
	private TextView text=null;
	private TextView list=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		checkbox=(TextView)findViewById(R.id.checkbox);
		ringtone=(TextView)findViewById(R.id.ringtone);
		checkbox2=(TextView)findViewById(R.id.checkbox2);
		text=(TextView)findViewById(R.id.text);
		list=(TextView)findViewById(R.id.list);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		SharedPreferences prefs=PreferenceManager
															.getDefaultSharedPreferences(this);
		
		checkbox.setText(new Boolean(prefs
																	.getBoolean("checkbox", false))
												.toString());
		ringtone.setText(prefs.getString("ringtone", "<unset>"));
		checkbox2.setText(new Boolean(prefs
																	.getBoolean("checkbox2", false))
												.toString());
		text.setText(prefs.getString("text", "<unset>"));
		list.setText(prefs.getString("list", "<unset>"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, EDIT_ID, Menu.NONE, "Edit Prefs")
				.setIcon(R.drawable.misc)
				.setAlphabeticShortcut('e');

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case EDIT_ID:
				startActivity(new Intent(this, EditPreferences.class));
				return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
}
