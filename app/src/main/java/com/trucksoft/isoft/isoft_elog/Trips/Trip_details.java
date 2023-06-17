package com.trucksoft.isoft.isoft_elog.Trips;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.driverchecklist.DispatchServiceGenerator;
import com.isoft.trucksoft_elog.driverchecklist.Dispatchline_decoder;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Trip_details extends AppCompatActivity {
    Trip_receyadap adapter;
    RecyclerView recyclerView;
    List<Triplist_model> movies;
    Context contexts;
    Preference pref;
    ProgressDialog dialog;
    Eld_api api;
    private static TextView txtfromdate,date_to;

    static int value=1;
    static TextView txtsearch;
    private ImageView imgback;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    String strfdate="";
    String strtodate="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_home);
        contexts=this;
        movies=new ArrayList<>();
        pref=Preference.getInstance(contexts);
        pref.putString(Constant.LAST_INDEX,"0");
        txtfromdate=findViewById(R.id.tfromdate);
        txtsearch=findViewById(R.id.tsearch);
        date_to=findViewById(R.id.ttodate);
        imgback=findViewById(R.id.driver_list_iv_back);
        recyclerView =findViewById(R.id.driver_list_listView);

        adapter = new Trip_receyadap(contexts, movies);


        adapter.setLoadMoreListener(new Trip_receyadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        if(movies.size()<=24)
                        {

                        }else {
                            loadMore(index, "" + strfdate, "" + strtodate);
                        }
                    }
                });
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = DispatchServiceGenerator.createService(Eld_api.class, contexts);

        load(0,"" + strfdate, "" + strtodate);

        txtfromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=1;
                DialogFragment newFragment2 = new SelectDateFragment();
                newFragment2.show(getSupportFragmentManager(), "DatePicker");
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(contexts, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
                    finish();
//                }
            }
        });


        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=2;
                DialogFragment newFragment2 = new SelectDateFragment();
                newFragment2.show(getSupportFragmentManager(), "DatePicker");
            }
        });
        txtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 strfdate=txtfromdate.getText().toString();
                 strtodate=date_to.getText().toString();
                if(txtsearch.getText().toString().contentEquals("Search") || txtsearch.getText().toString().contentEquals("search")) {
                    if ((strfdate != null && strfdate.length() > 0 && !strfdate.contentEquals("From Date")) && (strtodate != null && strtodate.length() > 0 && !strtodate.contentEquals("To Date"))) {
                        recyclerView.removeAllViews();
                        movies.clear();
                        load(0, strfdate, strtodate);
                        txtsearch.setText("Show All");
                    } else if (strfdate != null && strfdate.length() > 0) {
                        Toast.makeText(contexts, "Please select From date", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexts, "Please select To date", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    recyclerView.removeAllViews();
                    movies.clear();
                    strfdate="";
                    strtodate="";
                    txtsearch.setText("Search");
                    txtfromdate.setText("From Date");
                    date_to.setText("To Date");
                    load(0,"" + strfdate, "" + strtodate);
                }

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
    private void load(int index,String strfromdate,String strtodate){

Log.e("kd",""+strfromdate);
        Log.e("kdzz",""+strtodate);
        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        String page="0";
Log.e("cval","http://eld.e-logbook.info/elog_app/driver_trips.php?driver="+pref.getString(Constant.DRIVER_ID)+"&from="+strfromdate+"&to="
+strtodate+"&page="+page+"&cc="+pref.getString(Constant.COMPANY_CODE)+"&val=new");

        Call<List<Triplist_model>> call = api.getTrips(pref.getString(Constant.DRIVER_ID),""+strfromdate,""+strtodate,""+page,pref.getString(Constant.COMPANY_CODE),"new");

        call.enqueue(new Callback<List<Triplist_model>>() {
            @Override
            public void onResponse(Call<List<Triplist_model>> call, Response<List<Triplist_model>> response) {
                //Log.e(" Responsev"," "+response.toString());
              //  Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                    if(dialog !=null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    //  Log.e(" Responsecqevv","z "+response.body());
                    if(response.body() !=null) {
                        movies.addAll(response.body());
                    }else{

                    }

                    //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                    dialog.dismiss();
                    // load(0);
                    // Log.e("ggg"," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Triplist_model>> call, Throwable t) {
                // Log.e("tttt"," Response Error "+t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void loadMore(int index,String strfromdate,String strtodate){
//Log.e("indexc","loadmpre"+index);
        //add loading progress view
        movies.add(new Triplist_model("load"));
        adapter.notifyItemInserted(movies.size()-1);

        String page=pref.getString(Constant.LAST_INDEX);
        int ab= Integer.parseInt(pref.getString(Constant.LAST_INDEX));
        ab++;
        pref.putString(Constant.LAST_INDEX,""+ab);
        Call<List<Triplist_model>> call = api.getTrips(pref.getString(Constant.DRIVER_ID),""+strfromdate,""+strtodate,""+ab,pref.getString(Constant.COMPANY_CODE),"new");

        call.enqueue(new Callback<List<Triplist_model>>() {
            @Override
            public void onResponse(Call<List<Triplist_model>> call, Response<List<Triplist_model>> response) {
                // Log.e(" Responsekk","z "+response.toString());
                //Log.e(" Responsesskk"," z"+String.valueOf(response.code()));

                if(response.isSuccessful()){
                    //  Log.e(" Responsec","z "+response.body());
                    //remove loading view
                    movies.remove(movies.size()-1);

                    List<Triplist_model> result = response.body();
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
            public void onFailure(Call<List<Triplist_model>> call, Throwable t) {
                //  Log.e(TAG," Load More Response Error "+t.getMessage());
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



            //Log.e("month",""+month);
           // Log.e("value",""+value);
            if (month < 10 && day < 10) {
                if (value == 1)
                    txtfromdate.setText(year + "-0" + month + "-0" + day);
                else if (value == 2)
                    date_to.setText(year + "-0" + month + "-0" + day);
            } else if (day < 10) {
                if (value == 1)
                    txtfromdate.setText(year + "-" + month + "-0" + day);
                else if (value == 2)
                    date_to.setText(year + "-" + month + "-0" + day);
            } else if (month < 10) {
                if (value == 1)
                    txtfromdate.setText(year + "-0" + month + "-" + day);
                else if (value == 2)
                    date_to.setText(year + "-0" + month + "-" + day);
            } else {
                if (value == 1)
                    txtfromdate.setText(year + "-" + month + "-" + day);
                else if (value == 2)

                    date_to.setText(year + "-" + month + "-" + day);
            }


            String strfdate=txtfromdate.getText().toString();
            String strtodate=date_to.getText().toString();
            if ((strfdate != null && strfdate.length() > 0 && !strfdate.contentEquals("From Date")) && (strtodate != null && strtodate.length() > 0 && !strtodate.contentEquals("To Date"))) {
                txtsearch.setText("Search");
            }




        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent ink = new Intent(contexts, Home_activity.class);
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
        Trucksoft_elog_Notify_Utils.clearNotifications(contexts);
    }


    private void applogout()
    {
        Log.e("called","kk************logout");
        api = DispatchServiceGenerator.createService(Eld_api.class,contexts);
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
        pref = Preference.getInstance(contexts);
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
            finish();
            Intent mIntent = new Intent(contexts,
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
}
