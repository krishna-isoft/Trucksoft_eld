package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Getvalue_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transparent_ondutyalert extends Activity {
   Button btnsubmit;
     Button btnno;
     ImageView imgstatus;
     TextView txtalert;
     TextView txtstatus;
     private Context context;
     Preference pref;
     Font_manager font_manager;

    Eld_api api;
    private CommonUtil commonUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.onduty_exceedalertbb);
        context=this;
        pref=Preference.getInstance(context);
        font_manager=new Font_manager();
        commonUtil=new CommonUtil(context);
        btnsubmit = findViewById(R.id.btn_submit);
        btnno = findViewById(R.id.btn_no);
       imgstatus = findViewById(R.id.txt_img);
        txtalert =findViewById(R.id.txtalert);
      txtstatus =findViewById(R.id.txt_status);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                statusondutyalert();
                pref.putString(Constant.ONDUTY_ALERT,"activate");
                SimpleDateFormat fzz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dte = fzz.format(new Date());
                pref.putString(Constant.ONDUTY_ALERT_DATES,dte);

            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat fzz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dte = fzz.format(new Date());
                pref.putString(Constant.ONDUTY_ALERT_DATES,dte);
                pref.putString(Constant.ONDUTY_ALERT,"deactivate");
                onClickStatuspcOffDuty("0");
                finish();

            }
        });
    }

    private void onClickStatuspcOffDuty(String status) {
        gettodaysavevalues(commonUtil.OFF_DUTY, 4, status);
    }

    private void gettodaysavevalues(final String field, final int statusid, final String statuss) {


        String did = pref.getString(Constant.DRIVER_ID);

        api = ApiServiceGenerator.createService(Eld_api.class);

//        Call<List<Getvalue_model>> call = api.getsaveValues(vinnumber,field,""+statusid,statuss,lat,lon,did,address,olddversion);
//        Log.e("saveurl","eld_savedata.php?vin="+vinnumber+"&fname="+field+
//        "&statusid="+statusid+"&pc_status="+statuss+"&lat="+lat+"&lon="+lon+"&did="+did+
//         "&address="+straddress+"&versiion="+olddversion+"&testbreak=");
        Call<List<Getvalue_model>> call = api.getsaveValues_eld(""+pref.getString(Constant.VIN_NUMBER), field, "" + statusid, statuss, "", "", did, "", "", "", "", "","break");
        call.enqueue(new Callback<List<Getvalue_model>>() {
            @Override
            public void onResponse(Call<List<Getvalue_model>> call, Response<List<Getvalue_model>> response) {
                if (response.isSuccessful()) {




                } else {


                    //Log.e("ss"," Response "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Getvalue_model>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());

                // getvehicle();
            }
        });
    }

    public void statusondutyalert() {
finish();
        if (OnlineCheck.isOnline(this)) {


            if (OnlineCheck.isOnline(this)) {

                api = ApiServiceGenerator.createService(Eld_api.class);


                Call<JsonObject> call = null;

//                    Log.e("paramslist", "driver="+pref.getString(Constant.DRIVER_ID)
//                    +"&vin="+pref.getString(Constant.VIN_NUMBER)+"&cc="+pref.getString(Constant.COMPANY_CODE)
//                    +"&logid="+strondutylogid+"&value=1");
                String id="";
                try{
                    id=pref.getString(Constant.C_ONDUTY_TIME_ID);
                }catch (Exception e)
                {

                }
                call = api.updateondutyalertstatus(pref.getString(Constant.DRIVER_ID), pref.getString(Constant.VIN_NUMBER), pref.getString(Constant.COMPANY_CODE), ""+id, "1");


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        // Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();

                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

//                                        String status = resp
//                                                .getString("status");
                                        //  if(status !=null && status.contentEquals("1"))
                                        //   {
                                        String message = resp
                                                .getString("message");
                                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                        // }
                                    }
                                } catch (Exception e) {

                                }


                            } else {
                                // Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }
                        } else {

                            //   Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        // Log.e("imageresponseerrr",""+t.toString());
                    }
                });

            }
        }
    }

}
