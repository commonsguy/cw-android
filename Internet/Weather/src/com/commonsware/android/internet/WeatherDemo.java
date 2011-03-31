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

package com.commonsware.android.internet;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
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

public class WeatherDemo extends Activity {
	private LocationManager mgr=null;
	private String format;
	private WebView browser;
	private HttpClient client;
	private List<Forecast> forecasts=new ArrayList<Forecast>();
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
		format=getString(R.string.url);
		browser=(WebView)findViewById(R.id.webkit);
		client=new DefaultHttpClient();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																3600000, 1000,
																onLocationChange);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		mgr.removeUpdates(onLocationChange);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		client.getConnectionManager().shutdown();
	}
	
	private void updateForecast(Location loc) {
		String url=String.format(format, loc.getLatitude(),
														 loc.getLongitude());
		HttpGet getMethod=new HttpGet(url);
		
		try {
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody=client.execute(getMethod,
																				 responseHandler);
			buildForecasts(responseBody);
			
			String page=generatePage();
		
			browser.loadDataWithBaseURL(null, page, "text/html",
																	"UTF-8", null);
		}
		catch (Throwable t) {
			android.util.Log.e("WeatherDemo", "Exception fetching data", t);
			Toast
				.makeText(this, "Request failed: "+t.toString(), Toast.LENGTH_LONG)
				.show();
		}
	}
	
	void buildForecasts(String raw) throws Exception {
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
	}
	
	String generatePage() {
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
}