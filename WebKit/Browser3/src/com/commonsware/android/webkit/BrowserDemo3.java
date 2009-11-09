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

package com.commonsware.android.webkit;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Date;

public class BrowserDemo3 extends Activity {
	WebView browser;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		browser=(WebView)findViewById(R.id.webkit);
		browser.setWebViewClient(new Callback());
		
		loadTime();
	}
	
	void loadTime() {
		String page="<html><body><a href=\"clock\">"
						+new Date().toString()
						+"</a></body></html>";
						
						browser.loadDataWithBaseURL("x-data://base", page,
																				"text/html", "UTF-8",
																				null);
	}

	private class Callback extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			loadTime();
			
			return(true);
		}
	}
}