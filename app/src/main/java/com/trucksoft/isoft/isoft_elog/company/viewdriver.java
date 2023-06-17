package com.trucksoft.isoft.isoft_elog.company;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.PhoneNumberFormattingTextWatcher;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class viewdriver extends AppCompatActivity {
    private Context context;
    String[] states = { "California","Alabama","Alaska","Arizona","Arkansas","Colorado","Connecticut ","Delaware","District of Columbia","Florida","Georgia","Hawaii","Idaho","Illinois ","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine ","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri ","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico ","New York","North Carolina","North Dakota","Ohio","Oklahoma ","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota ","Tennessee","Texas","Utah","Vermont","Virginia","Washington ","West Virginia","Wisconsin","Wyoming" };
    Spinner spin_state;
    private EditText edtcname;
    private EditText edtfname;
    private EditText edtlname;
    private EditText edtemail;
    private EditText edtphone;
    private EditText edtdob;
    private EditText edtadd1;
    private EditText edtcity;
    private EditText edtzipcode;
    private EditText edtlic;
    private EditText edtnote;
    private TextView txtsubmit,txtaddcancel;
    private LinearLayout linbutt;
    private TextView txtedit;
    ProgressDialog dialogz;
    Eld_api api;
    private String ccode;
    private String cname;
    Preference pref;
    driver_model driverModel;
    private ImageView imgback;
    private TextView txttitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context = this;
        pref=Preference.getInstance(context);
        driverModel= (driver_model) getIntent().getSerializableExtra("drivervalue");
        ccode=pref.getString(Constant.COMPANY_LOGIN_CODE);
        cname=pref.getString(Constant.COMPANY_NAME);
        spin_state = findViewById(R.id.auto_stateaddress);
        txtedit=findViewById(R.id.driver_edit);
        imgback=findViewById(R.id.driver_list_iv_back);
        txttitle=findViewById(R.id.driver_list_tv_title);
        edtcname= findViewById(R.id.edt_compname);
        edtcname.setText(""+cname);
        edtfname= findViewById(R.id.edt_fname);
        edtfname.setText(""+driverModel.fname);
        edtlname= findViewById(R.id.edt_lname);
        edtlname.setText(""+driverModel.lname);
        edtemail= findViewById(R.id.edt_email);
        edtemail.setText(""+driverModel.e_mail);
        edtlic= findViewById(R.id.edt_lic);
        edtlic.setText(""+driverModel.license);
        edtnote= findViewById(R.id.edt_note);
        edtnote.setText(""+driverModel.note);
        edtphone=findViewById(R.id.edt_phonenumber);
        edtphone.addTextChangedListener(new PhoneNumberFormattingTextWatcher(edtphone));
        edtphone.setText(""+driverModel.phone);
        edtdob=findViewById(R.id.edt_dob);
        edtdob.setText(""+driverModel.dob);
        edtadd1= findViewById(R.id.edt_address1);
        edtadd1.setText(""+driverModel.address);
        edtcity= findViewById(R.id.edt_city);
        edtcity.setText(""+driverModel.city);
        edtzipcode= findViewById(R.id.edt_zipcode);
        edtzipcode.setText(""+driverModel.zip);
        txtsubmit=findViewById(R.id.add_submit);
        txtaddcancel=findViewById(R.id.add_cancel);
        linbutt=findViewById(R.id.linbutton);

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                states);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin_state.setAdapter(ad);

       edtcname.setEnabled(false);
       edtfname.setEnabled(false);
       edtlname.setEnabled(false);
       edtemail.setEnabled(false);
       edtphone.setEnabled(false);
       edtdob.setEnabled(false);
       edtadd1.setEnabled(false);
       edtcity.setEnabled(false);
       edtzipcode.setEnabled(false);
        edtlic.setEnabled(false);
        edtnote.setEnabled(false);
        linbutt.setVisibility(View.INVISIBLE);
        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtcname.setEnabled(true);
                edtfname.setEnabled(true);
                edtlname.setEnabled(true);
                edtemail.setEnabled(true);
                edtphone.setEnabled(true);
                edtdob.setEnabled(true);
                edtadd1.setEnabled(true);
                edtcity.setEnabled(true);
                edtzipcode.setEnabled(true);
                edtlic.setEnabled(true);
                edtnote.setEnabled(true);
                linbutt.setVisibility(View.VISIBLE);
                txtedit.setVisibility(View.INVISIBLE);
                txttitle.setText(" EDIT DRIVER ");
            }
        });
        txtaddcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        viewdriver.this,
                        Driverlist_home.class);
                startActivity(mIntent);

                finish();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        viewdriver.this,
                        Driverlist_home.class);
                startActivity(mIntent);

                finish();
            }
        });
        txtsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_fname="";
                String str_lname="";
                String str_email="";
                String str_lic="";
                String str_note="";
                String str_dob="";
                String str_state="";
                String str_address="";
                String str_city="";
                String str_zipcode="";

                String str_phonenumebr="";






                str_fname=edtfname.getText().toString().trim();
                str_lname=edtlname.getText().toString().trim();
                str_email=edtemail.getText().toString().trim();
                str_lic=edtlic.getText().toString().trim();
                str_note=edtnote.getText().toString().trim();
                str_dob=edtdob.getText().toString().trim();
                str_address=edtadd1.getText().toString().trim();
                str_city=edtcity.getText().toString().trim();
                str_zipcode=edtzipcode.getText().toString().trim();
                str_state=spin_state.getSelectedItem().toString();

                str_phonenumebr=edtphone.getText().toString().trim();

                if(str_fname!=null&& str_fname.length()>0
                        &&str_lname!=null &&str_lname.length()>0 &&str_email!=null&&str_lic!=null&&str_lic!=null&&str_address!=null&&str_address
                        !=null &&str_phonenumebr!=null&&str_phonenumebr.length()>0)
                {
                    dialogz = new ProgressDialog(context,
                            AlertDialog.THEME_HOLO_LIGHT);
                    dialogz.setMessage("Please wait...");
                    dialogz.setCancelable(false);
                    dialogz.show();
                    Log.e("regggg","value calling");
                    saveredriver(str_fname,str_lname,str_phonenumebr,str_email,str_dob,
                            str_address,str_city,str_zipcode,str_state,str_phonenumebr,str_lic,str_note);
                }else{
                    Log.e("regggg","empty calling");
                    Toast.makeText(context,"Please enter all fields",Toast.LENGTH_SHORT).show();
                }

            }


        });

    }

    private void saveredriver( String str_fname, String str_lname, String str_phonenumebr, String str_email, String str_dob, String str_address, String str_city, String str_zipcode, String str_state, String str_phonenumebr1, String str_lic, String str_note) {
        if (OnlineCheck.isOnline(context)) {

            api = ApiServiceGenerator.createService(Eld_api.class);


            Call<JsonObject> call = null;
//Log.e("staffid",""+driverModel.staff_id);
            call = api.editregdriver(""+driverModel.staff_id,
                    "" + str_fname, "" + str_lname, "" + str_email,
                    ""+str_phonenumebr,""+str_address,""+str_city,""+str_state,
                    ""+str_zipcode,""+str_lic,""+str_dob,str_note,""+ccode,"30");


            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("Responsestring", response.body().toString());
                    cancelprogresssdialogz();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String jsonresponse = response.body().toString();
                            //Log.e("jsonresponse", jsonresponse.toString());
                            try {
                                JSONObject resp = new JSONObject(jsonresponse);
                                if (response != null) {

                                    if (resp.has("status")) {
                                        Log.e("statusz",""+resp.getString("status"));
                                        String stat = resp.getString("status");
                                        Log.e("stat",""+stat);
                                        if(stat.contentEquals("false"))
                                        {
                                            String msg = resp.getString("error");
                                            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                        }else{
                                            String msg = resp.getString("msg");
                                            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                            Intent mIntent = new Intent(
                                                    viewdriver.this,
                                                    Driverlist_home.class);
                                            startActivity(mIntent);
                                            finish();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("eeeeeee", e.toString());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Responsestringerrr", t.toString());
                    Log.e("Respccc", call.toString());
                    cancelprogresssdialogz();
                }
            });

        }

    }
    private  void cancelprogresssdialogz() {

        try {
            if ((dialogz != null) && dialogz.isShowing()) {
                dialogz.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialogz = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(
                viewdriver.this,
                Driverlist_home.class);
        startActivity(mIntent);

        finish();
    }
}
