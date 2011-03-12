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

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class EU4You extends FragmentActivity implements CountryListener {
	private boolean detailsInline=false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		CountriesFragment countries
			=(CountriesFragment)getSupportFragmentManager()
														.findFragmentById(R.id.countries);
		
		countries.setCountryListener(this);
		
		Fragment f=getSupportFragmentManager().findFragmentById(R.id.details);
		
		detailsInline=(f!=null &&
										(getResources().getConfiguration().orientation==
											Configuration.ORIENTATION_LANDSCAPE));
		
		if (detailsInline) {
			countries.enablePersistentSelection();
		}
		else if (f!=null) {
			f.getView().setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onCountrySelected(Country c) {
		String url=getString(c.url);
														
		if (detailsInline) {
			((DetailsFragment)getSupportFragmentManager()
														.findFragmentById(R.id.details))
														.loadUrl(url);														
		}
		else {
			Intent i=new Intent(this, DetailsActivity.class);
			
			i.putExtra(DetailsActivity.EXTRA_URL, url);
			startActivity(i);
		}
	}
}
