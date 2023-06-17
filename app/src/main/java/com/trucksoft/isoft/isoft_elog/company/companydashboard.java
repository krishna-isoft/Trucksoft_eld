package com.trucksoft.isoft.isoft_elog.company;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.isoft.trucksoft_elog.Company_Login;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

public class companydashboard extends AppCompatActivity {
    private ImageView imgdriver;
    private TextView tcompany;
    Preference pref;
    private Context context;
    private ImageView imglogout;
    private TextView tdotnumber;
    private TextView temail,tstate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmpdash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context=this;
        pref=Preference.getInstance(context);
        imgdriver=findViewById(R.id.imgdriver);
        imglogout=findViewById(R.id.iv_exit);
        tcompany=findViewById(R.id.txtcompany);
        tdotnumber=findViewById(R.id.txtdotnumber);
        temail=findViewById(R.id.txtemail);
        tstate=findViewById(R.id.txtstate);
        tcompany.setText("Company Name : "+pref.getString(Constant.COMPANY_NAME));
        tdotnumber.setText("DOT Number :"+pref.getString(Constant.COMPANY_DOT_NUMBER));
        temail.setText("E-mail :"+pref.getString(Constant.COMPANY_EMAIL));
        tstate.setText("State :"+pref.getString(Constant.COMPANY_STATE));
        imgdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        companydashboard.this,
                        Driverlist_home.class);
                startActivity(mIntent);

                finish();
            }
        });
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.COMPANY_LOGIN_STATUS,"fail");
                pref.putString(Constant.COMPANY_LOGIN_CODE,"");
                pref.putString(Constant.COMPANY_NAME,"");
                Intent mIntent = new Intent(
                        companydashboard.this,
                        Company_Login.class);
                startActivity(mIntent);

                finish();
            }
        });
    }
}
