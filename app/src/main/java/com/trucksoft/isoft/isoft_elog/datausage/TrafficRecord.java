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

import android.net.TrafficStats;

public class TrafficRecord {
	public long tx=0;
	public long rx=0;
	String tag=null;
	
	TrafficRecord() {
		tx=TrafficStats.getTotalTxBytes();
		//rx=TrafficStats.getTotalRxBytes();
		rx=TrafficStats.getTotalRxBytes();
	}
	
	TrafficRecord(int uid, String tag) {
		tx=TrafficStats.getUidTxBytes(uid);
		//Log.e("tx", "*"+tx);
		rx=TrafficStats.getUidRxBytes(uid);
		//Log.e("rx", "*"+rx);
		this.tag=tag;
	}
}