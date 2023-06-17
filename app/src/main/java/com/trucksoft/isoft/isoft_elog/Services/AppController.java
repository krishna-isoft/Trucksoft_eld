package com.trucksoft.isoft.isoft_elog.Services;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class AppController extends Application implements LifecycleObserver {


    ///////////////////////////////////////////////
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.e("AppController", "Foreground");
        isAppInBackground(false);
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.e("AppController", "Background");
        isAppInBackground(true);
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
    private static AppController mInstance;
    public static AppController getInstance() {
        return mInstance;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        // addObserver
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

}
