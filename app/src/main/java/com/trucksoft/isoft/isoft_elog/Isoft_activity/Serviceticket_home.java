package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Serviceticket_home extends FragmentActivity implements View.OnClickListener {
    Serviceticket_receyadap adapter;
    RecyclerView recyclerView;
    List<Serviceticket_model> movies;
    Context contexts;
    Preference pref;
    ProgressDialog dialog;
    Eld_api api;
    TextView txtybe;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;

    private String lat="";
    private String lon="";
    private String strstate="";
    private String straddress="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceticket_home);
        contexts=this;
       // Log.e("calling","onactivity");
        movies=new ArrayList<>();
        pref=Preference.getInstance(contexts);
        recyclerView =findViewById(R.id.driver_list_listView);
txtybe=findViewById(R.id.dds);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
        adapter = new Serviceticket_receyadap(contexts, movies);


        adapter.setLoadMoreListener(new Serviceticket_receyadap.OnLoadMoreListener() {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = DispatchServiceGenerator.createService(Eld_api.class, contexts);

        load(0);
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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.driver_list_iv_back:
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(contexts, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
                    finish();
//                }
                break;

            case R.id.driver_list_tv_add_report:
pref.putString(Constant.DLIST_STATUS,"add");
deleteCache(contexts);
                Intent mIntent = new Intent(Serviceticket_home.this,
                        Serviceticket_elog.class);
                pref.putString("seletecteditem","");
                pref.putString("seletecteditemtrailer","");
                mIntent.putExtra("action", "add");
                pref.putString(Constant.VIEW_BACK,"ddddd");
                startActivityForResult(mIntent,1);

                break;

        }
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
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
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void load(int index){

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
      //  String baseurl=pref.getString(Constant.BASE_URL_IMAGE);
        //clearData();
        Log.e("urldz","http://eld.e-logbook.info/elog_app/list_service_tickets.php?page="
        +page+"&did="+pref.getString(Constant.DRIVER_ID)+"&cc="+pref.getString(Constant.COMPANY_CODE));
        Call<List<Serviceticket_model>> call = api.getServicetickets(page,pref.getString(Constant.DRIVER_ID),pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Service ticket");

        call.enqueue(new Callback<List<Serviceticket_model>>() {
            @Override
            public void onResponse(Call<List<Serviceticket_model>> call, Response<List<Serviceticket_model>> response) {
                 Log.e(" Responsev"," "+response.toString());
                 Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                    if(dialog !=null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                      Log.e(" Responsecqevv","z "+response.body());
                    if(response.body() !=null) {
                        movies.addAll(response.body());
                    }else{

                    }

                    //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                    dialog.dismiss();
                    // load(0);
                     Log.e("ggg"," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Serviceticket_model>> call, Throwable t) {
                Log.e("tttt"," Response Error "+t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void loadMore(int index){
//Log.e("call","loadmpre");
        //add loading progress view
        movies.add(new Serviceticket_model("load"));
        adapter.notifyItemInserted(movies.size()-1);

        String page=pref.getString(Constant.LAST_INDEX);
        int ab= Integer.parseInt(pref.getString(Constant.LAST_INDEX));
        ab++;
        pref.putString(Constant.LAST_INDEX,""+ab);
        Call<List<Serviceticket_model>> call = api.getServicetickets(page,pref.getString(Constant.DRIVER_ID),pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Service ticket");
        call.enqueue(new Callback<List<Serviceticket_model>>() {
            @Override
            public void onResponse(Call<List<Serviceticket_model>> call, Response<List<Serviceticket_model>> response) {
                // Log.e(" Responsekk","z "+response.toString());
                //Log.e(" Responsesskk"," z"+String.valueOf(response.code()));

                if(response.isSuccessful()){
                    //  Log.e(" Responsec","z "+response.body());
                    //remove loading view
                    movies.remove(movies.size()-1);

                    List<Serviceticket_model> result = response.body();
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
            public void onFailure(Call<List<Serviceticket_model>> call, Throwable t) {
                //  Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent ink = new Intent(contexts, Home_activity.class);
//            startActivity(ink);
//
//            finish();
//        }else{
            finish();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("calling","resume");
        pref=Preference.getInstance(contexts);
        String d=pref.getString(Constant.VIEW_BACK);
        Log.e("d",""+d);
        if( d!=null && d.length()>0) {
            if (d.contentEquals(Constant.VIEW_BACK)) {
                clearData();
                adapter = new Serviceticket_receyadap(contexts, movies);


                adapter.setLoadMoreListener(new Serviceticket_receyadap.OnLoadMoreListener() {
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
                recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
                recyclerView.addItemDecoration(new Dispatchline_decoder(2));
                recyclerView.setNestedScrollingEnabled(false);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);

                api = DispatchServiceGenerator.createService(Eld_api.class, contexts);

                load(0);
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

        // clearing the notification tray
        Trucksoft_elog_Notify_Utils.clearNotifications(contexts);
    }

    public void clearData() {
        movies.clear(); //clear list
        adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
    }


    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
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
        pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"failure");
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        pref.putString(Constant.VIN_NUMBER, "");
        pref.putString(Constant.BLUETOOTH_ADDRESS,"");
        pref.putString(Constant.BLUETOOTH_NAME,"");
        pref.putString(Constant.NETWORK_TYPE,Constant.CELLULAR);


        try {
            //finish();
            finishAffinity();
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

                //Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }



        return straddress;
    }

}
