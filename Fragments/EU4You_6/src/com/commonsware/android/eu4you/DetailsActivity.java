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

package com.commonsware.android.eu4you;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class DetailsActivity extends FragmentActivity {
	public static final String EXTRA_URL="com.commonsware.android.eu4you.EXTRA_URL";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		DetailsFragment details
			=(DetailsFragment)getSupportFragmentManager()
														.findFragmentById(R.id.details);
		
		details.loadUrl(getIntent().getStringExtra(EXTRA_URL));														
	}
}