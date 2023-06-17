package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.ifta_adater;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.EApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.ifta_model;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ifta_activity  extends AppCompatActivity {

    ifta_adater mAdapter;
    ProgressDialog dialog;
    Eld_api api;
    List<ifta_model> movies;
    Context contexts;
    Preference pref;
    ListView mListView;
    private  String dates;
    TextView txtemail;
    private EditText edtemail;
    private ImageView imgback;
    private  String str_fromdate="";
    private String str_todate="";
    private Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifta);
        movies=new ArrayList<>();
        contexts=this;
        pref=Preference.getInstance(contexts);

        mListView=findViewById(R.id.itemlist_view_lv);

        txtemail=findViewById(R.id.txt_email);
        edtemail=findViewById(R.id.edt_email);
        imgback=findViewById(R.id.driver_list_iv_back);
        intent=getIntent();
        if(intent !=null) {
            str_fromdate = intent.getStringExtra("fromdate");
            str_todate = intent.getStringExtra("todate");
            fetchdata();
        }
imgback.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
//        Intent mIntent = new Intent(
//                Ifta_activity.this,
//                Home_activity_bluetooth.class);
//        startActivity(mIntent);
    }
});


txtemail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(edtemail.getText().toString() !=null && edtemail.getText().toString().length()>0)
        {


                    sendemail(edtemail.getText().toString());


        }else{
            Toast.makeText(contexts,"Please enter E-mail ID",Toast.LENGTH_SHORT).show();
        }

    }
});




        mAdapter = new ifta_adater(contexts,
                movies,dates);
        mListView.setAdapter(mAdapter);

    }

    private void sendemail(String emailz) {

            if (OnlineCheck.isOnline(contexts)) {
                if(dialog !=null && dialog.isShowing()) {

                }else {
                    dialog = new ProgressDialog(contexts,
                            AlertDialog.THEME_HOLO_LIGHT);
                    dialog.setMessage("Please wait...");
                    dialog.setCancelable(false);
                    dialog.show();
                }
                api = ApiServiceGenerator.createService(Eld_api.class);
                Call<JsonObject> call = api.sendemailifta("" + str_fromdate
                        , "" + str_todate, "" + emailz,pref.getString(Constant.COMPANY_CODE),""+pref.getString(Constant.VIN_NUMBER));

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                        // Log.e("Responsestring", responsez.body().toString());
                        //Toast.makeText()
                        if (responsez.isSuccessful()) {
                            cancelprogresssdialog();
Toast.makeText(contexts,"IFTA report sent to given email successfully",Toast.LENGTH_SHORT).show();
                            edtemail.setText("");
                        } else {
                            cancelprogresssdialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        cancelprogresssdialog();
                    }
                });
            }

    }


    private void fetchdata(){


        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        api = EApiServiceGenerator.createService(Eld_api.class, contexts);
        String page="0";
        Log.e(" urllzzz"," @"+"trucksummariesifta.php?driver="+pref.getString(Constant.DRIVER_ID)+"&from="
        +str_fromdate+"&to="+str_todate+"&cc=ts&vin=1XPBD49XXFD281910");
//        Log.e("cval","list_notification.php?driver="+pref.getString(Constant.DRIVER_ID)+"&from="+strfromdate+"&to="
//                +strtodate+"&page="+page+"&cc="+pref.getString(Constant.COMPANY_CODE)+"&vin="+pref.getString(Constant.VIN_NUMBER)
//                +"&lat="+lat+
//                        "&lon="+lon+
//                        "&address="+straddress+
//                        "&state="+strstate+
//                        "&feature=Notification");
        dates=str_fromdate+" to "+str_todate;
        Call<List<ifta_model>> call = api.getifta(pref.getString(Constant.DRIVER_ID),str_fromdate,str_todate,""+pref.getString(Constant.COMPANY_CODE),""+pref.getString(Constant.VIN_NUMBER));
       // Call<List<ifta_model>> call = api.getifta(pref.getString(Constant.DRIVER_ID),str_fromdate,str_todate,"ts","1XPBD49XXFD281910");

        call.enqueue(new Callback<List<ifta_model>>() {
            @Override
            public void onResponse(Call<List<ifta_model>> call, Response<List<ifta_model>> response) {

                if(response.isSuccessful()){
                    cancelprogresssdialog();
                    //  Log.e(" Responsecqevv","z "+response.body());
                    if(response.body() !=null) {
                        movies.addAll(response.body());
                    }else{

                    }

                    //  adapter = new Currentreportadap(context, movies);
                    mAdapter = new ifta_adater(contexts,
                            movies,dates);
                    mListView.setAdapter(mAdapter);
                   // adapter.notifyDataChanged();
                }else{
                    cancelprogresssdialog();
                    // load(0);

                }
            }

            @Override
            public void onFailure(Call<List<ifta_model>> call, Throwable t) {

                cancelprogresssdialog();
            }
        });
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
