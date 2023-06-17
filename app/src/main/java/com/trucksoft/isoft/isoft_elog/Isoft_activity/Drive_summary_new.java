package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Model_class.Summarymodel;
import com.isoft.trucksoft_elog.Model_class.todaymodel;
import com.isoft.trucksoft_elog.Model_class.weeklymodel;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Drive_summary_new extends AppCompatActivity
{
    List<Summarymodel> movies;
    Eld_api api;
    Preference pref;
    Context context;
    Timer T;
    String dc;
    private ProgressDialog dialogz;
    private String stronduty="00:00",strofduty="00:00",strdrive="00:00",strsleep="00:00",strfreehrs="00:00";

    long londuty,ldrive,lfreehrs;
     FitChart fitChart1;
     FitChart fitChart2;

    FitChart fitChart3;
    FitChart fitChart4;


     private TextView txt1,txt2,txt3,txt4;
    private TextView txt5,txt6;
    private TextView txt7,txt8;



    private TextView txta1,txta2;
    private TextView txta3,txta4;
    private TextView txta5,txta6;
    Status_util futil;

    private TextView txtday,txtweek;
    private TextView txtback;
    Font_manager font_manager;
    LinearLayout lin_parent;
    GestureDetector gestureDetector;

    String dailymeter="";
    String weeklymeter="";
    TextView txtdailymeter,txtweekmeter;

    // The following are used for the shake detection
  //  private SensorManager mSensorManager;
   // private Sensor mAccelerometer;
  //  private ShakeDetector mShakeDetector;
    private int intweekon=0;
    private int intweekdrive=0;
    private String str_driveused="00:00";
    private String str_weeklydriveused="00:00";
    private String str_weeklyused="00:00";
    private String str_ondutyeused="00:00";
    private String str_currenttime="00:00";
    private TextView tondutyallowd;

    private  String strcurrentstatus="OFF_DUTY";

    private String mr_weekonused="00:00";
    private String mr_weekallow="00:00";

    private String mr_driveonused="00:00";
    private String mr_drivelimit="00:00";
    private String mr_onused="00:00";
    private String mr_onlimit="00:00";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    private TextView trule;
    private TextView daymile,weekmile;

    private String lat="";
    private String lon="";
    private String strstate="";
    private String straddress="";
    private TextView tweekexceed;

    private FrameLayout tdailydrive;
    private TextView tdriveexceed;
    private TextView tonexceed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_summarys_new);
        movies=new ArrayList<>();
        context=this;
        pref=Preference.getInstance(context);
        font_manager=new Font_manager();
        //SimpleDateFormat formatsec= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      //  dc =formatsec.format(new Date());
        dc=gettimeonedate();
        api = ApiServiceGenerator.createService(Eld_api.class);
        txt1=findViewById(R.id.txtd1);
        tdailydrive=findViewById(R.id.ldaily);
        tdriveexceed=findViewById(R.id.driveexceed);
        tonexceed=findViewById(R.id.onexceed);
        txtday=findViewById(R.id.txtday);
        txtweek=findViewById(R.id.txtweek);
        txtback=findViewById(R.id.txtback);
        tweekexceed=findViewById(R.id.weekexceed);
        trule=findViewById(R.id.trule);
        txtdailymeter=findViewById(R.id.daymeter);
        daymile=findViewById(R.id.todaymile);
        weekmile=findViewById(R.id.weekmileage);
        txtweekmeter=findViewById(R.id.wkmeter);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        txtback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        futil=new Status_util();
        //fl_statusd= findViewById(R.id.f_status);
        txt2=findViewById(R.id.txtd2);
        txt3=findViewById(R.id.txtd3);
        txt4=findViewById(R.id.txtd4);
        lin_parent=findViewById(R.id.linparent);
        txt5=findViewById(R.id.txtd5);
        txt6=findViewById(R.id.txtd6);

        txt7=findViewById(R.id.txtd7);
        txt8=findViewById(R.id.txtd8);
        tondutyallowd=findViewById(R.id.txtaonval);
        txta1=findViewById(R.id.txta1);
        txta2=findViewById(R.id.txta2);

        txta3=findViewById(R.id.txta3);
        txta4=findViewById(R.id.txta4);


        txta5=findViewById(R.id.txta5);
        txta6=findViewById(R.id.txta6);


        fitChart1 = findViewById(R.id.ftchart1);
        fitChart1.setMinValue(0f);
        fitChart1.setMaxValue(100f);

        fitChart2 = findViewById(R.id.ftchart2);
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }

        fitChart2.setMinValue(0f);
        fitChart2.setMaxValue(100f);


        fitChart3 = findViewById(R.id.ftchart3);
        fitChart3.setMinValue(0f);
        fitChart3.setMaxValue(100f);
        fitChart4 = findViewById(R.id.ftchart4);
        fitChart4.setMinValue(0f);
        fitChart4.setMaxValue(100f);
       // SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        //str_currenttime = formattime.format(new Date());
str_currenttime=gettimezonetime();

        actionBarSetup();
        getdata();
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(context, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
//                    Intent ink = new Intent(context, Home_activity_bluetooth.class);
//                    startActivity(ink);
                    finish();
//                }
            }
        });

        lin_parent.setOnTouchListener(new OnSwipeTouchListener(Drive_summary_new.this) {
            public void onSwipeTop() {
                //getdata();
            }
            public void onSwipeRight() {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent mIntent1 = new Intent(Drive_summary_new.this, Home_activity.class);
//                    startActivity(mIntent1);
//                    finish();
//                }else{
                   finish();
//                }
            }
            public void onSwipeLeft() {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent mIntent1 = new Intent(Drive_summary_new.this, Home_activity.class);
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
                           // Log.e("callv","#"+msg);
                            applogout();
                        }else if (msg.contentEquals("send_status")) {
                            //Log.e("callv","#"+msg);
                            getdata();
                        }



                    }
                }
            }
        };

        T = new Timer();

        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Log.e("run","timer..............");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTimeStringsTimerWorker();
                    }
                });
            }
        }, 1000, 1000);//4000 18

    }
    private void getdata(){
        str_driveused="00:00";
        str_weeklydriveused="00:00";
        str_weeklyused="00:00";
        str_ondutyeused="00:00";

        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        str_currenttime = formattime.format(new Date());

        dialogz = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        dialogz.setMessage("Please wait...");
        dialogz.setCancelable(false);
        dialogz.show();
        Log.e("vbn","http://eld.e-logbook.info/elog_app/eld_pchart_value.php?hstate="+pref.getString(Constant.STATE_FIELD)+"&cstate="+pref.getString(Constant.CURRENT_STATE)+"&did="+pref.getString(Constant.DRIVER_ID)+
                "&s_date="+dc+"&vin="+pref.getString(Constant.VIN_NUMBER)+"&cc="+pref.getString(Constant.COMPANY_CODE)+"&lat="+lat+"&lon="+lon+"&address="+straddress+"&state="+strstate+"&feature=Driver Summary");
         Call<List<Summarymodel>> call = api.getsummary(pref.getString(Constant.STATE_FIELD),pref.getString(Constant.CURRENT_STATE),""+pref.getString(Constant.DRIVER_ID),dc,pref.getString(Constant.VIN_NUMBER),pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Driver Summary");



        call.enqueue(new Callback<List<Summarymodel>>() {
            @Override
            public void onResponse(Call<List<Summarymodel>> call, Response<List<Summarymodel>> response) {
                   if(response.isSuccessful()){
                    List<Summarymodel> result = response.body();
                    if(result.size()>0){
                        //add loaded data
                        movies=new ArrayList<>();
                        movies.addAll(result);
                        setval(movies);
                    }else{
                        cancelprogresssdialog();
                        Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                   // Log.e("ddf"," Load More Response Error "+String.valueOf(response.code()));
                       //dialogz.dismiss();
                      // getdata();
                       cancelprogresssdialog();
                }
            }

            @Override
            public void onFailure(Call<List<Summarymodel>> call, Throwable t) {
                //Log.e("ddd"," Load More Response Error "+t.getMessage());
                cancelprogresssdialog();
            }
        });
    }
    private void setval(List<Summarymodel> movies) {
//Log.e("ddd","success");
        cancelprogresssdialog();
        try {
            for (int j = 0; j < movies.size(); j++)
            {
                Summarymodel grt = movies.get(j);
                List<todaymodel> tl=new ArrayList<>();
                List<weeklymodel> wl=new ArrayList<>();
               tl=grt.today;
               wl=grt.week;
                trule.setText(""+grt.rule);
                strcurrentstatus=grt.current_status;
                for(int k=0;k<tl.size();k++)
                {
                    todaymodel today= tl.get(k);
                    str_driveused=today.driv_used;
                    str_ondutyeused=today.onduty_remains;
                    mr_driveonused=today.driv_used;
                    mr_drivelimit=today.driv_limit;
                    mr_onused=today.onduty_used;
                    mr_onlimit=today.onduty_limit;
                    daymile.setText(""+today.dailymeter);
                    calldrive(today.driv_per,today.driv_limit,today.driv_remains);
                    callonduty(today.onduty_per,today.onduty_limit,today.onduty_remains);
                }
                for(int k=0;k<wl.size();k++) {
                    weeklymodel week = wl.get(k);
                    str_weeklydriveused=week.week_allowed_seven;

                    if(str_weeklydriveused ==null || str_weeklydriveused.length()==0)
                    {
                        if(grt.rule !=null && grt.rule.contains("Federal"))
                        {
                            str_weeklydriveused = "70:00";
                        }else {
                            str_weeklydriveused = "80:00";
                        }

                    }
                    mr_weekallow=str_weeklydriveused;
                    mr_weekonused=week.onduty_used;
                    //Log.e("str_weeklydriveused","&"+str_weeklydriveused);
                    str_weeklyused=week.week_remains;
                 //   Log.e("week_remains","&"+str_weeklyused);


                    weekmile.setText(""+week.weeklymeter);
                    callweekonduty(week.onduty_per,str_weeklydriveused,week.week_remains);
                }
                callbreak();

                //callweekdrive();

               // callonduty();
                //callweekonduty();

               // calloffduty();
               // callweekoffduty();

                //callsleep();
               // callweeksleep();
            }

        }catch (Exception e)
        {

        }
        //setData(5, 100);
    }


    public int splittime(String time)
    {
        int seconds = 00;
//Log.e("splittime",""+time);
try {
    if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
        String timeSplit[] = time.split(":");

        seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60;

    }
}catch (Exception e)
{
    Log.e("unread_comperr","#"+e.toString());

}
//Log.e("seconds",""+seconds);
        return seconds;

    }
    public int splittimenew(String time)
    {
        int seconds = 00;
//Log.e("splittime",""+time);
        try {
            if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
                String timeSplit[] = time.split(":");

                seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);

            }
        }catch (Exception e)
        {

        }
        return seconds;

    }
private void calldrive(String drivepercent, String driv_limit, String driv_used)
{



    txta2.setText(driv_limit+" hr");
    int sk1=splittime(driv_limit);
    int sk2=splittime(mr_driveonused);
    Log.e("sk1","@"+sk1);
    Log.e("sk2","@"+sk2);
if(sk2>sk1)
{
    tdailydrive.setVisibility(View.VISIBLE);
    tdriveexceed.setVisibility(View.VISIBLE);
    Log.e("mr_driveonused","@"+mr_driveonused);
    Log.e("skkk","@");

    int mr1=splittime(mr_driveonused);
    Log.e("mr1","@"+mr1);
    int mr2=splittime(mr_drivelimit);
    Log.e("mr2","@"+mr2);
    int mr3=mr1-mr2;
    Log.e("mr3","@"+mr3);
    Log.e("skkkdd",""+"Time Exceeded By : \n("+ getDurationString(mr3)+" Hr)");
    tdriveexceed.setText("Time Exceeded By : \n("+ getDurationString(mr3)+" Hr)");
    txt2.setText("00:00:00" + " hr");
}else {
    txt2.setText(driv_used + " hr");
}
    Resources resources = getResources();
    Collection<FitChartValue> values = new ArrayList<>();
    values.add(new FitChartValue(100f, resources.getColor(R.color.lightblue)));
    fitChart1.setValues(values);
}



//
//    //call onduty
    private void callonduty(String onduty_per, String onduty_limit, String onduty_used)
    {

Log.e("onduty_used","#"+onduty_used);
        if(onduty_used.contentEquals("00:00") || onduty_used.contains("-"))
        { tonexceed.setVisibility(View.VISIBLE);
            int mr1=splittime(mr_onused);
            Log.e("mrd1","@"+mr1);
            int mr2=splittime(mr_onlimit);
            Log.e("mrds2","@"+mr2);
            int mr3=mr1-mr2;
            txt6.setText("00:00:00" + " hr");
            tonexceed.setText("Time Exceeded By : \n("+ getDurationString(mr3)+" Hr)");
        }else {
            txt6.setText(onduty_used + " hr");
            tonexceed.setVisibility(View.INVISIBLE);
        }
        txta6.setText(onduty_limit+" hr");


        Resources resources = getResources();
        Collection<FitChartValue> values = new ArrayList<>();
        values.add(new FitChartValue(100f, resources.getColor(R.color.lightblue)));

        fitChart3.setValues(values);
    }
//
    private void callweekonduty(String onduty_per, String onduty_limit, String ondutyused)
    {

       // txt8.setText(ondutyused+" hr");

        if( ondutyused.contains("-"))
        {
            txt8.setText("00:00:00 hr");
            int m1=splittime(mr_weekonused);
            int m2=splittime(mr_weekallow);
            int m3=m1-m2;
            Log.e("exceed time", "##" + getDurationString(m3));
            tweekexceed.setVisibility(View.VISIBLE);
            tweekexceed.setText("Time Exceeded By : \n("+ getDurationString(m3)+" Hr)");
        }else {
            txt8.setText(ondutyused+":00  hr");
            tweekexceed.setVisibility(View.INVISIBLE);


        }

        tondutyallowd.setText(onduty_limit+" "+"hr");
        Resources resources = getResources();
        Collection<FitChartValue> values = new ArrayList<>();
        values.add(new FitChartValue(100f, resources.getColor(R.color.lightblue)));
        fitChart4.setValues(values);
    }
    private void callbreak()
    {


        Resources resources = getResources();
        Collection<FitChartValue> values = new ArrayList<>();
        values.add(new FitChartValue(100f, resources.getColor(R.color.lightblue)));
        fitChart2.setValues(values);
    }
//


    public String printsum(long different) {

        //	long secondsInMilli = 1000;
        long minutesInMilli =  60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different ;

        if(String.valueOf(elapsedHours).contains("-") || String.valueOf(elapsedMinutes).contains("-"))
        {
            return elapsedHours + ":" + elapsedMinutes ;
        }else {
            return pad(elapsedHours) + ":" + pad(elapsedMinutes) ;
        }

    }
    public String printsumdd(long different) {

    //	long secondsInMilli = 1000;
    long minutesInMilli = 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 168;

    long elapsedDays = different / daysInMilli;
    different = different % daysInMilli;

    long elapsedHours = different / hoursInMilli;
    different = different % hoursInMilli;

    long elapsedMinutes = different / minutesInMilli;
    different = different % minutesInMilli;

    long elapsedSeconds = different;

    if (String.valueOf(elapsedHours).contains("-") || String.valueOf(elapsedMinutes).contains("-")) {
        return elapsedHours + ":" + elapsedMinutes;
    } else {
        return pad(elapsedHours) + ":" + pad(elapsedMinutes);
    }


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
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           ActionBar ab = getSupportActionBar();
            ab.setTitle("Summary Details");
            ab.hide();
           // ab.setSubtitle("sub-title");
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    }
    class OnSwipeTouchListener implements View.OnTouchListener {

        //  private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
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
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
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
    protected void onPause() {
       // mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void cancelprogresssdialog()
    {
        try {
            if ((dialogz != null) && dialogz.isShowing()) {
                dialogz.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            //Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
           // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialogz = null;
        }
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
       // Log.e("called","************logout1");
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
           // finish();
          //  finishAffinity();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                finishAndRemoveTask();
//            }else{
//                finishAffinity();
//            }
            finishAffinity();
            Intent mIntent = new Intent(context,
                    Loginactivitynew.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("EXIT", true);
            startActivity(mIntent);
        }catch (Exception e)
        {
           // Log.e("serviceer","@"+e.toString());
        }




        // ((Activity) context). finishAndRemoveTask();
    }
    public void cancelnotification() {


        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        T.cancel();
    }

    private void updateTimeStringsTimerWorker()
    {
        SimpleDateFormat formatdatetimegg = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String time = formattime.format(new Date());
        int s1 = splittime(str_driveused);
        //Log.e("timeused",""+time);
        int s2 = splittimenew(time);
      //  Log.e("s2s2",""+s2);
       // Log.e("str_currenttime",""+str_currenttime);
        int s3=splittimenew(str_currenttime);
      //  Log.e("s3s3",""+s3);
        int s4=s2-s3;
        int res=s1-s4;
        String sk=""+res;
        if(strcurrentstatus.contentEquals("DRIVING")) {
            if (sk.contains("-")) {
                txt2.setText("00:00:00" + " hr");
                tdailydrive.setVisibility(View.VISIBLE);
                tdriveexceed.setVisibility(View.VISIBLE);
                int md1=splittime(mr_driveonused);
                int md2=splittime(mr_drivelimit);
                int md3=md1-md2;
                tdriveexceed.setText("Time Exceeded By : \n("+ getDurationString(md3+s4)+" Hr)");
            } else {
                tdriveexceed.setVisibility(View.INVISIBLE);
                txt2.setText("" + getDurationString(res) + " hr");
            }
        }
        if(strcurrentstatus.contentEquals("DRIVING") || strcurrentstatus.contentEquals("ON_DUTY")) {
            //Log.e("str_ondutyeusedzzz","&"+str_ondutyeused);
            int ons1 = splittime(str_ondutyeused);
           // Log.e("ons1","&"+ons1);
           // Log.e("s4s4","&"+s4);
            int onsres = ons1 - s4;
           // Log.e("onsres","&"+onsres);

            if( getDurationString(onsres).contains("-"))
            {
                txt6.setText("00:00:00 hr");
                tonexceed.setVisibility(View.VISIBLE);
                int mr1=splittime(mr_onused);
               // Log.e("mr1","@"+mr1);
                int mr2=splittime(mr_onlimit);
               // Log.e("mr2","@"+mr2);
                int mr3=mr1-mr2;
                txt6.setText("00:00:00" + " hr");
                tonexceed.setText("Time Exceeded By : \n("+ getDurationString(mr3+s4)+" Hr)");
            }else {
                txt6.setText("" + getDurationString(onsres) + " hr");
                tonexceed.setVisibility(View.INVISIBLE);
            }




            //   int onweeks0gg1 = splittime(str_weeklydriveused);
            //Log.e("str_weeklyused", "##" + str_weeklyused);
            int onweeks01 = splittime(str_weeklyused);

            int onweeksres = onweeks01 - s4;
            if( getDurationString(onweeksres).contains("-"))
            {
                txt8.setText("00:00:00 hr");
                int m1=splittime(mr_weekonused);
                int m2=splittime(mr_weekallow);
                int m3=m1-m2;
                Log.e("exceed time", "##" + getDurationString(m3+s4));
                tweekexceed.setVisibility(View.VISIBLE);
                tweekexceed.setText("Time Exceeded By : \n("+ getDurationString(m3+s4)+" Hr)");
            }else {
                txt8.setText("" + getDurationString(onweeksres) + " hr");
                tweekexceed.setVisibility(View.INVISIBLE);


            }
        }





    }
    private String getDurationStringsk(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + ":" + twoDigitString(minutes) ;
        // return twoDigitString(hours) + " : " + twoDigitString(minutes) ;
    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

          return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
       // return twoDigitString(hours) + " : " + twoDigitString(minutes) ;
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
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
    private String gettimeonedate()
    {
        String tdate="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        Log.e("timezone","&"+timezone);
        try {
//        TimeZone tz = TimeZone.getTimeZone(""+timezone);
//        Calendar c = Calendar.getInstance(tz);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        tdate=dateFormat.format(c.getTime());

            Calendar c = Calendar.getInstance();
            Date date = c.getTime(); //current date and time in UTC
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setTimeZone(TimeZone.getTimeZone(timezone)); //format in given timezone
            tdate = df.format(date);

            Log.e("tdate","&"+tdate);
        }catch (Exception e)
        {

        }
        return tdate;
    }
    private String gettimezonetime()
    {
        String tzonetime="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        try{
            TimeZone tz = TimeZone.getTimeZone(""+timezone);
            Calendar c = Calendar.getInstance(tz);
            tzonetime=String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                    String.format("%02d" , c.get(Calendar.MINUTE));
        }catch (Exception e)
        {

        }
        return  tzonetime;
    }
}
