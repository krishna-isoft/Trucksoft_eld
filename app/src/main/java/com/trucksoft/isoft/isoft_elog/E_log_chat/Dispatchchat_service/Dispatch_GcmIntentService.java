package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.Dispat_Application;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatchConfig;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatcherEndPoints;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Dispatch_GcmIntentService extends IntentService {

    //private static final String TAG = GcmIntentService.class.getSimpleName();
    private static final String TAG = "truckgbmkkkkkkkkkkk";
    public Dispatch_GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";

    Preference pref;


    @Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(KEY);
        switch (key) {
            case SUBSCRIBE:
                // subscribe to a topic
                String topic = intent.getStringExtra(TOPIC);
                subscribeToTopic(topic);
                break;
            case UNSUBSCRIBE:
                break;
            default:
                // if key is specified, register with GCM
                try {
                    registerGCM();
                }catch(Exception e)
                    {
                       Log.e("err fcm",""+e.toString());
                    }

        }

    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(TAG, "GCM Registration Tokensss: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);

            sharedPreferences.edit().putBoolean(DispatchConfig.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(DispatchConfig.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(DispatchConfig.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
Log.e("call...............kkkk","reg");
        // checking for valid login session
        /*User user = MyApplication.getInstance().getPrefManager().getUser();
        if (user == null) {
            // TODO
            // user not found, redirecting him to login screen
            return;
        }*/
        pref=Preference.getInstance(getApplicationContext());
        String driverid=pref.getString(Constant.DRIVER_ID);
        if(driverid==null)
        {
            return;
        }
        //Log.e("call","cancel"+driverid);
        //String endPoint = EndPoints.USER.replace("_ID_", user.getId());
        String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();
        String endPoint = DispatcherEndPoints.USER.replace("_ID_", driverid);
        Log.e("cvb", "endpoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.PUT,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e( "responseskkkkkk: ","" + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        // broadcasting token sent to server
                        Intent registrationComplete = new Intent(DispatchConfig.SENT_TOKEN_TO_SERVER);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to send gcm registration id to our sever. " + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                   // Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();
                params.put("cc", ""+cmpcode);
                //Log.e("ccdd " ,""+cmpcode);
                params.put("gcm_registration_id", token);
               // Log.e("new reg id " ,""+token);
                Log.e( "params: " ,""+ params.toString());
                return params;
            }
        };

        //Adding request to request queue
        Dispat_Application.getInstance().addToRequestQueue(strReq);
    }

    /**
     * Subscribe to a topic
     */
    public static void subscribeToTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(Dispat_Application.getInstance().getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(Dispat_Application.getInstance().getApplicationContext());
        String token = null;
        try {
            token = instanceID.getToken(Dispat_Application.getInstance().getApplicationContext().getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed to topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(Dispat_Application.getInstance().getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void unsubscribeFromTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token = null;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.unsubscribe(token, "");
                //Log.e(TAG, "Unsubscribed from topic: " + topic);
            } else {
               // Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            //Log.e(TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
