/***
	Copyright (c) 2008-2010 CommonsWare, LLC
	
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

package com.commonsware.android.grid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class GridDemo extends Activity
	implements AdapterView.OnItemSelectedListener {
	TextView selection;
	String[] items={"lorem", "ipsum", "dolor", "sit", "amet",
					"consectetuer", "adipiscing", "elit", "morbi", "vel",
					"ligula", "vitae", "arcu", "aliquet", "mollis",
					"etiam", "vel", "erat", "placerat", "ante",
					"porttitor", "sodales", "pellentesque", "augue", "purus"};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		selection=(TextView)findViewById(R.id.selection);
		
		GridView g=(GridView) findViewById(R.id.grid);
		g.setAdapter(new FunnyLookingAdapter(this,
												android.R.layout.simple_list_item_1,
												items));
		g.setOnItemSelectedListener(this);
	}
	
	public void onItemSelected(AdapterView<?> parent, View v,
															int position, long id) {
		selection.setText(items[position]);
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		selection.setText("");
	}
	
	private class FunnyLookingAdapter extends ArrayAdapter {
		Context ctxt;
		
		FunnyLookingAdapter(Context ctxt, int resource,
												String[] items) {
			super(ctxt, resource, items);
			
			this.ctxt=ctxt;
		}
		
		public View getView(int position, View convertView,
													ViewGroup parent) {
			TextView label=(TextView)convertView;
			
			if (convertView==null) {
				convertView=new TextView(ctxt);
				label=(TextView)convertView;
			}
			
			label.setText(items[position]);
			
			return(convertView);
		}
	}
}