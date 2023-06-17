package com.trucksoft.isoft.isoft_elog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_activity.Home_activity_bluetooth;
import com.isoft.trucksoft_elog.Isoft_activity.Privacy_elogbook;
import com.isoft.trucksoft_elog.Isoft_activity.Registration;
import com.isoft.trucksoft_elog.Isoft_activity.SplashScreenActivity;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.E_logutil;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.Services.E_logbook_ForegroundService;
import com.isoft.trucksoft_elog.Vehicle.Vehicle_seect;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Msg_Service;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by isoft on 5/11/17.
 */

public class Loginactivitynew extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private EditText edtUser;
    //private EditText edtPass;
    private Button btnLogin;
    private TextView txtfwd;
    private ImageView txtcompany;

    private String comp_code;
    ProgressDialog dialog;
    private String strstate = "";
    private String straddress = "";
    SharedPreferences.Editor editor;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String MyPREFERENCES = "Driver_Name";
    SharedPreferences sharedpreferences;

    private String stcode;
    boolean isRunning = false;
    CountDownTimer newtimer;

    Context cons;
    String str = "";
    private TextView txttime;
    private Long h1, m1, s1;
    Long lhour, lminute, lsecond, finalval;
    long lk;
    LocationManager locationManager;
    boolean GpsStatus;
    private TextView currenttime;
    private  Preference pref;
    TimerTask timerTask;
    private CommonUtil commonUtil;

    String make = "";
    private TextView txtloctime;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 3000; /* 2 sec */

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Handler progressBarHandler = new Handler();
    int progress = 0;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;

    private E_logutil eutil;
    private ArrayList<String> listvals;
    private String straname = "";
    private String imenumber1 = "0";
    private String imenumber2 = "0";
    Dialog dialoglogi;
    Font_manager_elog font_manager;
    private TextView helpline;
 private String devicetype = "0";
    private String androidId ="";
    private String unique_id="";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    Eld_api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        cons = this;
       // gps = new GPSTracker(getApplicationContext());
        pref = Preference.getInstance(this);
        str = pref.getString(Constant.LOGIN_CHECK);
if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0)
{

}else{
    pref.putString(Constant.TRACK_BLUETOOTH,"active");
}

    try {
            unique_id = id(cons);
            androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
           // Log.e("androidId", "@" + androidId);
        }catch (Exception e)
        {

        }
        try {
          //  Log.e("strstr","@"+str);
            if (str != null && str.length() > 0 && !str.contentEquals("null")) {
                if (str.equalsIgnoreCase("logged_inn")) {
                    finish();
                    Intent mIntent = new Intent(
                            Loginactivitynew.this,
                            SplashScreenActivity.class);
                    startActivity(mIntent);
                }

            }
        } catch (Exception e) {
           // Log.e("strstrerrr","@"+e.toString());
        }


if(pref.getString(Constant.BL_SCANNING_FLAG) !=null && pref.getString(Constant.BL_SCANNING_FLAG).length()>0)
{

}else{
    pref.putString(Constant.BL_SCANNING_FLAG,"0");
}

        pref.putString(Constant.DELETE_APP_STATUS, "0");
        pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH_CALLING, "1");
        pref.putString(Constant.DRIVER_MESSAGE_, "");
        pref.putString(Constant.BLUETOOTH_ADDRESS, "");
        pref.putString(Constant.BLUETOOTH_NAME, "");
        pref.putString(Constant.NETWORK_TYPE, Constant.CELLULAR);
        pref.putString(Constant.B_TIMER_STATUS,"0");
        pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"on");
        pref.putString(Constant.ELOG_DRIVER_TYPE,"0");
        if(pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS) !=null && pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).length()>0)
        {

        }else{
            pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");

        }
        font_manager = new Font_manager_elog();
        //Log.e("cfstrk","#"+str);
        commonUtil = new CommonUtil(cons);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                this.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        eutil = new E_logutil();
        listvals = new ArrayList<>();
        listvals = eutil.getname();
        getversionname();
        edtUser =  findViewById(R.id.staff_id);
        btnLogin = findViewById(R.id.login);
        helpline = findViewById(R.id.helpline);
        txtloctime = findViewById(R.id.txjjjjhttime);
        txtfwd =  findViewById(R.id.txtfd);
        txttime =  findViewById(R.id.txttime);
        currenttime =  findViewById(R.id.txtctime);
        txtcompany=findViewById(R.id.txt_compreg);
        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/digita.ttf");
        currenttime.setTypeface(face1);
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON_REMAINDER, "0");
        pref.putString(Constant.FEDERAL_RULE_DAY, "deactive");
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        txtfwd.setTypeface(face);
      //  txtcompany.setTypeface(face);
        btnLogin.setTypeface(face);
        stopService();
        setUpGClient();
        if(pref.getString(Constant.PRIVACY_KEY_ISOFTOFFICE) !=null && pref.getString(Constant.PRIVACY_KEY_ISOFTOFFICE).length()>0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 0);
                }
            }
        }
        final LocationManager manager = (LocationManager)cons.getSystemService    (Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

                requestGPSSettings();

        }

        if(pref.getString(Constant.PRIVACY_KEY_ISOFTOFFICE) !=null && pref.getString(Constant.PRIVACY_KEY_ISOFTOFFICE).length()>0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                insertDummyContactWrapper();
            }
        }else{
            callprivacyy();
        }

        try {
            if (pref.getString(Constant.LICENSE_ELOG) != null && pref.getString(Constant.LICENSE_ELOG).length() > 0) {
                edtUser.setText("" + pref.getString(Constant.LICENSE_ELOG));
            }
        } catch (Exception e) {

        }
        pref.putString(Constant.VOICE_OFF, "0");
        pref.putString(Constant.VOICE_ON, "0");
        pref.putString(Constant.VOICE_SLEEP, "0");
        pref.putString(Constant.VOICE_DRIVE, "0");
        pref.putString(Constant.BLUETOOTH_CONNECTED, "0");
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String oldversion = "" + packageInfo.versionCode;
            String version = pref.getString(Constant.OLD_VERSION_FIELD);
            //	Log.e("fdf","@"+version);
            if (version != null && version.length() > 0 && !version.contentEquals("null")) {
                Double a = 0.00;
                a = Double.parseDouble(version);
                Double b = 0.00;
                b = Double.parseDouble(oldversion);

                //	b=Integer.parseInt(oldversion);
                if (a > b) {
                    final String appPackageName = getPackageName();

                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }

        } catch (PackageManager.NameNotFoundException e) {

        }

/////////////////////////////////////////////////////////////

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.DRIVER_APP_REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    if (message != null && message.length() > 0 && !message.contentEquals("null")) {
                        if (message.contentEquals("ask_for_login")) {
                            String license = intent.getStringExtra("license");
                            setforceloginalert(license);
                        }
                    }
                }
            }
        };
        displayFirebaseRegId();


        //calendar = Calendar.getInstance();


        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        String myDate = format.format(new Date());


        String myDatex = format.format(new Date());
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(myDatex);
            txtloctime.setText("" + new SimpleDateFormat("h:mma").format(dateObj));

        } catch (final ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String myDatez = formatz.format(new Date());
        StringTokenizer skdte;
        if (myDatez.contains("-")) {
            skdte = new StringTokenizer(myDatez, "-");
            String styr = skdte.nextToken();
            String stmnth = skdte.nextToken();
            String stdte = skdte.nextToken();
            txttime.setText(getMonth(Integer.parseInt(stmnth)) + "," + stdte);
        }


        //time spilit
        StringTokenizer st = new StringTokenizer(myDate, ":");
        h1 = Long.parseLong(st.nextToken());
        m1 = Long.parseLong(st.nextToken());
        s1 = Long.parseLong(st.nextToken());
        lhour = h1;
        lminute = m1;
        lsecond = s1;
        long lh, lm, ls;
        lh = lhour * 3600;// 1 hour contains 3600 seconds
        lm = lminute * 60;// 1 minutes 60 seconds

        ls = lsecond;
        finalval = lh + lm + ls;
        lk = finalval * 1000;
        // incsecond = lk;

        if (isRunning) {
            newtimer.cancel();
        }
edtUser.setText("D6091528");
        timerTask = new MyTimerTask();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        //timer.scheduleAtFixedRate(timerTask, 0, 1000);
        timer.schedule(timerTask, 1000, 60 * 1000);
        setcurrenttime(myDate);

        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String phone = "(855) 922-9700";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }catch (Exception e)
                {

                }

            }
        });
      //  callloginalert();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = edtUser.getText().toString().trim();
                // String pwd = edtPass.getText().toString().trim();
              //  checkPermissionsphonestate();
                if (checkValidation()) {
//                    if (lat == null && lon == null) {
//                        lat = String.valueOf(gps.getLatitude());
//                        lon = String.valueOf(gps.getLongitude());
//                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (lat != null && lon != null) {
                        stcode = edtUser.getText().toString().trim();


                        if (lat.length() > 1 && lon.length() > 1
                                && (stcode != null && stcode.length() > 0)) {
                            //String macaddress = getMacAddress();
                            String macaddress = getWifiMacAddress();


                            pref.putString(Constant.LATITUDE, "" + lat);
                            pref.putString(Constant.LONGITUDE, "" + lon);
//                            login_validation(stcode, macaddress, uname);
                            logincheck(stcode, macaddress, uname);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Unable to get location.\nPlease retry",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Unable to get location.\nPlease retry",
                                Toast.LENGTH_SHORT).show();
                    }


                }
            }


        });

        txtfwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache(cons);
                Intent mIntent = new Intent(
                        Loginactivitynew.this,
                        Registration.class);
                startActivity(mIntent);

                finish();

            }
        });
        txtcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Loginactivitynew.this,
                        Company_Login.class);
                startActivity(mIntent);

                finish();
            }
        });
    }


    private boolean checkValidation() {
        // TODO Auto-generated method stub

        comp_code = edtUser.getText().toString().trim();
        // driver_id = edtPass.getText().toString().trim();

        if (comp_code != null) {
            if (comp_code.length() > 0) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(),
                        "Both feilds are mandatory !", Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        }
        return true;

    }


    private void logincheck(
            final String driverid, String macaddress, String uname)
    {
        dialog = new ProgressDialog(Loginactivitynew.this,
                AlertDialog.THEME_HOLO_LIGHT);
        if (lat != null) {
            try {
                getAddressFromLocation(Double.parseDouble(lat), Double.parseDouble(lat));
            } catch (Exception e) {

            }
        }

        if (OnlineCheck.isOnline(this)) {

            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            try {
                dialog.show();
            } catch (Exception e) {

            }
            api = ApiServiceGenerator.createService(Eld_api.class);
            //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
            Call<JsonObject> call = api.getlogindetail(driverid, lat, lon, "" + uname, "" + imenumber1, "" + imenumber2, "" + macaddress, "" + pref.getString(Constant.ELOG_FIREBASE_KEY),
                    "android", "" + straname, "" + straddress, "" + strstate, "" + androidId, "" + unique_id);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> responsez) {

                    Log.e("Responsestring", responsez.body().toString());
                    //Toast.makeText()
                    if (responsez.isSuccessful()) {
                        cancelprogresssdialog();
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

                                        if (status.equalsIgnoreCase("1")) {
                                            String driverid = response
                                                    .getString("s_id");

                                            pref.putString(Constant.LICENSE_ELOG, stcode);
                                            String compcode = response
                                                    .getString("ccode");
                                            String driver_type = "0";
                                            if (response.has("driver_type")) {
                                                driver_type = response
                                                        .getString("driver_type");
                                                pref.putString(Constant.ELOG_DRIVER_TYPE, "" + driver_type);
                                            }
                                            if (response.has("ccode")) {
                                                if (compcode != null && compcode.length() > 0 && !compcode.contentEquals("null")) {

                                                    String vinnumber = response
                                                            .getString("vin");
                                                    String drivername = response
                                                            .getString("s_name").trim();
                                                    editor.putString("driver_name",
                                                            drivername);
                                                    editor.putString(Constant.LOGIN_CHECK,
                                                            "logged_inn");
                                                    editor.commit();

                                     /*   String logo = response
                                                .getString("logo");*/

                                                    String driver_photo = response
                                                            .getString("driver_photo");
                                                    String strphone = response
                                                            .getString("phone");

                                                    String states = response
                                                            .getString("state");
                                                    String licenseno = response
                                                            .getString("licenseno");
                                                    //Log.e("kk","dd");
                                                    String modelname = response
                                                            .getString("model_name");
                                                    // Log.e("modell","@@@@"+modelname);
                                                    String vehno = response
                                                            .getString("vid");
                                                    String str_timezone = response
                                                            .getString("timezone");
                                                    if (response.has("ccode")) {

                                                        pref.putString(Constant.COMPANY_CODE,
                                                                compcode);
                                                    }
                                                    //eldkey
                                                    if (response.has("eldkey")) {
                                                        String dkey = response.getString("eldkey");
                                                        if (dkey != null && dkey.length() > 0 && !dkey.contentEquals("null")) {
                                                            pref.putString(Constant.ELD_KEY, "" + dkey);
                                                        } else {
                                                            pref.putString(Constant.ELD_KEY, "");
                                                        }
                                                    } else {
                                                        pref.putString(Constant.ELD_KEY, "");
                                                    }
                                                    pref.putString(Constant.DRIVER_HOME_TIMEZONE, "" + str_timezone);


                                                    if (response.has("make")) {
                                                        make = response.getString("make");
                                                        if (make != null && make.length() > 0 && !make.contentEquals("null")) {
                                                            if (make.contains(" ")) {
                                                                StringTokenizer st = new StringTokenizer(make, " ");
                                                                if (st.hasMoreTokens()) {
                                                                    pref.putString(Constant.ELD_MAKE, "" + st.nextToken());
                                                                }


                                                            } else {
                                                                pref.putString(Constant.ELD_MAKE, "" + make);
                                                            }
                                                        } else {
                                                            pref.putString(Constant.ELD_MAKE, "");
                                                        }
                                                    } else {
                                                        pref.putString(Constant.ELD_MAKE, "");
                                                    }

                                                    if (response.has("year")) {
                                                        String year = response.getString("year");
                                                        if (year != null && year.length() > 0 && !year.contentEquals("null")) {
                                                            pref.putString(Constant.ELD_YEAR, "" + year);
                                                        } else {
                                                            pref.putString(Constant.ELD_YEAR, "");
                                                        }
                                                    } else {
                                                        pref.putString(Constant.ELD_YEAR, "");
                                                    }

                                                    if (response.has("companyname")) {
                                                        String cname = response.getString("companyname");
                                                        if (cname != null && cname.length() > 0 && !cname.contentEquals("null")) {
                                                            pref.putString(Constant.COMPANY_NAME, "" + cname);
                                                        } else {
                                                            pref.putString(Constant.COMPANY_NAME, "");
                                                        }

                                                    } else {
                                                        pref.putString(Constant.COMPANY_NAME, "");

                                                    }


                                                    if (response.has("email")) {
                                                        String email = response.getString("email");
                                                        if (email != null && email.length() > 0 && !email.contentEquals("null")) {
                                                            pref.putString(Constant.COMPANYMAIL, "" + email);
                                                        } else {
                                                            pref.putString(Constant.COMPANYMAIL, "");
                                                        }

                                                    } else {
                                                        pref.putString(Constant.COMPANYMAIL, "");

                                                    }

                                                    if (response.has("blu_time")) {
                                                        String blutime = response.getString("blu_time");
                                                        if (blutime != null && blutime.length() > 0) {
                                                            pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "" + blutime);
                                                        } else {
//                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "150000" );//2.5 minutes
                                                            pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "120000");//45seconds
                                                        }
                                                    } else {
                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "120000");
                                                    }

                                                    if (response.has("blu_speedtime")) {
                                                        String blusptime = response.getString("blu_speedtime");
                                                        if (blusptime != null && blusptime.length() > 0) {
                                                            pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "" + blusptime);
                                                        } else {
//                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "150000" );//2.5 minutes
                                                            pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "60000");//30seconds
                                                        }
                                                    } else {
                                                        pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "60000");
                                                    }

                                                    if (response.has("blu_address")) {
                                                        String bluaddress = response.getString("blu_address");
                                                        String bluadname = response.getString("blu_name");
                                                        // Log.e("bluaddress","@"+bluaddress);
                                                        if (bluaddress != null && bluaddress.length() > 0) {
                                                            pref.putString(Constant.BLUETOOTH_ADDRESS, "" + bluaddress);
                                                            pref.putString(Constant.BLUETOOTH_NAME, "" + bluadname);
                                                        } else {
                                                            pref.putString(Constant.BLUETOOTH_ADDRESS, "");
                                                            pref.putString(Constant.BLUETOOTH_NAME, "");
                                                        }
                                                    } else {
                                                        pref.putString(Constant.BLUETOOTH_ADDRESS, "");
                                                        pref.putString(Constant.BLUETOOTH_NAME, "");
                                                    }


                                                    if (strphone == null || strphone.contentEquals("null")) {
                                                        strphone = "";
                                                    }
                                                    pref.putString(Constant.DRIVER_PHONE, "" + strphone);
                                                    pref.putString(Constant.DRIVER_ID,
                                                            driverid);
                                                    pref.putString(Constant.VIN_NUMBER,
                                                            vinnumber);
                                                    pref.putString(Constant.LICENSE_NUMBER,
                                                            licenseno);
                                                    pref.putString(Constant.ALERT_SYNC_TIME,
                                                            "00:00");
                                                    if (states != null && states.length() > 0 && !states.contentEquals("null")) {

                                                    } else {
                                                        states = "";
                                                    }
                                                    pref.putString(Constant.STATE_FIELD, "" + states);

                                                    String stime = response
                                                            .getString("s_time");
                                                    pref.putString(Constant.SERVER_TIME,
                                                            stime);
                                                    pref.putString(Constant.PC_STATUS,
                                                            commonUtil.PC_DISABLE);
                                                    pref.putString(Constant.DRIVER_NAME,
                                                            drivername);

                                                    pref.putString(Constant.NOTIFICATION_STATUS,
                                                            "NOTIFICATION_OFF");

                                      /*  pref.putString(Constant.COMPANY_LOGO,
                                                logo);*/

                                                    pref.putString(Constant.MODEL_NAME,
                                                            "" + modelname);
                                                    pref.putString(Constant.VID_NUMBER,
                                                            "" + vehno);

                                                    pref.putString(Constant.DRIVER_PHOTO,
                                                            driver_photo);
                                                    //   Log.e("commonUtil.LOGIN_ON",""+commonUtil.LOGIN_ON);
                                                    pref.putString(Constant.LOGIN_CHECK,
                                                            "logged_inn");

                                                    // Log.e(".LOGIN_ON",""+pref.getString(Constant.LOGIN_CHECK));
                                                    //pref.putString(Constant.PHOTO, psphoto);
                                                    pref.putString(
                                                            Constant.NOTIFICATION_CLICK_STATUS,
                                                            "0");

                                                    pref.putString(
                                                            Constant.DRIVER_MESSAGE_STATUS,
                                                            "1");
                                                    pref.putString(Constant.PREVIOUS_CURRENT_STATUS, commonUtil.OFF_DUTY);

                                                    JSONObject jvb = new JSONObject();
                                                    jvb = response.getJSONObject("state_rules");
                                                    // Log.e("jvb",""+jvb.toString());
                                                    String hmdriver = jvb.getString("day_drive_cycle");
                                                    String hmonduty = jvb.getString("day_onduty_cycle");
                                                    pref.putString(Constant.HOME_DRIVE_HOURS, "" + hmdriver);
                                                    pref.putString(Constant.HOME_ONDUTY_HOURS, "" + hmonduty);
                                                    pref.putString(Constant.FEDERAL_DRIVE_ACTIVE, "deactive");
                                                    pref.putString(Constant.CURRENT_STATUS_BB, commonUtil.OFF_DUTY);
                                                    pref.putString(Constant.NETWORK_DIALOG_CALLING, "0");
                                                    if (vinnumber != null && vinnumber.length() > 0 && !vinnumber.contentEquals("null") && !vinnumber.contentEquals("Demo")) {
                                                        if (response.has("d_type")) {
                                                            devicetype = response.getString("d_type");
                                                            if (devicetype != null && devicetype.length() > 0) {
                                                                if (devicetype.contentEquals("1")) {
                                                                    pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "yes");
                                                                } else {
                                                                    pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "no");
                                                                }
                                                            } else {
                                                                pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "no");
                                                            }

                                                        } else {
                                                            pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "no");
                                                        }

//                                                    Intent intr = new Intent(cons, Home_activity.class);
//                                                    intr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    intr.putExtra("EXIT", true);
//                                                    startActivity(intr);

                                                        if (pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) != null &&
                                                                pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {
                                                            pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY, "on");
                                                            pref.putString(Constant.NETWORK_TYPE, "" + Constant.BLUETOOTH);
                                                            Intent mIntent = new Intent(
                                                                    Loginactivitynew.this,
                                                                    Home_activity_bluetooth.class);
                                                            startActivity(mIntent);
                                                            finish();
                                                        } else {
                                                            pref.putString(Constant.NETWORK_TYPE, "" + Constant.CELLULAR);
//                                                        Intent mIntent = new Intent(
//                                                                Loginactivitynew.this,
//                                                                Home_activity.class);
                                                            Intent mIntent = new Intent(
                                                                    Loginactivitynew.this,
                                                                    Home_activity_bluetooth.class);
                                                            startActivity(mIntent);
                                                            finish();
                                                        }

                                                        Toast.makeText(cons, "Successfully logged in",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
//                                                    finish();
                                                    } else {

//                                                    Intent intr = new Intent(cons, Select_vehicle.class);
                                                        Intent intr = new Intent(cons, Vehicle_seect.class);
                                                        intr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intr.putExtra("EXIT", true);
                                                        startActivity(intr);
                                                        //Log.e("c succ", ""+"sucesssssssssss");

                                                        Toast.makeText(cons, "Successfully logged in",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
//                                                    finish();
                                                    }
                                                } else {
                                                    if (dialog != null && dialog.isShowing()) {
                                                        dialog.dismiss();
                                                    }

                                                    final AlertDialog alertDialog = new AlertDialog.Builder(
                                                            Loginactivitynew.this).create();

                                                    // Setting Dialog Title
                                                    alertDialog.setTitle("e-logbook");

                                                    // Setting Dialog Message
                                                    alertDialog.setMessage("Please register/assign company");

                                                    // Setting Icon to Dialog
                                                    // alertDialog.setIcon(R.drawable.tick);

                                                    // Setting OK Button
                                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Write your code here to execute after dialog closed
                                                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                                            alertDialog.dismiss();
                                                        }
                                                    });

                                                    // Showing Alert Message
                                                    alertDialog.show();
                                                }
                                            } else {
                                                if (dialog != null && dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }

                                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                                        Loginactivitynew.this).create();

                                                // Setting Dialog Title
                                                alertDialog.setTitle("e-logbook");

                                                // Setting Dialog Message
                                                alertDialog.setMessage("Please register/assign company");

                                                // Setting Icon to Dialog
                                                // alertDialog.setIcon(R.drawable.tick);

                                                // Setting OK Button
                                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Write your code here to execute after dialog closed
                                                        // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                                        alertDialog.dismiss();
                                                    }
                                                });

                                                // Showing Alert Message
                                                alertDialog.show();
                                            }

                                            //  syncprocess();


                                        } else if (status.equalsIgnoreCase("0")) {
                                            String message = response
                                                    .getString("message");
                                            Toast.makeText(getApplicationContext(),
                                                            message, Toast.LENGTH_SHORT)
                                                    .show();

                                        }

                                        cancelprogresssdialog();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                //Log.e("Exceptionwwwwwwww", e.toString());
                            }


                        }
                    }else{
                        cancelprogresssdialog();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    cancelprogresssdialog();
                    //Log.e("Exceptionwttttttt", t.toString());
                    // cancelprogresssdialogz();
                }
            });
        }
    }

//    private void login_validation(
//            final String stcode, String macaddress, String uname) {
//        dialog = new ProgressDialog(Loginactivitynew.this,
//                AlertDialog.THEME_HOLO_LIGHT);
//        if (lat != null) {
//            try {
//                getAddressFromLocation(Double.parseDouble(lat), Double.parseDouble(lat));
//            } catch (Exception e) {
//
//            }
//        }
//
//        if (OnlineCheck.isOnline(this)) {
//
//            dialog.setMessage("Please wait...");
//            dialog.setCancelable(false);
//            try {
//                dialog.show();
//            } catch (Exception e) {
//
//            }
//            WebServices.Loginnew(this, lat, lon, stcode, macaddress, uname, straname, imenumber1, imenumber2, "" + straddress, "" + strstate,androidId,unique_id,
//                    new JsonHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              String responseString, Throwable throwable) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers,
//                                    responseString, throwable);
//
//                            cancelprogresssdialog();
//                          //   Log.e("sds1", ""+responseString);
//                            // CommonMethod.showMsg(getActivity(), ""+
//                            // responseString);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              Throwable throwable, JSONArray errorResponse) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers, throwable,
//                                    errorResponse);
//                             //Log.e("sds1", ""+errorResponse);
//                            cancelprogresssdialog();
//                            // CommonMethod.showMsg(getActivity(), ""+
//                            // errorResponse);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              Throwable throwable, JSONObject errorResponse) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers, throwable,
//                                    errorResponse);
//                             //Log.e("sds2", ""+errorResponse);
//                            cancelprogresssdialog();
//
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              JSONArray response) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, response);
//                            cancelprogresssdialog();
//                             // Log.e("response logcv", ""+response.toString());
//                            if (response != null) {
//
//                            }
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              JSONObject response) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, response);
//                            //   dialog.dismiss();
//                            Log.e("responselog", ""+response.toString());
//
//                            try {
//
//                                // Log.e("response log", ""+response.toString());
//                                if (response != null) {
//
//                                    String status = response
//                                            .getString("status");
//
//                                    if (status.equalsIgnoreCase("1")) {
//                                        String driverid = response
//                                                .getString("s_id");
//
//                                        pref.putString(Constant.LICENSE_ELOG, stcode);
//                                        String compcode = response
//                                                .getString("ccode");
//                                        String driver_type="0";
//                                        if(response.has("driver_type"))
//                                        {
//                                            driver_type = response
//                                                    .getString("driver_type");
//                                            pref.putString(Constant.ELOG_DRIVER_TYPE,""+driver_type);
//                                        }
//                                        if (response.has("ccode")) {
//                                            if (compcode != null && compcode.length() > 0 && !compcode.contentEquals("null")) {
//
//                                                String vinnumber = response
//                                                        .getString("vin");
//                                                String drivername = response
//                                                        .getString("s_name").trim();
//                                                editor.putString("driver_name",
//                                                        drivername);
//                                                editor.putString(Constant.LOGIN_CHECK,
//                                                        "logged_inn");
//                                                editor.commit();
//
//                                     /*   String logo = response
//                                                .getString("logo");*/
//
//                                                String driver_photo = response
//                                                        .getString("driver_photo");
//                                                String strphone = response
//                                                        .getString("phone");
//
//                                                String states = response
//                                                        .getString("state");
//                                                String licenseno = response
//                                                        .getString("licenseno");
//                                                //Log.e("kk","dd");
//                                                String modelname = response
//                                                        .getString("model_name");
//                                                // Log.e("modell","@@@@"+modelname);
//                                                String vehno = response
//                                                        .getString("vid");
//                                                String str_timezone= response
//                                                        .getString("timezone");
//                                                if (response.has("ccode")) {
//
//                                                    pref.putString(Constant.COMPANY_CODE,
//                                                            compcode);
//                                                }
//                                                //eldkey
//                                                if (response.has("eldkey")) {
//                                                    String dkey = response.getString("eldkey");
//                                                    if (dkey != null && dkey.length() > 0 && !dkey.contentEquals("null")) {
//                                                        pref.putString(Constant.ELD_KEY, "" + dkey);
//                                                    } else {
//                                                        pref.putString(Constant.ELD_KEY, "");
//                                                    }
//                                                } else {
//                                                    pref.putString(Constant.ELD_KEY, "");
//                                                }
//                                                pref.putString(Constant.DRIVER_HOME_TIMEZONE,""+str_timezone);
//
//
//                                                if (response.has("make")) {
//                                                    make = response.getString("make");
//                                                    if (make != null && make.length() > 0 && !make.contentEquals("null")) {
//                                                        if (make.contains(" ")) {
//                                                            StringTokenizer st = new StringTokenizer(make, " ");
//                                                            if (st.hasMoreTokens()) {
//                                                                pref.putString(Constant.ELD_MAKE, "" + st.nextToken());
//                                                            }
//
//
//                                                        } else {
//                                                            pref.putString(Constant.ELD_MAKE, "" + make);
//                                                        }
//                                                    } else {
//                                                        pref.putString(Constant.ELD_MAKE, "");
//                                                    }
//                                                } else {
//                                                    pref.putString(Constant.ELD_MAKE, "");
//                                                }
//
//                                                if (response.has("year")) {
//                                                    String year = response.getString("year");
//                                                    if (year != null && year.length() > 0 && !year.contentEquals("null")) {
//                                                        pref.putString(Constant.ELD_YEAR, "" + year);
//                                                    } else {
//                                                        pref.putString(Constant.ELD_YEAR, "");
//                                                    }
//                                                } else {
//                                                    pref.putString(Constant.ELD_YEAR, "");
//                                                }
//
//                                                if (response.has("companyname")) {
//                                                    String cname = response.getString("companyname");
//                                                    if (cname != null && cname.length() > 0 && !cname.contentEquals("null")) {
//                                                        pref.putString(Constant.COMPANY_NAME, "" + cname);
//                                                    } else {
//                                                        pref.putString(Constant.COMPANY_NAME, "");
//                                                    }
//
//                                                } else {
//                                                    pref.putString(Constant.COMPANY_NAME, "");
//
//                                                }
//
//
//                                                if (response.has("email")) {
//                                                    String email = response.getString("email");
//                                                    if (email != null && email.length() > 0 && !email.contentEquals("null")) {
//                                                        pref.putString(Constant.COMPANYMAIL, "" + email);
//                                                    } else {
//                                                        pref.putString(Constant.COMPANYMAIL, "");
//                                                    }
//
//                                                } else {
//                                                    pref.putString(Constant.COMPANYMAIL, "");
//
//                                                }
//
//                                                if (response.has("blu_time")) {
//                                                    String blutime = response.getString("blu_time");
//                                                    if (blutime != null && blutime.length() > 0) {
//                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "" + blutime);
//                                                    } else {
////                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "150000" );//2.5 minutes
//                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "120000");//45seconds
//                                                    }
//                                                } else {
//                                                    pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "120000");
//                                                }
//
//                                                if (response.has("blu_speedtime")) {
//                                                    String blusptime = response.getString("blu_speedtime");
//                                                    if (blusptime != null && blusptime.length() > 0) {
//                                                        pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "" + blusptime);
//                                                    } else {
////                                                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "150000" );//2.5 minutes
//                                                        pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "60000");//30seconds
//                                                    }
//                                                } else {
//                                                    pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "60000");
//                                                }
//
//                                                if (response.has("blu_address")) {
//                                                    String bluaddress = response.getString("blu_address");
//                                                    String bluadname = response.getString("blu_name");
//                                                    // Log.e("bluaddress","@"+bluaddress);
//                                                    if (bluaddress != null && bluaddress.length() > 0) {
//                                                        pref.putString(Constant.BLUETOOTH_ADDRESS, "" + bluaddress);
//                                                        pref.putString(Constant.BLUETOOTH_NAME, "" + bluadname);
//                                                    } else {
//                                                        pref.putString(Constant.BLUETOOTH_ADDRESS, "");
//                                                        pref.putString(Constant.BLUETOOTH_NAME, "");
//                                                    }
//                                                } else {
//                                                    pref.putString(Constant.BLUETOOTH_ADDRESS, "");
//                                                    pref.putString(Constant.BLUETOOTH_NAME, "");
//                                                }
//
//
//                                                if (strphone == null || strphone.contentEquals("null")) {
//                                                    strphone = "";
//                                                }
//                                                pref.putString(Constant.DRIVER_PHONE, "" + strphone);
//                                                pref.putString(Constant.DRIVER_ID,
//                                                        driverid);
//                                                pref.putString(Constant.VIN_NUMBER,
//                                                        vinnumber);
//                                                pref.putString(Constant.LICENSE_NUMBER,
//                                                        licenseno);
//                                                pref.putString(Constant.ALERT_SYNC_TIME,
//                                                        "00:00");
//                                                if (states != null && states.length() > 0 && !states.contentEquals("null")) {
//
//                                                } else {
//                                                    states = "";
//                                                }
//                                                pref.putString(Constant.STATE_FIELD, "" + states);
//
//                                                String stime = response
//                                                        .getString("s_time");
//                                                pref.putString(Constant.SERVER_TIME,
//                                                        stime);
//                                                pref.putString(Constant.PC_STATUS,
//                                                        commonUtil.PC_DISABLE);
//                                                pref.putString(Constant.DRIVER_NAME,
//                                                        drivername);
//
//                                                pref.putString(Constant.NOTIFICATION_STATUS,
//                                                        "NOTIFICATION_OFF");
//
//                                      /*  pref.putString(Constant.COMPANY_LOGO,
//                                                logo);*/
//
//                                                pref.putString(Constant.MODEL_NAME,
//                                                        "" + modelname);
//                                                pref.putString(Constant.VID_NUMBER,
//                                                        "" + vehno);
//
//                                                pref.putString(Constant.DRIVER_PHOTO,
//                                                        driver_photo);
//                                                //   Log.e("commonUtil.LOGIN_ON",""+commonUtil.LOGIN_ON);
//                                                pref.putString(Constant.LOGIN_CHECK,
//                                                        "logged_inn");
//
//                                                // Log.e(".LOGIN_ON",""+pref.getString(Constant.LOGIN_CHECK));
//                                                //pref.putString(Constant.PHOTO, psphoto);
//                                                pref.putString(
//                                                        Constant.NOTIFICATION_CLICK_STATUS,
//                                                        "0");
//
//                                                pref.putString(
//                                                        Constant.DRIVER_MESSAGE_STATUS,
//                                                        "1");
//                                                pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.OFF_DUTY);
//
//                                                JSONObject jvb = new JSONObject();
//                                                jvb = response.getJSONObject("state_rules");
//                                                // Log.e("jvb",""+jvb.toString());
//                                                String hmdriver = jvb.getString("day_drive_cycle");
//                                                String hmonduty = jvb.getString("day_onduty_cycle");
//                                                pref.putString(Constant.HOME_DRIVE_HOURS, "" + hmdriver);
//                                                pref.putString(Constant.HOME_ONDUTY_HOURS, "" + hmonduty);
//                                                pref.putString(Constant.FEDERAL_DRIVE_ACTIVE, "deactive");
//                                                pref.putString(Constant.CURRENT_STATUS_BB, commonUtil.OFF_DUTY);
//                                                pref.putString(Constant.NETWORK_DIALOG_CALLING, "0");
//                                                if (vinnumber != null && vinnumber.length() > 0 && !vinnumber.contentEquals("null") && !vinnumber.contentEquals("Demo")) {
//                                                    if (response.has("d_type")) {
//                                                        devicetype = response.getString("d_type");
//                                                        if (devicetype != null && devicetype.length() > 0) {
//                                                            if (devicetype.contentEquals("1")) {
//                                                                pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "yes");
//                                                            } else {
//                                                                pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "no");
//                                                            }
//                                                        } else {
//                                                            pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "no");
//                                                        }
//
//                                                    } else {
//                                                        pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH, "no");
//                                                    }
//
////                                                    Intent intr = new Intent(cons, Home_activity.class);
////                                                    intr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                                    intr.putExtra("EXIT", true);
////                                                    startActivity(intr);
//
//                                                    if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) !=null &&
//                                                            pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes"))
//                                                    {
//                                                        pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"on");
//                                                        pref.putString(Constant.NETWORK_TYPE, "" + Constant.BLUETOOTH);
//                                                        Intent mIntent = new Intent(
//                                                                Loginactivitynew.this,
//                                                                Home_activity_bluetooth.class);
//                                                        startActivity(mIntent);
//                                                        finish();
//                                                    }else {
//                                                        pref.putString(Constant.NETWORK_TYPE, "" + Constant.CELLULAR);
////                                                        Intent mIntent = new Intent(
////                                                                Loginactivitynew.this,
////                                                                Home_activity.class);
//                                                        Intent mIntent = new Intent(
//                                                                Loginactivitynew.this,
//                                                                Home_activity_bluetooth.class);
//                                                        startActivity(mIntent);
//                                                        finish();
//                                                    }
//
//                                                    Toast.makeText(cons, "Successfully logged in",
//                                                            Toast.LENGTH_SHORT).show();
//                                                    dialog.dismiss();
////                                                    finish();
//                                                } else {
//
////                                                    Intent intr = new Intent(cons, Select_vehicle.class);
//                                                    Intent intr = new Intent(cons, Vehicle_seect.class);
//                                                    intr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    intr.putExtra("EXIT", true);
//                                                    startActivity(intr);
//                                                    //Log.e("c succ", ""+"sucesssssssssss");
//
//                                                    Toast.makeText(cons, "Successfully logged in",
//                                                            Toast.LENGTH_SHORT).show();
//                                                    dialog.dismiss();
////                                                    finish();
//                                                }
//                                            } else {
//                                                if (dialog != null && dialog.isShowing()) {
//                                                    dialog.dismiss();
//                                                }
//
//                                                final AlertDialog alertDialog = new AlertDialog.Builder(
//                                                        Loginactivitynew.this).create();
//
//                                                // Setting Dialog Title
//                                                alertDialog.setTitle("e-logbook");
//
//                                                // Setting Dialog Message
//                                                alertDialog.setMessage("Please register/assign company");
//
//                                                // Setting Icon to Dialog
//                                                // alertDialog.setIcon(R.drawable.tick);
//
//                                                // Setting OK Button
//                                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        // Write your code here to execute after dialog closed
//                                                        // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
//                                                        alertDialog.dismiss();
//                                                    }
//                                                });
//
//                                                // Showing Alert Message
//                                                alertDialog.show();
//                                            }
//                                        } else {
//                                            if (dialog != null && dialog.isShowing()) {
//                                                dialog.dismiss();
//                                            }
//
//                                            final AlertDialog alertDialog = new AlertDialog.Builder(
//                                                    Loginactivitynew.this).create();
//
//                                            // Setting Dialog Title
//                                            alertDialog.setTitle("e-logbook");
//
//                                            // Setting Dialog Message
//                                            alertDialog.setMessage("Please register/assign company");
//
//                                            // Setting Icon to Dialog
//                                            // alertDialog.setIcon(R.drawable.tick);
//
//                                            // Setting OK Button
//                                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    // Write your code here to execute after dialog closed
//                                                    // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
//                                                    alertDialog.dismiss();
//                                                }
//                                            });
//
//                                            // Showing Alert Message
//                                            alertDialog.show();
//                                        }
//
//                                        //  syncprocess();
//
//
//                                    } else if (status.equalsIgnoreCase("0")) {
//                                        String message = response
//                                                .getString("message");
//                                        Toast.makeText(getApplicationContext(),
//                                                message, Toast.LENGTH_SHORT)
//                                                .show();
//
//                                    }
//
//                                    cancelprogresssdialog();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              String responseString) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, responseString);
//                            cancelprogresssdialog();
//                        }
//
//                    });
//        }
//
//    }


    public static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            // System.out.println("Timer task started at:"+new Date());
            //completeTask();
            //System.out.println("Timer task finished at:"+new Date());


            //    long lc=86400000-lk;

            long a = lk / 1000;
            long b = a / 3600;//hour
            long c = a - b * 3600;
            long d = c / 60;//minutes
            long e = c - d * 60;//seconds
            // txttime.setText(b + ":" + d + ":" + e);


            lk = lk + 1000;
            h1 = b;
            m1 = d;
            s1 = e;

        }
    }

    public void setcurrenttime(String dates) { //txttime.setText(h1 + ":" + m1 + ":" + s1);

        final String dte = dates;
        newtimer = new CountDownTimer(1000000000, 60000) {

            public void onTick(long millisUntilFinished) {
                //Calendar c = Calendar.getInstance();
                isRunning = true;

                currenttime.setText(pad(h1) + ":" + pad(m1));

//Log.e("timerrunnibg","tyer");


            }

            public void onFinish() {
                isRunning = false;
            }
        };
        newtimer.start();
    }

    //@Override
    protected void onDestroy() {

        super.onDestroy();
        timerTask.cancel();
        if (isRunning) {
            newtimer.cancel();

        }
        mGoogleApiClient.disconnect();
    }




    public void CheckGpsStatus() {

        locationManager = (LocationManager) cons.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private String getlength(int i) {

        if (i > 9) {
            return "" + i;
        }
        return "0" + i;
    }


    public String getMonth(int month) {
        String monthtext = null;

        switch (month) {
            case 1:
                monthtext = "Jan";
                break;
            case 2:
                monthtext = "Feb";
                break;
            case 3:
                monthtext = "Mar";
                break;
            case 4:
                monthtext = "Apr";
                break;
            case 5:
                monthtext = "May";
                break;
            case 6:
                monthtext = "Jun";
                break;
            case 7:
                monthtext = "Jul";
                break;
            case 8:
                monthtext = "Aug";
                break;
            case 9:
                monthtext = "Sep";
                break;
            case 10:
                monthtext = "Oct";
                break;
            case 11:
                monthtext = "Nov";
                break;
            case 12:
                monthtext = "Dec";
                break;
        }
        return monthtext;
    }

    public static String pad(Long num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        //  Log.e("calling", "onstart");
        mGoogleApiClient.connect();
    }

    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
//Log.e("lat","@"+lat);
        // strcurrenttime=location.getTime();

        //String sttime1z=getDateCurrentTimeZone(strcurrenttime);
        // pref.putString(Constant.SERVER_TIME, sttime1z);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Log.e("lattttdd", "########" + lat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //pd.dismiss();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(15000); // Update location every second
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
           // Log.e("lat000","@"+lat);
            try {
                // Log.e("latlat1", "@" + lat);
                if (lat != null) {
                    // Log.e("calling", "" + address);
                    pref.putString(Constant.CURRENT_LATITUDE, "" + lat);
                    pref.putString(Constant.CURRENT_LOGINGITUDE, "" + lon);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
//        LocationAddress locationAddress = new LocationAddress();
//        locationAddress.getAddressFromLocation(latitude, longitude,
//                getApplicationContext(), new GeocoderHandler());
                    getAddressFromLocation(latitude, longitude);
                }
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Loginactivitynew.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(Loginactivitynew.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {


                } else {

                }
            }
        }
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    private void displayFirebaseRegId() {
        SharedPreferences prefz = getApplicationContext().getSharedPreferences(Trucksoft_elog_DriverConfig.ISOFT_SHARED_PREF, 0);
        String regId = prefz.getString("regId", null);
if(regId !=null)
{
    pref.putString(Constant.ELOG_FIREBASE_KEY,""+regId);
}
      //  Log.e("Firebase reg id: ", "" + regId);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Trucksoft_elog_Msg_Service.setActivityContext(this);
//        CheckGpsStatus() ;
//
//        if (!GpsStatus) {
//            turnGPSOn();
//        }
//        //Log.e("resume","call resume");
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.DRIVER_APP_REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

        // clear the notification area when the app is opened
        Trucksoft_elog_Notify_Utils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
//            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            permissionsNeeded.add("Write Contacts");
//        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
//            permissionsNeeded.add("Read Contacts");
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            permissionsNeeded.add("Write Contacts");
//        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//            permissionsNeeded.add("Read phone state");
//        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
//            permissionsNeeded.add("Send sms");
//        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
//            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
//        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
//            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean bool = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    bool = false;
            }
            bool = true;
        }
        return bool;
    }



    private void getMyLocation() {
        //Log.e("cv","getaccesslocation");
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(Loginactivitynew.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // Log.e("calling",""+"gps");
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(Loginactivitynew.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mLastLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    //Log.e("calling",""+"gps fail");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(Loginactivitynew.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(Loginactivitynew.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void getversionname() {
        straname = "" + listvals.get(Build.VERSION.SDK_INT);
        //Log.e("1212",""+listvals.get(Build.VERSION.SDK_INT));
    }





    private void callloginalert() {
        if (pref.getString(Constant.FORCE_LOGIN) != null && pref.getString(Constant.FORCE_LOGIN).contentEquals("active")) {
//            if (lat == null && lon == null) {
//                lat = String.valueOf(gps.getLatitude());
//                lon = String.valueOf(gps.getLongitude());
//            }
            // InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            pref.putString(Constant.FORCE_LOGIN, "deactive");
            stcode = pref.getString(Constant.DRIVER_LISCENCE);
            String uname = pref.getString(Constant.DRIVER_LISCENCE);


            if (stcode != null && stcode.length() > 0) {
                //String macaddress = getMacAddress();
                String macaddress = getWifiMacAddress();


                pref.putString(Constant.LATITUDE, "" + lat);
                pref.putString(Constant.LONGITUDE, "" + lon);
              //  login_validation(stcode, macaddress, uname);
logincheck(stcode, macaddress, uname);
                           /* Intent mIntent = new Intent(
                                    LoginActivity.this,
                                    Schedule.class);

                            startActivity(mIntent);*/
            } else {
                Toast.makeText(getApplicationContext(),
                        "Unable to get License.\nPlease retry",
                        Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void setforceloginalert(String license) {
        if (dialoglogi != null) {
            if (dialoglogi.isShowing()) {
                dialoglogi.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_alert, null);

        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", cons));
        txtstatus.setText("I see that you are moving. Did you need me to login to your logbook?");
        //final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialoglogi = new Dialog(cons, R.style.DialogTheme);
        dialoglogi.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialoglogi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglogi.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoglogi.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialoglogi.getWindow().setAttributes(lp);
        dialoglogi.show();
        //Log.e("inttt","kkzz");
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
                callloginalert();

            }
        });

    }
    public void logout_ok() {
        if (OnlineCheck.isOnline(this)) {
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.ELOG_NUMBERSS,
                "");
        //pref.getString(Constant.ELOG_NUMBERSS);
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dc = formatsec.format(new Date());
        api = ApiServiceGenerator.createService(Eld_api.class);
        //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
        Call<JsonObject> call = api.getlogout("" + pref.getString(Constant.DRIVER_ID), "" + pref.getString(Constant.DRIVER_ID)
                , "" + pref.getString(Constant.VIN_NUMBER), "" + dc, "", "", "" + imenumber1, "" + imenumber2, "");

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
                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                //Log.e("Exceptionwttttttt", t.toString());
                // cancelprogresssdialogz();
            }
        });
    }
    }


    private void cancelprogresssdialog() {
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialog = null;
        }
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, E_logbook_ForegroundService.class);
        stopService(serviceIntent);
    }

    public String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(cons, Locale.getDefault());

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


                // Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }
        return straddress;
    }


private void callprivacyy()
{
    Intent mIntent = new Intent(
            Loginactivitynew.this,
            Privacy_elogbook.class);
    startActivity(mIntent);
}

//    private void callprivacy()
//    {
//        if (dialogprivacy != null) {
//            if (dialogprivacy.isShowing()) {
//                dialogprivacy.dismiss();
//            }
//        }
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.privacy_dialogue, null);
//
//
//        final TextView btnsubmitz = dialogView.findViewById(R.id.tsubmit);
//
//        dialogprivacy = new Dialog(cons, R.style.DialogTheme);
//        dialogprivacy.setCancelable(false);
//        dialogprivacy.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogprivacy.setContentView(dialogView);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialogprivacy.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//
//        dialogprivacy.getWindow().setAttributes(lp);
//        dialogprivacy.show();
//        btnsubmitz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogprivacy.dismiss();
//                pref.putString(Constant.PRIVACY_KEY_ISOFTOFFICE,"1");
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			/*if (checkPermission(PERMISSION_LOCATION)) {
//
//			} else {
//				requestForPermission(PERMISSION_LOCATION);
//			}*/
//                    //Log.e("version",""+Build.VERSION_CODES.LOLLIPOP);
//                    //Log.e("version",""+android.os.Build.VERSION.SDK_INT);
//                    insertDummyContactWrapper();
//                }
//            }
//        });
//
//    }



    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void requestGPSSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(500);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                       // Log.i("", "All location settings are satisfied.");
                        Toast.makeText(getApplication(), "GPS is already enable", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                       // Log.i("", "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            status.startResolutionForResult(Loginactivitynew.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                        Toast.makeText(getApplication(), "Location settings are inadequate, and cannot be fixed here", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);      if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }    return uniqueID;
    }
}