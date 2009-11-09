/* Copyright (c) 2008-2009 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.commonsware.android.rotation.three;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.Button;
import android.util.Log;

public class RotationThreeDemo extends Activity {
	static final int PICK_REQUEST=1337;
	Button viewButton=null;
	Uri contact=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupViews();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
																		Intent data) {
		if (requestCode==PICK_REQUEST) {
			if (resultCode==RESULT_OK) {
				contact=data.getData();
				viewButton.setEnabled(true);
			}
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		setupViews();
	}
	
	private void setupViews() {
		setContentView(R.layout.main);
		
		Button btn=(Button)findViewById(R.id.pick);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i=new Intent(Intent.ACTION_PICK,
														Contacts.People.CONTENT_URI);

				startActivityForResult(i, PICK_REQUEST);
			}
		});
		
		viewButton=(Button)findViewById(R.id.view);
		
		viewButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startActivity(new Intent(Intent.ACTION_VIEW, contact));
			}
		});
		
		viewButton.setEnabled(contact!=null);
	}
}
