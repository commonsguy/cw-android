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

package com.commonsware.android.strings;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class StringsDemo extends Activity {
	EditText name;
	TextView result;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		name=(EditText)findViewById(R.id.name);
		result=(TextView)findViewById(R.id.result);
	}
	
	public void applyFormat(View v) {
		String format=getString(R.string.funky_format);
		String simpleResult=String.format(format,
											TextUtils.htmlEncode(name.getText().toString()));
		result.setText(Html.fromHtml(simpleResult));
	}
}