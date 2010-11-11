/***
	Copyright (c) 2008-2010 CommonsWare, LLC
	
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

package com.commonsware.android.weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.IBinder;
import android.webkit.WebView;
import java.util.ArrayList;

public class WeatherDemo extends Activity {
	private WebView browser;
	private WeatherBinder weather=null;
	private LocationManager mgr=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		browser=(WebView)findViewById(R.id.webkit);
		bindService(new Intent(this, WeatherService.class),
								onService, BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (mgr==null) {
			mgr.removeUpdates(onLocationChange);
		}
		
		unbindService(onService);
	}
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		builder
			.setTitle("Exception!")
			.setMessage(t.toString())
			.setPositiveButton("OK", null)
			.show();
	}
	
	String generatePage(ArrayList<Forecast> forecasts) {
		StringBuilder bufResult=new StringBuilder("<html><body><table>");
		
		bufResult.append("<tr><th width=\"50%\">Time</th>"+
											"<th>Temperature</th><th>Forecast</th></tr>");
		
		for (Forecast forecast : forecasts) {
			bufResult.append("<tr><td align=\"center\">");
			bufResult.append(forecast.getTime());
			bufResult.append("</td><td align=\"center\">");
			bufResult.append(forecast.getTemp());
			bufResult.append("</td><td><img src=\"");
			bufResult.append(forecast.getIcon());
			bufResult.append("\"></td></tr>");
		}
		
		bufResult.append("</table></body></html>");
		
		return(bufResult.toString());
	}
	
	LocationListener onLocationChange=new LocationListener() {
		public void onLocationChanged(Location location) {
			new FetchForecastTask().execute(location);
		}
		
		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}
		
		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}
		
		public void onStatusChanged(String provider, int status,
																	Bundle extras) {
			// required for interface, not used
		}
	};
	
	private ServiceConnection onService=new ServiceConnection() {
		public void onServiceConnected(ComponentName className,
																	 IBinder rawBinder) {
			weather=(WeatherBinder)rawBinder;
			mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
			mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																	3600000, 0, onLocationChange);
		}

		public void onServiceDisconnected(ComponentName className) {
			weather=null;
		}
	};
	
	class FetchForecastTask extends AsyncTask<Location, Void, String> {
		Exception e=null;
		
		@Override
		protected String doInBackground(Location... locs) {
			try {
				return(generatePage(weather.getForecast(locs[0])));
			}
			catch (Exception e) {
				this.e=e;
			}
			
			return(null);
		}
		
		@Override
		protected void onPostExecute(String page) {
			if (e==null) {
				browser.loadDataWithBaseURL(null, page, "text/html",
																		"UTF-8", null);
			}
			else {
				goBlooey(e);
			}
		}
	}
}

