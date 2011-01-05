/***
	Copyright (c) 2010-2011 CommonsWare, LLC
	
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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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

public class WeatherBinder extends Binder {
	private String forecast=null;
	private HttpClient client=null;
	private String format=null;

	void onCreate(Context ctxt) {
		client=new DefaultHttpClient();
		format=ctxt.getString(R.string.url);
	}
	
	void onDestroy() {
		client.getConnectionManager().shutdown();
	}
	
	ArrayList<Forecast> getForecast(Location loc) throws Exception {
		String url=String.format(format, loc.getLatitude(),
															loc.getLongitude());
		HttpGet getMethod=new HttpGet(url);
		ResponseHandler<String> responseHandler=new BasicResponseHandler();
		String responseBody=client.execute(getMethod, responseHandler);
		
		return(buildForecasts(responseBody));
	}
	
	private ArrayList<Forecast> buildForecasts(String raw) throws Exception {
		ArrayList<Forecast> forecasts=new ArrayList<Forecast>();
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
}
