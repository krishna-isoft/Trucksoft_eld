package com.trucksoft.isoft.isoft_elog.Services;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_activity.Transparent_breaknew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Break_info_model;
import com.isoft.trucksoft_elog.Model_class.Getvalue_model;
import com.isoft.trucksoft_elog.Model_class.newbrk_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Myworkplan extends Worker implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnCompleteListener<Void> {
    private static final String WORK_RESULT = "work_result";
    private Context context;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private String straddress="";
    Eld_api api;
    Preference pref;
    String strstate="";
    LocationManager locationManager;
    boolean GpsStatus ;
    String gp_status="0";
    int ik=0;
    static boolean isInBackground = true;
    List<Getvalue_model> movies;
    /**
     * The list of geofences used in this sample.
     */
    private ArrayList<Geofence> mGeofenceList;
    private CommonUtil commonUtil;

    public Myworkplan(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        //Log.e("callllllllll","context");

        super(context, workerParams);
        this.context=context;
        pref=Preference.getInstance(context);
        commonUtil=new CommonUtil(context);
        movies=new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
       // Log.e("callllllllll","context");
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Result doWork() {
        showNotification("elog", "Test");

        String str = pref.getString(Constant.LOGIN_CHECK);

        if (str != null && str.length() > 0 && !str.contentEquals("null")) {
            if (str.equalsIgnoreCase("logged_inn")) {
                if (isAppIsInBackground(context)) {
                    //Log.e("ddddd","back execute");
                  //  checkbreak();
                    try {
                        if (pref.getString(Constant.CURRENT_STATUS).contentEquals("" + commonUtil.OFF_DUTY)
                                || pref.getString(Constant.CURRENT_STATUS).contentEquals("" + commonUtil.SLEEP)) {

                        } else {
                            checkbreak();
                        }
                    }catch (Exception e)
                    {

                    }
                    //checkondutyalert();
                    callbreakrelease();
                }
            }
        }

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
//Log.e("outputData","&"+outputData.toString());
        return Result.success(outputData);

    }


    private void showNotification(String task, String desc) {
        //Log.e("dddddeeeeeeeeeee","service execute");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
//        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        String channelId = "task_channel";
//        String channelName = "task_name";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            NotificationChannel channel = new
//                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
//                .setContentTitle(task)
//                .setContentText(desc+strDate)
//                .setSmallIcon(R.mipmap.ic_launcher);

      //  manager.notify(1, builder.build());
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
        ik=0;
        if(lat !=null && lat.length()>0) {
            ik=1;
            savelocation("work");
        }

    }



    @Override
    public void onLocationChanged(Location location)
    {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        if(lat !=null && lat.length()>0 && !lat.contentEquals("null")) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                    //txtcity.setText("C.City :" + addresses.get(0).getLocality());
                } else {
                    // do your stuff
                }
            } catch (Exception e) {

            }
            try {
                //Log.e("latlat2", "@" + lat);
                if (lat != null) {
                    // Log.e("calling", "" + address);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);

                    getAddressFromLocation(latitude,longitude);

                }
            }catch (Exception e)
            {

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

        //pd.dismiss();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                  //  txtcity.setText("C.City :"+addresses.get(0).getLocality());
                    //txtcity.setText("Current City : Visalia");
                }
                else {
                    // do your stuff
                }
            }catch (Exception e)
            {

            }
            try {
                // Log.e("latlat1", "@" + lat);
                if (lat != null) {
                    // Log.e("calling", "" + address);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
                    getAddressFromLocation(latitude,longitude);
                    if(ik==0) {
                        ik=1;
                        savelocation("cn");
                    }
                }
            }catch (Exception e)
            {

            }

        }
    //    callgeo();
        //Log.e("latlat1", "@" + lat);
       // Log.e("lon", "@" + lon);
      //  Log.e("lonaddress", "@" + straddress);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        //pd.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        // pd.dismiss();
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
                        pref.putString(Constant.CURRENT_STATE,""+strstate);
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


    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    public  void savelocation(String cn) {
        //Log.e("callk",""+"savelocation"+"  "+cn);

        try {
        String oldtime=pref.getString(Constant.LOCATION_UPDATE_DATE);

        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String ctime = formatdatetime.format(new Date());
        //Log.e("ctime","&"+ctime);
      String difftime=  printDifference(oldtime,ctime);
            //Log.e("difftime","&"+difftime);
      String updatetime=pref.getString(Constant.LOCATION_UPDATE_STATUS);
            //Log.e("updatetime","&"+updatetime);

      long  intupdate=splittimeminutes(updatetime);
            //Log.e("intupdate","&"+intupdate);
      long intdiff=splittime(difftime);
            //Log.e("intdiff###","&"+intdiff);
            intdiff=intdiff+300;
            //Log.e("intdiff","&"+intdiff);
      if(intupdate<=intdiff || intupdate<0) {
          //Log.e("condi","true");

          pref.putString(Constant.LOCATION_UPDATE_DATE, "" + ctime);


          String str = pref.getString(Constant.LOGIN_CHECK);

          if (str != null && str.length() > 0 && !str.contentEquals("null")) {
              if (str.equalsIgnoreCase("logged_inn")) {
                  ik = 1;
                  if (OnlineCheck.isOnline(context)) {

                      api = ApiServiceGenerator.createService(Eld_api.class);


                      Call<JsonObject> call = null;

                      call = api.savedriverlocation(pref.getString(Constant.DRIVER_ID),
                              "" + lat, "" + lon, "" + straddress, "" + strstate, "" + gp_status, pref.getString(Constant.COMPANY_CODE), "back_" + cn);


                      call.enqueue(new Callback<JsonObject>() {
                          @Override
                          public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                              //Log.e("Responsestring", response.body().toString());
                              //Toast.makeText()
                              if (response.isSuccessful()) {
                                  ik = 0;
                                  if (response.body() != null) {
                                      String jsonresponse = response.body().toString();

                                      try {
                                          JSONObject resp = new JSONObject(jsonresponse);
                                          if (response != null) {

                                              String status = resp
                                                      .getString("status");
                                              if (status != null && status.contentEquals("true")) {

                                              } else {

                                              }
                                          }
                                      } catch (Exception e) {

                                      }


                                  }
                              }
                          }

                          @Override
                          public void onFailure(Call<JsonObject> call, Throwable t) {
                              ik = 0;
                          }
                      });

                  }
              }
          }
      }
        }catch (Exception e)
        {

        }
    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public String printDifference(String startxDate, String endxDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = null;

        try {
            startDate = simpleDateFormat.parse(startxDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = null;

        try {
            endDate = simpleDateFormat.parse(endxDate);
        } catch (ParseException e) {
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

    public static String pad(Long num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }
    public long splittime(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
            String timeSplit[] = time.split(":");

            seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60;

        }
        return seconds;

    }
    public long splittimeminutes(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {

            seconds = Integer.parseInt(time) * 60;

        }
        return seconds;

    }
//private void checkondutyalert()
//{
//    try {
//        if (pref.getString(Constant.CURRENT_STATUS) != null && pref.getString(Constant.CURRENT_STATUS).contentEquals("" + commonUtil.ON_DUTY)) {
//            String onstartime = "";
//            SimpleDateFormat formatime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            String strsl = formatime.format(new Date());
//            if (pref.getString(Constant.C_ONDUTY_TIME) != null && pref.getString(Constant.C_ONDUTY_TIME).length() > 0) {
//                onstartime = pref.getString(Constant.C_ONDUTY_TIME);
//
//
//                if (onstartime.contains(strsl)) {
//
//                } else {
//                    pref.putString(Constant.C_ONDUTY_TIME, "" + strsl + " " + "00:01");
//                    onstartime = "" + strsl + " " + "00:01";
//                    pref.putString(Constant.C_ONDUTY_TIME_ID,"");
//                }
//            } else {
//                pref.putString(Constant.C_ONDUTY_TIME, "" + strsl + " " + "00:01");
//                onstartime = "" + strsl + " " + "00:01";
//                pref.putString(Constant.C_ONDUTY_TIME_ID,"");
//            }
//          //  Log.e("onstartime", "" + onstartime);
//            SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//            String currenttime = formatdatetime.format(new Date());
//          //  Log.e("currenttime", "" + currenttime);
//            String a = printDifference(onstartime, currenttime);
//            long ontime = splittime(a);
//           // Log.e("ontime", "" + ontime);
////            if (ontime > 7200) {
////                //Log.e("boolondutyexceed",""+boolondutyexceed);
////                SimpleDateFormat fzz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
////                String dte = fzz.format(new Date());
////                if(pref.getString(Constant.ONDUTY_ALERT_DATES) !=null && pref.getString(Constant.ONDUTY_ALERT_DATES).length()>0 && pref.getString(Constant.ONDUTY_ALERT_DATES).contentEquals(dte))
////                {
////
////                }else {
////                    callonduty();
////                }
////
////
////            }
//
//        }
//    }catch (Exception e)
//    {
//        //Log.e("ee",""+e.toString());
//    }
//}
private void callbreakrelease()
{
    try{
        if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {
            if (pref.getString(Constant.BREAK_ACTIVATED_TIME) != null && pref.getString(Constant.BREAK_ACTIVATED_TIME).length()>0) {
                SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String currtime = formatdatetime.format(new Date());
                String earliertime=pref.getString(Constant.BREAK_ACTIVATED_TIME);
                String oktime=printDifference(earliertime,currtime);
                long ontime = splittime(oktime);
                if (pref.getString(Constant.BREAK_DURATION) != null && pref.getString(Constant.BREAK_DURATION).length()>0) {

                    String strduration = pref.getString(Constant.BREAK_DURATION);
                    long durat = splittime(strduration);

                    if (ontime > durat) {
                        newbreakrelease();
                    }
                }


            }
        }
    }catch (Exception e)
    {

    }
}

    private void newbreakrelease()
    {
        //Log.e("autocallk",""+"break"+"  ");

        try {




            if (OnlineCheck.isOnline(context)) {

                api = ApiServiceGenerator.createService(Eld_api.class);

                pref.putString(Constant.BREAK_ACTIVATED_TIME,"");

                Call<JsonObject> call = null;

//                Log.e("url","http://eld.e-logbook.info/elog_app/eld_acceptautobreak_new.php?cc="+pref.getString(Constant.COMPANY_CODE)
//                        +"&did="+pref.getString(Constant.DRIVER_ID)
//                        +"&brk_id="+strbrid+"&type="+pref.getString(Constant.BREAK_TYPE)+"&rule="+
//                        pref.getString(Constant.BREAK_RULE)+
//                        "&state="+strstate+"&address="+straddress
//                        +"&lat="+ lat+"&lon="+lon
//                        +"&trck="+"trck"+"&change_status="+strstatus+"&testbreak=");

                call = api.newbreakrelease(pref.getString(Constant.COMPANY_CODE),
                        pref.getString(Constant.DRIVER_ID),
                        ""+pref.getString(Constant.BREAK_LAST_ID),
                        "" + strstate, "" + straddress,"",""+pref.getString(Constant.BREAK_LAST_ID));


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()

                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                // Log.e("jsonresponse", jsonresponse.toString());
                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

                                        String status = resp
                                                .getString("status");
                                        // Log.e("status","@"+status);
                                        if (status != null && status.contentEquals("1")) {


                                        } else {

                                        }
                                    }
                                } catch (Exception e) {
                                    //  Log.e("Exceptionwwwwwwww", e.toString());

                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        // Log.e("Exceptionwttttttt", t.toString());

                    }
                });


            }

        }catch (Exception e)
        {

        }
    }
    private void checkbreak() {
        try{
            SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String cdtime = formatdatetime.format(new Date());
            if (pref.getString(Constant.BREAK_AVAILABLE_TODAY).contentEquals("yes")) {

                //Log.e("cdtime", "@" + cdtime);
                //Log.e("cdtimfgsde", "@" + pref.getString(Constant.BREAK_AVAILABLE_TODAY));
                if (pref.getString(Constant.BREAK_AVAILABLE_TODAY) != null && pref.getString(Constant.BREAK_AVAILABLE_TODAY).contentEquals("yes") && !pref.getString(Constant.BREAK_TAKEN).contentEquals("taken") && !pref.getString(Constant.BREAK_TAKEN).contentEquals("rejected")) {
                    String firststatustime = "" + pref.getString(Constant.BREAK_FIRSTSTATUS_TIME);
                    //Log.e("firststatustime", "@" + firststatustime);
                    long ftime = splittime(firststatustime);
                    //Log.e("ftime", "@" + ftime);
                    long ctime = splittime(cdtime);
                    //Log.e("ctime", "@" + ctime);

                    long restime = ctime - ftime;
                    //Log.e("restime", "@" + restime);

                    String strfromduration = "" + pref.getString(Constant.BREAK_REMAINDER);
                    //Log.e("strfromduration", "@" + strfromduration);

                    String strendduration = "" + pref.getString(Constant.BREAK_REMAINDER_TO);
                    //Log.e("strendduration", "@" + strendduration);
                    long lonfromduration = 00;
                    long lonendduration = 00;
                    lonfromduration = splittime(strfromduration);
                    //Log.e("lonfromduration", "@" + lonfromduration);
                    lonendduration = splittime(strendduration);
                    //Log.e("lonendduration", "@" + lonendduration);

                    if (restime >= lonfromduration && restime <= lonendduration) {
                        //Log.e("vallll", "@" + pref.getString(Constant.BREAK_ALERT_DISPLAY));
                        if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("skip")) {
                            //Log.e("vallll", "@" + pref.getString(Constant.BREAK_ALERT_DISPLAY));
                            String displytime = pref.getString(Constant.BREAK_DISPLAY_TIME);
                            //Log.e("displytime", "@" + displytime);
                            String alertinterval = pref.getString(Constant.BREAK_ALERT_INTERVAL);
                            ////Log.e("alertinterval", "@" + alertinterval);
                            long val1 = splittime(displytime);
                            //Log.e("val1", "@" + val1);
                            long val2 = splittime(cdtime);
                            //Log.e("val2", "@" + val2);
                            long resval = 00;
                            resval = val2 - val1;
                            //Log.e("resval", "@" + resval);
                            long alert = splittimeminute(alertinterval);
                            //Log.e("alert", "@" + alert);
                            if (resval >= alert) {
                                //Log.e("cond", "@");

                                //Log.e("brk", "@"+pref.getString(Constant.BREAK_TAKEN));


                                if (pref.getString(Constant.BREAK_TAKEN) != null && pref.getString(Constant.BREAK_TAKEN).contentEquals("taken") && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {
                                    pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                                    pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
                                    pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));
                                } else {
                                    pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                                    pref.putString(Constant.BREAK_ALERT_DISPLAY, "skip");
                                    pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));
                                    callbreaknotification();
                                }
                            }


                        } else {


                            if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {

                            } else {
//                                pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
//                                pref.putString(Constant.BREAK_ALERT_DISPLAY, "skip");
//                                pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));
                                callbreaknotification();
                            }
                        }
                        if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {
                            pref.putString(Constant.MANUAL_LAST_BREAKID,"");
                        } else {
                            pref.putString(Constant.MANUAL_LAST_BREAKID,""+pref.getString(Constant.BREAK_MSG_ID));
                        }
                    }else{
                        pref.putString(Constant.MANUAL_LAST_BREAKID,"");
                        pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                    }
                }else{
                    pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                }
            }else{
                pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                if(pref.getString(Constant.BREAKCOUNTT) !=null && pref.getString(Constant.BREAKCOUNTT).length()>0)
                {
                    int a=Integer.parseInt(pref.getString(Constant.BREAKCOUNTT));
                    a++;
                    pref.putString(Constant.BREAKCOUNTT,""+a);
                    if(a>=6)
                    {
                        pref.putString(Constant.BREAKCOUNTT,"1");
                        getbreakrefresh();
                    }
                }else {
                    pref.putString(Constant.BREAKCOUNTT,"1");
                    getbreakrefresh();
                }
            }
        }catch(Exception e)

        {

        }

    }



    public long splittimeminute(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {

            seconds = Integer.parseInt(time) * 60 ;

        }
        return seconds;

    }
    private void callbreaknotification()
    {
//Log.e("call","break notio");
        Intent ing=new Intent(context, Transparent_breaknew.class);
        ing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(ing);
    }
//    private void callonduty()
//    {
//
//        Intent ing=new Intent(context, Transparent_ondutyalert.class);
//        ing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(ing);
//    }

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

    private void getbreakrefresh() {
        //Log.e("calling","getbreakrefresh");
        String did = pref.getString(Constant.DRIVER_ID);

        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<newbrk_model>> call = api.getbreakrefreshnew(""+pref.getString(Constant.VIN_NUMBER), did,  ""+pref.getString(Constant.COMPANY_CODE));

        call.enqueue(new Callback<List<newbrk_model>>() {
            @Override
            public void onResponse(Call<List<newbrk_model>> call, Response<List<newbrk_model>> response) {
                if (response.isSuccessful()) {

                    List<newbrk_model> brkrefrsh = response.body();
                    setbreakrfresh(brkrefrsh);
                } else {


                }

            }

            @Override
            public void onFailure(Call<List<newbrk_model>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());


            }
        });
    }

    private void setbreakrfresh(List<newbrk_model> brkrefrsh) {
        try{
            if (brkrefrsh.size() > 0 && brkrefrsh != null) {
                for (int i = 0; i < brkrefrsh.size(); i++) {
                    newbrk_model ngt = new newbrk_model();
                    ngt = brkrefrsh.get(i);
                    String strrule=ngt.getRule();
                       if(strrule !=null && strrule.length()>0)
                    {
                        if(strrule.contentEquals("Federal") || strrule.contentEquals("federal"))
                        {
                            pref.putString(Constant.FEDERAL_DRIVE_ACTIVE,"active");
                        }else{
                            pref.putString(Constant.FEDERAL_DRIVE_ACTIVE,"deactive");
                        }
                    }

                    // Break_info_model br=gt.getbreakinfo();
                    List<Break_info_model> br = ngt.getbreakinfo();
                    if (br != null) {

                        for (int d = 0; d < br.size(); d++) {
                            pref.putString(Constant.BREAK_AVAILABLE_TODAY,"yes");
                            Break_info_model brmodel = br.get(d);



                            setbreakvalues(brmodel);
                        }
                    }else{
                        pref.putString(Constant.BREAK_AVAILABLE_TODAY,"no");
                    }
                }
            }
        }catch (Exception e)
        {

        }
    }

    private void setbreakvalues(Break_info_model newbreakmodel) {
        if (newbreakmodel != null) {
            //Log.e("valllllllllzzz", "@" + newbreakmodel.getMessage());
            pref.putString(Constant.BREAK_FIRSTSTATUS_TIME, "" + newbreakmodel.getFirst_status_time());
            pref.putString(Constant.BREAK_MESSAGE, "" + newbreakmodel.getMessage());
            pref.putString(Constant.BREAK_MSG_ID, "" + newbreakmodel.getBrk_id());
            pref.putString(Constant.BREAK_REMAINDER, "" + newbreakmodel.getRemainder());
            pref.putString(Constant.BREAK_REMAINDER_TO, "" + newbreakmodel.getRemainderto());
            pref.putString(Constant.BREAK_ALERT_INTERVAL, "" + newbreakmodel.getAlert_interval());
            pref.putString(Constant.BREAK_RULE, "" + newbreakmodel.getRule());
            pref.putString(Constant.BREAK_TYPE, "" + newbreakmodel.getType());
            pref.putString(Constant.BREAK_DURATION, "" + newbreakmodel.getHow_many_minutes());
            pref.putString(Constant.BREAK_APPLY_STATUS, "" + newbreakmodel.getEstatus());
            pref.putString(Constant.BREAK_LIVE_TIME, "" + newbreakmodel.getTill_now());
          String  strbrdialog = "" + newbreakmodel.getBreak_status();

            pref.putString(Constant.BREAK_TAKEN, "" + strbrdialog);

            if(pref.getString(Constant.BREAK_LAST_ID) !=null && pref.getString(Constant.BREAK_LAST_ID).length()>0 )
            {

            }else{
                pref.putString(Constant.BREAK_LAST_ID, "" + newbreakmodel.getBreak_taken_id());
            }

            try {
                if (pref.getString(Constant.CURRENT_STATUS).contentEquals("" + commonUtil.OFF_DUTY)
                        || pref.getString(Constant.CURRENT_STATUS).contentEquals("" + commonUtil.SLEEP)) {

                } else {
                    checkbreak();
                }
            }catch (Exception e)
            {

            }




        } else {
            pref.putString(Constant.BREAK_AVAILABLE_TODAY, "no");
        }

    }

}
