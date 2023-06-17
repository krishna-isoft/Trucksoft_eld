package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.geometris.wqlib.AbstractWherequbeStateObserver;
import com.geometris.wqlib.BaseRequest;
import com.geometris.wqlib.GeoData;
import com.geometris.wqlib.RequestHandler;
import com.geometris.wqlib.WQError;
import com.geometris.wqlib.WQScanner;
import com.geometris.wqlib.Whereqube;
import com.geometris.wqlib.WherequbeService;
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
import com.isoft.trucksoft_elog.AppModel;
import com.isoft.trucksoft_elog.Break_report.Breakreport_home;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_home.Dispatch_chathome;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_service.Dispatch_GcmIntentService;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Break_info_model;
import com.isoft.trucksoft_elog.Model_class.E_logutil;
import com.isoft.trucksoft_elog.Model_class.Exception_model;
import com.isoft.trucksoft_elog.Model_class.Faultcode_model;
import com.isoft.trucksoft_elog.Model_class.Fuel_model;
import com.isoft.trucksoft_elog.Model_class.Getvalue_model;
import com.isoft.trucksoft_elog.Model_class.MainException_model;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Model_class.Res_model;
import com.isoft.trucksoft_elog.Model_class.newbrk_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.MyApplication;
import com.isoft.trucksoft_elog.Record.ViewProxy;
import com.isoft.trucksoft_elog.Services.E_logbook_ForegroundService;
import com.isoft.trucksoft_elog.Services.Myworkplan;
import com.isoft.trucksoft_elog.Trips.Trip_details;
import com.isoft.trucksoft_elog.Vehicle.Vehicle_seect;
import com.isoft.trucksoft_elog.Vehicle_notification.Notiication_home;
import com.isoft.trucksoft_elog.datausage.TrafficSnapshot;
import com.isoft.trucksoft_elog.driverchecklist.Driverchecklist_home;
import com.isoft.trucksoft_elog.fuel_receipt.Upload_fuelbill;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.isoft.trucksoft_elog.show_loc.Show_mylocation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.trucksoft.isoft.isoft_elog.R;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;


/**
 * Created by isoft on 29/5/17.
 */

public class Home_activity_bluetooth extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
    private LinearLayout linonduty;
    private LinearLayout lindriveduty;
    private LinearLayout linsleepduty;
    private LinearLayout linoffduty;
    private TextView txttoptime;
    private TextView txtondutytotal;
    private String respdatetime="";
    private TextView txtoffdutytotal;
    private TextView txtdrivetotal;
    private TextView txtsleeptotal;
    MediaRecorder mediaRecorder;
    Dialog dialogdrivealert;
    Dialog dialogwarning;
    Dialog dialogscanning;
    Dialog dialogbreaknew;

    Dialog dialogprivacy;
    private int y = 0;
    private TextView txtonfromtime;
    private TextView txtdrivefromtime;
    private TextView txtsleepfromtime;
    private TextView txtoffromtime;
    private static final int notificationID_DRIVE = 110;
    public static final int RequestPermissionCode = 1;
    private TextView txtontotime;
    private TextView txtdrivetotime;
    private TextView txtsleeptotime;
    private TextView txtoftotime;
    private int intschedule = 0;
    private String stontime;
    private String stdrivetime;
    private String stsleeptime;
    private String stofftime;
    private String strtime;
    private ArrayList<String> starray;
    Timer T;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private Context context;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String driverid;
    private Preference pref;
    private String strbrdialog;
    Dialog dialogtenrelease;
    Dialog dialogthirtyrelease;
    private CommonUtil commonUtil;
    BluetoothAdapter bluetoothadapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private ImageView imgback;
    private TextView txtuser;
    long strlofftime;
    long strlontime;
    long strldrivetime;
    long strlsleptime;
    String strfile = "";
    private EditText name;
    long rset = 0;
    private TextView txtbduration;

    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    TrafficSnapshot latest=null;
    private  long totaldatausage=0;
    private  long olddatausage=0;
    private long newusage=0;

    private TextView txtondutyleft, txtdriveleft;
    private String mFileName;
    String datetime;
    String dname;
    private int medialevel = 0;
    private int serverResponseCode = 0;
    private String upLoadServerUri = null;

    private String filepath = null;
    LocationManager locationManager;
    boolean GpsStatus;
    Dialog dialog;
    ProgressDialog progressdlog;
    Font_manager_elog font_manager;

    // DashLogic service variables
    // DataLogger j1939DataLogger;
    //  Timer j1939UpdateTimer;
    private String vinnumber;
    // Status constants
    private final int STATUS_ON_DUTY = 1;
    private final int STATUS_DRIVING = 2;
    private final int STATUS_SLEEPER = 3;
    private final int STATUS_OFF_DUTY = 4;

    private byte[] image;
    RoundedImageView imgprofile;

    int imagetype = 0;
    private static final int RESULT_LOAD_IMAGES = 10;
    private Bitmap bitmap;
    private static byte[] img;

    //    private String d_photo;
    private TextView txtvinnumber, txtlcno, txt_model;
    private String str_vin, str_lcnum;
    private TextView txt_state;
    private String mde;
    private TextView txt_curnetstaus;
    private String str_timenew;
    private int rk = 0;
    long sktime = 00;
    private String strdtime;
    private String strdatetime;
    private TextView txttenremaining;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ImageView imgok;
    private ImageView img_vehicle;

    public static boolean active = false;
    Intent intent;
    Bundle extras;
    private ImageView img_location;
    Eld_api api;
    List<Getvalue_model> movies;
    private TextView txtchat;
    private TextView txtvind;
    private TextView txtcity;
    Dialog dialogrk;
    Dialog dialogresp;
    Dialog dialogdistance;
    Dialog dialoonduty;
    Dialog dialogdrive,dialogfederal,dialoglowvoltage,dialoglogi,dialogupdateapp,dialogbreak;

    Dialog dialogrec,dialogcustomalert;
    Dialog dialoggeoalert;
    String str_remarkstatus;
    int int_remarkcount = 0;
    private String strfield = "";
    private boolean boolvoicerecord=true;
    Dialog dialogunplug;
    Dialog dialogvehicleoff;
    E_logutil eutil;
    private ArrayList<String> listvals;
    private String straname = "";

    ImageView imguploadbill;

    private String breaktype = "";

    private TextView btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    File fileShare;

    private MediaScannerConnection conn;
    private static final int REQUEST_WRITE_STORAGE = 112;
    //RoundedImageView txtvimage;
    String vpath = "";
//TextView txtspeed;
    // Speedometer speedometer;

    private SeekBar mSeekBar = null;
//    MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();
    private Handler mbluetoothHandler = new Handler();
    private int mInterval = 60000; // 5 seconds by default, can be changed later

    private boolean isPlaying = false;
    private TextView mCurrentProgressTextView = null;
    private TextView mFileLengthTextView = null;
    private TextView btn_play;
    private String fnam = "";


    private String stroldlastid = "";
    private String strnewlastid = "";
    private String strlogin = "";
    long ston;
    long stdr;
    ImageView imggraph;
    private String stroldsleeptotal;
    long oldsleeptotal = 0;
    LinearLayout lin_parent;


   // GestureDetector gestureDetector;
    //  private ImageView btnthirty;
    private ImageView imgservice;
    //private ImageView btnthirty;




    AppLocationService appLocationService;
    private String straddress;
    private String strstate;

    private String olddversion = "";
    private String olddversionname = "";
    private ImageView imgengine;

    private ImageView imgbattery;
    private int inttrack=0;
    private int repinterval=20;


    //new
    private TextView txthrtytatus;
    private TextView txtresethr;
    private TextView resetbalance;

    Animation animBlink;

    private LinearLayout lin_thirty;
    private int pppcstatus = 0;

    private String resettime = "";
    private int intstate=0;

    int ak=0;
    TextView jkk;
    private TextView txt_duration, txt_breakleft;
    LinearLayout linthirty, linbreak, linten, lintenty;

    long longbduration = 0;
    long longblive = 0;

    TextView txttitle, hrtitle;
    private Button releasebtn;
    private TextView txtkey;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private TextView txtmake;
    private TextView txtyear;

    private TextView tenstatusonof;
    private TextView txttenused;
    private TextView txttenremain;
    private String tenresettime = "";
    long tenrset = 0;

    private Button btnreleaseten;
    private TextView txtthirtyremin;

    private Button btnreleasethirty;
    private int vdk = 0;
    private int vdkstat = 0;
    private int breakevent = 0;
    private TextView txtprcstatus;
    private LinearLayout linpvc;
    private Button btnpc;
    private TextView txtpc;
    private TextView txtday;
    private String imenumber1 = "0";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private String imenumber2 = "0";


    private ImageView imgfuellevel;
    List<Fuel_model> fuelvals = new ArrayList<>();

//    private int defaultcall = 0;


    private String statedrive="12:00";
    private String stateonduty="16:00";

    private TextView txtonallowed;
    private TextView txtdriveallowed;



    private static final int MY_REQUEST_CODE=129;



private ImageView imgexception;
List<Exception_model> listmovies;
private ArrayList<String> arrayexcept=new ArrayList<>();

private String timezonesid="";
    private String timezonesname="";
    private String d_photo="";

    private TextView txtcstate,txtrules;


private TextView ttval;
private TextView tresponse;
private String strvalx="";
    private TextView tspeedcountown;

    private TextView helpline;
int h=0;

    private LinearLayout imgbreaktake;
    private LinearLayout imgtakenetwork;
    private TextView txtnetworkstate;
    private TextView tconnection;
    Drawable img_draw;
    private ImageView imgbreakrep;

    private TextView txtdurationcolon,txtdurationsecond;
    private boolean boolondutyexceed=false;
    private boolean onrefresh=false;
private TextView txtspeed;
    private ImageView imgtrip;
    Double bluetoothspeed=0.0;
    String strbluestatus="OFF_DUTY";
    final IntentFilter uiIf = new IntentFilter();
    int blue_intschedule=158;
    int apicall=0;
    private int intbval=0;
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


    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    private TextView recordTimeText;
    private Animation animBlinkrec;


    CountDownTimer bluetoothtimer;


    boolean isRunning = false;

    boolean boolactice=false;

    private int kl=0;

    boolean boolatime=false;
    private String strealiertime;
    Dialog dialogondutyalert;
    private String strondutylogid="";

    private int iscellularRunning = 0;
    private ImageView imgnotification;


    //

    List<Whereqube> deviceList;
    //MyObserver mWherequbeObserver = new MyObserver();
    WQScanner mScanner = null;
    private boolean mScanning;
    protected long SCAN_PERIOD = 3000; ////scanning for 5 seconds
    private int i=158;
    private ProgressBar progressBarCircle;
    private CountDownTimer countDownTimer;

    private long timeCountInMilliSeconds = 1 * 60000;

     int valb=0;
    Dialog dialogdrivermsg;
    private TextView tmsg;

    private TextView msg_notification;
  String breaklastid="";
    ProgressDialog dialogz;
    Dialog dialogrkship;
    Dialog dialogloaddetails;
    private LinearLayout linloaddetails;
    private String str_newtruck="";
    private String str_newtrailor="";
    private String str_tripnum="";
    private String str_cmdty="";
    FloatingTextButton floatupdateapp;
    int mdk=0;


    private static Handler bHandler = new Handler();

    Timer btimer;
    private String str_obdtime="";
    private int bl_alert=0;
    private String str_datetime;

private TextView trlayout;

private String str_obdtimexx="";

private TextView tkval;
private ImageView imgifta;
//    Map<String, String> trackparams = new HashMap<>();
//    private int bltrack = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Move this to where you establish a user session
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_bluetooth);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bluetoothspeed = 0.0;
        context = this;
        // Log.e("calling","home sa");
        commonUtil = new CommonUtil(context);
        font_manager = new Font_manager_elog();
        intent = getIntent();
        eutil = new E_logutil();
        movies = new ArrayList<>();
        extras = intent.getExtras();
        tkval = findViewById(R.id.tk);
        animBlinkrec = AnimationUtils.loadAnimation(context,
                R.anim.blink_new);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        listvals = eutil.getname();

        try {
            SimpleDateFormat formattimex = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            str_obdtimexx = formattimex.format(new Date());
        } catch (Exception e) {

        }

        ttval = findViewById(R.id.ttval);
        tresponse = findViewById(R.id.ttle);
        ttval.setVisibility(View.VISIBLE);
        tresponse.setVisibility(View.GONE);
        txtcstate = findViewById(R.id.txt_cstate);
        trlayout = findViewById(R.id.tlayout);
        trlayout.setVisibility(View.GONE);
        txtcstate.setVisibility(View.GONE);
        tspeedcountown = findViewById(R.id.q1);
        txtrules = findViewById(R.id.txt_rules);
        imgtakenetwork=findViewById(R.id.imgtakenetwork);
        txtnetworkstate=findViewById(R.id.t_networkstate);
        tconnection=findViewById(R.id.tconnection);
        imgifta=findViewById(R.id.img_ifta);

        txtnetworkstate.startAnimation(animBlink);

        imgexception = findViewById(R.id.except_img);
        imgok =  findViewById(R.id.iv_pc);
        imgfuellevel = findViewById(R.id.img_fuellevel);
        txtonallowed = findViewById(R.id.timfddsder1);
        txtdriveallowed = findViewById(R.id.tiler1);
        floatupdateapp = findViewById(R.id.update_app);
        floatupdateapp.setVisibility(View.GONE);
        linloaddetails = findViewById(R.id.imgloaddetails);
        msg_notification = findViewById(R.id.message_notification);
        helpline = findViewById(R.id.helpline);

        txtspeed = findViewById(R.id.txt_speed);
        txtdurationcolon = findViewById(R.id.txtdurationcolon);
        txtdurationsecond = findViewById(R.id.txtdurationsecond);
        imgtrip = findViewById(R.id.iv_trip);
        setnetworkicons("WIRELESS");
        imgbreaktake = findViewById(R.id.imgtakebreak);
        imgbreakrep = findViewById(R.id.img_breakrep);
        imgnotification = findViewById(R.id.tnotification);
        txtspeed.setText("Speed: 0" + " MPH");
        h = 0;
        pref = Preference.getInstance(context);
        pref.putString(Constant.MANUAL_LAST_BREAKID, "");
        pref.putString(Constant.CERTIFY_ALERT_DIALOG, "nodisplay");
        pref.putBolean(Constant.BLUETOOTH_FLAG, boolactice);
        tmsg = findViewById(R.id.tmsg);
        tmsg.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        AppModel.getInstance().mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (AppModel.getInstance().mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
          //  finish();
            return;
        }
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            bltrack=1;
//        }
        deviceList = new ArrayList<Whereqube>();
       // trackparams.put("did",""+pref.getString(Constant.DRIVER_ID));
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//        && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            trackparams.put(pref.getString(Constant.DRIVER_ID)+"_","appstart");
//        }

        if (pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {
            mWherequbeObserver.register(this);
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(this, "Bluetooth Low Energy not supported", Toast.LENGTH_SHORT).show();
                //finish();
            }
            checkEnableBt();
            mScanner = new WQScanner(results);
            // Log.e(TAG, "populab  teList");
            if (AppModel.getInstance().mBtAdapter.isEnabled()) {
                populateList();
                //  tscan.setText("Device Scanning      :");
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int check = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (check == PackageManager.PERMISSION_GRANTED) {

            } else {
                callprivacy();
            }

        }


        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            float density = getResources().getDisplayMetrics().density;
            strvalx = "height : " + height + "width : " + width + " density : " + density + " scre " + trlayout.getText().toString() + "@" + tkval.getText().toString() +"v.24.8.15";
        } catch (Exception e) {
            strvalx = trlayout.getText().toString();
        }
        MyApplication.getInstance().setOnVisibilityChangeListener(new MyApplication.ValueChangeListener() {
            @Override
            public void onChanged(Boolean value) {
                // Log.e("isAppInBackground", String.valueOf(value));
            }
        });
        if (pref.getString(Constant.BLUETOOTH_TIMER_MANUALLY) != null && pref.getString(Constant.BLUETOOTH_TIMER_MANUALLY).length() > 0) {
        } else {
            pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY, "off");
        }


        if (pref.getString(Constant.B_TIMER_STATUS) != null && pref.getString(Constant.B_TIMER_STATUS).length() > 0) {
        } else {
            pref.putString(Constant.B_TIMER_STATUS, "0");
        }
        if (pref.getString(Constant.LOCATION_UPDATE_DATE) != null && pref.getString(Constant.LOCATION_UPDATE_DATE).length() > 0) {
        } else {
            SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String ctime = formatdatetime.format(new Date());
            pref.putString(Constant.LOCATION_UPDATE_DATE, "" + ctime);
        }

        if (pref.getString(Constant.BLUETOOTH_DISCONNECT_TIME) != null && pref.getString(Constant.BLUETOOTH_DISCONNECT_TIME).length() > 0) {

        } else {
            pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "150000");
        }
        if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).length() > 0) {

        } else {
            pref.putString(Constant.BREAK_ALERT_DISPLAY, "accept");
        }
        if (pref.getString(Constant.ONDUTY_ALERT) != null && pref.getString(Constant.ONDUTY_ALERT).length() > 0) {

        } else {
            pref.putString(Constant.ONDUTY_ALERT, "deactivate");
        }
        if (pref.getString(Constant.NETWORK_TYPE) == null || pref.getString(Constant.NETWORK_TYPE).length() == 0) {
            pref.putString(Constant.NETWORK_TYPE, Constant.CELLULAR);
        }
        if (pref.getString(Constant.BREAK_LAST_ID) != null && pref.getString(Constant.BREAK_LAST_ID).length() > 0) {
            pref.putString(Constant.BREAK_LAST_ID, "");
        }
        try {
            if (pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.CELLULAR)) {
                stopService();
//        Intent ink = new Intent(context, Home_activity.class);
//        startActivity(ink);
//        finish();
            }
        } catch (Exception e) {

        }

        try {
            if (pref.getString(Constant.LOCATION_UPDATE_STATUS) != null && pref.getString(Constant.LOCATION_UPDATE_STATUS).length() > 0) {
                // repinterval=Integer.parseInt(pref.getString(Constant.LOCATION_UPDATE_STATUS));
            } else {
                pref.putString(Constant.LOCATION_UPDATE_STATUS, "30");
                repinterval = 20;
            }
            if (pref.getString(Constant.LOCATION_UPDATE_DATE) != null && pref.getString(Constant.LOCATION_UPDATE_DATE).length() > 0) {
            } else {
                SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String ctime = formatdatetime.format(new Date());
                pref.putString(Constant.LOCATION_UPDATE_DATE, "" + ctime);
            }
        } catch (Exception e) {

        }


        // tbaz.setText("height : "+height +"width : "+width+" density : "+density);
        try {
            PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(Myworkplan.class, repinterval, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
                    .build();
            WorkManager.getInstance(context).enqueue(periodicWork);
        } catch (Exception e) {

        }

        if (pref.getString(Constant.FEDERAL_RULE_DAY) != null && pref.getString(Constant.FEDERAL_RULE_DAY).length() > 0) {

        } else {
            pref.putString(Constant.FEDERAL_RULE_DAY, "deactive");
        }

        if (pref.getString(Constant.CURRENT_STATUS_BB) != null && pref.getString(Constant.CURRENT_STATUS_BB).length() > 0) {

        } else {
            pref.putString(Constant.CURRENT_STATUS_BB, commonUtil.OFF_DUTY);
        }
        statedrive = pref.getString(Constant.HOME_DRIVE_HOURS);
        stateonduty = pref.getString(Constant.HOME_ONDUTY_HOURS);
        pref.putString(Constant.FEDERAL_ONDUTY_HOURS, "14:00");
        pref.putString(Constant.FEDERAL_DRIVE_HOURS, "11:00");
        txtonallowed.setText(stateonduty + " Hr");
        txtdriveallowed.setText(statedrive + " Hr");
        linthirty = findViewById(R.id.linthirty);
        lintenty = findViewById(R.id.lintenty);
        linten = findViewById(R.id.linten);
        txtpc = findViewById(R.id.txtpc);
        linbreak = findViewById(R.id.linbreak);
        linpvc = findViewById(R.id.linpc);
        txtprcstatus = findViewById(R.id.txt_prc);
        btnreleaseten = findViewById(R.id.releasetenbtn);
        txtkey = findViewById(R.id.txt_key);
        txtday = findViewById(R.id.txt_day);
        btnpc = findViewById(R.id.releasepcbtn);
        txtthirtyremin = findViewById(R.id.txtthirtyremaining);
        txtbduration = findViewById(R.id.txtbreakdur);
        tenstatusonof = (TextView)
                findViewById(R.id.txt_tenstatus);//ten status on off
        releasebtn = findViewById(R.id.releasebtn);
        txtmake = findViewById(R.id.txt_make);
        txtyear = findViewById(R.id.txt_year);
        btnreleasethirty = findViewById(R.id.releasetthrtybtn);
        gettimezone();
        txttenremaining = findViewById(R.id.txttenremaining);
        txttitle = findViewById(R.id.breaktitle);
        hrtitle = findViewById(R.id.hrsettitle);
        txttenused = findViewById(R.id.txttenused);
        txttenremain = findViewById(R.id.txtten);
        txttitle.startAnimation(animBlink);
        hrtitle.startAnimation(animBlink);
        tenstatusonof.startAnimation(animBlink);
        txtpc.startAnimation(animBlink);
        txtchat = findViewById(R.id.txtchatfont);
        txthrtytatus = findViewById(R.id.txt_thtystatus);
        txthrtytatus.startAnimation(animBlink);
        lin_parent = findViewById(R.id.linparent);
        lin_thirty = findViewById(R.id.linthty);
        imgengine = findViewById(R.id.img_engine);
        imgbattery = findViewById(R.id.img_battery);
        txtresethr = findViewById(R.id.rkk);
        resetbalance = findViewById(R.id.rkkt);
        txtchat.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txt_duration = (TextView) findViewById(R.id.txtduration);
        txt_breakleft = findViewById(R.id.txtbreakleft);
        img_location = findViewById(R.id.my_location);
        img_vehicle = (ImageView) findViewById(R.id.vehicle_report);
        imgservice = findViewById(R.id.img_service);
        linonduty = (LinearLayout) findViewById(R.id.patdetmain3);
        imggraph = findViewById(R.id.img_graph);
        txt_curnetstaus = (TextView) findViewById(R.id.txtresp);
        lindriveduty = (LinearLayout) findViewById(R.id.patdetmain4);
        linsleepduty = (LinearLayout) findViewById(R.id.patdetmain1);
        linoffduty = (LinearLayout) findViewById(R.id.patdetmain2);
        // speedometer = (Speedometer) findViewById(R.id.Speedometer);
        imgback = (ImageView) findViewById(R.id.iv_exit);
        imguploadbill = findViewById(R.id.upload_bill);
        //  txtvimage=findViewById(R.id.txt_img);
        txtonfromtime = (TextView) findViewById(R.id.timer1);
        txtvind = findViewById(R.id.txt_vid);
        txtcity = findViewById(R.id.txtcity);
        //txtspeed=findViewById(R.id.txt_speedo);
        txt_state = (TextView) findViewById(R.id.txtstate);
        txtdrivefromtime = (TextView) findViewById(R.id.timer2);
        txtsleepfromtime = (TextView) findViewById(R.id.timer3);
        txtoffromtime = (TextView) findViewById(R.id.timer4);
        txtvinnumber = (TextView) findViewById(R.id.txt_vin);
        txtlcno = (TextView) findViewById(R.id.txt_lcnum);
        txt_model = (TextView) findViewById(R.id.txt_model);
        txtuser = (TextView) findViewById(R.id.user_profile_named);
        txtondutyleft = (TextView) findViewById(R.id.time_left);
        txtdriveleft = (TextView) findViewById(R.id.dtime_left);
        // txttopdate = (TextView) findViewById(R.id.txttime);
        txttoptime = (TextView) findViewById(R.id.txtctime);
        txtondutytotal = (TextView) findViewById(R.id.totime1);
        txtdrivetotal = (TextView) findViewById(R.id.totime2);
        txtsleeptotal = (TextView) findViewById(R.id.totime3);
        txtoffdutytotal = (TextView) findViewById(R.id.totime4);
        txtontotime = (TextView) findViewById(R.id.timerstop1);
        txtdrivetotime = (TextView) findViewById(R.id.timerstop2);
        txtsleeptotime = (TextView) findViewById(R.id.timerstop3);
        txtoftotime = (TextView) findViewById(R.id.timerstop4);
        txtdriveleft = (TextView) findViewById(R.id.dtime_left);
        imgprofile = findViewById(R.id.user_profile);
        // pref = Preference.getInstance(this);
        pref.putString(Constant.VOICE_OFF, "0");
        pref.putString(Constant.VOICE_ON, "0");
        pref.putString(Constant.VOICE_SLEEP, "0");
        pref.putString(Constant.VOICE_DRIVE, "0");
        dname = pref.getString(Constant.DRIVER_NAME);
        d_photo = pref.getString(Constant.DRIVER_PHOTO);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            olddversion = "" + packageInfo.versionCode;
            olddversionname = "" + packageInfo.versionName;
            //  Log.e("oldversion",""+olddversion);
        } catch (Exception e) {

        }


        try {
            if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS) != null && pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).length() > 0) {

            } else {
                pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");

            }


        } catch (Exception e) {

        }
        if (pref.getString(Constant.BL_SCANNING_FLAG) != null && pref.getString(Constant.BL_SCANNING_FLAG).length() > 0) {
            if (pref.getString(Constant.BLUETOOTH_ADDRESS) != null && pref.getString(Constant.BLUETOOTH_ADDRESS).length() > 0) {

            } else {
                pref.putString(Constant.BL_SCANNING_FLAG, "0");
            }

        } else {

            pref.putString(Constant.BL_SCANNING_FLAG, "0");

        }
        if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
            try {
                boolactice = pref.getBolean(Constant.BLUETOOTH_FLAG);
            } catch (Exception e) {

            }
            if (!boolactice) {
                if (pref.getString(Constant.BL_SCANNING_FLAG).contentEquals("0")) {
                    if (pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {
                        callbluetoothscanning();
                    }
                }
            }
        }
        //Log.e("calling","oncreate");
        String key = pref.getString(Constant.ELD_KEY);
        if (key != null && key.length() > 0 && !key.contentEquals("null")) {
            txtkey.setText("ELD Ser.#:" + key);
        } else {
            txtkey.setText("ELD Ser.#:");
        }

        String mkea = pref.getString(Constant.ELD_MAKE);
        if (mkea != null && mkea.length() > 0 && !mkea.contentEquals("null")) {
            txtmake.setText("Make : " + mkea);
        } else {
            txtmake.setText("Make : ");
        }


        String app_status = pref.getString(Constant.DELETE_APP_STATUS);
//Log.e("app_status","@"+app_status);

        if (app_status != null && app_status.length() > 0 && !app_status.contentEquals("null")) {
            if (app_status.contentEquals("0")) {
                pref.putString(Constant.DELETE_APP_STATUS, "1");
                deleteappfolder();
            }

        } else {
            pref.putString(Constant.DELETE_APP_STATUS, "1");
            deleteappfolder();
        }


        String yearaa = pref.getString(Constant.ELD_YEAR);
        if (yearaa != null && yearaa.length() > 0 && !yearaa.contentEquals("null")) {
            txtyear.setText("Year : " + yearaa);
        } else {
            txtyear.setText("Year : ");
        }

        str_vin = pref.getString(Constant.VIN_NUMBER).trim();
        if (str_vin.contentEquals("Demo") || str_vin.contentEquals("DEMO")) {
            Intent intr = new Intent(context, Vehicle_seect.class);
            intr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intr.putExtra("EXIT", true);
            startActivity(intr);
            finish();

        }


        if (pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) != null && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).length() > 0 && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("no")) {

            pref.putString(Constant.NETWORK_TYPE, Constant.CELLULAR);
            setnetworkicons("WIRELESS");
        } else {
            setnetworkicons("WIRELESS");

        }


        mWherequbeObserver.register(this);

        WherequbeService.getInstance().setReqHandler(BaseRequest.OBD_MEASUREMENT, myEventHandler);


        uiIf.addAction("REFRESH");

        if (pref.getString(Constant.NETWORK_TYPE) != null && pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
            setnetworkicons("WIRELESS");
        } else {
            setnetworkicons("WIRELESS");
        }

        if (lat != null) {

            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);
//            LocationAddress locationAddress = new LocationAddress();
//            locationAddress.getAddressFromLocation(latitude, longitude,
//                    getApplicationContext(), new GeocoderHandler());
            getAddressFromLocation(latitude, longitude);
        }

        str_lcnum = pref.getString(Constant.LICENSE_NUMBER);
        str_remarkstatus = pref.getString(Constant.REMARK_STATUS);
        int_remarkcount = pref.getInt(Constant.REMARK_COUNT);
        String mname = pref.getString(Constant.MODEL_NAME);
        //setdrivealert();
        txt_model.setText("Model : " + mname);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intentuser_profile = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intentuser_profile, 0);
            }
        }
        //txtspeed.setText(""+"Speedo meter : ");
//        speedometer.onSpeedChanged(speedometer.getCurrentSpeed()-speedometer.getCurrentSpeed());

        vpath = pref.getString(Constant.VECHLE_IMAGES);
        vinnumber = pref.getString(Constant.VIN_NUMBER);

        // pref.putString(Constant.LOGIN_CHECK,
        //                "logged_off");
        strlogin = pref.getString(Constant.LOGIN_CHECK);

        if (vpath != null && vpath.length() > 0 && !vpath.contentEquals("null")) {

        } else {
            vpath = "http://eld.e-logbook.info/elog_app/vechleimages/" + vinnumber + ".jpg";
        }
//        Picasso.with(context)
//                .load(vpath + "?.time();")
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .error(R.mipmap.nimg)
//                .into(txtvimage);


        Calendar now = Calendar.getInstance();
        fnam = now.get(Calendar.HOUR_OF_DAY) + ""
                + now.get(Calendar.MINUTE) + "" + now.get(Calendar.SECOND);
        try {
            SimpleDateFormat formatz1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String myDatez1 = formatz1.format(new Date());
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(myDatez1);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);


            StringTokenizer skdte;
            if (myDatez1.contains("-")) {
                skdte = new StringTokenizer(myDatez1, "-");
                String styr = skdte.nextToken();
                String stmnth = skdte.nextToken();
                String stdte = skdte.nextToken();
                txtday.setText("" + goal + "    " + getMonth(Integer.parseInt(stmnth)) + ". " + stdte);
            }


        } catch (Exception e) {

        }
        getversionname();
        // remarkback("ON_DUTY");
        String vno = pref.getString(Constant.VID_NUMBER);
        if (vno != null && vno.length() > 0 && !vno.contentEquals("null")) {
            if (vno.contains(":")) {
                StringTokenizer sk = new StringTokenizer(vno, ":");
                String a = sk.nextToken();
                if (sk.hasMoreTokens()) {
                    String b = sk.nextToken();
                    txtvind.setText("Vehc#:" + b);
                } else {
                    txtvind.setText("Vehc#:");
                }
            } else {
                txtvind.setText("Vehc#:" + vno);
            }
        } else {
            txtvind.setText("Vehc# :");
        }
        String states = "";
        try {
            states = pref.getString(Constant.STATE_FIELD);
            txtcstate.setVisibility(View.GONE);
            txtrules.setText("Home state rules applied");
            // ttval.setText("Home state rules applied");
            if (pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) != null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
                txtcstate.setVisibility(View.VISIBLE);
                txtcstate.setText("Current state : " + strstate);
                txtrules.setText("Federal rules applied");
            }
        } catch (Exception e) {

        }
        if (states == null || states.contentEquals("null")) {
            states = "";
        }

        txt_state.setText("Home State :" + states);
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

            //  imgok.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_button) );
            imgok.setImageResource(R.drawable.prl);
            txtprcstatus.setText("OFF");//before ON
            txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
            txtprcstatus.startAnimation(animBlink);


        } else {
            imgok.setImageResource(R.drawable.prl);
            txtprcstatus.setText("OFF");//before ON
            txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
            txtprcstatus.startAnimation(animBlink);
            // imgok.setBackground( getResources().getDrawable(R.drawable.bg_button) );
        }
        if (str_vin != null && str_vin.length() > 0 && !str_vin.contentEquals("null")) {
            txtvinnumber.setText("VIN# :" + str_vin);
        } else {

            txtvinnumber.setText("VIN# :");
        }

        driverid = pref.getString(Constant.DRIVER_ID);


        if (d_photo != null && d_photo.length() > 0) {
            Picasso.with(context)
                    .load(d_photo + "?.time();")
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.userpic)
                    .into(imgprofile);
        }
        if (str_lcnum != null && str_lcnum.length() > 0 && !str_lcnum.contentEquals("null")) {
            txtlcno.setText("Lic# : " + str_lcnum);
        } else {
            txtlcno.setText("Lic# : ");
        }
        registerGCM();
        starray = new ArrayList<>();
        CheckGpsStatus();
        if (!GpsStatus) {
            try {
                getMyLocation();
            } catch (Exception e) {

            }
        }


        if (str_vin.contentEquals("Demo")) {

        } else if (strlogin.contentEquals("logged_off")) {
            pref.putString(Constant.LOGIN_CHECK,
                    "logged_off");
            pref.putString(Constant.ELOG_NUMBERSS,
                    "");
            cancelprogresssdialog();
            //pref.getString(Constant.ELOG_NUMBERSS);
            pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
            pref.putString(Constant.DRIVE_NOTIFICATON, "0");
            cancelnotification();
            Intent mIntent12 = new Intent(Home_activity_bluetooth.this,
                    Loginactivitynew.class);
            startActivity(mIntent12);
            finish();

        } else {


            onNewIntent(getIntent());
        }

        imgtakenetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) != null && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).length() > 0 && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("no")) {

                } else {

                    if (intschedule == STATUS_DRIVING && pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS) != null && pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {
                        callwarningnetwork();
                    } else {
                        callnetworktype();
                    }
                }
            }
        });


        linloaddetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loddetailspopup();
                getlogvalues();
            }
        });
        floatupdateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();

                try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        tmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.C_LATITUDE, "" + lat);
                pref.putString(Constant.C_LONGITUDE, "" + lon);
                Intent ink = new Intent(context, Message_view.class);
                startActivity(ink);


            }
        });
        imgifta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.C_LATITUDE, "" + lat);
                pref.putString(Constant.C_LONGITUDE, "" + lon);
                Intent ink = new Intent(context, Iftahome_activity.class);
                startActivity(ink);
            }
        });
        imgtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk = 0;
                pref.putString(Constant.C_LATITUDE, "" + lat);
                pref.putString(Constant.C_LONGITUDE, "" + lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Trip_details.class);
                startActivity(mIntent);
                //finish();
            }
        });
        imgnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk = 0;
                pref.putString(Constant.C_LATITUDE, "" + lat);
                pref.putString(Constant.C_LONGITUDE, "" + lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Notiication_home.class);
                startActivity(mIntent);
                // finish();
            }
        });
        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = "+1.855.922.9700";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });
        imgbreaktake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk = 0;
                takebreakdialog();
            }
        });
        imgbreakrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk = 0;
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Breakreport_home.class);
                startActivity(mIntent);
                //finish();
            }
        });
        MovableFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineCheck.isOnline(context)) {
                    // getrefreshvalues(str_vin);
                    if (str_vin.contentEquals("Demo")) {

                    } else {
                        mdk = 0;
                        gettodayvalues(str_vin, "20");

                    }
                }
            }
        });


        getime();
        upLoadServerUri = "http://eld.e-logbook.info/elog_app/upload_to_server.php";
        // upLoadServerUri = "http://e-logbook.info/elog_app/upload_voice.php";


        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();


        try {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                requestGPSSettings();

            }
        } catch (Exception e) {

        }

        dialog = new Dialog(context, R.style.DialogTheme);

        try {
            Calendar calander = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

            String time = simpleDateFormat.format(calander.getTime());

            txttoptime.setText("" + time);
        } catch (Exception e) {
            e.printStackTrace();
        }


        driverid = pref.getString(Constant.DRIVER_ID);
        txtuser.setText("" + dname);
        mde = pref.getString(Constant.ELOG_NUMBERSS);
        T = new Timer();

        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTimeStringsTimerWorker();
                    }
                });
            }
        }, 16000, 16000);//4000 18

        if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) !=null &&
                pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes"))
        {
        btimer = new Timer();
        btimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        str_datetime = formattime.format(new Date());
                        SimpleDateFormat formattimekkk = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String sk= formattimekkk.format(new Date());
                        try {
                            if (str_obdtime != null && str_obdtime.length() > 0) {
                                long obd = splittime(str_obdtime);
                                long tbd = splittime(str_datetime);
                                long resbd = tbd - obd;
                                //Log.e("obdzz","@"+obd);
                                //Log.e("tbdzz","@"+tbd);
                                //Log.e("resbdzzz","@"+resbd);
                                // trsc.setText("D"+str_datetime);
                                if (resbd > 40) {// if obd response not received more than 60 seconds then call scanning function
//                                    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                                    {
//                                        trackparams.put("_resbd"+sk," > 40");
//                                    }


                                    callautoconnect();
                                } else {
                                    //if bluetooth disconnected -> call scanning
                                    if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
                                        callautoconnect();
                                    }
                                }
                            } else {
                                //new changes
//                                if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                        && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                                {
//                                    trackparams.put("_elseresbd"+sk," > 40");
//                                }

                                SimpleDateFormat formattimezz = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                String str_datetimezz = formattimezz.format(new Date());
                                try {
                                     //Log.e("str_obdtimexx","@"+str_obdtimexx);
                                     //Log.e("str_datetimezz","@"+str_datetimezz);
                                    if (str_obdtimexx != null && str_obdtimexx.length() > 0) {
                                        long obd = splittime(str_obdtimexx);
                                        long tbd = splittime(str_datetimezz);
                                        long resbd = tbd - obd;
                                          //Log.e("obdzz","@"+obd);
                                        //Log.e("tbdzz","@"+tbd);
                                          //Log.e("resbd","@"+resbd);

                                        // trsc.setText("D"+str_datetime);
                                        if (resbd > 40) {// if obd response not received more than 60 seconds then call scanning function
                                            if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
                                                callautoconnect();
                                                try {
                                                    SimpleDateFormat formattimex = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                                    str_obdtimexx = formattimex.format(new Date());
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
//                                        else {
//                                            //if bluetooth disconnected -> call scanning
//                                            if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("0")) {
//                                                callautoconnect();
//                                            }
//                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }
        }, 42000, 42000);//4000 18

    }


        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagetype = 0;
                takeipicture();


            }
        });

        btnpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineCheck.isOnline(context)) {
                    String pcstatus = pref.getString(Constant.PC_STATUS).trim();
                    mdk=0;
                    if (pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_DISABLE);
                        gettodaypcenable(vinnumber);
                        callstatusenable();
                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);
                        final int sdk = Build.VERSION.SDK_INT;
                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                            imgok.setImageResource(R.drawable.prl);

                        } else {
                            imgok.setImageResource(R.drawable.prl);
                        }
                    }
                    try {
                        Uri alarmSound;

                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getApplicationContext().getPackageName() + "/raw/pcoff");

                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                        r.play();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Network not available", Toast.LENGTH_SHORT);
                }
            }
        });
        imguploadbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imagetype = 1;
//                mdk=0;
//                takeipicture();

                pref.putString(Constant.C_LATITUDE, "" + lat);
                pref.putString(Constant.C_LONGITUDE, "" + lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Upload_fuelbill.class);
                startActivity(mIntent);
            }
        });


        imgengine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Engine_fault_code.class);
                startActivity(mIntent);
               // finish();
            }
        });
        imgbattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk=0;
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Battery_health.class);
                startActivity(mIntent);
               // finish();
            }
        });
        imgfuellevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk=0;
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Fuel_level.class);
                startActivity(mIntent);
               // finish();
            }
        });
        linten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineCheck.isOnline(context)) {
                    if (pppcstatus == 4) {
                        callenabletenhour();
                    } else if (pppcstatus == 0) {
                        try {
                            Uri alarmSound;

                            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                    + "://" + getApplicationContext().getPackageName() + "/raw/tenon");

                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                            r.play();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mdk=0;
                        savetenhours(commonUtil.OFF_DUTY, intschedule, "4");
                    }
                }
            }
        });
        btnreleaseten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (OnlineCheck.isOnline(context)) {
                    mdk=0;
                    if (pppcstatus == 4) {
                        callenabletenhour();
                    } else if (pppcstatus == 0) {
                        savetenhours(commonUtil.OFF_DUTY, intschedule, "4");
                    }
                }
            }
        });
        btnreleasethirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (OnlineCheck.isOnline(context)) {
                    mdk=0;
                    if (pppcstatus == 2) {
                        callenablethirtyfourhour();
                    } else if (pppcstatus == 0) {

                        gettodaysavethirtyreset(commonUtil.OFF_DUTY, intschedule, "2");
                    }
                }

            }
        });
        lin_thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineCheck.isOnline(context)) {
                    mdk=0;
                    if (pppcstatus == 2) {
                        callenablethirtyfourhour();
                    } else if (pppcstatus == 0) {
                        gettodaysavethirtyreset(commonUtil.OFF_DUTY, intschedule, "2");
                    }
                }

            }
        });
        releasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineCheck.isOnline(context)) {
                   // mdk=0;
                  // newbreakrelease();
                    stopbreakdialog();
                }
            }
        });
        imggraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk=0;
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
//                Intent mIntent = new Intent(
//                        Home_activity_bluetooth.this,
//                        Drive_summary_new.class);
//                startActivity(mIntent);
                //finish();
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Driversummary_web.class);
                startActivity(mIntent);

            }
        });



        imgservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        Serviceticket_home.class);
                startActivity(mIntent);
               // finish();
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    String msg = intent.getStringExtra("message");
                    if (msg != null && msg.length() > 0 && !msg.contentEquals("null")) {
                        if(msg.contentEquals("bluetooth_status"))
                        {
                            String speed = intent.getStringExtra("speed");
                           com.isoft.trucksoft_elog.Isoft_activity.GeoData gk=new com.isoft.trucksoft_elog.Isoft_activity.GeoData();
                           gk.setVehicleSpeed(Double.parseDouble(speed));

                         //  for(int k=0;k<20;k++) {
                               //Log.e("speedk","........."+k);
                               updateOBDDatastring(gk);
                         //  }

                        }else if(msg.contentEquals("Check_BT"))
                        {
                            //Log.e("msg","........."+msg);
                            try{
                                String cstatus=pref.getString(Constant.CURRENT_STATUS);
                                //Log.e("cstatus","........."+cstatus);
                                if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null")) {
                                    if(cstatus.contentEquals(commonUtil.DRIVING))
                                    { String bluetime=pref.getString(Constant.BLUETOOTH_RESPONSE_TIME);
                                        String prevstatus = pref.getString(Constant.PREVIOUS_CURRENT_STATUS);
                                        if(prevstatus !=null && prevstatus.length()>0) {

                                        }else{
                                            prevstatus=commonUtil.OFF_DUTY;
                                        }
                                        if(bluetime !=null && bluetime.length()>0) {

                                            SimpleDateFormat formattimed = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                            String time = formattimed.format(new Date());
                                            long bltime=splittimeseconds(bluetime);
                                            long ctime = splittimeseconds(time);

                                            long difftime=ctime-bltime;
                                            if(difftime>=15)
                                            {

                                                if (OnlineCheck.isOnline(context)) {
                                                    gettodaysavevaluesbluetooth(prevstatus, intschedule,"", "","","","");
                                                }
                                            }
                                        }else{

                                            if (OnlineCheck.isOnline(context)) {
                                                gettodaysavevaluesbluetooth(prevstatus, intschedule,"", "","","","");
                                            }
                                        }

savebluetoothstatus("0","0");
                                    }
                                }
                            }catch (Exception e)
                            {

                            }
                        }

                        else if(msg.contentEquals("custom_alert"))
                        {
                            String msgvl = intent.getStringExtra("msg");
                      //  Log.e("msg","........."+msgvl);
customalert(msgvl);

                        }






                       else if (msg.contentEquals("send_status")) {

                            //Log.e("msgk",""+msg);
                            String type = intent.getStringExtra("statustype");
                            // Log.e("type",""+type);
                            if (type.contentEquals("DRIVING")) {
                                txt_curnetstaus.setText("STATUS :  DRIVING.");
                            }


                            stroldlastid = pref.getString(Constant.STATUS_LASTID_OLD);
                            strnewlastid = pref.getString(Constant.STATUS_LASTID_NEW);
                            // Log.e("stroldlastid",""+stroldlastid);
                            //Log.e("strnewlastid",""+strnewlastid);
                            if (strnewlastid != null && strnewlastid.length() > 0 && !strnewlastid.contentEquals("null")) {
                                if (stroldlastid != null && stroldlastid.length() > 0 && !stroldlastid.contentEquals("null")) {
                                    if (stroldlastid.contentEquals(strnewlastid)) {

                                    } else {
                                        mdk=0;
                                        pref.putString(Constant.STATUS_LASTID_OLD, strnewlastid);
                                        gettodaynotif();
                                    }
                                } else {
                                    mdk=0;
                                    pref.putString(Constant.STATUS_LASTID_OLD, strnewlastid);
                                    gettodaynotif();
                                }

                            } else {
                                // currentstatus(vinnumber);
                                //Log.e("lastid","noval");
                                mdk=0;
                                gettodaynotif();
                            }
                        } else if (msg.contentEquals("interval_update")) {
                            String type = intent.getStringExtra("type");
                            try {
                                if (type != null && type.length() > 0) {
                                    int vcval=20;
                                    vcval=Integer.parseInt(type);

                                    pref.putString(Constant.LOCATION_UPDATE_STATUS, "" +vcval);
                                    if(vcval>0)
                                    {
                                        repinterval=vcval;
                                    }
                                    if(pref.getString(Constant.LOCATION_UPDATE_DATE)!=null && pref.getString(Constant.LOCATION_UPDATE_DATE).length()>0) {
                                    }else{
                                        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        String ctime = formatdatetime.format(new Date());
                                        pref.putString(Constant.LOCATION_UPDATE_DATE, "" + ctime);
                                    }
                                }
                            }catch (Exception e)
                            {

                            }
                        }



                        else if (msg.contentEquals("send_notification")) {

                        } else if (msg.contentEquals("send_location")) {
                            String type = intent.getStringExtra("statustype");
                            if (type.contentEquals("DRIVING") || type.contentEquals("NOT DRIVING")) {
                                if (type.contentEquals("DRIVING")) {
                                    txt_curnetstaus.setText(" STATUS : DRIVING.");
                                }


                                // gettodaynotif();

                            }
                        }else if (msg.contentEquals("app_logout")) {
                            //Log.e("callv","#"+msg);
                            applogout();
                        }

                       else if (msg.contentEquals("send_notification_distance")) {
                            //dd
                            String mil = "0";
                            if (intent.hasExtra("estatus")) {
                                mil = intent.getStringExtra("estatus");
                            }
                            setdistancealert(mil);
                        } else if (msg.contentEquals("send_notification_onduty")) {
                            //dd
                            String mil = "0";
                            if (intent.hasExtra("estatus")) {
                                mil = intent.getStringExtra("estatus");
                            }
                            setondutyalert();
                        } else if (msg.contentEquals("send_notification_drive")) {


                            setdrivealert();
                        }else if (msg.contentEquals("low_voltage")) {

setlowvoltage();
                           // setdrivealert();
                        }
                        else if (msg.contentEquals("state_changed")) {
                            String stdrv = intent.getStringExtra("statedrive");
                            String stond = intent.getStringExtra("stateonduty");
statedrive=stdrv;
stateonduty=stond;
                            txtonallowed.setText(stateonduty+" Hr");
                            txtdriveallowed.setText(statedrive+" Hr");
                          //  getrefreshvalues(str_vin);
                            mdk=0;
                            gettodayvalues(str_vin,"20");
                           federallawalert();
                        }

                        else if (msg.contentEquals("unplugged")) {
                            callunpluggeddialog();
                        } else if (msg.contentEquals("send_update_version")) {
                            if (intent.hasExtra("estatus")) {
                                String msgd = intent.getStringExtra("estatus");
                                callappupdate(msgd);
                            } else {
                                callversionupdate();
                            }
                        } else if (msg.contentEquals("location")) {
                            String status = "";
                            if (intent.hasExtra("estatus")) {
                                status = intent.getStringExtra("estatus");
                                //calllocationupdate(status);
                            } else {
                                status = "";
                            }

                        }else if(msg.contentEquals("IGN_OFF"))
                        {
                          //  Log.e("calling","engine");
callengineoff();

                        }
                        else if(msg.contentEquals("geo_fence"))
                        {

                            String msgk = intent.getStringExtra("msgval");
                            String typed = intent.getStringExtra("typed");
                            geofencelalert(typed,msgk);

                        }
                    }
                }
            }
        };


        txtchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent intr = new Intent(context, Dispatch_chathome.class);
                startActivity(intr);
                //finish();
            }
        });

        lindriveduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdk=0;
                if (pref.getString(Constant.VIN_NUMBER).contentEquals("NO VIN")) {
                    onClickStatusDriving();
                } else {
                    calldrivebuttonalert();
                }
            }
        });
        img_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        Intent intr=new Intent(context,InspectionReportHome.class);
//        startActivity(intr);
                mdk=0;
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                // Intent intent = new Intent(context, InspectionReportHome.class);
                Intent intent = new Intent(context, Driverchecklist_home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pref.putString(Constant.VIEW_BACK, "failed");
                intent.putExtra("EXIT", true);
                startActivity(intent);
               // finish();
            }
        });

        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent intr = new Intent(context, Show_mylocation.class);
                startActivity(intr);
               // finish();
            }
        });


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  onClickBackImage();

//stopService();
setlogoutalert();
            }
        });

        linonduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("clcik","onduty");
                if (OnlineCheck.isOnline(context)) {
                    //Log.e("callong","onduty success");
                   // onClickStatusOnDuty();block 22-05-2020
                    tspeedcountown.setText("Timer : ");
                    if (bluetoothtimer != null) {
                        try {
                            isRunning = false;
                            bluetoothtimer.cancel();

                        } catch (Exception e) {

                        }
                    }
                    pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"off");
                    onClickStatusOnDutynew();
                    pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.ON_DUTY);
                    pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.ON_DUTY);
                }else{
                    //Log.e("clcik","no network");
                }
            }
        });



        linsleepduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("clcik","sleepppp");
                if (OnlineCheck.isOnline(context)) {
                    tspeedcountown.setText("Timer : ");
                    if (bluetoothtimer != null) {
                        try {
                            isRunning = false;
                            bluetoothtimer.cancel();

                        } catch (Exception e) {

                        }
                    }
                    pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"off");
                    onClickStatusSleeper();
                    pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.SLEEP);
                    pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.SLEEP);
                }
            }
        });
        imgexception.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calleception();
            }
        });
        linoffduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("clcik","offduttyyy");
                if (OnlineCheck.isOnline(context)) {
                    tspeedcountown.setText("Timer : ");
                    if (bluetoothtimer != null) {
                        try {
                            isRunning = false;
                            bluetoothtimer.cancel();

                        } catch (Exception e) {

                        }
                    }
                    pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"off");
                    pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.OFF_DUTY);
                    pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.OFF_DUTY);
                    onClickStatusOffDuty();
                }
            }
        });

        imgok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OnlineCheck.isOnline(context)) {
                    String pcstatus = pref.getString(Constant.PC_STATUS).trim();
                    if (pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_DISABLE);
                        gettodaypcenable(vinnumber);
                        callstatusenable();
                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);
                        final int sdk = Build.VERSION.SDK_INT;
                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                            imgok.setImageResource(R.drawable.prl);

                        } else {
                            imgok.setImageResource(R.drawable.prl);
                        }

                        try {
                            Uri alarmSound;

                            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                    + "://" + getApplicationContext().getPackageName() + "/raw/pcoff");

                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                            r.play();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();
                        onClickStatuspcOffDuty("1");
                        txtprcstatus.setText("ON");//after OFF
                        txtprcstatus.setTextColor(Color.parseColor("#072607"));
                        txtprcstatus.startAnimation(animBlink);
                        final int sdk = Build.VERSION.SDK_INT;
                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                            imgok.setImageResource(R.drawable.prlactive);
                            // imgok.setBackgroundDrawable( getResources().getDrawable(R.drawable.pcdisable) );

                        } else {

                            //imgok.setBackground( getResources().getDrawable(R.drawable.pcdisable) );
                            imgok.setImageResource(R.drawable.prlactive);
                        }
                        //btn_pc.setBackgroundColor(getResources().getColor(R.color.light_gray));
                        try {
                            Uri alarmSound;

                            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                    + "://" + getApplicationContext().getPackageName() + "/raw/pcon");

                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                            r.play();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "Network not available", Toast.LENGTH_SHORT);
                }
            }
        });


    }

    ////////////////////////////////
    // Click handlers
    ////////////////////////////////
    public void onClickStatusOnDuty() {

        // CheckGpsStatus() ;
        //  if(GpsStatus && lat !=null && lat.length()>0) {
        //Log.e("intschedule","@"+intschedule);
        //Log.e("STATUS_ON_DUTY","@"+STATUS_ON_DUTY);
        if (intschedule != STATUS_ON_DUTY) {
            // setcurrentstatus(commonUtil.ON_DUTY,intschedule,"");
            if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
            {
                gettodaysavevaluesbluetooth(commonUtil.ON_DUTY, intschedule,"", "","","","");
            }else {
                gettodaysavevalues(commonUtil.ON_DUTY, intschedule, "");
            }

        }
    }


    // Click handlers
    ////////////////////////////////
    public void onClickStatusOnDutynew() {

        // CheckGpsStatus() ;
        //  if(GpsStatus && lat !=null && lat.length()>0) {
        //Log.e("intschedule","@"+intschedule);
        //Log.e("STATUS_ON_DUTY","@"+STATUS_ON_DUTY);
        if (intschedule != STATUS_ON_DUTY) {

                gettodaysavevalues(commonUtil.ON_DUTY, intschedule, "");


        }
    }






    public void onClickStatusOnDutyBluetooth(String rmark,String trckval,String latitude,String longitude) {

            if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
            {
                gettodaysavevaluesbluetooth(commonUtil.ON_DUTY, intschedule, "",rmark,""+trckval, latitude, longitude);
            }else {
                gettodaysavevalues(commonUtil.ON_DUTY, intschedule, "");
            }


    }

    public void onClickStatusDriving() {
        // CheckGpsStatus();
        //  if(GpsStatus && lat !=null && lat.length()>0)
        // {
        if (intschedule != STATUS_DRIVING) {

            // setcurrentstatus(commonUtil.DRIVING,intschedule,"");

            if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
            {
                gettodaysavevaluesbluetooth(commonUtil.DRIVING, intschedule, "","","dr", "", "");
            }else{
                gettodaysavevalues(commonUtil.DRIVING, intschedule, "");
            }

        }

    }
    public void onClickStatusDrivingbluetooth(String latitude,String longitude) {


            if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
            {

                gettodaysavevaluesbluetooth(commonUtil.DRIVING, intschedule, "","_","drvcr1", latitude, longitude);
            }else{

                gettodaysavevalues(commonUtil.DRIVING, intschedule, "");
            }



    }
    public void onClickStatusSleeper() {
//        CheckGpsStatus();
//        if(GpsStatus && lat !=null && lat.length()>0)
//        {
        if (intschedule != STATUS_SLEEPER) {
            //setcurrentstatus(commonUtil.SLEEP,intschedule,"");
            gettodaysavevalues(commonUtil.SLEEP, intschedule, "");

        }
//        }
//        else
//        {
//            Toast.makeText(context,"Please activate location first", Toast.LENGTH_LONG).show();
//        }
    }
    public void onClickStatusSleeperbluetooth(String rmark, String trckval, String latitude, String longitude) {
        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
            gettodaysavevaluesbluetooth(commonUtil.SLEEP, intschedule, "",rmark,""+trckval,latitude,longitude);
        }else{
            //setcurrentstatus(commonUtil.SLEEP,intschedule,"");
            gettodaysavevalues(commonUtil.SLEEP, intschedule, "");

        }

    }

    //    public void onClickStatusOffDuty(String status) {
//        //CheckGpsStatus();
//        // if(GpsStatus && lat !=null && lat.length()>0)
//        //{
//
//
//        if (intschedule != STATUS_OFF_DUTY) {
//
//           // setcurrentstatus(commonUtil.OFF_DUTY, intschedule,status);
//            gettodaysavevalues(commonUtil.OFF_DUTY, intschedule,status);
//        }
//    }
    public void onClickStatuspcOffDuty(String status) {

        if (pppcstatus != 1) {

            // setcurrentstatus(commonUtil.OFF_DUTY, intschedule,status);
            gettodaysavevalues(commonUtil.OFF_DUTY, intschedule, status);
        }

    }


    public void onClickStatusOffDuty() {
        if (intschedule != STATUS_OFF_DUTY) {

            // setcurrentstatus(commonUtil.OFF_DUTY,intschedule,"");
            gettodaysavevalues(commonUtil.OFF_DUTY, intschedule, "");
        }
    }
    public void onClickStatusOffDutyblueetooth( String rmark,String trckval,String latitude,String longitude) {
        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
        {
            gettodaysavevaluesbluetooth(commonUtil.OFF_DUTY, intschedule, "",rmark,""+trckval, latitude, longitude);
        }else {
            gettodaysavevalues(commonUtil.OFF_DUTY, intschedule, "");
        }

    }


    public void  updateTimeStringsTimerWorker() // This is called using runOnUiThread every 4s to update status time strings
    {
        if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active"))
        {
            statedrive= pref.getString(Constant.FEDERAL_DRIVE_HOURS);
            stateonduty=pref.getString(Constant.FEDERAL_ONDUTY_HOURS);
            txtonallowed.setText(stateonduty+" Hr");
            txtdriveallowed.setText(statedrive+" Hr");
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
        //Log.e("intstate",""+intstate);
       // Log.e("21332342",""+stateonduty);
        SimpleDateFormat formatdatetimegg = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat fzz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        datetime = formatdatetimegg.format(new Date());
        String time = formattime.format(new Date());
        //Log.e("time",""+time);
        // strtime = formatsec.format(new Date());
        String dfg = formatsec.format(new Date());

        String dte = fzz.format(new Date());

        long s1 = splittime(dfg);
        // Log.e("strtime","@"+strtime);
        if (rk == 1) {
            long s0 = splittime(strtime);
            rk = 0;
            sktime = s0 - s1;
            // Log.e("sktime", "@" + sktime);
        }
        String s3 = printsum(s1 + sktime);
        strdtime = printsum(s1 + sktime);
        strdatetime = dte + " " + strdtime;
        //   txttoptime.setText(s3);
        try {
            Calendar calander = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

            String timed = simpleDateFormat.format(calander.getTime());

            txttoptime.setText("" + timed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringTokenizer str = new StringTokenizer(time, ":");
        String hr = str.nextToken();
        String min = str.nextToken();
        String scx = str.nextToken();

        if ((hr.contentEquals("24") || hr.contentEquals("00")) && min.contentEquals("00") && (scx.contentEquals("00") || scx.contentEquals("01") || scx.contentEquals("02") || scx.contentEquals("03") || scx.contentEquals("04") || scx.contentEquals("05") || scx.contentEquals("06"))) {
            T.cancel();
            pref.putString(Constant.ALERT_SYNC_TIME, "00:00");
            Intent mIntent = new Intent(Home_activity_bluetooth.this, Home_activity_bluetooth.class);
            startActivity(mIntent);
            finish();
        } else if ((hr.contentEquals("23") && min.contentEquals("59"))) {
            T.cancel();
            pref.putString(Constant.ALERT_SYNC_TIME, "00:00");
            Intent mIntent = new Intent(Home_activity_bluetooth.this, Home_activity_bluetooth.class);
            startActivity(mIntent);
            finish();
        }


        if (intschedule == STATUS_ON_DUTY) {
            // Log.e("stontime","#"+stontime);
            // Log.e("strdatetime","#"+strdatetime);
            if (stontime != null && stontime.length() > 0 && !stontime.contentEquals("null")) {

            } else {
                stontime = dte + " " + "00:01";
            }
            // Log.e("stontime",""+stontime);
            //Log.e("strdatetime",""+strdatetime);
            String a = printDifference(stontime, strdatetime);
            long ontime = splittime(a);
            pref.putString(Constant.CURRENT_SCHEDULE_TOTAL, commonUtil.ON_DUTY + ">>" + a);
            ontime += strlontime;
            ston += strlontime;
            String skon = "" + printsum(ontime);
            //Log.e("skon",""+skon);
            if (skon.contains("-")) {
                String sk1 = "" + printsum(strlontime);
                txtondutytotal.setText(sk1);
            } else {
                txtondutytotal.setText(skon);
            }//gg
            long optime = splittime(stateonduty);

            stdr = strldrivetime;
            // Log.e("drive",""+stdr);
            // Log.e("ontime",""+ontime);
            long opnewtime = optime - ontime - stdr;
            String opk = printsum(opnewtime);
           // Log.e("opkopk",""+opk);
            if (opk.contains("-")) {
                txtondutyleft.setText("00:00");
            } else {
                txtondutyleft.setText("" + opk + "" + " Hr");
            }
        } else if (intschedule == STATUS_DRIVING) {
            // Log.e("stdrivetimeqq","$"+stdrivetime);
            //  Log.e("strdatetimeqq","$"+strdatetime);
            if (stdrivetime != null && stdrivetime.length() > 0 && !stdrivetime.contentEquals("null")) {

            } else {
                stdrivetime = dte + " " + "00:01";
            }
            String a = printDifference(stdrivetime, strdatetime);
            stdr = 00;
            long ontime = splittime(a);
            stdr = ontime;
            pref.putString(Constant.CURRENT_SCHEDULE_TOTAL, commonUtil.DRIVING + ">>" + a);
            ontime += strldrivetime;
            stdr += strldrivetime;
            String skon = "" + printsum(ontime);
            if (skon.contains("-")) {
                String sk2 = "" + printsum(strldrivetime);
                txtdrivetotal.setText(sk2);
            } else {
                txtdrivetotal.setText(skon);
            }
           // Log.e("statedrive","@"+statedrive);
            long optime = splittime(statedrive);
            long opnewtime = optime - ontime;
            String opk = printsum(opnewtime);

            if (opk.contains("-")) {
                txtdriveleft.setText("00:00");
            } else {
                txtdriveleft.setText("" + opk + "" + " Hr");
            }

            //ggg
            long optime1 = splittime(stateonduty);
          //  long opnewtime1 = optime1 - ontime - stdr; bug fixing
            long opnewtime1 = optime1 - ontime - strlontime ;
            // Log.e("drive1",""+stdr);
            //Log.e("ontime1",""+ontime);


            String opk1 = printsum(opnewtime1);
            //Log.e("opk1opk1",""+opk1);
            if (opk1.contains("-")) {
                txtondutyleft.setText("00:00");
            } else {
                txtondutyleft.setText("" + opk1 + "" + " Hr");
            }



            //new for onduty calculation nk
            if(strlontime>0 || strldrivetime>0)
            {
                try {
                    String cura = printDifference(respdatetime, strdatetime);
                    long cttime = splittime(cura);
                    //Log.e("cttime", "@" + cttime);
                    long newontime = 0;
                    newontime = strlontime;
                    newontime += cttime;
                    String stronz = "" + printsum(newontime);
                    txtondutytotal.setText("" + stronz);
                }catch (Exception e)
                {

                }
            }




        } else if (intschedule == STATUS_SLEEPER) {
            //Log.e("stsleeptime","$"+stsleeptime);
            // Log.e("strdatetimeqq","$"+strdatetime);

            if (stsleeptime != null && stsleeptime.length() > 0 && !stsleeptime.contentEquals("null")) {

            } else {
                stsleeptime = dte + " " + "00:01";
            }
            String a = printDifference(stsleeptime, strdatetime);
            long ontime = splittime(a);
            pref.putString(Constant.CURRENT_SCHEDULE_TOTAL, commonUtil.SLEEP + ">>" + a);
            ontime += strlsleptime;
            String skon = "" + printsum(ontime);
            if (skon.contains("-")) {
                String sk3 = "" + printsum(strlsleptime);
                txtsleeptotal.setText(sk3);
            } else {
                txtsleeptotal.setText(skon);
            }



            //new for onduty calculation nk
            if(strlontime>0 || strldrivetime>0)
            {
                try {
                    String cura = printDifference(respdatetime, strdatetime);
                    long cttime = splittime(cura);
                    //Log.e("cttime", "@" + cttime);
                    long newontime = 0;
                    newontime = strlontime;
                    newontime += cttime;
                    String stronz = "" + printsum(newontime);
                    txtondutytotal.setText("" + stronz);


                    long optimezzz = splittime(stateonduty);
                    long opnewtimezzz = optimezzz - strlontime - strldrivetime - cttime;
                    String opkzzz = printsum(opnewtimezzz);
                    //Log.e("opkopkopk","#"+opk);
                    if (opkzzz.contains("-")) {
                        txtondutyleft.setText("00:00");
                    } else {
                        txtondutyleft.setText("" + opkzzz + "" + " Hr");
                    }
                }catch (Exception e)
                {

                }
            }
        } else if (intschedule == STATUS_OFF_DUTY) {//16
            // Log.e("pppcstatus",""+pppcstatus);
            if (pppcstatus == 2) {

                rset += 16;
//                if (rset > 86400) {
//                    txtresethr.setText("" + printthirtysum(rset));
//                } else {
                    txtresethr.setText("" + printsum(rset));
//                }

                long currntime = splittime("34:00:00");
                long lres = currntime - rset;//ll
                //86400 24 hr

                if (lres < 0) {
                    getenablesavethirtyreset(commonUtil.OFF_DUTY, intschedule, "0");
                    lres = 0;
                }

//                if (lres > 86400) {
//                    resetbalance.setText("" + printthirtysum(lres));
//                    txtthirtyremin.setText("" + printthirtysum(lres));
//                } else {
                    resetbalance.setText("" + printsum(lres));
                    txtthirtyremin.setText("" + printsum(lres));
//                }
                txttenremain.setText("00:00 Hr");
            } else if (pppcstatus == 4) {
                resetbalance.setText("00:00 Hr");


                tenrset += 16;
//                if (tenrset > 36000) {
//                    txttenused.setText("" + printthirtysum(tenrset));
//
//                } else {
                    txttenused.setText("" + printsum(tenrset));
//                }

                long currntime = splittime("10:00:00");
                long lres = currntime - tenrset;//ll
                //Log.e("lres",""+lres);
                //86400 24 hr
                if (lres < 0) {
                    releasetenhour(commonUtil.OFF_DUTY, intschedule, "0");
                    lres = 0;
                }

//                if (lres > 36000) {
//                    txttenremain.setText("" + printthirtysum(lres));
//                    txttenremaining.setText("" + printthirtysum(lres));
//                } else {
                    txttenremain.setText("" + printsum(lres));
                    txttenremaining.setText("" + printsum(lres));
//                }


            } else {
                resetbalance.setText("00:00 Hr");
                txttenremain.setText("00:00 Hr");
            }
            if (stofftime != null && stofftime.length() > 0 && !stofftime.contentEquals("null")) {

            } else {
                stofftime = dte + " " + "00:01";
            }
            // Log.e("stofftime",""+stofftime);
            // Log.e("strdatetime",""+strdatetime);
            String a = printDifference(stofftime, strdatetime);
            pref.putString(Constant.CURRENT_SCHEDULE_TOTAL, commonUtil.OFF_DUTY + ">>" + a);
            long ontime = splittime(a);
            ontime += strlofftime;
            String skon = "" + printsum(ontime);
            if (skon.contains("-")) {
                String sk4 = "" + printsum(strlofftime);
                txtoffdutytotal.setText(sk4);
            } else {
                txtoffdutytotal.setText(skon);
            }

            //new for onduty calculation nk
            if(strlontime>0 || strldrivetime>0)
            {
                try {
                    String cura = printDifference(respdatetime, strdatetime);
                    long cttime = splittime(cura);
                    //Log.e("cttime", "@" + cttime);
                    long newontime = 0;
                    newontime = strlontime;
                    newontime += cttime;
                    String stronz = "" + printsum(newontime);
                    txtondutytotal.setText("" + stronz);


                    long optimezzz = splittime(stateonduty);
                    long opnewtimezzz = optimezzz - strlontime - strldrivetime - cttime;
                    String opkzzz = printsum(opnewtimezzz);
                    //Log.e("opkopkopk","#"+opk);
                    if (opkzzz.contains("-")) {
                        txtondutyleft.setText("00:00");
                    } else {
                        txtondutyleft.setText("" + opkzzz + "" + " Hr");
                    }
                }catch (Exception e)
                {

                }
            }
        }
        if (vdk == 1) {
            vdkstat++;
            if (vdkstat > 4) {
                if (breakevent == 0) {
                  //  getteabreakback(commonUtil.OFF_DUTY, intschedule, "3", "8");
                    newbreakreleasenew();
                } else if (breakevent == 1) {
                  //  getlunchbreaksavevaluesback(commonUtil.OFF_DUTY, intschedule, "3", "9");
                    newbreakreleasenew();
                } else if (breakevent == 2) {
                   // setautobreakacceptback("" + commonUtil.OFF_DUTY, intschedule, "3", breakid);
                   // acceptbreak(intduration, "auto", "");

                    newbreakreleasenew();
                }
                vdk = 0;
            }
        } else {
            vdkstat = 0;
        }
//Log.e("bltrack","@"+bltrack);
//        if(bltrack!=0)
//        {
//            bltrack++;
//            if(bltrack==8)
//            {
//                savetrackresp();
//                bltrack=1;
//            }
//        }
        if (pppcstatus == 3) {
            long lblive = 60;

            longblive += 16;
            pref.putString(Constant.BREAK_LIVE_TIME, "" + printsum(longblive));
            // Log.e("longblive", "" + longblive);
            // Log.e("dxxb", "" + printsum(longblive));
            lblive = longbduration - longblive;
            // Log.e("longbduration", "" + longbduration);
            if (longblive >= longbduration) {
                //ss
                // Log.e("calling", "" + "calling");
                //  gettodaynotif();
       //  newbreakrelease();
                newbreakreleasenew();
            } else {
                //  Log.e("calling", "" + "not calling");
            }
            // Log.e("longblive", "" + longblive);
            lblive += 60;
            if (printsum(lblive).contains("-")) {
                txt_duration.setText("00:00");
                txt_breakleft.setText("Time Left : " + printsum(longblive));
                setbreakduration("00:00");
            } else {
               // txt_duration.setText("" + printsum(lblive));
                setbreakduration("" + printsum(lblive));
                txt_breakleft.setText("Time Left : " + printsum(longblive));
            }
        }
        if(pref.getString(Constant.BREAK_AVAILABLE_TODAY) !=null && pref.getString(Constant.BREAK_AVAILABLE_TODAY).contentEquals("no"))
        {
            ak++;
            if(ak==100)
            {
                //Log.e("akkkk",""+ak);
                getbreakrefresh();
                ak=0;
            }
        }

        intbval++;
        //Log.e("intbval","@"+intbval);
        if(intbval>=3)
        {
              String stcheck = "";
                    try {
                        //Log.e("intbvalstk","@"+pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS));

                                if (pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS) !=null && pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {
                                    pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");
                                    savebluetoothstatus("0", "0");

                                }

                    } catch (Exception e) {

                    }
                    //Log.e("stcheckk","@"+stcheck);
            intbval=0;
        }
    }

    ////////////////////////////////
    // Other methods
    ////////////////////////////////
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
    public String printDifferencesec(String startxDate, String endxDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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


        return pad(elapsedHours) + ":" + pad(elapsedMinutes)+":"+pad(elapsedSeconds);
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

    public static String padint(int num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        if (lat != null && lat.length() > 0 && !lat.contentEquals("null")) {
            pref.putString(Constant.CURRENT_LATITUDE,""+lat);
            pref.putString(Constant.CURRENT_LOGINGITUDE,""+lon);
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                    txtcity.setText("Cur.City :" + addresses.get(0).getLocality());
                } else {
                    // do your stuff
                }
            } catch (Exception e) {

            }
            //Log.e("speed","@"+location.getSpeed());
           // Log.e("speedzz","@"+location.getSpeed() * 18 / 5);
            //calculating the speed with getSpeed method it returns speed in m/s so we are converting it into kmph

            try {
              //  Log.e("latlat2", "@" + lat);
                if (lat != null) {
                    // Log.e("calling", "" + address);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
//                    LocationAddress locationAddress = new LocationAddress();
//                    locationAddress.getAddressFromLocation(latitude, longitude,
//                            getApplicationContext(), new GeocoderHandler());
                    getAddressFromLocation(latitude, longitude);
                }
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       // Log.e("lattttdd", "########" + lat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           // checkLocationPermission();
        }

        //pd.dismiss();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
           // Log.e("latttt", "########" + lat);
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                    txtcity.setText("Cur.City :" + addresses.get(0).getLocality());
                    //txtcity.setText("Current City : Visalia");
                } else {
                    // do your stuff
                }
            } catch (Exception e) {

            }
            try {
                // Log.e("latlat1", "@" + lat);
                if (lat != null) {
                    pref.putString(Constant.CURRENT_LATITUDE,""+lat);
                    pref.putString(Constant.CURRENT_LOGINGITUDE,""+lon);
                    // Log.e("calling", "" + address);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
                    getAddressFromLocation(latitude, longitude);
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //pd.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // pd.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {
            return true;
        }
    }
    public void logout_okk() {
        if (OnlineCheck.isOnline(this)) {
            progressdlog = new ProgressDialog(context,
                    AlertDialog.THEME_HOLO_LIGHT);
            progressdlog.setMessage("Please wait...");
            progressdlog.setCancelable(false);
            progressdlog.show();
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.ELOG_NUMBERSS,
                "");
        //pref.getString(Constant.ELOG_NUMBERSS);
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        pref.putString(Constant.BLUETOOTH_CONNECTED, "0");
        pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");
        SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dc = formatsec.format(new Date());
        api = ApiServiceGenerator.createService(Eld_api.class);
        //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
        Call<JsonObject> call = api.getlogout("" + pref.getString(Constant.DRIVER_ID), "" + pref.getString(Constant.DRIVER_ID)
                , "" + pref.getString(Constant.VIN_NUMBER), "" + dc, ""+lat, ""+lon, ""+imenumber1, ""+imenumber2, ""+strstate);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                Log.e("Responsestring", responsez.body().toString());
                //Toast.makeText()
                cancelprogresssdialog();
                if (responsez.isSuccessful()) {

                    if (responsez.body() != null) {
                        String jsonresponse = responsez.body().toString();
                        //Log.e("jsonresponse", jsonresponse.toString());
                        try {
                            JSONObject response = new JSONObject(jsonresponse);
                            try {

                                Log.e("responselog", "" + response.toString());
                                if (response != null) {
                                    if (response.has("status")) {
                                        try {
                                            String statuss = response.getString("status");
                                            if (statuss.contentEquals("1")) {
                                                calllogout();
                                            } else if (statuss.contentEquals("2")) {
                                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                                        Home_activity_bluetooth.this).create();

                                                // Setting Dialog Title
                                                alertDialog.setTitle("e-logbook");

                                                // Setting Dialog Message
                                                alertDialog.setMessage("Now your vehicle is in driving mode. So you are not allowed to logout now.");

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
                                            } else {
                                                if (response.has("message")) {
                                                    String msg = response.getString("message");
                                                    Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "Logout Failed !", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (Exception e) {

                                        }

                                    }
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
                 cancelprogresssdialogz();
            }
        });
    }
    }



    private void calllogout() {
        deleteCache(context);
        pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH,"no");
        pref.putString(Constant.BL_SCANNING_FLAG,"0");
        pref.putString(Constant.ONDUTY_ALERT_DATES,"");
        pref.putString(Constant.BREAK_AVAILABLE_TODAY, "no");
        pref.putString(Constant.BREAK_ALERT_DISPLAY, "empty");
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.ONDUTY_ALERT_DATES,"");
        pref.putString(Constant.NETWORK_TYPE,Constant.CELLULAR);
        pref.putString(Constant.ELOG_NUMBERSS,
                "");
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");

        cancelnotification();
        Intent mIntent12 = new Intent(getApplicationContext(),
                Loginactivitynew.class);
        mIntent12.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent12.putExtra("EXIT", true);
        startActivity(mIntent12);
        finish();
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
    public long splittimeseconds(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
            String timeSplit[] = time.split(":");

            seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60+Integer.parseInt(timeSplit[2]);

        }
        return seconds;

    }
    public String printsum(long different) {
        long minutesInMilli = 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        if(elapsedDays>=1)
        {
            elapsedHours+=elapsedDays*24;
        }

        if (String.valueOf(elapsedHours).contains("-") || String.valueOf(elapsedMinutes).contains("-")) {
            return elapsedHours + ":" + elapsedMinutes;
        } else {
            return pad(elapsedHours) + ":" + pad(elapsedMinutes);
        }

    }

//    public String printthirtysum(long different) {
//
//        //	long secondsInMilli = 1000;
//        long minutesInMilli = 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 34;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//        //  Log.e("elapsedDayskk",""+elapsedDays);
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different;
//
//        if (String.valueOf(elapsedHours).contains("-") || String.valueOf(elapsedMinutes).contains("-")) {
//            return elapsedHours + ":" + elapsedMinutes;
//        } else {
//            return pad(elapsedHours) + ":" + pad(elapsedMinutes);
//        }
//
//    }

    public static String padstring(String nums) {
        int num = Integer.parseInt(nums);
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }

    private void setyellow() {
        String pcstatusf = pref.getString(Constant.PC_STATUS);
        //Log.e("pcstatus",""+pcstatusf);
        if (intschedule == STATUS_ON_DUTY) {
            String pcstatus = pref.getString(Constant.PC_STATUS);
            if (!pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
                callstatus(STATUS_ON_DUTY);
            }

            //String skonstatus=dbUtil.getlastonstatusduty(driverid,vinnumber);
            //if(skonstatus !=null && skonstatus.length()>0 && !skonstatus.contentEquals("null"))
            // {
            //  if(skonstatus.contentEquals("1"))
            // {
            //     callstatusdisable();
            // }
            // }
            // We are "ON DUTY", make sure data logging is started
            //  j1939DataLogger.StartLogging(driverid);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimeractive, null));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdrive, null));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleep, null));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoff, null));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimeractive));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdrive));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleep));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoff));
            }
        } else if (intschedule == STATUS_DRIVING) {
            // We are "DRIVING", make sure data logging is started
            // j1939DataLogger.StartLogging(driverid);
            String pcstatus = pref.getString(Constant.PC_STATUS);
            if (!pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
                callstatus(STATUS_DRIVING);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimer, null));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdriveactive, null));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleep, null));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoff, null));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimer));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdriveactive));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleep));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoff));
            }
        } else if (intschedule == STATUS_SLEEPER) {

            // Log.e("STATUS_SLEEPER","#"+STATUS_SLEEPER);
            String pcstatus = pref.getString(Constant.PC_STATUS);
            if (!pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
                callstatus(STATUS_SLEEPER);
            }
            // Mode is "SLEEPER", make sure data logging is stopped
            // j1939DataLogger.StopLogging();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimer, null));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdrive, null));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleepactive, null));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoff, null));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimer));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdrive));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleepactive));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoff));
            }
        } else if (intschedule == STATUS_OFF_DUTY) {
            String pcstatus = pref.getString(Constant.PC_STATUS);
            // Log.e("edd",""+pcstatus);
            if (!pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
                callstatus(STATUS_OFF_DUTY);
            }
            // We are "OFF DUTY", make sure data logging is stopped
            //j1939DataLogger.StopLogging();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimer, null));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdrive, null));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleep, null));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoffactive, null));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                linonduty.setBackground(getResources().getDrawable(R.drawable.atimer));
                lindriveduty.setBackground(getResources().getDrawable(R.drawable.imgdrive));
                linsleepduty.setBackground(getResources().getDrawable(R.drawable.imgsleep));
                linoffduty.setBackground(getResources().getDrawable(R.drawable.imgoffactive));
            }
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        cancelprogresssdialog();
//Log.e("calling",""+"destroy");
// dialoglogout
        // timerTask.cancel();
        try {
            T.cancel();
            mGoogleApiClient.disconnect();
            if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) !=null &&
                    pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {
                btimer.cancel();
            }
        }catch (Exception e)
        {

        }
        //  j1939UpdateTimer.cancel();
        if(pref.getString(Constant.B_TIMER_STATUS) !=null && pref.getString(Constant.B_TIMER_STATUS).contentEquals("1")) {
            pref.putString(Constant.B_TIMER_STATUS, "2");
        }

        /*try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
//    try {
//        mWherequbeObserver.unregister(this);
//
//    } catch (Exception ignore) {
//        //Log.e(TAG, ignore.toString());
//    }
       // startService();
        if (pref.getString(Constant.LOGIN_CHECK) != null && pref.getString(Constant.LOGIN_CHECK).length() > 0 && !pref.getString(Constant.LOGIN_CHECK).contentEquals("null")) {
            if (pref.getString(Constant.LOGIN_CHECK).equalsIgnoreCase("logged_inn")) {
                startService();
            }
        }
                //
    }

    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.iv_report:
                pref.putString(Constant.C_LATITUDE,""+lat);
                pref.putString(Constant.C_LONGITUDE,""+lon);
                Intent mIntent1 = new Intent(Home_activity_bluetooth.this, Report_Home.class);
                startActivity(mIntent1);
                //finish();
                break;
        }
    }

    private void recordvoice() {
        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //Log.e("datetime",""+formatdatetime.getTimeZone());
        datetime = formatdatetime.format(new Date());

        PackageManager pmanager = this.getPackageManager();
        if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            // Set the file location for the audio
            //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

            final File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "E-logbook" + File.separator);


            if (!mFile.exists()) {
                mFile.mkdirs();
            }
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "E-logbook" + File.separator + datetime + fnam + "_" + driverid + ".mp3";

//Log.e("path",""+Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "E-logbook"+File.separator+datetime+fnam+"_"+driverid+".mp3");

            strfile = datetime + fnam + "_" + driverid + ".mp3";


            //mFileName += "/elog_trucksoft.3gp";
            // mFileName += "/"+datetime+"_"+driverid+".mp3";
            filepath = mFileName;
            //filepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/01.jpg";
            // Create the recorder
            mediaRecorder = new MediaRecorder();
            // Set the audio format and encoder
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Setup the output location
            mediaRecorder.setOutputFile(mFileName);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // Start the recording
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        } else { // no mic on device
            Toast.makeText(this, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Home_activity_bluetooth.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(Home_activity_bluetooth.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                       /* Toast.makeText(Home_activity.this,"Permission
                                Denied",Toast.LENGTH_LONG).show());*/
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }



    private void uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(filepath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody did = RequestBody.create(MediaType.parse("text/plain"), "" + pref.getString(Constant.DRIVER_ID));
        RequestBody msgid = RequestBody.create(MediaType.parse("text/plain"), "" + pref.getString(Constant.BREAK_MSG_ID));
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), "" + strstate);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), "" + straddress);
        RequestBody testbreak = RequestBody.create(MediaType.parse("text/plain"), "breakk");

        api = ApiServiceGenerator.createService(Eld_api.class);
       // Eld_api getResponse = AppConfig.getRetrofit().create(Eld_api.class);
        Call call = api.uploadFile(fileToUpload, filename, did, msgid, state, address, testbreak);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                //Log.e("responseaudio", "success");
                boolvoicerecord=true;
                cancelprogresssdialog();
                pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                boolvoicerecord=true;
                cancelprogresssdialog();
                pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
                //Log.e("responseaudioer", "fail" + t.toString());
            }
        });
    }




    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.e("call","onRestart");
    }

    public void CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void gettodaynotif() {
        try {
            // Log.e("latlat1", "@" + lat);
            if (lat != null) {
                // Log.e("calling1", "" + address);
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
        //Log.e("notif","calling");
//        dialogz = new ProgressDialog(context,
//                AlertDialog.THEME_HOLO_LIGHT);
//        dialogz.setMessage("Please wait...");
//       // dialogz.setCancelable(false);
//        dialogz.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
//        Call<List<Getvalue_model>> call = api.getValnotification(did,vinnumber,lat,lon,address,olddversion);
//        Log.e("refreshcall", "http://eld.e-logbook.info/elog_app/eld_getlogvalues.php?did=" + did +
//                "&vin=" + vinnumber +
//                "&addres=s" + straddress +
//                "&versiion=" + olddversion +
//                "&lat=" + lat +
//                "&lon=" + lon +
//                "version_name" + olddversionname);
        Call<List<Getvalue_model>> call = api.getValues_eld(did, vinnumber, straddress, olddversion, lat, lon, olddversionname, strstate,timezonesname,timezonesid,strvalx,"bluetooth","");

        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if (response.isSuccessful()) {
                    //dialogz.dismiss();
                    //dd
                    movies = response.body();
                    settodayval(movies,"0");
                    savestate("");
                } else {
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

//k2


    private void takeipicture() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(Home_activity_bluetooth.this);

    }


    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {
                    InputStream iStream = getContentResolver().openInputStream(result.getUri());
                    img = getBytes(iStream);
                    if (imagetype == 0) {
                        (imgprofile).setImageURI(result.getUri());
                        uploadphoto();
                    } else if (imagetype == 2) {
//                        (txtvimage).setImageURI(result.getUri());
//                        uploadphoto();
                    } else {
//Log.e("vaz",""+imagetype);
                        // image = getBytes(iStream);
                        //  new ImageUploadTask2().execute();

                        Bitmap bitmaph = BitmapFactory.decodeByteArray(img, 0, img.length);
                        store(bitmaph, driverid);

                        uploadphoto();
                    }
                } catch (Exception e) {

                }
                //  Log.e("ddd",""+result.getUri().toString());
                // decodeFile(result.getUri().toString());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        name.setText(result.get(0));
                    }
                    break;
                }

            }
        }
    }

    public void uploadphoto() {
        progressdlog = new ProgressDialog(Home_activity_bluetooth.this,
                AlertDialog.THEME_HOLO_LIGHT);
        if (OnlineCheck.isOnline(this)) {

            progressdlog.setMessage("Please wait...");
            progressdlog.setCancelable(false);
            progressdlog.show();

            SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dc = formatsec.format(new Date());
            api = ApiServiceGenerator.createService(Eld_api.class);
            //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
            Call<JsonObject> call = api.uploadprofileimg("" + pref.getString(Constant.DRIVER_ID), "" +img
                    , "" + lat, "" + lon);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {
                    cancelprogresssdialog();
                    Log.e("Responsestring", responsez.body().toString());
                    //Toast.makeText()
                    if (responsez.isSuccessful()) {

                        if (responsez.body() != null) {
                            Toast.makeText(
                                            context, "Image upload successfully", Toast.LENGTH_LONG)
                                    .show();


                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    cancelprogresssdialog();
                }
            });
        }

    }



    private void callstatus(int Mode) {
        //mkb
//Log.e("intsch",""+intschedule);
        // if (Mode == STATUS_DRIVING && intschedule != STATUS_DRIVING)
        String pcstatus = pref.getString(Constant.PC_STATUS);
        //Log.e("pcstatus",""+pcstatus);
        if (!pcstatus.contentEquals(commonUtil.PC_ENABLE)) {
            if (Mode == STATUS_DRIVING) {
                //Log.e("sucess","drive");
                //  onClickStatusDriving();

                //lindriveduty.setEnabled(false);
                // lindriveduty.setAlpha(1.00f);
                if (pref.getString(Constant.VIN_NUMBER).contentEquals("NO VIN")) {
                    // lindriveduty.setEnabled(true);
                    // lindriveduty.setAlpha(1.00f);
                    linonduty.setEnabled(true);
                    linonduty.setAlpha(1.00f);

                    linsleepduty.setEnabled(true);
                    linsleepduty.setAlpha(1.00f);

                    linoffduty.setEnabled(true);
                    linoffduty.setAlpha(1.00f);

                    linten.setEnabled(true);
                    linten.setAlpha(1.00f);

                    lin_thirty.setEnabled(true);
                    lin_thirty.setAlpha(1.00f);

                    img_vehicle.setEnabled(true);
                    img_vehicle.setAlpha(1.00f);


                    imgbreaktake.setEnabled(false);
                    imgbreaktake.setAlpha(0.40f);
                } else {
                    linonduty.setEnabled(false);
                    linonduty.setAlpha(0.40f);

                    linsleepduty.setEnabled(false);
                    linsleepduty.setAlpha(0.40f);

                    linoffduty.setEnabled(false);
                    linoffduty.setAlpha(0.40f);

                    linten.setEnabled(false);
                    linten.setAlpha(0.40f);

                    lin_thirty.setEnabled(false);
                    lin_thirty.setAlpha(0.40f);

                    img_vehicle.setEnabled(false);
                    img_vehicle.setAlpha(0.40f);
                    imgbreaktake.setEnabled(false);
                    imgbreaktake.setAlpha(0.40f);
                }
            }

            // else if (Mode == STATUS_ON_DUTY && intschedule != STATUS_ON_DUTY)
            else if (Mode == STATUS_ON_DUTY) {
                //onClickStatusOnDuty();
                if (pref.getString(Constant.VIN_NUMBER).contentEquals("NO VIN")) {
                    lindriveduty.setEnabled(true);
                    lindriveduty.setAlpha(1.00f);
                }
                // lindriveduty.setEnabled(false);
                //  lindriveduty.setAlpha(0.40f);

                linonduty.setEnabled(true);
                linonduty.setAlpha(1.00f);

                linsleepduty.setEnabled(true);
                linsleepduty.setAlpha(1.00f);

                linoffduty.setEnabled(true);
                linoffduty.setAlpha(1.00f);

                linten.setEnabled(true);
                linten.setAlpha(1.00f);

                lin_thirty.setEnabled(true);
                lin_thirty.setAlpha(1.00f);

                img_vehicle.setEnabled(true);
                img_vehicle.setAlpha(1.00f);
                imgbreaktake.setEnabled(true);
                imgbreaktake.setAlpha(1.00f);
            }
            //  else if (Mode == STATUS_OFF_DUTY && intschedule != STATUS_OFF_DUTY)
            else if (Mode == STATUS_OFF_DUTY) {
                //onClickStatusOffDuty();

                // lindriveduty.setEnabled(false);
                //  lindriveduty.setAlpha(0.40f);
                if (pref.getString(Constant.VIN_NUMBER).contentEquals("NO VIN")) {
                    lindriveduty.setEnabled(true);
                    lindriveduty.setAlpha(1.00f);
                }
                linonduty.setEnabled(true);
                linonduty.setAlpha(1.00f);

                linsleepduty.setEnabled(true);
                linsleepduty.setAlpha(1.00f);

                linoffduty.setEnabled(true);
                linoffduty.setAlpha(1.00f);

                linten.setEnabled(true);
                linten.setAlpha(1.00f);

                lin_thirty.setEnabled(true);
                lin_thirty.setAlpha(1.00f);
                img_vehicle.setEnabled(true);
                img_vehicle.setAlpha(1.00f);
                imgbreaktake.setEnabled(true);
                imgbreaktake.setAlpha(1.00f);
            } else if (Mode == STATUS_SLEEPER) {
                // onClickStatusSleeper();

                // lindriveduty.setEnabled(false);
                // lindriveduty.setAlpha(0.40f);
                if (pref.getString(Constant.VIN_NUMBER).contentEquals("NO VIN")) {
                    lindriveduty.setEnabled(true);
                    lindriveduty.setAlpha(1.00f);
                }
                linonduty.setEnabled(true);
                linonduty.setAlpha(1.00f);

                linsleepduty.setEnabled(true);
                linsleepduty.setAlpha(1.00f);

                linoffduty.setEnabled(true);
                linoffduty.setAlpha(1.00f);
                linten.setEnabled(true);
                linten.setAlpha(1.00f);

                lin_thirty.setEnabled(true);
                lin_thirty.setAlpha(1.00f);
                img_vehicle.setEnabled(true);
                img_vehicle.setAlpha(1.00f);
                imgbreaktake.setEnabled(true);
                imgbreaktake.setAlpha(1.00f);
            }
        }
    }

    @Override
    public void onBackPressed() {
        try {


            if (AppModel.getInstance().mDevice!=null)
            {
                WherequbeService.getInstance().disconnect();

            }
        }catch (Exception e)
        {

        }
        super.onBackPressed();

        //Log.e("calling","back pressed");
        Runtime.getRuntime().gc();
        System.gc();
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
        cancelprogresssdialog();
        //startService();
    }


//k1

    private void gettodaysavevalues(final String field, final int statusid, final String statuss) {
        try {
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
        } catch (Exception e) {

        }
      //  apicall=1;

        String did = pref.getString(Constant.DRIVER_ID);
        //Log.e("address","@"+address);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);

//        Call<List<Getvalue_model>> call = api.getsaveValues(vinnumber,field,""+statusid,statuss,lat,lon,did,address,olddversion);
//        Log.e("saveurl","eld_savedata.php?vin="+vinnumber+"&fname="+field+
//        "&statusid="+statusid+"&pc_status="+statuss+"&lat="+lat+"&lon="+lon+"&did="+did+
//         "&address="+straddress+"&versiion="+olddversion);
        Call<List<Getvalue_model>> call = api.getsaveValues_elds(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress, olddversion, strstate,timezonesname,timezonesid,"break","M");
        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                    //dd
//                    movies = response.body();
//                    settodayval(movies,"0");
//                    savestate("");
                } else {
                    cancelprogresssdialog();
                    //  getvehicle();
                    callsaveDialog(field, statusid, statuss);
                    //Log.e("ss"," Response "+String.valueOf(response.code()));
                }
               // apicall=0;
            }

            @Override
            public void onFailure(Call<List<Getvalue_model>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                callsaveDialog(field, statusid, statuss);
                // getvehicle();
               // apicall=0;
            }
        });
    }

    //k34
    private void gettodaysavethirtyreset(final String field, final int statusid, final String statuss) {
        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/thon");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String did = pref.getString(Constant.DRIVER_ID);
        // Log.e("did",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
        /*   Call<List<Getvalue_model>> call = api.getsavethirtyreset(vinnumber,field,""+statusid,statuss,lat,lon,did,address);*/
        Call<List<Responsemodel>> call = api.getsavethirtyreset_eldnew(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress,strstate,timezonesname,timezonesid,"");

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                } else {
                    cancelprogresssdialog();
                    callthirtysaveDialog(field, statusid, statuss);
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                callthirtysaveDialog(field, statusid, statuss);
                // getvehicle();
            }
        });
    }
    //k10 reset active
    private void savetenhours(final String field, final int statusid, final String statuss) {
        String did = pref.getString(Constant.DRIVER_ID);
        // Log.e("did",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<List<Responsemodel>> call = api.getsavetenhours_eld(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress,strstate,timezonesname,timezonesid,"");

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                } else {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                callsavetenhourdialog(field, statusid, statuss);
                // getvehicle();
            }
        });
    }


    private void callnetworkDialog() {
        try {
            if (pref.getString(Constant.NETWORK_TYPE) != null && pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH) & pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS) != null &&
                    pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {
            } else {


                View view = View.inflate(context, R.layout.off_dialog, null);
                TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
                TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
                txt_msg.setText("" + "Network / Connection problem. Please retry");
                final Dialog dialogmk = new Dialog(context, R.style.DialogTheme);
                //dialog = new Dialog(this, R.style.DialogTheme);
                dialogmk.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogmk.setContentView(view);
                dialogmk.setCanceledOnTouchOutside(false);
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
                        gettodayvalues(str_vin, "20");

                    }
                });
            }
        }catch (Exception e)
        {

        }
    }

    private void callthirtysaveDialog(final String field, final int statusid, final String statuss) {
        View view = View.inflate(context, R.layout.off_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Network / Connection problem. Please retry");
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
                gettodaysavethirtyreset(field, statusid, statuss);

            }
        });
    }

    private void callsavetenhourdialog(final String field, final int statusid, final String statuss) {
        View view = View.inflate(context, R.layout.off_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Network / Connection problem. Please retry");
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
                savetenhours(field, statusid, statuss);

            }
        });
    }


    private void callsaveDialog(final String field, final int statusid, final String statuss) {
        View view = View.inflate(context, R.layout.off_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Network / Connection problem. Please retry");
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
                gettodaysavevalues(field, statusid, statuss);

            }
        });
    }

    //k34 release
    private void getenablesavethirtyreset(final String field, final int statusid, final String statuss) {
        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/thoff");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String did = pref.getString(Constant.DRIVER_ID);
        // Log.e("did",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);

        /*      Call<List<Getvalue_model>> call = api.getenablethirtyreset(vinnumber,field,""+statusid,statuss,lat,lon,did,address);*/
        Call<List<Responsemodel>> call = api.getenablethirtyreset_eldnew(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress,"","");

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                    //Log.e("resk","sucess");

                } else {
                    //Log.e("resk","fail");
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                callthirtyenableDialog(field, statusid, statuss);
                // getvehicle();
            }
        });
    }

    private void callthirtyenableDialog(final String field, final int statusid, final String statuss) {
        View view = View.inflate(context, R.layout.off_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Network / Connection problem. Please retry");
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
                getenablesavethirtyreset(field, statusid, statuss);

            }
        });
    }


    private void releasetenhour(final String field, final int statusid, final String statuss) {
        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/tenoff");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String did = pref.getString(Constant.DRIVER_ID);
        //Log.e("call",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
 Call<List<Responsemodel>> call = api.releasetenhour_eld(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress,strstate,timezonesname,timezonesid,"");

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                } else {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                calltenhourDialog(field, statusid, statuss);
                // getvehicle();
            }
        });
    }

    private void calltenhourDialog(final String field, final int statusid, final String statuss) {
        View view = View.inflate(context, R.layout.off_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Network / Connection problem. Please retry");
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
                releasetenhour(field, statusid, statuss);

            }
        });
    }







    //k3
    private void gettodaypcenable(String str_vin) {
        try {
            // Log.e("latlat1", "@" + lat);
            if (lat != null) {
                // Log.e("calling1", "" + address);
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
        // Log.e("did",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
       // Log.e("pcenabele","http://eld.e-logbook.info/elog_app/eld_pcenable.php?did="+did+"&vin="+vinnumber+"&lat="+lat+"&lon="+lon+"&address="+straddress+"&versiion="+olddversion+"state"+strstate);
        Call<List<Responsemodel>> call = api.getpcenable_eld(did, vinnumber, lat, lon, straddress, olddversion,strstate,timezonesname,timezonesid,"");

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                } else {
                    cancelprogresssdialog();
                    gettodayvalues(str_vin, "20");
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                callpcDialog();
                // getvehicle();
            }
        });
    }


    private void callstatusdisable() {
        //   lindriveduty.setEnabled(false);
        //   lindriveduty.setAlpha(0.40f);
        // lindriveduty.setEnabled(false);
        // lindriveduty.setAlpha(1.00f);

        linonduty.setEnabled(false);
        linonduty.setAlpha(0.40f);

        linsleepduty.setEnabled(false);
        linsleepduty.setAlpha(0.40f);

        linoffduty.setEnabled(false);
        linoffduty.setAlpha(0.40f);


    }

    private void callstatusenable() {

        // lindriveduty.setEnabled(false);
        // lindriveduty.setAlpha(1.00f);

        linonduty.setEnabled(true);
        linonduty.setAlpha(1.00f);

        linsleepduty.setEnabled(true);
        linsleepduty.setAlpha(1.00f);

        linoffduty.setEnabled(true);
        linoffduty.setAlpha(1.00f);


    }


    private void callpcDialog() {
        View view = View.inflate(context, R.layout.off_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Network / Connection problem. Please retry");
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
                gettodaypcenable(vinnumber);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String strkvin = pref.getString(Constant.VIN_NUMBER);
            if (strkvin == null || strkvin.length()==0 || strkvin.contentEquals("null")) {

                finish();
                Intent mIntent = new Intent(
                        Home_activity_bluetooth.this,
                        SplashScreenActivity.class);
                startActivity(mIntent);


            }
        }catch (Exception e)
        {

        }


        if(onrefresh)
        {
            onNewIntent(getIntent());
            onrefresh=false;
        }else{

        }

        try
        {
            if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {
                registerReceiver(uiRefresh, uiIf);
            }

          //  startRepeatingTask();
        }catch (Exception e)
        {

        }




        try {
            pref = Preference.getInstance(context);
            strlogin = pref.getString(Constant.LOGIN_CHECK);//nm
            str_vin = pref.getString(Constant.VIN_NUMBER).trim();
            // Log.e("boolstate","resume"+strlogin);
//        if(boolstate)
//        {
//            Log.e("calling","resume");
//            getdataserver(str_vin);
//        }
            if (strlogin.contentEquals("logged_off")) {
                pref.putString(Constant.LOGIN_CHECK,
                        "logged_off");
                pref.putString(Constant.ELOG_NUMBERSS,
                        "");
                cancelprogresssdialog();
                //pref.getString(Constant.ELOG_NUMBERSS);
                pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
                pref.putString(Constant.DRIVE_NOTIFICATON, "0");
                cancelnotification();
                Intent mIntent12 = new Intent(Home_activity_bluetooth.this,
                        Loginactivitynew.class);
                startActivity(mIntent12);
                finish();
            } else if (str_vin.contentEquals("Demo") || str_vin.contentEquals("DEMO")) {
                cancelprogresssdialog();
                Intent intr = new Intent(context, Vehicle_seect.class);
                intr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intr.putExtra("EXIT", true);
                startActivity(intr);
                finish();

            }

        } catch (Exception e) {

        }
        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(DispatchConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

        // clearing the notification tray
        Trucksoft_elog_Notify_Utils.clearNotifications(context);
    }

    @Override
    protected void onPause() {
        onrefresh=true;
    //    brf=0;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        cancelprogresssdialog();
if(pref.getString(Constant.B_TIMER_STATUS) !=null && pref.getString(Constant.B_TIMER_STATUS).contentEquals("1")) {
    pref.putString(Constant.B_TIMER_STATUS, "2");
}

        try{
            callautoconnect();
        }catch (Exception e)
        {

        }


    }



//    public void updatenotifystatus(String vcode) {
//
//
//        if (OnlineCheck.isOnline(this)) {
//
//            WebServices.updatenotify(this, vcode, new JsonHttpResponseHandler() {
//                //	@Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      String responseString, Throwable throwable) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, responseString,
//                            throwable);
//
//                }
//
//                //@Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      Throwable throwable, JSONArray errorResponse) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable,
//                            errorResponse);
//
//                }
//
//                //@Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      Throwable throwable, JSONObject errorResponse) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable,
//                            errorResponse);
//
//                }
//
//                //@Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      JSONArray response) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, response);
//
//                    //Log.e("response",""+response.toString());
//
//                    try {
//                        if (response != null) {
//
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //@Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      JSONObject response) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, response);
//                    // Log.e("response1",""+response.toString());
//
//                    try {
//                        if (response != null) {
//
//
//                            String status = response
//                                    .getString("status");
//
//                            if (status.equalsIgnoreCase("1")) {
//
//                            } else if (status.equalsIgnoreCase("0")) {
//
//                            }
//
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //@Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      String responseString) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, responseString);
//                }
//
//            });
//        }
//
//    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if (extras.containsKey("notify")) {

                String ntfy = extras.getString("notify");
                String msgg = extras.getString("msgg");
                String ststatus = extras.getString("estatus");
                //breakid = extras.getString("breakid");

                pref.putString(Constant.NOTIFICATION_MESSAGE, "" + msgg);
                pref.putString(Constant.NOTIFICATION_ESTATUS, "" + ststatus);
                pref.putString(Constant.NOTIFICATION_STATUSMK, "" + ntfy);
                //Log.e("mskkk","@"+msgg);

                Intent ink = new Intent(context, Home_activity_bluetooth.class);
                // ink.putExtra("notifys",""+ntfy);
                ink.putExtra("msgg", "" + msgg);
                ink.putExtra("estatus", "" + ststatus);
                ink.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ink.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finishAffinity();
                startActivity(ink);

            } else {
                // Log.e("calld", "one");
                //getdataserver(str_vin);
                if (str_vin.contentEquals("Demo")) {

                } else {
                    gettodayvalues(str_vin, "20");

                }
            }
        } else {
            //Log.e("calld", "two");
            //  getdataserver(str_vin);
            if (str_vin.contentEquals("Demo")) {

            } else {

                gettodayvalues(str_vin, "20");

            }
        }

        try {
            Calendar c = Calendar.getInstance();


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());


            uploaddatausage(formattedDate);
            pref.putString(Constant.DATAUSAGEUPDATE_DATE, formattedDate);
//            }
        } catch (Exception e) {

        }
    }

    public void cancelnotification() {


        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    private void settodayval(List<Getvalue_model> vals,String vald) {
        rset = 0;
        boolactice=false;
        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
        SimpleDateFormat formatime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strsl = formatime.format(new Date());
        if (vals.size() > 0 && vals != null) {
            for (int i = 0; i < vals.size(); i++) {
                Getvalue_model gt = new Getvalue_model();
                gt = vals.get(i);
                String ontotal = gt.getsonduty();
               // Log.e("ontotal",""+ontotal);
                String oftotal = gt.getsoffdutty();
                // Log.e("oftotal",""+oftotal);
                String drtotal = gt.getsdriving();
                 //Log.e("drtotal",""+drtotal);
                String sltotal = gt.getssleep();
                //Log.e("sltotal",""+sltotal);
                String cstatus = gt.getstatus();
                String soldsleep = gt.getsoldsleep();
//Log.e("soldsleep",""+soldsleep);
                String skonstatus = gt.getpcstatus();
                //Log.e("versioncode",""+gt.getVersioncode());
                try
                {
                    strondutylogid=gt.getonid();
                    pref.putString(Constant.C_ONDUTY_TIME_ID,strondutylogid);
                }catch (Exception e)
                {

                }

                SimpleDateFormat formatdatetimez = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                respdatetime = formatdatetimez.format(new Date());
                String strrule= gt.getRule();
                if(strrule !=null && strrule.length()>0)
                {
                    if(strrule.contentEquals("Federal") || strrule.contentEquals("federal"))
                    {
                        pref.putString(Constant.FEDERAL_DRIVE_ACTIVE,"active");
                    }else{
                        pref.putString(Constant.FEDERAL_DRIVE_ACTIVE,"deactive");
                      // String states = pref.getString(Constant.STATE_FIELD);
            txtcstate.setVisibility(View.GONE);
            txtrules.setText("Home state rules applied");
           // ttval.setText("Home state rules applied");
                    }
                }
                try {
                    String locinterval = gt.getLoc_interval();

                    if (locinterval != null && locinterval.length() > 0) {
                        int locval = 30;
                        locval = Integer.parseInt(locinterval);
                        if (locval <= 0) {
                            pref.putString(Constant.LOCATION_UPDATE_STATUS, "20");
                            repinterval = 20;
                            if (pref.getString(Constant.LOCATION_UPDATE_DATE) != null && pref.getString(Constant.LOCATION_UPDATE_DATE).length() > 0) {
                            } else {
                                SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                String ctime = formatdatetime.format(new Date());
                                pref.putString(Constant.LOCATION_UPDATE_DATE, "" + ctime);
                            }
                        } else {
                            pref.putString(Constant.LOCATION_UPDATE_STATUS, "" + locval);
                            //   repinterval = locval;
                            repinterval = 20;
                            if (pref.getString(Constant.LOCATION_UPDATE_DATE) != null && pref.getString(Constant.LOCATION_UPDATE_DATE).length() > 0) {
                            } else {
                                SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String ctime = formatdatetime.format(new Date());
                                pref.putString(Constant.LOCATION_UPDATE_DATE, "" + ctime);
                            }
                        }
                    }

                } catch (Exception e) {

                }

                try{
                    String ncount=gt.getUnread_app();
                    if(ncount !=null && ncount.length()>0)
                    {
                        int rescount=Integer.parseInt(ncount);
                        if(rescount>0)
                        {
                            msg_notification.setVisibility(View.VISIBLE);
                            msg_notification.setText(""+rescount);
                            tmsg.setTextColor(Color.parseColor("#E1000A"));
                        }else{
                            msg_notification.setVisibility(View.GONE);
                            tmsg.setTextColor(Color.parseColor("#008000"));
                            msg_notification.setText("0");
                        }
                    }
                }catch (Exception e)
                {

                }
                try{
                    String mval=gt.getMsgval();
                    if(mval!=null && mval.length()>0)
                    {
                        tmsg.setVisibility(View.VISIBLE);
                    }else{
                        tmsg.setVisibility(View.INVISIBLE);
                    }

                }catch (Exception e)
                {

                }


                try {
                    String bluaddress =gt.getBlu_address();
                    String bluadname = gt.getBlu_name();

                    if (bluaddress != null && bluaddress.length() > 0) {
                        pref.putString(Constant.BLUETOOTH_ADDRESS, "" + bluaddress);

                    }

                    if (bluadname != null && bluadname.length() > 0) {

                        pref.putString(Constant.BLUETOOTH_NAME, "" + bluadname);
                    }
                }catch (Exception e)
                {

                }

                try
                {
                 String lastatus=gt.getLastlogstatus();
               //  Log.e("lastatus","@"+lastatus);
                 if(lastatus !=null && lastatus.length()>0 && !lastatus.contentEquals("null"))
                 { //pref.getString(Constant.CURRENT_STATUS_BB);
                     pref.putString(Constant.CURRENT_STATUS_BB,""+lastatus);
                 }
                }catch (Exception e){

                }

                try{
                    if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null") && vald.contentEquals("20")) {
                        if(cstatus.contentEquals(commonUtil.OFF_DUTY))
                        {
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.OFF_DUTY);
                            pref.putString(Constant.CURRENT_STATUS,commonUtil.OFF_DUTY);
                            pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.OFF_DUTY);
                        }else if(cstatus.contentEquals(commonUtil.ON_DUTY))
                        {
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.ON_DUTY);
                            pref.putString(Constant.CURRENT_STATUS,commonUtil.ON_DUTY);
                            pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.ON_DUTY);

                        }else if(cstatus.contentEquals(commonUtil.SLEEP))
                        {
                            pref.putString(Constant.CURRENT_STATUS_BB,commonUtil.SLEEP);
                            pref.putString(Constant.CURRENT_STATUS,commonUtil.SLEEP);
                            pref.putString(Constant.PREVIOUS_CURRENT_STATUS,commonUtil.SLEEP);

                        }else{

                            pref.putString(Constant.CURRENT_STATUS,commonUtil.DRIVING);

                        }
                    }
                }catch (Exception e)
                {

                }
                try{

                    String bluetime=gt.getBlu_time();
                    String blusptime=gt.getBlu_speedtime();

                    if(bluetime !=null && bluetime.length()>0)
                    {
                        pref.putString(Constant.BLUETOOTH_DISCONNECT_TIME, "" + bluetime);
                    }
                    if(blusptime !=null && blusptime.length()>0) {
                        pref.putString(Constant.BLUETOOTH_SPEED_CHANGE_TIME, "" + blusptime);
                    }

                    String blscanning=gt.getBlu_scanning();
                    if(blscanning !=null && blscanning.length()>0) {
                        //Log.e("blscanning",""+blscanning);
                        mInterval=Integer.parseInt(blscanning);
                    }

                }catch (Exception e)
                {

                }

                if(gt.getVersioncode() !=null && gt.getVersioncode().length()>0)
                {
                    pref.putString(Constant.APP_VERSION,gt.getVersioncode());
                    pref.putString(Constant.APP_VERSION_NAME,gt.getVersionname());
                    appalert();
                }
                if (pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
                    statedrive = pref.getString(Constant.FEDERAL_DRIVE_HOURS);
                    stateonduty = pref.getString(Constant.FEDERAL_ONDUTY_HOURS);
                    txtonallowed.setText(stateonduty + " Hr");
                    txtdriveallowed.setText(statedrive + " Hr");
                }
                String reserthr = gt.getResethr();
                resettime = gt.getResethr();
                String tenreserthr = gt.getTenresethr();
                tenresettime = gt.getTenresethr();
                String breakduration = gt.getBreakduration();


                pref.putString(Constant.VOICE_ON, "" + gt.getVoiceon());
                pref.putString(Constant.VOICE_OFF, "" + gt.getVoiceoff());
                pref.putString(Constant.VOICE_SLEEP, "" + gt.getVoicesleep());
                pref.putString(Constant.VOICE_DRIVE, "" + gt.getVoicedrive());
                //  Log.e("vstat","###"+gt.getVoiceon());
                if (skonstatus != null && skonstatus.length() > 0 && !skonstatus.contentEquals("null")) {

                } else {
                    skonstatus = "0";
                }
                //Log.e("remark","@"+reserthr);

                String eldstatus = gt.getEldstatus();
                // Log.e("eldstatus","@"+eldstatus);
                if (eldstatus != null && eldstatus.length() > 0 && !eldstatus.contentEquals("null")) {
                    if (eldstatus.contentEquals("1")) {
                        callvehicleoffdialog();
                    } else if (eldstatus.contentEquals("2")) {
//                callvehicleoffdialog2();
                    } else {
                        if (dialogvehicleoff != null && dialogvehicleoff.isShowing()) {
                            dialogvehicleoff.dismiss();
                        }
                    }
                } else {
                    if (dialogvehicleoff != null && dialogvehicleoff.isShowing()) {
                        dialogvehicleoff.dismiss();
                    }
                }
                String remak = gt.getremark();
                if (remak != null && remak.length() > 0 && !remak.contentEquals("null")) {

                } else {
                    //tp
                    if (str_remarkstatus != null && str_remarkstatus.length() > 0 && !str_remarkstatus.contentEquals("null")) {
                        if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null")) {
                            if (str_remarkstatus.contentEquals(cstatus)) {
//Log.e("rstatus",""+str_remarkstatus);

                                int_remarkcount = pref.getInt(Constant.REMARK_COUNT);
                                // Log.e("rstatuscount",""+int_remarkcount);
                                if (int_remarkcount < 1) {
                                    int_remarkcount++;
                                    // Log.e("rstatuscount1",""+int_remarkcount);
                                    pref.putString(Constant.REMARK_STATUS, "" + cstatus);
                                    pref.putInt(Constant.REMARK_COUNT, int_remarkcount);

                                    if (skonstatus.contentEquals("0")) {
                                        remarkback(cstatus);
                                    }
                                }
                            } else {
                                str_remarkstatus = cstatus;
                                int_remarkcount = 1;
                                //Log.e("rcs",""+str_remarkstatus);
                                // Log.e("rcscnt",""+int_remarkcount);
                                pref.putString(Constant.REMARK_STATUS, "" + cstatus);
                                pref.putInt(Constant.REMARK_COUNT, int_remarkcount);
                                if (skonstatus.contentEquals("0")) {
                                    remarkback(cstatus);
                                }
                            }
                        } else {
                            str_remarkstatus = cstatus;
                            int_remarkcount = 1;
                            //Log.e("rcs",""+str_remarkstatus);
                            // Log.e("rcscnt",""+int_remarkcount);
                            pref.putString(Constant.REMARK_STATUS, "" + cstatus);
                            pref.putInt(Constant.REMARK_COUNT, int_remarkcount);
                            if (skonstatus.contentEquals("0")) {
                                remarkback(cstatus);
                            }
                        }

                    } else {
                        str_remarkstatus = cstatus;
                        int_remarkcount = 1;
                        // Log.e("rcs",""+str_remarkstatus);
                        // Log.e("rcscnt",""+int_remarkcount);
                        pref.putString(Constant.REMARK_STATUS, "" + cstatus);
                        pref.putInt(Constant.REMARK_COUNT, int_remarkcount);
                        if (skonstatus.contentEquals("0")) {
                            remarkback("");
                        }
                    }


                }
                if (skonstatus != null && skonstatus.length() > 0 && !skonstatus.contentEquals("null")) {
                    txttenused.setText("00:00 Hr");
                    txttenremain.setText("00:00 Hr");
                    txtresethr.setText("00:00 Hr");
                    resetbalance.setText("00:00 Hr");

//lk

                    if (skonstatus.contentEquals("1")) {
                        imgok.setEnabled(true);
                        imgok.setClickable(true);
                        boolactice=true;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        //  btnthirty.setEnabled(true);
                        //  btnthirty.setClickable(true);
                        linbreak.setVisibility(View.GONE);
                        linthirty.setVisibility(View.GONE);
                        lintenty.setVisibility(View.GONE);
                        linpvc.setVisibility(View.VISIBLE);
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();
                        txtprcstatus.setText("ON");
                        txtprcstatus.setTextColor(Color.parseColor("#072607"));
                        txtprcstatus.startAnimation(animBlink);
                        txthrtytatus.setText("OFF");//before ON
                        txthrtytatus.setTextColor(Color.parseColor("#E1000A"));
                        txthrtytatus.startAnimation(animBlink);
                        txtresethr.setText("00:00 Hr");
                        tenstatusonof.setText("OFF");//before ON
                        tenstatusonof.setTextColor(Color.parseColor("#E1000A"));
                        tenstatusonof.startAnimation(animBlink);

                        final int sdk = Build.VERSION.SDK_INT;
                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                            imgok.setImageResource(R.drawable.prlactive);
                            // btnthirty.setImageResource(R.drawable.toff);


                        } else {


                            imgok.setImageResource(R.drawable.prlactive);
                            //  btnthirty.setImageResource(R.drawable.toff);
                        }

                        pppcstatus = 1;
                        resetbalance.setText("00:00 Hr");
                    } else if (skonstatus.contentEquals("2")) {
                        boolactice=true;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        linbreak.setVisibility(View.GONE);
                        linthirty.setVisibility(View.VISIBLE);
                        lintenty.setVisibility(View.GONE);
                        linpvc.setVisibility(View.GONE);
                        if (resettime == null || resettime.length() == 0 || resettime.contentEquals("null")) {
                            resettime = "00:00:01";
                        }

                        rset = splittime(resettime);
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();
                        imgok.setEnabled(false);
                        imgok.setClickable(false);


                        txthrtytatus.setText("ON");
                        txthrtytatus.setTextColor(Color.parseColor("#17BD17"));
                        txthrtytatus.startAnimation(animBlink);
                        txtresethr.setText("" + reserthr);
                        tenstatusonof.setText("OFF");//before ON
                        tenstatusonof.setTextColor(Color.parseColor("#E1000A"));
                        tenstatusonof.startAnimation(animBlink);
                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);


                        long currntime = splittime("34:00:00");

                        long usedtime = splittime(reserthr);

                        long lres = currntime - usedtime;//ll

//                        if (lres > 86400) {
//                            resetbalance.setText("" + printthirtysum(lres));
//                            txtthirtyremin.setText("" + printthirtysum(lres));
//                        } else {
                            resetbalance.setText("" + printsum(lres));
                            txtthirtyremin.setText("" + printsum(lres));
//                        }
                        pppcstatus = 2;

                    } else if (reserthr != null && !reserthr.contentEquals("00:00:00") && !reserthr.contentEquals("null")) {
                        if (resettime == null || resettime.length() == 0 || resettime.contentEquals("null")) {
                            resettime = "00:00:01";
                        }
                        boolactice=true;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        linbreak.setVisibility(View.GONE);
                        linthirty.setVisibility(View.VISIBLE);
                        lintenty.setVisibility(View.GONE);
                        linpvc.setVisibility(View.GONE);
                        rset = splittime(resettime);
                        txtresethr.setText("" + reserthr);
                        long currntime = splittime("34:00:00");
                        long usedtime = splittime(reserthr);
                        long lres = currntime - usedtime;//ll
                        //86400 24 hr
                        String hr = "";
//                        if (lres > 86400) {
//                            resetbalance.setText("" + printthirtysum(lres));
//                            txtthirtyremin.setText("" + printthirtysum(lres));
//
//                        } else {
                            resetbalance.setText("" + printsum(lres));
                            txtthirtyremin.setText("" + printsum(lres));
//                        }
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();
                        imgok.setEnabled(false);
                        imgok.setClickable(false);

                        pppcstatus = 2;
                        txthrtytatus.setText("ON");
                        txthrtytatus.setTextColor(Color.parseColor("#17BD17"));
                        txthrtytatus.startAnimation(animBlink);
                        txtresethr.setText("" + reserthr);
                        tenstatusonof.setText("OFF");//before ON
                        tenstatusonof.setTextColor(Color.parseColor("#E1000A"));
                        tenstatusonof.startAnimation(animBlink);
                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);
                    } else if (skonstatus.contentEquals("3")) {
                        boolactice=false;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        linbreak.setVisibility(View.VISIBLE);
                        linthirty.setVisibility(View.GONE);
                        lintenty.setVisibility(View.GONE);
                        linpvc.setVisibility(View.GONE);
                        resetbalance.setText("00:00 Hr");
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();
                        imgok.setEnabled(false);
                        imgok.setClickable(false);
                        lin_thirty.setEnabled(false);
                        lin_thirty.setClickable(false);

                        txthrtytatus.setText("OFF");//before ON
                        txthrtytatus.setTextColor(Color.parseColor("#E1000A"));
                        txthrtytatus.startAnimation(animBlink);

                        tenstatusonof.setText("OFF");//before ON
                        tenstatusonof.setTextColor(Color.parseColor("#E1000A"));
                        tenstatusonof.startAnimation(animBlink);

                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);


                        if (breakduration == null || breakduration.length() == 0 || breakduration.contentEquals("null") || breakduration.contentEquals("00:")) {
                            breakduration = "00:00";
                        }
                        pref.putString(Constant.BREAK_DURATION,""+breakduration);
                        longbduration = splittime("" + breakduration);
                        if(pref.getString(Constant.BREAK_LIVE_TIME) !=null &&pref.getString(Constant.BREAK_LIVE_TIME).contentEquals("closed"))
                        {
                            String breaklive="00:00:00";
                            longblive = splittime("" + breaklive);
                            pref.putString(Constant.BREAK_ACTIVATED_TIME,"");
                        }else{
                            String breaklive=""+pref.getString(Constant.BREAK_LIVE_TIME);
                            if(breaklive.contains("-"))
                            {
                                breaklive="00:00:00";
                                longblive = splittime("" + breaklive);
                                pref.putString(Constant.BREAK_ACTIVATED_TIME,"");
                                //newbreakrelease();
                            }else {
                                longblive = splittime("" + breaklive);
                            }
                        }
                        txtbduration.setText("" + breakduration);
                        //long lblive = longbduration - longblive;
                        if(longblive>longbduration)
                        {
                            txt_breakleft.setText("Time Left : 00:00");
                            setbreakduration("00:00");
                            //sddd
                        }else{
                            txt_breakleft.setText("Time Left : " + printsum(longblive));
                            long lbval=00;
                            lbval=longbduration-longblive;
                            // txt_duration.setText("" + printsum(lbval));
                            setbreakduration("" + printsum(lbval));
                        }

                        pppcstatus = 3;
                    } else if (skonstatus.contentEquals("4")) {

                        linbreak.setVisibility(View.GONE);
                        linthirty.setVisibility(View.GONE);
                        lintenty.setVisibility(View.VISIBLE);
                        linpvc.setVisibility(View.GONE);

                        if (tenresettime == null || tenresettime.length() == 0 || tenresettime.contentEquals("null")) {
                            tenresettime = "00:00:01";
                        }
                        imgok.setEnabled(false);
                        imgok.setClickable(false);
                        tenrset = splittime(tenresettime);
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();


                        txttenused.setText("" + tenresettime);
                        long currntime = splittime("10:00:00");

                        long usedtime = splittime(tenresettime);
                        tenrset = splittime(tenresettime);
                        long lres = currntime - usedtime;//ll 36000 86400

//                        if (lres > 36000) {
//                            txttenremaining.setText("" + printthirtysum(lres));
//                            txttenremain.setText("" + printthirtysum(lres));
//                        } else {
                            txttenremain.setText("" + printsum(lres));
                            txttenremaining.setText("" + printsum(lres));
//                        }

                        pppcstatus = 4;
//dk
                        boolactice=true;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        tenstatusonof.setText("ON");
                        tenstatusonof.setTextColor(Color.parseColor("#17BD17"));
                        tenstatusonof.startAnimation(animBlink);
                        tenstatusonof.bringToFront();

                        txthrtytatus.setText("OFF");//before ON
                        txthrtytatus.setTextColor(Color.parseColor("#E1000A"));
                        txthrtytatus.startAnimation(animBlink);

                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);

                    } else if (tenreserthr != null && !tenreserthr.contentEquals("00:00:00") && !tenreserthr.contentEquals("null")) {
                        linbreak.setVisibility(View.GONE);
                        linthirty.setVisibility(View.GONE);
                        lintenty.setVisibility(View.VISIBLE);
                        linpvc.setVisibility(View.GONE);

                        if (tenresettime == null || tenresettime.length() == 0 || tenresettime.contentEquals("null")) {
                            tenresettime = "00:00:01";
                        }

                        tenrset = splittime(tenresettime);
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_ENABLE);
                        callstatusdisable();
                        imgok.setEnabled(false);
                        imgok.setClickable(false);
                        txttenused.setText("" + tenresettime);
                        long currntime = splittime("10:00:00");

                        long usedtime = splittime(tenresettime);
                        tenrset = splittime(tenresettime);
                        long lres = currntime - usedtime;//ll 36000 86400

//                        if (lres > 36000) {
//                            txttenremaining.setText("" + printthirtysum(lres));
//                            txttenremain.setText("" + printthirtysum(lres));
//                        } else {
                            txttenremain.setText("" + printsum(lres));
                            txttenremaining.setText("" + printsum(lres));
//                        }
                        pppcstatus = 4;
                        boolactice=true;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        tenstatusonof.setText("ON");
                        tenstatusonof.setTextColor(Color.parseColor("#17BD17"));
                        tenstatusonof.startAnimation(animBlink);
                        tenstatusonof.bringToFront();

                        txthrtytatus.setText("OFF");//before ON
                        txthrtytatus.setTextColor(Color.parseColor("#E1000A"));
                        txthrtytatus.startAnimation(animBlink);

                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);

                    } else {
                        boolactice=false;
                        pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                        pppcstatus = 0;
                        txthrtytatus.setText("OFF");//before ON
                        txtresethr.setText("00:00 Hr");
                        resetbalance.setText("00:00 Hr");
                        txthrtytatus.setTextColor(Color.parseColor("#E1000A"));
                        imgok.setEnabled(true);
                        imgok.setClickable(true);
                        lin_thirty.setEnabled(true);
                        lin_thirty.setClickable(true);
                        linbreak.setVisibility(View.GONE);
                        linthirty.setVisibility(View.GONE);
                        lintenty.setVisibility(View.GONE);
                        linpvc.setVisibility(View.GONE);
                        //btnthirty.setEnabled(true);
                        // btnthirty.setClickable(true);
                        pref.putString(Constant.PC_STATUS, commonUtil.PC_DISABLE);
                        txtprcstatus.setText("OFF");//before ON
                        txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                        txtprcstatus.startAnimation(animBlink);
                        tenstatusonof.setText("OFF");//before ON
                        tenstatusonof.setTextColor(Color.parseColor("#E1000A"));
                        tenstatusonof.startAnimation(animBlink);
                        final int sdk = Build.VERSION.SDK_INT;
                        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                            imgok.setImageResource(R.drawable.prl);
                            // btnthirty.setImageResource(R.drawable.toff);

                        } else {
                            imgok.setImageResource(R.drawable.prl);
                            //btnthirty.setImageResource(R.drawable.toff);
                        }
                        callstatusenable();

                    }
                } else {
                    boolactice=false;
                    pref.putBolean(Constant.BLUETOOTH_FLAG,boolactice);
                    linbreak.setVisibility(View.GONE);
                    linthirty.setVisibility(View.GONE);
                    linpvc.setVisibility(View.GONE);
                    lintenty.setVisibility(View.GONE);
                    txthrtytatus.setText("OFF");//before ON
                    txtresethr.setText("00:00 Hr");
                    txthrtytatus.setTextColor(Color.parseColor("#E1000A"));
                    pref.putString(Constant.PC_STATUS, commonUtil.PC_DISABLE);
                    txtprcstatus.setText("OFF");//before ON
                    txtprcstatus.setTextColor(Color.parseColor("#E1000A"));
                    txtprcstatus.startAnimation(animBlink);
                    tenstatusonof.setText("OFF");//before ON
                    tenstatusonof.setTextColor(Color.parseColor("#E1000A"));
                    tenstatusonof.startAnimation(animBlink);
                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        imgok.setImageResource(R.drawable.prl);
                        // btnthirty.setImageResource(R.drawable.toff);

                    } else {
                        imgok.setImageResource(R.drawable.prl);
                        // btnthirty.setImageResource(R.drawable.toff);
                    }
                    imgok.setEnabled(true);
                    imgok.setClickable(true);
                    //btnthirty.setEnabled(true);
                    // btnthirty.setClickable(true);
                    callstatusenable();
                }
                // Log.e("cstatus", "" + cstatus);
                //  stkstatus=cstatus;
                if (cstatus == null || cstatus.length() == 0 || cstatus.contentEquals("null")) {
                    cstatus = "";
                }
                txt_curnetstaus.setText("STATUS : " + cstatus);
                //1
                if (oftotal != null && oftotal.length() > 0 && !oftotal.contentEquals("null")) {
                } else {
                    oftotal = "00:00";
                }
                long otime = splittime(oftotal);
                strlofftime = otime;
              //  txtoffromtime.setText(gt.getofftime());
                txtoffromtime.setText(converttwelvehr(gt.getofftime()));
                String of_status = gt.getofstatus();
                if (of_status != null && of_status.length() > 0 && !of_status.contentEquals("null")) {
                    if (of_status.contentEquals("0")) {
                        intschedule = STATUS_OFF_DUTY;
                        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        stofftime = formatdatetime.format(new Date());

                        //pref.putString(Constant.SHIPPING_NO_ACTIVE,"deactive");
                        //stofftime = strsl + " " + gt.getofftime();
                        setyellow();
                        txtoffdutytotal.setText(oftotal);
                        txtoftotime.setText("");
                    } else {
                        //  txtoftotime.setText(gt.getofttime());
                        txtoftotime.setText(converttwelvehr(gt.getofttime()));
                        txtoffdutytotal.setText(oftotal);
                    }
                } else {
                    txtoftotime.setText("00:00");
                    txtoffdutytotal.setText("00:00");
                }


                String on_status = gt.getonstatus();
                if (on_status != null && on_status.length() > 0 && !on_status.contentEquals("null")) {
                    // Log.e("on_status","#"+on_status);
                    // Log.e("cstatus","#"+cstatus);
                    if (on_status.contentEquals("0") && cstatus.contentEquals("ON_DUTY")) {
                        intschedule = STATUS_ON_DUTY;
                        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        stontime = formatdatetime.format(new Date());
                        //pref.putString(Constant.SHIPPING_NO_ACTIVE,"deactive");
                      //  stontime = strsl + " " + gt.getonftime();
                      //  pref.putString(Constant.C_ONDUTY_TIME,""+ strsl + " " + gt.getonftime());
                        setyellow();

                        //  String a = printDifference(stontime,strsl+" "+strdtime);

                        txtondutytotal.setText(ontotal);
                        txtontotime.setText("");
                    } else {
                        txtontotime.setText(gt.getonttime());
                        txtondutytotal.setText(ontotal);
                    }
                } else {
                    txtontotime.setText("00:00");
                    txtondutytotal.setText("00:00");
                }
                //3

                if (drtotal != null && drtotal.length() > 0 && !drtotal.contentEquals("null")) {
                } else {
                    drtotal = "00:00";
                }
                long drtime = splittime(drtotal);
                strldrivetime = drtime;
                String skdrv = "" + printsum(strldrivetime);
                txtdrivetotal.setText(skdrv);
                long optimek = splittime(statedrive);

                long opnewtimek = optimek - strldrivetime;
                String opkk = printsum(opnewtimek);
                if (opkk.contains("-")) {
                    txtdriveleft.setText("00:00");
                } else {
                    txtdriveleft.setText("" + opkk + "" + " Hr");
                }

                String dr_status = gt.getdrstatus();
                txtdrivefromtime.setText(gt.getdrftime());

                //2 *****************on duty
                if (ontotal != null && ontotal.length() > 0 && !ontotal.contentEquals("null")) {
                } else {
                    ontotal = "00:00";
                }
                long ontime = splittime(ontotal);
                strlontime = ontime;
                String skon = "" + printsum(strlontime);
                txtondutytotal.setText(skon);
                long optime = splittime(stateonduty);
                long opnewtime = optime - strlontime - strldrivetime;
                String opk = printsum(opnewtime);
                if (opk.contains("-")) {
                    txtondutyleft.setText("00:00");
                } else {
                    txtondutyleft.setText("" + opk + "" + " Hr");
                }
                txtonfromtime.setText(gt.getonftime());


                if (dr_status != null && dr_status.length() > 0 && !dr_status.contentEquals("null")) {
                    if (dr_status.contentEquals("0") && cstatus.contentEquals("DRIVING")) {
                        {


                            intschedule = STATUS_DRIVING;
                            //stdrivetime = strsl + " " + gt.getdrftime();
                            SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            stdrivetime = formatdatetime.format(new Date());

                            setyellow();

                            txtdrivetotal.setText(drtotal);
                            txtdrivetotime.setText("");
//                            if(pref.getString(Constant.SHIPPING_NO_ACTIVE).contentEquals("deactive"))
//                            {
//                                responsetrip(gt.getdrid());
//                                pref.putString(Constant.SHIPPING_NO_ACTIVE,"active");
//                            }

                        }
                    } else {
                        txtdrivetotal.setText(drtotal);
                        txtdrivetotime.setText(gt.getdrttime());
                    }
                } else {
                    txtdrivetotal.setText("00:00");
                    txtdrivetotime.setText("00:00");
                }
                //4


                if (sltotal != null && sltotal.length() > 0 && !sltotal.contentEquals("null")) {
                } else {
                    sltotal = "00:00";
                }
                long sltime = splittime(sltotal);
                // Log.e("sltime", "" +sltime);
                oldsleeptotal = splittime(soldsleep);
                // Log.e("oldsleeptotal", "" +oldsleeptotal);
                strlsleptime = sltime + oldsleeptotal;
                //Log.e("strlsleptime", "" +strlsleptime);
               // txtsleepfromtime.setText(gt.getslftime());

                //new change 12 hr
                txtsleepfromtime.setText(converttwelvehr(gt.getslftime()));


                String sl_status = gt.getslstatus();
                // Log.e("sl_status", "" + gt.getslstatus());
                //Log.e("cstatus", "##" + cstatus);
                if (sl_status != null && sl_status.length() > 0 && !sl_status.contentEquals("null")) {
                    if (sl_status.contentEquals("0") && cstatus.contentEquals("SLEEP")) {
                        intschedule = STATUS_SLEEPER;
                        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        stsleeptime = formatdatetime.format(new Date());

                        //  stsleeptime = strsl + " " + gt.getslftime();
                        // Log.e("stsleeptime", "" + stsleeptime);
                        setyellow();//vv
                        //txtsleeptotal.setText(sltotal);
                        txtsleeptotal.setText(printsum(strlsleptime));
                        txtsleeptotime.setText("");// txtsleeptotal.setText(a);
                        //pref.putString(Constant.SHIPPING_NO_ACTIVE,"deactive");
                    } else {
                        // Log.e("sltotalxd", "" + sltotal);
                        // txtsleeptotal.setText(sltotal);
                        txtsleeptotal.setText(printsum(strlsleptime));
                       // txtsleeptotime.setText(gt.getslttime());
                        txtsleeptotime.setText(converttwelvehr(gt.getslttime()));
                    }
                } else {
                    txtsleeptotal.setText("00:00");
                }
                try {
                    long optimezzz = splittime(stateonduty);
                    //Log.e("strlontime","#"+strlontime);
                    // Log.e("strldrivetime","#"+strldrivetime);
                    long opnewtimezzz = optimezzz - strlontime - strldrivetime;
                    String opkzzz = printsum(opnewtimezzz);
                    //Log.e("opkopkopk","#"+opk);
                    if (opkzzz.contains("-")) {
                        txtondutyleft.setText("00:00");
                    } else {
                        txtondutyleft.setText("" + opkzzz + "" + " Hr");
                    }


                    long optimeddd = splittime(statedrive);

                    long opnewtimekddd = optimeddd - strldrivetime;
                    String opkkddd = printsum(opnewtimekddd);
                    if (opkkddd.contains("-")) {
                        txtdriveleft.setText("00:00");
                    } else {
                        txtdriveleft.setText("" + opkkddd + "" + " Hr");
                    }


                } catch (Exception e) {

                }

                try{
                    //Log.e("pppcstatus","&"+pppcstatus);
                    if(pppcstatus==3 || pppcstatus==1  || pppcstatus==4 || pppcstatus==2)
                    {
                        imgbreaktake.setEnabled(false);
                        imgbreaktake.setAlpha(0.40f);
                    }else{
                        if(intschedule==STATUS_DRIVING)
                        {
                            imgbreaktake.setEnabled(false);
                            imgbreaktake.setAlpha(0.40f);
                        }else {
                            imgbreaktake.setEnabled(true);
                            imgbreaktake.setAlpha(1.00f);
                        }
                    }

                }catch (Exception e)
                {

                }
                apicall = 0;

            }
        }

    }



    private void gettodayvalues(String str_vin, String vald) {

       // defaultcall = 1;
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
        cancelprogresssdialog();
        String did = pref.getString(Constant.DRIVER_ID);
        //Log.e("did",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
//        Log.e("refreshcall", "http://eld.e-logbook.info/elog_app/eld_getlogvalues.php?did=" + did +
//                "&vin=" + vinnumber +
//                "&addres=s" + straddress +
//                "&versiion=" + olddversion +
//                "&lat=" + lat +
//                "&lon=" + lon +
//                "&version_name=" + olddversionname+"&state="+strstate);
        Call<List<Getvalue_model>> call = api.getValues_eld(did, str_vin, straddress, olddversion, lat, lon, olddversionname, strstate,timezonesname,timezonesid,strvalx,"bluetooth","");

        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if (response.isSuccessful()) {
                    //dd
                    movies = response.body();
                    settodayval(movies, vald);
                    cancelprogresssdialog();
                    if(mdk==0)
                    {
                        mdk=1;
                        getbreakrefresh();
                    }else {
                        savestate("");
                    }


                } else {
                    cancelprogresssdialog();
                    //  getvehicle();
                    callnetworkDialog();
                    // Log.e("ss"," Response "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Getvalue_model>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                // getvehicle();
                callnetworkDialog();
            }
        });
    }




    private void registerGCM() {
        //Log.e("cl","registergcm");
        try {
            Intent intent = new Intent(this, Dispatch_GcmIntentService.class);
            intent.putExtra("key", "register");
            startService(intent);
        }catch (Exception e)
        {

        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    //temporarly blocked for switch purpose
    private void remarkback(String status) {
        if (dialogrk != null) {
            if (dialogrk.isShowing()) {
                dialogrk.dismiss();
            }
        }
        //   String status=currentstatus;
        String sdkr = "";
        try {
           // Uri alarmSound = null;
            if (status.contentEquals("ON DUTY")) {
                if (pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    sdkr = "ON DUTY";
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
                }
            } else if (status.contentEquals("ON_DUTY")) {
                if (pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                    sdkr = "ON DUTY";
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
                }
            } else if (status.contentEquals("DRIVING")) {
                if (pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                    sdkr = "DRIVING";
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/drivv");
                }
            } else if (status.contentEquals("SLEEP")) {
                if (pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {

                    sdkr = "SLEEP";
                    //Log.e("statusssskk",""+status);
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
                }
            } else if (status.contentEquals("SLEEPER")) {
                if (pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                    sdkr = "SLEEP";
                    //Log.e("statusssskk",""+status);
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
                }
            } else if (status.contentEquals("OFF DUTY")) {
                if (pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                    sdkr = "OFF DUTY";
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
                }
            } else if (status.contentEquals("OFF_DUTY")) {
                if (pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                    sdkr = "OFF DUTY";
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
                }
            }
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
//            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = this.getLayoutInflater();
        //lq
        // final View dialogView = inflater.inflate(R.layout.remark_popup_new, null);
        final View dialogView = inflater.inflate(R.layout.remark_popup_dd, null);

        name = dialogView.findViewById(R.id.rmark);
        name.setHint("Enter reason for " + sdkr);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        btnSpeak = dialogView.findViewById(R.id.btnspeak);
        final Button btnthirtyhr = dialogView.findViewById(R.id.btn_thr);
        // btnthirtyhr.setVisibility(View.GONE);
        final LinearLayout linbrk = dialogView.findViewById(R.id.linbreak);
        if (status != null && status.length() > 0 && !status.contentEquals("null")) {
            if (status.contentEquals("DRIVING")) {
                linbrk.setVisibility(View.GONE);
            }
        }

        final Button btntea = dialogView.findViewById(R.id.btn_tea);
        final Button btnlunch = dialogView.findViewById(R.id.btn_lunch);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogrk = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        if (status != null && status.length() > 0 && !status.contentEquals("null")) {
            if (status.contentEquals("ON_DUTY")) {
                status = "ON DUTY";
            } else if (status.contentEquals("DRIVING")) {
                status = "DRIVING";
            } else if (status.contentEquals("SLEEP")) {
                status = "SLEEP";
            } else if (status.contentEquals("OFF_DUTY")) {
                status = "OFF DUTY";
                // status="DRIVING";
            } else {
                status = "DRIVING";
            }
        } else {
            status = "OFF DUTY";
        }
        if (status.contentEquals("ON DUTY")) {
            imgstatus.setBackgroundResource(R.drawable.timeimg);
        } else if (status.contentEquals("DRIVING")) {
            imgstatus.setBackgroundResource(R.drawable.driveimg);
        } else if (status.contentEquals("SLEEPER")) {
            imgstatus.setBackgroundResource(R.drawable.sleepimg);
        } else if (status.contentEquals("SLEEP")) {
            imgstatus.setBackgroundResource(R.drawable.sleepimg);
        } else if (status.contentEquals("OFF DUTY")) {
            imgstatus.setBackgroundResource(R.drawable.offimg);
        }
        txtstatus.setText("STATUS : " + status);
        dialogrk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrk.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrk.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogrk.getWindow().setAttributes(lp);
        if ((status.contentEquals("ON DUTY") || status.contentEquals("ON_DUTY")) && pref.getString(Constant.VOICE_ON).contentEquals("1")) {
            dialogrk.show();
        } else if ((status.contentEquals("DRIVING") || status.contentEquals("DRIVE")) && pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
            dialogrk.show();
        } else if ((status.contentEquals("OFF_DUTY") || status.contentEquals("OFF DUTY")) && pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
            dialogrk.show();
        } else if ((status.contentEquals("SLEEPER") || status.contentEquals("SLEEP")) && pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
            dialogrk.show();
        }
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        btnthirtyhr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breaktype = "THIRTY MINUTES";
                btntea.setBackgroundColor(getResources().getColor(R.color.orange));
                btnlunch.setBackgroundColor(getResources().getColor(R.color.ts));
                name.setText("34 HR RESET");
                if (OnlineCheck.isOnline(context)) {
                    dialogrk.dismiss();
                    // gettodaysavethirtyreset(commonUtil.OFF_DUTY, intschedule, "2");
                }

            }
        });

        btntea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breaktype = "TEA";
                btntea.setBackgroundColor(getResources().getColor(R.color.orange));
                btnlunch.setBackgroundColor(getResources().getColor(R.color.ts));
                name.setText("Take 10 minutes break...");
                dialogrk.dismiss();
                String did = pref.getString(Constant.DRIVER_ID);
                // Log.e("did",""+did);
                String fld = txt_curnetstaus.getText().toString().trim();
                StringTokenizer sdkg = new StringTokenizer(fld, ":");
                String a = sdkg.nextToken();
                String field = "";
                if (sdkg.hasMoreTokens()) {
                    field = "" + sdkg.nextToken();
                    strfield = field.trim();
                }
                breakevent = 0;
                acceptbreak(10,"manual","0");


            }
        });
        btnlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breaktype = "LUNCH";
                btntea.setBackgroundColor(getResources().getColor(R.color.ts));
                btnlunch.setBackgroundColor(getResources().getColor(R.color.orange));

                name.setText("Take 30 minutes break...");

                String did = pref.getString(Constant.DRIVER_ID);
                dialogrk.dismiss();
                breakevent = 1;
                acceptbreak(30,"manual","0");


            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressdlog = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                String did = pref.getString(Constant.DRIVER_ID);
                // Log.e("did",""+did);
                String fld = txt_curnetstaus.getText().toString().trim();
                StringTokenizer sdkg = new StringTokenizer(fld, ":");
                String a = sdkg.nextToken();
                String field = "";
                if (sdkg.hasMoreTokens()) {
                    field = "" + sdkg.nextToken();
                    strfield = field.trim();
                }

                //  Log.e("breaktype",""+breaktype);
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
                Call<List<Remark_model>> call = api.saveremark(vinnumber, field, lat, lon, did, "" + name.getText().toString().trim(), breaktype);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        if (response.isSuccessful()) {
                            //Log.e("field",""+strfield);
                            responseremark(strfield);
                            dialogrk.dismiss();
                            cancelprogresssdialog();
                        } else {

                            cancelprogresssdialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                        //Log.e("dd"," Response Error "+t.getMessage());
                        cancelprogresssdialog();

                    }
                });

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrk.dismiss();

                // setdistancealert();
                //setondutyalert();
            }
        });

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void setdistancealert(final String mils) {
        if (dialogdistance != null) {
            if (dialogdistance.isShowing()) {
                dialogdistance.dismiss();
            }
        }
        Double reslt = 00.00;
        if (mils != null && mils.length() > 0 && !mils.contentEquals("null")) {
            Double dk = Double.parseDouble(mils);
            reslt = roundTwoDecimals(dk);
        }

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.distance_notification, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogdistance = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        String name = pref.getString(Constant.MODEL_NAME);

        txtstatus.setText("Your Vehicle " + name + " distance  " + reslt + " Miles ");
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
        } catch (Exception e) {
            //  Log.e("err",""+e.toString());
        }
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = pref.getString(Constant.DRIVER_ID);
                String type = "DISTANCE";
                dialogdistance.dismiss();

                api = ApiServiceGenerator.createService(Eld_api.class);
                //    Log.e("url","&did="+did+"&name="+type);
                Call<List<Remark_model>> call = api.updatenotification(did, "" + type);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        if (response.isSuccessful()) {

                            // dialogdistance.dismiss();
                        } else {

                            //dialogdistance.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                        //Log.e("dd"," Response Error "+t.getMessage());
                        dialogdistance.dismiss();
//setdistancealert(mils);
                    }
                });

            }
        });


    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    private void setondutyalert() {
        if (dialoonduty != null) {
            if (dialoonduty.isShowing()) {
                dialoonduty.dismiss();
            }
        }

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.onduty_notification, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialoonduty = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("Your's today's ONDUTY total hour's exceeded limit");
        dialoonduty.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoonduty.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoonduty.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialoonduty.getWindow().setAttributes(lp);
        dialoonduty.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = pref.getString(Constant.DRIVER_ID);

                dialoonduty.dismiss();
                String type = "ON_DUTY";
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
                Call<List<Remark_model>> call = api.updatenotification(did, "" + type);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        if (response.isSuccessful()) {
                            // dialoonduty.dismiss();

                        } else {

                            // dialoonduty.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                        dialoonduty.dismiss();
                        //   Log.e("ded"," Response Error "+t.getMessage());

//setondutyalert();
                    }
                });

            }
        });


    }
    private void federallawalert() {
        //tm

        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/fed");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dialogfederal != null) {
            if (dialogfederal.isShowing()) {
                dialogfederal.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.federal_notification, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
       dialogfederal = new Dialog(context, R.style.DialogTheme);
        dialogfederal.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("Hi there! we just noticed that you crossed your home state. Please note that Federal rules will be applied now");

        dialogfederal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfederal.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogfederal.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogfederal.getWindow().setAttributes(lp);
        dialogfederal.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfederal.dismiss();
               // gettodayvalues(str_vin);
            }
        });


    }
    //low_voltage
    private void setlowvoltage() {
        //tm
        if (dialoglowvoltage != null) {
            if (dialoglowvoltage.isShowing()) {
                dialoglowvoltage.dismiss();
            }
        }

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.lowvoltage_notification, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialoglowvoltage = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("STABLE/ This device may be unplugged");
        dialoglowvoltage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglowvoltage.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogdrive.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialoglowvoltage.getWindow().setAttributes(lp);
        dialoglowvoltage.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialoglowvoltage.dismiss();

            }
        });


    }
    //drive
    private void setdrivealert() {
        //tm
        if (dialogdrive != null) {
            if (dialogdrive.isShowing()) {
                dialogdrive.dismiss();
            }
        }

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.driving_notification, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogdrive = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("Your's today's DRIVE total hour's exceeded limit");
        dialogdrive.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdrive.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogdrive.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogdrive.getWindow().setAttributes(lp);
        dialogdrive.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = pref.getString(Constant.DRIVER_ID);

                dialogdrive.dismiss();
                String type = "ON_DUTY";
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","vin="+vinnumber+"&fname="+field+"&lat="+lat+"&lon="+lon+"&did="+did+"&name="+name.getText().toString().trim());
                Call<List<Remark_model>> call = api.updatenotification(did, "" + type);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        if (response.isSuccessful()) {
                            // dialoonduty.dismiss();

                        } else {

                            // dialoonduty.dismiss();
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

    private void responseremark(String status) {
        if (dialogresp != null) {
            if (dialogresp.isShowing()) {
                dialogresp.dismiss();
            }
        }
//Log.e("statuss",""+status);
        // Log.e("statusssize",""+status.length());
        String val = "";
        if (status.contentEquals("OFF_DUTY")) {
            // Log.e("level",""+"OFF_DUTY");

            val = "Nice work. Get some rest please. Thank you";
            if (pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
                setalaramtatu("offresp");
            }

        } else if (status.contentEquals("SLEEP")) {
            val = "Get some Rest";
            if (pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
                setalaramtatu("sleepresp");
            }
        } else if (status.contentEquals("DRIVING")) {
            val = "Have a safe Drive";
            if (pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
                setalaramtatu("driveresp");
            }
        } else if (status.contentEquals("ON_DUTY")) {
            val = "Have a Nice day";
            if (pref.getString(Constant.VOICE_ON).contentEquals("1")) {
                setalaramtatu("onresp");
            }
        }


        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remark_response, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        final TextView txtlevel = dialogView.findViewById(R.id.txt_level);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogresp = new Dialog(context, R.style.DialogTheme);
        txtlevel.setText("STATUS : " + status);
        txtstatus.setText("" + val);
        dialogresp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogresp.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogresp.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogresp.getWindow().setAttributes(lp);
        if ((status.contentEquals("ON DUTY") || status.contentEquals("ON_DUTY")) && pref.getString(Constant.VOICE_ON).contentEquals("1")) {
            dialogresp.show();
        } else if ((status.contentEquals("DRIVING") || status.contentEquals("DRIVE")) && pref.getString(Constant.VOICE_DRIVE).contentEquals("1")) {
            dialogresp.show();
        } else if ((status.contentEquals("OFF_DUTY") || status.contentEquals("OFF DUTY")) && pref.getString(Constant.VOICE_OFF).contentEquals("1")) {
            dialogresp.show();
        } else if ((status.contentEquals("SLEEPER") || status.contentEquals("SLEEP")) && pref.getString(Constant.VOICE_SLEEP).contentEquals("1")) {
            dialogresp.show();
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogresp.dismiss();
            }
        });


    }

    private void setalaramtatu(String voice) {
        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/" + voice);

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getversionname() {
        try {
            straname = "" + listvals.get(Build.VERSION.SDK_INT);
        }catch (Exception e)
        {

        }

    }


    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL("http://xxx.xxx.xxx/image.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                // Log.e("err",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            imgprofile.setImageBitmap(result);
        }
    }

    private void store(Bitmap imageToSave, String fileName) {

//    File mFile = new File(Environment.getExternalStoragePublicDirectory(
//            Environment.DIRECTORY_PICTURES) + "/FuelBill");

        File mFile = new File(Environment.getExternalStorageDirectory() + File.separator + "/FuelBill");
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        fileShare = new File(Environment.getExternalStorageDirectory() + File.separator + "/FuelBill", fileName + System.currentTimeMillis() + ".jpg");
        if (fileShare.exists()) {
            fileShare.delete();
        }

        //trying to show up in gallery
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    fileShare.getAbsolutePath(), fileShare.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(fileShare);
            // Bitmap last = ProcessingBitmap(imageToSave);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //SaveImage(MainActivity.this, imageToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //updating mSeekBar
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (mediaPlayer != null) {
//
//                int mCurrentPosition = mediaPlayer.getCurrentPosition();
//                mSeekBar.setProgress(mCurrentPosition);
//
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
//                        - TimeUnit.MINUTES.toSeconds(minutes);
//                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));
//
//                updateSeekBar();
//            }
//        }
//    };
//
//    private void updateSeekBar() {
//        mHandler.postDelayed(mRunnable, 1000);
//    }
//
//    private void prepareMediaPlayerFromPoint(int progress) {
//        //set mediaPlayer to start from middle of the audio file
//
//        mediaPlayer = new MediaPlayer();
//
//        try {
//            mediaPlayer.setDataSource(strpath);
//            mediaPlayer.prepare();
//            mSeekBar.setMax(mediaPlayer.getDuration());
//            mediaPlayer.seekTo(progress);
//
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    stopPlaying();
//                }
//            });
//
//        } catch (IOException e) {
//            //Log.e("err", "prepare() failed");
//        }
//
//        //keep screen on while playing audio
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }
//
//    private void onPlay(boolean isPlaying) {
//        if (!isPlaying) {
//            //currently MediaPlayer is not playing audio
//            if (mediaPlayer == null) {
//                startPlaying(); //start from beginning
//            } else {
//                resumePlaying(); //resume the currently paused MediaPlayer
//            }
//
//        } else {
//            //pause the MediaPlayer
//            pausePlaying();
//        }
//    }
//
//    private void pausePlaying() {
//        //btn_yes.setTextColor(Color.parseColor("#a9a9a9"));
//        //mPlayButton.setImageResource(R.drawable.ic_media_play);
//        mHandler.removeCallbacks(mRunnable);
//        mediaPlayer.pause();
//    }
//
//    private void resumePlaying() {
//        btn_play.setTextColor(Color.parseColor("#a9a9a9"));
//        //  mPlayButton.setImageResource(R.drawable.ic_media_pause);
//        mHandler.removeCallbacks(mRunnable);
//        mediaPlayer.start();
//        updateSeekBar();
//    }
//
//    private void stopPlaying() {
//        //  mPlayButton.setImageResource(R.drawable.ic_media_play);
//        mHandler.removeCallbacks(mRunnable);
//        mediaPlayer.stop();
//        mediaPlayer.reset();
//        mediaPlayer.release();
//        mediaPlayer = null;
//
//        mSeekBar.setProgress(mSeekBar.getMax());
//        isPlaying = !isPlaying;
//
//        mCurrentProgressTextView.setText(mFileLengthTextView.getText());
//        mSeekBar.setProgress(mSeekBar.getMax());
//
//        //allow the screen to turn off again once audio is finished playing
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }
//
//    private void startPlaying() {
//        // mPlayButton.setImageResource(R.drawable.ic_media_pause);
//        btn_play.setTextColor(Color.parseColor("#a9a9a9"));
//        mediaPlayer = new MediaPlayer();
//
//        try {
//            // Log.e("ritem path",""+strpath);
//            mediaPlayer.setDataSource(strpath);
//            mediaPlayer.prepare();
//            mSeekBar.setMax(mediaPlayer.getDuration());
//
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mediaPlayer.start();
//                }
//            });
//        } catch (IOException e) {
//            //Log.e("dd", "prepare() failed");
//        }
//
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mSeekBar.setVisibility(View.GONE);
//                mCurrentProgressTextView.setVisibility(View.GONE);
//                mFileLengthTextView.setVisibility(View.GONE);
//                myChronometer.setVisibility(View.VISIBLE);
//                btn_play.setTextColor(Color.parseColor("#17BD17"));
//                stopPlaying();
//            }
//        });
//
//        updateSeekBar();
//
//        //keep screen on while playing audio
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }

    private void callunpluggeddialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.unplugg, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        dialogunplug = new Dialog(context, R.style.DialogTheme);
        dialogunplug.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogunplug.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogunplug.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogunplug.getWindow().setAttributes(lp);
        dialogunplug.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogunplug.dismiss();
            }
        });
    }

    private void callappupdate(String message) {
        View view = View.inflate(context, R.layout.app_update, null);
        TextView tv = (TextView) view.findViewById(R.id.tno);
        TextView btntv = (TextView) view.findViewById(R.id.btnone);
        tv.setText(Html.fromHtml(message));
        final Dialog dialogupdate = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialogupdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogupdate.setContentView(view);
        dialogupdate.show();

        btntv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialogupdate.dismiss();
                pref.putString(Constant.APP_AUTO_UPDATE, "0");
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


    }

    private void callversionupdate() {

        View view = View.inflate(context, R.layout.update_version_dialog, null);
        TextView txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setText("" + "Please update new version");
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
                final String appPackageName = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));

            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogmk.dismiss();
            }
        });
    }//calllocationupdate
    private void callvehicleoffdialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.second_vehicle_dialog, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        dialogvehicleoff = new Dialog(context, R.style.DialogTheme);
        dialogvehicleoff.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogvehicleoff.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogvehicleoff.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogvehicleoff.getWindow().setAttributes(lp);
        dialogvehicleoff.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogvehicleoff.dismiss();
                vdk = 1;
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
                        pref.putString(Constant.CURRENT_STATE, "" + strstate);
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
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }

checkstate();

        return straddress;
    }
private void checkstate()
{
    //Log.e("call","checkstate");

    String statec = pref.getString(Constant.STATE_FIELD);

    try {
        if (pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active") ) {
            txtcstate.setVisibility(View.VISIBLE);
            txtcstate.setText("Current state : " + strstate);
            txtrules.setText("Federal rules applied");
        } else {
            if (strstate.contentEquals(statec)) {

            } else {
//
                if (intstate == 0 && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("deactive")) {
                    String str = pref.getString(Constant.LOGIN_CHECK);
                    if (str != null && str.length() > 0 && !str.contentEquals("null")) {
                        if (str.equalsIgnoreCase("logged_inn")) {
                            statechangedalert();
                        }
                    }
                } else {

                }
//Log.e("intstateintstate",""+intstate);
            }
        }
    } catch (Exception e) {

    }

}
    private void statechangedalert() {
        //tm
        txtcstate.setVisibility(View.VISIBLE);
        txtcstate.setText("Current state : " + strstate);
        txtrules.setText("Federal rules applied");
        statedrive = "" + pref.getString(Constant.FEDERAL_DRIVE_HOURS);
        stateonduty = "" + pref.getString(Constant.FEDERAL_ONDUTY_HOURS);
        pref.putString(Constant.FEDERAL_DRIVE_ACTIVE, "active");
        txtonallowed.setText(stateonduty + " Hr");
        txtdriveallowed.setText(statedrive + " Hr");

        long optime = splittime(stateonduty);
        //Log.e("strlontime","#"+strlontime);
        // Log.e("strldrivetime","#"+strldrivetime);
        long opnewtime = optime - strlontime - strldrivetime;
        String opk = printsum(opnewtime);
        //Log.e("opkopkopk","#"+opk);
        if (opk.contains("-")) {
            txtondutyleft.setText("00:00");
        } else {
            txtondutyleft.setText("" + opk + "" + " Hr");
        }


        long optimek = splittime(statedrive);

        long opnewtimek = optimek - strldrivetime;
        String opkk = printsum(opnewtimek);
        if (opkk.contains("-")) {
            txtdriveleft.setText("00:00");
        } else {
            txtdriveleft.setText("" + opkk + "" + " Hr");
        }


        savestate(strstate);


        updateTimeStringsTimerWorker();
        intstate = 1;
        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/fed");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dialogfederal != null) {
            if (dialogfederal.isShowing()) {
                dialogfederal.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.federal_notification, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogfederal = new Dialog(context, R.style.DialogTheme);
        dialogfederal.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("Hi there! we just noticed that you crossed your home state. Please note that Federal rules will be applied now");
        dialogfederal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfederal.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogfederal.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogfederal.getWindow().setAttributes(lp);
        dialogfederal.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfederal.dismiss();

            }
        });


    }

    private void setlogoutalert()
    {
        Trucksoft_elog_Notify_Utils.clearNotifications(context);
        if (dialoglogi != null) {
            if (dialoglogi.isShowing()) {
                dialoglogi.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.logout_alert, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialoglogi = new Dialog(context, R.style.DialogTheme);
        dialoglogi.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText("BY LOGIN OFF, YOUR LOGBOOK WILL NOT BE RECORDED");
        dialoglogi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglogi.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoglogi.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialoglogi.getWindow().setAttributes(lp);
        dialoglogi.show();
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoglogi.dismiss();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoglogi.dismiss();

               // savebluetoothstatus("0","0");
                logout_okk();
            }
        });

    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
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


    private void getime()
    {
        try {
            String androidId ="";
            imenumber1 = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
           // Log.e("androidId", "@" + androidId);
        }catch (Exception e)
        {

        }
    }

    public static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)){
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac==null){
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length()>0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
    private void cancelprogresssdialog()
    {
        try {
            if ((progressdlog != null) && progressdlog.isShowing()) {
                progressdlog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
           // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
           // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            progressdlog = null;
        }
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
private void deleteappfolder()
{
    try {
        File dir = new File(Environment.getExternalStorageDirectory().getPath());
       // Log.e("dir1", "" + dir);
        if (dir.isDirectory()) {
           // Log.e("dir2", "" + dir);
            String[] children = dir.list();
           // Log.e("children", "" + children);
            for (int i = 0; i < children.length; i++) {
               // Log.e("children[i]", "" + children[i]);
                if (children[i].contains("e-logbook")) {
                   // Log.e("vds", "" + children[i]);
                    File fk = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + children[i]);
                    //fk.delete();
                    deleteDirectory(fk);
                    // new File(dir, children[i]).delete();
                }

            }
        }
    }catch (Exception e){

    }

}

    private void getMyLocation(){
       // Log.e("mGoogleApiClient@@@",""+mGoogleApiClient);

      //  Log.e("mGoogleApiClient333@@@",""+mGoogleApiClient);

        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(Home_activity_bluetooth.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                   // Log.e("permission","sucess");
                    mLastLocation =                     LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
                                            .checkSelfPermission(Home_activity_bluetooth.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mLastLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                   // Log.e("calling",""+"gps fail");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(Home_activity_bluetooth.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                   // Log.e("calling",""+"gps unavail");
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
    private void savestate(String address) {
        if(lat !=null && lat.length()>0) {
            String gp_status = "0";
            try {
                CheckGpsStatus();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Do something for lollipop and above versions
                    if (!GpsStatus) {
                        gp_status = "1";
                        // turnGPSOn();activate gps
                        //  getMyLocation();
                    } else {
                        gp_status = "0";
                    }
                } else {
                    gp_status = "0";
                    // do something for phones running an SDK before lollipop
                }
            } catch (Exception e) {

            }
            String svadd = "";
            if (address != null && strstate != null && strstate.length() > 0) {
                svadd = pref.getString(Constant.STATE_FIELD);
            }
            String vin = pref.getString(Constant.VIN_NUMBER);
            // Log.e("dccc","@"+dc);2019-01-14
            Call<List<Faultcode_model>> call = api.savestates(pref.getString(Constant.DRIVER_ID), strstate, vin, "" + svadd, "" + address, "" + lat, "" + lon, "" + straddress, gp_status);
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
        }else{
            if(mdk==0)
            {
                mdk=1;
                getbreakrefresh();
            }
        }
    }


private void calleception()
{
    String did = pref.getString(Constant.DRIVER_ID);
   // Log.e("did",""+did);
    progressdlog = new ProgressDialog(context,
            AlertDialog.THEME_HOLO_LIGHT);
    progressdlog.setMessage("Please wait...");
    progressdlog.setCancelable(false);
    progressdlog.show();
    api = ApiServiceGenerator.createService(Eld_api.class);
    Call<List<MainException_model>> call = api.getexception(did,""+lat,""+lon,""+straddress,""+strstate,"Exceptions");

    call.enqueue(new Callback<List<MainException_model>>() {
        @Override
        public void onResponse(Call<List<MainException_model>> call, Response<List<MainException_model>> response) {
            if (response.isSuccessful()) {
                cancelprogresssdialog();
                List<MainException_model> lvm = response.body();
                for(int j=0;j<lvm.size();j++)
                {
                    MainException_model mv=new MainException_model();
                    mv=lvm.get(j);
                    //Log.e("result@@@",""+mv.result);
                    String rse=mv.reason;
                   // Log.e("rse@@@",""+rse);
                    callexceptionval(mv.result,rse);
                }
              //  Log.e("result","success");

            } else {
               // Log.e("result","fail");
                cancelprogresssdialog();
                Toast.makeText(context, "Please try again!" , Toast.LENGTH_SHORT).show();


            }
        }

        @Override
        public void onFailure(Call<List<MainException_model>> call, Throwable t) {
           // Log.e("dd"," Response Error "+t.getMessage());
            cancelprogresssdialog();
            Toast.makeText(context, "Please try again!" , Toast.LENGTH_SHORT).show();


        }
    });
}
    private void callexceptionval(List<Exception_model> listex,String reasss)
    {arrayexcept=new ArrayList<>();
        listmovies=new ArrayList<>();
        final Dialog dialogex = new Dialog(context);
        dialogex.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogex.setCancelable(false);
        //dialogex.setTitle("Exception Detail");
        dialogex.setContentView(R.layout.eceptionlist);
//        Button dialogsubmit =dialogex.findViewById(R.id.btn_submit);
//        Button dialogcancel =dialogex.findViewById(R.id.btn_cancel);
        TextView tstatew=dialogex.findViewById(R.id.tstateez);
        TextView tclose=dialogex.findViewById(R.id.tclose);
        tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        tstatew.setText(" Current State : "+strstate);
        //EditText reason=dialogex.findViewById(R.id.edtreason);
        LinearLayout linexcep = dialogex.findViewById(R.id.lin_excep);
//        if(reasss !=null && reasss.length()>0 && !reasss.contentEquals("null")) {
//            reason.setText(""+reasss);
//        }
        tclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogex.dismiss();
            }
        });

        if(listex.size()>0)
        {
            for(int i=0;i< listex.size();i++)
            {
                Exception_model ev=new Exception_model();
                ev=listex.get(i);

                View layout2 = LayoutInflater.from(this).inflate(R.layout.ex_list, linexcep, false);

                TextView txttype =  layout2.findViewById(R.id.txt_type);
                TextView txtdetail =  layout2.findViewById(R.id.txt_detail);
                TextView txtid =  layout2.findViewById(R.id.txt_id);
                TextView txturl=  layout2.findViewById(R.id.txt_url);
                //  CheckBox chselect=layout2.findViewById(R.id.ch_select);
                txtid.setText(""+ev.id);
                if(ev.article !=null&& ev.article.length()>0)
                {
                    txttype.setText(ev.type+"("+ev.article+")");
                }else
                {
                    txttype.setText(ev.type);
                }
                if(ev.url !=null && ev.url.length()>0)
                {
                    txturl.setText(""+ev.url);
                }
                txtdetail.setText(""+ev.detail);

                layout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(txturl.getText().toString() !=null && txturl.getText().toString().length()>0)
                        {
                            try
                            {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""+txturl.getText().toString()));
                                startActivity(browserIntent);
                            }catch (Exception e)
                            {
                                 //Log.e("errorrrkkk","@"+e.toString());
                            }

                        }
                    }
                });
                linexcep.addView(layout2);
            }
        }





        dialogex.show();
    }

private void gettimezone() {
    try {
        TimeZone tz = TimeZone.getDefault();
timezonesname=""+tz.getDisplayName(false, TimeZone.SHORT);
timezonesid=""+tz.getID();
     }catch (Exception e)
    {

    }

}

    private void appalert()
    {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String oldversion=""+packageInfo.versionCode;
            String version=pref.getString(Constant.APP_VERSION);
            //	Log.e("fdf","@"+version);
            if(version !=null && version.length()>0 && !version.contentEquals("null") )
            {
                Double a=0.00;
                a=Double.parseDouble(version);
                Double b=0.00;
                b=Double.parseDouble(oldversion);

                //	b=Integer.parseInt(oldversion);
                if(a>b)
                {
                    //  updateappalert();
                    floatupdateapp.setVisibility(View.VISIBLE);
                }else{
                    floatupdateapp.setVisibility(View.GONE);
                }
            }

        }
        catch (PackageManager.NameNotFoundException e) {

        }

    }







    private GeoData prevGeoData = null;
    private void updateOBDData(final GeoData geoData) {
        runOnUiThread(new Runnable() {
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
                    pref.putString(Constant.NETWORK_TYPE,Constant.BLUETOOTH);
                   // mInterval = 60000;
                    bl_alert++;
                    intbval=0;
                    pref.putString(Constant.BL_SCANNING_FLAG,"1");
                 try{
                     if(str_obdtime !=null && str_obdtime.length()>0)
                    {

                    }else{
                        bluetoothconnectalert("Bluetooth connection successfully");
                    }

                    if(bl_alert==3)
                    {
                          if (dialogcustomalert != null) {
            if (dialogcustomalert.isShowing()) {
                dialogcustomalert.dismiss();
            }
        }
                    }

                 }catch (Exception e)
                 {

                 }
                    try {
                        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        str_obdtime = formattime.format(new Date());
                    }catch (Exception e)
                    {

                    }
                    //brf=1;
                    try {
                        String dconnecttime = pref.getString(Constant.BLUETOOTH_DISCONNECT_TIME);
                        mInterval = Integer.parseInt(dconnecttime);
                    } catch (Exception e) {

                    }
                    vin = geoData.getVin().trim();
                    String strvin=pref.getString(Constant.VIN_NUMBER);

                    String bnval=checkbactive();
                    SimpleDateFormat formattimed = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String time = formattimed.format(new Date());

                    //tresponse.setVisibility(View.VISIBLE);
                    try {
                        tresponse.setText("Speed : " + geoData.getVehicleSpeed());
                    }catch (Exception e)
                    {
                        tresponse.setText("er");
                    }
                    if(bnval !=null && bnval.contentEquals("0")) {
                        if (vin != null && vin.length() > 0 && vin.contentEquals("" + strvin)) {
                            pref.putString(Constant.BLUETOOTH_RESPONSE_TIME,""+time);


                            try {
                                if (dialogscanning != null) {
                                    if (dialogscanning.isShowing()) {
                                        dialogscanning.dismiss();
                                    }

                                }
                            } catch (Exception e) {

                            }
                            setnetworkicons("BLUETOOTH");

                            pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "1");

//                            if (dialogscanningval == 0) {
                                if ((odometer = geoData.getOdometer()) != null) {
                                    odometer_milesvak = odometer / MILES_TO_KM;
                                }
//                                savebluetoothstatus("1", "" + odometer_milesvak);
//
//
//                                dialogscanningval = 1;
//                            }
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


                            ttval.setText(".");
                            ttval.setVisibility(View.VISIBLE);

                            // vinData.setValueText(vin);
                            //  else
                            // vinData.setValueText("--");
                            if ((odometer = geoData.getOdometer()) != null) {
                                odometer_miles = odometer / MILES_TO_KM;
                                //  odometerText = String.format(Locale.US, "%.1f(%.1f)", odometer, odometer_miles);
                                // odometerTextnew= String.format(Locale.US, "%.1f(%.1f)", odometer_miles);
                                odometerTextnew = String.format(Locale.US, "%.1f", odometer_miles);
                                //  ttval.setText("Bluetooth connected,  odometer:"+odometerText +"km");
                                try {
                                    ttval.setText("Odometer : " + odometerTextnew + " Miles");
                                } catch (Exception e) {

                                }


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
                            if (iscellularRunning == 0) {
                                setnetworkicons("BLUETOOTH");

                                savebluetoothstatus("1", "" + odometer_miles);
                            }
                            if ((speed = geoData.getVehicleSpeed()) != null) {
//                                if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                                        && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                                {
//                                    trackparams.put("_speed",""+speed);
//                                }

                                bluetoothspeed = geoData.getVehicleSpeed();
                                speed /= MILES_TO_KM;//spmk
                                speedText = String.format(Locale.US, "%.0f", speed);
                                tresponse.setText("speed : "+speed +"mph,     odo: "+odometerTextnew+"mil,  ("+time+")");

                                txtspeed.setText("Speed: " + speedText + " MPH");
                                if (!boolactice) {
                                    if (speed > 5) {
                                        pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"on");
                                        tspeedcountown.setText("Timer : ");
                                        if (bluetoothtimer != null) {
                                            try {
                                                isRunning = false;
                                                bluetoothtimer.cancel();

                                            } catch (Exception e) {

                                            }
                                        }

                                        if (intschedule == STATUS_DRIVING) {

                                        } else {

                                            if (OnlineCheck.isOnline(context)) {
                                                // onClickStatusDriving();
                                                if (apicall == 0) {
                                                    apicall = 1;
                                                    onClickStatusDrivingbluetooth(""+geoData.getLatitude(),""+geoData.getLongitude());
                                                }
                                                intschedule = STATUS_DRIVING;
                                                setyellow();
                                                updateTimeStringsTimerWorker();
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
                                                tspeedcountown.setVisibility(View.VISIBLE);

                                                if(pref.getString(Constant.BLUETOOTH_TIMER_MANUALLY).contentEquals("on")) {
                                                    Double finalOdometer_miles = odometer_miles;
                                                    bluetoothtimer = new CountDownTimer(speedtimed, 1000) {

                                                        public void onTick(long millisUntilFinished) {
                                                            //   Log.e(" remaining140: ","" + millisUntilFinished / 1000);
                                                            //here you can have your logic to set text to edittext
                                                            isRunning = true;
                                                            tspeedcountown.setText("Timer : " + millisUntilFinished / 1000 + "  sec");

                                                        }

                                                        public void onFinish() {
                                                            isRunning = false;
                                                            tspeedcountown.setText("Timer : 0.");
                                                            // Log.e(" remaining: ","" +"done!"+isRunning);
                                                            if (blue_intschedule == STATUS_ON_DUTY) {

                                                                if (OnlineCheck.isOnline(context)) {
                                                                    // onClickStatusOnDuty();
                                                                    if (apicall == 0) {
                                                                        apicall = 1;

                                                                        onClickStatusOnDutyBluetooth("Stopped / Under 5MPH", "" + inttrack, "" + geoData.getLatitude(), "" + geoData.getLongitude());
                                                                        intschedule = STATUS_ON_DUTY;
                                                                        setyellow();
                                                                        updateTimeStringsTimerWorker();//
                                                                        inttrack++;
                                                                        //savebluetoothstatus("0", ""+ finalOdometer_miles);
                                                                    }
                                                                }
                                                            } else if (blue_intschedule == STATUS_OFF_DUTY) {

                                                                if (OnlineCheck.isOnline(context)) {
                                                                    if (apicall == 0) {
                                                                        apicall = 1;
                                                                        onClickStatusOffDutyblueetooth("Stopped / Under 5MPH", "" + inttrack, "" + geoData.getLatitude(), "" + geoData.getLongitude());
                                                                        intschedule = STATUS_OFF_DUTY;
                                                                        setyellow();
                                                                        updateTimeStringsTimerWorker();//
                                                                        inttrack++;
                                                                    }
                                                                }
                                                            } else if (blue_intschedule == STATUS_SLEEPER) {

                                                                if (OnlineCheck.isOnline(context)) {

                                                                    if (apicall == 0) {

                                                                        apicall = 1;
                                                                        onClickStatusSleeperbluetooth("Stopped / Under 5MPH", "" + inttrack, "" + geoData.getLatitude(), "" + geoData.getLongitude());
                                                                        intschedule = STATUS_SLEEPER;
                                                                        setyellow();
                                                                        updateTimeStringsTimerWorker();//
                                                                        inttrack++;
                                                                    }
                                                                }
                                                            }

                                                        }

                                                    }.start();
                                                }

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


    private void callnetworktype() {

        if (dialogupdateapp != null) {
            if (dialogupdateapp.isShowing()) {
                dialogupdateapp.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;

        dialogView= inflater.inflate(R.layout.add_netwokrktype, null);



        final TextView cellular = dialogView.findViewById(R.id.t_cellular);
        final TextView tbluetooth = dialogView.findViewById(R.id.t_blutooth);



        final TextView tclose=dialogView.findViewById(R.id.txtalert);
        dialogupdateapp = new Dialog(context, R.style.DialogTheme);
        dialogupdateapp.setCancelable(false);
        tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
      //  tname.setText(""+pref.getString(Constant.COMPANY_NAME));
        dialogupdateapp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogupdateapp.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogupdateapp.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogupdateapp.getWindow().setAttributes(lp);
        dialogupdateapp.show();
        cellular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
//                    if (AppModel.getInstance().mDevice != null) {
//                        WherequbeService.getInstance().disconnect();
//                        // Log.d(TAG, "Disconnected from Whereqube sensor");
//                    }
//                }
//                //Log.e("callz",""+Constant.CELLULAR);
//                pref.putString(Constant.NETWORK_TYPE,""+Constant.CELLULAR);
                dialogupdateapp.dismiss();
                iscellularRunning=0;
                savebluetoothstatus("0","0");
                pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");
                setnetworkicons("WIRELESS");
                //                try {
//                    unregisterReceiver(uiRefresh);
//                }catch (IllegalArgumentException ie)
//                {
//
//                }catch (Exception e)
//                {
//
//                }
//                scanLeDevice(false);
//                stopService();
//                Intent ink=new Intent(context, Home_activity.class);
//                startActivity(ink);
//                finish();
            }
        });
        tbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.NETWORK_TYPE,""+Constant.BLUETOOTH);
              //  Log.e("callz",""+Constant.BLUETOOTH);
              //  WherequbeService.getInstance().setReqHandler(BaseRequest.OBD_MEASUREMENT, myEventHandler);
                dialogupdateapp.dismiss();
                //callbluetoothscanning();
                setnetworkicons("BLUETOOTH");
               pref.putString(Constant.NETWORK_TYPE, "" + Constant.BLUETOOTH);
                Intent mIntent = new Intent(
                        context,
                        Home_activity_bluetooth.class);
                startActivity(mIntent);
                finish();
            }
        });
        tclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogupdateapp.dismiss();
            }
        });


    }

    static RequestHandler myEventHandler = new RequestHandler() {
        @Override
        public void onRecv(@NonNull Context context, @NonNull BaseRequest request) {

            AppModel.getInstance().mLastEvent = request;
            Intent intent = new Intent("REFRESH");
            context.sendBroadcast(intent);
        }
    };






    private void savebluetoothstatus(final String val,String odometer) {
      //  Log.e("blresponsecall","success");
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
        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
        {
            SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String strztime = formattime.format(new Date());
//            trackparams.put("bstatuscall"+strztime,"");
        }
//        Log.e("valll","http://eld.e-logbook.info/elog_app/Bth_conn.php?vin="+str_vin+"&bt_status=1&address="+straddress
//        +"&did="+did+"&blu_address="+bladdress+"&blue_name="+blname
//        +"&odometer="+odometer+"&val="+stcheck);


        Call<Res_model> call = api.savebluetoothstatus(str_vin, ""+val, "" +straddress,did,bladdress,blname,odometer,stcheck );
        call.enqueue(new Callback<Res_model>() {

            public void onResponse(Call<Res_model> call, Response<Res_model> response) {
                if (response.isSuccessful()) {
                    try{
                        Res_model rk=response.body();
//Log.e("blresponse","success");

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

                    if(val.contentEquals("1")) {
                        iscellularRunning=1;
                        setnetworkicons("BLUETOOTH");

                    }else{
                        iscellularRunning = 0;
                        pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");

                        setnetworkicons("WIRELESS");

                    }
                } else {

                    if(val.contentEquals("1")) {
                        iscellularRunning=1;
                        setnetworkicons("BLUETOOTH");
                    }
                }
            }

            @Override
            public void onFailure(Call<Res_model> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());

            }
        });

//        if(val!=null && val.contentEquals("0"))
//        {
//            iscellularRunning = 0;
//            pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "0");
//
//            setnetworkicons("WIRELESS");
//        }
    }


    private void gettodaysavevaluesbluetooth(final String field, final int statusid, final String statuss, String rmark, String trckval, String lati, String longi) {
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

        String did = pref.getString(Constant.DRIVER_ID);

        api = ApiServiceGenerator.createService(Eld_api.class);
//        Log.e("savebl", "eld_savedata.php?vin=" + vinnumber +
//                "&fname=" + field +
//                "&statusid=s" + statusid +
//                "&pc_status=" + statuss +
//                "&lat=" + lat +
//                "&lon=" + lon +
//                "&did=" + did +
//                "&address=" + straddress +
//                "&versiion=" + olddversion
//                + "&state=" + strstate
//                + "&timezonename=" + timezonesname
//                + "&timezoneid=" + timezonesid
//                + "&bvalues=" + "bluetooth"
//                + "&testbreak=" + "break"
//                + "&remark=" + rmark);

        String sk = "";

        sk = ">>" + pref.getString(Constant.CURRENT_STATUS_BB);

        Call<List<Getvalue_model>> call = api.getsaveValues_eldbluetooth(vinnumber, field, "" + statusid, statuss, lat, lon, did, straddress, olddversion, strstate, timezonesname, timezonesid, "bluetooth", rmark, field + sk, "" + trckval,"break");
        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if (response.isSuccessful()) {
//dd
                   // movies = response.body();
                   // settodayval(movies, "0");
                   // savestate("");

                    gettodayvalues(str_vin, "20");
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
    }



    private void calldrivebuttonalert() {
        try{

            if (dialogdrivealert != null) {
                if (dialogdrivealert.isShowing()) {
                    dialogdrivealert.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.drive_button_alert, null);




            final TextView tclose = dialogView.findViewById(R.id.txtalert);
            dialogdrivealert = new Dialog(context, R.style.DialogTheme);
            // dialogdrivealert.setCancelable(false);
            tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
            //  tname.setText(""+pref.getString(Constant.COMPANY_NAME));
            dialogdrivealert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogdrivealert.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogdrivealert.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialogdrivealert.getWindow().setAttributes(lp);
            dialogdrivealert.show();

            tclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogdrivealert.dismiss();
                }
            });
        }catch (Exception e)
        {

        }

    }




    private void callwarningnetwork() {
        try{

            if (dialogwarning != null) {
                if (dialogwarning.isShowing()) {
                    dialogwarning.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.drive_button_alert, null);


//

            final TextView tclose = dialogView.findViewById(R.id.txtalert);
            final TextView ttet=dialogView.findViewById(R.id.t_blutooth);
            dialogwarning = new Dialog(context, R.style.DialogTheme);
            // dialogdrivealert.setCancelable(false);
            tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
            ttet.setText("When DRIVING: Cellular to Bluetooth is NOT Allowed.");
            //  tname.setText(""+pref.getString(Constant.COMPANY_NAME));
            dialogwarning.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogwarning.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogwarning.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialogwarning.getWindow().setAttributes(lp);
            dialogwarning.show();

            tclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogwarning.dismiss();
                }
            });
        }catch (Exception e)
        {

        }

    }


private void callsignalicon()
{
    if(pref.getString(Constant.NETWORK_TYPE) !=null && pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH))
    {


        if(pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS) !=null &&
                pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS).contentEquals("1")) {

            setnetworkicons("BLUETOOTH");

        }else{

            setnetworkicons("WIRELESS");
        }
    }else{
        setnetworkicons("WIRELESS");
    }
}







    private void updateOBDDatastring(final com.isoft.trucksoft_elog.Isoft_activity.GeoData geoData) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    final double MILES_TO_KM = 1.609344;
                    SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                     strealiertime = formatdatetime.format(new Date());
                    Double speed;
                    String speedText = "";
                    String bnval=checkbactive();
                    SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String time = formattime.format(new Date());
                    pref.putString(Constant.BLUETOOTH_RESPONSE_TIME,""+time);
                    bl_alert++;
                    try{
                        if(str_obdtime !=null && str_obdtime.length()>0)
                        {

                        }else{
                            str_obdtime=formattime.format(new Date());
                            bluetoothconnectalert("Bluetooth connection successfully");
                        }

                        if(bl_alert==3)
                        {
                            if (dialogcustomalert != null) {
                                if (dialogcustomalert.isShowing()) {
                                    dialogcustomalert.dismiss();
                                }
                            }
                        }

                    }catch (Exception e)
                    {

                    }
                    intbval=0;
                    tresponse.setVisibility(View.GONE);
                    tresponse.setText(bnval+"..("+time+")");
                    try {
                        if (dialogscanning != null) {
                            if (dialogscanning.isShowing()) {
                                dialogscanning.dismiss();
                            }
                        }
                    }catch (Exception e)
                    {

                    }
                    setnetworkicons("BLUETOOTH");
                    pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS, "1");
//                    if(dialogscanningval==0)
//                    {
//                        savebluetoothstatus("1","");
//
//
//                        dialogscanningval=1;
//                    }
                    savebluetoothstatus("1", "" );
                    ttval.setText("");
                    ttval.setVisibility(View.VISIBLE);
//ZZZ
                    if ((speed = geoData.getVehicleSpeed()) != null) {
                        bluetoothspeed=geoData.getVehicleSpeed();
                        speed /= MILES_TO_KM;//spmk

                        speedText = String.format(Locale.US, "%.0f", speed);
                        txtspeed.setText("Speed: "+speedText+" MPH");
                        if (!boolactice) {
                            if (speed > 5) {
                                tspeedcountown.setText("Timer : ");
                                if (bluetoothtimer != null) {
                                    pref.putString(Constant.B_TIMER_STATUS,"0");
                                    pref.putString(Constant.B_TIMER_STATUS_TIME_REMAINING, "0");

                                    try {
                                        isRunning = false;
                                        bluetoothtimer.cancel();

                                    } catch (Exception e) {

                                    }
                                }

                                if (intschedule == STATUS_DRIVING) {

                                } else {
                                    intschedule = STATUS_DRIVING;
                                    setyellow();
                                    updateTimeStringsTimerWorker();
                                    if (OnlineCheck.isOnline(context)) {
                                        if (apicall == 0) {
                                            onClickStatusDrivingbluetooth("","");
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
                                       // Log.e("dkkkk","&"+pref.getString(Constant.B_TIMER_STATUS));
                                        tspeedcountown.setVisibility(View.VISIBLE);
                                        if(pref.getString(Constant.B_TIMER_STATUS) !=null && pref.getString(Constant.B_TIMER_STATUS).contentEquals("2")) {
                                            pref.putString(Constant.B_TIMER_STATUS, "0");
                                            SimpleDateFormat fdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                            String curnttime = fdatetime.format(new Date());
                                            long scnd=splittimeseconds(curnttime);
                                           // Log.e("scnd","&"+scnd);

                                            String oldtime=pref.getString(Constant.B_TIMER_STATUS_TIME);
                                            long oldscnd=splittimeseconds(oldtime);
                                            //Log.e("oldscnd","&"+oldscnd);
                                            long newsecnd=scnd-oldscnd;
                                            //Log.e("oldscnd","&"+newsecnd);

                                            long dc=Integer.parseInt(pref.getString(Constant.B_TIMER_STATUS_TIME_REMAINING));
                                            long snewsecnd=newsecnd-dc;
                                            //Log.e("snewsecnd","&"+snewsecnd);
                                        }
                                        bluetoothtimer = new CountDownTimer(speedtimed, 1000) {

                                            public void onTick(long millisUntilFinished) {
                                                 //  Log.e(" remaining140: ","" + millisUntilFinished );
                                                //here you can have your logic to set text to edittext
                                                pref.putString(Constant.B_TIMER_STATUS,"1");
                                                SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                                String ctime = formatdatetime.format(new Date());
                                                pref.putString(Constant.B_TIMER_STATUS_TIME, "" + ctime);
                                                pref.putString(Constant.B_TIMER_STATUS_TIME_REMAINING, "" + millisUntilFinished / 1000);
                                                isRunning = true;
                                                tspeedcountown.setText("Timer : "+millisUntilFinished / 1000 + "  sec");

                                            }

                                            public void onFinish() {
                                                pref.putString(Constant.B_TIMER_STATUS,"0");
                                                pref.putString(Constant.B_TIMER_STATUS_TIME_REMAINING, "0");

                                                isRunning = false;
                                                tspeedcountown.setText("Timer : 0.");
                                                // Log.e(" remaining: ","" +"done!"+isRunning);
                                                if (blue_intschedule == STATUS_ON_DUTY) {

                                                    if (OnlineCheck.isOnline(context)) {
                                                        // onClickStatusOnDuty();
                                                        if (apicall == 0) {
                                                            intschedule = STATUS_ON_DUTY;
                                                            setyellow();
                                                            updateTimeStringsTimerWorker();//
                                                            inttrack++;
                                                            onClickStatusOnDutyBluetooth("Stopped / Under 5MPH", "" + inttrack,"","");
                                                        }
                                                    }
                                                } else if (blue_intschedule == STATUS_OFF_DUTY) {

                                                    if (OnlineCheck.isOnline(context)) {
                                                        if (apicall == 0) {
                                                            intschedule = STATUS_OFF_DUTY;
                                                            setyellow();
                                                            updateTimeStringsTimerWorker();//
                                                            inttrack++;
                                                            onClickStatusOffDutyblueetooth("Stopped / Under 5MPH", "" + inttrack,"","");
                                                        }
                                                    }
                                                } else if (blue_intschedule == STATUS_SLEEPER) {

                                                    if (OnlineCheck.isOnline(context)) {

                                                        if (apicall == 0) {
                                                            intschedule = STATUS_SLEEPER;
                                                            setyellow();
                                                            updateTimeStringsTimerWorker();//
                                                            inttrack++;
                                                            onClickStatusSleeperbluetooth("Stopped / Under 5MPH", "" + inttrack,"","");
                                                        }
                                                    }
                                                }

                                            }

                                        }.start();

                                    }


                                }



                            }
                        }else{
                            //Log.e("result","faoile"+boolactice);
                        }
                        //
                        // speedData.setValueText(speedText);
                        // speedDataTimestamp.setValueText(formatter.print(geoData.getVehicleSpeedTimestamp()));
                    } else {
                        //  speedData.setValueText("--");
                        //  speedDataTimestamp.setValueText("");
                    }

                    // saverec(geoData);
                } catch (Exception e) {
                    //  Log.e(TAG, e.toString());
                }
            }
        });
    }

    private void populateList() {
        /* Initialize device list container */
        //Log.e(TAG, "populateList");
        scanLeDevice(true);

    }
    private Runnable scanTimeout = new Runnable() {
        @Override
        public void run() {
         //   Log.e("bHandler","scanTimeout");
            scanLeDevice(false);
        }
    };

    public void stopScanning()
    {
       // Log.e("bHandler","running");
        bHandler.postDelayed(scanTimeout, SCAN_PERIOD);
    }

    private void scanLeDevice(final boolean enable) {
        // Log.e("scanzddd","enable"+enable);
        if (enable) {
            SimpleDateFormat formattime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String skk = formattime.format(new Date());
//            if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                    && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//            {
//                trackparams.put("scan_device"+skk,""+enable);
//            }

            if(mScanning == true) return;
            // Stops scanning after a predefined scan period.
             // Log.e("scanz","enable"+enable);
            clearScanResults();
            mScanning = true;
            mScanner.start(SCAN_PERIOD);
            setProgressBarIndeterminateVisibility(true);
           // mScanButton.setEnabled(false);

        }
        else {
            //Log.e("scanz","timeout"+enable);
            // Cancel the scan timeout callback if still active or else it may fire later.
            mScanning = false;
            mScanner.stop();
            setProgressBarIndeterminateVisibility(false);
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
            i--;
             //Log.e( "Wherequbescanned ","" + wqubes.size());
            //Log.e( "Wherequbescannedecc ","" + wqubes.size());
           // tscan.setText("Device Scanning       :  "+wqubes.size());
            // Note: Dont try to stop scan in the callback
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat formattime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String strztime = formattime.format(new Date());
//                    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//                    {
//                        trackparams.put("wqscaning_"+strztime,""+wqubes.size());
//                    }

                    for (Whereqube wqube: wqubes) {
                      //  tscanstatus.setVisibility(View.VISIBLE);
                     //   tscanstatus.setText("Device detected :");
                        addDevice(wqube);
                    }
                }
            });
        }
    };
    static long connection_request_time = 0;
    private void addDevice(Whereqube device) {
        boolean deviceFound = false;
        SimpleDateFormat formattimeb = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String strztime = formattimeb.format(new Date());
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            trackparams.put("adc_"+strztime,"adddevice");
//        }

        for (Whereqube listDev : deviceList) {
            if (listDev.mDevice.getAddress().equals(device.mDevice.getAddress())) {
                deviceFound = true;
                break;
            }
        }

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
          //  tscanstatus.setVisibility(View.VISIBLE);
          //  tscanstatus.setText("Device detected : Success");
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
                               // Log.e("TAG",""+str_datetime);
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

            } catch (Exception e) {

            }
        }

    }
    class MyObserver extends AbstractWherequbeStateObserver
    {

        public  void onConnected()
        {  pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"1");
            runOnUiThread(new Runnable() {
                public void run() {
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    // Log.d(TAG, "DEVICE_CONNECT_MSG");
                    //btnConnectDisconnect.setText("Disconnect");

//                    AppModel.getInstance().mConnectTime = System.currentTimeMillis();
//
//                    Intent intent = new Intent(context, Home_activity_bluetooth.class);
//                    startActivity(intent);
                }
            });
        }

        @Override
        public void onSynced() {

        }

        @Override
        public void onDiscovered() {

        }

        public  void onDisconnected()
        {pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
            runOnUiThread(new Runnable() {
                public void run() {
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    // Log.d(TAG, "OBD_DISCONNECT_MSG");
                    //btnConnectDisconnect.setText("Connect");
                    //Log.e(TAG, "Unexpected DISCONNECT event");

                }
            });
           // callbluetoothdisconnect("");
        }

        public  void onError(WQError ec)
        {
            //Log.w(TAG, "Error:"+ec.mCode);
        }

    }


    MyObserver mWherequbeObserver = new MyObserver();


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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }

    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean bool=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    bool= false;
            }
            bool=true;
        }
        return bool;
    }


    private void callbluetoothscanning() {

        try{

            if (dialogscanning != null) {
                if (dialogscanning.isShowing()) {
                    dialogscanning.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.bletooth_search, null);


//

            final TextView tclose = dialogView.findViewById(R.id.txtalert);
            final TextView tval=dialogView.findViewById(R.id.tval);
            final TextView treconnect=dialogView.findViewById(R.id.treconnect);
            final TextView treconnectmsg=dialogView.findViewById(R.id.treconnectmsg);
            dialogscanning = new Dialog(context, R.style.DialogTheme);
            progressBarCircle = dialogView.findViewById(R.id.progressBarCircle);
            dialogscanning.setCancelable(false);
            tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
           // ttet.setText("Cellular to Bluetooth is allowed. But Bluetooth to Cellular is not.You will be able to change this once you are not in driving mode.");
            //  tname.setText(""+pref.getString(Constant.COMPANY_NAME));


            dialogscanning.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogscanning.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogscanning.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialogscanning.getWindow().setAttributes(lp);
            dialogscanning.show();
            progressBarCircle.setMax(60);
            progressBarCircle.setProgress(1);
            valb=0;
            treconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treconnect.setVisibility(View.GONE);
                    treconnectmsg.setVisibility(View.GONE);
                    progressBarCircle.setMax(60);
                    progressBarCircle.setProgress(1);
                    countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            //  textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                            //Log.e("runtimer","@"+ (int)(millisUntilFinished / 1000));
                            tval.setText(""+(int)(millisUntilFinished / 1000));
                            progressBarCircle.setProgress((int)(millisUntilFinished / 1000));

                        }

                        @Override
                        public void onFinish() {
                            treconnect.setVisibility(View.VISIBLE);
                            //  progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
                            // progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);


                        }

                    }.start();
                    countDownTimer.start();
                }
            });
            countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    //  textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                    //Log.e("runtimer","@"+ (int)(millisUntilFinished / 1000));
                    tval.setText(""+(int)(millisUntilFinished / 1000));
                    progressBarCircle.setProgress((int)(millisUntilFinished / 1000));

                }

                @Override
                public void onFinish() {
                    treconnect.setVisibility(View.VISIBLE);
                    treconnectmsg.setVisibility(View.VISIBLE);
                  //  progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
                   // progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);


                }

            }.start();
            countDownTimer.start();
            tclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        countDownTimer.cancel();
                    }catch (Exception e)
                    {

                    }
                    dialogscanning.dismiss();
                }
            });
        }catch (Exception e)
        {

        }

    }
    private void uploaddatausage(String date){
        if (OnlineCheck.isOnline(this)) {

            latest=new TrafficSnapshot(this);
            olddatausage=pref.getLong(Constant.DATA_USAGE);
            totaldatausage=latest.device1.rx+latest.device1.tx;
            //Log.e("totaldatausage", "*"+totaldatausage);
            //Log.e("olddatausage", "*"+olddatausage);

            if(totaldatausage>=olddatausage) {
                newusage = totaldatausage - olddatausage;
            }else
            {
                newusage =0;
               // Log.e("newusage", "********************"+newusage);
            }

            String name="";
            try {
                name = Build.BRAND+" "+ Build.MODEL;
            }catch (Exception e)
            {

            }



            Call<JsonObject> call=null;
            api = ApiServiceGenerator.createService(Eld_api.class);
            call = api.updatedatausage(pref.getString(Constant.DRIVER_ID),""+newusage,""+name,pref.getString(Constant.COMPANY_CODE),date);


            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                  //  Log.e("Responsestring", response.body().toString());
                    //Toast.makeText()
                    if (response.isSuccessful()) {

                        if (response.body() != null) {
                            String jsonresponse = response.body().toString();

                            try {
                                JSONObject resp = new JSONObject(jsonresponse);
                                if (response != null) {

                                    String status = resp
                                            .getString("status");
                                    if(status !=null && status.contentEquals("1"))
                                    {
                                        String message=resp
                                                .getString("message");
                                        String usage=resp
                                                .getString("dailyusage");
                                        if(usage!=null && usage.length()>0) {
                                            pref.putLong(Constant.DATA_USAGE, Long.parseLong(usage));
                                        }
                                        // Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch (Exception e)
                            {

                            }


                        } else {
                          //  Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                        }
                    }else{

                       // Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    //Log.e("imageresponseerrr",""+t.toString());
                }
            });

        }

    }





    private void takebreakdialog(){
        try{

            if (dialogbreak != null) {
                if (dialogbreak.isShowing()) {
                    dialogbreak.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.takebreakdialog, null);


            final TextView teabreak = dialogView.findViewById(R.id.t_tea);
            final TextView lunchbreak = dialogView.findViewById(R.id.t_lunch);


            final TextView tclose = dialogView.findViewById(R.id.txtalert);
            dialogbreak = new Dialog(context, R.style.DialogTheme);
            dialogbreak.setCancelable(false);

            tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
            //  tname.setText(""+pref.getString(Constant.COMPANY_NAME));
            dialogbreak.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogbreak.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogbreak.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialogbreak.getWindow().setAttributes(lp);
            dialogbreak.show();
            teabreak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SimpleDateFormat formatsec= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String  dates=formatsec.format(new Date());
                    SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String dfg = formatdatetime.format(new Date());
                    if (dialogbreak != null) {
                        if (dialogbreak.isShowing()) {
                            dialogbreak.dismiss();
                        }
                    }
                    acceptbreak(10,"manual","0");

                }
            });
            lunchbreak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat formatsec= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String  dates=formatsec.format(new Date());
                    SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String dfg = formatdatetime.format(new Date());
                    if (dialogbreak != null) {
                        if (dialogbreak.isShowing()) {
                            dialogbreak.dismiss();
                        }
                    }
                    acceptbreak(30,"manual","1");

                }
            });
            tclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogbreak.dismiss();

                }
            });
        }catch (Exception e)
        {
        }

    }


    private void checkEnableBt() {
        try {
            if (pref.getString(Constant.NETWORK_TYPE) != null || pref.getString(Constant.NETWORK_TYPE).length() > 0 && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes")) {
               // if (pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
                    bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothadapter != null) {
                        if (!bluetoothadapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        }
                    }
               // }
            }
        }catch (Exception e)
        {

        }
    }


    private void callenabletenhour()
    {
        try{

            if (dialogtenrelease != null) {
                if (dialogtenrelease.isShowing()) {
                    dialogtenrelease.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.enable_hour, null);
            TextView txt_ok = (TextView) dialogView.findViewById(R.id.txt_ok);
            TextView txt_cancel = dialogView.findViewById(R.id.txt_cancel);
            TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
            txt_msg.setText("" + "10 hr reset incomplete. Acknowledge");

            dialogtenrelease = new Dialog(context, R.style.DialogTheme);
            dialogtenrelease.setCancelable(false);

            dialogtenrelease.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogtenrelease.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogtenrelease.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialogtenrelease.getWindow().setAttributes(lp);
            dialogtenrelease.show();


            txt_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogtenrelease.dismiss();
                    releasetenhour(commonUtil.OFF_DUTY, intschedule, "0");

                }
            });
            txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogtenrelease.dismiss();
                }
            });
        }catch (Exception e)
        {
            //Log.e("cdddddddd","@"+e.toString());
        }

    }



    private void callenablethirtyfourhour()
    {
        try{

            if (dialogthirtyrelease != null) {
                if (dialogthirtyrelease.isShowing()) {
                    dialogthirtyrelease.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.enable_hour, null);
            TextView txt_ok = (TextView) dialogView.findViewById(R.id.txt_ok);
            TextView txt_cancel = dialogView.findViewById(R.id.txt_cancel);
            TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
            txt_msg.setText("" + "34 hr reset incomplete. Acknowledge");

            dialogthirtyrelease = new Dialog(context, R.style.DialogTheme);
            dialogthirtyrelease.setCancelable(false);

            dialogthirtyrelease.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogthirtyrelease.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogthirtyrelease.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialogthirtyrelease.getWindow().setAttributes(lp);
            dialogthirtyrelease.show();


            txt_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogthirtyrelease.dismiss();
                    getenablesavethirtyreset(commonUtil.OFF_DUTY, intschedule, "0");

                }
            });
            txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogthirtyrelease.dismiss();
                }
            });
        }catch (Exception e)
        {
            // Log.e("cdddddddd","@"+e.toString());
        }

    }

    private String converttwelvehr(String tval)
    {
        // Log.e("tval",""+tval);
        String str = "";
        if (tval != null && tval.length() > 0) {
            try {
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    final Date dateObj = sdf.parse(tval);
                    str = new SimpleDateFormat("hh:mm a").format(dateObj);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
            }catch (Exception e)
            {

            }

        }
        return str;
    }


//    Runnable mStatusChecker = new Runnable() {
//        @Override
//        public void run() {
//            //Log.e("callingd","mStatusChecker");
//            try {
//                Log.e("callingd","callautoconnect");
//              //  callautoconnect(); //this function can change value of mInterval.
//
//            } finally {
//                // 100% guarantee that this always happens, even if
//                // your update method throws an exception
//                mHandler.postDelayed(mStatusChecker, mInterval);
//            }
//        }
//    };

//    void startRepeatingTask() {
//        mStatusChecker.run();
//    }
//
//    void stopRepeatingTask() {
//        mHandler.removeCallbacks(mStatusChecker);
//    }

    private void callautoconnect()
    {
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm", Locale.getDefault());
       String strztime = formattime.format(new Date());
        String val="0";
        try{
            val=checkbactive();
        }catch (Exception e)
        {

        }
//        if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//                && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//        {
//            trackparams.put("callsutoconect"+strztime,""+pref.getString(Constant.BLUETOOTH_CONNECTED_STATUS));
//        }

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

    private String checkbactive() {
        String val="0";
        try {
            if (pref.getString(Constant.NETWORK_TYPE) != null || pref.getString(Constant.NETWORK_TYPE).length() > 0) {
                if (pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
                    bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothadapter != null) {
                        if (!bluetoothadapter.isEnabled()) {
                            val="1";
                        }else{
                            val="0";
                        }
                    }
                }else{
                    val="0";
                }
            }
        }catch (Exception e)
        {

        }
        return val;
    }
    private void applogout()
    {
        //Log.e("called","kk************logout");
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
        //Log.e("called","************logout1");
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
        pref.putString(Constant.BL_SCANNING_FLAG,"0");
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
            //Log.e("serviceer","@"+e.toString());
        }




        // ((Activity) context). finishAndRemoveTask();
    }

    private void newbreakalert(){

        if (dialogbreaknew != null) {
            if (dialogbreaknew.isShowing()) {
                dialogbreaknew.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.newbreak_notification, null);


        final Button btn_yes = dialogView.findViewById(R.id.btn_yes);
        final Button btn_no = dialogView.findViewById(R.id.btn_no);
        final Button btn_skip = dialogView.findViewById(R.id.btn_skip);

        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogbreaknew = new Dialog(context, R.style.DialogTheme);
        dialogbreaknew.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText(""+pref.getString(Constant.BREAK_MESSAGE));
        dialogbreaknew.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbreaknew.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogbreaknew.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogbreaknew.getWindow().setAttributes(lp);
        dialogbreaknew.show();

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbreaknew.dismiss();

                String strduration=pref.getString(Constant.BREAK_DURATION);
                int intduration=10;
                if(strduration !=null &&strduration.length()>0)
                {
                    intduration=Integer.parseInt(strduration);
                }
                acceptbreak(intduration,"auto","");
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbreaknew.dismiss();
                pref.putString(Constant.BREAK_ALERT_DISPLAY,"skip");
                SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String cdtime = formatdatetime.format(new Date());
                pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.BREAK_ALERT_DISPLAY,"skip");
                strbrdialog="taken";
                pref.putString(Constant.BREAK_TAKEN,""+strbrdialog);
                if (checkPermission()) {
                    dialogbreaknew.dismiss();
                    medialevel = 0;
                   // callrecorddialog();
                    newvoicerecord();
                } else {
                    requestPermission();
                }
            }
        });


    }



    private void setbreakvalues(Break_info_model newbreakmodel) {
        if (newbreakmodel != null) {
            Log.e("valllllllllzzz", "@" + newbreakmodel.getMessage());
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


            String breaklive="" + newbreakmodel.getTill_now();
            if (breaklive.contains("-")) {
                breaklive = "00:00:00";
                longblive = splittime("" + breaklive);
                pref.putString(Constant.BREAK_ACTIVATED_TIME,"");
            } else {
                longblive = splittime("" + breaklive);
            }

            strbrdialog = "" + newbreakmodel.getBreak_status();
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

    private void checkbreak() {
        //Log.e("cdtimeava", "@" + pref.getString(Constant.BREAK_AVAILABLE_TODAY));
        try{
            SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String cdtime = formatdatetime.format(new Date());
            if (pref.getString(Constant.BREAK_AVAILABLE_TODAY).contentEquals("yes")) {

                //Log.e("cdtime", "@" + cdtime);
               // Log.e("cdtime", "@" + pref.getString(Constant.BREAK_AVAILABLE_TODAY));
               // Log.e("taken", "@" + pref.getString(Constant.BREAK_TAKEN));
                if (pref.getString(Constant.BREAK_AVAILABLE_TODAY) != null && pref.getString(Constant.BREAK_AVAILABLE_TODAY).contentEquals("yes") && !pref.getString(Constant.BREAK_TAKEN).contentEquals("taken") && !pref.getString(Constant.BREAK_TAKEN).contentEquals("rejected")) {
                    String firststatustime = "" + pref.getString(Constant.BREAK_FIRSTSTATUS_TIME);
                   // Log.e("firststatustime", "@" + firststatustime);
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
                    //Log.e("l88888888", "@" + lonendduration);

                    if (restime >= lonfromduration && restime <= lonendduration) {

                        if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("skip")) {
                           // Log.e("vallll", "@" + pref.getString(Constant.BREAK_ALERT_DISPLAY));
                            String displytime = pref.getString(Constant.BREAK_DISPLAY_TIME);
                           // Log.e("displytime", "@" + displytime);
                            String alertinterval = pref.getString(Constant.BREAK_ALERT_INTERVAL);
                           // Log.e("alertinterval", "@" + alertinterval);
                            long val1 = splittime(displytime);
                           // Log.e("val1", "@" + val1);
                            long val2 = splittime(cdtime);
                           // Log.e("val2", "@" + val2);
                            long resval = 00;
                            resval = val2 - val1;
                          //  Log.e("resval", "@" + resval);
                            long alert = splittimeminute(alertinterval);
                          //  Log.e("alert", "@" + alert);
                            if (resval >= alert) {
                                //Log.e("cond", "@");


                                if (strbrdialog != null && strbrdialog.contentEquals("taken") && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {
                                    pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                                    pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
                                    pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));
                                } else {
                                    if(pppcstatus==0) {
                                        pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                                        pref.putString(Constant.BREAK_ALERT_DISPLAY, "skip");
                                        pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));
                                        newbreakalert();
                                    }
                                }
                            }


                        } else {


                            if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {

                            } else {
                                if(pppcstatus==0) {
//                                    pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
//                                    pref.putString(Constant.BREAK_ALERT_DISPLAY, "skip");
//                                    pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));
                                    newbreakalert();
                                }
                            }
                        }


                        if (pref.getString(Constant.BREAK_ALERT_DISPLAY) != null && pref.getString(Constant.BREAK_ALERT_DISPLAY).contentEquals("taken")) {
                            pref.putString(Constant.MANUAL_LAST_BREAKID,"");
                        } else {
                            pref.putString(Constant.MANUAL_LAST_BREAKID,""+pref.getString(Constant.BREAK_MSG_ID));
                        }
                    }else{
                        pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                        pref.putString(Constant.MANUAL_LAST_BREAKID,"");

                    }
                }else{
                    pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                }
            }else{
                pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
            }
        }catch(Exception e)

        {

        }

    }


    private void acceptbreak(int intduration,String strbreaktype,String brk)
    {
       // Log.e("callk",""+"break"+"  ");
        pref.putString(Constant.BREAK_ALERT_DISPLAY,"accept");
        try {




            if (OnlineCheck.isOnline(context)) {
                progressdlog = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                String strstatus=commonUtil.OFF_DUTY;
                if(intduration <=10)
                {
                    if(intschedule==STATUS_DRIVING)
                    {
                        strstatus=commonUtil.ON_DUTY;
                    }else{
                        if(intschedule==STATUS_ON_DUTY)
                        {
                            strstatus=commonUtil.ON_DUTY;
                        }else  if(intschedule==STATUS_SLEEPER)
                        {
                            strstatus=commonUtil.SLEEP;
                        }else{
                            strstatus=commonUtil.OFF_DUTY;
                        }
                    }
                }else{
                    if(intschedule==STATUS_DRIVING || intschedule==STATUS_ON_DUTY)
                    {
                        strstatus=commonUtil.OFF_DUTY;
                    }else{
                        if(intschedule==STATUS_SLEEPER)
                        {
                            strstatus=commonUtil.SLEEP;
                        }else{
                            strstatus=commonUtil.OFF_DUTY;
                        }
                    }
                }
                String strbrid="0";
                if(strbreaktype.contentEquals("auto"))
                {
                    strbrid=pref.getString(Constant.BREAK_MSG_ID);
                }else{
                    if(pref.getString(Constant.BREAK_DURATION) !=null && pref.getString(Constant.BREAK_DURATION).contentEquals(""+intduration))
                    {
                        if(pref.getString(Constant.MANUAL_LAST_BREAKID) !=null && pref.getString(Constant.MANUAL_LAST_BREAKID).length()>0)
                        {
                            strbrid=pref.getString(Constant.MANUAL_LAST_BREAKID);
                        }
                    }
                }

                api = ApiServiceGenerator.createService(Eld_api.class);



                Call<JsonObject> call = null;



                if(strbreaktype.contentEquals("auto")) {

//                                Log.e("url","http://eld.e-logbook.info/elog_app/eld_acceptautobreak_new.php?cc="+pref.getString(Constant.COMPANY_CODE)
//                                        +"&did="+pref.getString(Constant.DRIVER_ID)
//                                        +"&brk_id="+strbrid+"&type="+pref.getString(Constant.BREAK_TYPE)+"&rule="+
//                                        pref.getString(Constant.BREAK_RULE)+
//                                        "&state="+strstate+"&address="+straddress
//                                        +"&lat="+ lat+"&lon="+lon
//                                        +"&trck="+"trck"+"&change_status="+strstatus+"&testbreak=");
                    call = api.newbreakaccept(pref.getString(Constant.COMPANY_CODE),
                            pref.getString(Constant.DRIVER_ID),
                            strbrid, "" + pref.getString(Constant.BREAK_TYPE),
                            "" + pref.getString(Constant.BREAK_RULE),
                            "" + strstate, "" + straddress,
                            "" + lat, "" + lon,
                            "break_accept",
                            "" + strstatus, "", brk,"");
                }else{
                    String strt="10 minutes break";
                    if(brk.contentEquals("1"))
                    {  strt="30 minutes break";

                    }
                    String trule="0";
                    if (pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active"))
                    {
                        trule="1";
                    }
//                                Log.e("urlmanzzzz","http://eld.e-logbook.info/elog_app/eld_acceptautobreak_new.php?cc="+pref.getString(Constant.COMPANY_CODE)
//                                        +"&did="+pref.getString(Constant.DRIVER_ID)
//                                        +"&brk_id="+strbrid+"&type="+strt+"&rule="+
//                                        trule+
//                                        "&state="+strstate+"&address="+straddress
//                                        +"&lat="+ lat+"&lon="+lon
//                                        +"&trck="+"trck"+"&change_status="+strstatus+"&testbreak=&break_type="+brk+"&eld_active=");


                    call = api.newbreakaccept(pref.getString(Constant.COMPANY_CODE),
                            pref.getString(Constant.DRIVER_ID),
                            strbrid, ""+strt,
                            ""+trule ,
                            "" + strstate, "" + straddress,
                            "" + lat, "" + lon,
                            "break_accept",
                            "" + strstatus, "", brk,"");
                }


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        cancelprogresssdialog();
                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                 //Log.e("jsonresponse", jsonresponse.toString());
                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

                                        String status = resp
                                                .getString("status");
                                        //Log.e("status","@"+status);
                                        if (status != null && status.contentEquals("1")) {
                                            pref.putString(Constant.BREAK_ALERT_DISPLAY,"taken");
                                            gettodayvalues(str_vin,"20");

                                            String lastid = resp
                                                    .getString("lastid");
                                            pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
                                            pref.putString(Constant.BREAK_TAKEN,"taken");
                                            pref.putString(Constant.BREAK_LAST_ID,""+lastid);
                                            SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                            String time = formatdatetime.format(new Date());

                                            pref.putString(Constant.BREAK_ACTIVATED_TIME,""+time);

                                        } else {

                                        }
                                    }
                                } catch (Exception e) {
                                    //Log.e("Exceptionwwwwwwww", e.toString());
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //Log.e("Exceptionwttttttt", t.toString());
                        cancelprogresssdialog();
                    }
                });


            }

        }catch (Exception e)
        {

        }
    }


    private void newbreakreleasenew()
    {
        //Log.e("callk",""+"break"+"  ");

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
                       // cancelprogresssdialog();
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
                                            try {
                                                Uri alarmSound;

                                                alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                                        + "://" + getApplicationContext().getPackageName() + "/raw/bfinished");

                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                                                r.play();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            pref.putString(Constant.BREAK_LIVE_TIME, "");
                                            gettodayvalues(str_vin,"20");



                                        } else {

                                        }
                                    }
                                } catch (Exception e) {
                                    //  Log.e("Exceptionwwwwwwww", e.toString());
                                 //   cancelprogresssdialog();
                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        // Log.e("Exceptionwttttttt", t.toString());
                      //  cancelprogresssdialog();
                    }
                });


            }

        }catch (Exception e)
        {

        }
    }


    private void newbreakrelease(String reason)
    {
        //Log.e("callk",""+"break"+"  ");

        try {




            if (OnlineCheck.isOnline(context)) {
                progressdlog = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                api = ApiServiceGenerator.createService(Eld_api.class);

                pref.putString(Constant.BREAK_ACTIVATED_TIME,"");

                Call<JsonObject> call = null;

//                Log.e("breakreleaseapi","http://eld.e-logbook.info/elog_app/eld_breakrelease.php?cc="+pref.getString(Constant.COMPANY_CODE)
//                        +"&did="+pref.getString(Constant.DRIVER_ID)
//                        +"&brk_tk_id="+pref.getString(Constant.BREAK_LAST_ID)+ "&state="+strstate+"&address="+straddress
//                        +"&lat="+ lat+"&lon="+lon
//                        +"&break_id="+""+pref.getString(Constant.BREAK_LAST_ID)+"&testbreak=");

                call = api.newbreakrelease(pref.getString(Constant.COMPANY_CODE),
                        pref.getString(Constant.DRIVER_ID),
                        ""+pref.getString(Constant.BREAK_LAST_ID),
                        "" + strstate, "" + straddress,"",""+pref.getString(Constant.BREAK_LAST_ID),reason);


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        cancelprogresssdialog();
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
                                            try {
                                                Uri alarmSound;

                                                alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                                        + "://" + getApplicationContext().getPackageName() + "/raw/bfinished");

                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                                                r.play();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            pref.putString(Constant.BREAK_LIVE_TIME, "");
                                            gettodayvalues(str_vin,"20");



                                        } else {

                                        }
                                    }
                                } catch (Exception e) {
                                  //  Log.e("Exceptionwwwwwwww", e.toString());
                                    cancelprogresssdialog();
                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                       // Log.e("Exceptionwttttttt", t.toString());
                        cancelprogresssdialog();
                    }
                });


            }

        }catch (Exception e)
        {

        }
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
                            txtcstate.setVisibility(View.GONE);
                            txtrules.setText("Home state rules applied");
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
    public long splittimeminute(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {

            seconds = Integer.parseInt(time) * 60;

        }
        return seconds;

    }
    public void newvoicerecord()
    {

        if (dialogrec != null) {
            if (dialogrec.isShowing()) {
                dialogrec.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.recordvoice_main, null);

        TextView tslide = (TextView) dialogView.findViewById(R.id.tslide);
        TextView txtalert=dialogView.findViewById(R.id.txtalert);
        tslide.startAnimation(animBlinkrec);
        ImageButton audioSendButton = dialogView.findViewById(R.id.chat_audio_send_button);
        View slideText = dialogView.findViewById(R.id.slideText);
        View recordPanel = dialogView.findViewById(R.id.record_panel);
        recordTimeText = dialogView.findViewById(R.id.recording_time_text);
        dialogrec = new Dialog(context, R.style.DialogTheme);
        dialogrec.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        //  txtstatus.setText(Html.fromHtml("" + pref.getString(Constant.BREAK_MESSAGE)));
        dialogrec.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrec.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrec.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogrec.getWindow().setAttributes(lp);
        dialogrec.show();

        txtalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrec.dismiss();
            }
        });
        audioSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = dp(30);
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;
                    // startRecording();
                    startrecord();
                    audioSendButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Log.e("val"," up cancel");
                    startedDraggingX = -1;
                    // stoprecord();
                    sendrecord();
                    // stopRecording(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    if (x < -distCanMove) {
                        stoprecord();
                        //Log.e("val"," move 0");
                        // stopRecording(false);
                    }
                    x = x + ViewProxy.getX(audioSendButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        params.leftMargin = dp(30) + (int) dist;
                        slideText.setLayoutParams(params);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }
                        ViewProxy.setAlpha(slideText, alpha);
                    }
                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                            + dp(30)) {
                        if (startedDraggingX == -1) {
                            startedDraggingX = x;
                            distCanMove = (recordPanel.getMeasuredWidth()
                                    - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                            if (distCanMove <= 0) {
                                distCanMove = dp(80);
                            } else if (distCanMove > dp(80)) {
                                distCanMove = dp(80);
                            }
                        }
                    }
                    if (params.leftMargin > dp(30)) {
                        params.leftMargin = dp(30);
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });



    }
    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }
    private void startrecord() {
        // TODO Auto-generated method stub

        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();
        recordvoice();
    }
    private void sendrecord()
    {
        dialogrec.dismiss();
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
            }

        } catch (Exception e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
        try {



            progressdlog = ProgressDialog.show(Home_activity_bluetooth.this, "Uploading",
                    "Please wait...", true);
            // new ImageUploadTask().execute();
            if(boolvoicerecord) {
                boolvoicerecord=false;
                uploadFile();
            }
        } catch (Exception e) {
            //Log.e("errrrr", "@" + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void stoprecord() {
        // TODO Auto-generated method stub
        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        recordTimeText.setText("00:00");
        vibrate();
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
            }

        } catch (Exception e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }
    private void setbreakduration(String tval)
    {
        txtdurationcolon.startAnimation(animBlink);
        //Log.e("tval","@"+tval);
        try {
            if (tval != null && tval.length() > 0 && tval.contains(":")) {
                StringTokenizer smk = new StringTokenizer(tval, ":");
                String d1 = smk.nextToken();
                String d2 = smk.nextToken();
                // String d3 = smk.nextToken();
                txt_duration.setText("" + d1);
                txtdurationcolon.setText(":");
                txtdurationsecond.setText("" + d2);
            }
        }catch (Exception e)
        {
            //Log.e("terrr","@"+e.toString());
        }
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

    private void getlogvalues()
    {
        dialogz = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        dialogz.setMessage("Please wait...");
        dialogz.setCancelable(false);
        dialogz.show();


        String did = pref.getString(Constant.DRIVER_ID);
        String vinnumber = "" + pref.getString(Constant.VIN_NUMBER);

        SimpleDateFormat fzz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String datetime = fzz.format(new Date());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DATE, -1);
//        String yesterdayAsString = fzz.format(calendar.getTime());

        api = ApiServiceGenerator.createService(Eld_api.class);
        //Log.e("url","getLoadDetails.php?vin="+vinnumber+"&did="+did+"&date="+datetime);
        Call<JsonObject> call = api.getlogdetails(vinnumber, did, "" + datetime);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                cancelprogresssdialogz();
                //Log.e("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        String jsonresponse = response.body().toString();
                        //Log.e("jsonresponse", jsonresponse.toString());
                        try {
                            JSONObject resp = new JSONObject(jsonresponse);
                            if (response != null) {

                                if (resp.has("status")) {
                                    String stat = resp.getString("status");

                                    if (stat != null && stat.contentEquals("1")) {
                                        String dataz=resp.getString("data");

                                        JSONArray jsonarray = new JSONArray(dataz);
                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject explrObject = jsonarray.getJSONObject(i);
                                          //  Log.e("explrObject",""+explrObject.toString());
                                            str_newtruck=explrObject.getString("truck");
                                            str_newtrailor=explrObject.getString("trailer");
                                            str_tripnum=explrObject.getString("trip_num");
                                            str_cmdty=explrObject.getString("commodity");
                                        }
                                        loddetailspopup();
                                    } else {
                                        loddetailspopup();
                                    }
                                }

                            }
                        } catch (Exception e) {
                            //Log.e("Exceptionwwwwwwww", e.toString());
                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e("Exceptionwttttttt", t.toString());
                cancelprogresssdialogz();
            }
        });
    }
    public  void loddetailspopup(){
        if(dialogloaddetails !=null) {
            if (dialogloaddetails.isShowing()) {
                dialogloaddetails.dismiss();
            }
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View dialogView = inflater.inflate(R.layout.remark_popup_new, null);
        final View dialogView = inflater.inflate(R.layout.load_details_popup, null);

        EditText edttruck = dialogView.findViewById(R.id.edttruck);
        EditText edttrailor = dialogView.findViewById(R.id.edttrailor);
        EditText edtcmdty = dialogView.findViewById(R.id.edtcommodity);
        EditText edtshipdoc = dialogView.findViewById(R.id.edtshipdoc);
        TextView txtpencil=dialogView.findViewById(R.id.edtpencil);
        TextView txtpencil1=dialogView.findViewById(R.id.edtpencil1);
        TextView txtpencil2=dialogView.findViewById(R.id.edtpencil2);
        TextView txtpencil3=dialogView.findViewById(R.id.edtpencil3);
        String vno = pref.getString(Constant.VID_NUMBER);
        if(vno !=null && vno.length()>0 && !vno.contentEquals("null"))
        {
            edttruck.setText(""+vno);
        }else if(str_newtruck !=null && str_newtruck.length()>0 && !str_newtruck.contentEquals("null"))
        {
            edttruck.setText(""+str_newtruck);
        }
        if(str_newtrailor !=null && str_newtrailor.length()>0 && !str_newtrailor.contentEquals("null"))
        {
            edttrailor.setText(""+str_newtrailor);
        }
        if(str_tripnum !=null && str_tripnum.length()>0 && !str_tripnum.contentEquals("null"))
        {
            edtshipdoc.setText(""+str_tripnum);
        }

        if(str_cmdty !=null && str_cmdty.length()>0 && !str_cmdty.contentEquals("null"))
        {
            edtcmdty.setText(""+str_cmdty);
        }
        txtpencil.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtpencil1.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtpencil2.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtpencil3.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);

        dialogloaddetails = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);



        dialogloaddetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogloaddetails.setContentView(dialogView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogloaddetails.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogloaddetails.getWindow().setAttributes(lp);
        dialogloaddetails.show();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edttruck.getText().toString() !=null && edttruck.getText().toString().length()>0 && edttrailor.getText().toString()!=null && edttrailor.getText().toString().length()>0 &&
                        edtcmdty.getText().toString() !=null &&  edtcmdty.getText().toString().length()>0 && edtshipdoc.getText().toString() !=null && edtshipdoc.getText().toString().length()>0) {
                    dialogz = new ProgressDialog(context,
                            AlertDialog.THEME_HOLO_LIGHT);
                    dialogz.setMessage("Please wait...");
                    dialogz.setCancelable(false);
                    dialogz.show();


                    String did = pref.getString(Constant.DRIVER_ID);
                    String vinnumber = "" + pref.getString(Constant.VIN_NUMBER);

                    SimpleDateFormat fzz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String datetime = fzz.format(new Date());

                    api = ApiServiceGenerator.createService(Eld_api.class);
                    //Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+rowid+"&did="+did+"&num="+msg+"&trip="+""+tripid);
                    Call<JsonObject> call = api.savelogdetails(vinnumber, did, "" + datetime, "" + edttruck.getText().toString(),
                            edttrailor.getText().toString(), edtcmdty.getText().toString(), edtshipdoc.getText().toString());

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            cancelprogresssdialogz();
                            //Log.e("Responsestring", response.body().toString());
                            //Toast.makeText()
                            if (response.isSuccessful()) {

                                if (response.body() != null) {
                                    String jsonresponse = response.body().toString();
                                    //Log.e("jsonresponse", jsonresponse.toString());
                                    try {
                                        JSONObject resp = new JSONObject(jsonresponse);
                                        if (response != null) {

                                            if (resp.has("status")) {
                                                String stat = resp.getString("status");

                                                if (stat != null && stat.contentEquals("1")) {
                                                    String msg = resp.getString("message");
                                                    Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

                                                    dialogloaddetails.dismiss();


                                                } else {
                                                    String msg = resp.getString("message");
                                                    Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        }
                                    } catch (Exception e) {
                                        //Log.e("Exceptionwwwwwwww", e.toString());
                                    }


                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            //Log.e("Exceptionwttttttt", t.toString());
                            cancelprogresssdialogz();
                        }
                    });
                }else {

                    Toast.makeText(context, "Please enter values all fields" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogloaddetails.dismiss();
            }
        });

    }


    private void getbreakrefresh() {
        Log.e("calling","getbreakrefresh");
        String did = pref.getString(Constant.DRIVER_ID);


        api = ApiServiceGenerator.createService(Eld_api.class);
//        Call<List<newbrk_model>> call = api.getbreakrefresh(""+pref.getString(Constant.VIN_NUMBER), did, ""+trule , ""+pref.getString(Constant.COMPANY_CODE));
        Call<List<newbrk_model>> call = api.getbreakrefreshnew(""+pref.getString(Constant.VIN_NUMBER), did, ""+pref.getString(Constant.COMPANY_CODE));

        call.enqueue(new Callback<List<newbrk_model>>() {
            @Override
            public void onResponse(Call<List<newbrk_model>> call, Response<List<newbrk_model>> response) {
                if (response.isSuccessful()) {
                    List<newbrk_model> brkrefrsh = response.body();
                    setbreakrfresh(brkrefrsh);
                } else {


                }
               // savestate("");
            }

            @Override
            public void onFailure(Call<List<newbrk_model>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());


            }
        });
    }

    private void callengineoff(){
if(pref.getString(Constant.NETWORK_TYPE) !=null && pref.getString(Constant.NETWORK_TYPE).contentEquals(Constant.BLUETOOTH)) {
    try {

        if(pref.getString(Constant.CURRENT_STATUS_BB) !=null && pref.getString(Constant.CURRENT_STATUS_BB).toString().contentEquals(commonUtil.DRIVING)) {
            String cstatus = "" + pref.getString(Constant.CURRENT_STATUS_BB);
            //Log.e("cstatus", "@" + cstatus);
            if (cstatus != null && cstatus.length() > 0 && !cstatus.contentEquals("null")) {
                if (cstatus.contentEquals(commonUtil.OFF_DUTY)) {
                    if (OnlineCheck.isOnline(context)) {
                        if (apicall == 0) {
                            apicall = 1;
                            onClickStatusOffDutyblueetooth("Stopped", "" + inttrack, "" + lat, "" + lon);
                            intschedule = STATUS_OFF_DUTY;
                            setyellow();
                            updateTimeStringsTimerWorker();//
                            inttrack++;
                        }
                    }
                } else if (cstatus.contentEquals(commonUtil.ON_DUTY)) {
                    if (OnlineCheck.isOnline(context)) {
                        // onClickStatusOnDuty();
                        if (apicall == 0) {
                            apicall = 1;
                            onClickStatusOnDutyBluetooth("Stopped", "" + inttrack, "" + lat, "" + lon);
                            intschedule = STATUS_ON_DUTY;
                            setyellow();
                            updateTimeStringsTimerWorker();//
                            inttrack++;
                        }
                    }

                } else if (cstatus.contentEquals(commonUtil.SLEEP)) {
                    if (OnlineCheck.isOnline(context)) {

                        if (apicall == 0) {

                            apicall = 1;
                            onClickStatusSleeperbluetooth("Stopped", "" + inttrack, "" + lat, "" + lon);
                            intschedule = STATUS_SLEEPER;
                            setyellow();
                            updateTimeStringsTimerWorker();//
                            inttrack++;
                        }
                    }
                } else {
                    if (OnlineCheck.isOnline(context)) {
                        if (apicall == 0) {
                            apicall = 1;
                            onClickStatusOffDutyblueetooth("Stopped", "" + inttrack, "" + lat, "" + lon);
                            intschedule = STATUS_OFF_DUTY;
                            setyellow();
                            updateTimeStringsTimerWorker();//
                            inttrack++;
                        }
                    }
                }
            }
            tspeedcountown.setText("Timer : ");
            if (bluetoothtimer != null) {
                try {
                    isRunning = false;
                    bluetoothtimer.cancel();

                } catch (Exception e) {

                }
            }
            pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY, "off");
        }

    } catch (Exception e) {

    }
}

    }





    private void customalert(String msgval) {

        if (dialogcustomalert != null) {
            if (dialogcustomalert.isShowing()) {
                dialogcustomalert.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;

            dialogView= inflater.inflate(R.layout.custom_alert, null);



        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);

        txtstatus.setText(""+msgval);
        dialogcustomalert = new Dialog(context, R.style.DialogTheme);
        dialogcustomalert.setCancelable(false);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        dialogcustomalert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcustomalert.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogcustomalert.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogcustomalert.getWindow().setAttributes(lp);
        dialogcustomalert.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcustomalert.dismiss();


            }
        });


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
                        //Log.i("", "All location settings are satisfied.");
                        Toast.makeText(getApplication(), "GPS is already enable", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i("", "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            status.startResolutionForResult(Home_activity_bluetooth.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                        //Toast.makeText(getApplication(), "Location settings are inadequate, and cannot be fixed here", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    private void callprivacy()
    {
        if (dialogprivacy != null) {
            if (dialogprivacy.isShowing()) {
                dialogprivacy.dismiss();
            }
        }
        String val2="  * When you change status OFF DUTY, SLEEP, ON DUTY"+ " \n \n" +

                "  * When you the system automatically changes status to 'DRIVING'\n" +
                " \n" + "  * When taking breaks"+" \n"
                +" \n" + "  * When moving one state to another to help you keep up with the current state rules. \n"
                +" \n" + "  * When performing a Vehicle inspection report (DVIR) (Pre and Post-Trip) \n"
                +" \n" +"  * If you are uploading an image to report a fault in your truck while doing DVIR\n"
                +" \n" +"  * When Certifying your log.";

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.location_privacy, null);
        final TextView btnsubmitz = dialogView.findViewById(R.id.tsubmit);
        final TextView tcancel = dialogView.findViewById(R.id.tcancel);
        final TextView lsub = dialogView.findViewById(R.id.lsub);
        lsub.setText(""+val2);
        dialogprivacy = new Dialog(context, R.style.DialogTheme);
        dialogprivacy.setCancelable(false);
        dialogprivacy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogprivacy.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogprivacy.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogprivacy.getWindow().setAttributes(lp);
        dialogprivacy.show();
        btnsubmitz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogprivacy.dismiss();
                insertDummyContactWrapper();
            }
        });
        tcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogprivacy.dismiss();
            }
        });

    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, E_logbook_ForegroundService.class);
        stopService(serviceIntent);
    }
    public void startService() {
        Intent serviceIntent = new Intent(this, E_logbook_ForegroundService.class);
        startService(serviceIntent);
    }
    private void bluetoothconnectalert(String msgval) {

        if (dialogcustomalert != null) {
            if (dialogcustomalert.isShowing()) {
                dialogcustomalert.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;

        dialogView= inflater.inflate(R.layout.bluetooth_alert, null);



        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);

        txtstatus.setText(""+msgval);
        dialogcustomalert = new Dialog(context, R.style.DialogTheme);
        dialogcustomalert.setCancelable(false);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        dialogcustomalert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcustomalert.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogcustomalert.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogcustomalert.getWindow().setAttributes(lp);
        dialogcustomalert.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcustomalert.dismiss();


            }
        });


    }

    private void geofencelalert(String title,String msgval) {

        if (dialoggeoalert != null) {
            if (dialoggeoalert.isShowing()) {
                dialoggeoalert.dismiss();
            }
        }

        try {
            Uri alarmSound;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/appupdatetone");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;

        dialogView= inflater.inflate(R.layout.geo_alert, null);



        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final TextView ttitled = dialogView.findViewById(R.id.ttitle);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        ttitled.setText(""+title);
        txtstatus.setText(""+msgval);
        dialoggeoalert = new Dialog(context, R.style.DialogTheme);
        dialoggeoalert.setCancelable(false);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        dialoggeoalert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoggeoalert.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoggeoalert.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialoggeoalert.getWindow().setAttributes(lp);
        dialoggeoalert.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggeoalert.dismiss();


            }
        });


    }

    private void setnetworkicons(String type)
    {
//Log.e("callingtype","@"+type);
        if(type.contentEquals("BLUETOOTH")) {
            imgtakenetwork.setBackgroundResource(R.drawable.green_large);
            img_draw = txtnetworkstate.getContext().getResources().getDrawable(R.drawable.bluetoothicon);
            txtnetworkstate.setCompoundDrawablesWithIntrinsicBounds(null, null, img_draw, null);
            txtnetworkstate.setText("Bluetooth");
            if (Build.VERSION.SDK_INT >= 23) {
                txtnetworkstate.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                tconnection.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                txtnetworkstate.setTextColor(getResources().getColor(R.color.companycolor));
                tconnection.setTextColor(getResources().getColor(R.color.white));
            }

        }else{

          txtnetworkstate.setText("Cellular");
            imgtakenetwork.setBackgroundResource(R.drawable.yellow_large);
            img_draw = txtnetworkstate.getContext().getResources().getDrawable( R.drawable.cellular_icon);
            txtnetworkstate.setCompoundDrawablesWithIntrinsicBounds( null, null, img_draw, null);
            if (Build.VERSION.SDK_INT >= 23) {
                txtnetworkstate.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                tconnection.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
            } else {
                txtnetworkstate.setTextColor(getResources().getColor(R.color.companycolor));
                tconnection.setTextColor(getResources().getColor(R.color.companycolor));
            }


        }
    }

//private void savetrackresp() {
//    if(pref.getString(Constant.TRACK_BLUETOOTH) !=null && pref.getString(Constant.TRACK_BLUETOOTH).length()>0
//            && pref.getString(Constant.TRACK_BLUETOOTH).contentEquals("active"))
//    {
//    Log.e("calldabe", "track");
//    Call<Response_model> call = api.savetrckrespp(trackparams);
//
//
//    call.enqueue(new Callback<Response_model>() {
//        @Override
//        public void onResponse(Call<Response_model> call, Response<Response_model> response) {
//            Log.e(" Responsev", " " + response.toString());
//            Log.e(" Responsesskk", " " + String.valueOf(response.code()));
//            if (response.isSuccessful()) {
//
//
//                if (response.body() != null) {
//                    // movies.addAll(response.body());
//                    // Log.e(" Responsecqevv","z "+response.body());
//
//
//                } else {
//
//                }
//
//            }
//            trackparams = new HashMap<>();
//            trackparams.put("did",""+pref.getString(Constant.DRIVER_ID));
//        }
//
//        @Override
//        public void onFailure(Call<Response_model> call, Throwable t) {
//            trackparams = new HashMap<>();
//            trackparams.put("did",""+pref.getString(Constant.DRIVER_ID));
//
//        }
//    });
//}
//}


    private void stopbreakdialog()
    {
        LayoutInflater inflater = this.getLayoutInflater();
        //lq
        // final View dialogView = inflater.inflate(R.layout.remark_popup_new, null);
        final View dialogView = inflater.inflate(R.layout.break_stop, null);

        name = dialogView.findViewById(R.id.rmark);
        name.setHint("Break Not Completed. Please respond your reason" );
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        btnSpeak = dialogView.findViewById(R.id.btnspeak);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        //final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogrk = new Dialog(context, R.style.DialogTheme);

     //   txtstatus.setText("STATUS : " );
        dialogrk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrk.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrk.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogrk.getWindow().setAttributes(lp);
        dialogrk.show();
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                promptSpeechInput();
            }
        });




        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString() !=null && name.getText().toString().length()>0) {
                    mdk = 0;
                    newbreakrelease(name.getText().toString());
                    dialogrk.dismiss();
                }else{
                    Toast.makeText(context,"Please enter reason for release break",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrk.dismiss();

                // setdistancealert();
                //setondutyalert();
            }
        });

    }
}
