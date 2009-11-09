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

package com.commonsware.android.menus;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MenuDemo extends ListActivity {
	TextView selection;
	String[] items={"lorem", "ipsum", "dolor", "sit", "amet",
					"consectetuer", "adipiscing", "elit", "morbi", "vel",
					"ligula", "vitae", "arcu", "aliquet", "mollis",
					"etiam", "vel", "erat", "placerat", "ante",
					"porttitor", "sodales", "pellentesque", "augue", "purus"};
	public static final int EIGHT_ID = Menu.FIRST+1;
	public static final int SIXTEEN_ID = Menu.FIRST+2;
	public static final int TWENTY_FOUR_ID = Menu.FIRST+3;
	public static final int TWO_ID = Menu.FIRST+4;
	public static final int THIRTY_TWO_ID = Menu.FIRST+5;
	public static final int FORTY_ID = Menu.FIRST+6;
	public static final int ONE_ID = Menu.FIRST+7;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		setListAdapter(new ArrayAdapter<String>(this,
								android.R.layout.simple_list_item_1, items));
		selection=(TextView)findViewById(R.id.selection);
		
		registerForContextMenu(getListView());
	}
	
	public void onListItemClick(ListView parent, View v,
																int position, long id) {
	 	selection.setText(items[position]);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
																		ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return(applyMenuChoice(item) ||
						super.onOptionsItemSelected(item));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return(applyMenuChoice(item) ||
						super.onContextItemSelected(item));
	}
	
	private void populateMenu(Menu menu) {
		menu.add(Menu.NONE, ONE_ID, Menu.NONE, "1 Pixel");
		menu.add(Menu.NONE, TWO_ID, Menu.NONE, "2 Pixels");
		menu.add(Menu.NONE, EIGHT_ID, Menu.NONE, "8 Pixels");
		menu.add(Menu.NONE, SIXTEEN_ID, Menu.NONE, "16 Pixels");
		menu.add(Menu.NONE, TWENTY_FOUR_ID, Menu.NONE, "24 Pixels");
		menu.add(Menu.NONE, THIRTY_TWO_ID, Menu.NONE, "32 Pixels");
		menu.add(Menu.NONE, FORTY_ID, Menu.NONE, "40 Pixels");
	}
	
	private boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
			case ONE_ID:
				getListView().setDividerHeight(1);
				return(true);
		
			case EIGHT_ID:
				getListView().setDividerHeight(8);
				return(true);
		
			case SIXTEEN_ID:
				getListView().setDividerHeight(16);
				return(true);
		
			case TWENTY_FOUR_ID:
				getListView().setDividerHeight(24);
				return(true);
		
			case TWO_ID:
				getListView().setDividerHeight(2);
				return(true);
		
			case THIRTY_TWO_ID:
				getListView().setDividerHeight(32);
				return(true);
		
			case FORTY_ID:
				getListView().setDividerHeight(40);
				return(true);
		}

		return(false);
	}
}