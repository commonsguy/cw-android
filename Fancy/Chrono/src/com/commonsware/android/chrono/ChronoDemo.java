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

package com.commonsware.android.chrono;

import android.app.Activity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Calendar;

public class ChronoDemo extends Activity {
	DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
	TextView dateAndTimeLabel;
	Calendar dateAndTime=Calendar.getInstance();
			
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		dateAndTimeLabel=(TextView)findViewById(R.id.dateAndTime);
		
		updateLabel();
	}
	
	public void chooseDate(View v) {
		new DatePickerDialog(ChronoDemo.this, d,
													dateAndTime.get(Calendar.YEAR),
													dateAndTime.get(Calendar.MONTH),
													dateAndTime.get(Calendar.DAY_OF_MONTH))
			.show();
	}
	
	public void chooseTime(View v) {
		new TimePickerDialog(ChronoDemo.this, t,
													dateAndTime.get(Calendar.HOUR_OF_DAY),
													dateAndTime.get(Calendar.MINUTE),
													true)
			.show();
	}
	
	private void updateLabel() {
		dateAndTimeLabel.setText(fmtDateAndTime
															.format(dateAndTime.getTime()));
	}
	
	DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
													int dayOfMonth) {
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}
	};
	
	TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay,
													int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			updateLabel();
		}
	};	
}