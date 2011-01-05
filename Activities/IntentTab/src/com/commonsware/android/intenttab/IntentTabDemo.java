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

package com.commonsware.android.intenttab;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TabHost;

public class IntentTabDemo extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TabHost host=getTabHost();
		Intent i=new Intent(this, CWBrowser.class);
		
		i.putExtra(CWBrowser.URL, "http://commonsware.com");
		host.addTab(host.newTabSpec("one")
						.setIndicator("CW")
						.setContent(i));
		
		i=new Intent(i);
		i.putExtra(CWBrowser.URL, "http://www.android.com");
		host.addTab(host.newTabSpec("two")
						.setIndicator("Android")
						.setContent(i));
	}
}
