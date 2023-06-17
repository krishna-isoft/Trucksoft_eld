package com.trucksoft.isoft.isoft_elog.Multiused;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class OnlineCheck {

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		Toast.makeText(context, "Please check your network connection !",
				Toast.LENGTH_SHORT).show();
		return false;

	}

}
