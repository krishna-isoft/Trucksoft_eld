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
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Digit_Signature;
import com.isoft.trucksoft_elog.Isoft_activity.Home_activity_bluetooth;
import com.isoft.trucksoft_elog.Isoft_activity.Report_Home;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Multiused.ActivityDialog_notification;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isoft on 29/1/18.
 */

public class Subdetailview extends AppCompatActivity implements Currentreportadap.refreshpage{
    RecyclerView recyclerView;
    List<Daily_reportmodel> movies;
    Currentreportadap adapter;
    Eld_api api;

    String TAG = "MainActivity - ";
    Context context;
    Preference pref;
    ProgressDialog dialog,progressdlog;

    String dc;
    private LinearLayout list;
    String sdate;
    private TextView txtdate;
    private TextView txtemail;
    int progress=0;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Dialog dialogprivacy;
    Dialog dialogcontact;
    WebView myWebView;
    private String driverid;
    ArrayList<String> arraylistschedule = new ArrayList<>();
    ArrayList<String> arraylisttotal = new ArrayList<>();
    private long strdrive;    long stron;

    ActivityDialog_notification dialog_notification;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String breakid;
    private CommonUtil commonUtil;
    TextView txtdatec;
    TextView txtdriver;
    FrameLayout frameview;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private TextView txtclarify;

    private TextView txtexception;
    private String strsldate="";

    private String strstate="";
    private String straddress="";
    private String lat="";
    private String lon="";
    private TextView ttrip;

    private TextView tfmcsa;

    Dialog dialogfmcsa;
    Font_manager_elog font_manager;
    private static  TextView tfrom;
    private static TextView tto;
    private static int value=0;
    private int tripactive=0;
    Dialog dialogrkship;

    String type="weekly";
    private Intent inkkk;
    List<Summarymodel> resulttotal = new ArrayList<>();
    private ImageView imgback;
    private TextView thome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_detailview);
        recyclerView = (RecyclerView) findViewById(R.id.chatactivity_listview2);
        list = (LinearLayout) findViewById(R.id.chatactivity_listview);
        movies = new ArrayList<>();
        arraylisttotal = new ArrayList<>();
        context=this;
        inkkk=getIntent();
        font_manager=new Font_manager_elog();
        imgback=findViewById(R.id.driver_list_iv_back);
        commonUtil = new CommonUtil(context);
        dialog_notification=new ActivityDialog_notification();
        dialog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        frameview=findViewById(R.id.frame_memecontent);
        thome=findViewById(R.id.t_home);
        arraylistschedule = new ArrayList<>();
        pref=   Preference.getInstance(context);
        pref.putString(Constant.LAST_INDEX,"0");
        sdate=pref.getString(Constant.MONTHLY_DATE);
        String sname=pref.getString(Constant.DRIVER_NAME);
        txtdatec=findViewById(R.id.txtdate);
        txtdriver=findViewById(R.id.txtdr);
        tfmcsa=findViewById(R.id.tfmcsa);
        txtclarify=findViewById(R.id.txtcertfy);
        txtdriver.setText("Driver : "+sname);
        dc=pref.getString(Constant.MONTHLY_DATE);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        ttrip=findViewById(R.id.ttrip);
        //Log.e("sdate","............."+sdate);
        txtdate=(TextView)findViewById(R.id.tt3);
        myWebView = (WebView) findViewById(R.id.webview);
        txtdate.setText("  "+sdate);
        setdat(sdate);
        txtemail=(TextView)findViewById(R.id.txtemail);
        thome.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        txtexception=findViewById(R.id.txtexce);
if(inkkk !=null)
{
    if(inkkk.hasExtra("type"))
    {
        type=inkkk.getStringExtra("type");
    }
}





        driverid=pref.getString(Constant.DRIVER_ID);
        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        // Log.e("grlink",""+"http://e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
      //  myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+sdate+"&driverid="+driverid+"&did="+driverid);
        myWebView.loadUrl("https://eld.e-logbook.info/drivergraph.php?date="+sdate+"&driverid="+driverid+"&did="+driverid);

      //  Log.e("suburl","http://eld.e-logbook.info/drivergraph.php?date="+sdate+"&driverid="+driverid+"&did="+driverid);
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent1 = new Intent(Subdetailview.this, Report_Home.class);
                mIntent1.putExtra("class_name",""+type);
                startActivity(mIntent1);
                finish();
            }
        });
        thome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type !=null && type.length()>0) {
//                    if (pref.getString(Constant.NETWORK_TYPE).contentEquals("" + Constant.CELLULAR)) {
//                        Intent ink = new Intent(context, Home_activity.class);
//                        startActivity(ink);
//                        finish();
//                    } else {
                        finish();
//                    }
                }else{
//                  if (pref.getString(Constant.NETWORK_TYPE).contentEquals("" + Constant.CELLULAR)) {
//                        Intent ink = new Intent(context, Home_activity.class);
//                        startActivity(ink);
//                        finish();
//                    } else {
                      Intent ink = new Intent(context, Home_activity_bluetooth.class);
                      startActivity(ink);
                      finish();
//                    }
                }
            }
        });

       // gettotalhour(sdate);
        gettotalvalue(sdate);
        txtclarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drivertype=pref.getString(Constant.ELOG_DRIVER_TYPE);
                if(drivertype.contentEquals("1"))
                {
                    democontactalert();
                }else {
                if (txtclarify.getText().toString().contentEquals("Certify Now")
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
        adapter = new Currentreportadap(context, movies,strsldate, Subdetailview.this);


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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        api = ApiServiceGenerator.createService(Eld_api.class);

        load(0);

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
                                dialog_notification.setpopupnotification(Subdetailview.this,context,type,status,breakid);
                                // dialogdia.show();
                            }
                        }
                    }
                }
            }
        };




        //*************************************************************************

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
                        String ttime="--";
                        String dtime="--";
                        String vid="__";
                        String vinn="__";
                        if(val.address==null || val.address.length()==0 || val.address.contentEquals("null"))
                        {
                            address="--";
                        }else
                        {
                            address=val.address;
                        }
                        String rmark="";
                        if(val.remark==null || val.remark.length()==0 || val.remark.contentEquals("null"))
                        {
                            rmark="__";
                        }else
                        {
                            rmark=val.remark;
                        }


                    if(val.ttime==null || val.ttime.length()==0 || val.ttime.contentEquals("null"))
                    {
                        ttime="__:__";
                    }else
                    {
                        ttime=val.ttime;
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
                int check = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (check == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(context, Digit_Signature.class);
                    intent.putStringArrayListExtra("arraylistschedule", arraylistschedule);
                    intent.putStringArrayListExtra("arraylisttotal", arraylisttotal);
                    //Log.e("resulttotalsize", "" + resulttotal.size());
                    intent.putExtra("totalval", (Serializable) resulttotal);
                    startActivity(intent);
                }else {
                   callprivacy();
                }
            }
        });
    }
    private void load(int index){



        String page="0";
      //  Log.e("index",""+index);
       // Log.e("vin",""+pref.getString(Constant.VIN_NUMBER));
       // Log.e("s_date",""+dc);
       // Log.e("page",""+page);
       // Log.e("did",""+pref.getString(Constant.DRIVER_ID));
       // Log.e("calling","load");
       // Log.e("loadmore",""+page);


        //Log.e("curl","current_report_scrolling.php?index="+index+"&vin="+pref.getString(Constant.VIN_NUMBER)
       // +"&s_date="+dc+"&page="+page+"&did="+pref.getString(Constant.DRIVER_ID));
        Call<List<Daily_reportmodel>> call = api.getdaylog(index,pref.getString(Constant.VIN_NUMBER),dc,page,pref.getString(Constant.DRIVER_ID),"");

        call.enqueue(new Callback<List<Daily_reportmodel>>() {
            @Override
            public void onResponse(Call<List<Daily_reportmodel>> call, Response<List<Daily_reportmodel>> response) {
               // Log.e(" Responsev"," "+response.toString());
                // Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                   cancelprogresssdialog();
                   // Log.e(" Responsecqe","z "+response.body());
                    movies.addAll(response.body());

                    //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                 cancelprogresssdialog();
                    // load(0);
                    //Log.e(TAG," Response Error "+String.valueOf(response.code()));
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
        //Log.e("curlmore","current_report_scrolling.php?index="+index+"&vin="+pref.getString(Constant.VIN_NUMBER)
            //    +"&s_date="+dc+"&page="+page+"&did="+pref.getString(Constant.DRIVER_ID));
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
                        Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
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

//    private void gettotalhour(String sdate)
//    {
//        {
//           // dialog = new ProgressDialog(context,
//                  //  AlertDialog.THEME_HOLO_LIGHT);
//
//            if (OnlineCheck.isOnline(context)) {
//               // dialog.setMessage("Please wait...");
//              //  dialog.setCancelable(false);
//               // dialog.show();
//                WebServices.gettotalhr(context,sdate,""+straddress,""+strstate,"Weekly/Monthly view Report",
//                        new JsonHttpResponseHandler() {
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers,
//                                                  String responseString, Throwable throwable) {
//                                // TODO Auto-generated method stub
//                                super.onFailure(statusCode, headers,
//                                        responseString, throwable);
//                                // Log.e("sdsk", "k"+responseString);
//                                //Log.e("sdsk", "k"+throwable);
//                               // dialog.dismiss();
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
//                                //Log.e("sds1", "k"+errorResponse);
//                              //  dialog.dismiss();
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
//                                //Log.e("sds2", "k"+errorResponse);
//                               // dialog.dismiss();
//
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers,
//                                                  JSONArray response) {
//                                // TODO Auto-generated method stub
//                                super.onSuccess(statusCode, headers, response);
//                               // dialog.dismiss();
//                                //Log.e("responsech", "k"+response.toString());
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
//                               // dialog.dismiss();
//                               // Log.e("response log", "k"+response.toString());
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
//                                //dialog.dismiss();
//                               // Log.e("response logz", "k"+responseString.toString());
//
//                                // CommonMethod.showMsg(getActivity(), ""+
//                                // responseString);
//                            }
//
//                        });
//            }
//
//        }
//    }

    private void setValue(JSONArray response) {
        strdrive=00;
        stron=00;
        list.removeAllViews();
        try {
            if (response.length() > 0) {

                for (int i = 0; i < response.length(); i++) {
                    JSONObject mJsonObject = response.getJSONObject(i);
                    String driving = mJsonObject.getString("sdriving");
                    String offduty = mJsonObject.getString("soffdutty");
                    String sleep = mJsonObject.getString("ssleep");
                    String onduty = mJsonObject.getString("sonduty");
                    String imgurl = mJsonObject.getString("imgurl");
                    pref.putString(Constant.GRAPH_IMURL,""+imgurl);
                    if(mJsonObject.has("trip_num")) {
                        String trip_num = mJsonObject.getString("trip_num");
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
                    }
                    if(offduty !=null && offduty.length()>0 && !offduty.contentEquals("null"))
                    {
                        if(offduty.contentEquals("23:59:00") || offduty.contentEquals("23:58:00"))
                        {
                            tripactive = 1;
                        }
                    }
                    if(mJsonObject.has("certify"))
                    {
                        String stat = mJsonObject.getString("certify");
                        if(stat.contentEquals("1"))
                        {
                            String statdate = mJsonObject.getString("certifydate");
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
                            txtclarify.setBackgroundResource(R.drawable.button_activate_ident);
                            txtclarify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
                        }else{
                            txtclarify.setBackgroundResource(R.drawable.button_ident);
                            txtclarify.setText(" CERTIFY NOW ");
                        }

                    }




                    if(driving !=null && driving.length()>0 && !driving.contentEquals("null"))
                    {

                    }else
                    {
                        driving="00:00:00";
                    }
                    if(offduty !=null && offduty.length()>0 && !offduty.contentEquals("null"))
                    {

                    }else
                    {
                        offduty="00:00:00";
                    }
                    if(sleep !=null && sleep.length()>0 && !sleep.contentEquals("null"))
                    {

                    }else
                    {
                        sleep="00:00:00";
                    }
                    if(onduty !=null && onduty.length()>0 && !onduty.contentEquals("null"))
                    {

                    }else
                    {
                        onduty="00:00:00";
                    }

                    arraylisttotal.add("ON" + ">>" +onduty);
                    arraylisttotal.add("OFF" + ">>" +offduty);
                    arraylisttotal.add("D" + ">>" +driving);
                    arraylisttotal.add("SB" + ">>" +sleep);
                    strdrive+=splittime(driving);
                    stron+=splittime(onduty);
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


                    //   TextView txt_duration1 = (TextView) ll.findViewById(R.id.tduration1);

//                    TextView thomestate=ll
//                            .findViewById(R.id.tomestate);

                    TextView txt_status2 = (TextView) ll
                            .findViewById(R.id.tstatus2);


//                    thomestate.setText("Home State : "+pref.getString(Constant.STATE_FIELD));
//                    txtstate.setText("Current state : "+pref.getString(Constant.CURRENT_STATE));
                   // txtstate1.setText(""+pref.getString(Constant.STATE_FIELD));
                    //txtstate2.setText(""+pref.getString(Constant.STATE_FIELD));
                   // txtstate3.setText(""+pref.getString(Constant.STATE_FIELD));
                    TextView txt_start2 = (TextView) ll
                            .findViewById(R.id.tstart2);
                    TextView txt_timeallowdr = (TextView) ll
                            .findViewById(R.id.tallowedtime2);

                    TextView txt_timeallowon = (TextView) ll
                            .findViewById(R.id.tallowedtime3);
                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
                        txt_timeallowdr.setText("11:00");
                    }else{
                        txt_timeallowdr.setText(""+pref.getString(Constant.HOME_DRIVE_HOURS));
                    }
                    txt_timeallowdr.setTextColor(Color.parseColor("#17BD17"));
                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
                        txt_timeallowon.setText("14:00");
                    }else{
                        txt_timeallowon.setText(""+pref.getString(Constant.HOME_ONDUTY_HOURS));
                    }
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


                    // txt_status1.setText(ststatus1);
                    txt_start1.setText(""+sleep);
                    // txt_status2.setText(ststatus2);
                    txt_start2.setText(""+driving);
                    // txt_status3.setText(ststatus3);
                    txt_start3.setText(""+onduty);

                    long sptime = splittime(""+pref.getString(Constant.HOME_DRIVE_HOURS));
                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
                        sptime = splittime("11:00:00");
                    }else{
                        sptime = splittime(""+pref.getString(Constant.HOME_DRIVE_HOURS));
                    }

                    //Log.e("sptime0",""+sptime);
                    //String sp=""+strdrive;
                    //Log.e("sptime",""+sp);
                    long newtime = sptime - strdrive;
                    //Log.e("newtime",""+newtime);
                    String ak = printsum(newtime);
                    //Log.e("ak",""+ak);

                    long optime = splittime("16:00:00");
                    if(pref.getString(Constant.FEDERAL_DRIVE_ACTIVE) !=null && pref.getString(Constant.FEDERAL_DRIVE_ACTIVE).contentEquals("active")) {
                        optime = splittime("14:00:00");
                    }else {
                        optime = splittime(""+pref.getString(Constant.HOME_ONDUTY_HOURS));
                    }

                    long opnewtime = optime - stron- strdrive;
                    String opk = printsum(opnewtime);
                    if(ak.contains("-")) {
                        if(ak.contains(":")){
                            StringTokenizer sgh=new StringTokenizer(ak,":");
                            String f1=sgh.nextToken();
                            String f2=sgh.nextToken();
                            if(f2.contains("-"))
                            {
                                f2=removeFirstChar(f2);
                                // Log.e("ak11",""+opk);
                            }
                            if(f1.contains("-"))
                            {
                                //Log.e("ak111",""+opk);
                                txt_duration2.setText(f1+":"+f2);
                            }else{
                                //   Log.e("ak1111",""+f1+":"+f2);
                                txt_duration2.setText("-"+f1+":"+f2);
                            }
                        }

                        // txt_duration2.setText(""+ak);
                    }else
                    {
                        txt_duration2.setText(ak);
                    }

                    if(opk.contains("-")) {
                        if(opk.contains(":"))
                        {
                            StringTokenizer sgh=new StringTokenizer(opk,":");
                            String f1=sgh.nextToken();
                            String f2=sgh.nextToken();
                            if(f2.contains("-"))
                            {
                                f2=removeFirstChar(f2);
                                // Log.e("ak11",""+opk);
                            }
                            if(f1.contains("-"))
                            {
                                //Log.e("ak111",""+opk);
                                txt_duration3.setText(f1+":"+f2);
                            }else{
                                //   Log.e("ak1111",""+f1+":"+f2);
                                txt_duration3.setText("-"+f1+":"+f2);
                            }
                        }

                        // txt_duration3.setText(""+opk);
                    }else {
                        txt_duration3.setText(opk);
                    }

                    list.addView(ll);

                    String except=mJsonObject.getString("exceptions");
                    txtexception.setPaintFlags(txtexception.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    if(except !=null && except.length()>0) {
                        //Log.e("rk", "" + except.toString());
                        JSONObject job = new JSONObject(except);
                        String resul = job.getString("result");
                        String reason = job.getString("reason");
                        String reasss = job.getString("reason");
                        JSONArray jsonArray = new JSONArray(resul);
                        txtexception.setText("Exceptions : "+jsonArray.length());
                        txtexception.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {

                                    if (jsonArray.length() > 0) {
                                        final Dialog dialogex = new Dialog(context);
                                        dialogex.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialogex.setCancelable(false);
                                        //dialogex.setTitle("Exception Detail");
                                        dialogex.setContentView(R.layout.eceptionlistrep);

                                        Button dialogcancel =dialogex.findViewById(R.id.btn_cancel);
                                        TextView reason=dialogex.findViewById(R.id.edtreason);
                                        LinearLayout linexcep = dialogex.findViewById(R.id.lin_excep);
                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                        lp.copyFrom(dialogex.getWindow().getAttributes());
                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                        lp.gravity = Gravity.CENTER;

                                        dialogex.getWindow().setAttributes(lp);
                                        //   reason.setVisibility(View.GONE);
                                        if(reasss !=null && reasss.length()>0 && !reasss.contentEquals("null")) {
                                            reason.setText("          Reason : "+reasss);
                                        }
                                        for (int k = 0; k < jsonArray.length(); k++) {
                                            JSONObject jk = new JSONObject();
                                            jk = jsonArray.getJSONObject(k);
                                            String sk1 = "" + jk.getString("type");
                                            String sk2 = "" + jk.getString("article");
                                            String sk3 = "" + jk.getString("detail");

                                            View layout2 = LayoutInflater.from(context).inflate(R.layout.ex_list, linexcep, false);

                                            TextView txttype =  layout2.findViewById(R.id.txt_type);
                                            TextView txtdetail =  layout2.findViewById(R.id.txt_detail);
                                            TextView txtid =  layout2.findViewById(R.id.txt_id);
                                            CheckBox chselect=layout2.findViewById(R.id.ch_select);
                                            chselect.setVisibility(View.INVISIBLE);
                                            if(sk2 !=null&& sk2.length()>0)
                                            {
                                                txttype.setText(sk1+"("+sk2+")");
                                            }else
                                            {
                                                txttype.setText(sk1);
                                            }
                                            txtdetail.setText(""+sk3);
                                            linexcep.addView(layout2);
                                        }
                                        dialogcancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogex.dismiss();


                                            }
                                        });


                                        dialogex.show();
                                    }
                                }catch (Exception e)
                                {

                                }
                            }
                        });

                    }
                }


            }
        } catch (Exception e) {
            // TODO: handle exception
        }
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

    @Override
    public void refreshrep(String id, String msg) {
movies=new ArrayList<>();
recyclerView.removeAllViews();
list.removeAllViews();
        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        // Log.e("grlink",""+"http://e-logbook.info/drivergraph.php?date="+dc+"&driverid="+driverid+"&did="+driverid);
        //  myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+sdate+"&driverid="+driverid+"&did="+driverid);
        myWebView.loadUrl("http://eld.e-logbook.info/drivergraph.php?date="+sdate+"&driverid="+driverid+"&did="+driverid);

      //  gettotalhour(sdate);
        gettotalvalue(sdate);
        txtclarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtclarify.getText().toString().contentEquals("Certify Now") ||
                        txtclarify.getText().toString().contentEquals("Certify now")
                        || txtclarify.getText().toString().contentEquals(" CERTIFY NOW ")) {
                    savecertify("" + dc, "1");
                }
            }
        });
        adapter = new Currentreportadap(context, movies,strsldate, Subdetailview.this);


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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
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
          // cancelprogresssdialog();
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            frameview.setAnimation(outAnimation);


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));
        Trucksoft_elog_Notify_Utils.clearNotifications(context);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent1 = new Intent(Subdetailview.this, Report_Home.class);
        mIntent1.putExtra("class_name",""+type);
        startActivity(mIntent1);
        finish();
    }
    private void setdat(String myDatez)
    {
        strsldate=myDatez;
        try {
            StringTokenizer skdte;
            if (myDatez.contains("-")) {
                skdte = new StringTokenizer(myDatez, "-");
                String styr = skdte.nextToken();
                String stmnth = skdte.nextToken();
                String stdte = skdte.nextToken();
                txtdatec.setText(stdte+" "+getMonth(Integer.parseInt(stmnth)) + " " + styr);
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

    @Override
    protected void onDestroy() {

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
            //Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            //Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialog = null;
        }

        try {
            if ((progressdlog != null) && progressdlog.isShowing()) {
                progressdlog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            //Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            //Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            progressdlog = null;
        }
    }

    private  void savecertify(String date,String status) {
        progressdlog = new ProgressDialog(context,
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
                    txtclarify.setBackgroundResource(R.drawable.button_activate_ident);
                    txtclarify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
                    cancelprogresssdialog();
                    Intent mIntent1 = new Intent(Subdetailview.this, Report_Home.class);
                   // Log.e("type","@"+type);
                    mIntent1.putExtra("class_name",""+type);
                    startActivity(mIntent1);
                    finish();
                } else {
                    cancelprogresssdialog();
                     //Log.e("result","fail");
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

        Intent intent = getIntent();
        finish();
        startActivity(intent);
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

        dialogfmcsa = new Dialog(context, R.style.DialogTheme);
        dialogfmcsa.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
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
                    Toast.makeText(context,"Please enter Enforcement officer code",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager manager = getSupportFragmentManager();
                value=1;
                DialogFragment newFragment2 = new SelectDateFragment();
                newFragment2.show(manager, "DatePicker");
            }
        });
        tto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.fragment.app.FragmentManager manager = getSupportFragmentManager();
                value=2;
                DialogFragment newFragment2 = new SelectDateFragment();
                newFragment2.show(manager, "DatePicker");
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

            if (OnlineCheck.isOnline(context)) {
                progressdlog = new ProgressDialog(context,
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
                               // String jsonresponse = response.body().toString();
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
    private void showmissingtripnumalert()
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.alert_shipdoc, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final EditText edt_number = dialogView.findViewById(R.id.edt_tripnum);
        dialogrkship = new Dialog(context, R.style.DialogTheme);



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
            {   progressdlog = new ProgressDialog(context,
                    AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                String did=pref.getString(Constant.DRIVER_ID);
                String vinnumber=""+pref.getString(Constant.VIN_NUMBER);

                String straction="save";
                String msg=edt_number.getText().toString().trim();
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+sdate);
                Call<JsonObject> call = api.updatetripno(vinnumber,"0",did,""+msg,""+msg,straction,sdate);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                       // Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        cancelprogresssdialog();
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
                                                dialogrkship.dismiss();
                                                String msg = resp.getString("message");


                                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();

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
                         cancelprogresssdialog();
                    }
                });


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
        Call<List<Summarymodel>> call = api.getsummary(pref.getString(Constant.STATE_FIELD),pref.getString(Constant.CURRENT_STATE),""+pref.getString(Constant.DRIVER_ID),dated,pref.getString(Constant.VIN_NUMBER),pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Driver Summary");



        call.enqueue(new Callback<List<Summarymodel>>() {
            @Override
            public void onResponse(Call<List<Summarymodel>> call, Response<List<Summarymodel>> response) {
                if(response.isSuccessful()){
                    //Log.e("response","success");
                    resulttotal = response.body();
                   // Log.e("result","success"+response.body().toString());
                   // Log.e("result.size",""+resulttotal.size());
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
                                todaycount=grt.today_count;

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


//                                if(grt.sevenDaysCertify!=null)
//                                {
//                                    sevendaysstatus =grt.sevenDaysCertify;
//                                    Log.e("sevendaysstatus","@"+sevendaysstatus);
//                                }

                                for(int k=0;k<tl.size();k++) {
                                    todaymodel today = tl.get(k);
                                    driving=today.driv_used;
                                    offduty=today.offduty_used;

                                    Log.e("offduty",""+offduty);
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
                                       // txtokcertify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
                                       // txtokcertify.setVisibility(View.VISIBLE);
                                         txtclarify.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
                                        txtclarify.setBackgroundResource(R.drawable.button_activate_ident);
                                        //txtclarify.setVisibility(View.INVISIBLE);
                                    }else if(stat.contentEquals("2"))
                                    {
                                        txtclarify.setBackgroundResource(R.drawable.button_grey);
                                        txtclarify.setText("NOT REQUIRED");
                                        txtclarify.setVisibility(View.INVISIBLE);
                                    }

                                    else{
                                        txtclarify.setBackgroundResource(R.drawable.button_ident);
                                        txtclarify.setText(" CERTIFY NOW ");
                                        txtclarify.setVisibility(View.VISIBLE);

                                       // txtokcertify.setVisibility(View.GONE);
                                    }

                                }else{
                                    txtclarify.setBackgroundResource(R.drawable.button_ident);
                                    txtclarify.setText(" CERTIFY NOW ");
                                    txtclarify.setVisibility(View.VISIBLE);

                                   // txtokcertify.setVisibility(View.GONE);
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

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                            //Log.e("gerror",""+e.toString());
                        }
                    }else{
                        // cancelprogresssdialog();
                        Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                   // Log.e("ddf"," Load More Response Error "+String.valueOf(response.code()));
                    //dialogz.dismiss();
                    // getdata();
                    //cancelprogresssdialog();
                }
            }

            @Override
            public void onFailure(Call<List<Summarymodel>> call, Throwable t) {
                //Log.e("ddd"," Load More Response Error "+t.getMessage());
                //  cancelprogresssdialog();
            }
        });
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

        dialogcontact = new Dialog(context, R.style.DialogTheme);
        dialogcontact.setCancelable(false);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

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
                " * Generate report time save signature ";
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.file_privacy, null);
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
            @RequiresApi(api = Build.VERSION_CODES.M)
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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


}
