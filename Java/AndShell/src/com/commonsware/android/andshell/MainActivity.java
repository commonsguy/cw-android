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

package com.commonsware.android.andshell;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bsh.Interpreter;

public class MainActivity extends Activity {
	private Interpreter i=new Interpreter();
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		Button btn=(Button)findViewById(R.id.eval);
		final EditText script=(EditText)findViewById(R.id.script);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String src=script.getText().toString();
				
				try {
					i.set("context", MainActivity.this);
					i.eval(src);
				}
				catch (bsh.EvalError e) {
					AlertDialog.Builder builder=
										new AlertDialog.Builder(MainActivity.this);
					
					builder
						.setTitle("Exception!")
						.setMessage(e.toString())
						.setPositiveButton("OK", null)
						.show();
				}
			}
		});
	}
}