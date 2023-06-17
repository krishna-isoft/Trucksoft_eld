package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Message_model;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Message_view extends AppCompatActivity {
    private Context context;
    Preference pref;
    ProgressDialog progressdlog;
    Eld_api api;
    private String straddress;
    private String strstate;
    private LinearLayout linexcep;
    private TextView txtback;
    Font_manager font_manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagelist_dialog);
        context=this;
        pref=Preference.getInstance(context);
        font_manager=new Font_manager();
        txtback=findViewById(R.id.txtback);
        txtback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        linexcep = findViewById(R.id.lin_excep);
        checkmessage();
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(context, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{

                    finish();
//                }
            }
        });
    }


    private void checkmessage()
    {
        String did = pref.getString(Constant.DRIVER_ID);
        // Log.e("did",""+did);
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
        String lat=pref.getString(Constant.C_LATITUDE);
        String lon=pref.getString(Constant.C_LONGITUDE);
        Call<List<Message_model>> call = api.getmessages(did,""+pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,""+straddress,""+strstate,"Message view");

        call.enqueue(new Callback<List<Message_model>>() {
            @Override
            public void onResponse(Call<List<Message_model>> call, Response<List<Message_model>> response) {
                if (response.isSuccessful()) {
                    cancelprogresssdialog();
                    List<Message_model> lvm = response.body();
setdisplaymessag(lvm);

                } else {
                    // Log.e("result","fail");
                    cancelprogresssdialog();
                    Toast.makeText(context, "Please try again!" , Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<List<Message_model>> call, Throwable t) {
                // Log.e("dd"," Response Error "+t.getMessage());
                cancelprogresssdialog();
                Toast.makeText(context, "Please try again!" , Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void setdisplaymessag(List<Message_model> listmsgs) {
        if(listmsgs.size()>0)
        {
            for(int i=0;i< listmsgs.size();i++)
            {
                Message_model ev=new Message_model();
                ev=listmsgs.get(i);

                View layout2 = LayoutInflater.from(this).inflate(R.layout.msg_list, linexcep, false);

                TextView txttype =  layout2.findViewById(R.id.txt_type);
                TextView txtdetail =  layout2.findViewById(R.id.txt_detail);
                TextView txtsender=  layout2.findViewById(R.id.txt_sender);
                TextView txtdate=  layout2.findViewById(R.id.txt_date);
                TextView txtreaddate=  layout2.findViewById(R.id.txt_readdate);

                if(ev.created_at !=null&& ev.created_at.length()>0)
                {

                    txtdate.setText("Created at : "+yr(ev.created_at));
                }
                if(ev.read_at !=null&& ev.read_at.length()>0)
                {
                    if(ev.read_at.contentEquals("Now")|| ev.read_at.contentEquals("now"))
                    {
                        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String time = formatdatetime.format(new Date());
                        txtreaddate.setText("Read on : " + yr(time));
                    }else {
                        txtreaddate.setText("Read on : " + yr(ev.read_at));
                    }
                }

if(ev.sender.contentEquals("Company"))
{
    txtsender.setText(": "+pref.getString(Constant.COMPANY_NAME));
}else{
    txtsender.setText(": "+"E-logbook");
}
                if(ev.title !=null&& ev.title.length()>0)
                {
                    txttype.setText(": "+ev.title);
                }else
                {
                    txttype.setText(": ");
                }

                txtdetail.setText(": "+ev.msg);


                linexcep.addView(layout2);
            }
        }

    }

    private void cancelprogresssdialog()
    {
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

                //Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }



        return straddress;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent ink = new Intent(context, Home_activity.class);
//            startActivity(ink);
//            finish();
//        }else{
            finish();
//        }
    }

    private static String yr(String val)
    {
        String time="00:00";
        try {
            DateFormat dk = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat outputformatk = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

            Date date = null;


            try {

                date = dk.parse(val);

                time = outputformatk.format(date);
                Log.e("time", "@@" + time);
            } catch (Exception e) {
                Log.e("Exceptionzzz", "@" + e.toString());
            }
        }catch (Exception e)
        {

        }
        return time;
    }
}
