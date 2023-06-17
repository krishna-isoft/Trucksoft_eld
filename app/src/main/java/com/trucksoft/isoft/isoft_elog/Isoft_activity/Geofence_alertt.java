package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.trucksoft.isoft.isoft_elog.R;

public class Geofence_alertt extends Activity {
    Button btnsubmit;
    TextView ttitled;
    TextView txtalert;
    TextView txtstatus;
    Font_manager font_manager;
    private Context context;
    Intent inte;
    String title="";
    String msgval="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_alertk);
        Log.e("calb","geo alert");
        context=this;
        inte=getIntent();
       // if(inte.hasCategory("message"))
        //{
            title=inte.getStringExtra("typed");
        Log.e("titlezzz","@"+title);
            msgval=inte.getStringExtra("msgval");
       // }
        try {
            Uri alarmSound = null;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/appupdatetone");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
          btnsubmit = findViewById(R.id.btn_submit);
          ttitled = findViewById(R.id.ttitle);
          txtalert = findViewById(R.id.txtalert);
          txtstatus = findViewById(R.id.txt_status);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        ttitled.setText(""+title);
        txtstatus.setText(""+msgval);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();


            }
        });
    }
}
