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

package com.commonsware.android.resources;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XMLResourceDemo extends ListActivity {
	TextView selection;
	ArrayList<String> items=new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		selection=(TextView)findViewById(R.id.selection);
		
		try {
			XmlPullParser xpp=getResources().getXml(R.xml.words);
			
			while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
				if (xpp.getEventType()==XmlPullParser.START_TAG) {
					if (xpp.getName().equals("word")) {
						items.add(xpp.getAttributeValue(0));
					}
				}
				
				xpp.next();
			}
		}
		catch (Throwable t) {
			Toast
				.makeText(this, "Request failed: "+t.toString(), 4000)
				.show();
		}
		
		setListAdapter(new ArrayAdapter<String>(this,
														android.R.layout.simple_list_item_1,
														items));
	}
	
	public void onListItemClick(ListView parent, View v, int position,
									long id) {
		selection.setText(items.get(position).toString());
	}
}