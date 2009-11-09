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

package com.commonsware.android.inflation;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class InflationDemo extends Activity {
	private final static Map<Integer,String> MESSAGES;
	private boolean otherStuffVisible=false;
	private Menu theMenu=null;
	
	static {
		MESSAGES=new HashMap<Integer,String>();
		MESSAGES.put(R.id.close, "I don't wanna!");
		MESSAGES.put(R.id.no_icon, "Where's my picture?");
		MESSAGES.put(R.id.later, "Quoth the Maven, \"#4\"");
		MESSAGES.put(R.id.last, "i always get picked last...");
		MESSAGES.put(R.id.non_ghost, "I ain't 'fraid of no ghost!");
		MESSAGES.put(R.id.ghost, "Boo!");
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		theMenu=menu;
		
		new MenuInflater(getApplication())
																	.inflate(R.menu.sample, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.non_ghost) {
			otherStuffVisible=!otherStuffVisible;
			
			theMenu.setGroupVisible(R.id.other_stuff, otherStuffVisible);
		}
		
		String message=MESSAGES.get(item.getItemId());
		
		if (message!=null) {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			
			return(true);
		}
		
		return(super.onOptionsItemSelected(item));
	}
}
