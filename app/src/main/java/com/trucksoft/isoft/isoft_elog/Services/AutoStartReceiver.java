package com.trucksoft.isoft.isoft_elog.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.work.impl.utils.ForceStopRunnable;

import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;

@SuppressLint("RestrictedApi")
public class AutoStartReceiver extends ForceStopRunnable.BroadcastReceiver {
    Preference pref;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("callbv","device reboot");
        pref=Preference.getInstance(context);
        if(pref.getString(Constant.NETWORK_TYPE) !=null && pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR))
        {

        }else {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent serviceIntent = new Intent(context, E_logbook_ForegroundService.class);
                serviceIntent.putExtra("inputExtra", "Bluetooth service on");

                ContextCompat.startForegroundService(context, serviceIntent);
            }
        }
    }
}
