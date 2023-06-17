package com.trucksoft.isoft.isoft_elog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_activity.BaseActivity;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.Services.E_logbook_ForegroundService;
import com.isoft.trucksoft_elog.company.companydashboard;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class Company_Login extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private EditText edtemail,edtpass;
    private Button btnLogin;

    ProgressDialog dialog;
    private String strstate = "";
    private String straddress = "";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

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
    private Calendar calendar;
    private  Preference pref;
    TimerTask timerTask;
    private TextView txtloctime;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 3000; /* 2 sec */
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    Font_manager_elog font_manager;
   // private TextView helpline;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private TextView txtcompreg;
    Eld_api api;
    ProgressDialog dialogz;
    private ImageView imgback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companylogin);
        cons = this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
       // gps = new GPSTracker(getApplicationContext());
        pref = Preference.getInstance(this);
        txtcompreg=findViewById(R.id.txt_compreg);
        font_manager = new Font_manager_elog();
        edtemail =findViewById(R.id.edt_email);
        edtpass =findViewById(R.id.edt_password);
        imgback=findViewById(R.id.driver_list_iv_back);

        btnLogin =findViewById(R.id.login);
     //   helpline = findViewById(R.id.helpline);
        txtloctime = findViewById(R.id.txjjjjhttime);
        txttime =findViewById(R.id.txttime);
        currenttime = findViewById(R.id.txtctime);
        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/digita.ttf");
        currenttime.setTypeface(face1);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        btnLogin.setTypeface(face);
        stopService();
        setUpGClient();
        final LocationManager manager = (LocationManager)cons.getSystemService    (Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            requestGPSSettings();

        }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                insertDummyContactWrapper();
            }

        calendar = Calendar.getInstance();


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

        timerTask = new MyTimerTask();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        //timer.scheduleAtFixedRate(timerTask, 0, 1000);
        timer.schedule(timerTask, 1000, 60 * 1000);
        setcurrenttime(myDate);

//        helpline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    String phone = "(855) 922-9700";
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//                    startActivity(intent);
//                }catch (Exception e)
//                {
//
//                }
//
//            }
//        });
        txtcompreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Company_Login.this,
                        Company_Registration.class);
                startActivity(mIntent);

                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    if (lat != null && lon != null) {
                       String stremail = edtemail.getText().toString().trim();
                        String strpass= edtpass.getText().toString().trim();



                        if (stremail !=null && stremail.length()>0 && strpass !=null && strpass.length()>0) {

               fetchlogin(stremail,strpass);
                        } else if (stremail ==null || stremail.length()==0)
                        {
                           edtemail.setError("Please enter E-mail");
                        } else if (strpass !=null && strpass.length()>0)
                        {
                            edtemail.setError("Please enter password");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter all fields",
                                    Toast.LENGTH_SHORT).show();
                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                "Unable to get location.\nPlease retry",
//                                Toast.LENGTH_SHORT).show();
//                    }



            }


        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Company_Login.this,
                        Loginactivitynew.class);
                startActivity(mIntent);

                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(
                Company_Login.this,
                Loginactivitynew.class);
        startActivity(mIntent);

        finish();
    }

    private void fetchlogin(String stremail, String strpass) {
        dialogz = new ProgressDialog(cons,
                AlertDialog.THEME_HOLO_LIGHT);
        dialogz.setMessage("Please wait...");
        dialogz.setCancelable(false);
        dialogz.show();
        if (OnlineCheck.isOnline(cons)) {

            api = ApiServiceGenerator.createService(Eld_api.class);


            Call<JsonObject> call = null;

            call = api.getcompanylogin(stremail,
                    "" + strpass, "" + straddress,lat,lon);


            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                   // Log.e("Responsestring", response.body().toString());
                    cancelprogresssdialogz();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String jsonresponse = response.body().toString();
                            //Log.e("jsonresponse", jsonresponse.toString());
                            try {
                                JSONObject resp = new JSONObject(jsonresponse);
                                if (response != null) {

                                    if (resp.has("status")) {
                                       // Log.e("statusz",""+resp.getString("status"));
                                        String stat = resp.getString("status");
                                        //Log.e("stat",""+stat);
                                        if(stat.contentEquals("false"))
                                        {
                                            String msg = resp.getString("error");
                                            Toast.makeText(cons,msg,Toast.LENGTH_SHORT).show();
                                        }else{
                                            String cmpres = resp.getString("comp_data");
                                            Toast.makeText(cons,"Login Successfully",Toast.LENGTH_SHORT).show();
//                                            Intent mIntent = new Intent(
//                                                    Company_Registration.this,
//                                                    Company_Login.class);
//                                            startActivity(mIntent);
//                                            finish();
                                            try {
                                                JSONObject respz = new JSONObject(cmpres);
                                                String ccode = respz.getString("ccode");
                                                String cname = respz.getString("companyname");
                                                String dot = respz.getString("dot");
                                                String cemail = respz.getString("email");
                                                String cstate = respz.getString("state");
                                                Log.e("ccode",""+ccode);
                                                Log.e("cname",""+cname);
                                                pref.putString(Constant.COMPANY_LOGIN_STATUS,"success");
                                                pref.putString(Constant.COMPANY_LOGIN_CODE,ccode);
                                                pref.putString(Constant.COMPANY_NAME,cname);
                                                pref.putString(Constant.COMPANY_DOT_NUMBER,""+dot);
                                                pref.putString(Constant.COMPANY_EMAIL,""+cemail);
                                                pref.putString(Constant.COMPANY_STATE,""+cstate);

                                                Intent mIntent = new Intent(
                                                        Company_Login.this,
                                                        companydashboard.class);
                                            startActivity(mIntent);
                                            finish();
                                            }catch (Exception e)
                                            {

                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("eeeeeee", e.toString());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Responsestringerrr", t.toString());
                    Log.e("Respccc", call.toString());
                    cancelprogresssdialogz();
                }
            });

        }
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
            if (ContextCompat.checkSelfPermission(Company_Login.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(Company_Login.this,
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



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");

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
                int permissionLocation = ContextCompat.checkSelfPermission(Company_Login.this,
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
                                            .checkSelfPermission(Company_Login.this,
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
                                        status.startResolutionForResult(Company_Login.this,
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
        int permissionLocation = ContextCompat.checkSelfPermission(Company_Login.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
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
                            status.startResolutionForResult(Company_Login.this, REQUEST_CHECK_SETTINGS);
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
    private  void cancelprogresssdialogz() {

        try {
            if ((dialogz != null) && dialogz.isShowing()) {
                dialogz.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialogz = null;
        }
    }
}