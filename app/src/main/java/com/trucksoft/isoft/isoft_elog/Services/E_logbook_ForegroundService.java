package com.trucksoft.isoft.isoft_elog.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.geometris.wqlib.AbstractWherequbeStateObserver;
import com.geometris.wqlib.BaseRequest;
import com.geometris.wqlib.GeoData;
import com.geometris.wqlib.RequestHandler;
import com.geometris.wqlib.WQError;
import com.geometris.wqlib.WQScanner;
import com.geometris.wqlib.Whereqube;
import com.geometris.wqlib.WherequbeService;
import com.google.gson.Gson;
import com.isoft.trucksoft_elog.AppModel;
import com.isoft.trucksoft_elog.Isoft_activity.Home_activity_bluetooth;
import com.isoft.trucksoft_elog.Isoft_activity.Responsemodel;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Getvalue_model;
import com.isoft.trucksoft_elog.Model_class.Res_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class E_logbook_ForegroundService extends Service {
    public static final String CHANNEL_ID = "e-logbook";
    Preference pref;
    Eld_api api;
    private String straddress;
    private String strstate;
    String lat, lon;
    private String vinnumber;
    List<Whereqube> deviceList;
    //MyObserver mWherequbeObserver = new MyObserver();
    WQScanner mScanner = null;
    final IntentFilter uiIf = new IntentFilter();
    private boolean mScanning;
    protected long SCAN_PERIOD = 4000; ////scanning for 5 seconds
    private int iscellularRunning = 0;
    Double bluetoothspeed=0.0;
    boolean boolactice=false;
    boolean isRunning = false;
    long rset = 0;
    CountDownTimer bluetoothtimer;
    CountDownTimer drivingtimer;
    private int intschedule = 0;
    boolean boolatime=false;
    private final int STATUS_DRIVING = 2;
    private final int STATUS_SLEEPER = 3;
    private final int STATUS_OFF_DUTY = 4;
    private final int STATUS_ON_DUTY = 1;
    private CommonUtil commonUtil;
    String strbluestatus="OFF_DUTY";
    String strcstatus="OFF_DUTY";
    int blue_intschedule=158;
    private String olddversion = "";
    private String olddversionname = "";
    List<Getvalue_model> movies;
    private String timezonesid="";
    private String timezonesname="";
    int dialogscanningval=0;
    private String str_obdtime="";
    private String str_datetime="";
    private String str_cdatetime="";
    private static Handler bHandler = new Handler();
    int apicall=0;
   // Timer btimer;
    int ik=0;
    Timer btimer;
    BluetoothAdapter bluetoothadapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    BroadcastReceiver uiRefresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseRequest bs = AppModel.getInstance().mLastEvent;
            if(bs != null && bs.requestId == BaseRequest.OBD_MEASUREMENT) {
                GeoData geoData = (GeoData) bs.getObject();
                if(geoData!= null)

                    updateOBDData(geoData);
            }

        }
    };
//    Map<String, String> trackparams = new HashMap<>();
//    private int bltrack = 0;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e("calling","foreground");
        bluetoothspeed=0.0;
        pref= Preference.getInstance(getApplicationContext());
        commonUtil = new CommonUtil(getApplicationContext());
        String input = intent.getStringExtra("inputExtra");
        if (pref.getString(Constant.LOGIN_CHECK) != null && pref.getString(Constant.LOGIN_CHECK).length() > 0 && !pref.getString(Constant.LOGIN_CHECK).contentEquals("null")) {
            if (pref.getString(Constant.LOGIN_CHECK).equalsIgnoreCase("logged_inn")) {

                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    olddversion = "" + packageInfo.versionCode;
                    olddversionname = "" + packageInfo.versionName;
                    //  Log.e("oldversion",""+olddversion);
                } catch (Exception e) {

                }
                movies = new ArrayList<>();


                apicall=0;








                try {
                    // clearing the notification tray
                    Trucksoft_elog_Notify_Utils.clearNotifications(getApplicationContext());
                    createNotificationChannel();
                    Intent notificationIntent = new Intent(this, Home_activity_bluetooth.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this,
                            0, notificationIntent, 0);

                    Uri alarmSound;

                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getApplicationContext().getPackageName() + "/raw/appupdatetone");
                    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setContentTitle("Bluetooth Connection")
                            .setContentText(input)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setSound(alarmSound)
                            .build();

                    startForeground(1, notification);
                } catch (Exception e) {

                }



                //if device login only it's run
                try {
                    strcstatus = pref.getString(Constant.CURRENT_STATUS_BB);

                    if (strcstatus.contentEquals(commonUtil.OFF_DUTY)) {
                        intschedule = STATUS_OFF_DUTY;

                    } else if (strcstatus.contentEquals(commonUtil.ON_DUTY)) {

                        intschedule = STATUS_ON_DUTY;

                    } else if (strcstatus.contentEquals(commonUtil.SLEEP)) {

                        intschedule = STATUS_SLEEPER;

                    } else if (strcstatus.contentEquals(commonUtil.DRIVING)) {
                        intschedule = STATUS_DRIVING;
                    }

                }catch (Exception e)
                {

                }
                vinnumber= pref.getString(Constant.VIN_NUMBER);
                //trackparams.put("did",""+pref.getString(Constant.DRIVER_ID));
                //trackparams.put("back",""+pref.getString(Constant.DRIVER_ID));
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            bltrack=1;
//        }else{
//            bltrack=0;
//        }
                //do heavy work on a background thread
                try {
                    AppModel.getInstance().mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (AppModel.getInstance().mBtAdapter == null) {
                        Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e)
                {

                }
                deviceList = new ArrayList<Whereqube>();
                mWherequbeObserver.register(this);
                if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
                    Toast.makeText(this,"Bluetooth Low Energy not supported", Toast.LENGTH_SHORT).show();
                    // finish();
                }

                mScanner = new WQScanner(results);
                // Log.e(TAG, "populab  teList");
                try {
                    if (AppModel.getInstance().mBtAdapter.isEnabled()) {
                        populateList();
                        //  tscan.setText("Device Scanning      :");
                    }
                }catch (Exception e)
                {

                }

                mWherequbeObserver.register(this);

                WherequbeService.getInstance().setReqHandler(BaseRequest.OBD_MEASUREMENT, myEventHandler);



                uiIf.addAction("REFRESH");

                try
                {

                    registerReceiver(uiRefresh, uiIf);

                    //  startRepeatingTask();
                }catch (Exception e)
                {

                }
                btimer = new Timer();

                btimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        //Log.e("btimer", "@@"+"running");
                        try {
                            String stcheck = "";
                            try {
                                bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
                                if (bluetoothadapter != null) {
                                    if (!bluetoothadapter.isEnabled()) {
                                        stcheck = "deactivate";
                                        if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {
                                            iscellularRunning = 0;
                                            savebluetoothstatus("0", "0");
                                        }
                                    } else {
                                        stcheck = "activate";
                                    }
                                }
                            } catch (Exception e) {

                            }
//                    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                    {
//                        trackparams.put("_"+stcheck,""+stcheck);
//                    }
                            //Log.e("stcheck", "@" + stcheck);
                            if (stcheck.contentEquals("activate")) {
                                SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                if (str_datetime != null) {
                                    str_cdatetime = str_datetime;
                                }
                                str_datetime = formattime.format(new Date());
                                // Log.e("TAG", "" + str_datetime);
                                // System.out.println("Task Timer on Fixed Rate");
                                //  Log.e("val", "Task Timer on Fixed Rate");
                                //Log.e("str_obdtime", "@@@" + str_obdtime);
                                if (str_obdtime != null && str_obdtime.length() > 0) {
                                    long obd = splittime(str_obdtime);
                                    long tbd = splittime(str_datetime);
                                    long resbd = tbd - obd;

                                    // Log.e("obd", "@" + obd);
                                    // Log.e("tbd", "@" + tbd);
                                    //Log.e("resbd", "@" + resbd);

                                    if (resbd > 40) {// if obd response not received more than 60 seconds then call scanning function
                                        if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {
                                            savebluetoothstatus("0", "0");
                                            iscellularRunning = 0;
//                                    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                                    {
//                                        trackparams.put("_disc","yes");
//                                    }
                                        }
                                        callautoconnect();
                                    } else {
                                        //if bluetooth disconnected -> call scanning
                                        if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
                                            callautoconnect();
                                        }
                                    }
                                } else {
                                    long obd = splittime(str_cdatetime);
                                    long tbd = splittime(str_datetime);
                                    long resbd = tbd - obd;

                                    //Log.e("obdz", "@" + obd);
                                    //Log.e("tbdz", "@" + tbd);
                                    //Log.e("resbdz", "@" + resbd);
                                    //ncorrection

                                    if (resbd > 40) {// if obd response not received more than 60 seconds then call scanning function
                                        //Log.e("caamm", "@");
                                        if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {
                                            savebluetoothstatus("0", "0");
                                            iscellularRunning = 0;
                                            pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
//                                    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                                    {
//                                        trackparams.put("_discc","yes");
//                                    }
                                        }
                                        callautoconnect();
                                        if (str_datetime != null) {
                                            str_cdatetime = str_datetime;
                                        }
                                    } else {
                                        //if bluetooth disconnected -> call scanning
                                        if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
                                            //  Log.e("caammdk", "@");
                                            callautoconnect();
                                        }
                                    }
                                }
                            }
                        }catch(Exception e)
                        {

                        }
                        // Log.e("bltrackzzz", "@@"+bltrack);
//                if(bltrack!=0) {
//                    if (bltrack == 4) {
//                        savetrackresp();
//                        bltrack = 1;
//                    }
//                    bltrack++;
//                }
                    }
                }, 45000, 45000);//4000 18
            }
        }


        return START_NOT_STICKY;
    }
    static RequestHandler myEventHandler = new RequestHandler() {
        @Override
        public void onRecv(@NonNull Context context, @NonNull BaseRequest request) {

            AppModel.getInstance().mLastEvent = request;
            Intent intent = new Intent("REFRESH");
            context.sendBroadcast(intent);
        }
    };

    @Override
    public void onDestroy() {
        try{
       btimer.cancel();
        }catch (Exception e)
        {

        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    class MyObserver extends AbstractWherequbeStateObserver
    {

        public  void onConnected()
        {  pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"1");

        }

        @Override
        public void onSynced() {

        }

        @Override
        public void onDiscovered() {

        }

        public  void onDisconnected()
        {pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");

        }

        public  void onError(WQError ec)
        {
            //Log.w(TAG, "Error:"+ec.mCode);
        }

    }


 MyObserver mWherequbeObserver = new MyObserver();
    private void populateList() {
        /* Initialize device list container */
        //Log.e(TAG, "populateList");
        scanLeDevice(true);

    }
    private void scanLeDevice(final boolean enable) {
//        Log.e("scanzddd","enable"+enable);
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            trackparams.put("_scandevice",""+enable);
//        }
        if (enable) {
            if(mScanning == true) return;
            // Stops scanning after a predefined scan period.
            //Log.e("scanz","enable"+enable);
            clearScanResults();
            mScanning = true;
            mScanner.start(SCAN_PERIOD);
            //setProgressBarIndeterminateVisibility(true);
            // mScanButton.setEnabled(false);
        }
        else {
            //Log.e("scanz","timeout"+enable);
            // Cancel the scan timeout callback if still active or else it may fire later.
            mScanning = false;
            mScanner.stop();
            //setProgressBarIndeterminateVisibility(false);
            //mScanButton.setEnabled(true);
        }
    }
    private void clearScanResults() {

        deviceList.clear();
        // Make sure the display is updated; any old devices are removed from the ListView.
        //mScanResultsAdapter.notifyDataSetChanged();
    }

    WQScanner.ScanResultListener results = new WQScanner.ScanResultListener()
    {
        @Override
        public void onScanCompleted(final List<Whereqube> wqubes) {

          //  Log.e( "Wherequbescanned ","" + wqubes.size());
           // Log.e( "Wherequbescannedecc ","" + wqubes.size());
            // tscan.setText("Device Scanning       :  "+wqubes.size());
            // Note: Dont try to stop scan in the callback

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {

                    for (Whereqube wqube: wqubes) {
                        //  tscanstatus.setVisibility(View.VISIBLE);
                        //   tscanstatus.setText("Device detected :");
//                        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                        {
//                            trackparams.put("_scaner","");
//                        }
                        addDevice(wqube);
                    }
                }
            });






//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    for (Whereqube wqube: wqubes) {
//                        //  tscanstatus.setVisibility(View.VISIBLE);
//                        //   tscanstatus.setText("Device detected :");
//                        addDevice(wqube);
//                    }
//                }
//            });
        }
    };
    static long connection_request_time = 0;
    private void addDevice(Whereqube device) {
        boolean deviceFound = false;

        for (Whereqube listDev : deviceList) {
            if (listDev.mDevice.getAddress().equals(device.mDevice.getAddress())) {
                deviceFound = true;
                break;
            }
        }
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            trackparams.put("_adddevice",""+deviceList.size());
//        }
        if (!deviceFound) {

            if (pref.getString(Constant.BLUETOOTH_ADDRESS) != null && pref.getString(Constant.BLUETOOTH_ADDRESS).length() > 0) {
                if (device.mDevice.getAddress().contentEquals(pref.getString(Constant.BLUETOOTH_ADDRESS))) {
                    deviceList.add(device);
                    Collections.sort(deviceList);
                }
            }else if (pref.getString(Constant.BLUETOOTH_NAME) != null && pref.getString(Constant.BLUETOOTH_NAME).length() > 0) {
                if (device.mDevice.getAddress().contentEquals(pref.getString(Constant.BLUETOOTH_NAME))) {
                    deviceList.add(device);
                    Collections.sort(deviceList);
                }
            }else{
                deviceList.add(device);
                Collections.sort(deviceList);
            }
        }
        if(connection_request_time !=0 && (System.currentTimeMillis()-connection_request_time)<60000)
            return; //Wait for 60 seconds before issuing another connect request.
        if( pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {

            try {
                stopScanning();
                String deviceAddress = device.mDevice.getAddress();
                String devicename = device.mDevice.getName();
                if (deviceAddress != null && deviceAddress.length() > 0) {
                    if (pref.getString(Constant.BLUETOOTH_ADDRESS) != null && pref.getString(Constant.BLUETOOTH_ADDRESS).length() > 0) {
                        if (deviceAddress.contentEquals(pref.getString(Constant.BLUETOOTH_ADDRESS).trim()) || devicename.contentEquals(pref.getString(Constant.BLUETOOTH_ADDRESS).trim())) {

                            // tscanconnect.setVisibility(View.VISIBLE);
                            // tscanconnect.setText("Device Connected :"+deviceAddress+" "+devicename);
                            AppModel.getInstance().mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                            if (WherequbeService.getInstance().isServiceInitialized()) {
                                connection_request_time = System.currentTimeMillis();
                                pref.putString(Constant.BLUETOOTH_ADDRESS, "" + deviceAddress);
                                pref.putString(Constant.BLUETOOTH_NAME, "" + devicename);
                                WherequbeService.getInstance().connect(deviceAddress);
                                SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                str_datetime = formattime.format(new Date());
                                //Log.e("TAG",""+str_datetime);
                                registerReceiver(uiRefresh, uiIf);
                            }
                        }
                    }else{
                        AppModel.getInstance().mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                        if (WherequbeService.getInstance().isServiceInitialized()) {
                            connection_request_time = System.currentTimeMillis();
                            pref.putString(Constant.BLUETOOTH_ADDRESS, "" + deviceAddress);
                            pref.putString(Constant.BLUETOOTH_NAME, "" + devicename);
                            WherequbeService.getInstance().connect(deviceAddress);
                            SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                            str_datetime = formattime.format(new Date());
                            //Log.e("TAG",""+str_datetime);
                            registerReceiver(uiRefresh, uiIf);
                        }
                    }
                }
//                if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                        && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                {
//                    trackparams.put("ddddd","");
//                }
            } catch (Exception e) {

            }
        }

    }

    private Runnable scanTimeout = new Runnable() {
        @Override
        public void run() {
            //Log.e("bHandler","scanTimeout");
            scanLeDevice(false);
        }
    };

    public void stopScanning()
    {
        //Log.e("bHandler","running");
        bHandler.postDelayed(scanTimeout, SCAN_PERIOD);
    }

    private GeoData prevGeoData = null;
    private void updateOBDData(final GeoData geoData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                try {
                    final double MILES_TO_KM = 1.609344;
//                    SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                    strealiertime = formatdatetime.format(new Date());
                    Double odometer;
                    Double odometer_miles=0.0;
                    Double odometer_milesvak=0.0;
                    Double engineHours;
                    Double speed;
                    Double RPM;
                    Double longitude;
                    Double latitude;
                    Long gpstime;
                    String vin = "";
                    String odometerText = "";
                    String odometerTextnew = "";
                    String engineHoursText = "";
                    String RPMText = "";
                    String speedText = "";
                    String latlngText = "";
                    String gpsTimeText = "";
                    // mInterval = 60000;
                    pref.putString(Constant.NETWORK_TYPE,Constant.BLUETOOTH);
                    try {
                        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        str_obdtime = formattime.format(new Date());
                    }catch (Exception e)
                    {

                    }

                    vin = geoData.getVin().trim();
                    String strvin=pref.getString(Constant.VIN_NUMBER);

                   // String bnval=checkbactive();
                    SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String time = formattime.format(new Date());
                    //tresponse.setVisibility(View.VISIBLE);


                        if (vin != null && vin.length() > 0 && vin.contentEquals("" + strvin)) {

                            pref.putString(Constant.BLUETOOTH_RESPONSE_TIME,""+time);


                            pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "1");

                            if (dialogscanningval == 0) {
                                if ((odometer = geoData.getOdometer()) != null) {
                                    odometer_milesvak = odometer / MILES_TO_KM;
                                }

                                dialogscanningval = 1;
                            }
                            DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
                            DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy/M/d h:m:s a");
                            if (geoData.isDataSet()) {
                                if (prevGeoData != null) {
                                    Long prevSeconds = new Double(Math.floor(prevGeoData.getTimeStamp().getMillis() / 1000)).longValue();
                                    Long seconds = new Double(Math.floor(geoData.getTimeStamp().getMillis() / 1000)).longValue();
                                    //Seconds seconds = Seconds.secondsBetween( prevGeoData.getTimeStamp(), geoData.getTimeStamp());
                                    //Log.e("GeometrisManager", "prevtime: " + prevSeconds + " current Timestamp: " + seconds);
                                    if ((seconds - prevSeconds) == 0) return;
                                    //dataReceivedTimestamp.setValueText(formatter.print(geoData.getTimeStamp()) + " (" + (seconds - prevSeconds) + ")");

                                } else {
                                    //dataReceivedTimestamp.setValueText(formatter.print(geoData.getTimeStamp()));
                                }

                            }
                            if (geoData.isDataSet())// && (changedData || prevGeoData==null))
                            {
                                prevGeoData = geoData.copy();
                            } else
                                return;


                            if ((odometer = geoData.getOdometer()) != null) {
                                odometer_miles = odometer / MILES_TO_KM;
                                //  odometerText = String.format(Locale.US, "%.1f(%.1f)", odometer, odometer_miles);
                                // odometerTextnew= String.format(Locale.US, "%.1f(%.1f)", odometer_miles);
                                odometerTextnew = String.format(Locale.US, "%.1f", odometer_miles);



                            } else {

                            }
                            if ((engineHours = geoData.getEngTotalHours()) != null) {
                                engineHoursText = String.format(Locale.US, "%f", engineHours);
                                // engineHoursData.setValueText(engineHoursText);
                                //engineHoursDataTimestamp.setValueText(formatter.print(geoData.getEngTotalHoursTimestamp()));
                            } else {
                                //engineHoursData.setValueText("--");
                                //engineHoursDataTimestamp.setValueText("--");
                            }
                            if ((RPM = geoData.getEngineRPM()) != null) {
                                RPMText = String.format(Locale.US, "%.0f", RPM);
                                // RPMData.setValueText(RPMText);
                                // RPMDataTimestamp.setValueText(formatter.print(geoData.getEngineRpmTimestamp()));

                            } else {
                                //  RPMData.setValueText("--");
                                // RPMDataTimestamp.setValueText("--");
                            }
                            if ((speed = geoData.getVehicleSpeed()) != null) {


                                bluetoothspeed = geoData.getVehicleSpeed();
                                speed /= MILES_TO_KM;//spmk
                                speedText = String.format(Locale.US, "%.0f", speed);
//                                if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                        && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                                {
//                                    trackparams.put("_speed",""+speed);
//                                }

                                try
                                {

                                        boolactice=pref.getBolean(Constant.BLUETOOTH_FLAG);


                                }catch (Exception e)
                                {

                                }
                                if (!boolactice) {

                                    if (iscellularRunning == 0) {

                                        savebluetoothstatus("1", "" + odometer_miles);
                                        iscellularRunning=1;
                                    }
                                    if (speed > 5) {

                                        if (bluetoothtimer != null) {
                                            try {
                                                isRunning = false;
                                                bluetoothtimer.cancel();

                                            } catch (Exception e) {

                                            }
                                        }

                                        if (intschedule == STATUS_DRIVING) {

                                        } else {

                                            if(apicall==0) {
                                                apicall=1;


                                                if (OnlineCheck.isOnline(getApplicationContext())) {
                                                    // onClickStatusDriving();
                                                    intschedule = STATUS_DRIVING;
                                                    onClickStatusDrivingbluetooth();

                                                }
                                            }


                                        }




                                    } else {

                                        strbluestatus = pref.getString(Constant.CURRENT_STATUS_BB);

                                        if (strbluestatus.contentEquals(commonUtil.OFF_DUTY)) {
                                            blue_intschedule = STATUS_OFF_DUTY;

                                        } else if (strbluestatus.contentEquals(commonUtil.ON_DUTY)) {

                                            blue_intschedule = STATUS_ON_DUTY;

                                        } else if (strbluestatus.contentEquals(commonUtil.SLEEP)) {

                                            blue_intschedule = STATUS_SLEEPER;

                                        }


                                        if (intschedule == blue_intschedule) {

                                        } else {
                                            // Log.e("isRunning",""+isRunning);
                                            if (isRunning) {

                                            } else {
                                                //  Log.e("isRunningstaus", "fail");
                                                long speedtimed = 60000;
                                                try {
                                                    String dconnecttimesp = pref.getString(Constant.BLUETOOTH_SPEED_CHANGE_TIME);
                                                    speedtimed = Long.parseLong(dconnecttimesp);
                                                } catch (Exception e) {

                                                }
                                               
                                                bluetoothtimer = new CountDownTimer(speedtimed, 1000) {

                                                    public void onTick(long millisUntilFinished) {
                                                        //   Log.e(" remaining140: ","" + millisUntilFinished / 1000);
                                                        //here you can have your logic to set text to edittext
                                                        isRunning = true;

                                                    }

                                                    public void onFinish() {
                                                        isRunning = false;

                                                        if (blue_intschedule == STATUS_ON_DUTY) {

                                                            if (OnlineCheck.isOnline(getApplicationContext())) {
                                                                if(apicall==0) {
                                                                    apicall = 1;
                                                                    intschedule = STATUS_ON_DUTY;
                                                                    onClickStatusOnDutyBluetooth("Stopped / Under 5MPH...");
                                                                }
                                                            }
                                                        } else if (blue_intschedule == STATUS_OFF_DUTY) {

                                                            if (OnlineCheck.isOnline(getApplicationContext())) {
                                                                if(apicall==0) {
                                                                    apicall = 1;
                                                                    intschedule = STATUS_OFF_DUTY;

                                                                    onClickStatusOffDutyblueetooth("Stopped / Under 5MPH...");
                                                                }
                                                            }
                                                        } else if (blue_intschedule == STATUS_SLEEPER) {

                                                            if (OnlineCheck.isOnline(getApplicationContext())) {

                                                                if(apicall==0) {
                                                                    apicall = 1;
                                                                    intschedule = STATUS_SLEEPER;

                                                                    onClickStatusSleeperbluetooth("Stopped / Under 5MPH...");
                                                                }
                                                            }
                                                        }

                                                    }

                                                }.start();

                                            }


                                        }



                                    }
                                } else {
                                    //Log.e("result","faoile"+boolactice);
                                }
                                //
                                // speedData.setValueText(speedText);
                                // speedDataTimestamp.setValueText(formatter.print(geoData.getVehicleSpeedTimestamp()));
                            } else {

                                //  speedData.setValueText("--");
                                //  speedDataTimestamp.setValueText("");
                            }
                            if ((longitude = geoData.getLongitude()) != null) {

                                latlngText = String.format(Locale.US, "%f", longitude);
                                //longitudeData.setValueText(latlngText);
                            } else {
                                //longitudeData.setValueText("--");
                            }
                            if ((latitude = geoData.getLatitude()) != null) {

                                latlngText = String.format(Locale.US, "%f", latitude);
                                //latitudeData.setValueText(latlngText);
                            } else {
                                //latitudeData.setValueText("--");
                            }
                            if ((gpstime = geoData.getGpsTime()) != null) {
                                // gpsTimestamp.setValueText(formatter1.print(gpstime));
                            } else {
                                // gpsTimestamp.setValueText("--");
                            }
                            // saverec(geoData);
                            if(ik==0)
                           {
                                saverec(geoData);
                            }
                        }

//                    else{
//                        ttval.setText("..");
//                        txtspeed.setText("Speed :0");
//                        savebluetoothstatus("0","");
//                    }
                } catch (Exception e) {
                    //  Log.e(TAG, e.toString());
                }
            }
        });
    }

    public void onClickStatusDrivingbluetooth() {


        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
        {
            gettodaysavevaluesbluetooth(commonUtil.DRIVING, intschedule, "","...");
        }



    }

    public void onClickStatusOffDutyblueetooth( String rmark) {
        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
        {
            gettodaysavevaluesbluetooth(commonUtil.OFF_DUTY, intschedule, "",rmark);
        }

    }
    public void onClickStatusSleeperbluetooth(String rmark) {
        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
            gettodaysavevaluesbluetooth(commonUtil.SLEEP, intschedule, "",rmark);
        }
    }
    public void onClickStatusOnDuty() {
        if (intschedule != STATUS_ON_DUTY) {
            // setcurrentstatus(commonUtil.ON_DUTY,intschedule,"");
            if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
            {
                gettodaysavevaluesbluetooth(commonUtil.ON_DUTY, intschedule, "","");
            }
        }
    }
    public void onClickStatusOnDutyBluetooth(String rmark) {

        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
        {
            gettodaysavevaluesbluetooth(commonUtil.ON_DUTY, intschedule, "",rmark);
        }


    }
    private void gettodaysavevaluesbluetooth(final String field, final int statusid, final String statuss,String rmark) {
        try {
            // Log.e("latlat1", "@" + lat);
            if (lat != null) {
                //Log.e("calling1", "" + address);
                double latitude = Double.parseDouble(lat);
                double longitude = Double.parseDouble(lon);
//                LocationAddress locationAddress = new LocationAddress();
//                locationAddress.getAddressFromLocation(latitude, longitude,
//                        getApplicationContext(), new GeocoderHandler());
                getAddressFromLocation(latitude, longitude);
            }
        } catch (Exception e) {

        }
        apicall = 1;
        vinnumber= pref.getString(Constant.VIN_NUMBER);
        String did = pref.getString(Constant.DRIVER_ID);

        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Getvalue_model>> call = api.getsaveValues_eldbluetooth(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress, olddversion, strstate,timezonesname,timezonesid,"bluetooth",rmark,field,"backcalling","break","");
        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if (response.isSuccessful()) {

                    movies = response.body();
                    settodayval(movies,"0");


                } else {
                    apicall = 0;
                }
            }

            @Override
            public void onFailure(Call<List<Getvalue_model>> call, Throwable t) {
                apicall = 0;
            }
        });
    }
    public String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

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
        //Log.e("rule", "@"+pref.getString(Constant.FEDERAL_RULE_DAY));
        if(pref.getString(Constant.FEDERAL_RULE_DAY).contentEquals("deactive"))
        {
          //  checkstate();
            //Log.e("callz","checkstate");
        }else{

        }


        return straddress;
    }
    private void settodayval(List<Getvalue_model> vals,String vald) {
        rset = 0;
        boolactice=false;
        SimpleDateFormat formatime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strsl = formatime.format(new Date());
        if (vals.size() > 0 && vals != null) {
            for (int i = 0; i < vals.size(); i++) {
                Getvalue_model gt = new Getvalue_model();
                gt = vals.get(i);
                String ontotal = gt.getsonduty();

                String oftotal = gt.getsoffdutty();
                // Log.e("oftotal",""+oftotal);
                String drtotal = gt.getsdriving();
                // Log.e("drtotal",""+drtotal);
                String sltotal = gt.getssleep();
                //Log.e("sltotal",""+sltotal);
                String cstatus = gt.getstatus();
                String soldsleep = gt.getsoldsleep();
//Log.e("soldsleep",""+soldsleep);
                String skonstatus = gt.getpcstatus();

                try{
                    if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null") && vald.contentEquals("20")) {
                        if(cstatus.contentEquals(commonUtil.OFF_DUTY))
                        {
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.OFF_DUTY);

                        }else if(cstatus.contentEquals(commonUtil.ON_DUTY))
                        {
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.ON_DUTY);

                        }else if(cstatus.contentEquals(commonUtil.SLEEP))
                        {
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.SLEEP);

                        }else{
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.OFF_DUTY);

                        }
                    }
                }catch (Exception e)
                {

                }



                String reserthr = gt.getResethr();
                String tenreserthr = gt.getTenresethr();

                String rule=gt.getRule();
                if(rule !=null && rule.contentEquals("Federal"))
                {
                    pref.putString(Constant.FEDERAL_RULE_DAY,"active");

                }else{
                    pref.putString(Constant.FEDERAL_RULE_DAY,"deactive");

                }
                //  Log.e("breakduration",""+breakduration);

                String breaklive = gt.getBreaklive();
                pref.putString(Constant.VOICE_ON, "" + gt.getVoiceon());
                pref.putString(Constant.VOICE_OFF, "" + gt.getVoiceoff());
                pref.putString(Constant.VOICE_SLEEP, "" + gt.getVoicesleep());
                pref.putString(Constant.VOICE_DRIVE, "" + gt.getVoicedrive());
                //  Log.e("vstat","###"+gt.getVoiceon());
                if (skonstatus != null && skonstatus.length() > 0 && !skonstatus.contentEquals("null")) {

                } else {
                    skonstatus = "0";
                }

                if (skonstatus != null && skonstatus.length() > 0 && !skonstatus.contentEquals("null")) {
                    if (skonstatus.contentEquals("1")) {
                        boolactice=true;
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);

                    } else if (skonstatus.contentEquals("2")) {
                        boolactice=true;
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);


                    } else if (reserthr != null && !reserthr.contentEquals("00:00:00") && !reserthr.contentEquals("null")) {

                        boolactice=true;
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);

                    } else if (skonstatus.contentEquals("3")) {
                        boolactice=false;
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);

                    } else if (skonstatus.contentEquals("4")) {

                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);

                        boolactice=true;

                    } else if (tenreserthr != null && !tenreserthr.contentEquals("00:00:00") && !tenreserthr.contentEquals("null")) {

                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);

                        boolactice=true;


                    } else {
                        boolactice=false;

                        pref.putString(Constant.PC_STATUS, commonUtil.PC_DISABLE);


                    }
                } else {
                    boolactice=false;

                    pref.putString(Constant.PC_STATUS, commonUtil.PC_DISABLE);

                }


                String of_status = gt.getofstatus();
                if (of_status != null && of_status.length() > 0 && !of_status.contentEquals("null")) {
                    if (of_status.contentEquals("0")) {
                        intschedule = STATUS_OFF_DUTY;
                        pref.putString(Constant.CURRENT_STATUS,commonUtil.OFF_DUTY);

                    }
                }

                String on_status = gt.getonstatus();
                if (on_status != null && on_status.length() > 0 && !on_status.contentEquals("null")) {
                    if (on_status.contentEquals("0") && cstatus.contentEquals("ON_DUTY")) {
                        intschedule = STATUS_ON_DUTY;
                        pref.putString(Constant.CURRENT_STATUS,commonUtil.ON_DUTY);
                    }
                }
                //3


                String dr_status = gt.getdrstatus();

                if (dr_status != null && dr_status.length() > 0 && !dr_status.contentEquals("null")) {
                    if (dr_status.contentEquals("0") && cstatus.contentEquals("DRIVING")) {
                        {


                            intschedule = STATUS_DRIVING;
                            pref.putString(Constant.CURRENT_STATUS,commonUtil.DRIVING);

                        }
                    }
                }
                //4

                String sl_status = gt.getslstatus();
                if (sl_status != null && sl_status.length() > 0 && !sl_status.contentEquals("null")) {
                    if (sl_status.contentEquals("0") && cstatus.contentEquals("SLEEP")) {
                        intschedule = STATUS_SLEEPER;
                        pref.putString(Constant.CURRENT_STATUS,commonUtil.SLEEP);
                    } else {

                    }
                }


            }
        }
        apicall = 0;

    }

private void savebluetoothstatus( String val,String odometer) {
    try {
        // Log.e("latlat1", "@" + lat);
        if (lat != null) {
            //Log.e("calling1", "" + address);
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);
//                LocationAddress locationAddress = new LocationAddress();
//                locationAddress.getAddressFromLocation(latitude, longitude,
//                        getApplicationContext(), new GeocoderHandler());
            getAddressFromLocation(latitude, longitude);
        }
    } catch (Exception e) {

    }


    String did = pref.getString(Constant.DRIVER_ID);

    api = ApiServiceGenerator.createService(Eld_api.class);
    String blname="";
    String bladdress="";
    try{
        blname=pref.getString(Constant.BLUETOOTH_NAME);
        bladdress=pref.getString(Constant.BLUETOOTH_ADDRESS);
    }catch (Exception e)
    {

    }
    String stcheck="";
    try {
        bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothadapter != null) {
            if (!bluetoothadapter.isEnabled()) {
                stcheck = "deactivate";
            } else {
                stcheck = "activate";
            }
        }
    }catch (Exception e)
    {

    }
    //Log.e("valll","Bth_conn.php?vin="+str_vin+"&bt_status=1&address="+straddress
    // +"&did="+did+"&blu_address="+bladdress+"&blue_name="+blname
    // +"&odometer="+odometer);
String str_vin=pref.getString(Constant.VIN_NUMBER);

    Call<Res_model> call = api.savebluetoothstatusbck(str_vin, ""+val, "" +straddress,did,bladdress,blname,odometer,""+stcheck,"back" );
    call.enqueue(new Callback<Res_model>() {

        public void onResponse(Call<Res_model> call, Response<Res_model> response) {
            if (response.isSuccessful()) {
                try{
                    Res_model rk=response.body();

                    if(rk.status.contentEquals("true")) {
                        String bluaddress = rk.blu_address;
                        String bluname = rk.blue_name;

                        if(pref.getString(Constant.BLUETOOTH_ADDRESS)==null || pref.getString(Constant.BLUETOOTH_ADDRESS).length()==0) {
                            if(bluaddress !=null && bluaddress.length()>0) {

                                pref.putString(Constant.BLUETOOTH_ADDRESS, "" + bluaddress);
                                pref.putString(Constant.BLUETOOTH_NAME, "" + bluname);
                            }
                        }
                    }

                }catch (Exception e)
                {

                }


            } else {

            }
        }

        @Override
        public void onFailure(Call<Res_model> call, Throwable t) {
            //Log.e("dd"," Response Error "+t.getMessage());

        }
    });
}

private void callautoconnect()
{
//    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//    {
//        trackparams.put("_autoconect","yes");
//    }
    if(pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
        try {
            //mWherequbeObserver.register(context);
            if(mScanner == null)
                mScanner = new WQScanner(results);
            if(AppModel.getInstance().mBtAdapter.isEnabled()) {
                populateList();
            }
            try {
                WherequbeService.getInstance().setReqHandler(BaseRequest.OBD_MEASUREMENT, myEventHandler);


                uiIf.addAction("REFRESH");
            }catch (Exception e)
            {

            }
        }catch (Exception e)
        {

        }
    }
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

    private  void saverec(GeoData geoData) {
        ik=1;
        //btnclockout.setVisibility(View.GONE);
        final Responsemodel listclock = new Responsemodel();
        try
        {
        //Log.e("kd", "" + "ok");


        String val = "";

        api = ApiServiceGenerator.createService(Eld_api.class);
        String jsonInString = new Gson().toJson(geoData);
        JSONObject mJSONObject = new JSONObject();
            SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String stk = formattime.format(new Date());
        try {
            mJSONObject = new JSONObject(jsonInString);

        } catch (Exception e) {

        }
            SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
           String strealiertime = formatdatetime.format(new Date());
        Call<Responsemodel> call = api.saverecdd(mJSONObject,"FR_"+strealiertime);

        call.enqueue(new Callback<Responsemodel>() {
            @Override
            public void onResponse(Call<Responsemodel> call, Response<Responsemodel> response) {
                if (response.isSuccessful()) {
                   // cancelprogresssdialog();
                    //Log.e(" Responsecqevv", "z " + response.body());
                    if (response.body() != null) {
                        // listclock.addAll(response.body());

                    } else {
                        // Toast.makeText(context,"Clock Out failed. Please try again",Toast.LENGTH_LONG).show();
                    }

                    //  adapter = new Currentreportadap(context, movies);


                } else {
                    //cancelprogresssdialog();

                }
            }

            @Override
            public void onFailure(Call<Responsemodel> call, Throwable t) {
                //cancelprogresssdialog();
            }
        });
    }catch (Exception e)
        {

        }

    }

//    private void savetrackresp() {
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            Log.e("calldabe", "track");
//            api = DispatchServiceGenerator.createService(ReportApi.class);
//            Call<Response_model> call = api.savetrckrespp(trackparams);
//
//
//            call.enqueue(new Callback<Response_model>() {
//                @Override
//                public void onResponse(Call<Response_model> call, Response<Response_model> response) {
//                    Log.e(" Responsev", " " + response.toString());
//                    Log.e(" Responsesskk", " " + String.valueOf(response.code()));
//                    if (response.isSuccessful()) {
//
//
//                        if (response.body() != null) {
//                            // movies.addAll(response.body());
//                            // Log.e(" Responsecqevv","z "+response.body());
//
//
//                        } else {
//
//                        }
//
//                    } else {
//
//
//                    }
//                    trackparams = new HashMap<>();
//                    trackparams.put("did",""+pref.getString(Constant.DRIVER_ID));
//                    trackparams.put("back",""+pref.getString(Constant.DRIVER_ID));
//                }
//
//                @Override
//                public void onFailure(Call<Response_model> call, Throwable t) {
//                    trackparams = new HashMap<>();
//                    trackparams.put("back",""+pref.getString(Constant.DRIVER_ID));
//
//                }
//            });
//        }
//    }

}
