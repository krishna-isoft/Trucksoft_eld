package com.trucksoft.isoft.isoft_elog.Model_class;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Digit_Signature;
import com.isoft.trucksoft_elog.Isoft_activity.Home_activity_bluetooth;
import com.isoft.trucksoft_elog.Isoft_activity.MonthlyReport;
import com.isoft.trucksoft_elog.Isoft_activity.WeeklyReport;
import com.isoft.trucksoft_elog.Isoft_adapter.DailyReportBean;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Dialog_notification;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isoft on 30/12/17.
 */

public class Manual_Report
        extends Fragment implements DatePickerDialog.OnDateSetListener,Currentreportadap.refreshpage{

    Activity mActivity;
    // IsoftelogChartSub logview;

    Context contxt;
    DailyReportBean mBean;
    private LinearLayout list;
    ArrayList<String> arraylistsche = new ArrayList<>();
//    private LinearLayout list_second;
    private ImageView imprint;
    FrameLayout memecontentView;
    private long strdrive;
    long stron;
    ArrayList<String> arrayliststatus= new ArrayList<>();


    public ArrayList<DailyReportBean> mBeans;
    Preference pref;
    ProgressDialog dialog,progressdlog;
    ArrayList<String> arraylistschedule = new ArrayList<>();

    private TextView txtdate;
    WebView myWebView;
    private String driverid;

    ArrayList<HashMap<String, String>> arraylistschedulechart = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hasmapschedule = new HashMap<String, String>();
    String TAG = "Manual Report - ";
    // private  ImageView imggraph;

    private TextView imgback;
    private  ImageView imglogout;
    private CommonUtil commonUtil;
    private String currentdate;
    private  String lat,lon;
    private ImageView txtemail;
    private TextView txt_date;

    int progress=0;
    Bundle bundle;
    Font_manager_elog font_manager;
    private TextView txtdayrep;
    private TextView txtsevenrep;
    private TextView txtthirtyrep;
    private TextView txtcustrep;
    private TextView txtr1;
    private TextView txtr2;
    private TextView txtr3;
    private TextView txtr4;

    RecyclerView recyclerView;
    List<Daily_reportmodel> movies;
    Currentreportadap adapter;
    Eld_api api;
    String dc;

    Dialog_notification dialog_notification;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String breakid;
    FrameLayout frameview;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    //private TextView txtexception;
    private TextView txtclarify;
    private TextView txtokcertify;
    private String strsldate="";

    private String strstate="";
    private String straddress="";
    private TextView ttrip;
    private ImageView tfmcsa;
    Dialog dialogfmcsa;

    private static  TextView tfrom;
    private static TextView tto;
    private static int value=0;
    private int tripactive=0;
    Dialog dialogrkship;

    List<Summarymodel> resulttotal = new ArrayList<>();

    Dialog dialogprivacy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.manual_rep, container, false);
        contxt=getActivity();
        pref = Preference.getInstance(contxt);
        driverid=pref.getString(Constant.DRIVER_ID);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        mBean=new DailyReportBean();
        font_manager=new Font_manager_elog();
        movies = new ArrayList<>();
        bundle = getArguments();
        dialog_notification=new Dialog_notification();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chatactivity_listview2);
       // Log.e("calling","custom date report............");
        commonUtil = new CommonUtil(contxt);
        arraylistschedule = new ArrayList<>();
       // txtexception=rootView.findViewById(R.id.txtexce);
        txtokcertify=rootView.findViewById(R.id.tcert);
        txtclarify=rootView.findViewById(R.id.txtcertfy);
        frameview=rootView.findViewById(R.id.frame_memecontent);
        tfmcsa=rootView.findViewById(R.id.tfmcsa);
        imglogout=(ImageView) rootView.findViewById(R.id.iv_exit);
        txtemail=rootView.findViewById(R.id.temail);
        ttrip=rootView.findViewById(R.id.ttrip);
        imgback=(TextView)rootView.findViewById(R.id.tbt2);
        txt_date=(TextView)rootView.findViewById(R.id.txtdate);
        imgback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contxt));
        txtdayrep=(TextView)rootView.findViewById(R.id.txt_dayrep);
        txtsevenrep=(TextView)rootView.findViewById(R.id.txt_sevenrep);
        txtthirtyrep=(TextView)rootView.findViewById(R.id.txt_thirtyrep);
        txtcustrep=(TextView)rootView.findViewById(R.id.txt_custrep);
        txtr1=(TextView)rootView.findViewById(R.id.txt_r1);
        txtr2=(TextView)rootView.findViewById(R.id.txt_r2);
        txtr3=(TextView)rootView.findViewById(R.id.txt_r3);
        txtr4=(TextView)rootView.findViewById(R.id.txt_r4);
        txtdayrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contxt));
        txtsevenrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contxt));
        txtthirtyrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contxt));
        txtcustrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contxt));
        txtcustrep.setTextColor(Color.parseColor("#ffba00"));
        txtdayrep.setTextColor(Color.parseColor("#2d5d88"));
        txtsevenrep.setTextColor(Color.parseColor("#2d5d88"));
        txtthirtyrep.setTextColor(Color.parseColor("#2d5d88"));
        txtr4.setTextColor(Color.parseColor("#ffba00"));
        txtr1.setTextColor(Color.parseColor("#2d5d88"));
        txtr2.setTextColor(Color.parseColor("#2d5d88"));
        txtr3.setTextColor(Color.parseColor("#2d5d88"));
        list = (LinearLayout) rootView.findViewById(R.id.chatactivity_listview);
      //  list_second= (LinearLayout) rootView.findViewById(R.id.chatactivity_listview2);
        imprint=(ImageView)rootView.findViewById(R.id.imgprint) ;
        memecontentView = (FrameLayout)rootView.findViewById(R.id.frame_memecontent);
        imprint.setVisibility(View.INVISIBLE);//Constant.MONTHLY_DATE
       // SimpleDateFormat formatsec= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
       //  dc =formatsec.format(new Date());
        dc=gettimeonedate();
        lat=pref.getString(Constant.LATITUDE);
        lon=pref.getString(Constant.LONGITUDE);
        txtdate=(TextView)rootView.findViewById(R.id.tt3);
        txtdate.setText("  "+dc);
        // imggraph= (ImageView) findViewById(R.id.myBitmapz);
        currentdate=dc;

        myWebView = (WebView) rootView.findViewById(R.id.webview);


        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        // Log.e("grlink",""+"http://e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
        //myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
        myWebView.loadUrl("https://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);


        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
gettotalvalue(dc);
       // gettotalhour(dc);
        dialog = new ProgressDialog(contxt,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        adapter = new Currentreportadap(contxt, movies,strsldate,Manual_Report.this);


        adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contxt));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = ApiServiceGenerator.createService(Eld_api.class);

        load(0);


        SimpleDateFormat formatda= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dts=formatda.format(new Date());

        final String av=driverid+"-"+dts+".png";

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent12 = new Intent(getActivity(),
                        Home_activity_bluetooth.class);


                startActivity(mIntent12);

            }
        });

        txtclarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtclarify.getText().toString().contentEquals("Certify Now")
                        || txtclarify.getText().toString().contentEquals(" CERTIFY NOW ")) {
                    if(tripactive==1)
                    {
                    savecertify("" + dc, "1");
                }else{
                    showmissingtripnumalert();
                }
                }
            }
        });

        tfmcsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callfmcsa();
            }
        });
//*****************************************************************************

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              //  Log.e("call","........."+"rep..............");
                // checking for type intent filter
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    // new push notification is received
                    //handlePushNotification(intent);
                    String msg = intent.getStringExtra("message");
                    //Log.e("msg","........."+msg);
                    if(msg.contentEquals("send_notification"))
                    {
                        String type=intent.getStringExtra("statustype");
                        String status="";
                        String statustype="";
                        if(intent.hasExtra("estatus")) {
                            status = intent.getStringExtra("estatus");
                        }
                        if(intent.hasExtra("estatustype")) {
                            statustype = intent.getStringExtra("estatustype");
                        }
                        if(intent.hasExtra("breakid")) {
                            breakid = intent.getStringExtra("breakid");
                        }
                        if(statustype !=null && statustype.length()>0 && !statustype.contentEquals("null"))
                        {
                            if(statustype.contentEquals("0")) {
                                if(status ==null || status.length()==0 || status.contentEquals("null"))
                                {
                                    status=commonUtil.OFF_DUTY;
                                }
                                //setpopupnotification(type,status);


                                // dialogdia=
                                dialog_notification.setpopupnotification(getActivity(),context,type,status,breakid);
                                // dialogdia.show();
                            }
                        }
                    }
                }
            }
        };




        //*************************************************************************


        txtsevenrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WeeklyReport();
                setfragment(bundle, fragment);
            }
        });
        txtdayrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Dailyreport_main();
                setfragment(bundle, fragment);
            }
        });
        txtthirtyrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MonthlyReport();
                setfragment(bundle, fragment);
            }
        });

        txtr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WeeklyReport();
                setfragment(bundle, fragment);
            }
        });
        txtr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MonthlyReport();
                setfragment(bundle, fragment);
            }
        });
        txtr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Dailyreport_main();
                setfragment(bundle, fragment);
            }
        });
//        SimpleDateFormat formatz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        String myDatez = formatz.format(new Date());
        strsldate=gettimeonedate();
        txt_date.setText(""+gettimeonedate());
txt_date.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FragmentManager fm = getActivity().getFragmentManager();
        Calendar now = Calendar.getInstance();
        DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                Manual_Report.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(true); //set dark them for dialog?
        datepickerdialog.vibrate(true); //vibrate on choosing date?
        datepickerdialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        datepickerdialog.showYearPickerFirst(false); //choose year first?
        datepickerdialog.setAccentColor(Color.parseColor("#9C27A0")); // custom accent color
        datepickerdialog.setTitle("Please select a date"); //dialog title
        datepickerdialog.show(fm, "Datepickerdialog"); //show dialog
    }
});

        txtemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arraylistschedule = new ArrayList<>();
//                Intent intent=new Intent(context, Digit_Signature.class);
//                intent.putStringArrayListExtra("arraylistschedule",arraylistschedule);
//                intent.putStringArrayListExtra("arraylisttotal",arraylisttotal);
//                startActivity(intent);
                for(int i=0;i<movies.size();i++)
                {
                    Daily_reportmodel val=movies.get(i);
                    //Log.e("val",""+val.message);
                    if(val.message !=null && val.message.length()>0 && !val.message.contentEquals("null")) {
                        String address="--";
                        if(val.address==null || val.address.length()==0 || val.address.contentEquals("null"))
                        {
                            address="--";
                        }else
                        {
                            address=val.address;
                        }
                        String ttime="--";
                        String rmark="";
                        String dtime="--";
                        String vid="__";
                        String vinn="__";
                        if(val.ttime==null || val.ttime.length()==0 || val.ttime.contentEquals("null"))
                        {
                            ttime="__:__";
                        }else
                        {
                            ttime=val.ttime;
                        }
                        if(val.remark==null || val.remark.length()==0 || val.remark.contentEquals("null"))
                        {
                            rmark="__";
                        }else
                        {
                            rmark=val.remark;
                        }

                        if(val.vid==null || val.vid.length()==0 || val.vid.contentEquals("null"))
                        {
                            vid="__";
                        }else
                        {
                            vid=val.vid;
                        }
                        if(val.vin==null || val.vin.length()==0 || val.vin.contentEquals("null"))
                        {
                            vinn="__";
                        }else
                        {
                            vinn=val.vin;
                        }
                        if(val.dtime==null || val.dtime.length()==0 || val.dtime.contentEquals("null"))
                        {
                            dtime="--";
                        }else
                        {
                            dtime=val.dtime;
                        }
                        arraylistschedule.add(val.message + ">>" + val.ftime + ">>" + ttime + ">>" + dtime + ">>" + address+">>"+rmark+">>"+vid+">>"+vinn);
                    }
                }

                int check = ActivityCompat.checkSelfPermission(contxt, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (check == PackageManager.PERMISSION_GRANTED) {
                Intent intent=new Intent(contxt, Digit_Signature.class);
                intent.putStringArrayListExtra("arraylistschedule",arraylistschedule);
                Log.e("resulttotalsize", "" + resulttotal.size());
                intent.putExtra("totalval", (Serializable) resulttotal);
                startActivity(intent);
                 } else {
                 callprivacy();
                }
            }
        });
return rootView;
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        txt_date.setText(year+"-"+(++monthOfYear)+"-"+dayOfMonth);
        strsldate=txt_date.getText().toString();
        dc=txt_date.getText().toString();
        Log.e("dc",""+dc);
      // strdrive=00;sstron=00;
       // schedule_getdata(year+"-"+(+monthOfYear)+"-"+dayOfMonth);
        dialog = new ProgressDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

arraylistsche=new ArrayList<>();
        arraylistschedule=new ArrayList<>();
        arrayliststatus=new ArrayList<>();

        arraylistschedulechart=new ArrayList<HashMap<String, String>>();
        clear();
list.removeAllViews();

        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
       //  Log.e("grlink",""+"http://e-logbook.info/drivergraph.php?date="+year+"-"+(+monthOfYear)+"-"+dayOfMonth+"&driverid="+driverid+"&did="+driverid);
       // myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+year+"-"+(+monthOfYear)+"-"+dayOfMonth+"&driverid="+driverid+"&did="+driverid);
        myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);

      //  gettotalhour(dc);
gettotalvalue(dc);
        adapter = new Currentreportadap(contxt, movies,strsldate,Manual_Report.this);


        adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contxt));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = ApiServiceGenerator.createService(Eld_api.class);

        load(0);


    }

    @Override
    public void refreshrep(String id, String msg) {
        dialog = new ProgressDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        arraylistsche=new ArrayList<>();
        arraylistschedule=new ArrayList<>();
        arrayliststatus=new ArrayList<>();

        arraylistschedulechart=new ArrayList<HashMap<String, String>>();
        clear();
        list.removeAllViews();

        //Log.e("dc",""+dc);
        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        //Log.e("grlink",""+"http://e-logbook.info/drivergraph.php?date="+year+"-"+(+monthOfYear)+"-"+dayOfMonth+"&driverid="+driverid+"&did="+driverid);
        myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);

      //  gettotalhour(dc);
        gettotalvalue(dc);

        adapter = new Currentreportadap(contxt, movies,strsldate,Manual_Report.this);


        adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contxt));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = ApiServiceGenerator.createService(Eld_api.class);

        load(0);


    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            frameview.setAnimation(inAnimation);
            frameview.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            frameview.setAnimation(outAnimation);

            //dialogz.dismiss();
        }
    }





    public String removeFirstChar(String s){
        return s.substring(1);
    }







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

    public String pad(Long num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }

    public long splittime(String time)
    {

        String timeSplit[] = time.split(":");
        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 +  Integer.parseInt(timeSplit[1]) * 60 ;


        return seconds;
    }



    public String getTotal(String ftime, String totime) {
        String a = "";
        if (ftime != null && ftime.length() > 0 && totime != null && totime.length() > 0) {
//			DateFormat dateFormatter = new SimpleDateFormat("dd/M/yyyy");
//			dateFormatter.setLenient(false);
//			Date today = new Date();
//			String s = dateFormatter.format(today);

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("kk:mm");
            try {
                // Log.e("ftime",""+ftime);
                // Log.e("totime",""+totime);
                Date date1 = simpleDateFormat.parse(ftime);
                Date date2 = simpleDateFormat.parse(totime);
                a = printDifference(date1, date2);
            } catch (Exception e) {

            }
        }

        return a;
    }
    public String printDifference(Date startDate, Date endDate) {
//Log.e("startDate",""+startDate);
        // Log.e("endDate",""+endDate);
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        // Log.e("endDate.getTime()",""+endDate.getTime());
        // Log.e("startDate.getTime()",""+startDate.getTime());

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


        return pad(elapsedHours) + ":" + pad(elapsedMinutes) ;

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


        return pad(elapsedHours) + ":" + pad(elapsedMinutes) ;

    }


    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(Current_scheduleActivity.this,
                Home_activity.class);
        startActivity(mIntent);
        finish();
    }
    */


    public void setfragment(Bundle bundle, Fragment fragment) {
        fragment.setArguments(bundle);
        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit();
    }
    private void load(int index){



        String page="0";
       // Log.e("index",""+index);
       // Log.e("vin",""+pref.getString(Constant.VIN_NUMBER));
       // Log.e("s_date",""+dc);
       // Log.e("page",""+page);
        //Log.e("did",""+pref.getString(Constant.DRIVER_ID));

        Call<List<Daily_reportmodel>> call = api.getdaylog(index,pref.getString(Constant.VIN_NUMBER),dc,page,pref.getString(Constant.DRIVER_ID),"");

        call.enqueue(new Callback<List<Daily_reportmodel>>() {
            @Override
            public void onResponse(Call<List<Daily_reportmodel>> call, Response<List<Daily_reportmodel>> response) {
               // Log.e(" Responsev"," "+response.toString());
                // Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                   cancelprogresssdialog();
                    //Log.e(" Responsecqe","z "+response.body());
                    movies.addAll(response.body());

                    //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                  cancelprogresssdialog();
                    // load(0);
                  //  Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Daily_reportmodel>> call, Throwable t) {
                //Log.e(TAG," Response Error "+t.getMessage());
                cancelprogresssdialog();
            }
        });
    }

    private void loadMore(int index){

        //add loading progress view
        movies.add(new Daily_reportmodel("load"));
        adapter.notifyItemInserted(movies.size()-1);

        String page=pref.getString(Constant.LAST_INDEX);
        Call<List<Daily_reportmodel>> call = api.getdaylog(index,pref.getString(Constant.VIN_NUMBER),dc,page,pref.getString(Constant.DRIVER_ID),"");
        call.enqueue(new Callback<List<Daily_reportmodel>>() {
            @Override
            public void onResponse(Call<List<Daily_reportmodel>> call, Response<List<Daily_reportmodel>> response) {
                // Log.e(" Response","z "+response.toString());
                //Log.e(" Responsesskk"," z"+String.valueOf(response.code()));

                if(response.isSuccessful()){
                    //  Log.e(" Responsec","z "+response.body());
                    //remove loading view
                    movies.remove(movies.size()-1);

                    List<Daily_reportmodel> result = response.body();
                    if(result.size()>0){
                        //add loaded data
                        movies.addAll(result);
                    }else{//result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(contxt,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                   // Log.e(TAG," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Daily_reportmodel>> call, Throwable t) {
                //Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }
    public void clear() {
        int size = this.movies.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.movies.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(contxt).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));
        Trucksoft_elog_Notify_Utils.clearNotifications(contxt);
    }
    @Override
    public void onDestroy() {
        cancelprogresssdialog();

        super.onDestroy();
    }
    private void cancelprogresssdialog()
    {
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
         //   Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
           // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialog = null;
        }

        try {
            if ((progressdlog != null) && progressdlog.isShowing()) {
                progressdlog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            //   Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            progressdlog = null;
        }
    }


    private  void savecertify(String date,String status) {
        progressdlog = new ProgressDialog(contxt,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();

        String did = pref.getString(Constant.DRIVER_ID);
        // Log.e("date","@"+date);


        //  Log.e("valll","http://eld.e-logbook.info/elog_app/certify_log.php?driver="+did+"&date="+date+"&status="+status);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<Respp_model> call = api.savecertifystatus(""+did,date, ""+status,""+pref.getString(Constant.CURRENT_STATE),""+pref.getString(Constant.COMPANY_CODE));
        call.enqueue(new Callback<Respp_model>() {

            public void onResponse(Call<Respp_model> call, Response<Respp_model> response) {
                if (response.isSuccessful()) {
                    //Log.e("result","success");
                    Respp_model rk=new Respp_model();
                    rk=response.body();
                    String statdate = rk.cdate;
                    // Log.e("statdate","@"+statdate);
                    String stcontime="";
                    String mdate="";
                    String newdate="";
                    if(statdate !=null && statdate.length()>0)
                    {
                        StringTokenizer stkk=new StringTokenizer(statdate," ");
                        mdate=stkk.nextToken();
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date oneWayTripDate = input.parse(mdate);                 // parse input
                            newdate=output.format(oneWayTripDate);    // format output
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String _24HourTime = "";
                        if(stkk.hasMoreTokens())
                        {
                            _24HourTime=stkk.nextToken();
                        }
                        try {

                            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                            Date _24HourDt = _24HourSDF.parse(_24HourTime);
                            //   System.out.println(_24HourDt);
                            stcontime=_12HourSDF.format(_24HourDt);
                            // System.out.println(_12HourSDF.format(_24HourDt));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    txtokcertify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
                    txtokcertify.setVisibility(View.VISIBLE);
                    txtclarify.setVisibility(View.INVISIBLE);
                    cancelprogresssdialog();
                } else {
                    cancelprogresssdialog();
                    // Log.e("result","fail");
                }
            }

            @Override
            public void onFailure(Call<Respp_model> call, Throwable t) {
                // Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
            }
        });
    }

    public String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(contxt, Locale.getDefault());

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
                try {
                    if (straddress.contains("India")) {
                        straddress = "2020 W Whitendale Ave, Visalia, CA 93277, USA..";
                    }
                } catch (Exception e) {

                }
                //Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }



        return straddress;
    }
    @Override
    public void refreshrepload(String val) {

        Log.e("valsd","@"+val);
        Fragment fragment = new Manual_Report();
        setfragment(bundle, fragment);
    }

//    @Override
//    public void refreshrvisible(String val) {
//        if(val !=null && val.contentEquals("1"))
//        {
//            ttrip.setVisibility(View.VISIBLE);
//        }else if(val !=null && val.contentEquals("0"))
//        {
//            ttrip.setVisibility(View.GONE);
//        }
//
//        else{
//            ttrip.setVisibility(View.INVISIBLE);
//        }
//    }

    private void callfmcsa() {

        if (dialogfmcsa != null) {
            if (dialogfmcsa.isShowing()) {
                dialogfmcsa.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fmcsa_display_request, null);


        final Button btn_submit = dialogView.findViewById(R.id.btn_submit);
        tfrom = dialogView.findViewById(R.id.edt_datefrom);
        tto = dialogView.findViewById(R.id.edt_dateto);
        final EditText edtofficer=dialogView.findViewById(R.id.edt_officer);

        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        SimpleDateFormat formatsec = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String datesf = formatsec.format(new Date());
        tfrom.setText(""+datesf);
        tto.setText(""+datesf);

        dialogfmcsa = new Dialog(contxt, R.style.DialogTheme);
        dialogfmcsa.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", contxt));
        // txtstatus.setText(Html.fromHtml("" + pref.getString(Constant.BREAK_MESSAGE)));
        dialogfmcsa.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfmcsa.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogfmcsa.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogfmcsa.getWindow().setAttributes(lp);
        dialogfmcsa.show();
        txtalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfmcsa.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtofficer.getText().toString() !=null && edtofficer.getText().toString().length()>0) {
                    dialogfmcsa.dismiss();
                    sendfmcacode(tfrom.getText().toString(), tto.getText().toString(), edtofficer.getText().toString());
                }else{
                    Toast.makeText(contxt,"Please enter Enforcement officer code",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=1;
                DialogFragment newFragment2 = new SelectDateFragment();
                newFragment2.show(getFragmentManager(), "DatePicker");
            }
        });
        tto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=2;
                DialogFragment newFragment2 = new SelectDateFragment();
                newFragment2.show(getFragmentManager(), "DatePicker");
            }
        });


    }

    private void sendfmcacode(String datefrom,String dateto,String stroffcode) {
        String did = pref.getString(Constant.DRIVER_ID);
        try {

            if (OnlineCheck.isOnline(contxt)) {
                progressdlog = new ProgressDialog(contxt,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                api = FmcsaServiceGenerator.createService(Eld_api.class);
                Call<JsonObject> call = api.sendfmcsa(did, datefrom, dateto, "" + stroffcode);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        cancelprogresssdialog();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                Log.e("jsonresponse", jsonresponse.toString());
                            }
                        } else {


                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //Log.e("dd"," Response Error "+t.getMessage());
                        cancelprogresssdialog();

                    }
                });
            }
        }catch (Exception e)
        {

        }
    }

    public static class SelectDateFragment extends DialogFragment implements
            android.app.DatePickerDialog.OnDateSetListener {
        int yy, mm, dd;
        //int value;
//		public SelectDateFragment() {
//			// Required empty public constructor
//		}
//		public  SelectDateFragment(int i) {
//			// TODO Auto-generated constructor stub
//			value = i;
//		}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            yy = calendar.get(Calendar.YEAR);
            mm = calendar.get(Calendar.MONTH);
            dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new android.app.DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {



            // Log.e("month",""+month);
            // Log.e("value",""+value);
            if (month < 10 && day < 10) {
                if (value == 1)
                    tfrom.setText(year + "-0" + month + "-0" + day);
                else if (value == 2)
                    tto.setText(year + "-0" + month + "-0" + day);
            } else if (day < 10) {
                if (value == 1)
                    tfrom.setText(year + "-" + month + "-0" + day);
                else if (value == 2)
                    tto.setText(year + "-" + month + "-0" + day);
            } else if (month < 10) {
                if (value == 1)
                    tfrom.setText(year + "-0" + month + "-" + day);
                else if (value == 2)
                    tto.setText(year + "-0" + month + "-" + day);
            } else {
                if (value == 1)
                    tfrom.setText(year + "-" + month + "-" + day);
                else if (value == 2)

                    tto.setText(year + "-" + month + "-" + day);
            }




        }

    }
    private void showmissingtripnumalert()
    {
        LayoutInflater inflater = (LayoutInflater)contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.alert_shipdoc, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final EditText edt_number = dialogView.findViewById(R.id.edt_tripnum);
        dialogrkship = new Dialog(contxt, R.style.DialogTheme);



        dialogrkship.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrkship.setContentView(dialogView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrkship.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogrkship.getWindow().setAttributes(lp);
        dialogrkship.show();

        edt_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogrkship.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String did=pref.getString(Constant.DRIVER_ID);
                String vinnumber=""+pref.getString(Constant.VIN_NUMBER);

                String straction="save";
                String msg=edt_number.getText().toString().trim();
                api = ApiServiceGenerator.createService(Eld_api.class);
              //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
                Call<JsonObject> call = api.updatetripno(vinnumber,"0",did,""+msg,""+msg,straction,""+gettimeonedate());

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                //Log.e("jsonresponse", jsonresponse.toString());
                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null)
                                    {

                                        if(resp.has("status"))
                                        {
                                            String stat=resp.getString("status");

                                            if(stat !=null && stat.contentEquals("1")) {
                                                String msg = resp.getString("message");


                                                Toast.makeText(contxt, ""+msg, Toast.LENGTH_SHORT).show();


refreshrepload("");
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
                        // cancelprogresssdialogz();
                    }
                });

                dialogrkship.dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialogrkship.dismiss();
            }
        });
    }

    private void gettotalvalue(String dated){
        api = ApiServiceGenerator.createService(Eld_api.class);

//        Log.e("vbn","http://eld.e-logbook.info/elog_app/eld_pchart_value.php?hstate="+pref.getString(Constant.STATE_FIELD)+"&cstate="+pref.getString(Constant.CURRENT_STATE)+"&did="+pref.getString(Constant.DRIVER_ID)+
//                "&s_date="+dated+"&vin="+pref.getString(Constant.VIN_NUMBER)+"&cc="+pref.getString(Constant.COMPANY_CODE)+"&lat="+lat+"&lon="+lon+"&address="+straddress+"&state="+strstate+"&feature=Driver Summary");
//
        Call<List<Summarymodel>> call = api.getsummary(pref.getString(Constant.STATE_FIELD),pref.getString(Constant.CURRENT_STATE),""+pref.getString(Constant.DRIVER_ID),dated,pref.getString(Constant.VIN_NUMBER),pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Driver Summary");



        call.enqueue(new Callback<List<Summarymodel>>() {
            @Override
            public void onResponse(Call<List<Summarymodel>> call, Response<List<Summarymodel>> response) {
                if(response.isSuccessful()){
                    //Log.e("response","success");
                    resulttotal = response.body();
                    //Log.e("result","success"+response.body().toString());
                    //Log.e("result.size",""+resulttotal.size());
                    if(resulttotal.size()>0){
                        try {
//                            String driving = mJsonObject.getString("sdriving");
//                            String offduty = mJsonObject.getString("soffdutty");
//                            String sleep = mJsonObject.getString("ssleep");
//                            String onduty = mJsonObject.getString("sonduty");
//                            String imgurl = mJsonObject.getString("imgurl");
                            String driving = "";
                            String offduty = "";
                            String sleep = "";
                            String onduty = "";
                            String imgurl ="";
                            String ondutylimit="";
                            String drivelimit="";
                            String ondutyremains="";
                            String driveremains="";
                            for (int j = 0; j < resulttotal.size(); j++)
                            {
                                Summarymodel grt = resulttotal.get(j);
                                Log.e("ruled",""+grt.rule);
                                List<todaymodel> tl=new ArrayList<>();
                                //  List<weeklymodel> wl=new ArrayList<>();
                                tl=grt.today;
                                for(int k=0;k<tl.size();k++) {
                                    todaymodel today = tl.get(k);
                                    driving=today.driv_used;
                                    offduty=today.offduty_used;
                                    sleep=today.sleep_used;
                                    onduty=today.onduty_used;
                                    ondutylimit=today.onduty_limit;
                                    drivelimit=today.driv_limit;
                                    ondutyremains=today.onduty_remains;
                                    driveremains=today.driv_remains;
                                }

                                if(grt.trip_num !=null) {
                                    String trip_num = grt.trip_num;
                                    if (trip_num != null && trip_num.length() > 0 && !trip_num.contentEquals("null")) {
                                        int vala = Integer.parseInt(trip_num);
                                        if (vala > 0) {
                                            tripactive = 1;
                                        } else {
                                            tripactive = 0;
                                        }
                                    } else {
                                        tripactive = 0;
                                    }
                                } else {
                                    tripactive = 0;
                                }

                                if(grt.imgurl !=null)
                                {imgurl=grt.imgurl;
                                    pref.putString(Constant.GRAPH_IMURL,""+imgurl);
                                }else{
                                    pref.putString(Constant.GRAPH_IMURL,""+imgurl);
                                }
//                                if(grt.sevenDaysCertify!=null)
//                                {
//                                    sevendaysstatus =grt.sevenDaysCertify;
//                                    Log.e("sevendaysstatus","@"+sevendaysstatus);
//                                }

                                if(grt.certify !=null)
                                {
                                    String stat = grt.certify;
                                    if(stat.contentEquals("1"))
                                    {
                                        String statdate = grt.certifydate;
                                        String stcontime="";
                                        String mdate="";
                                        String newdate="";
                                        if(statdate !=null && statdate.length()>0)
                                        {
                                            StringTokenizer stkk=new StringTokenizer(statdate," ");
                                            mdate=stkk.nextToken();


                                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
                                            try {
                                                Date oneWayTripDate = input.parse(mdate);                 // parse input
                                                newdate=output.format(oneWayTripDate);    // format output
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                            String _24HourTime = "";
                                            if(stkk.hasMoreTokens())
                                            {
                                                _24HourTime=stkk.nextToken();
                                            }
                                            try {

                                                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                                Date _24HourDt = _24HourSDF.parse(_24HourTime);
                                                //   System.out.println(_24HourDt);
                                                stcontime=_12HourSDF.format(_24HourDt);
                                                // System.out.println(_12HourSDF.format(_24HourDt));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        // txtclarify.setBackgroundResource(R.drawable.button_activate_ident);
                                        txtokcertify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
                                        txtokcertify.setVisibility(View.VISIBLE);
                                        txtclarify.setVisibility(View.INVISIBLE);
                                    }else{
                                        txtclarify.setBackgroundResource(R.drawable.button_ident);
                                        txtclarify.setText(" CERTIFY NOW ");
                                        txtclarify.setVisibility(View.VISIBLE);
                                        txtokcertify.setVisibility(View.GONE);
                                    }

                                }else{
                                    txtclarify.setBackgroundResource(R.drawable.button_ident);
                                    txtclarify.setText(" CERTIFY NOW ");
                                    txtclarify.setVisibility(View.VISIBLE);
                                    txtokcertify.setVisibility(View.GONE);
                                }



                                pref.putString(Constant.GRAPH_IMURL,""+imgurl);

                                if(driving !=null && driving.length()>0 && !driving.contentEquals("null"))
                                {

                                }else
                                {
                                    driving="00:00";
                                }
                                if(offduty !=null && offduty.length()>0 && !offduty.contentEquals("null"))
                                {

                                }else
                                {
                                    offduty="00:00";
                                }
                                if(sleep !=null && sleep.length()>0 && !sleep.contentEquals("null"))
                                {

                                }else
                                {
                                    sleep="00:00";
                                }
                                if(onduty !=null && onduty.length()>0 && !onduty.contentEquals("null"))
                                {

                                }else
                                {
                                    onduty="00:00";
                                }
                                // Log.e("driving","@"+driving);
                                // Log.e("offduty","@"+offduty);
                                // Log.e("onduty","@"+onduty);
                                //Log.e("sleep","@"+sleep);
//                                arraylisttotal.add("ON" + ">>" +onduty);
//                                arraylisttotal.add("OFF" + ">>" +offduty);
//                                arraylisttotal.add("D" + ">>" +driving);
//                                arraylisttotal.add("SB" + ">>" +sleep);
                                strdrive+=splittime(driving);
                                stron+=splittime(onduty);
//                                if(sevendaysstatus !=null && sevendaysstatus.length()>0 && sevendaysstatus.contentEquals("0")) {
//
//                                    if(pref.getString(Constant.CERTIFY_ALERT_DIALOG) !=null && pref.getString(Constant.CERTIFY_ALERT_DIALOG).contentEquals("nodisplay")) {
//                                        tk= new CountDownTimer(3000, 1000) {
//
//                                            public void onTick(long millisUntilFinished) {
//                                                //Log.e(" remaining: ", "" + millisUntilFinished / 1000);
//                                                //here you can have your logic to set text to edittext
//                                            }
//
//                                            public void onFinish() {
//
//                                                if(ik==0) {
//                                                    if (dialogcertify != null) {
//                                                        if (dialogcertify.isShowing()) {
//                                                            dialogcertify.dismiss();
//                                                        }
//                                                    }
//                                                    logcertifyalert();
//                                                }else{
//                                                    if (dialogcertify != null) {
//                                                        if (dialogcertify.isShowing()) {
//                                                            dialogcertify.dismiss();
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                        }.start();
//                                    }
//                                }

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View ll = inflater.inflate(R.layout.daily_adap, null);

                                TextView txt_status = (TextView) ll
                                        .findViewById(R.id.tstatus);


                                TextView txt_start = (TextView) ll
                                        .findViewById(R.id.tstart);


                                TextView txt_duration = (TextView) ll.findViewById(R.id.tduration);

                                /////////////////////////

                                TextView txt_status1 = (TextView) ll
                                        .findViewById(R.id.tstatus1);


                                TextView txt_start1 = (TextView) ll
                                        .findViewById(R.id.tstart1);


                                //  TextView txt_duration1 = (TextView) ll.findViewById(R.id.tduration1);



                                TextView txt_status2 = (TextView) ll
                                        .findViewById(R.id.tstatus2);


//                        thomestate.setText("Home State : "+pref.getString(Constant.STATE_FIELD));
//                        txtstate.setText("Current state : "+pref.getString(Constant.CURRENT_STATE));
                                //txtstate1.setText(""+pref.getString(Constant.STATE_FIELD));
                                //txtstate2.setText(""+pref.getString(Constant.STATE_FIELD));
                                //txtstate3.setText(""+pref.getString(Constant.STATE_FIELD));
                                TextView txt_start2 = (TextView) ll
                                        .findViewById(R.id.tstart2);
                                TextView txt_timeallowdr = (TextView) ll
                                        .findViewById(R.id.tallowedtime2);

                                TextView txt_timeallowon = (TextView) ll
                                        .findViewById(R.id.tallowedtime3);
                                if(grt.rule !=null && grt.rule.length()>0)
                                {
                                    if(grt.rule.contentEquals("Federal Rule"))
                                    {

                                    }else{

                                    }
                                }
                                txt_timeallowdr.setText(""+drivelimit);

                                txt_timeallowdr.setTextColor(Color.parseColor("#17BD17"));

                                txt_timeallowon.setText(""+ondutylimit);

                                txt_timeallowon.setTextColor(Color.parseColor("#17BD17"));
                                TextView txt_status3 = (TextView) ll
                                        .findViewById(R.id.tstatus3);

                                TextView txt_duration2 = (TextView) ll.findViewById(R.id.tduration2);
                                TextView txt_start3 = (TextView) ll
                                        .findViewById(R.id.tstart3);
                                TextView txt_duration3 = (TextView) ll.findViewById(R.id.tduration3);

                                //	if (ststatus.contentEquals("ON")) {
                                txt_status.setBackgroundResource(R.color.Red);
                                //}else if (ststatus1.contentEquals("OFF")) {
                                txt_status1.setBackgroundResource(R.color.dd);
                                //}else if (ststatus2.contentEquals("D")) {
                                txt_status2.setBackgroundResource(R.color.golden);
                                //}else
                                txt_status3.setBackgroundResource(R.color.inroute);


                                //txt_status.setText(ststatus);
                                txt_start.setText(""+offduty);
                                txt_start1.setText(""+sleep);
                                //  txt_duration1.setText("455555436");
                                txt_duration2.setText(""+driveremains);
                                txt_duration3.setText(""+ondutyremains);


                                txt_start2.setText(""+driving);
                                txt_start3.setText(""+onduty);
                                list.addView(ll);

                            }

                        }catch (Exception e)
                        {
                            Log.e("gerror",""+e.toString());
                        }
                    }else{
                        // cancelprogresssdialog();
                        Toast.makeText(contxt,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                    Log.e("ddf"," Load More Response Error "+String.valueOf(response.code()));
                    //dialogz.dismiss();
                    // getdata();
                    //cancelprogresssdialog();
                }
            }

            @Override
            public void onFailure(Call<List<Summarymodel>> call, Throwable t) {
                Log.e("ddd"," Load More Response Error "+t.getMessage());
                //  cancelprogresssdialog();
            }
        });
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


    private void callprivacy()
    {
        if (dialogprivacy != null) {
            if (dialogprivacy.isShowing()) {
                dialogprivacy.dismiss();
            }
        }
        String val2=" \n  * When uploading images. \n \n " +
                " * Gerate reporte time save signature & report generate ";
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.file_privacy, null);
        final TextView btnsubmitz = dialogView.findViewById(R.id.tsubmit);
        final TextView tcancel = dialogView.findViewById(R.id.tcancel);
        final TextView lsub = dialogView.findViewById(R.id.lsub);
        lsub.setText(""+val2);
        dialogprivacy = new Dialog(contxt, R.style.DialogTheme);
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
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1024);
            }
        });
        tcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogprivacy.dismiss();
            }
        });

    }
}

