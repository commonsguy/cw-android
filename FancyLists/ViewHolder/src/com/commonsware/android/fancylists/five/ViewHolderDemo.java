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

package com.commonsware.android.fancylists.five;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewHolderDemo extends ListActivity {
	private TextView selection;
	private static final String[] items={"lorem", "ipsum", "dolor",
					"sit", "amet",
					"consectetuer", "adipiscing", "elit", "morbi", "vel",
					"ligula", "vitae", "arcu", "aliquet", "mollis",
					"etiam", "vel", "erat", "placerat", "ante",
					"porttitor", "sodales", "pellentesque", "augue", "purus"};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		setListAdapter(new IconicAdapter());
		selection=(TextView)findViewById(R.id.selection);
	}
	
	private String getModel(int position) {
		return(((IconicAdapter)getListAdapter()).getItem(position));
	}
	
	public void onListItemClick(ListView parent, View v,
															int position, long id) {
	 	selection.setText(getModel(position));
	}
	
	class IconicAdapter extends ArrayAdapter<String> {
		IconicAdapter() {
			super(ViewHolderDemo.this, R.layout.row, R.id.label,
						items);
		}
		
		public View getView(int position, View convertView,
												ViewGroup parent) {
			View row=super.getView(position, convertView, parent);
			ViewHolder holder=(ViewHolder)row.getTag();
			
			if (holder==null) {													
				holder=new ViewHolder(row);
				row.setTag(holder);
			}
			
			if (getModel(position).length()>4) {
				holder.icon.setImageResource(R.drawable.delete);
			}	
			else {
				holder.icon.setImageResource(R.drawable.ok);
			}
			
			return(row);
		}
	}
}
