package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Isoft_adapter.ReportDetailsBean;
import com.isoft.trucksoft_elog.Isoft_adapter.ReportHomeAdapter;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Dailyreport_main;
import com.isoft.trucksoft_elog.Model_class.Manual_Report;
import com.isoft.trucksoft_elog.Month_digital_sig;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Dialog_notification;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isoft on 14/10/16.
 */
public class WeeklyReport extends Fragment {

    Context contxt;

    private String stimes;
    private Preference pref;
    ProgressDialog dialog;
    ReportDetailsBean mBean;
    private ListView list;
    public static ArrayList<ReportDetailsBean> mBeans;
//    private TextView imgback;
Dialog dialogprivacy;
    private String driverid;
    private String currentdate;
    private CommonUtil commonUtil;
//    private ImageView imglogout;
    private  String lat,lon;
//    private  TextView txtdate;
    Font_manager_elog font_manager;
    private TextView txtdayrep;
    private TextView txtsevenrep;
    private TextView txtthirtyrep;
    private TextView txtcustrep;
    private TextView txtr1;
    private TextView txtr2;
    private TextView txtr3;
    private TextView txtr4;
    ArrayList<String> arraylistschedule = new ArrayList<>();
    Bundle bundle;
    Dialog_notification dialog_notification;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String breakid;
    private int drivetotal=00;
    private int ondutytotal=00;
    private int ondrivetotal=00;
    private TextView tdriveseven;
    private TextView tondutyseven;
    private TextView toddrseven;
    Eld_api api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.weeklyreport, container, false);
        pref = Preference.getInstance(getActivity());
        list = (ListView) rootView.findViewById(R.id.chatactivity_listview);
        contxt = getActivity();
        font_manager=new Font_manager_elog();
        stimes=pref.getString(Constant.SERVER_TIME);

        bundle = getArguments();
        commonUtil = new CommonUtil(contxt);
        dialog_notification=new Dialog_notification();
        //Log.e("calling","weekly report............");
        arraylistschedule = new ArrayList<>();
        tdriveseven=(TextView)rootView.findViewById(R.id.tdriveseven);
        tondutyseven=(TextView)rootView.findViewById(R.id.tondutyseven);
        toddrseven=(TextView)rootView.findViewById(R.id.toddrseven);



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
        txtcustrep.setTextColor(Color.parseColor("#2d5d88"));
        txtdayrep.setTextColor(Color.parseColor("#2d5d88"));
        txtthirtyrep.setTextColor(Color.parseColor("#2d5d88"));
        txtsevenrep.setTextColor(Color.parseColor("#ffba00"));

        txtr4.setTextColor(Color.parseColor("#2d5d88"));
        txtr1.setTextColor(Color.parseColor("#2d5d88"));
        txtr3.setTextColor(Color.parseColor("#2d5d88"));
        txtr2.setTextColor(Color.parseColor("#ffba00"));
        driverid=pref.getString(Constant.DRIVER_ID);
        lat=pref.getString(Constant.LATITUDE);
        lon=pref.getString(Constant.LONGITUDE);
        SimpleDateFormat formatz= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String sdate=pref.getString(Constant.MONTHLY_DATE);
       // txtdate=(TextView)rootView.findViewById(R.id.tt3);
        Calendar c = Calendar.getInstance();

        int month =
                c.get(Calendar.MONTH);
        //txtdate.setText("  "+getMonth(month));
        currentdate= formatz.format(new Date());
        getfulldata();
     //   getweeklyreport();

//*****************************************************************************

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("call","........."+"rep..............");
                // checking for type intent filter
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    // new push notification is received
                    //handlePushNotification(intent);
                    String msg = intent.getStringExtra("message");
                    Log.e("msg","........."+msg);
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


        txtdayrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Dailyreport_main();
                setfragment(bundle, fragment);
            }
        });
        txtcustrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Manual_Report();
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
        txtr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MonthlyReport();
                setfragment(bundle, fragment);
            }
        });
        txtr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Manual_Report();
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


        MovableFloatingActionButton fab = (MovableFloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = ActivityCompat.checkSelfPermission(contxt, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (check == PackageManager.PERMISSION_GRANTED) {
                Intent intent=new Intent(contxt, Month_digital_sig.class);
                intent.putStringArrayListExtra("arraylistscheduled",arraylistschedule);

                startActivity(intent);
                } else {
                  callprivacy();
                }
            }
        });
        return rootView;
    }

//    private void getweeklyreport()
//    {
//        dialog = new ProgressDialog(contxt,
//                AlertDialog.THEME_HOLO_LIGHT);
//
//        if (OnlineCheck.isOnline(contxt)) {
//            dialog.setMessage("Please wait...");
//            dialog.setCancelable(false);
//            dialog.show();
//            api = DispatchServiceGenerator.createService(ReportApi.class);
//
//
//            Call<JSONArray> call = null;
//
//
//            String did= pref.getString(Constant.DRIVER_ID);
//            String vin=pref.getString(Constant.VIN_NUMBER);
//            Log.e("pcvvvvv", "http://eld.e-logbook.info/elog_app/weekly_report.php?did="+did+"&vin="+vin);
//            call = api.getweeklyreport(did,vin);
//
//
//            call.enqueue(new Callback<JSONArray>() {
//                @Override
//                public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
//                  //   Log.e("Responsestring", response.body().toString());
//                    //Toast.makeText()
//                    if (response.isSuccessful()) {
//                        cancelprogresssdialog();
//                        if (response.body() != null) {
//                            String jsonresponse = response.body().toString();
//
//                            try {
//                                JSONObject resp = new JSONObject(jsonresponse);
//                                if (response != null) {
//
//
//                                }
//                            } catch (Exception e) {
//
//                            }
//
//
//                        } else {
//                             Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        cancelprogresssdialog();
//                           Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JSONArray> call, Throwable t) {
//                    cancelprogresssdialog();
//                     Log.e("imageresponseerrr",""+t.toString());
//                }
//            });
//
//        }
//
//    }

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
    private void getfulldata() {
        dialog = new ProgressDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT);
        mBeans = new ArrayList<ReportDetailsBean>();
        if (OnlineCheck.isOnline(getActivity())) {
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            api = ApiServiceGenerator.createService(Eld_api.class);
            String vin = pref.getString(Constant.VIN_NUMBER);
            // Log.e("dccc","@"+dc);2019-01-14
            SimpleDateFormat formatdc= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dsf =formatdc.format(new Date());
            Call<List<reportz_model>> call = api.getweeklyreport(pref.getString(Constant.DRIVER_ID),  vin,""+dsf);
            call.enqueue(new Callback<List<reportz_model>>() {
                @Override
                public void onResponse(Call<List<reportz_model>> call, Response<List<reportz_model>> response) {
                    cancelprogresssdialog();
                    if (response.isSuccessful()) {
                        List<reportz_model> result = response.body();
                        if (result.size() > 0) {
                            setfullValue(result);
                        } else {

                        }
                        //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                    }

                }

                @Override
                public void onFailure(Call<List<reportz_model>> call, Throwable t) {
                    // Log.e("ddd"," Load More Response Error "+t.getMessage());
                    cancelprogresssdialog();
                }
            });
        }
    }
//    private void schedulefull_getdata() {
//        dialog = new ProgressDialog(getActivity(),
//                AlertDialog.THEME_HOLO_LIGHT);
//        mBeans= new ArrayList<ReportDetailsBean>();
//        if (OnlineCheck.isOnline(getActivity())) {
//            dialog.setMessage("Please wait...");
//            dialog.setCancelable(false);
//            dialog.show();
//            WebServices.elog_getmonthlyreport(getActivity(),
//                    new JsonHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              String responseString, Throwable throwable) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers,
//                                    responseString, throwable);
//                             Log.e("sdsk", "k"+responseString);
//                            // Log.e("sdsk", "k"+throwable);
//                            dialog.dismiss();
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
//                             Log.e("sds1", "k"+errorResponse);
//                            dialog.dismiss();
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
//                            Log.e("sds2", "k"+errorResponse);
//                            dialog.dismiss();
//
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              JSONArray response) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, response);
//                            dialog.dismiss();
//                             Log.e("response logcvb1", "k"+response.toString());
//                            if (response != null) {
//                                //  getActivity().stopService(new Intent(contxt, BroadcastService.class));
//
//
//                                setfullValue(response);
//                            }
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              JSONObject response) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, response);
//                            dialog.dismiss();
//                             Log.e("response log", "k"+response.toString());
//
//                            try {
//
//
//
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
//                            dialog.dismiss();
//                             Log.e("response logz", "k"+responseString.toString());
//
//                            // CommonMethod.showMsg(getActivity(), ""+
//                            // responseString);
//                        }
//
//                    });
//        }
//
//    }






    public void setfullValue( List<reportz_model> response) {
        try {
            mBeans = new ArrayList<ReportDetailsBean>();
            arraylistschedule=new ArrayList<>();
            if (response.size() > 0) {

                for (int i = 0; i < response.size(); i++) {
                    mBean = new ReportDetailsBean();
                    reportz_model mJsonObject = new reportz_model();
                    mJsonObject=response.get(i);
                    String skonduty = mJsonObject.sonduty;
                    String skoffduty = mJsonObject.soffdutty;
                    String skdrive = mJsonObject.sdriving;
                    String sksleep = mJsonObject.ssleep;
                    String datt = mJsonObject.date;
                    String statdate = mJsonObject.certifydate;
                    String trip_num = mJsonObject.trip_num;

                    if(mJsonObject.oddrvtime!=null)
                    {
                        String oddr= mJsonObject.oddrvtime;
                        if(oddr !=null && oddr.length()>0 && !oddr.contentEquals("null"))
                        {

                            if(oddr !=null && oddr.length()>0 && !oddr.contentEquals("null"))
                            {
                                mBean.setOddrvtime(""+oddr);
                                ondrivetotal+=splittime(oddr);
                                  }else{
                                mBean.setOddrvtime("00:00:00");
                            }



                        }else{
                            mBean.setOddrvtime("00:00:00");
                        }
                    }
                    if(mJsonObject.certify !=null)
                    {
                        String stat = mJsonObject.certify;
                        if(stat.contentEquals("1"))
                        {
                            mBean.setcertify(stat);

                        }else{
                            mBean.setcertify(stat);
                        }

                    }else{
                        mBean.setcertify("0");
                    }
if(skonduty !=null && skonduty.length()>0 && !skonduty.contentEquals("null"))
{
    mBean.setOndutye(skonduty);
}else
{
    mBean.setOndutye("00:00:00");
}

                    if(skonduty ==null || skonduty.length()==0 || skonduty.contentEquals("null"))
                    {
                        skonduty="00:00:00";
                    }
                    ondutytotal+=splittime(skonduty);
                    if(skoffduty ==null || skoffduty.length()==0 || skoffduty.contentEquals("null"))
                    {
                        skoffduty="00:00:00";
                    }
                    if(skdrive ==null || skdrive.length()==0 || skdrive.contentEquals("null"))
                    {
                        skdrive="00:00:00";
                    }
                    drivetotal+=splittime(skdrive);


                    if(sksleep ==null || sksleep.length()==0 || sksleep.contentEquals("null"))
                    {
                        sksleep="00:00:00";
                    }

                    arraylistschedule.add(datt+">>"+skonduty+">>"+skoffduty+">>"+skdrive+">>"+sksleep);
                    if(skoffduty !=null && skoffduty.length()>0 && !skoffduty.contentEquals("null")) {
                        mBean.setOffdutye(skoffduty);
                    }else
                    {
                        mBean.setOffdutye("00:00:00");
                    }

                    if(skdrive !=null && skdrive.length()>0 && !skdrive.contentEquals("null")) {
                        mBean.setdrivee(skdrive);
                    }else
                    {
                        mBean.setdrivee("00:00:00");
                    }

                    // Log.e("sksleep", "" + sksleep);
                    if(sksleep !=null && sksleep.length()>0 && !sksleep.contentEquals("null")) {
                        mBean.setsleep(sksleep);
                    }else
                    {
                        mBean.setsleep("00:00:00");
                    }
                    if (statdate != null && statdate.length() > 0 && !statdate.contentEquals("null")) {
                        mBean.setCertifydate(statdate);
                    } else {
                        mBean.setCertifydate("");
                    }


                    if (trip_num != null && trip_num.length() > 0 && !trip_num.contentEquals("null") ) {
                        int vala=Integer.parseInt(trip_num);
                        if(vala>0) {
                            mBean.setTrip_num("1");
                        }else{
                            mBean.setTrip_num("0");
                        }
                    } else {
                        mBean.setTrip_num("0");
                    }


                    mBean.setdate(datt);
                    mBeans.add(mBean);

                    list.setAdapter(new ReportHomeAdapter(getActivity(), mBeans,"weekly"));

                }
                try{
                    String sdrive=getDurationString(drivetotal);
                    tdriveseven.setText("Total Driving (7 days) : "+sdrive+" Hours");

                }catch (Exception e)
                {

                }


                try{
                    String sonduty=getDurationString(ondutytotal);

                    tondutyseven.setText("Total Onduty (7days)  : "+sonduty+" Hours");
                }catch (Exception e)
                {

                }

                try{
                    String sodrcr=getDurationString(ondrivetotal);
                    toddrseven.setText("Total OD+DR (7days)  : "+sodrcr+" Hours");
                }catch (Exception e)
                {

                }

            }

            //scheduleSendLocation();
        } catch (Exception e) {
            // TODO: handle exception
        }
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
    public String pad(Long num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }

    //get month names
    public String getMonth(int month) {
        String monthtext = null;

        switch (month) {
            case 0:
                monthtext = "January";
                break;
            case 1:
                monthtext = "February";
                break;
            case 2:
                monthtext = "March";
                break;
            case 3:
                monthtext = "April";
                break;
            case 4:
                monthtext = "May";
                break;
            case 5:
                monthtext = "June";
                break;
            case 6:
                monthtext = "July";
                break;
            case 7:
                monthtext = "August";
                break;
            case 8:
                monthtext = "September";
                break;
            case 9:
                monthtext = "October";
                break;
            case 10:
                monthtext = "November";
                break;
            case 11:
                monthtext = "December";
                break;
        }
        return monthtext;
    }
    public void setfragment(Bundle bundle, Fragment fragment) {
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment).commit();
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
        if(dialog !=null && dialog.isShowing())
        {
            dialog.dismiss();
            dialog=null;
        }
        super.onDestroy();
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
    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

      //  return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
        return twoDigitString(hours) + " : " + twoDigitString(minutes) ;
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