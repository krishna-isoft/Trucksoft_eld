/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	Licensed under the Apache License, Version 2.0 (the "License"); you may not
	use this file except in compliance with the License. You may obtain a copy
	of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
	by applicable law or agreed to in writing, software distributed under the
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
	OF ANY KIND, either express or implied. See the License for the specific
	language governing permissions and limitations under the License.
	
	From _Tuning Android Applications_
		http://commonsware.com/AndTuning
*/

package com.trucksoft.isoft.isoft_elog.datausage;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.HashMap;

public class TrafficSnapshot {
	public TrafficRecord device=null;
	public TrafficRecord device1=null;
	HashMap<Integer, TrafficRecord> apps=
		new HashMap<Integer, TrafficRecord>();
	private int appud=0;
	
	public TrafficSnapshot(Context ctxt) {
		device=new TrafficRecord();
		
		HashMap<Integer, String> appNames=new HashMap<Integer, String>();
		
		for (ApplicationInfo app :
					ctxt.getPackageManager().getInstalledApplications(0)) {

			//Log.e("packageName", app.packageName);
			if(app.packageName.contentEquals("com.trucksoft.isoft.isoft_elog"))
			{appNames.put(app.uid, app.packageName);
			appud=app.uid;
			}
			
			
		}
		if(appud !=0)
		{
		//Log.e("appud", "**************"+appud);
		device1=new TrafficRecord(appud,"com.trucksoft.isoft.isoft_elog");
			for (Integer uid : appNames.keySet()) {
				apps.put(uid, new TrafficRecord(uid, appNames.get(uid)));
			}
		}
		

	}
}