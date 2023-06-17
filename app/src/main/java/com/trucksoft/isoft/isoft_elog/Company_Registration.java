package com.trucksoft.isoft.isoft_elog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Company_Registration extends AppCompatActivity {
    String[] states = { "California","Alabama","Alaska","Arizona","Arkansas","Colorado","Connecticut ","Delaware","District of Columbia","Florida","Georgia","Hawaii","Idaho","Illinois ","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine ","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri ","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico ","New York","North Carolina","North Dakota","Ohio","Oklahoma ","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota ","Tennessee","Texas","Utah","Vermont","Virginia","Washington ","West Virginia","Wisconsin","Wyoming" };
    Spinner spin_state;
    Spinner spin_state2;
    Spinner spinstatebill;
    Spinner spinstateship;

    private EditText edtcname;
    private EditText edtfname;
    private EditText edtlname;
    private EditText edtemail;
    private EditText edtpassword;
    private EditText edtcpassword;
    private EditText edtdotnumber;
    private EditText edtadd1,edtadd2;
    private EditText edtcity;
    private EditText edtzipcode;
    private EditText edtconutry;
    private EditText edtbphone;
    private EditText edtphone;
    private TextView txtsubmit;
    private Context context;
    Eld_api api;
    CheckBox chkbill;
    CheckBox chkship;
    private LinearLayout linbill;
    private LinearLayout linship;

    private EditText edtphonebill;
    private EditText edtaddressbill1;
    private EditText edtaddressbill2;
    private EditText edtcitybill;
    private EditText edtstatebill;
    private EditText edtzipcodebill;
    private EditText edtcountrybill;

    private EditText edtphoneship;
    private EditText edtaddressship1;
    private EditText edtaddressship2;
    private EditText edtcityship;
    private EditText edtstateship;
    private EditText edtzipcodeship;
    private EditText edtcountryship;
    ProgressDialog dialogz;
    private ImageView imgback;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyreg);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context=this;
        imgback=findViewById(R.id.driver_list_iv_back);
         spin_state = findViewById(R.id.auto_state);
         linbill=findViewById(R.id.lin_bill);
         linship=findViewById(R.id.lin_ship);
        spin_state2= findViewById(R.id.auto_stateaddress);
        edtcname= findViewById(R.id.edt_compname);
        edtfname= findViewById(R.id.edt_fname);
        edtlname= findViewById(R.id.edt_lname);
        edtemail= findViewById(R.id.edt_email);
        edtpassword= findViewById(R.id.edt_paswowrd);
        edtcpassword= findViewById(R.id.edt_cpaswowrd);
        edtdotnumber= findViewById(R.id.edt_dotnumber);
        edtadd1= findViewById(R.id.edt_address1);
        edtadd2= findViewById(R.id.edt_address2);
        edtcity= findViewById(R.id.edt_city);
        edtzipcode= findViewById(R.id.edt_zipcode);
        edtconutry= findViewById(R.id.edt_country);
        edtbphone= findViewById(R.id.edt_bphonenumber);
        edtbphone.addTextChangedListener(new PhoneNumberFormattingTextWatcher(edtbphone));
        edtphone=findViewById(R.id.edt_phonenumber);
        edtphone.addTextChangedListener(new PhoneNumberFormattingTextWatcher(edtphone));
        txtsubmit=findViewById(R.id.add_submit);
        chkbill=findViewById(R.id.checkBox_bill);
        chkship=findViewById(R.id.checkBox_ship);


       edtphonebill=findViewById(R.id.edt_phonenumberbill);
       edtaddressbill1=findViewById(R.id.edt_addressbill);
       edtaddressbill2=findViewById(R.id.edt_addressbill2);
       edtcitybill=findViewById(R.id.edt_citybill);
       spinstatebill=findViewById(R.id.auto_statebill);
       spinstateship=findViewById(R.id.auto_stateship);
       edtzipcodebill=findViewById(R.id.edt_zipcodebill);
       edtcountrybill=findViewById(R.id.edt_countrybill);

       edtphoneship=findViewById(R.id.edt_phonenumbership);
       edtaddressship1=findViewById(R.id.edt_addressship);
       edtaddressship2=findViewById(R.id.edt_addressship2);
       edtcityship=findViewById(R.id.edt_cityship);
        edtzipcodeship=findViewById(R.id.edt_zipcodeship);
        edtcountryship=findViewById(R.id.edt_countryship);

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
        spin_state2.setAdapter(ad);
        spinstatebill.setAdapter(ad);
        spinstateship.setAdapter(ad);
        chkbill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                                                   if(chkbill.isChecked())
                                                   {
                                                       String str_address1=""+edtadd1.getText().toString();
                                                       String str_address2=""+edtadd2.getText().toString();
                                                       String str_city=""+edtcity.getText().toString();
                                                       String str_zipcode=""+edtzipcode.getText().toString();
                                                       String str_phone=edtbphone.getText().toString();
                                                       String str_country=""+edtconutry.getText().toString();
                                                       int val=spin_state2.getSelectedItemPosition();
                                                       Log.e("bill","checked");
                                                       linbill.setVisibility(View.VISIBLE);

                                                       edtphonebill.setText(""+str_phone);
                                                       edtaddressbill1.setText(""+str_address1);
                                                       edtaddressbill2.setText(""+str_address2);
                                                       edtcitybill.setText(""+str_city);
                                                       edtzipcodebill.setText(""+str_zipcode);
                                                       edtcountrybill.setText(""+str_country);
                                                       spinstatebill.setSelection(val);
                                                   }else{
                                                       Log.e("bill","unchecked");
                                                       linbill.setVisibility(View.GONE);
                                                       edtphonebill.setText("");
                                                       edtaddressbill1.setText("");
                                                       edtaddressbill2.setText("");
                                                       edtcitybill.setText("");
                                                       edtzipcodebill.setText("");
                                                       edtcountrybill.setText("");

                                                   }
                                               }
                                           }
        );

        chkship.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {


                                                   if(chkship.isChecked())
                                                   {
                                                       String str_address1=""+edtadd1.getText().toString();
                                                       String str_address2=""+edtadd2.getText().toString();
                                                       String str_city=""+edtcity.getText().toString();
                                                       String str_zipcode=""+edtzipcode.getText().toString();
                                                       String str_phone=edtbphone.getText().toString();
                                                       String str_country=""+edtconutry.getText().toString();
                                                       int val=spin_state2.getSelectedItemPosition();
                                                       linship.setVisibility(View.VISIBLE);
                                                       edtphoneship.setText(""+str_phone);
                                                       edtaddressship1.setText(""+str_address1);
                                                       edtaddressship2.setText(""+str_address2);
                                                       edtcityship.setText(""+str_city);
                                                       edtzipcodeship.setText(""+str_zipcode);
                                                       edtcountryship.setText(""+str_country);
                                                      // Log.e("bill","checked"+edtadd1.getText().toString());
                                                       spinstateship.setSelection(val);
                                                   }else{
                                                       Log.e("bill","unchecked");
                                                       linship.setVisibility(View.GONE);
                                                       edtphoneship.setText("");
                                                       edtaddressship1.setText("");
                                                       edtaddressship2.setText("");
                                                       edtcityship.setText("");

                                                       edtzipcodeship.setText("");
                                                       edtcountryship.setText("");
                                                   }
                                               }
                                           }
        );
        txtsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_cname="";
                String str_fname="";
                String str_lname="";
                String str_email="";
                String str_password="";
                String str_cpassword="";
                String str_dot="";
                String str_state1="";
                String str_address1="";
                String str_address2="";
                String str_city="";
                String str_zipcode="";
                String str_state2="";
                String str_country="";
                String str_phonenumebr="";
                String str_businessphone="";

                //bill
                String str_addressbill1="";
                String str_addressbill2="";
                String str_citybill="";
                String str_zipcodebill="";
                String str_statebill="";
                String str_countrybill="";
                String str_phonenumebrbill="";
                //bill
                String str_addressship1="";
                String str_addressbillship2="";
                String str_cityship="";
                String str_zipcodeship="";
                String str_stateship="";
                String str_countryship="";
                String str_phonenumebrship="";


                str_cname=edtcname.getText().toString().trim();
                str_fname=edtfname.getText().toString().trim();
                str_lname=edtlname.getText().toString().trim();
                str_email=edtemail.getText().toString().trim();
                str_password=edtpassword.getText().toString().trim();
                str_cpassword=edtcpassword.getText().toString().trim();
                str_dot=edtdotnumber.getText().toString().trim();
                str_address1=edtadd1.getText().toString().trim();
                str_address2=edtadd2.getText().toString().trim();
                str_city=edtcity.getText().toString().trim();
                str_zipcode=edtzipcode.getText().toString().trim();
                str_state1=spin_state.getSelectedItem().toString();
                str_state2=spin_state2.getSelectedItem().toString();
                str_country=edtconutry.getText().toString().trim();
                str_phonenumebr=edtphone.getText().toString().trim();
                str_businessphone=edtbphone.getText().toString().trim();
                //bill
                str_addressbill1=""+edtaddressbill1.getText().toString().trim();
                str_addressbill2=""+edtaddressbill2.getText().toString().trim();
                str_citybill=""+edtcitybill.getText().toString().trim();
                str_zipcodebill=""+edtzipcodebill.getText().toString().trim();
                str_statebill=""+spinstatebill.getSelectedItem().toString();
                str_countrybill=""+edtcountrybill.getText().toString().trim();
                str_phonenumebrbill=""+edtphonebill.getText().toString().trim();
                //bill
                str_addressship1=""+edtaddressship1.getText().toString().trim();
                str_addressbillship2=""+edtaddressship2.getText().toString().trim();
                str_cityship=""+edtcityship.getText().toString().trim();
                str_zipcodeship=""+edtzipcodeship.getText().toString().trim();
                str_stateship=""+spinstateship.getSelectedItem().toString();
                str_countryship=""+edtbphone.getText().toString().trim();
                str_phonenumebrship=""+edtbphone.getText().toString().trim();
                if(str_cname!=null && str_cname.length()>0 && str_fname!=null&& str_fname.length()>0
                        &&str_lname!=null &&str_lname.length()>0 &&str_email!=null&&str_password!=null&&str_cpassword!=null&&str_dot!=null&&str_state1
                        !=null&&str_address1!=null&&str_address2!=null&&str_city!=null&&str_zipcode!=null&&
                 str_state2!=null&&str_country!=null&&str_phonenumebr!=null)
                {
                    dialogz = new ProgressDialog(context,
                            AlertDialog.THEME_HOLO_LIGHT);
                    dialogz.setMessage("Please wait...");
                    dialogz.setCancelable(false);
                    dialogz.show();
                    Log.e("regggg","value calling");
                    saveregistration(str_cname,str_fname,str_lname,str_phonenumebr,str_email,str_password,str_state1,
                            str_dot,str_address1,str_address2,str_city,str_state2,str_country,str_zipcode,str_phonenumebr
                    , str_addressbill1,
                            str_addressbill2,
                            str_citybill,
                            str_zipcodebill,
                            str_statebill,
                            str_countrybill,
                            str_phonenumebrbill,str_addressship1,
                            str_addressbillship2,
                            str_cityship,
                            str_zipcodeship,
                            str_stateship,
                            str_countryship,
                            str_phonenumebrship);
                }else{
                    Log.e("regggg","empty calling");
                    Toast.makeText(context,"Please enter all fields",Toast.LENGTH_SHORT).show();
                }

            }


        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Company_Registration.this,
                        Company_Login.class);
                startActivity(mIntent);

                finish();
            }
        });
    }
    private void saveregistration(String str_cname, String str_fname, String str_lname, String str_phonenumebr, String str_email, String str_password, String str_state1, String str_dot, String str_address1, String str_address2, String str_city, String str_state2, String str_country, String str_zipcode, String strPhonenumebr, String str_addressbill1, String str_addressbill2, String str_citybill, String str_zipcodebill, String str_statebill, String str_countrybill, String str_phonenumebrbill, String str_addressship1, String str_addressbillship2, String str_cityship, String str_zipcodeship, String str_stateship, String str_countryship, String str_phonenumebrship)
    {
        if (OnlineCheck.isOnline(context)) {

            api = ApiServiceGenerator.createService(Eld_api.class);


            Call<JsonObject> call = null;

            call = api.saveregcompany(str_cname,
                    "" + str_fname, "" + str_lname, "" + str_phonenumebr,
                    "" + str_email, "" + str_password, str_state1,  str_dot,""+str_address1,""+str_address2,""+str_city,""+str_state2,""+str_country,""+str_zipcode,str_phonenumebr,""+str_addressbill1,""+str_addressbill2,""+str_citybill,
                    ""+str_statebill,""+str_countrybill,""+str_zipcodebill,""+str_phonenumebrbill
                    ,""+str_addressship1,""+str_addressbillship2,""+str_cityship,
                    ""+str_stateship,""+str_countryship,""+str_zipcodeship,""+str_phonenumebrship);


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
                                                    Company_Registration.this,
                                                    Company_Login.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(
                Company_Registration.this,
                Company_Login.class);
        startActivity(mIntent);

        finish();
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

}
