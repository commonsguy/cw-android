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

package com.commonsware.android.fancylists.five;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class ViewWrapper {
	View base;
	TextView label=null;
	ImageView icon=null;
	
	ViewWrapper(View base) {
		this.base=base;
	}
	
	TextView getLabel() {
		if (label==null) {
			label=(TextView)base.findViewById(R.id.label);
		}
		
		return(label);
	}
	
	ImageView getIcon() {
		if (icon==null) {
			icon=(ImageView)base.findViewById(R.id.icon);
		}
		
		return(icon);
	}
}
