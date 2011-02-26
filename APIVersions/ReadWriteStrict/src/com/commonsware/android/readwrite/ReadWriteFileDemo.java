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

package com.commonsware.android.readwrite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ReadWriteFileDemo extends Activity {
	private final static String NOTES="notes.txt";
	private EditText editor;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		StrictWrapper.init();
	
		editor=(EditText)findViewById(R.id.editor);
	}
	
	public void onResume() {
		super.onResume();
		
		try {
			InputStream in=openFileInput(NOTES);
			
			if (in!=null) {
				InputStreamReader tmp=new InputStreamReader(in);
				BufferedReader reader=new BufferedReader(tmp);
				String str;
				StringBuilder buf=new StringBuilder();
				
				while ((str = reader.readLine()) != null) {
					buf.append(str+"\n");
				}
				
				in.close();
				editor.setText(buf.toString());
			}
		}
		catch (java.io.FileNotFoundException e) {
			// that's OK, we probably haven't created it yet
		}
		catch (Throwable t) {
			Toast
				.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG)
				.show();
		}
	}
	
	public void onPause() {
		super.onPause();
		
		try {
			OutputStreamWriter out=
					new OutputStreamWriter(openFileOutput(NOTES, 0));
			
			out.write(editor.getText().toString());
			out.close();		
		}
		catch (Throwable t) {
			Toast
				.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG)
				.show();
		}
	}
}