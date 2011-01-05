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

package com.commonsware.android.fancylists.six;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class RateListDemo extends ListActivity {
	private static final String[] items={"lorem", "ipsum", "dolor",
					"sit", "amet",
					"consectetuer", "adipiscing", "elit", "morbi", "vel",
					"ligula", "vitae", "arcu", "aliquet", "mollis",
					"etiam", "vel", "erat", "placerat", "ante",
					"porttitor", "sodales", "pellentesque", "augue", "purus"};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		ArrayList<RowModel> list=new ArrayList<RowModel>();
		
		for (String s : items) {
			list.add(new RowModel(s));
		}
		
		setListAdapter(new RatingAdapter(list));
	}
	
	private RowModel getModel(int position) {
		return(((RatingAdapter)getListAdapter()).getItem(position));
	}
	
	class RatingAdapter extends ArrayAdapter<RowModel> {
		RatingAdapter(ArrayList<RowModel> list) {
			super(RateListDemo.this, R.layout.row, R.id.label, list);
		}
		
		public View getView(int position, View convertView,
												ViewGroup parent) {
			View row=super.getView(position, convertView, parent);
			ViewHolder holder=(ViewHolder)row.getTag();
													
			if (holder==null) {		
				holder=new ViewHolder(row);
				row.setTag(holder);
				
				RatingBar.OnRatingBarChangeListener l=
										new RatingBar.OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar,
																				float rating,
																				boolean fromTouch)	{
						Integer myPosition=(Integer)ratingBar.getTag();
						RowModel model=getModel(myPosition);
						
						model.rating=rating;
					
						LinearLayout parent=(LinearLayout)ratingBar.getParent();
						TextView label=(TextView)parent.findViewById(R.id.label);
				
						label.setText(model.toString());
					}
				};
				
				holder.rate.setOnRatingBarChangeListener(l);
			}

			RowModel model=getModel(position);
			
			holder.rate.setTag(new Integer(position));
			holder.rate.setRating(model.rating);
			
			return(row);
		}
	}
	
	class RowModel {
		String label;
		float rating=2.0f;
		
		RowModel(String label) {
			this.label=label;
		}
		
		public String toString() {
			if (rating>=3.0) {
				return(label.toUpperCase());
			}
			
			return(label);
		}
	}
}