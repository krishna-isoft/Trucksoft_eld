package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_config;

/**
 * Created by Lincoln on 14/10/15.
 */

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.geometris.wqlib.WherequbeService;
import com.geometris.wqlib.Wqa;
import com.isoft.trucksoft_elog.AppModel;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_helper.DispatchPrefManager;


/**
 * Created by Ravi on 13/05/15.
 */

public class Dispat_Application extends Application {

    public static final String TAG = Dispat_Application.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static Dispat_Application mInstance;

    private DispatchPrefManager pref;
    AppModel mModel;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Log.e("GeometrisApp","App initialize ...");
        Wqa.getInstance().initialize(this);
try {
    WherequbeService.getInstance().initialize(this);
    Log.e("GeometrisApp", "App created ...");
}catch (Exception e)
{
    Log.e("bluetoothservice",""+e.toString());
}
        mModel = AppModel.getInstance();
    }

    public static synchronized Dispat_Application getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public DispatchPrefManager getPrefManager() {
        if (pref == null) {
            pref = new DispatchPrefManager(this);
        }

        return pref;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void logout() {
        pref.clear();
        /*Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }
    @Override
    public void onTerminate()
    {
        super.onTerminate();
        WherequbeService.getInstance().destroy(this);
        Log.d("GeometrisApp","App terminated. ----------");
    }
}
