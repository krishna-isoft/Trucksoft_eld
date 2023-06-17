package com.trucksoft.isoft.isoft_elog.Model_class;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Digit_Signature;
import com.isoft.trucksoft_elog.Isoft_activity.MonthlyReport;
import com.isoft.trucksoft_elog.Isoft_activity.WeeklyReport;
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

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by isoft on 26/1/18.
 */

public class Dailyreport_main extends Fragment implements Currentreportadap.refreshpage {
    RecyclerView recyclerView;
    List<Daily_reportmodel> movies;
    Currentreportadap adapter;
    static Eld_api api;

   // String TAG = "MainActivity - ";
    Context contexts;
    Preference pref;
    ProgressDialog dialog;
     ProgressDialog progressdlog;
    Dialog dialogdia;
//    ProgressDialog dialogz;
    String dc;
    String selectdate;
    String ddd;
    String dsf;
  //  String dsftime;
    private LinearLayout list;
    int progress=0;
    CountDownTimer tk;
    String sk4 = "" ;
    String sk5 = "";
    String sk6 = "" ;
    Dialog dialogprivacy;
    Dialog dialogcontact;
    Font_manager_elog font_manager;
    private TextView txtdayrep;
    private TextView txtsevenrep;
    private TextView txtthirtyrep;
    private TextView txtcustrep;
    private TextView txtr1;
    private TextView txtr2;
    private TextView txtr3;
    private TextView txtr4;
    private String driverid;
    Bundle bundle;
    WebView myWebView;
    ArrayList<String> arraylistschedule = new ArrayList<>();
    ArrayList<String> arraylisttotal = new ArrayList<>();
    private long stroff,strdrive,strslp;
    long stron;

    private CommonUtil commonUtil;
    Dialog_notification dialog_notification;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String breakid;
    TextView txtrightarrow;
    TextView txtleftarrow;
    TextView txtslctdate;

    LinearLayout lc;
    TextView txtdates;
    String strslctdate="";
    TextView txtdrivername;
    private TextView txtkey;
    private TextView txtexception;
    FrameLayout frameview;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private TextView txtclarify;
    private TextView txtokcertify;
    private ImageView temail;
    LocationManager locationManager;
    boolean GpsStatus;

    private String lat="";
    private String lon="";
    private String strstate="";
    private String straddress="";

    private ImageView tfmcsa;

    Dialog dialogfmcsa;

    private static  TextView tfrom;
    private static TextView tto;
    private static int value=0;
    Dialog dialogcertify;
    private String sevendaysstatus="";
    private TextView ttrip;


    private int tripactive=0;
    Dialog dialogrkship;
    int ik=0;
    private TextView thomestate;
    List<Summarymodel> resulttotal = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.current_report, container,
                false);
        contexts=getActivity();
        commonUtil=new CommonUtil(contexts);

        dialog_notification=new Dialog_notification();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chatactivity_listview2);
       // recyclerView.setNestedScrollingEnabled(false);
       // recyclerView.setHasFixedSize(false);
        list = (LinearLayout) rootView.findViewById(R.id.chatactivity_listview);
        movies = new ArrayList<>();
      //Log.e("Oncreate","Oncreate");
        bundle = getArguments();

       arraylistschedule = new ArrayList<>();
     arraylisttotal = new ArrayList<>();
        pref=   Preference.getInstance(contexts);
        pref.putString(Constant.LAST_INDEX,"0");
       // gettimezonetime();
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        thomestate=rootView.findViewById(R.id.hstate);
        String drname=pref.getString(Constant.DRIVER_NAME);
        ttrip=rootView.findViewById(R.id.ttrip);
        txtokcertify=rootView.findViewById(R.id.tcert);
        txtdayrep=rootView.findViewById(R.id.txt_dayrep);
        txtsevenrep=rootView.findViewById(R.id.txt_sevenrep);
        txtclarify=rootView.findViewById(R.id.txtcertfy);
        temail=rootView.findViewById(R.id.temail);
        txtdrivername=rootView.findViewById(R.id.txtdr);
        txtdrivername.setText("Driver : "+drname);
        thomestate.setText("Home State : "+pref.getString(Constant.STATE_FIELD));
        txtthirtyrep=rootView.findViewById(R.id.txt_thirtyrep);
        lc=rootView.findViewById(R.id.linsc);
        txtkey=rootView.findViewById(R.id.txtkey);
        txtexception=rootView.findViewById(R.id.txtexce);
        frameview=rootView.findViewById(R.id.frame_memecontent);
        txtcustrep=rootView.findViewById(R.id.txt_custrep);
        txtdates=rootView.findViewById(R.id.txtdate);
        txtr1=rootView.findViewById(R.id.txt_r1);
        txtr2=rootView.findViewById(R.id.txt_r2);
        txtr3=rootView.findViewById(R.id.txt_r3);
        txtr4=rootView.findViewById(R.id.txt_r4);
        tfmcsa=rootView.findViewById(R.id.tfmcsa);
        myWebView = rootView.findViewById(R.id.webview);
        txtleftarrow= rootView.findViewById(R.id.leftarrow);
        txtrightarrow= rootView.findViewById(R.id.rightarrow);
        txtslctdate= rootView.findViewById(R.id.slctdate);
        txtleftarrow.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contexts));
        txtrightarrow.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contexts));
        txtdayrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contexts));
        txtsevenrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contexts));
        txtthirtyrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contexts));
        txtcustrep.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",contexts));
        txtcustrep.setTextColor(Color.parseColor("#2d5d88"));
        txtdayrep.setTextColor(Color.parseColor("#ffba00"));
        txtsevenrep.setTextColor(Color.parseColor("#2d5d88"));
        txtthirtyrep.setTextColor(Color.parseColor("#2d5d88"));

        txtr4.setTextColor(Color.parseColor("#2d5d88"));
        txtr1.setTextColor(Color.parseColor("#ffba00"));
        txtr2.setTextColor(Color.parseColor("#2d5d88"));
        txtr3.setTextColor(Color.parseColor("#2d5d88"));
        driverid=pref.getString(Constant.DRIVER_ID);
      //  logcertifyalert();
        //hideSoftKeyboard(getActivity());
        //SimpleDateFormat formatsec= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        //SimpleDateFormat formatdc= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
       // SimpleDateFormat formatsecdd= new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        ddd =gettimeonedatenformat();
       // dsf =formatsecdd.format(new Date());
  dsf=gettimeonedatenformat();

        dc=gettimeonedate();
        setdat(dc);
      //  txtslctdate.setText(""+dc);
        String key=pref.getString(Constant.ELD_KEY);
        if(key !=null && key.length()>0 && !key.contentEquals("null"))
        {
            txtkey.setText("ELD Ser.# : "+key);
        }else{
            txtkey.setText("ELD Ser.# : ");
        }
      //  selectdate=formatsec.format(new Date());
        selectdate=gettimeonedate();
        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        // Log.e("grlinkx",""+"http://e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);

//        lc.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("calling","swipe");
//                return gesture.onTouchEvent(event);
//            }
//        });
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
        ik=0;
       callgraph(dc);
        gettotalvalue(dc);
        //gettotalhour(dc);

        txtclarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   String drivertype=pref.getString(Constant.ELOG_DRIVER_TYPE);
                if(drivertype.contentEquals("1"))
                {
                    democontactalert();
                }else {
                    if (txtclarify.getText().toString().contentEquals("Certify Now") ||
                            txtclarify.getText().toString().contentEquals("Certify now")
                            || txtclarify.getText().toString().contentEquals(" CERTIFY NOW ")) {
                        if (tripactive == 1) {
                            savecertify("" + dc, "1");
                        } else {
                            showmissingtripnumalert();
                        }
                    }
                }
            }
        });
        temail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dir = new File(contexts.getFilesDir() + File.separator + getResources().getString(R.string.appname));
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }












                arraylistschedule = new ArrayList<>();
                for(int i=0;i<movies.size();i++)
                {
                    Daily_reportmodel val=movies.get(i);
                    //Log.e("val",""+val.message);
                    if(val.message !=null && val.message.length()>0 && !val.message.contentEquals("null")) {
                        String address="--";
                        String dtime="--";
                        String ttime="--";
                        String rmark="__";
                        String vid="__";
                        String vinn="__";
                        if(val.address==null || val.address.length()==0 || val.address.contentEquals("null"))
                        {
                            address="__";
                        }else
                        {
                            address=val.address;
                        }
                        if(val.dtime==null || val.dtime.length()==0 || val.dtime.contentEquals("null"))
                        {
                            dtime="--";
                        }else
                        {
                            dtime=val.dtime;
                        }
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
                        arraylistschedule.add(val.message + ">>" + val.ftime + ">>" + ttime + ">>" + dtime + ">>" + address +">>"+rmark+">>"+vid+">>"+vinn);
                    }
                }
                int check = ActivityCompat.checkSelfPermission(contexts, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (check == PackageManager.PERMISSION_GRANTED) {
//kkm
                    Intent intent=new Intent(contexts, Digit_Signature.class);
                intent.putStringArrayListExtra("arraylistschedule",arraylistschedule);
                //intent.putExtra("QuestionListExtra", movies);
                intent.putStringArrayListExtra("arraylisttotal",arraylisttotal);
               // Log.e("resulttotalsize", "" + resulttotal.size());
                intent.putExtra("totalval", (Serializable) resulttotal);
                startActivity(intent);
                  } else {
                   callprivacy();
                }
            }
        });

tfmcsa.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
           String drivertype=pref.getString(Constant.ELOG_DRIVER_TYPE);
                if(drivertype.contentEquals("1"))
                {
                    democontactalert();
                }else {
                    callfmcsa();
                }
    }
});
        txtleftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.e("000000", "" + dsf);
                int a = 0;
                StringTokenizer sk = new StringTokenizer(dsf, " ");
                String dcdate = sk.nextToken();
               // Log.e("1", "" + dcdate);
               // Log.e("2", "" + dc);
                   if(dcdate.contentEquals(dc))
                   {
                   //  Log.e("trueeeeeee","true");
                  }else {
            }
                   // a = getCountOfDays(dcdate, ddd);//2018/09/20
                    a = getCountOfDays(ddd, dcdate);//2018/09/20
              //  Log.e("a1",""+a);
                a-=1;
                if(a==0)
                {
                    txtrightarrow.setVisibility(View.INVISIBLE);
                }else {
                    txtrightarrow.setVisibility(View.VISIBLE);
                }

               // Log.e("a2",""+a);
                   // Log.e("result a",""+a);
                //}
              String ldate=  getYesterdayDateString(a);
             // Log.e("ldate",""+ldate);
                dsf=ldate;
                StringTokenizer skdd=new StringTokenizer(ldate," ");
                String slctdate=skdd.nextToken();
               // Log.e("slctdate",""+slctdate);
                setdat(""+convertdate(slctdate));
               // txtslctdate.setText(""+convertdate(slctdate));
                selectdate=""+convertdate(slctdate);
                dc=""+convertdate(slctdate);
                callgraph(slctdate);
                gettotalvalue(""+convertdate(slctdate));
               // gettotalhour(""+convertdate(slctdate));
               // loadshow(0,selectdate);
                //Log.e("cdate",""+dsf);
                clearData();
                api = ApiServiceGenerator.createService(Eld_api.class);

                adapter = new Currentreportadap(contexts, movies,selectdate,Dailyreport_main.this);


                adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                int index = movies.size() - 1;
                                loadMore(index,selectdate);
                            }
                        });
                        //Calling loadMore function in Runnable to fix the
                        // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
                    }
                });
                //recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
                recyclerView.addItemDecoration(new Dispatchline_decoder(2));
                recyclerView.setNestedScrollingEnabled(false);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);


                load(0,selectdate);




            }
        });
        txtrightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("000000", "" + dsf);
                int a = 0;
                StringTokenizer sk = new StringTokenizer(dsf, " ");
                String dcdate = sk.nextToken();
               // Log.e("1", "" + dcdate);
               // Log.e("2", "" + dc);
                if(dcdate.contentEquals(dc))
                {
                   // Log.e("trueeeeeee","true");
                }else {
                }
                // a = getCountOfDays(dcdate, ddd);//2018/09/20
                a = getCountOfDays(ddd, dcdate);//2018/09/20
              //  Log.e("a1",""+a);
                a+=1;
                if(a==0)
                {
                    txtrightarrow.setVisibility(View.INVISIBLE);
                }else {
                    txtrightarrow.setVisibility(View.VISIBLE);
                }

               // Log.e("a2",""+a);
                //Log.e("result a",""+a);
                //}
                String ldate=  getYesterdayDateString(a);
                //Log.e("ldate",""+ldate);
                dsf=ldate;
                StringTokenizer skdd=new StringTokenizer(ldate," ");
                String slctdate=skdd.nextToken();
                //Log.e("slctdate",""+slctdate);
               // txtslctdate.setText(""+convertdate(slctdate));
                setdat(""+convertdate(slctdate));

                selectdate=""+convertdate(slctdate);
                dc=""+convertdate(slctdate);
                callgraph(slctdate);
                gettotalvalue(""+convertdate(slctdate));
             //   gettotalhour(""+convertdate(slctdate));
              //  loadshow(0,selectdate);
                //Log.e("cdate",""+dsf);
                clearData();
                api = ApiServiceGenerator.createService(Eld_api.class);

                adapter = new Currentreportadap(contexts, movies,selectdate,Dailyreport_main.this);


                adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                int index = movies.size() - 1;
                                loadMore(index,selectdate);
                            }
                        });
                        //Calling loadMore function in Runnable to fix the
                        // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
                    }
                });
                //recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
                recyclerView.addItemDecoration(new Dispatchline_decoder(2));
                recyclerView.setNestedScrollingEnabled(false);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);


                load(0,selectdate);

            }
        });
//*****************************************************************************

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Log.e("call","........."+"rep..............");
                // checking for type intent filter
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    // new push notification is received
                    //handlePushNotification(intent);
                    String msg = intent.getStringExtra("message");
                   // Log.e("msg","........."+msg);
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
                                dialog_notification.setpopupnotification(getActivity(),contexts,type,status,breakid);
                           // dialogdia.show();
                            }
                        }
                    }else if (msg.contentEquals("send_status")) {
                    refreshrepload("");
                    }else if (msg.contentEquals("state_changed")) {
                        refreshrepload("");
                    }
                }
            }
        };




        //*************************************************************************






        txtsevenrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tk.cancel();
                }catch (Exception e)
                {

                }
                ik=1;
                Fragment fragment = new WeeklyReport();
                setfragment(bundle, fragment);
            }
        });
        txtcustrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tk.cancel();
                }catch (Exception e)
                {

                }
                ik=2;
                Fragment fragment = new Manual_Report();
                setfragment(bundle, fragment);
            }
        });
        txtthirtyrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tk.cancel();
                }catch (Exception e)
                {

                }
                ik=3;
                Fragment fragment = new MonthlyReport();
                setfragment(bundle, fragment);
            }
        });
        txtr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tk.cancel();
                }catch (Exception e)
                {

                }
                ik=1;
                Fragment fragment = new WeeklyReport();
                setfragment(bundle, fragment);
            }
        });
        txtr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ik=1;
                Fragment fragment = new MonthlyReport();
                setfragment(bundle, fragment);
            }
        });
        txtr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ik=1;
                Fragment fragment = new Manual_Report();
                setfragment(bundle, fragment);
            }
        });

        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        api = ApiServiceGenerator.createService(Eld_api.class);

        adapter = new Currentreportadap(contexts, movies,strslctdate,Dailyreport_main.this);


        adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index,selectdate);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


            load(0,selectdate);







        return rootView;
    }

private void callgraph(String dc)
{
    //Log.e("graphlink","https://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
    Log.e("garp",""+"https://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
   //myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
    myWebView.loadUrl("https://eld.e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
//myWebView.loadUrl("https://eld.e-logbook.info/drivergraph_new.php?date=2019-10-12&driverid=325");




}

    private void loadshow(int index,String datess){

       // Log.e("kd",""+datess);
        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        String page="0";
        clearData();
        Call<List<Daily_reportmodel>> call = api.getMovies(index,pref.getString(Constant.VIN_NUMBER),datess,page,pref.getString(Constant.DRIVER_ID));

        call.enqueue(new Callback<List<Daily_reportmodel>>() {
            @Override
            public void onResponse(Call<List<Daily_reportmodel>> call, Response<List<Daily_reportmodel>> response) {
               // Log.e(" Responsev"," "+response.toString());
               // Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
//                    if(dialog !=null && dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
                    cancelprogresssdialog();
                  //  Log.e(" Responsecqevv","z "+response.body());
                    movies.addAll(response.body());

                    //  adapter = new Currentreportadap(context, movies);
                    adapter = new Currentreportadap(contexts, movies,strslctdate,Dailyreport_main.this);

                    recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
                    recyclerView.addItemDecoration(new Dispatchline_decoder(2));
                    recyclerView.setNestedScrollingEnabled(false);
                    //recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter);
                }else{
                    cancelprogresssdialog();
                    // load(0);
                   // Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Daily_reportmodel>> call, Throwable t) {
              //  Log.e(TAG," Response Error "+t.getMessage());
                cancelprogresssdialog();
            }
        });
    }


    private void load(int index,String datess){

//Log.e("kd",""+datess);
        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        String page="0";
        //clearData();
        Log.e("curl","current_report_scrolling.php?index="+index+"&vin="+pref.getString(Constant.VIN_NUMBER)
        +"&s_date="+dc+"&page="+page+"&did="+pref.getString(Constant.DRIVER_ID)+"&testapi=");
        Call<List<Daily_reportmodel>> call = api.getdaylog(index,pref.getString(Constant.VIN_NUMBER),datess,page,pref.getString(Constant.DRIVER_ID),"");

        call.enqueue(new Callback<List<Daily_reportmodel>>() {
            @Override
            public void onResponse(Call<List<Daily_reportmodel>> call, Response<List<Daily_reportmodel>> response) {
              //   Log.e(" Responsev"," "+response.toString());
                // Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                   cancelprogresssdialog();
                   //  Log.e(" Responsecqevv","z "+response.body());
                    movies.addAll(response.body());

                  //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                    cancelprogresssdialog();
                    // load(0);
                   // Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Daily_reportmodel>> call, Throwable t) {
                //Log.e(TAG," Response Error "+t.getMessage());
               cancelprogresssdialog();
            }
        });
    }

    private void loadMore(int index,String selectdate){
//Log.e("call","loadmpre");
        //add loading progress view
        movies.add(new Daily_reportmodel("load"));
        adapter.notifyItemInserted(movies.size()-1);

        String page=pref.getString(Constant.LAST_INDEX);
       // Log.e("curlmore","current_report_scrolling.php?index="+index+"&vin="+pref.getString(Constant.VIN_NUMBER)
       //         +"&s_date="+selectdate+"&page="+page+"&did="+pref.getString(Constant.DRIVER_ID)+"&testapi=");
        Call<List<Daily_reportmodel>> call = api.getdaylog(index,pref.getString(Constant.VIN_NUMBER),selectdate,page,pref.getString(Constant.DRIVER_ID),"");
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
                        Toast.makeText(contexts,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                  //  Log.e(TAG," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Daily_reportmodel>> call, Throwable t) {
              //  Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }









    private void gettotalvalue(String dated){
        api = ApiServiceGenerator.createService(Eld_api.class);

//        Log.e("vbnzzz","http://eld.e-logbook.info/elog_app/eld_pchart_value.php?hstate="+pref.getString(Constant.STATE_FIELD)+"&cstate="+pref.getString(Constant.CURRENT_STATE)+"&did="+pref.getString(Constant.DRIVER_ID)+
//                "&s_date="+dated+"&vin="+pref.getString(Constant.VIN_NUMBER)+"&cc="+pref.getString(Constant.COMPANY_CODE)+"&lat="+lat+"&lon="+lon+"&address="+straddress+"&state="+strstate+"&feature=Driver Summary");

        Call<List<Summarymodel>> call = api.getsummary(pref.getString(Constant.STATE_FIELD),pref.getString(Constant.CURRENT_STATE),""+pref.getString(Constant.DRIVER_ID),dated,pref.getString(Constant.VIN_NUMBER),pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Driver Summary");



        call.enqueue(new Callback<List<Summarymodel>>() {
            @Override
            public void onResponse(Call<List<Summarymodel>> call, Response<List<Summarymodel>> response) {
                if(response.isSuccessful()){
                  //  Log.e("response","success");
                    resulttotal = response.body();
                  //  Log.e("result","success"+response.body().toString());
                  //  Log.e("result.size",""+resulttotal.size());
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
                            String todaycount="";
                            for (int j = 0; j < resulttotal.size(); j++)
                            {
                                Summarymodel grt = resulttotal.get(j);
                                //Log.e("ruled",""+grt.rule);
                                List<todaymodel> tl=new ArrayList<>();
                              //  List<weeklymodel> wl=new ArrayList<>();
                                tl=grt.today;
                                try {
                                    if(grt.today_count !=null) {
                                        todaycount = grt.today_count;
                                    }
                                }catch (Exception e)
                                {

                                }
                                if(grt.imgurl !=null)
                                {imgurl=grt.imgurl;
                                    pref.putString(Constant.GRAPH_IMURL,""+imgurl);
                                }else{
                                    pref.putString(Constant.GRAPH_IMURL,""+imgurl);
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

                                    if(tripactive==0) {
                                        if (driving.contentEquals("00:00") && onduty.contentEquals("00:00") && offduty.contentEquals("00:00")
                                                && sleep.contentEquals("00:00")) {
                                            tripactive=1;
                                        }else if(todaycount.contentEquals("0"))
                                        {
                                            tripactive=1;
                                        }
                                    }
                                }
                        if(grt.sevenDaysCertify!=null)
                        {
                            sevendaysstatus =grt.sevenDaysCertify;
                            //Log.e("sevendaysstatus","@"+sevendaysstatus);
                        }

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
                        arraylisttotal.add("ON" + ">>" +onduty);
                        arraylisttotal.add("OFF" + ">>" +offduty);
                        arraylisttotal.add("D" + ">>" +driving);
                        arraylisttotal.add("SB" + ">>" +sleep);
                        strdrive+=splittime(driving);
                        stron+=splittime(onduty);
                        if(sevendaysstatus !=null && sevendaysstatus.length()>0 && sevendaysstatus.contentEquals("0")) {

                            if(pref.getString(Constant.CERTIFY_ALERT_DIALOG) !=null && pref.getString(Constant.CERTIFY_ALERT_DIALOG).contentEquals("nodisplay")) {
                                tk= new CountDownTimer(10000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        //Log.e(" remaining: ", "" + millisUntilFinished / 1000);
                                        //here you can have your logic to set text to edittext
                                    }

                                    public void onFinish() {

                                        if(ik==0) {
                                            if (dialogcertify != null) {
                                                if (dialogcertify.isShowing()) {
                                                    dialogcertify.dismiss();
                                                }
                                            }
                                            logcertifyalert();
                                        }else{
                                            if (dialogcertify != null) {
                                                if (dialogcertify.isShowing()) {
                                                    dialogcertify.dismiss();
                                                }
                                            }
                                        }
                                    }

                                }.start();
                            }
                        }

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
                                txt_duration2.setText(""+driveremains);
                                txt_duration3.setText(""+ondutyremains);
                                txt_start2.setText(""+driving);
                        txt_start3.setText(""+onduty);
                        list.addView(ll);

//                        String except=mJsonObject.getString("exceptions");
//                        txtexception.setPaintFlags(txtexception.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                        if(except !=null && except.length()>0) {
//                            Log.e("rk", "" + except.toString());
//                            JSONObject job = new JSONObject(except);
//                            String resul = job.getString("result");
//
//                            String reasss = job.getString("reason");
//                            JSONArray jsonArray = new JSONArray(resul);
//                            txtexception.setText("Exceptions : "+jsonArray.length());
//                            txtexception.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    try {
//
//                                        if (jsonArray.length() > 0) {
//                                            final Dialog dialogex = new Dialog(contexts);
//                                            dialogex.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                            dialogex.setCancelable(false);
//                                            //dialogex.setTitle("Exception Detail");
//                                            dialogex.setContentView(R.layout.eceptionlistrep);
//
//                                            Button dialogcancel =dialogex.findViewById(R.id.btn_cancel);
//                                            TextView reason=dialogex.findViewById(R.id.edtreason);
//                                            LinearLayout linexcep = dialogex.findViewById(R.id.lin_excep);
//                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                                            lp.copyFrom(dialogex.getWindow().getAttributes());
//                                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                                            lp.gravity = Gravity.CENTER;
//
//                                            dialogex.getWindow().setAttributes(lp);
//                                            //   reason.setVisibility(View.GONE);
//                                            if(reasss !=null && reasss.length()>0 && !reasss.contentEquals("null")) {
//                                                reason.setText("          Reason : "+reasss);
//                                            }
//                                            for (int k = 0; k < jsonArray.length(); k++) {
//                                                JSONObject jk = new JSONObject();
//                                                jk = jsonArray.getJSONObject(k);
//                                                String sk1 = "" + jk.getString("type");
//                                                String sk2 = "" + jk.getString("article");
//                                                String sk3 = "" + jk.getString("detail");
//
//                                                View layout2 = LayoutInflater.from(contexts).inflate(R.layout.ex_list, linexcep, false);
//
//                                                TextView txttype =  layout2.findViewById(R.id.txt_type);
//                                                TextView txtdetail =  layout2.findViewById(R.id.txt_detail);
//                                                TextView txtid =  layout2.findViewById(R.id.txt_id);
//                                                CheckBox chselect=layout2.findViewById(R.id.ch_select);
//                                                chselect.setVisibility(View.INVISIBLE);
//                                                if(sk2 !=null&& sk2.length()>0)
//                                                {
//                                                    txttype.setText(sk1+"("+sk2+")");
//                                                }else
//                                                {
//                                                    txttype.setText(sk1);
//                                                }
//                                                txtdetail.setText(""+sk3);
//                                                linexcep.addView(layout2);
//                                            }
//                                            dialogcancel.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    dialogex.dismiss();
//
//
//                                                }
//                                            });
//
//
//                                            dialogex.show();
//                                        }
//                                    }catch (Exception e)
//                                    {
//
//                                    }
//                                }
//                            });
//
//                        }
                            }

                        }catch (Exception e)
                        {
//Log.e("gerror",""+e.toString());
                        }
                    }else{
                       // cancelprogresssdialog();
                        Toast.makeText(contexts,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                     //Log.e("ddf"," Load More Response Error "+String.valueOf(response.code()));
                    //dialogz.dismiss();
                    // getdata();
                    //cancelprogresssdialog();
                }
            }

            @Override
            public void onFailure(Call<List<Summarymodel>> call, Throwable t) {
               // Log.e("ddd"," Load More Response Error "+t.getMessage());
              //  cancelprogresssdialog();
            }
        });
    }

//    private void gettotalhour(String sdate)
//    {
//        if (dialogcertify != null) {
//            if (dialogcertify.isShowing()) {
//                dialogcertify.dismiss();
//            }
//        }
//           // dialogz = new ProgressDialog(context,
//                //    AlertDialog.THEME_HOLO_LIGHT);
//
//            if (OnlineCheck.isOnline(contexts)) {
//               // dialogz.setMessage("Please wait...");
//               // dialogz.setCancelable(false);
//               // dialogz.show();
//                WebServices.gettotalhr(contexts,sdate,""+straddress,""+strstate,"Day Report",
//                        new JsonHttpResponseHandler() {
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers,
//                                                  String responseString, Throwable throwable) {
//                                // TODO Auto-generated method stub
//                                super.onFailure(statusCode, headers,
//                                        responseString, throwable);
//                                // Log.e("sdsk", "k"+responseString);
//                               // Log.e("sdsk", "k"+throwable);
//                               // dialogz.dismiss();
//                                // CommonMethod.showMsg(getActivity(), ""+
//                                // responseString);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers,
//                                                  Throwable throwable, JSONArray errorResponse) {
//                                // TODO Auto-generated method stub
//                                super.onFailure(statusCode, headers, throwable,
//                                        errorResponse);
//                              //  Log.e("sds1", "k"+errorResponse);
//                               // dialogz.dismiss();
//                                // CommonMethod.showMsg(getActivity(), ""+
//                                // errorResponse);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers,
//                                                  Throwable throwable, JSONObject errorResponse) {
//                                // TODO Auto-generated method stub
//                                super.onFailure(statusCode, headers, throwable,
//                                        errorResponse);
//                              //  Log.e("sds2", "k"+errorResponse);
//                               // dialogz.dismiss();
//
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers,
//                                                  JSONArray response) {
//                                // TODO Auto-generated method stub
//                                super.onSuccess(statusCode, headers, response);
//                                //dialogz.dismiss();
//                                Log.e("responsech", "k"+response.toString());
//                                if (response != null) {
//
//
//                                    setValue(response);
//                                }
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers,
//                                                  JSONObject response) {
//                                // TODO Auto-generated method stub
//                                super.onSuccess(statusCode, headers, response);
//                                //dialogz.dismiss();
//                                //Log.e("response log", "k"+response.toString());
//
//                                try {
//
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers,
//                                                  String responseString) {
//                                // TODO Auto-generated method stub
//                                super.onSuccess(statusCode, headers, responseString);
//                               // dialogz.dismiss();
//                               // Log.e("response logz", "k"+responseString.toString());
//
//                                // CommonMethod.showMsg(getActivity(), ""+
//                                // responseString);
//                            }
//
//                        });
//            }
//
//
//    }

//    private void setValue(JSONArray response) {
//        strdrive=00;
//        stron=00;
//        list.removeAllViews();
//        try {
//            if (response.length() > 0) {
//
//                for (int i = 0; i < response.length(); i++) {
//                    JSONObject mJsonObject = response.getJSONObject(i);
//                    String driving = mJsonObject.getString("sdriving");
//                    String offduty = mJsonObject.getString("soffdutty");
//                    String sleep = mJsonObject.getString("ssleep");
//                    String onduty = mJsonObject.getString("sonduty");
//                    String imgurl = mJsonObject.getString("imgurl");
//                    if(mJsonObject.has("trip_num")) {
//                        String trip_num = mJsonObject.getString("trip_num");
//                        if (trip_num != null && trip_num.length() > 0 && !trip_num.contentEquals("null")) {
//                            int vala = Integer.parseInt(trip_num);
//                            if (vala > 0) {
//                                tripactive = 1;
//                            } else {
//                                tripactive = 0;
//                            }
//                        } else {
//                            tripactive = 0;
//                        }
//                    }
//                    if(mJsonObject.has("sevenDaysCertify"))
//                    {
//                        sevendaysstatus = mJsonObject.getString("sevenDaysCertify");
//                        //Log.e("sevendaysstatus","@"+sevendaysstatus);
//                    }
//
//if(mJsonObject.has("certify"))
//{
//    String stat = mJsonObject.getString("certify");
//    if(stat.contentEquals("1"))
//    {
//        String statdate = mJsonObject.getString("certifydate");
//        String stcontime="";
//        String mdate="";
//        String newdate="";
//        if(statdate !=null && statdate.length()>0)
//        {
//            StringTokenizer stkk=new StringTokenizer(statdate," ");
//            mdate=stkk.nextToken();
//
//
//            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
//            try {
//                Date oneWayTripDate = input.parse(mdate);                 // parse input
//                newdate=output.format(oneWayTripDate);    // format output
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//
//            String _24HourTime = "";
//            if(stkk.hasMoreTokens())
//            {
//                _24HourTime=stkk.nextToken();
//            }
//            try {
//
//                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
//                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
//                Date _24HourDt = _24HourSDF.parse(_24HourTime);
//             //   System.out.println(_24HourDt);
//                stcontime=_12HourSDF.format(_24HourDt);
//               // System.out.println(_12HourSDF.format(_24HourDt));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//       // txtclarify.setBackgroundResource(R.drawable.button_activate_ident);
//        txtokcertify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
//        txtokcertify.setVisibility(View.VISIBLE);
//        txtclarify.setVisibility(View.INVISIBLE);
//    }else{
//        txtclarify.setBackgroundResource(R.drawable.button_ident);
//        txtclarify.setText(" CERTIFY NOW ");
//        txtclarify.setVisibility(View.VISIBLE);
//        txtokcertify.setVisibility(View.GONE);
//    }
//
//}
//
//
//
//                    pref.putString(Constant.GRAPH_IMURL,""+imgurl);
//
//                    if(driving !=null && driving.length()>0 && !driving.contentEquals("null"))
//                    {
//
//                    }else
//                    {
//                        driving="00:00";
//                    }
//                    if(offduty !=null && offduty.length()>0 && !offduty.contentEquals("null"))
//                    {
//
//                    }else
//                    {
//                        offduty="00:00";
//                    }
//                    if(sleep !=null && sleep.length()>0 && !sleep.contentEquals("null"))
//                    {
//
//                    }else
//                    {
//                        sleep="00:00";
//                    }
//                    if(onduty !=null && onduty.length()>0 && !onduty.contentEquals("null"))
//                    {
//
//                    }else
//                    {
//                        onduty="00:00";
//                    }
//                   // Log.e("driving","@"+driving);
//                   // Log.e("offduty","@"+offduty);
//                   // Log.e("onduty","@"+onduty);
//                    //Log.e("sleep","@"+sleep);
//                    arraylisttotal.add("ON" + ">>" +onduty);
//                    arraylisttotal.add("OFF" + ">>" +offduty);
//                    arraylisttotal.add("D" + ">>" +driving);
//                    arraylisttotal.add("SB" + ">>" +sleep);
//                    strdrive+=splittime(driving);
//                    stron+=splittime(onduty);
//if(sevendaysstatus !=null && sevendaysstatus.length()>0 && sevendaysstatus.contentEquals("0")) {
//
//    if(pref.getString(Constant.CERTIFY_ALERT_DIALOG) !=null && pref.getString(Constant.CERTIFY_ALERT_DIALOG).contentEquals("nodisplay")) {
//       tk= new CountDownTimer(3000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                //Log.e(" remaining: ", "" + millisUntilFinished / 1000);
//                //here you can have your logic to set text to edittext
//            }
//
//            public void onFinish() {
//
//                if(ik==0) {
//                    if (dialogcertify != null) {
//                        if (dialogcertify.isShowing()) {
//                            dialogcertify.dismiss();
//                        }
//                    }
//                    logcertifyalert();
//                }else{
//                    if (dialogcertify != null) {
//                        if (dialogcertify.isShowing()) {
//                            dialogcertify.dismiss();
//                        }
//                    }
//                }
//            }
//
//        }.start();
//    }
//}
//
//                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View ll = inflater.inflate(R.layout.daily_adap, null);
//
//                    TextView txt_status = (TextView) ll
//                            .findViewById(R.id.tstatus);
////                    TextView thomestate=ll
////                            .findViewById(R.id.tomestate);
//
//                    TextView txt_start = (TextView) ll
//                            .findViewById(R.id.tstart);
//
//
//                    TextView txt_duration = (TextView) ll.findViewById(R.id.tduration);
//
//                    /////////////////////////
//
//                    TextView txt_status1 = (TextView) ll
//                            .findViewById(R.id.tstatus1);
//
//
//                    TextView txt_start1 = (TextView) ll
//                            .findViewById(R.id.tstart1);
//
//
//                  //  TextView txt_duration1 = (TextView) ll.findViewById(R.id.tduration1);
//
//
//
//                    TextView txt_status2 = (TextView) ll
//                            .findViewById(R.id.tstatus2);
//
//
//
////                    thomestate.setText("Home State : "+pref.getString(Constant.STATE_FIELD));
////                    txtstate.setText("Current state : "+pref.getString(Constant.CURRENT_STATE));
//                    //txtstate1.setText(""+pref.getString(Constant.STATE_FIELD));
//                    //txtstate2.setText(""+pref.getString(Constant.STATE_FIELD));
//                    //txtstate3.setText(""+pref.getString(Constant.STATE_FIELD));
//                    TextView txt_start2 = (TextView) ll
//                            .findViewById(R.id.tstart2);
//                    TextView txt_timeallowdr = (TextView) ll
//                            .findViewById(R.id.tallowedtime2);
//
//                    TextView txt_timeallowon = (TextView) ll
//                            .findViewById(R.id.tallowedtime3);
//                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
//                        txt_timeallowdr.setText("11:00");
//                    }else{
//                        txt_timeallowdr.setText(""+pref.getString(Constant.HOME_DRIVE_HOURS));
//                    }
//                    txt_timeallowdr.setTextColor(Color.parseColor("#17BD17"));
//                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
//                        txt_timeallowon.setText("14:00");
//                    }else{
//                        txt_timeallowon.setText(""+pref.getString(Constant.HOME_ONDUTY_HOURS));
//                    }
//                    txt_timeallowon.setTextColor(Color.parseColor("#17BD17"));
//                    TextView txt_status3 = (TextView) ll
//                            .findViewById(R.id.tstatus3);
//
//                    TextView txt_duration2 = (TextView) ll.findViewById(R.id.tduration2);
//                    TextView txt_start3 = (TextView) ll
//                            .findViewById(R.id.tstart3);
//                    TextView txt_duration3 = (TextView) ll.findViewById(R.id.tduration3);
//
//                    //	if (ststatus.contentEquals("ON")) {
//                    txt_status.setBackgroundResource(R.color.Red);
//                    //}else if (ststatus1.contentEquals("OFF")) {
//                    txt_status1.setBackgroundResource(R.color.dd);
//                    //}else if (ststatus2.contentEquals("D")) {
//                    txt_status2.setBackgroundResource(R.color.golden);
//                    //}else
//                    txt_status3.setBackgroundResource(R.color.inroute);
//
//
//                    //txt_status.setText(ststatus);
//                    txt_start.setText(""+offduty);
//                  //  txt_duration1.setText("455555436");
//
//                    long sptime = splittime(""+pref.getString(Constant.HOME_DRIVE_HOURS));
//                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
//                        sptime = splittime("11:00:00");
//                    }else{
//                        sptime = splittime(""+pref.getString(Constant.HOME_DRIVE_HOURS));
//                    }
//
//
//
//
//                    long newtime = sptime - strdrive;
//                    //Log.e("newtime",""+newtime);
//                    String ak = printsum(newtime);
//                    //Log.e("ak",""+ak);
//
//                    long optime = splittime("16:00:00");
//                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
//                        optime = splittime("14:00:00");
//                    }else {
//                        optime = splittime(""+pref.getString(Constant.HOME_ONDUTY_HOURS));
//                    }
//
//                    long dfff = stron+strdrive;
//                    String opkff = printsum(dfff);
//
//
//
//
//
//
//                    long opnewtime = optime - stron-strdrive;
//                    String opk = printsum(opnewtime);
//                    if(ak.contains("-")) {
//
//
//                            if(ak.contains(":")){
//                                StringTokenizer sgh=new StringTokenizer(ak,":");
//                                String f1=sgh.nextToken();
//                                String f2=sgh.nextToken();
//                                if(f2.contains("-"))
//                                {
//                                    f2=removeFirstChar(f2);
//                                    // Log.e("ak11",""+opk);
//                                }
//                                if(f1.contains("-"))
//                                {
//                                    //Log.e("ak111",""+opk);
//                                    txt_duration2.setText(f1+":"+f2);
//                                }else{
//                                    //   Log.e("ak1111",""+f1+":"+f2);
//                                    txt_duration2.setText("-"+f1+":"+f2);
//                                }
//                            }
//
//
//                            //txt_duration2.setText(""+ak);
//
//
//                       // txt_duration2.setText(""+ak);
//                    }else
//                    {
//                        txt_duration2.setText(ak);
//                    }
////hh
//                    if(opk.contains("-")) {
//                        if(opk.contains(":"))
//                        {
//                            StringTokenizer sgh=new StringTokenizer(opk,":");
//                            String f1=sgh.nextToken();
//                            String f2=sgh.nextToken();
//                            if(f2.contains("-"))
//                            {
//                                f2=removeFirstChar(f2);
//                              //  Log.e("ak11",""+opk);
//                            }
//                            if(f1.contains("-"))
//                            {
//                                //Log.e("ak111",""+opk);
//                                txt_duration3.setText(f1+":"+f2);
//                            }else{
//                             //   Log.e("ak1111",""+f1+":"+f2);
//                                txt_duration3.setText("-"+f1+":"+f2);
//                            }
//                        }
//
//                       // txt_duration3.setText(""+opk);
//                    }else {
//                        txt_duration3.setText(opk);
//                    }
//                   // txt_status1.setText(ststatus1);
//                    txt_start1.setText(""+sleep);
//                   // txt_status2.setText(ststatus2);
//                    txt_start2.setText(""+driving);
//                   //opkff
//                    //txt_start3.setText(""+onduty);
//                    txt_start3.setText(""+opkff);
//                    list.addView(ll);
//
//                    String except=mJsonObject.getString("exceptions");
//                    txtexception.setPaintFlags(txtexception.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                    if(except !=null && except.length()>0) {
//                        //Log.e("rk", "" + except.toString());
//                        JSONObject job = new JSONObject(except);
//                        String resul = job.getString("result");
//
//                        String reasss = job.getString("reason");
//                        JSONArray jsonArray = new JSONArray(resul);
//                        txtexception.setText("Exceptions : "+jsonArray.length());
//                        txtexception.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                try {
//
//                                    if (jsonArray.length() > 0) {
//                                        final Dialog dialogex = new Dialog(contexts);
//                                        dialogex.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialogex.setCancelable(false);
//                                        //dialogex.setTitle("Exception Detail");
//                                        dialogex.setContentView(R.layout.eceptionlistrep);
//
//                                        Button dialogcancel =dialogex.findViewById(R.id.btn_cancel);
//                                        TextView reason=dialogex.findViewById(R.id.edtreason);
//                                        LinearLayout linexcep = dialogex.findViewById(R.id.lin_excep);
//                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                                        lp.copyFrom(dialogex.getWindow().getAttributes());
//                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                                        lp.gravity = Gravity.CENTER;
//
//                                        dialogex.getWindow().setAttributes(lp);
//                                     //   reason.setVisibility(View.GONE);
//                                        if(reasss !=null && reasss.length()>0 && !reasss.contentEquals("null")) {
//                                            reason.setText("          Reason : "+reasss);
//                                        }
//                                        for (int k = 0; k < jsonArray.length(); k++) {
//                                            JSONObject jk = new JSONObject();
//                                            jk = jsonArray.getJSONObject(k);
//                                            String sk1 = "" + jk.getString("type");
//                                            String sk2 = "" + jk.getString("article");
//                                            String sk3 = "" + jk.getString("detail");
//
//                                            View layout2 = LayoutInflater.from(contexts).inflate(R.layout.ex_list, linexcep, false);
//
//                                            TextView txttype =  layout2.findViewById(R.id.txt_type);
//                                            TextView txtdetail =  layout2.findViewById(R.id.txt_detail);
//                                            TextView txtid =  layout2.findViewById(R.id.txt_id);
//                                            CheckBox chselect=layout2.findViewById(R.id.ch_select);
//                                            chselect.setVisibility(View.INVISIBLE);
//                                            if(sk2 !=null&& sk2.length()>0)
//                                            {
//                                                txttype.setText(sk1+"("+sk2+")");
//                                            }else
//                                            {
//                                                txttype.setText(sk1);
//                                            }
//                                            txtdetail.setText(""+sk3);
//                                            linexcep.addView(layout2);
//                                        }
//                                        dialogcancel.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialogex.dismiss();
//
//
//                                            }
//                                        });
//
//
//                                        dialogex.show();
//                                    }
//                                }catch (Exception e)
//                                {
//
//                                }
//                            }
//                        });
//
//                    }
//                }
//
//
//            }
//        } catch (Exception e) {
//            //Log.e("edddddddd",""+e.toString());
//            // TODO: handle exception
//        }
//    }

    private int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }

    public String removeFirstChar(String s){
        return s.substring(1);
    }


    public long splittime(String time)
    {

        String timeSplit[] = time.split(":");
        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 +  Integer.parseInt(timeSplit[1]) * 60 ;


        return seconds;
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
    public void setfragment(Bundle bundle, Fragment fragment) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit();
    }

    @Override
    public void refreshrep(String id, String msg) {
        recyclerView.removeAllViews();
        movies=new ArrayList<>();
        //Log.e("calling","refresh"+selectdate);
      //  selectdate=""+convertdate(slctdate);
        list.removeAllViews();
        callgraph(selectdate);
        gettotalvalue(""+selectdate);
       // gettotalhour(selectdate);
        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        api = ApiServiceGenerator.createService(Eld_api.class);

        adapter = new Currentreportadap(contexts, movies,strslctdate,Dailyreport_main.this);


        adapter.setLoadMoreListener(new Currentreportadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index,selectdate);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });


        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        load(0,selectdate);

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
           // dialogz.dismiss();
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            frameview.setAnimation(outAnimation);
           // progressBarHolder.setVisibility(View.GONE);
//cancelprogresssdialog();

//            if(dialog !=null && dialog.isShowing() || dialogz !=null && dialogz.isShowing())
//            {
//
//            }else {
//
//
//                if(((Activity) getActivity())!=null)
//                {
//                    if (!((Activity) getActivity()).isFinishing()) {
//                        // progressDialog = new ProgressDialog(getActivity());
//                        dialogz = new ProgressDialog(contexts,
//                                AlertDialog.THEME_HOLO_LIGHT);
//                        dialogz.setMessage("Graph Loading...");
//                        dialogz.setCancelable(false);
//                        dialogz.show();//issue
//                        callprogress();
//                    }
//                }else {
//                    //Log.e("ckl", "//////////////////////closed");
//                }
//
//
//            }
            //dialogz.dismiss();
        }
    }

//    private void callprogress()
//    {
//        new Thread(new Runnable() {
//
//            public void run() {
//                long timerEnd = System.currentTimeMillis() + 14 * 700;
//
//                while (timerEnd >  System.currentTimeMillis()) {
//
//                    progress = 14 - (int) (timerEnd - System.currentTimeMillis()) / 700;
//                    // Update the progress bar
//
//                    progressBarHandler.post(new Runnable() {
//                        public void run() {
//
//                        }
//                    });
//
//                   /* try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        Log.w("App","Progress thread cannot sleep");
//                    }*/
//                }
//                progressBarHandler.post(new Runnable() {
//                    public void run() {
//                        if(dialogz !=null && dialogz.isShowing()) {
//                            dialogz.dismiss();
//                        }
//                    }
//                });
//            }
//        }).start();
//    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(contexts).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));
        Trucksoft_elog_Notify_Utils.clearNotifications(contexts);
    }

    @Override
    public void onDestroy() {
       cancelprogresssdialog();
        super.onDestroy();
    }
    private Date yesterday(int a) {
       // Log.e("aaaaaaaaaaaa",""+a);

        final Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, -a);
        cal.add(Calendar.DATE, a);
        return cal.getTime();
    }
    private String getYesterdayDateString(int a) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      //  Log.e("kkkk",""+dc);
        //Log.e("aa",""+a);
        //DateFormat dateFormat = new SimpleDateFormat(dc);
        return dateFormat.format(yesterday(a));
    }
    public int getCountOfDays(String createdDateString, String expireDateString) {
       // Log.e("createdDateString",""+createdDateString);
       // Log.e("expireDateString",""+expireDateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ( (int) dayCount);
    }
    private String convertdate(String dst)
    {
        StringTokenizer svd=new StringTokenizer(dst,"/");
        String a=svd.nextToken();
        a+="-"+svd.nextToken();
        a+="-"+svd.nextToken();
        return a;


    }
    public void clearData() {
        movies=new ArrayList<>(); //clear list
        adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
    }

//    final GestureDetector gesture = new GestureDetector(getActivity(),
//            new GestureDetector.SimpleOnGestureListener() {
//
//                @Override
//                public boolean onDown(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                                       float velocityY) {
//                    Log.e("jhkgjhk.APP_TAG", "onFling has been called!");
//                    final int SWIPE_MIN_DISTANCE = 120;
//                    final int SWIPE_MAX_OFF_PATH = 250;
//                    final int SWIPE_THRESHOLD_VELOCITY = 200;
//                    try {
//                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                            return false;
//                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                            Log.e("Constants.APP_TAG", "Right to Left");
//                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                            Log.e("Constants.APP_TAG", "Left to Right");
//                        }
//                    } catch (Exception e) {
//                        // nothing
//                        Log.e("a", ""+e.toString());
//                    }
//                    Log.e("dffd", ""+velocityY);
//                    return super.onFling(e1, e2, velocityX, velocityY);
//                }
//            });
    private void setdat(String myDatez)
    {strslctdate=myDatez;

        try {
            StringTokenizer skdte;
            if (myDatez.contains("-")) {
                skdte = new StringTokenizer(myDatez, "-");
                String styr = skdte.nextToken();
                String stmnth = skdte.nextToken();
                String stdte = skdte.nextToken();
                txtslctdate.setText(stdte+" "+getMonth(Integer.parseInt(stmnth)) + " " + styr);
            txtdates.setText(stdte+" "+getMonth(Integer.parseInt(stmnth)) + " " + styr);

            }
        }catch (Exception e)
        {

        }

    }
    public String getMonth(int month)
    {
        String monthtext = null;

        switch (month)
        {
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
    private void cancelprogresssdialog()
    {
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

    private  void savecertify(String date,String status) {
        progressdlog = new ProgressDialog(contexts,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();

        String did = pref.getString(Constant.DRIVER_ID);
        // Log.e("date","@"+date);


        //Log.e("valll","http://eld.e-logbook.info/elog_app/certify_log.php?driver="+did+"&date="+date+"&status="+status+"&state="+pref.getString(Constant.CURRENT_STATE)+"&cc="+pref.getString(Constant.COMPANY_CODE));
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
                    // txtclarify.setBackgroundResource(R.drawable.button_activate_ident);
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

        Geocoder geocoder = new Geocoder(contexts, Locale.getDefault());

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

        dialogfmcsa = new Dialog(contexts, R.style.DialogTheme);
        dialogfmcsa.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", contexts));
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
                    Toast.makeText(contexts,"Please enter Enforcement officer code",Toast.LENGTH_SHORT).show();
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
    public static class SelectDateFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
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
            return new DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this, yy, mm, dd);
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
    private void sendfmcacode(String datefrom,String dateto,String stroffcode) {
        String did = pref.getString(Constant.DRIVER_ID);
try {

    if (OnlineCheck.isOnline(contexts)) {
        progressdlog = new ProgressDialog(contexts,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = FmcsaServiceGenerator.createService(Eld_api.class);

        //Log.e("drvid",""+did);
        //Log.e("date_from",""+datefrom);
        //Log.e("date_to",""+dateto);
        //Log.e("inv_code",""+stroffcode);


        Call<JsonObject> call = api.sendfmcsa(did, datefrom, dateto, "" + stroffcode);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                cancelprogresssdialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonresponse = response.body().toString();
                        //Log.e("jsonresponse", jsonresponse.toString());
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



    private void logcertifyalert() {

try{
        if (dialogcertify != null) {
            if (dialogcertify.isShowing()) {
                dialogcertify.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.certify_alert, null);


        final Button btn_yes = dialogView.findViewById(R.id.btn_yes);



        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        dialogcertify = new Dialog(contexts, R.style.DialogTheme);
        dialogcertify.setCancelable(false);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", contexts));
        dialogcertify.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcertify.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogcertify.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogcertify.getWindow().setAttributes(lp);
        dialogcertify.show();


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogcertify.dismiss();
                } catch (Exception e) {

                }
                Fragment fragment = new WeeklyReport();
                setfragment(bundle, fragment);
                // ceritfychangestatus();
            }
        });



    }catch (Exception e)
{

}
    }



    public void CheckGpsStatus() {

        locationManager = (LocationManager) contexts.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void refreshrepload(String val) {

        //Log.e("valsd","@"+val);
        Fragment fragment = new Dailyreport_main();
        setfragment(bundle, fragment);
    }

//    @Override
//    public void refreshrvisible(String val) {
//if(val !=null && val.contentEquals("1"))
//{
//    ttrip.setVisibility(View.VISIBLE);
//}else if(val !=null && val.contentEquals("0"))
//{
//    ttrip.setVisibility(View.GONE);
//}
//
//else{
//    ttrip.setVisibility(View.INVISIBLE);
//}
//    }
private void showmissingtripnumalert()
{
    LayoutInflater inflater = (LayoutInflater)contexts.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    final View dialogView = inflater.inflate(R.layout.alert_shipdoc, null);
    final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
    final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
    final EditText edt_number = dialogView.findViewById(R.id.edt_tripnum);
    dialogrkship = new Dialog(contexts, R.style.DialogTheme);



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
            //Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
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


                                            Toast.makeText(contexts, ""+msg, Toast.LENGTH_SHORT).show();

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
    private String gettimeonedate()
    {
        String tdate="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        //Log.e("timezone","&"+timezone);
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

            //Log.e("tdate","&"+tdate);
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
            int a = c.get(Calendar.AM_PM);
            if(a == Calendar.AM)
            {
                Log.e("tzonetimeam","@"+tzonetime);
            }else{
                Log.e("tzonetimepm","@"+tzonetime);
            }
        }catch (Exception e)
        {

        }
        Convert24to12(tzonetime);
        Log.e("tzonetime","@"+tzonetime);
        return  tzonetime;
    }
    public  String Convert24to12(String time)
    {
        String convertedTime ="";
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
            Date date = parseFormat.parse(time);
            convertedTime=displayFormat.format(date);
            System.out.println("convertedTime : "+convertedTime);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        Log.e("convertedTime","@"+convertedTime);
        return convertedTime;
        //Output will be 10:23 PM
    }
    private String gettimeonedatenformat()
    {
        String tdate="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        //Log.e("timezone","&"+timezone);
        try {
//        TimeZone tz = TimeZone.getTimeZone(""+timezone);
//        Calendar c = Calendar.getInstance(tz);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        tdate=dateFormat.format(c.getTime());

            Calendar c = Calendar.getInstance();
            Date date = c.getTime(); //current date and time in UTC
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            df.setTimeZone(TimeZone.getTimeZone(timezone)); //format in given timezone
            tdate = df.format(date);

            //Log.e("tdate","&"+tdate);
        }catch (Exception e)
        {

        }
        return tdate;
    }

    private void democontactalert() {
        //tm


        if (dialogcontact != null) {
            if (dialogcontact.isShowing()) {
                dialogcontact.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.contact_company, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);

        final TextView txtalert = dialogView.findViewById(R.id.txtalert);

        dialogcontact = new Dialog(contexts, R.style.DialogTheme);
        dialogcontact.setCancelable(false);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", contexts));

        dialogcontact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcontact.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogcontact.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogcontact.getWindow().setAttributes(lp);
        dialogcontact.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcontact.dismiss();
                // gettodayvalues(str_vin);
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
        String val2=" \n  * When uploading images. \n \n " +
                " * Generate report time save signature  ";
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.file_privacy, null);
        final TextView btnsubmitz = dialogView.findViewById(R.id.tsubmit);
        final TextView tcancel = dialogView.findViewById(R.id.tcancel);
        final TextView lsub = dialogView.findViewById(R.id.lsub);
lsub.setText(""+val2);
        dialogprivacy = new Dialog(contexts, R.style.DialogTheme);
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
