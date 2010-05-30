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

package com.commonsware.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import java.io.StringReader;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class WeatherPlusService extends Service {
	public static final int NOTIF_ID=1337;
	public static final String SHUTDOWN="SHUTDOWN";
	public static final String BROADCAST_ACTION=
						"com.commonsware.android.service.ForecastUpdateEvent";
	private LocationManager mgr=null;
	private String forecast=null;
	private HttpClient client=null;
	private String format=null;
	private Intent broadcast=new Intent(BROADCAST_ACTION);
	private final Binder binder=new LocalBinder();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		client=new DefaultHttpClient();
		format=getString(R.string.url);
		
		mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
		mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																10000, 10000.0f, onLocationChange);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return(binder);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mgr.removeUpdates(onLocationChange);
		client.getConnectionManager().shutdown();
	}
	
	synchronized public String getForecastPage() {
		return(forecast);
	}
	
	private void updateForecast(Location loc) {
		new FetchForecastTask().execute(loc);
	}
	
	List<Forecast> buildForecasts(String raw) throws Exception {
		List<Forecast> forecasts=new ArrayList<Forecast>();
		DocumentBuilder builder=DocumentBuilderFactory
															.newInstance()
															.newDocumentBuilder();
		Document doc=builder.parse(new InputSource(new StringReader(raw)));
		NodeList times=doc.getElementsByTagName("start-valid-time");
		
		for (int i=0;i<times.getLength();i++) {
			Element time=(Element)times.item(i);
			Forecast forecast=new Forecast();
			
			forecasts.add(forecast);
			forecast.setTime(time.getFirstChild().getNodeValue());
		}
		
		NodeList temps=doc.getElementsByTagName("value");
		
		for (int i=0;i<temps.getLength();i++) {
			Element temp=(Element)temps.item(i);
			Forecast forecast=forecasts.get(i);
			
			forecast.setTemp(new Integer(temp.getFirstChild().getNodeValue()));
		}
		
		NodeList icons=doc.getElementsByTagName("icon-link");
		
		for (int i=0;i<icons.getLength();i++) {
			Element icon=(Element)icons.item(i);
			Forecast forecast=forecasts.get(i);
			
			forecast.setIcon(icon.getFirstChild().getNodeValue());
		}
		
		return(forecasts);
	}
	
	String generatePage(List<Forecast> forecasts) {
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
			updateForecast(location);
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
	
	public class LocalBinder extends Binder {
		WeatherPlusService getService() {
			return(WeatherPlusService.this);
		}
	}
	
	class Forecast {
		String time="";
		Integer temp=null;
		String iconUrl="";
		
		String getTime() {
			return(time);
		}
	
		void setTime(String time) {
			this.time=time.substring(0,16).replace('T', ' ');
		}
		
		Integer getTemp() {
			return(temp);
		}
		
		void setTemp(Integer temp) {
			this.temp=temp;
		}
		
		String getIcon() {
			return(iconUrl);
		}
		
		void setIcon(String iconUrl) {
			this.iconUrl=iconUrl;
		}
	}
	
	class FetchForecastTask extends AsyncTask<Location, Void, Void> {
		@Override
		protected Void doInBackground(Location... locs) {
			Location loc=locs[0];
			String url=String.format(format, loc.getLatitude(),
																loc.getLongitude());
			HttpGet getMethod=new HttpGet(url);
			
			try {
				ResponseHandler<String> responseHandler=new BasicResponseHandler();
				String responseBody=client.execute(getMethod, responseHandler);
				String page=generatePage(buildForecasts(responseBody));
				
				synchronized(this) {
					forecast=page;
				}
			
				sendBroadcast(broadcast);
			}
			catch (Throwable t) {
				android.util.Log.e("WeatherPlus",
														"Exception in updateForecast()", t);
			}
			
			return(null);
		}
		
		@Override
		protected void onProgressUpdate(Void... unused) {
			// not needed here
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			// not needed here
		}
	}
}