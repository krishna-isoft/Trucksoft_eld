package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.PicassoTrustAll;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Serviceticket_view extends AppCompatActivity {
    private Context context;
    private Intent intent;
    Serviceticket_model smodel;
    Font_manager font_manager;
    private TextView txtnickname;
    private TextView txtvinnumber;
    private TextView txtvehiclenumber;
    private TextView txtreporteddate;
    private TextView txtmessage;
    private TextView txtback;
    private ImageView imgticket;
    private LinearLayout linimg;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    Dialog dialograting;
    ProgressDialog progressdlog;
    private TextView tfixed;
    private TextView tnotfixed;
    Eld_api api;
    Preference pref;
    private EditText edtremark;
    private String strstatus="Fixed";
    private String strrating="";
    private LinearLayout linfooter;
    private TextView txtsid;
    private TextView treopen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceticket_view);
        context=this;
        intent=getIntent();
        pref=Preference.getInstance(context);
        font_manager=new Font_manager();
        linfooter=findViewById(R.id.linfooter);
        txtnickname=findViewById(R.id.tnickname);
        tnotfixed=findViewById(R.id.tnotfixed);
        treopen=findViewById(R.id.treopen);
        txtsid=findViewById(R.id.tsid);
        txtvinnumber=findViewById(R.id.tvin);
        edtremark=findViewById(R.id.tremark);
        txtvehiclenumber=findViewById(R.id.tvnumber);
        txtreporteddate=findViewById(R.id.tdate);
        txtmessage=findViewById(R.id.tmessage);
        txtback=findViewById(R.id.txtback);
        imgticket=findViewById(R.id.img_view);
        linimg=findViewById(R.id.linimg);
        tfixed=findViewById(R.id.tfixed);
        txtback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        smodel= (Serviceticket_model) getIntent().getSerializableExtra("servicevalue");
       // Log.e("sid","@"+smodel.nickname);
        txtnickname.setText(""+smodel.nickname);
        txtvinnumber.setText(""+smodel.vehicle);
        txtvehiclenumber.setText(""+smodel.truckno);
        txtreporteddate.setText(""+smodel.date_reported);
        txtmessage.setText(""+smodel.message);
        if(smodel.attachments.contentEquals("1"))
        {
            linimg.setVisibility(View.VISIBLE);
            PicassoTrustAll.getInstance(context)
                    .load(smodel.img+"?.time();")
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                   // .error(R.drawable.whitekt)
                    .into(imgticket);

        }
        treopen.setVisibility(View.GONE);
if(smodel.remark !=null)
{
    edtremark.setText(""+smodel.remark);
}

        txtsid.setText("S. No#"+smodel.stid);
        if(smodel.status.contentEquals("1"))
        {
            linfooter.setVisibility(View.GONE);
            edtremark.setEnabled(false);
            treopen.setVisibility(View.VISIBLE);
        }
        tfixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tfixed","clicked");
                callratingstar();
            }
        });
        tnotfixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strstatus="Not fixed";
                saveeditdata("edit");
            }
        });

        imgticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intenttt = new Intent(context, Image_view_ticket.class);
                intenttt.putExtra("img_url", "" + smodel.img);
                startActivity(intenttt);
            }
        });
        treopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveeditdata("open");
            }
        });

        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttt = new Intent(context, Serviceticket_home.class);
                startActivity(intenttt);
                finish();
            }
        });
    }




    private void callratingstar() {
        try{

            if (dialograting != null) {
                if (dialograting.isShowing()) {
                    dialograting.dismiss();
                }
            }
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView;

            dialogView = inflater.inflate(R.layout.rating_dialog, null);


            final TextView tok = dialogView.findViewById(R.id.tok);
            final TextView tlater = dialogView.findViewById(R.id.tlater);
            ratingBar =  dialogView.findViewById(R.id.ratingBar);
            txtRatingValue =  dialogView.findViewById(R.id.txtRatingValue);

            final TextView tclose = dialogView.findViewById(R.id.txtalert);
            dialograting = new Dialog(context, R.style.DialogTheme);
            dialograting.setCancelable(false);
            tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
            //  tname.setText(""+pref.getString(Constant.COMPANY_NAME));
            dialograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialograting.setContentView(dialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialograting.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialograting.getWindow().setAttributes(lp);
            dialograting.show();

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    txtRatingValue.setText(String.valueOf(rating));

                }
            });
            tclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialograting.dismiss();
                }
            });

            tok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ratting","@"+txtRatingValue.getText().toString());
                    strstatus="Fixed";
                    strrating=txtRatingValue.getText().toString();
                    dialograting.dismiss();
                    saveeditdata("edit");
                }
            });

            tlater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    strstatus="Fixed";
                    strrating="";
                    dialograting.dismiss();
                    saveeditdata("edit");
                }
            });

            //tlater.setOnClickListener();
        }catch (Exception e)
        {
Log.e("dialogerror","@"+e.toString());
        }

    }
    public void saveeditdata(String type) {

        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        if (OnlineCheck.isOnline(this)) {


            if (OnlineCheck.isOnline(this)) {
                progressdlog = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();

                api = ApiServiceGenerator.createService(Eld_api.class);
                Call<JsonObject> call=null;

if(type.contentEquals("edit")) {
    call = api.editserviceticket(pref.getString(Constant.DRIVER_ID), smodel.id, edtremark.getText().toString(), strstatus,
            strrating);
}else{
    String id=smodel.parent_id;
    if(smodel.parent_id.contentEquals("0"))
    {
         id=smodel.id;
    }
    call = api.reopenticket(pref.getString(Constant.DRIVER_ID), id, pref.getString(Constant.LICENSE_NUMBER), pref.getString(Constant.COMPANY_CODE),pref.getString(Constant.DRIVER_NAME),
            pref.getString(Constant.COMPANY_NAME),pref.getString(Constant.DRIVER_NAME));
}


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {
                            cancelprogresssdialog();
                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                Log.e("jsonresponse", "@"+jsonresponse.toString());
                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

                                        String status = resp
                                                .getString("status");
                                        if(status !=null && status.contentEquals("1"))
                                        {
                                            String message=resp
                                                    .getString("message");
                                            Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
                                            Intent intenttt = new Intent(context, Serviceticket_home.class);
                                            startActivity(intenttt);
                                            finish();
                                        }
                                    }
                                }catch (Exception e)
                                {

                                }


                            } else {
                                Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            cancelprogresssdialog();
                            Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        cancelprogresssdialog();
                        Log.e("imageresponseerrr",""+t.toString());
                    }
                });

            }}
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intenttt = new Intent(context, Serviceticket_home.class);
        startActivity(intenttt);
        finish();
    }
}
