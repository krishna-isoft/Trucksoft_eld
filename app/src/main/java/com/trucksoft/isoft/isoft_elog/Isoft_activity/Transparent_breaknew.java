package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transparent_breaknew extends Activity {
    private Context context;
    private Preference pref;
    private CommonUtil commonUtil;
    private Button btn_yes;
    private Button btn_no;
    private Button btn_skip;
    private String lat,lon;
    Eld_api api;
    private String strstate="";
    private String straddress="";
    private TextView txtstatus;
    public static final int RequestPermissionCode = 1;
    Font_manager font_manager;
    TextView txtalert;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newbreak_notification_traback);
        context=this;
        pref=Preference.getInstance(context);
        font_manager=new Font_manager();
        commonUtil=new CommonUtil(context);
         btn_yes =findViewById(R.id.btn_yes);
         btn_no = findViewById(R.id.btn_no);
        txtstatus = findViewById(R.id.txt_status);
        SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String cdtime = formatdatetime.format(new Date());
        pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
        pref.putString(Constant.BREAK_ALERT_DISPLAY, "skip");
        pref.putString(Constant.BREAK_ID_CURRENT, "" + pref.getString(Constant.BREAK_MSG_ID));

        try {
            txtstatus.setText(Html.fromHtml("" + pref.getString(Constant.BREAK_MESSAGE)));
        }catch (Exception e)
        {
           // txtstatus.setText("" + pref.getString(Constant.BREAK_MESSAGE));
        }
        btn_skip = findViewById(R.id.btn_skip);
        txtalert = findViewById(R.id.txtalert);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        if(lat==null)
        {
            lat=pref.getString(Constant.LATITUDE);
            lon=pref.getString(Constant.LONGITUDE);
        }
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.BREAK_ALERT_DISPLAY,"skip");
                SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String cdtime = formatdatetime.format(new Date());
                pref.putString(Constant.BREAK_DISPLAY_TIME, "" + cdtime);
                finish();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.BREAK_ALERT_DISPLAY,"taken");
                String strduration=pref.getString(Constant.BREAK_DURATION);
                int intduration=10;
                if(strduration !=null &&strduration.length()>0)
                {
                    intduration=Integer.parseInt(strduration);
                }
                acceptbreak(intduration,"auto");
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.BREAK_ALERT_DISPLAY,"skip");
                if(checkPermission()) {


                    Intent ink=new Intent(context,Recordvoice_activity_new.class);
                    ink.putExtra("breakid",""+pref.getString(Constant.BREAK_MSG_ID));
                    startActivity(ink);
                    finish();
                    //callrecorddialog();
                }else {
                    requestPermission();
                }
            }
        });
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(Transparent_breaknew.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(Transparent_breaknew.this, "Permission Granted",
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

    private void acceptbreak(int intduration,String strbreaktype)
    {
        //Log.e("callk",""+"savelocation"+"  "+cn);

        try {




            if (OnlineCheck.isOnline(context)) {
                String strstatus=commonUtil.OFF_DUTY;
                if(intduration <=10)
                {
                    if(pref.getString(Constant.CURRENT_STATUS).contentEquals(""+commonUtil.DRIVING))
                    {
                        strstatus=commonUtil.ON_DUTY;
                    }else{

                            strstatus=pref.getString(Constant.CURRENT_STATUS);

                    }
                }else{
                    if(pref.getString(Constant.CURRENT_STATUS).contentEquals(""+commonUtil.DRIVING) ||
                            pref.getString(Constant.CURRENT_STATUS).contentEquals(""+commonUtil.ON_DUTY))
                    {
                        strstatus=commonUtil.OFF_DUTY;
                    }else{
                        strstatus=pref.getString(Constant.CURRENT_STATUS);
                    }
                }
                String strbrid="0";

                    strbrid=pref.getString(Constant.BREAK_MSG_ID);


                api = ApiServiceGenerator.createService(Eld_api.class);



                Call<JsonObject> call = null;

                call = api.newbreakaccept(pref.getString(Constant.COMPANY_CODE), pref.getString(Constant.DRIVER_ID),
                        strbrid, "" + pref.getString(Constant.BREAK_TYPE), "" + pref.getString(Constant.BREAK_RULE), "" + strstate, "" + straddress, "" + lat, "" + lon, "break_accept", ""+strstatus,"","","");


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();

                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

                                        String status = resp
                                                .getString("status");
                                        if (status != null && status.contentEquals("1")) {
                                            pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
                                            pref.putString(Constant.BREAK_TAKEN,"taken");
                                            SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                            String time = formatdatetime.format(new Date());

                                            pref.putString(Constant.BREAK_ACTIVATED_TIME,""+time);
                                            String lastid = resp
                                                    .getString("lastid");
                                            pref.putString(Constant.BREAK_LAST_ID,""+lastid);
                                            finish();

                                        } else {

                                        }
                                    }
                                } catch (Exception e) {

                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });


            }

        }catch (Exception e)
        {

        }
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
