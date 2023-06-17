package com.trucksoft.isoft.isoft_elog.Vehicle;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.Dispat_Application;
import com.isoft.trucksoft_elog.Isoft_activity.Home_activity_bluetooth;
import com.isoft.trucksoft_elog.Isoft_activity.MovableFloatingActionButton;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Dispatchline_decoder;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Model_class.Update_vehicle;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class Vehicle_seect extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener {
    private static final String TAG = Vehicle_seect.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;

    Preference pref;
    Context context;
    ProgressDialog dialog;
    Eld_api api;
    // url to fetch contacts json
    List<Update_vehicle> listupdatevechile;
    private   String URL = "";
    private int MY_SOCKET_TIMEOUT_MS=18000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;

    private EditText edtsearch;
    private ImageView imglogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

context=this;
        // toolbar fancy stuff
        listupdatevechile=new ArrayList<>();
        pref=Preference.getInstance(context);
        edtsearch=findViewById(R.id.edt_search);
        imglogout=findViewById(R.id.img_logout);
        String license=pref.getString(Constant.LICENSE_NUMBER);
        String ccode=pref.getString(Constant.COMPANY_CODE);
       // URL="http://e-logbook.info/elog_app/chat/v1/get_details/"+license+"/"+ccode;

        URL="http://eld.e-logbook.info/elog_app/chat/v1/get_vehicledetails/"+license+"/"+ccode;

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(context, contactList, this);
        if(pref.getString(Constant.BL_SCANNING_FLAG) !=null && pref.getString(Constant.BL_SCANNING_FLAG).length()>0)
        {

        }else{
            pref.putString(Constant.BL_SCANNING_FLAG,"0");
        }
        // white background notification bar
        whiteNotificationBar(recyclerView);

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
//        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

        fetchContacts();
        edtsearch.addTextChangedListener(tc);

        MovableFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineCheck.isOnline(context)) {
                    clearData();
                }
            }
        });

       // getSupportActionBar().setDisplayShowHomeEnabled(true);
     //   getSupportActionBar().setIcon(R.drawable.back_dashboard);


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
                        }



                    }
                }
            }
        };
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.LOGIN_CHECK,
                        "logged_off");
                pref.putString(Constant.ELOG_NUMBERSS,
                        "");
                //pref.getString(Constant.ELOG_NUMBERSS);
                pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
                pref.putString(Constant.DRIVE_NOTIFICATON, "0");
                logout_okk();

                Intent mIntent12 = new Intent(Vehicle_seect.this,
                        Loginactivitynew.class);

                startActivity(mIntent12);
                finish();
            }
        });
    }

    /**
     * fetches json by making http calls
     */
    private void fetchContacts() {
        Dispat_Application.getInstance().getRequestQueue().getCache().remove(URL);
        dialog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Getting vehicle Please wait...");
        dialog.setCancelable(false);
        dialog.show();
       // Log.e("url",""+URL);
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<Contact> items=new ArrayList<>();
                        items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
                        }.getType());


                        try {
                            if ((dialog != null) && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (final IllegalArgumentException e) {
                            // Handle or log or ignore
                        } catch (final Exception e) {
                            // Handle or log or ignore
                        } finally {
                           dialog = null;
                        }
                       // dialog.dismiss();
                        contactList.clear();

                        contactList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                try {
                    if ((dialog != null) && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    dialog = null;
                }
               // Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                fetchContacts();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Dispat_Application.getInstance().addToRequestQueue(request);

    }














    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            dialog = null;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            dialog = null;
        }
        super.onPause();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Contact contact) {
       // Toast.makeText(getApplicationContext(), "Selected: " + contact.getmodel_name() + ", " + contact.getvin(), Toast.LENGTH_LONG).show();
        String nam=contact.getmodel_name();

        String vivz=contact.getvin();
        if(contact.getstatus().trim().contentEquals("1"))
        {
            String message=contact.message;
if(message !=null && message.length()>0 && !message.contentEquals("null"))
{
  //  Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
    alertDialogmessage(message);

}else {
   // Toast.makeText(context, "The Selected Vehicle is UNAVAILABLE. Please select another one.", Toast.LENGTH_LONG).show();
alertDialogmessage("Vechile in Use by another Driver");
}
        }else if(contact.vehiclestatus.contentEquals("stale"))
        {
            String message=contact.message;
            if(message !=null && message.length()>0 && !message.contentEquals("null"))
            {
                //  Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
                alertDialogmessage(message);

            }else {
                // Toast.makeText(context, "The Selected Vehicle is UNAVAILABLE. Please select another one.", Toast.LENGTH_LONG).show();
                alertDialogmessage("Vechile Device Unplugged");
            }
        }


        else{

            String vstatus=""+contact.getVehiclestatus();
            if(vstatus !=null && vstatus.length()>0 && !vstatus.contentEquals("null")) {
                if (vstatus.contentEquals("On") || vstatus.contentEquals("on")) {
                    pref.putString(Constant.ENGINE_STATUS_FIELD,"IGN_ON");
                } else if (vstatus.contentEquals("moving")) {
                    pref.putString(Constant.ENGINE_STATUS_FIELD,"IGN_ON");
                }
            }

            //Log.e("st","not assigned");
            updatevechlelist(vivz,nam);
        }
    }
    private void updatevechlelist(final String vivz, final String nam)
    {
        String ccode=pref.getString(Constant.COMPANY_CODE);
        //Log.e("ccode",""+ccode);
        String licnse=pref.getString(Constant.LICENSE_NUMBER);
        String did=pref.getString(Constant.DRIVER_ID);
        dialog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Update vehicle Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);

//        Log.e("urlup","http://eld.e-logbook.info/elog_app/update_vehicle_elog.php?ccode="+ccode+
//        "&license="+licnse+"&vin="+vivz+"&nickname="+nam);
        Call<List<Update_vehicle>> call = api.updatevehicle(ccode,licnse,vivz,nam,did);

        call.enqueue(new Callback<List<Update_vehicle>>() {
            @Override
            public void onResponse(Call<List<Update_vehicle>> call, retrofit2.Response<List<Update_vehicle>> response) {
                if(response.isSuccessful()){
                    try {
                        if ((dialog != null) && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        dialog = null;
                    }
                    listupdatevechile=response.body();
                    setVal(listupdatevechile);

                }else{
                    try {
                        if ((dialog != null) && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        dialog = null;
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Update_vehicle>> call, Throwable t) {
                // Log.e("dd"," Response Error "+t.getMessage());
                try {
                    if ((dialog != null) && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    dialog = null;
                }
                // getvehicle();
                updatevechlelist(vivz,nam);
            }
        });
    }

    private void setVal(List<Update_vehicle> listupdatevechile) {
        if(listupdatevechile.size()>0)
        {
            for(int i=0;i<listupdatevechile.size();i++)
            {
                Update_vehicle upc=new Update_vehicle();
                upc=listupdatevechile.get(i);

                String status=upc.respstatus;
              //  status="2";
                if(status.contentEquals("1"))
                {
                    String model_name=upc.model_name;
                    //Log.e("new md","@"+model_name);
                    pref.putString(Constant.MODEL_NAME,
                            model_name);
                    String viz=upc.vin;
                    //Log.e("viz",""+viz);
                    pref.putString(Constant.VIN_NUMBER,
                            viz);

                    String make=upc.make;
                    String year=upc.year;
                    if(make !=null && make.length()>0 && !make.contentEquals("null")) {
                        pref.putString(Constant.ELD_MAKE, "" + make);
                    }else{
                        pref.putString(Constant.ELD_MAKE, "");
                    }

                    if(year !=null && year.length()>0 && !year.contentEquals("null")) {
                        pref.putString(Constant.ELD_YEAR, "" + year);
                    }else{
                        pref.putString(Constant.ELD_YEAR, "");
                    }
                    String devicetype=upc.d_type;




                    String vimg=upc.vehicle_image;
                    pref.putString(Constant.VECHLE_IMAGES,
                            vimg);
                    //Log.e("vimg",""+vimg);
                    String vvname=upc.vehicle_name;
                    String deviceAddress=upc.blu_address;
                  //  Log.e("deviceAddress","@"+deviceAddress);
                    String devicename=upc.blu_name;
                    //Log.e("devicename","@"+devicename);
String dkey=upc.eldkey;

                    // pref.putString(Constant.VID_NUMBER,
                    //      txtvno);
                    pref.putString(Constant.VID_NUMBER,
                            vvname);
                    if(dkey !=null && dkey.length()>0 && !dkey.contentEquals("null")) {
                        pref.putString(Constant.ELD_KEY, "" + dkey);
                    }else{
                        pref.putString(Constant.ELD_KEY, "");
                    }
//                    if(make !=null && make.length()>0 && !make.contentEquals("null")) {
//                        pref.putString(Constant.ELD_MAKE, "" + make);
//                    }else{
//                        pref.putString(Constant.ELD_MAKE, "");
//                    }
//
//                    if(year !=null && year.length()>0 && !year.contentEquals("null")) {
//                        pref.putString(Constant.ELD_YEAR, "" + year);
//                    }else{
//                        pref.putString(Constant.ELD_YEAR, "");
//                    }

                    if(devicetype !=null && devicetype.length()>0) {
                       // Log.e("model_name","@"+devicetype);
                        if (devicetype.contentEquals("1")) {
                            //Log.e("condition","@success");
                      pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH,"yes");
                        }else{
                            pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH,"no");
                        }
                    }else{
                        pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH,"no");
                    }

                    if(deviceAddress !=null && deviceAddress.length()>0) {
                        pref.putString(Constant.BLUETOOTH_ADDRESS, "" + deviceAddress);
                        pref.putString(Constant.BLUETOOTH_NAME, "" + devicename);
                    }else{
                        pref.putString(Constant.BLUETOOTH_ADDRESS, "");
                        pref.putString(Constant.BLUETOOTH_NAME, "");
                    }
//Log.e("v_status","&"+pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH));
                    if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) !=null &&
                    pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("yes"))
                    {
                        pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"on");
                        pref.putString(Constant.NETWORK_TYPE, "" + Constant.BLUETOOTH);
                        Intent mIntent = new Intent(
                                Vehicle_seect.this,
                                Home_activity_bluetooth.class);
                        startActivity(mIntent);
                        finish();
                    }else {
                        pref.putString(Constant.NETWORK_TYPE, "" + Constant.CELLULAR);
//                        Intent mIntent = new Intent(
//                                Vehicle_seect.this,
//                                Home_activity.class);
                        Intent mIntent = new Intent(
                                Vehicle_seect.this,
                                Home_activity_bluetooth.class);
                        startActivity(mIntent);
                        finish();
                    }

                }else if(status.contentEquals("2"))
                {
                    String message=upc.message;

                    if(message !=null && message.length()>0 && !message.contentEquals("null"))
                    {
                       // Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
                        alertDialogmessage(message);

                    }else {
                      //  Toast.makeText(context, "The Selected Vehicle is UNAVAILABLE. Please select another one.", Toast.LENGTH_LONG).show();
                        alertDialogmessage("Vechile in Use by another Driver");

                    }
                }

            }
        }
    }
    public void logout_okk()
    {
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.ELOG_NUMBERSS,
                "");
        //pref.getString(Constant.ELOG_NUMBERSS);
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        SimpleDateFormat formatsec= new SimpleDateFormat("HH:mm", Locale.getDefault());
		String dc =formatsec.format(new Date());
            api = ApiServiceGenerator.createService(Eld_api.class);
            //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
            Call<JsonObject> call = api.getlogout(""+pref.getString(Constant.DRIVER_ID), ""+pref.getString(Constant.DRIVER_ID)
                    , ""+pref.getString(Constant.VIN_NUMBER), "" + dc, "" , "","","","" );

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                    Log.e("Responsestring", responsez.body().toString());
                    //Toast.makeText()
                    if (responsez.isSuccessful()) {

                        if (responsez.body() != null) {
                            String jsonresponse = responsez.body().toString();
                            //Log.e("jsonresponse", jsonresponse.toString());
                            try {
                                JSONObject response = new JSONObject(jsonresponse);
                                try {

                                    Log.e("responselog", "" + response.toString());
                                    if (response != null) {

                                        String status = response
                                                .getString("status");




                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                //Log.e("Exceptionwwwwwwww", e.toString());
                            }


                        }
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    //Log.e("Exceptionwttttttt", t.toString());
                    // cancelprogresssdialogz();
                }
            });
        }


    private void alertDialogmessage(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("");
        builder.setMessage(""+message);

        builder.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        final AlertDialog adialog = builder.create();
        adialog.show(); //show() should be called before dialog.getButton().


        final Button positiveButton = adialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        positiveButton.setLayoutParams(positiveButtonLL);
    }
    public void clearData() {

        contactList.clear(); //clear list
        mAdapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(null);
        mAdapter = new ContactsAdapter(context, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        try
        {
            while (recyclerView.getItemDecorationCount() > 1) {
                recyclerView.removeItemDecorationAt(0);
            }

        }catch (Exception e)
        {

        }
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

        fetchContacts();
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
       // Log.e("called","kk************logout");
        api = ApiServiceGenerator.createService(Eld_api.class);
        String did=pref.getString(Constant.DRIVER_ID);
        String vin=pref.getString(Constant.VIN_NUMBER);
        cancelnotification();

        //  Log.e("url","&did="+did+"&name="+type);
        Call<List<Remark_model>> call = api.applogout(did,""+vin,did,""+pref.getString(Constant.CURRENT_LATITUDE),""+pref.getString(Constant.CURRENT_LOGINGITUDE));

        call.enqueue(new Callback<List<Remark_model>>() {
            @Override
            public void onResponse(Call<List<Remark_model>> call, retrofit2.Response<List<Remark_model>> response) {
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
      //  Log.e("called","************logout1");
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
        pref.putString(Constant.BLUETOOTH_TIMER_MANUALLY,"off");

        try {
            finish();
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
    public void cancelnotification() {


        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }

    TextWatcher tc=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            mAdapter.getFilter().filter(query);
        }

        @Override
        public void afterTextChanged(Editable query) {
            mAdapter.getFilter().filter(query);
        }
    };
}
