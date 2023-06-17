package com.trucksoft.isoft.isoft_elog.show_loc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_api.Location_model;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_mylocation extends AppCompatActivity implements OnMapReadyCallback{
    private Context context;
    ProgressDialog dialog;
    Eld_api api;
    List<Location_model> movies;
    Geocoder geocoder;
    private String errorMessage;
    private TextView txtaddress;
    private String vin;
    Preference pref;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String breakid;
    private CommonUtil commonUtil;
    ImageView imgback;


    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    private String lat="";
    private String lon="";
    private String strstate="";
    private String straddress="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_location);
        context=this;
        pref=Preference.getInstance(context);
        vin= pref.getString(Constant.VIN_NUMBER);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        imgback=findViewById(R.id.add_driver_checklist_iv_back);
       // Log.e("vin",""+vin);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        txtaddress=(TextView)findViewById(R.id.taddrss);
        api = ApiServiceGenerator.createService(Eld_api.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(context, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
                    finish();
//                }
            }
        });
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Log.e("call","........."+"fcm..............");
                // checking for type intent filter
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    // new push notification is received
                    //handlePushNotification(intent);
                    String msg = intent.getStringExtra("message");
                    //  Log.e("msg","........."+msg);
                    if (msg != null && msg.length() > 0 && !msg.contentEquals("null")) {

                        if (msg.contentEquals("app_logout")) {
                            Log.e("callv","#"+msg);
                            applogout();
                        }



                    }
                }
            }
        };




        //*************************************************************************


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       getcurrentloc(googleMap);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent intent = new Intent(context, Home_activity.class);
//            startActivity(intent);
//            finish();
//        }else{
            finish();
//        }
    }

private void getcurrentloc(final GoogleMap googleMap)
{
    // vin="1N6BA1F46HN555066";
    dialog = new ProgressDialog(context,
            AlertDialog.THEME_HOLO_LIGHT);
    dialog.setMessage("Please wait...");
    dialog.setCancelable(false);
    dialog.show();
    api = ApiServiceGenerator.createService(Eld_api.class);
//    Log.e("val","get_mylocation.php?vin="+vin+"&lat="+lat+
//            "&lon="+lon+
//            "&address="+straddress+
//            "&state="+strstate+
//            "&feature=Find My Vehicle");
    Call<List<Location_model>> call = api.getvehicle(vin,""+lat,""+lon,""+straddress,""+strstate,"Find My Vehicle");

    call.enqueue(new Callback<List<Location_model>>() {
        @Override
        public void onResponse(Call<List<Location_model>> call, Response<List<Location_model>> response) {
            if(response.isSuccessful()){
                dialog.dismiss();
                movies=response.body();
                setVal(movies,googleMap);
            }else{
                dialog.dismiss();
                callnetworkdialog(googleMap);
                Log.e("ss"," Response "+String.valueOf(response.code()));
            }
        }

        @Override
        public void onFailure(Call<List<Location_model>> call, Throwable t) {
            Log.e("dd"," Response Error "+t.getMessage());
            dialog.dismiss();
            callnetworkdialog(googleMap);
        }
    });
}
private void setVal(List<Location_model> locmov,GoogleMap googleMap)
{
    for(int i=0;i<locmov.size();i++)
    {
        Location_model li=new Location_model();
        li=locmov.get(i);

        String lat=li.getendlat();
        String lon=li.getendlong();
        String address=li.getaddress();
        String model=li.getmodel_name();
        if(address !=null && address.length()>0 && !address.contentEquals("null"))
        {
            txtaddress.setText(address);
        }else{
            if(lat !=null && lat.length()>0 && !lat.contentEquals("null")) {
                getaddress(Double.parseDouble(lat), Double.parseDouble(lon));
            }
        }
        if(lat !=null && lat.length()>0 && !lat.contentEquals("null"))
        {
            LatLng currentLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            googleMap.addMarker(new MarkerOptions().position(currentLocation)
                    .title(""+txtaddress.getText().toString()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
           // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
           // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,10));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        }

    }
}
private void getaddress(double lat,double lon)
{
   // Log.e("lat",""+lat);
   // Log.e("lon",""+lon);
    List<Address> addresses = null;

    try {
        addresses = geocoder.getFromLocation(
                lat,
                lon,
                // In this sample, get just a single address.
                1);
    } catch (IOException ioException) {
        // Catch network or other I/O problems.
        errorMessage = getString(R.string.service_not);
        Log.e("error1", errorMessage, ioException);
    } catch (IllegalArgumentException illegalArgumentException) {
        // Catch invalid latitude or longitude values.
        errorMessage = getString(R.string.latwrong);
        Log.e("error1", errorMessage);


    }
    if(addresses !=null && addresses.size()>0)
    {
        Address address = addresses.get(0);
        ArrayList<String> addressFragments = new ArrayList<String>();

        // Fetch the address lines using getAddressLine,
        // join them, and send them to the thread.
        for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }
        txtaddress.setText(addressFragments.toString());
    }
}
private void callnetworkdialog(final GoogleMap googleMap)
{
    View view = View.inflate(context, R.layout.retry, null);
    TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
    final Dialog dialogmk = new Dialog(context, R.style.DialogTheme);
    //dialog = new Dialog(this, R.style.DialogTheme);
    dialogmk.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialogmk.setContentView(view);
    dialogmk.show();
    // Get screen width and height in pixels
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    // The absolute width of the available display size in pixels.
    int displayWidth = displayMetrics.widthPixels;
    // The absolute height of the available display size in pixels.
    int displayHeight = displayMetrics.heightPixels;

    // Initialize a new window manager layout parameters
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

    // Copy the alert dialog window attributes to new layout parameter instance
    layoutParams.copyFrom(dialogmk.getWindow().getAttributes());
    // Set alert dialog width equal to screen width 70%
    int dialogWindowWidth = (int) (displayWidth * 0.75f);
    // Set alert dialog height equal to screen height 70%
    int dialogWindowHeight = (int) (displayHeight * 0.30f);
    layoutParams.width = dialogWindowWidth;
    layoutParams.height = dialogWindowHeight;

    // Apply the newly created layout parameters to the alert dialog window
    dialogmk.getWindow().setAttributes(layoutParams);
    txt_ok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogmk.dismiss();
            getcurrentloc(googleMap);
        }
    });
}

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));
        Trucksoft_elog_Notify_Utils.clearNotifications(context);
    }

    @Override
    protected void onDestroy() {
        if(dialog !=null && dialog.isShowing())
        {
            dialog.dismiss();
            dialog=null;
        }
        super.onDestroy();
    }

    private void applogout()
    {
        Log.e("called","kk************logout");
        api = ApiServiceGenerator.createService(Eld_api.class);
        String did=pref.getString(Constant.DRIVER_ID);
        String vin=pref.getString(Constant.VIN_NUMBER);
        cancelnotification();

        //  Log.e("url","&did="+did+"&name="+type);
        Call<List<Remark_model>> call = api.applogout(did,""+vin,did,""+pref.getString(Constant.CURRENT_LATITUDE),""+pref.getString(Constant.CURRENT_LOGINGITUDE));

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


        try {
            finish();
            Intent mIntent = new Intent(context,
                    Loginactivitynew.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("EXIT", true);
            startActivity(mIntent);
        }catch (Exception e)
        {
            Log.e("serviceer","@"+e.toString());
        }




        // ((Activity) context). finishAndRemoveTask();
    }
    public void cancelnotification() {


        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

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
                        pref.putString(Constant.CURRENT_STATE, "" + strstate);
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
}
