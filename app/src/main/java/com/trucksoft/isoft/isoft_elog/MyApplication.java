package com.trucksoft.isoft.isoft_elog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.Dispat_Application;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.Services.E_logbook_ForegroundService;


/**
 * Created by isoft on 26/10/17.
 */

public class MyApplication extends Dispat_Application implements LifecycleObserver {

Preference pref;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        pref=Preference.getInstance(base);

    }
    ///////////////////////////////////////////////
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.e("AppController", "Foreground");
        isAppInBackground(false);
        try{
        stopService();
        } catch (Exception e) {

        }

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.e("AppController", "Background");
        isAppInBackground(true);
        String str = pref.getString(Constant.LOGIN_CHECK);
        try {
            if (str != null && str.length() > 0 && !str.contentEquals("null")) {
                if (str.equalsIgnoreCase("logged_inn")) {
                    if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) !=null &&
                            pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {

                        startService();
                    }
                }

            }
        } catch (Exception e) {

        }

    }
///////////////////////////////////////////////



    // Adding some callbacks for test and log
    public interface ValueChangeListener {
        void onChanged(Boolean value);
    }
    private ValueChangeListener visibilityChangeListener;
    public void setOnVisibilityChangeListener(ValueChangeListener listener) {
        this.visibilityChangeListener = listener;
    }
    private void isAppInBackground(Boolean isBackground) {
        if (null != visibilityChangeListener) {
            visibilityChangeListener.onChanged(isBackground);
        }
    }
    private static MyApplication mInstance;
    public static MyApplication getInstance() {
        return mInstance;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // addObserver
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void startService() {
        //Log.e("clickk","foreground");
        boolean boolactice=false;


        try
        {

                boolactice=pref.getBolean(Constant.BLUETOOTH_FLAG);

        }catch (Exception e)
        {

        }
        if(pref.getString(Constant.NETWORK_TYPE) !=null && pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.CELLULAR))
        {
            stopService();
        }else  if (boolactice) {
            stopService();
        }
        else {
            Intent serviceIntent = new Intent(this, E_logbook_ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Bluetooth service on");

            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }
    public void stopService() {
        try {
            Intent serviceIntent = new Intent(this, E_logbook_ForegroundService.class);
            stopService(serviceIntent);
        }catch (Exception e)
        {

        }
    }
}
