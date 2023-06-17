package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Fuel_model;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fuel_level extends FragmentActivity {
    ProgressDialog progressdlog;
    Context context;
    Eld_api api;
    Preference pref;
    String dc;
    private TextView txtfuellevl;
    private TextView txtfueltype,txtfuelechonomy;
    private TextView txtback;
    Font_manager font_manager;
    LinearLayout lin_parent;
    GestureDetector gestureDetector;
    List<Fuel_model> fuelvals=new ArrayList<>();
    private String vinnumber="";
    private GaugeView mGaugeView1;
    private LinearLayout linfueltype;
    private LinearLayout linfuelechnomy;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    private String lat="";
    private String lon="";
    private String strstate="";
    private String straddress="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuellevel);
        context=this;
        txtfuellevl=findViewById(R.id.txtfuellevl);
        txtfueltype=findViewById(R.id.txtfueltype);
        txtfuelechonomy=findViewById(R.id.txtfuelechonomy);
        lin_parent=findViewById(R.id.linparent);
        linfuelechnomy=findViewById(R.id.linfuelechnomy);
        linfueltype=findViewById(R.id.linfueltype);
        api = ApiServiceGenerator.createService(Eld_api.class);
        pref=Preference.getInstance(context);
        vinnumber=pref.getString(Constant.VIN_NUMBER);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        SimpleDateFormat formatsec= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dc =formatsec.format(new Date());
        font_manager=new Font_manager();
        txtback=findViewById(R.id.txtback);
        mGaugeView1 =  findViewById(R.id.gauge_view1);
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
        txtback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(context, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else
//                {
                    finish();
//                }
            }
        });
        getfuellevel();
        lin_parent.setOnTouchListener(new OnSwipeTouchListener(Fuel_level.this) {
            public void onSwipeTop() {
                //getdata();
            }
            public void onSwipeRight() {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent mIntent1 = new Intent(Fuel_level.this, Home_activity.class);
//                    startActivity(mIntent1);
//                    finish();
//                }else{
                    finish();
//                }
            }
            public void onSwipeLeft() {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent mIntent1 = new Intent(Fuel_level.this, Home_activity.class);
//                    startActivity(mIntent1);
//                    finish();
//                }else{
                    finish();
//                }
            }
            public void onSwipeBottom() {
                //  getdata();
            }

        });


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
    }

    private void getfuellevel()
    {
        fuelvals=new ArrayList<>();
        final String did=pref.getString(Constant.DRIVER_ID);
        //Log.e("notif","calling");
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        // dialogz.setCancelable(false);
        progressdlog.show();
        String ccode=pref.getString(Constant.COMPANY_CODE);
        api = ApiServiceGenerator.createService(Eld_api.class);
//        Log.e("val","get_fuellevel.php?did="+did+"&vin="+vinnumber+"&ccode="+ccode+"&lat="+lat+
//                "&lon="+lon+
//                "&address="+straddress+
//                "&state="+strstate+
//                "&feature=Fuel Level");
        Call<List<Fuel_model>> call = api.getValues_eld(did,vinnumber,ccode,""+lat,""+lon,""+straddress,""+strstate,"Fuel Level");

        call.enqueue(new Callback<List<Fuel_model>>() {
            @Override
            public void onResponse(Call<List<Fuel_model>> call, Response<List<Fuel_model>> response) {
                if(response.isSuccessful()){
                    progressdlog.dismiss();
                    fuelvals=response.body();
                    displayfuelslevel(fuelvals);
                }else{
                    progressdlog.dismiss();
                    Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
                    //  getvehicle();
                    //Log.e("ss"," Response "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Fuel_model>> call, Throwable t) {
                // Log.e("dd"," Response Error "+t.getMessage());
                progressdlog.dismiss();

            }
        });
    }

    private void displayfuelslevel( List<Fuel_model> fuelvals)
    {
if(fuelvals !=null & fuelvals.size()>0)
{
    for(int i=0;i<fuelvals.size();i++)
    {
        Fuel_model flv=new Fuel_model();
        flv=fuelvals.get(i);
        String flevel=flv.f_level;
        if(flevel !=null && flevel.length()>0 && !flevel.contentEquals("null")) {
            txtfuellevl.setText(flevel);

        }
       // Log.e("flevel","@"+flevel);
        if(flevel !=null && flevel.length()>0 && !flevel.contentEquals("null")&& !flevel.contentEquals("%")) {
            if(flevel.contains("%"))
            {
                StringTokenizer skt=new StringTokenizer(flevel,"%");
                String valc=skt.nextToken();
                mGaugeView1.setTargetValue(Float.parseFloat(valc));
            }else {
                mGaugeView1.setTargetValue(Float.parseFloat(flevel));
            }
        }else{
            mGaugeView1.setTargetValue(0);
        }
//        mGaugeView1.setTargetValue(Float.parseFloat(flevel));
        String ftype=flv.style_fuel_type;
        if(ftype !=null && ftype.length()>0 && !ftype.contentEquals("null")) {
            txtfueltype.setText(ftype);
            linfueltype.setVisibility(View.VISIBLE);
        }else{
            linfueltype.setVisibility(View.GONE);
        }

        String fechonomy=flv.fuel_economy;
        if(fechonomy !=null && fechonomy.length()>0 && !fechonomy.contentEquals("null")) {
            txtfuelechonomy.setText(fechonomy);
            linfuelechnomy.setVisibility(View.VISIBLE);
        }else{
            linfuelechnomy.setVisibility(View.GONE);
        }
    }
}
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    }

    class OnSwipeTouchListener implements View.OnTouchListener {

        //  private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {

        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent ink = new Intent(context, Home_activity.class);
//            startActivity(ink);
//            finish();
//        }else{
            finish();
//        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

        // clearing the notification tray
        Trucksoft_elog_Notify_Utils.clearNotifications(context);
    }


    private void applogout()
    {
        //Log.e("called","kk************logout");
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
            //finish();
            finishAffinity();
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
