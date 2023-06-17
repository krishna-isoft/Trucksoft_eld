package com.trucksoft.isoft.isoft_elog.isoft_eloggcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;



/**
 * Created by isoft_elog on 1/5/17.
 */

public class Trucksoft_elog_Driverid_service extends FirebaseInstanceIdService {
    private static final String TAG = Trucksoft_elog_Driverid_service.class.getSimpleName();
    String refreshedToken;
    private static Preference pref;
    @Override
    public void onTokenRefresh() {

        super.onTokenRefresh();

        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            // Log.e("Firbase id login", "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Firbaseexception", ""+e.toString());
        }
        //  Log.e("refreshedToken","dfd"+refreshedToken);

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Trucksoft_elog_DriverConfig.DRIVER_APP_REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: kk" + token);
        pref = Preference.getInstance(this);
        if(token !=null && token.length()>0 && !token.contentEquals("null")) {
            pref.putString(Constant.ELOG_FIREBASE_KEY,
                    "" + token);
            // addregkey(token);
        }
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Trucksoft_elog_DriverConfig.ISOFT_SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



}

