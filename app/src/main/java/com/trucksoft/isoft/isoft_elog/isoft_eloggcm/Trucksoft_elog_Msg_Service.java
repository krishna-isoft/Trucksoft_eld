package com.trucksoft.isoft.isoft_elog.isoft_eloggcm;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_activity.Appupdateclasselog;
import com.isoft.trucksoft_elog.Isoft_activity.Geofence_alertt;
import com.isoft.trucksoft_elog.Isoft_activity.Home_activity_bluetooth;
import com.isoft.trucksoft_elog.Isoft_activity.Responsemodel;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Faultcode_model;
import com.isoft.trucksoft_elog.Model_class.Getvalue_model;
import com.isoft.trucksoft_elog.Model_class.Location_model;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by isoft on 1/5/17.
 */

public class Trucksoft_elog_Msg_Service extends FirebaseMessagingService implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private static final String TAG = Trucksoft_elog_Msg_Service.class.getSimpleName();

    private Trucksoft_elog_Notify_Utils notificationUtils;
    private static final int NOTIFY_ME_ID=1337;

    private static final int NOTIFY_ME_ID_DISPATCH=1340;
    private static final int NOTIFY_ME_ID_REMARK=1341;
    private static final int NOTIFY_ME_APP_UPDATE=1341555;
    private static final String CHANNEL_ID="e-logbook app update";
    private Preference pref;
    SharedPreferences sp;
    String lat, lon;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 10; /* 2 sec */
String str;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;
    String sktype;
    String statustype;
    private CommonUtil commonUtil;
    private Context context;
    private static Context myContext;
    private String driverid;
    private String currentdate;
    private String str_vin;
    static boolean isInBackground = true;
    Dialog dialogrk;
    Eld_api api;
    String vinnumber;
    int apicall=0;
    List<Getvalue_model> movies;
    String currentstatus="";
    NotificationUtilsoreo_elog mNotificationUtils;
    Dialog dialogdistance;
    Dialog dialoonduty;
    Dialog dialogdrive;
    Dialog dialognotificaton;
//    ProgressDialog dialogz;
    Font_manager font_manager;
    String str_remarkstatus;
    int int_remarkcount=0;
    private int medialevel=0;
    Chronometer myChronometer;
    Timer Tk;
    MediaRecorder mediaRecorder;
    private String strnorowid;
    private String mFileName;
    String datetime;
    private String filepath = null;
    Dialog dialogresp;
    String breaktype="";

    private String strlastid="";
    Dialog dialogunplug,dialoglogi;
    private String strstate,straddress;
    LocationManager locationManager;
    boolean GpsStatus;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // Log.e(TAG, "From: " + remoteMessage.getFrom());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
context=this;
        font_manager=new Font_manager();
        mNotificationUtils = new NotificationUtilsoreo_elog(this);
        movies=new ArrayList<>();
        commonUtil = new CommonUtil(context);

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
           // Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
           // Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                //Log.e("remotmsg","@"+remoteMessage.getData().toString());
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "errrrrrr88888888888: " + e.getMessage());
            }


        }
    }

    private void handleNotification(String message) {
        if (!Trucksoft_elog_Notify_Utils.isAppIsInBackground(getApplicationContext())) {

        }else{

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleDataMessage(JSONObject json) {
        Log.e("kdjson",json.toString());
       // Log.e(TAG, "push json...kk.......: " + json.toString());
       // Log.e("isoft", "push json.............: ");
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this).build();
//        mGoogleApiClient.connect();
        pref = Preference.getInstance(this);
        str_remarkstatus=pref.getString(Constant.REMARK_STATUS);
        str = pref.getString(Constant.LOGIN_CHECK);
        int_remarkcount=pref.getInt(Constant.REMARK_COUNT);
        //Log.e("call","back");
        try {
            JSONObject data = json.getJSONObject("data");
           Log.e("data",""+data.toString());
            if(json.has("lastid"))
            {
                strlastid=json.getString("lastid");
                //Log.e("kkmm",""+strlastid);
            }


            if(data.has("title"))
            {
                Log.e("kkmm","title");
                String title=data.getString("title");
                Log.e("title","@"+title);
              if(title.contentEquals("Check_BT"))
              {
                  if (isAppIsInBackground(context)) {
                      Log.e("calling","app closed");

                      try{
                          String cstatus=pref.getString(Constant.CURRENT_STATUS);
                          if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null")) {
                              if(cstatus.contentEquals(commonUtil.DRIVING))
                              { String bluetime=pref.getString(Constant.BLUETOOTH_RESPONSE_TIME);
                                  String prevstatus = pref.getString(Constant.PREVIOUS_CURRENT_STATUS);
                                  if(prevstatus !=null && prevstatus.length()>0) {

                                  }else{
                                      prevstatus=commonUtil.OFF_DUTY;
                                  }
                              if(bluetime !=null && bluetime.length()>0) {
                                  Log.e("bluetime",""+bluetime);
                                  SimpleDateFormat formattimed = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                  String time = formattimed.format(new Date());
long bltime=splittimeseconds(bluetime);
                                  long ctime = splittimeseconds(time);
Log.e("bltime",""+bltime);
                                  Log.e("ctime",""+ctime);
                                  long difftime=ctime-bltime;
                                  Log.e("difftime",""+difftime);
                                  if(difftime>=15)
                                  {

                                      if (OnlineCheck.isOnline(context)) {
                                          Log.e("prevstatus",""+prevstatus);
                                          Log.e("apicall",""+apicall);
                                          if (apicall == 0) {
                                              apicall = 1;
                                              gettodaysavevaluesbluetooth(prevstatus, 0,"", "Bluetooth disconnected.","","","");

                                          }
                                      }
                                  }
                              }else{

                                      if (OnlineCheck.isOnline(context)) {
                                          if (apicall == 0) {
                                              apicall = 1;
                                              gettodaysavevaluesbluetooth(prevstatus, 0,"", ".","","","");

                                          }
                                      }
                              }

                              }
                          }
                      }catch (Exception e)
                      {

                      }
                  }else{
                      Log.e("calling","app opened");
                      Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
                      pushNotification.putExtra("message", title);
                     // pushNotification.putExtra("statustype", statustype);
                      LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                  }
                  Log.e("bluetooth","fcm called");
              }else  if(title.contentEquals("geo_fence"))
              {
                  String typed=data.getString("type");
                  Log.e("typed", "@"+typed);
                  String msgk=data.getString("message");
                  Log.e("msgk", "@"+msgk);
                  if (isAppIsInBackground(context)) {
                      Log.e("calling", "app closed");
                      str = pref.getString(Constant.LOGIN_CHECK);
//Log.e("call","break notio");
                      if (str != null && str.length() > 0 && !str.contentEquals("null")) {
                          if (str.equalsIgnoreCase("logged_inn")) {
                              try {
                                  Intent ing = new Intent(context, Geofence_alertt.class);
                                  ing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  ing.putExtra("message", title);
                                  ing.putExtra("typed", typed);
                                  ing.putExtra("msgval", msgk);
                                  startActivity(ing);
                              } catch (Exception e) {
                                  Log.e("Exceptionff", "@" + e.toString());
                              }
                          }
                      }
                  }else{
                      Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
                      pushNotification.putExtra("message", title);
                      pushNotification.putExtra("typed", typed);
                      pushNotification.putExtra("msgval", msgk);
                      LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                  }
              }


              else{
                  Log.e("bluetooth","fcm failed");
              }
            }else{
                Log.e("kkmm","title fds");
            }




            sktype=data.getString("message");
            Log.e("gcm ststus", "sktype: " + sktype);
           // Log.e(TAG, "title: " + title);
if(sktype.contentEquals("send_location"))
{

    updateloc();
    statustype=data.getString("type");
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("statustype", statustype);
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

}else if(sktype.contentEquals("IGN_OFF"))
{
 if (isAppIsInBackground(context)) {
        //  Log.e("calling","app closed");
          try{

              if(pref.getString(Constant.NETWORK_TYPE) !=null && pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
                  String cstatus=pref.getString(Constant.CURRENT_STATUS);
                  if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null")) {
                      if (cstatus.contentEquals(commonUtil.DRIVING)) {
                          String prevstatus = pref.getString(Constant.PREVIOUS_CURRENT_STATUS);
                          if(prevstatus !=null && prevstatus.length()>0) {

                          }else{
                              prevstatus=commonUtil.OFF_DUTY;
                          }
                          if (OnlineCheck.isOnline(context)) {
                              if (apicall == 0) {
                                  apicall = 1;
                                  gettodaysavevaluesbluetooth(prevstatus, 0,"", ".","","","");

                              }
                          }
                      }
                  }
//              if(pref.getString(Constant.CURRENT_STATUS) !=null && pref.getString(Constant.CURRENT_STATUS).toString().contentEquals(commonUtil.DRIVING)) {
//            String cstatus = "" + pref.getString(Constant.CURRENT_STATUS_BB);
//          //  Log.e("cstatus", "@" + cstatus);
//            if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null")) {
//                if (cstatus.contentEquals(commonUtil.OFF_DUTY)) {
//                    if (OnlineCheck.isOnline(context)) {
//                        if (apicall == 0) {
//                            apicall = 1;
//                            gettodaysavevaluesbluetooth(commonUtil.OFF_DUTY, 0,"", ",","","","");
//
//                        }
//                    }
//                } else if (cstatus.contentEquals(commonUtil.ON_DUTY)) {
//                    if (OnlineCheck.isOnline(context)) {
//                        // onClickStatusOnDuty();
//                        if (apicall == 0) {
//                            apicall = 1;
//                            gettodaysavevaluesbluetooth(commonUtil.ON_DUTY, 0,"", ",","","","");
//
//                        }
//                    }
//
//                } else if (cstatus.contentEquals(commonUtil.SLEEP)) {
//                    if (OnlineCheck.isOnline(context)) {
//
//                        if (apicall == 0) {
//
//                            apicall = 1;
//                            gettodaysavevaluesbluetooth(commonUtil.SLEEP, 0,"", ",","","","");
//
//                        }
//                    }
//                } else {
//                    if (OnlineCheck.isOnline(context)) {
//                        if (apicall == 0) {
//                            apicall = 1;
//
//                            gettodaysavevaluesbluetooth(commonUtil.OFF_DUTY, 0,"", ",","","","");
//
//
//                        }
//                    }
//                }
//            }
//
//            }
            pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY, "off");
        }


              }catch (Exception e)
          {

          }
          //applogout();
    }else {
     Log.e("calling", "app openedmmmmmmmmmmmmmm");
     Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
     pushNotification.putExtra("message", sktype);
     pushNotification.putExtra("statustype", statustype);
     LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
 }
}

else if(sktype.contentEquals("change_status"))
{
  //  pref.putString(Constant.STATUS_LASTID_NEW,""+strlastid);
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    // pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_USER);
    pushNotification.putExtra("message", sktype);
  //  pushNotification.putExtra("statustype", statustype);
    //  pushNotification.putExtra("chat_room_id", chatRoomId);
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

}
//else if(sktype.contentEquals("state_changed"))
//{
//    JSONObject detail = data.getJSONObject("details");
//
//    Log.e("eightdays",""+detail.getString("eightdays"));
//    Log.e("drive_time",""+detail.getString("drive_time"));
//    Log.e("onduty_time",""+detail.getString("onduty_time"));
//
//    pref.putString(Constant.STATE_DRIVETIME,detail.getString("drive_time"));
//    pref.putString(Constant.STATE_ONDUTYTIME,""+detail.getString("onduty_time"));
//    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
//      pushNotification.putExtra("message", sktype);
//      pushNotification.putExtra("statedrive", ""+detail.getString("drive_time"));
//      pushNotification.putExtra("stateonduty", ""+detail.getString("onduty_time"));
//      LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//}


else if(sktype.contentEquals("app_logout"))
{
    if (isAppIsInBackground(context)) {
          Log.e("calling","app closed");
          applogout();
    }else {
        Log.e("calling","app openedmmmmmmmmmmmmmm");
        Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
        pushNotification.putExtra("message", sktype);
        pushNotification.putExtra("statustype", statustype);
       LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        pref = Preference.getInstance(context);
        //Log.e("login", "fail");
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.BLUETOOTH_CONNECTED,"0");
        pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        pref.putString(Constant.VIN_NUMBER, "");
        pref.putString(Constant.BLUETOOTH_ADDRESS,"");
        pref.putString(Constant.BLUETOOTH_NAME,"");
        pref.putString(Constant.NETWORK_TYPE,Constant.CELLULAR);

    }

}

else if(sktype.contentEquals("bluetooth_status"))
{
    statustype=data.getString("type");
    Log.e("type","@"+statustype);
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    // pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_USER);
    pushNotification.putExtra("speed", statustype);
    pushNotification.putExtra("message", sktype);
    //  pushNotification.putExtra("chat_room_id", chatRoomId);
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
}
else if(sktype.contentEquals("bluetooth_track"))
{
    statustype=data.getString("type");
    Log.e("typedd","@"+statustype);
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
   pref.putString(Constant.TRACK_BLUETOOTH,""+statustype);
    pushNotification.putExtra("message", sktype);
    //  pushNotification.putExtra("chat_room_id", chatRoomId);
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
}

else if(sktype.contentEquals("custom_alert"))
{
    statustype=data.getString("type");
    Log.e("type","@"+statustype);
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    // pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_USER);
    pushNotification.putExtra("msg", statustype);
    pushNotification.putExtra("message", sktype);
    //  pushNotification.putExtra("chat_room_id", chatRoomId);
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
}



else if(sktype.contentEquals("send_status"))
{
    statustype=data.getString("type");

    pref.putString(Constant.STATUS_LASTID_NEW,""+strlastid);
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
   // pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_USER);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("statustype", statustype);
  //  pushNotification.putExtra("chat_room_id", chatRoomId);
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

    String str = pref.getString(Constant.LOGIN_CHECK);
    driverid= pref.getString(Constant.DRIVER_ID);

    // Log.e("app work",""+isInBackground);
    if(driverid !=null && driverid.length()>0 && !driverid.contentEquals("null")) {
        if (isAppIsInBackground(context)) {
           //  Log.e("calling","app closed");

            if (str != null && str.length() > 0 && !str.contentEquals("null")) {
                if (str.equalsIgnoreCase("logged_inn")) {
                    if(lat==null || lat.length()==0) {
                        lat = pref.getString(Constant.LATITUDE);
                        lon = pref.getString(Constant.LONGITUDE);

                    }
                    gettodaynotif();//temporarly blocked for switch

                   //  Log.e("cback","call back service");

                }
            }

        }
    }
}else if(sktype.contentEquals("interval_update"))
{
    String type=data.getString("type");
    if (isAppIsInBackground(context)) {
        try {
            if (type != null && type.length() > 0) {
                int vcval=30;
                vcval=Integer.parseInt(type);

                pref.putString(Constant.LOCATION_UPDATE_STATUS, "" +vcval);
            }
        }catch (Exception e)
        {

        }
    }else{

        Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
        // pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_USER);
        pushNotification.putExtra("message", sktype);
        pushNotification.putExtra("type", type);
        //  pushNotification.putExtra("chat_room_id", chatRoomId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

}



else if(sktype.contentEquals("send_notification"))
{
//    statustype=data.getString("type");
//    String status="";
//    String statustyped="";
//    String breakid="";
//    if(data.has("estatus"))
//    {
//        status=data.getString("estatus");
//    }
//    if(data.has("statustype"))
//    {
//        statustyped=data.getString("statustype");
//    }
//    if(data.has("breakid"))
//    {
//        breakid=data.getString("breakid");
//    }
//    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
//    pushNotification.putExtra("message", sktype);
//    pushNotification.putExtra("estatus", ""+status);
//    pushNotification.putExtra("estatustype", ""+statustyped);
//    pushNotification.putExtra("statustype", ""+statustype);
//    pushNotification.putExtra("breakid", ""+breakid);
//    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//    pref = Preference.getInstance(this);
//
//    str = pref.getString(Constant.LOGIN_CHECK);
//    driverid= pref.getString(Constant.DRIVER_ID);
//    if (str != null && str.length() > 0 && !str.contentEquals("null")) {
//        if (str.equalsIgnoreCase("logged_inn")) {
//            notification(statustype, status, breakid);
//        }
//    }
//   // Log.e("callingdriverid",""+driverid);
//    if(driverid !=null && driverid.length()>0 && !driverid.contentEquals("null")) {
//        if (isAppIsInBackground(context)) {
//           // Log.e("calling","not app closed"); //temporarly blocked for switch purpose
//Intent ing=new Intent(context, Transparent_notify.class);
//            ing.putExtra("message", sktype);
//            ing.putExtra("estatus", ""+status);
//            ing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ing.putExtra("estatustype", ""+statustyped);
//            ing.putExtra("statustype", ""+statustype);
//            ing.putExtra("breakid", ""+breakid);
//startActivity(ing);
//        }
//    }
}else if(sktype.contentEquals("send_notification_distance"))
{
    String statust=data.getString("type");
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("estatus", ""+statust);
    pushNotification.putExtra("estatustype", "");
    pushNotification.putExtra("statustype", "");
    pushNotification.putExtra("breakid", "");
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    str = pref.getString(Constant.LOGIN_CHECK);
    if (isAppIsInBackground(context)) {
       // Log.e("calling", "not app closedsnd notif");
        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
                calldialogdistance(statust);//temporarly blocked for switch purpose
            }
        }
    }else{
        //Log.e("calling", " app dsnd notif");
    }
}else if(sktype.contentEquals("send_notification_onduty"))
{
    String statust=data.getString("type");
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("estatus", ""+statust);
    pushNotification.putExtra("estatustype", "");
    pushNotification.putExtra("statustype", "");
    pushNotification.putExtra("breakid", "");
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    str = pref.getString(Constant.LOGIN_CHECK);
    if (isAppIsInBackground(context)) {
       // Log.e("calling", "not app closedsnd notif");
      //  ondutyalert();
        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
                if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    calldialogdonduty();//temporarly blocked for switch purpose
                }
            }
        }
    }else{
        //Log.e("calling", " app dsnd notif");
    }

}else if(sktype.contentEquals("send_notification_drive"))
{
    String statust=data.getString("type");
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("estatus", ""+statust);
    pushNotification.putExtra("estatustype", "");
    pushNotification.putExtra("statustype", "");
    pushNotification.putExtra("breakid", "");
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    str = pref.getString(Constant.LOGIN_CHECK);
    if (isAppIsInBackground(context)) {
        // Log.e("calling", "not app closedsnd notif");
        //  ondutyalert();
        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
                if(pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                    calldialogdrive();
                }
            }
        }
    }else{
        //Log.e("calling", " app dsnd notif");
    }

}else if(sktype.contentEquals("low_voltage"))
{
    String statust=data.getString("type");
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("estatus", "");
    pushNotification.putExtra("estatustype", "");
    pushNotification.putExtra("statustype", "");
    pushNotification.putExtra("breakid", "");
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
}

else if(sktype.contentEquals("send_speedo"))
{
    String statust=data.getString("type");
    Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
    pushNotification.putExtra("message", sktype);
    pushNotification.putExtra("estatus", ""+statust);
    pushNotification.putExtra("estatustype", "");
    pushNotification.putExtra("statustype", "");
    pushNotification.putExtra("breakid", "");
    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
}else if(sktype.contentEquals("unplugged"))
{
    str = pref.getString(Constant.LOGIN_CHECK);
    //Log.e("str",""+str);
    if (isAppIsInBackground(context)) {
      //  Log.e("app closed",""+str);
        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
              //  callunpluggeddialog();
                calldialogdunplug();
            }
        }
    }else{
        String statust=data.getString("type");
        Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
        pushNotification.putExtra("message", sktype);
        pushNotification.putExtra("estatus", ""+statust);
        pushNotification.putExtra("estatustype", "");
        pushNotification.putExtra("statustype", "");
        pushNotification.putExtra("breakid", "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

}else if(sktype.contentEquals("send_update_version"))
{
    str = pref.getString(Constant.LOGIN_CHECK);
    String statust=data.getString("type");
    //Log.e("str",""+str);
    if (isAppIsInBackground(context)) {
        //Log.e("app closed",""+str);
        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
                //  callunpluggeddialog();
            //    calldialogdunplug();
            }
        }
    }else{
        pref.putString(Constant.APP_AUTO_UPDATE,"1");
        pref.putString(Constant.APP_AUTO_UPDATE_MSG,""+statust);
        Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
        pushNotification.putExtra("message", sktype);
        pushNotification.putExtra("estatus", ""+statust);
        pushNotification.putExtra("estatustype", "");
        pushNotification.putExtra("statustype", "");
        pushNotification.putExtra("breakid", "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }
    appupdatenotification(statust);

}else if(sktype.contentEquals("location"))
{
    str = pref.getString(Constant.LOGIN_CHECK);
    //Log.e("str",""+str);
    if (isAppIsInBackground(context)) {
      //  Log.e("app closed",""+str);
        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
                //  callunpluggeddialog();
                //    calldialogdunplug();
            }
        }
    }else{
        String statust=data.getString("type");
        Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
        pushNotification.putExtra("message", sktype);
        pushNotification.putExtra("estatus", ""+statust);
        pushNotification.putExtra("estatustype", "");
        pushNotification.putExtra("statustype", "");
        pushNotification.putExtra("breakid", "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }

}else if(sktype.contentEquals("ask_for_login"))
{
//Log.e("sktype","@"+sktype);
  if(data.has("title"))
  {
      String  license=data.getString("title");

      if(license !=null && license.length()>0 && !license.contentEquals("null"))
      {

          if (isAppIsInBackground(context)) {
              callloginalert(license);

          }else{
              pref.putString(Constant.FORCE_LOGIN,"active");
              pref.putString(Constant.DRIVER_LISCENCE,""+license);
              String statust=data.getString("type");
              Intent pushNotification = new Intent(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION);
              pushNotification.putExtra("message", sktype);
              pushNotification.putExtra("estatus", "");
              pushNotification.putExtra("title", ""+license);
              pushNotification.putExtra("estatustype", "");
              pushNotification.putExtra("statustype", "");
              pushNotification.putExtra("breakid", "");
              LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
          }
      }

  }


}

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception_d: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception_d: " + e.getMessage());
        }
    }

    private void calldialogdonduty() {
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Responsemodel>> call = api.getValnotificationtest(did,vinnumber,lat,lon);

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if(response.isSuccessful()){

                    ondutyalert();
                }else{
                    ondutyalert();
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
               ondutyalert();
            }
        });
    }
    private void calldialogdunplug() {
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Responsemodel>> call = api.getValnotificationtest(did,vinnumber,lat,lon);

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if(response.isSuccessful()){

                    callunpluggeddialog();
                }else{
                    callunpluggeddialog();
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                calldialogdunplug();
            }
        });
    }
    private void calldialogdrive() {
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Responsemodel>> call = api.getValnotificationtest(did,vinnumber,lat,lon);

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if(response.isSuccessful()){

                    drivalert();
                }else{
                    drivalert();
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                drivalert();
            }
        });
    }
    private void calldialogdistance(final String statust) {
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Responsemodel>> call = api.getValnotificationtest(did,vinnumber,lat,lon);

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if(response.isSuccessful()){
                    setdlert(statust);
                }else{
                    setdlert(statust);

                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                 //Log.e("dd"," Response Error "+t.getMessage());

                setdlert(statust);

            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
// Get last known recent location.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Trucksoft_elog_Msg_Service.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {



            }
        }
        if(mGoogleApiClient.isConnected()) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            // Note that this can be NULL if last location isn't already known.
            if (mCurrentLocation != null) {
                // Print current location if not null
                //Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
                LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                lat = String.valueOf(mCurrentLocation.getLatitude());
                lon = String.valueOf(mCurrentLocation.getLongitude());

                if (sktype != null && sktype.length() > 0) {
                    if (sktype.contentEquals("send_location")) {
                        if (lat != null && lat.length() > 0 && !lat.contentEquals("null")) {
                           updateloc();
                        }
                        //logout();
                    }
                }
            }
            // Begin polling for new location updates.
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            //	Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            //Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
try{
        lat= String.valueOf(location.getLatitude());
        lon= String.valueOf(location.getLongitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
}catch (Exception e)
{

}
    }
    protected void startLocationUpdates() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(Trucksoft_elog_Msg_Service.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                }
            }
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL);
            // Request location updates
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }catch (Exception e)
        {

        }
    }







    public String printDifference(String startxDate, String endxDate)
    {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = null;

        try
        {
            startDate = simpleDateFormat.parse(startxDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        Date endDate = null;

        try
        {
            endDate = simpleDateFormat.parse(endxDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        return pad(elapsedHours) + ":" + pad(elapsedMinutes);
    }

    public static String pad(Long num)
    {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res =  "" + num;

        return res;
    }

    public static String padint(int num)
    {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res =  "" + num;

        return res;
    }
    public static boolean isAppIsInBackground(Context context) {
        isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void notification(String msgg,String status,String breakid) {

        try {


            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/speechtune");

            // Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Constant.NO_STATUS = 1;
        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note;
        Intent intent = new Intent(this, Home_activity_bluetooth.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notify","success");
        intent.putExtra("msgg",""+msgg);
        intent.putExtra("breakid",""+breakid);
        intent.putExtra("estatus",""+status);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

             pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        } else {
             pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }


        Notification.Builder builder = new Notification.Builder(this);

        builder.setAutoCancel(false);
        builder.setTicker("");
        builder.setContentTitle("Elogbook Notification");
        builder.setContentText(Html.fromHtml(msgg));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setSubText("");   //API level 16
        }
        builder.setNumber(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.build();
        } else {

            note = new Notification.Builder(this)
                    .setContentTitle("Title").setContentText("Text")
                    .setSmallIcon(R.mipmap.ic_launcher).getNotification();
        }

        note = builder.getNotification();
        mgr.notify(NOTIFY_ME_ID_DISPATCH, note);
    }
    public void cancelnotification()
    {



        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }
    public void cancelnotificationremark()
    {



        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_REMARK);

    }


//    private void remarkback(String status) {
//        if(status !=null && status.length()>0 && !status.contentEquals("null")) {
//        //Log.e("call","remarkdialog");
//        if (dialogrk != null) {
//            if (dialogrk.isShowing()) {
//                dialogrk.dismiss();
//            }
//        }
//        //Log.e("call","rem");
//        //LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//
//        //LayoutInflater inflater = context.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.remark_popup_dd, null);
//
//        final EditText name = dialogView.findViewById(R.id.rmark);
//        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
//        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
//        final Button btnthirtyhr = dialogView.findViewById(R.id.btn_thr);
//        btnthirtyhr.setVisibility(View.GONE);
//
//        final LinearLayout linbrk = dialogView.findViewById(R.id.linbreak);
//        if (status != null && status.length() > 0 && !status.contentEquals("null")) {
//            if (status.contentEquals("DRIVING")) {
//                linbrk.setVisibility(View.GONE);
//            }
//        }
//        final Button btntea = dialogView.findViewById(R.id.btn_tea);
//        final Button btnlunch = dialogView.findViewById(R.id.btn_lunch);
//        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
//        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
//        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
//        //td
//        dialogrk = new Dialog(this, R.style.DialogTheme);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            dialogrk.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        } else {
//            dialogrk.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        }
//        //dialog = new Dialog(this, R.style.DialogTheme);
//        //  Log.e("statuskk",""+status);
//        if (status != null && status.length() > 0 && !status.contentEquals("null")) {
//            if (status.contentEquals("ON_DUTY")) {
//                status = "ON DUTY";
//            } else if (status.contentEquals("DRIVING")) {
//                status = "DRIVING";
//            } else if (status.contentEquals("SLEEP")) {
//                status = "SLEEPER";
//            } else if (status.contentEquals("OFF_DUTY")) {
//                status = "OFF DUTY";
//                // status="DRIVING";
//            } else {
//                status = "DRIVING";
//            }
//        } else {
//            status = "OFF DUTY";
//        }
//        // Log.e("sttt",""+status);
//        if (status.contentEquals("ON DUTY")) {
//            imgstatus.setBackgroundResource(R.drawable.timeimg);
//        } else if (status.contentEquals("DRIVING")) {
//            imgstatus.setBackgroundResource(R.drawable.driveimg);
//        } else if (status.contentEquals("SLEEPER")) {
//            imgstatus.setBackgroundResource(R.drawable.sleepimg);
//        } else if (status.contentEquals("OFF DUTY")) {
//            imgstatus.setBackgroundResource(R.drawable.offimg);
//        }
//        txtstatus.setText("STATUS : " + currentstatus);
//        dialogrk.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogrk.setContentView(dialogView);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialogrk.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//        try {
//            dialogrk.getWindow().setAttributes(lp);
//            if((status.contentEquals("ON DUTY")||status.contentEquals("ON_DUTY")) && pref.getString(Constant.VOICE_ON).contentEquals("1")) {
//                dialogrk.show();
//            }else if((status.contentEquals("DRIVING")||status.contentEquals("DRIVE") ) && pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
//                dialogrk.show();
//            }else if((status.contentEquals("OFF_DUTY")||status.contentEquals("OFF DUTY")) && pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
//                dialogrk.show();
//            }else if((status.contentEquals("SLEEPER")||status.contentEquals("SLEEP")) && pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
//                dialogrk.show();
//            }
//        } catch (Exception e) {
//            //Log.e("dialog error",""+e.toString());
//        }
//        btntea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                breaktype = "TEA";
//                btntea.setBackgroundColor(btntea.getContext().getResources().getColor(R.color.orange));
//                btnlunch.setBackgroundColor(btnlunch.getContext().getResources().getColor(R.color.ts));
//                btnthirtyhr.setBackgroundColor(btnlunch.getContext().getResources().getColor(R.color.ts));
//
//
//                String did = pref.getString(Constant.DRIVER_ID);
//                // Log.e("did",""+did);
//
//
//                vinnumber = pref.getString(Constant.VIN_NUMBER);
//
////                api = DispatchServiceGenerator.createService(ReportApi.class);
////                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
////                Call<List<Remark_model>> call = api.saveremark(vinnumber,currentstatus,lat,lon,did,""+name.getText().toString().trim(),breaktype);
////
////                call.enqueue(new Callback<List<Remark_model>>() {
////                    @Override
////                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
////                        if(response.isSuccessful()){
////                            dialogrk.dismiss();
////                            responseremark(currentstatus);
////                            cancelnotificationremark();
////                            Toast.makeText(context,"Remark saved successfully",Toast.LENGTH_SHORT).show();
////
////                            //  dialogz.dismiss();v
////                        }else{
////
////                            // dialogz.dismiss();
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
////                        //Log.e("dd"," Response Error "+t.getMessage());
////                        // dialogz.dismiss();
////
////                    }
////                });
//
//
//                api = DispatchServiceGenerator.createService(ReportApi.class);
//                // String hlink="http://e-logbook.info//elog_app/get_lunchbreak_save.php?";
////
//                // Log.e("urlz",hlink+"vin="+vinnumber+"&fname="+field+"&statusid"+statusid
//                //       +"&pc_status="+statuss+"&lat="+lat+"&lon="+lon+"&did="+did+"&address="+address);
//                Call<List<Responsemodel>> call = api.getsaveteaValues_eld(vinnumber, "", "", "3", lat, lon, did, "", "","","");
//
//                call.enqueue(new Callback<List<Responsemodel>>() {
//                    @Override
//                    public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
//                        if (response.isSuccessful()) {
//                            dialogrk.dismiss();
//                            responseremark(currentstatus);
//                            cancelnotificationremark();
//                            Toast.makeText(context, "Remark saved successfully", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            responseremark(currentstatus);
//                            cancelnotificationremark();
//                            Toast.makeText(context, "Remark saved successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
//
//                    }
//                });
//
//
//            }
//        });
////        btnthirtyhr.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                breaktype="THIRTY MINUTES";
////                btntea.setBackgroundColor(btntea.getContext().getResources().getColor(R.color.ts));
////                btnlunch.setBackgroundColor(btnlunch.getContext().getResources().getColor(R.color.ts));
////                btnthirtyhr.setBackgroundColor(btnlunch.getContext().getResources().getColor(R.color.orange));
////
////                String did=pref.getString(Constant.DRIVER_ID);
////                // Log.e("did",""+did);
////
////
////
////                vinnumber=pref.getString(Constant.VIN_NUMBER);
////
////                api = DispatchServiceGenerator.createService(ReportApi.class);
////                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
////                Call<List<Remark_model>> call = api.saveremark(vinnumber,currentstatus,lat,lon,did,""+name.getText().toString().trim(),breaktype);
////
////                call.enqueue(new Callback<List<Remark_model>>() {
////                    @Override
////                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
////                        if(response.isSuccessful()){
////                            dialogrk.dismiss();
////                            responseremark(currentstatus);
////                            cancelnotificationremark();
////                            Toast.makeText(context,"Remark saved successfully",Toast.LENGTH_SHORT).show();
////
////                            //  dialogz.dismiss();
////                        }else{
////
////                            // dialogz.dismiss();
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
////                        //Log.e("dd"," Response Error "+t.getMessage());
////                        // dialogz.dismiss();
////
////                    }
////                });
////
////            }
////        });
//        btnlunch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                breaktype = "LUNCH";
//                btntea.setBackgroundColor(btntea.getContext().getResources().getColor(R.color.ts));
//                btnlunch.setBackgroundColor(btnlunch.getContext().getResources().getColor(R.color.orange));
//                btnthirtyhr.setBackgroundColor(btnlunch.getContext().getResources().getColor(R.color.ts));
//
//
//                String did = pref.getString(Constant.DRIVER_ID);
//                // Log.e("did",""+did);
//
//
//                vinnumber = pref.getString(Constant.VIN_NUMBER);
//
////                    api = DispatchServiceGenerator.createService(ReportApi.class);
////                    //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
////                    Call<List<Remark_model>> call = api.saveremark(vinnumber,currentstatus,lat,lon,did,""+name.getText().toString().trim(),breaktype);
////
////                    call.enqueue(new Callback<List<Remark_model>>() {
////                        @Override
////                        public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
////                            if(response.isSuccessful()){
////                                dialogrk.dismiss();
////                                responseremark(currentstatus);
////                                cancelnotificationremark();
////                                Toast.makeText(context,"Remark saved successfully",Toast.LENGTH_SHORT).show();
////
////                                //  dialogz.dismiss();
////                            }else{
////
////                                // dialogz.dismiss();
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<List<Remark_model>> call, Throwable t) {
////                            //Log.e("dd"," Response Error "+t.getMessage());
////                            // dialogz.dismiss();
////
////                        }
////                    });
////
//
//
//                api = DispatchServiceGenerator.createService(ReportApi.class);
//                Call<List<Responsemodel>> call = api.getsavelunchValues_eld(vinnumber, "OFF_DUTY", "" + "", "3", lat, lon, did, "", "","","");
//
//                call.enqueue(new Callback<List<Responsemodel>>() {
//                    @Override
//                    public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
//                        if (response.isSuccessful()) {
//                            dialogrk.dismiss();
//                            responseremark(currentstatus);
//                            cancelnotificationremark();
//                            Toast.makeText(context, "Remark saved successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            dialogrk.dismiss();
//                            responseremark(currentstatus);
//                            cancelnotificationremark();
//                            Toast.makeText(context, "Remark saved successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
//                        //Log.e("dd"," Response Error "+t.getMessage());
//
//                    }
//                });
//
//
//            }
//        });
//        btnsubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String did = pref.getString(Constant.DRIVER_ID);
//                // Log.e("did",""+did);
//
//
//                vinnumber = pref.getString(Constant.VIN_NUMBER);
//
//                api = DispatchServiceGenerator.createService(ReportApi.class);
//                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
//                Call<List<Remark_model>> call = api.saveremark(vinnumber, currentstatus, lat, lon, did, "" + name.getText().toString().trim(), breaktype);
//
//                call.enqueue(new Callback<List<Remark_model>>() {
//                    @Override
//                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
//                        if (response.isSuccessful()) {
//                            dialogrk.dismiss();
//                            responseremark(currentstatus);
//                            cancelnotificationremark();
//                            Toast.makeText(context, "Remark saved successfully", Toast.LENGTH_SHORT).show();
//
//                            //  dialogz.dismiss();
//                        } else {
//
//                            // dialogz.dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
//                        //Log.e("dd"," Response Error "+t.getMessage());
//                        // dialogz.dismiss();
//
//                    }
//                });
//
//            }
//        });
//        btncancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogrk.dismiss();
//            }
//        });
//    }
//    }
    private void gettodaynotif()
    {
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
       // Log.e("didk",""+did);
//        dialogz = new ProgressDialog(context,
//                AlertDialog.THEME_HOLO_LIGHT);
//        dialogz.setMessage("Please wait...");
//       // dialogz.setCancelable(false);
//        dialogz.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Getvalue_model>> call = api.getValues_eld(did,vinnumber,"","",lat,lon,"","","","","","fcmservic","");

        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if(response.isSuccessful()){
                    //dialogz.dismiss();
                    movies=response.body();
                    //setVal(movies);
                    //Log.e("respkk","success");//dd
                    settodayval(movies);
                    savestate();
                }else{
                    savestate();
                    //  dialogz.dismiss();
                    gettodaynotif();
                    //  getvehicle();
                    //Log.e("ss"," Response "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Getvalue_model>> call, Throwable t) {
                // Log.e("dd"," Response Error "+t.getMessage());
                // dialogz.dismiss();
                gettodaynotif();
                // getvehicle();
            }
        });
    }


    private void savestate() {
        String gp_status="0";
        try{
            CheckGpsStatus() ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                // Do something for lollipop and above versions
                if(!GpsStatus)
                {
                    gp_status="1";
                    // turnGPSOn();activate gps
                    //  getMyLocation();
                }else{
                    gp_status="0";
                }
            } else{
                gp_status="0";
                // do something for phones running an SDK before lollipop
            }
        }catch (Exception e)
        {

        }
        String svadd="";
        String address ="";
        if(lat !=null && lat.length()>0) {
             address = getAddressFromLocation(Double.parseDouble(lat), Double.parseDouble(lon));
        }else{

        }
        if(address !=null && strstate !=null && strstate.length()>0)
        {
            svadd=pref.getString(Constant.STATE_FIELD);
        }
        String vin = pref.getString(Constant.VIN_NUMBER);
        // Log.e("dccc","@"+dc);2019-01-14
        Call<List<Faultcode_model>> call = api.savestates(pref.getString(Constant.DRIVER_ID), strstate, vin,""+svadd,"",""+lat,""+lon,""+address,gp_status);
        call.enqueue(new Callback<List<Faultcode_model>>() {
            @Override
            public void onResponse(Call<List<Faultcode_model>> call, Response<List<Faultcode_model>> response) {
                if (response.isSuccessful()) {
                    List<Faultcode_model> result = response.body();
                    if (result.size() > 0) {

                    } else {

                    }
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Faultcode_model>> call, Throwable t) {
                // Log.e("ddd"," Load More Response Error "+t.getMessage());
            }
        });
    }
    public String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                if (address.getAddressLine(0) != null && address.getAddressLine(0).length() > 0 && !address.getAddressLine(0).contentEquals("null")) {
                    sb.append(address.getAddressLine(0)).append("\n");

                    try {
                        strstate = address.getAdminArea();
                        //Log.e("state","#"+strstate);
                    } catch (Exception e) {

                    }
                } else {

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    try {
                        strstate = address.getAdminArea();
                        //  Log.e("state","@"+strstate);
                    } catch (Exception e) {

                    }
                }
                straddress = sb.toString();
                //Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }


        return straddress;
    }
    private void settodayval(List<Getvalue_model> vals) {
        SimpleDateFormat formatime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strsl = formatime.format(new Date());
        if (vals.size() > 0 && vals != null) {
            for (int i = 0; i < vals.size(); i++) {
                Getvalue_model gt = new Getvalue_model();
                gt = vals.get(i);
                currentstatus=gt.getstatus();
                pref.putString(Constant.VOICE_OFF,""+gt.getVoiceoff());
                pref.putString(Constant.VOICE_ON,""+gt.getVoiceon());
                pref.putString(Constant.VOICE_SLEEP,""+gt.getVoicesleep());
                pref.putString(Constant.VOICE_DRIVE,""+gt.getVoicedrive());


String rmark=gt.getremark();
if(rmark !=null && rmark.length()>0 && !rmark.contentEquals("null"))
{

}else {
   // statusnotification(currentstatus);
   // remarkback(currentstatus);


    if(str_remarkstatus !=null && str_remarkstatus.length()>0 && !str_remarkstatus.contentEquals("null"))
    {
        if(str_remarkstatus.contentEquals(currentstatus))
        {
            int_remarkcount=pref.getInt(Constant.REMARK_COUNT);

            if(int_remarkcount<1)
            {
                int_remarkcount++;
                pref.putString(Constant.REMARK_STATUS,""+currentstatus);
                pref.putInt(Constant.REMARK_COUNT,int_remarkcount);
                statusnotification(currentstatus);
              //  remarkback(currentstatus);
            }
        }else{
            str_remarkstatus=currentstatus;
            int_remarkcount=1;
            pref.putString(Constant.REMARK_STATUS,""+currentstatus);
            pref.putInt(Constant.REMARK_COUNT,int_remarkcount);
           // remarkback(currentstatus);
            statusnotification(currentstatus);
        }

    }else
    {
        str_remarkstatus=currentstatus;
        int_remarkcount=1;
        pref.putString(Constant.REMARK_STATUS,""+currentstatus);
        pref.putInt(Constant.REMARK_COUNT,int_remarkcount);
        //remarkback(currentstatus);
        statusnotification(currentstatus);
    }






}
            }
        }
    }
    private void statusnotification(String currentstatus){

       // Log.e("succcess", "************************");
pref.putString(Constant.STATUS_DD,""+currentstatus);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String status=currentstatus;
            try {
                Uri alarmSound =null;
                if(status.contentEquals("ON DUTY")) {
                    if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
                    }
                }else if(status.contentEquals("ON_DUTY")) {
                    if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
                    }
                }else if(status.contentEquals("DRIVING"))
                {
                    if(pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/drivv");
                    }
                }else if(status.contentEquals("SLEEP"))
                {
                    //Log.e("statusssskk",""+status);
                    if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
                    }
                }else if(status.contentEquals("SLEEPER"))
                {
                   // Log.e("statusssskk",""+status);
                    if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
                    }
                }else if(status.contentEquals("OFF DUTY"))
                {
                    if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
                    }
                }else if(status.contentEquals("OFF_DUTY"))
                {
                    if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
                    }
                }
                if(alarmSound !=null) {
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                    r.play();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//////////////////////////////////////////////////////////////////////////////////////////////////////

            if(status.contentEquals("ON DUTY")) {
                if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }else if(status.contentEquals("ON_DUTY")) {
                if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }else if(status.contentEquals("DRIVING"))
            {
                if(pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }else if(status.contentEquals("SLEEP"))
            {
                //Log.e("statusssskk",""+status);
                if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }else if(status.contentEquals("SLEEPER"))
            {
                // Log.e("statusssskk",""+status);
                if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }else if(status.contentEquals("OFF DUTY"))
            {
                if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }else if(status.contentEquals("OFF_DUTY"))
            {
                if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                    setnot1(currentstatus);
                }
            }







        } else {

            String status=currentstatus;
            try {
                Uri alarmSound =null;
                if(status.contentEquals("ON DUTY")) {
                    if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
                    }
                }else if(status.contentEquals("ON_DUTY")) {
                    if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
                    }
                }else if(status.contentEquals("DRIVING"))
                {
                    if(pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/drivv");
                    }
                }else if(status.contentEquals("SLEEP"))
                {
                    //Log.e("statusssskk",""+status);
                    if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
                    }
                }else if(status.contentEquals("SLEEPER"))
                {
                    // Log.e("statusssskk",""+status);
                    if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
                    }
                }else if(status.contentEquals("OFF DUTY"))
                {
                    if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
                    }
                }else if(status.contentEquals("OFF_DUTY"))
                {
                    if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
                    }
                }
                if(alarmSound !=null) {
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                    r.play();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            if(status.contentEquals("ON DUTY")) {
                if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }else if(status.contentEquals("ON_DUTY")) {
                if(pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }else if(status.contentEquals("DRIVING"))
            {
                if(pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }else if(status.contentEquals("SLEEP"))
            {
                //Log.e("statusssskk",""+status);
                if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }else if(status.contentEquals("SLEEPER"))
            {
                // Log.e("statusssskk",""+status);
                if(pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }else if(status.contentEquals("OFF DUTY"))
            {
                if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }else if(status.contentEquals("OFF_DUTY"))
            {
                if(pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                    setnot2(currentstatus);
                }
            }






        }
    }
    private void setnot2(String currentstatus)
    {

        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification note;
        Intent intent = new Intent(this, Home_activity_bluetooth.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE );

        } else {
            pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setAutoCancel(false);
        builder.setTicker("");
        builder.setContentTitle("E-logbook");
        builder.setContentText("Status "+currentstatus+" changed");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setSubText("");   //API level 16
        }
        builder.setNumber(100);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.build();
        } else {

            note = new Notification.Builder(this)
                    .setContentTitle("Title").setContentText("Text")
                    .setSmallIcon(R.mipmap.ic_launcher).getNotification();
        }

        note = builder.getNotification();
        mgr.notify(NOTIFY_ME_ID_REMARK, note);
    }
    private void setnot1(String currentstatus)
    {

        Notification.Builder nb=new Notification.Builder(this);
        Intent intent = new Intent(this, Home_activity_bluetooth.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE );

        } else {
            pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
       // PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
//

        nb= mNotificationUtils.
                getAndroidChannelNotification("E-logbook", "Status "+currentstatus+"changed");
        nb.setContentIntent(pendingIntent);
        //nb.setDefaults(0);
        //nb.setOngoing(true);
        mNotificationUtils.getManager().notify(NOTIFY_ME_ID_REMARK, nb.build());
    }
    private void  setdlert(String mils)
    {
        //Log.e("call","remarkdialog");
        if(dialogdistance !=null) {
            if (dialogdistance.isShowing()) {
                dialogdistance.dismiss();
            }
        }
        Double reslt=00.00;
        if(mils !=null && mils.length()>0 && !mils.contentEquals("null"))
        {
            Double dk=Double.parseDouble(mils);
            reslt=roundTwoDecimals(dk);
        }
        String name=pref.getString(Constant.MODEL_NAME);
        //Log.e("call","rem");
        //LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.distance_notification, null);

        //final EditText name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
       // final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus=dialogView.findViewById(R.id.txt_img);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //td
        dialogdistance = new Dialog(this, R.style.DialogTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogdistance.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialogdistance.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        txtstatus.setText("Your Vehicle "+ name +"distance  "+reslt+" Miles ");
        dialogdistance.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdistance.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogdistance.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        try {
            dialogdistance.getWindow().setAttributes(lp);
            dialogdistance.show();
        }catch (Exception e)
        {
            //Log.e("dialog error",""+e.toString());
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did=pref.getString(Constant.DRIVER_ID);
                String type="DISTANCE";


                api = ApiServiceGenerator.createService(Eld_api.class);
               // Log.e("url","&did="+did+"&name="+type);
                Call<List<Remark_model>> call = api.updatenotification(did,""+type);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        if(response.isSuccessful()){

                            dialogdistance.dismiss();
                        }else{

                            dialogdistance.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                       // Log.e("dd"," Response Error "+t.getMessage());
                        dialogdistance.dismiss();
//setdistancealert(mils);
                    }
                });

            }
        });


    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    private void ondutyalert()
    {
        //Log.e("call","remarkdialog");
        if(dialoonduty !=null) {
            if (dialoonduty.isShowing()) {
                dialoonduty.dismiss();
            }
        }

        //Log.e("call","rem");
        //LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.onduty_notification, null);

        //final EditText name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        // final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus=dialogView.findViewById(R.id.txt_img);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //td
        dialoonduty = new Dialog(this, R.style.DialogTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialoonduty.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialoonduty.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        txtstatus.setText("Your's today's ONDUTY total hour's exceeded limit");
        dialoonduty.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoonduty.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoonduty.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        try {
            dialoonduty.getWindow().setAttributes(lp);
            dialoonduty.show();
        }catch (Exception e)
        {
            //Log.e("dialog error",""+e.toString());
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did=pref.getString(Constant.DRIVER_ID);


                String type="ON_DUTY";
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
                Call<List<Remark_model>> call = api.updatenotification(did,""+type);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        dialoonduty.dismiss();
                        if(response.isSuccessful()){


                        }else{

                            //dialoonduty.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                        dialoonduty.dismiss();
                      //  Log.e("ded"," Response Error "+t.getMessage());

//setondutyalert();
                    }
                });

            }
        });


    }

    private void drivalert()
    {//tm
        //Log.e("call","remarkdialog");
        if(dialogdrive !=null) {
            if (dialogdrive.isShowing()) {
                dialogdrive.dismiss();
            }
        }

        //Log.e("call","rem");
        //LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.driving_notification, null);

        //final EditText name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        // final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus=dialogView.findViewById(R.id.txt_img);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //td
        dialogdrive = new Dialog(this, R.style.DialogTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogdrive.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialogdrive.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        txtstatus.setText("Your's today's DRIVE total hour's exceeded limit");
        dialogdrive.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdrive.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogdrive.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        try {
            dialogdrive.getWindow().setAttributes(lp);
            dialogdrive.show();
        }catch (Exception e)
        {
            //Log.e("dialog error",""+e.toString());
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did=pref.getString(Constant.DRIVER_ID);


                String type="DRIVING";
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
                Call<List<Remark_model>> call = api.updatenotification(did,""+type);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {

                        dialogdrive.dismiss();
                        if(response.isSuccessful()){
                            //dialogdrive.dismiss();

                        }else{

                            //dialogdrive.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                        dialogdrive.dismiss();
                      //  Log.e("ded"," Response Error "+t.getMessage());

//setondutyalert();
                    }
                });

            }
        });


    }


    private void responseremark(String status){
        if(dialogresp !=null) {
            if (dialogresp.isShowing()) {
                dialogresp.dismiss();
            }
        }
       // Log.e("statuss",""+status);
      //  Log.e("statusssize",""+status.length());
        String val="";
        if(status.contentEquals("OFF_DUTY"))
        {
            //Log.e("level",""+"OFF_DUTY");
            val="Nice work. Get some rest please. Thank you";
            setalaramtatu("offresp");
        }else if(status.contentEquals("SLEEP"))
        {
            val="Get some Rest";
            setalaramtatu("sleepresp");
        }else if(status.contentEquals("DRIVING"))
        {
            val="Have a safe Drive";
            setalaramtatu("driveresp");
        }else if(status.contentEquals("ON_DUTY"))
        {
            val="Have a Nice day";
            setalaramtatu("onresp");
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       // LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remark_response, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        final  TextView txtlevel=dialogView.findViewById(R.id.txt_level);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogresp = new Dialog(context, R.style.DialogTheme);
        txtlevel.setText("STATUS : "+status);
        txtstatus.setText(""+val);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogresp.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialogresp.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        dialogresp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogresp.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogresp.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        try {
            dialogresp.getWindow().setAttributes(lp);
            dialogresp.show();
        }catch (Exception e)
        {
            //Log.e("dialog error",""+e.toString());
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogresp.dismiss();
            }
        });


    }

private void setalaramtatu(String voice)
{
    try {
        Uri alarmSound =null;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/"+voice);

        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
        r.play();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
private void sendlocation()
{

}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
private void applogout()
{
    Log.e("called","************logout");
    api = ApiServiceGenerator.createService(Eld_api.class);
    String did=pref.getString(Constant.DRIVER_ID);
    String vin=pref.getString(Constant.VIN_NUMBER);
    cancelnotification();

  //  Log.e("url","&did="+did+"&name="+type);
    Call<List<Remark_model>> call = api.applogout(did,""+vin,did,lat,lon);

    call.enqueue(new Callback<List<Remark_model>>() {
        @Override
        public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
            if(response.isSuccessful()){
//Log.e("msg","logout successfully");

            }else{


            }
        }

        @Override
        public void onFailure(Call<List<Remark_model>> call, Throwable t) {
            //Log.e("dd"," Response Error "+t.getMessage());

//setdistancealert(mils);
        }
    });
    Log.e("called","************logout1");
    pref = Preference.getInstance(context);
     //Log.e("login", "fail");
    pref.putString(Constant.LOGIN_CHECK,
            "logged_off");
    pref.putString(Constant.BLUETOOTH_CONNECTED,"0");
    pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
    pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
    pref.putString(Constant.DRIVE_NOTIFICATON, "0");
    pref.putString(Constant.VIN_NUMBER, "");
    pref.putString(Constant.BLUETOOTH_ADDRESS,"");
    pref.putString(Constant.BLUETOOTH_NAME,"");
    pref.putString(Constant.NETWORK_TYPE,Constant.CELLULAR);


try
{
    Log.e("cdr","@"+"ok");
    ((Activity) context).finish();
    ((Activity) context).finishAffinity();
//    Intent mIntent = new Intent(getApplicationContext(),
//            Loginactivitynew.class);
//     //mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
//    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    mIntent.putExtra("EXIT", true);
//    getApplicationContext().startActivity(mIntent);
    ((Activity) context).finishAffinity();
}catch (Exception e)
{
    Log.e("cdr","@"+e.toString());
}

   // ((Activity) context). finishAndRemoveTask();
}

    // System.exit(0);

    public static void setActivityContext(Context context){
        myContext = context;
    }



    private void updateloc()
    {
        api = ApiServiceGenerator.createService(Eld_api.class);
        String did=pref.getString(Constant.DRIVER_ID);
        String vin=pref.getString(Constant.VIN_NUMBER);
        cancelnotification();

          Log.e("url","cur_loc_drv.php?driverid="+did+"&vin="+vin+"&lat="+lat+"&long="+lon);
        Call<List<Location_model>> call = api.updatelocation(did,""+vin,lat,lon);

        call.enqueue(new Callback<List<Location_model>>() {
            @Override
            public void onResponse(Call<List<Location_model>> call, Response<List<Location_model>> response) {
                if(response.isSuccessful()){
//Log.e("msg","logout successfully");

                }else{


                }
            }

            @Override
            public void onFailure(Call<List<Location_model>> call, Throwable t) {
               // Log.e("dd"," Response Error "+t.getMessage());


            }
        });

    }

    private void callloginalert(String license) {
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Responsemodel>> call = api.getkd();

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if(response.isSuccessful()){

                    setforceloginalert(license);
                }else{
                    setforceloginalert(license);
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                setforceloginalert(license);
            }
        });
    }

    private void setforceloginalert(String license)
    {
        if (dialoglogi != null) {
            if (dialoglogi.isShowing()) {
                dialoglogi.dismiss();
            }
        }
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.login_alert, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("I see that you are moving. Did you need me to login to your logbook?");
         //final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialoglogi = new Dialog(context, R.style.DialogTheme);
        dialoglogi.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialoglogi.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialoglogi.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
       // dialoglogi.setCancelable(false);

        //dialog = new Dialog(this, R.style.DialogTheme);
        //Log.e("inttt","kk");

        dialoglogi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglogi.setContentView(dialogView);
        //Log.e("inttt","gg");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoglogi.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        //Log.e("inttt","mm");

        try {
            dialoglogi.getWindow().setAttributes(lp);
           // Log.e("inttt","kkfdfjfg");
            dialoglogi.show();
        }catch (Exception e)
        {
            //Log.e("dialog error","@"+e.toString());
        }
       // Log.e("inttt","kkzz");
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoglogi.dismiss();
                logout_ok();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoglogi.dismiss();
                pref.putString(Constant.FORCE_LOGIN,"active");
                pref.putString(Constant.DRIVER_LISCENCE,""+license);
                Intent mIntent = new Intent(getApplicationContext(),
                        Loginactivitynew.class);
                // mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtra("EXIT", true);
                getApplicationContext().startActivity(mIntent);
              //  ((Activity) context).finish();
            }
        });

    }
    private void callunpluggeddialog()
    {//tm
        //Log.e("call","remarkdialog");
        if(dialogunplug !=null && dialogunplug.isShowing()) {
            dialogunplug.dismiss();
            }


        //Log.e("call","rem");
        //LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.unplugg, null);

        //final EditText name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);

    dialogunplug = new Dialog(this, R.style.DialogTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        dialogunplug.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
        dialogunplug.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        dialogunplug.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogunplug.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogunplug.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        try {
        dialogunplug.getWindow().setAttributes(lp);
        dialogunplug.show();
        }catch (Exception e)
        {
            //Log.e("dialog error",""+e.toString());
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dialogunplug.dismiss();
        }
        });


    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

       // Log.e("call","update createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.appupdatetone);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(sound, attributes);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void appupdatenotification(String mesg)
    {
       // Log.e("call","update notification");
        createNotificationChannel();
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.appupdatetone);


        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, Appupdateclasselog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE );

        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
       // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App update available")
                .setContentText(""+mesg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFY_ME_APP_UPDATE, builder.build());


    }

    public void logout_ok()
    {
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.ELOG_NUMBERSS,
                "");
        //pref.getString(Constant.ELOG_NUMBERSS);
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        SimpleDateFormat formatsec= new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dc =formatsec.format(new Date());
        api = ApiServiceGenerator.createService(Eld_api.class);
        //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
        Call<JsonObject> call = api.getlogout(""+pref.getString(Constant.DRIVER_ID), ""+pref.getString(Constant.DRIVER_ID)
                , ""+pref.getString(Constant.VIN_NUMBER), "" + dc, "" , "","","","" );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                Log.e("Responsestring", responsez.body().toString());
                //Toast.makeText()
                if (responsez.isSuccessful()) {

                    if (responsez.body() != null) {
                        String jsonresponse = responsez.body().toString();
                        //Log.e("jsonresponse", jsonresponse.toString());
                        try {
                            JSONObject response = new JSONObject(jsonresponse);
                            try {

                                Log.e("responselog", "" + response.toString());
                                if (response != null) {

                                    String status = response
                                            .getString("status");




                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            //Log.e("Exceptionwwwwwwww", e.toString());
                        }


                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                //Log.e("Exceptionwttttttt", t.toString());
                // cancelprogresssdialogz();
            }
        });
    }

    public void CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void gettodaysavevaluesbluetooth(final String field, final int statusid, final String statuss, String rmark, String trckval, String lati, String longi) {
        //Log.e("call bapi", "@");
        //Log.e("vinnumber", "@"+vinnumber);
        String did=pref.getString(Constant.DRIVER_ID);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        if(vinnumber !=null &&vinnumber.length()>0)
        {
            try {
                //Log.e("latlat1", "@" + lat)
                if(lati !=null)
                {
                    double latitude = Double.parseDouble(lati);
                    double longitude = Double.parseDouble(longi);
                    getAddressFromLocation(latitude, longitude);
                }else {
                    CheckGpsStatus();
                    if (GpsStatus) {
                        if (lat != null) {
                            //Log.e("calling1", "" + address);
                            double latitude = Double.parseDouble(lat);
                            double longitude = Double.parseDouble(lon);
                            getAddressFromLocation(latitude, longitude);
                        }
                    }else{
                        lat="";
                        lon="";
                        straddress="";
                    }
                }
            } catch (Exception e) {

            }
            apicall = 1;



            api = ApiServiceGenerator.createService(Eld_api.class);


            String sk = "";

            sk = ">>" + pref.getString(Constant.CURRENT_STATUS_BB);

            Call<List<Getvalue_model>> call = api.getsaveValues_eldbluetooth(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress, "", strstate, "", "", "bluetooth", rmark, field + sk, "" + trckval,"break");
            call.enqueue(new Callback<List<Getvalue_model>>() {
                @Override
                public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                    if (response.isSuccessful()) {

                        movies = response.body();

                    } else {
                        apicall = 0;
                        //Log.e("respkk", "empty");
                    }

                }

                @Override
                public void onFailure(Call<List<Getvalue_model>> call, Throwable t) {
                    //Log.e("respkk", "##" + t.toString());
                    apicall = 0;
                }
            });
        }
    }

    public long splittimeseconds(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
            String timeSplit[] = time.split(":");

            seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60+Integer.parseInt(timeSplit[2]);

        }
        return seconds;

    }

}
