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

package com.commonsware.android.files;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class StaticFileDemo extends ListActivity {
	TextView selection;
	ArrayList<String> items=new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		selection=(TextView)findViewById(R.id.selection);
		
		try {
			InputStream in=getResources().openRawResource(R.raw.words);
			DocumentBuilder builder=DocumentBuilderFactory
																.newInstance()
																.newDocumentBuilder();
			Document doc=builder.parse(in, null);
			NodeList words=doc.getElementsByTagName("word");
			
			for (int i=0;i<words.getLength();i++) {
				items.add(((Element)words.item(i)).getAttribute("value"));
			}
			
			in.close();
		}
		catch (Throwable t) {
			Toast
				.makeText(this, "Exception: "+t.toString(), 2000)
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