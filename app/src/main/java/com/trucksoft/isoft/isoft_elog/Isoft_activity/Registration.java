package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.SearchstateAdapter;
import com.isoft.trucksoft_elog.Isoft_adapter.state_model;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import org.apache.http.Header;

/**
 * Created by isoft on 29/5/17.
 */

public class Registration extends Activity implements  View.OnFocusChangeListener{
    private EditText edt_fname;
    private EditText edt_lname;
    private EditText edt_email;
    private EditText edt_license;
    private EditText edt_phone;
    private TextView tstate;
    private EditText edt_city;
    private String state_id="";
    private String state_name="";
    private Button btnSubmit;
    ProgressDialog dialog;
    Eld_api api;
    private String msg=null;
    private String str_name=null;
    private String str_dmsg=null;
    private Context context;
    int progress=0;
    ProgressDialog progressBar;
    private  String msgdetail;
    SearchstateAdapter mAdaptertrailer;
    private Handler progressBarHandler = new Handler();

    public final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.reg_new);
        //setContentView(R.layout.registration_new);
        context=this;
        setContentView(R.layout.registration_demo);
        edt_fname=findViewById(R.id.stname);
        edt_lname=findViewById(R.id.stlname);
        tstate=findViewById(R.id.add_state);
        edt_city=findViewById(R.id.tcity);

        edt_email=findViewById(R.id.stmail);
        edt_phone=findViewById(R.id.stmob);
        btnSubmit=findViewById(R.id.login);
        edt_license=findViewById(R.id.txt_license) ;
        edt_email.addTextChangedListener(watch);
       // edt_license.addTextChangedListener(watch);
        edt_email.setOnFocusChangeListener(this);

        tstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getstatedetails();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=edt_fname.getText().toString().trim();
                String lastname=edt_lname.getText().toString().trim();
                String pemail=edt_email.getText().toString().trim();

                String smobile=edt_phone.getText().toString().trim();
                String license=edt_license.getText().toString().trim();
                String city=edt_city.getText().toString().trim();

                String pe=null;
                String sn=null;

                if (pemail.length() > 0) {
                    if (pemail.length() >= 10) {
                        if (pemail.length() <= 50) {
                            if (pemail.matches(EMAIL_PATTERN)) {
                                pe = null;
                            } else {
                                pe = "Enter a valid e-mail id e.g. : xyz123@gmail.com";

                            }
                        } else {
                            pe = "Maximum of " + 50
                                    + " characters are allowed";
                        }
                    } else {
                        pe = "Enter minimum of " + 10
                                + " characters";
                    }

                }else
                {
                    pe = "Enter a e-mail id";
                }

                edt_email.setError(pe);

                if (fname.length() > 0) {
                    if (fname.length() >= 2) {
                        if (fname.length() <= 30) {
                            if (checkname(fname)) {
                                sn = null;
                            } else {
                                sn = "Enter a valid name e.g. : Andrew";
                            }
                        } else {
                            sn = "Maximum of " + 30
                                    + " characters are allowed";
                        }
                    } else {
                        sn = "Enter minimum of " + 2
                                + " characters";
                    }
                } else {
                    sn = "Enter a driver name";
                }
                edt_fname.setError(sn);
String ln=null;
                if (lastname.length() > 0) {
                    if (lastname.length() >= 1) {
                        if (lastname.length() <= 30) {
                            if (checkname(lastname)) {
                                ln = null;
                            } else {
                                ln = "Enter a valid name e.g. : Andrew";
                            }
                        } else {
                            ln = "Maximum of " + 30
                                    + " characters are allowed";
                        }
                    } else {
                        ln = "Enter minimum of " + 1
                                + " characters";
                    }
                } else {
                    ln = null;
                }
                edt_lname.setError(ln);
                String sp=null;

//                if(spass.length()>0)
//                {
//                    sp=null;
//                }else
//                {
//                    sp="Enter a password";
//                    edt_pwd.setError("Enter a password");
//                }



String sid=null;

                if (license.length() > 0) {
                    if (license.length() >= 2) {
                        if (license.length() <= 12) {
                            if (checkalphanumeric(license)) {
                                sid = null;
                            } else {
                                sid = "Enter a valid Driver License e.g. : H234525";
                            }
                        } else {
                            sid = "Maximum of " + 12
                                    + " characters are allowed";
                        }
                    } else {
                        sid = "Enter minimum of " + 2
                                + " characters";
                    }
                } else {
                    sid = "Enter a Driver License";
                }
                edt_license.setError(sid);
String mob=null;
                if(smobile.length()>0)
                {
                    if (smobile.length() >= 2) {
                        if (smobile.length() <= 15) {
                            mob=null;
                        } else {
                            mob = "Maximum of " + 15
                                    + " characters are allowed";
                        }
                    } else {
                        mob = "Enter minimum of " + 2
                                + " characters";
                    }
                }else
                {
                    mob="Enter a Mobile Number";

                }
                edt_phone.setError(mob);






//if(isPhoneNumberValid(mob,"us"))
//{
//    edt_phone.setError(null);
//    Log.e("mnumber","valid");
//    mob="";
//}else{
//    mob="Invalid Mobile Number";
//    edt_phone.setError(mob);
//    Log.e("mnumberz","failes");
//}



                if(pe==null && msg==null && str_name ==null & sn==null && sp==null && sid==null && ln==null && mob==null) {
                   // savedetails(sname,lastname, pemail, spass,did,smobile,vinz);
                    if(tstate.getText().toString().trim().contentEquals("Select State"))
                    {
                       Toast.makeText(context,"Please select State",Toast.LENGTH_SHORT).show();
                    }else {
                        savedriver(fname, lastname, pemail, smobile, license, city);
                    }
                }
            }
        });
    }
    private void savedriver(String fname, String lastname, String semail,String mobile,String license,String city)
    {
        dialog = new ProgressDialog(Registration.this,
                AlertDialog.THEME_HOLO_LIGHT);

        if (OnlineCheck.isOnline(this)) {
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            api = ApiServiceGenerator.createService(Eld_api.class);


            Call<JsonObject> call = null;

//                    Log.e("paramslist", "driver="+pref.getString(Constant.DRIVER_ID)
//                    +"&vin="+pref.getString(Constant.VIN_NUMBER)+"&cc="+pref.getString(Constant.COMPANY_CODE)
//                    +"&logid="+strondutylogid+"&value=1");
            call = api.savedemodrivers(fname,lastname, semail, mobile, license,state_name,city);


            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                     Log.e("Responsestring", response.body().toString());
                    //Toast.makeText()
                    if (response.isSuccessful()) {
                        cancelprogresssdialog();
                        if (response.body() != null) {
                            String jsonresponse = response.body().toString();
                            Log.e("jsonresponse", ""+jsonresponse);
                            try {
                                JSONObject resp = new JSONObject(jsonresponse);
                                Log.e("resp", ""+resp);
                                if (resp != null) {

                                        String status = resp
                                                .getString("status");
                                      if(status !=null && status.contentEquals("1"))
                                       {
                                    String message = resp
                                            .getString("message");
                                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                           Intent mIntent = new Intent(
                                                   Registration.this,
                                                   Loginactivitynew.class);

                                           startActivity(mIntent);
                                    finish();
                                     }else if(status !=null && status.contentEquals("2"))
                                      {
                                          String message = resp
                                                  .getString("message");
                                          Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                      }
                                }
                            } catch (Exception e) {

                            }


                        } else {
                            // Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        cancelprogresssdialog();
                        //   Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    cancelprogresssdialog();
                    // Log.e("imageresponseerrr",""+t.toString());
                }
            });

        }

    }
    private void cancelprogresssdialog() {

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
//    private void savedetails(String sname, String lastname, String semail, String spass,String did, String mobile, String vinz) {
//        dialog = new ProgressDialog(Registration.this,
//                AlertDialog.THEME_HOLO_LIGHT);
//
//        if (OnlineCheck.isOnline(this)) {
//            dialog.setMessage("Please wait...");
//            dialog.setCancelable(false);
//            dialog.show();
//            WebServices.elog_reg_latest(this, sname,lastname, semail, spass, did,mobile,vinz,
//                    new JsonHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              String responseString, Throwable throwable) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers,
//                                    responseString, throwable);
//
//                            dialog.dismiss();
//                            // CommonMethod.showMsg(getActivity(), ""+
//                            // responseString);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              Throwable throwable, JSONArray errorResponse) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers, throwable,
//                                    errorResponse);
//                            // Log.e("sds2h", ""+errorResponse.toString());
//                            dialog.dismiss();
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers,
//                                              Throwable throwable, JSONObject errorResponse) {
//                            // TODO Auto-generated method stub
//                            super.onFailure(statusCode, headers, throwable,
//                                    errorResponse);
//                            // Log.e("sds2", ""+errorResponse);
//                            dialog.dismiss();
//
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              JSONArray response) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, response);
//                            dialog.dismiss();
//
//                            if (response != null) {
//                                // Log.e("resp1",""+response.toString());
//                            }
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              JSONObject response) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, response);
//                            dialog.dismiss();
//
//
//                            try {
//
//
//                                if (response != null) {
//
//
//                                      Log.e("resp2","@"+response.toString());
//                                    String status = response
//                                            .getString("status");
//
//                                    if (status.equalsIgnoreCase("1")) {
//                                        String email=response
//                                                .getString("email");
//                                        String otpid=response
//                                                .getString("otp");
//                                        msgdetail = "Welcome "
//                                                + "\n";
//                                        msgdetail = msgdetail
//                                                + "Your OTP          : "+"\n"+ otpid + "\n";
//
//                                        BackgroundMail bm = new BackgroundMail(
//                                                context);
//                                        bm.setGmailUserName("isoftelog@gmail.com ");
//                                        bm.setGmailPassword("elog@123");
//                                        bm.setMailTo(email);
//                                        bm.setFormSubject("Elog Account  - "+otpid+" is your verification code for secure access");
//                                        bm.setFormBody(msgdetail);
//                                        bm.send();
//
//                                        progressBar = new ProgressDialog(context,
//                                                AlertDialog.THEME_HOLO_LIGHT);
//
//
//                                        progressBar.setMessage("Please wait...");
//                                        progressBar.setCancelable(false);
//                                        progressBar.show();
//
//
//                                        new Thread(new Runnable() {
//
//                                            public void run() {
//                                                long timerEnd = System.currentTimeMillis() + 15 * 1000;
//
//                                                while (timerEnd >  System.currentTimeMillis()) {
//
//                                                    progress = 15 - (int) (timerEnd - System.currentTimeMillis()) / 1000;
//                                                    // Update the progress bar
//
//                                                    progressBarHandler.post(new Runnable() {
//                                                        public void run() {
//                                                            progressBar.setProgress(progress);
//                                                        }
//                                                    });
//
//                                                    try {
//                                                        Thread.sleep(500);
//                                                    } catch (InterruptedException e) {
//                                                        Log.w("App","Progress thread cannot sleep");
//                                                    }
//                                                }
//                                                progressBarHandler.post(new Runnable() {
//                                                    public void run() {
//                                                        progressBar.dismiss();
//                                                        Intent mIntent = new Intent(
//                                                                Registration.this,
//                                                                Email_verificatin.class);
//
//                                                        startActivity(mIntent);
//
//                                                        finish();
//                                                    }
//                                                });
//                                            }
//                                        }).start();
//
//                                    }else
//                                    {
//                                        String message = response
//                                                .getString("message");
//                                        Toast.makeText(getApplicationContext(),
//                                                message, Toast.LENGTH_SHORT)
//                                                .show();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers,
//                                              String responseString) {
//                            // TODO Auto-generated method stub
//                            super.onSuccess(statusCode, headers, responseString);
//                            dialog.dismiss();
//
//                        }
//
//                    });
//        }
//
//    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String pemail = edt_email.getText().toString().trim();
        String sname=edt_fname.getText().toString().trim();
        switch (v.getId()) {
            case R.id.stmail:
                if (pemail.length() > 0) {
                    if (pemail.length() >= 10) {
                        if (pemail.length() <= 50) {
                            if (pemail.matches(EMAIL_PATTERN)) {
                            } else {
                                msg = "Enter a valid e-mail id e.g. : xyz123@gmail.com";
                            }
                        } else {
                            msg = "Maximum of " + 50
                                    + " characters are allowed";
                        }
                    } else {
                        msg = "Enter minimum of " + 10
                                + " characters";
                    }
                    edt_email.setError(msg);
                }
                break;


            case R.id.stname:
                if (sname.length() > 0) {
                    if (sname.length() >=2) {
                        if (sname.length() <= 30) {
                            if (checkname(sname)) {
                            } else {
                                str_name = null;
                                edt_fname.setError("Enter a valid name e.g. : Andrew");

                            }
                        } else {
                            str_name = null;
                            edt_fname.setError("Maximum of " + 30
                                    + " characters are allowed");
                        }
                    } else {
                        str_name = null;
                        edt_fname.setError("Enter minimum of "
                                + 2 + " characters");
                    }
                }
                break;
        }
    }








    TextWatcher watch=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence arg0, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int start, int before, int count) {
            String pemail = edt_email.getText().toString().trim();
            String s_name=edt_fname.getText().toString().trim();
          //  String did=edt_license.getText().toString().trim();
            if (edt_email.getText().hashCode() == arg0.hashCode()) {
                if (pemail.length() > 0) {
                    if (pemail.length() <= 50) {
                        msg = null;
                    } else {
                        msg = "Maximum of " + 50
                                + " characters are allowed";
                    }
                } else {
                    msg = null;
                }
                edt_email.setError(msg);
            }




            if (edt_fname.getText().hashCode() == arg0.hashCode()) {
                if (s_name.length() > 0) {
                    if (s_name.length() <= 30) {
                        str_name = null;
                    } else {
                        str_name = "Maximum of " + 30
                                + " characters are allowed";
                    }
                } else {
                    str_name = null;
                }
                edt_fname.setError(str_name);
            }


//            if (edt_license.getText().hashCode() == arg0.hashCode()) {
//                if (s_name.length() > 0) {
//                    if (s_name.length() <= 12) {
//                        str_dmsg = null;
//                    } else {
//                        str_dmsg = "Maximum of " + 12
//                                + " characters are allowed";
//                    }
//                } else {
//                    str_dmsg = null;
//                }
//                edt_license.setError(str_dmsg);
//            }






        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };
    public boolean checkname(String str) {
        if (str.matches("[a-zA-Z._ ]+$")
                && (!(str.substring(0, 1).matches("[. ]+$")))) {
            return true;
        }
        return false;
    }
    public boolean checkalphanumeric(String str) {
        if (str.matches("[a-zA-Z0-9 ]+$") && (!(str.matches("[0-9]+$")))) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(
                Registration.this,
                Loginactivitynew.class);

        startActivity(mIntent);

        finish();
    }

    private void getstatedetails() {

        dialog = new ProgressDialog(Registration.this,
                AlertDialog.THEME_HOLO_LIGHT);

        if (OnlineCheck.isOnline(this)) {
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();


            api = ApiServiceGenerator.createService(Eld_api.class);

            Call<List<state_model>> call = api.getstatedetails();

            call.enqueue(new Callback<List<state_model>>() {
                @Override
                public void onResponse(Call<List<state_model>> call, Response<List<state_model>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        //  movies = response.body();
                        setstatedialog(response.body());
                    } else {

                        //  getvehicle();
                        dialog.dismiss();
                        // Log.e("ss"," Response "+String.valueOf(response.code()));
                    }

                }

                @Override
                public void onFailure(Call<List<state_model>> call, Throwable t) {
                    //Log.e("dd"," Response Error "+t.getMessage());

                    dialog.dismiss();
                }
            });
        }
    }
    private void setstatedialog(final List<state_model> alist)
    {
        View view = View.inflate(context, R.layout.select_state, null);
        final ListView listView=view.findViewById(R.id.status_select_listview);
        final ImageView imgclose=view.findViewById(R.id.imgclose);
        final EditText inputSearch =  view.findViewById(R.id.inputSearch);
        inputSearch.setHint("Enter State Name");
        final Dialog dialogaleryes = new Dialog(context,R.style.DialogTheme);
        dialogaleryes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogaleryes.setContentView(view);
        dialogaleryes.show();

        mAdaptertrailer= new SearchstateAdapter(alist,getApplicationContext());

        listView.setAdapter(mAdaptertrailer);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogaleryes.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewk, int position, long id) {
                // View v1 = (View) view.getParent().;


                Log.e("namekkcczz","&"+mAdaptertrailer.getItem(position));
                state_model mark =mAdaptertrailer.getItem(position);
                Log.e("namekkcc","&"+mark.state_name);
                state_name=""+mark.state_name;
                tstate.setText(""+mark.state_name);
                state_id=mark.id;
                dialogaleryes.dismiss();
//Log.e("demoassignname","@"+demoassignname.getText().toString());
              //  savetrailerdetail(tid.getText().toString(), txtName.getText().toString());

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString=inputSearch.getText().toString();
                List<state_model> dalist=new ArrayList<>();
                int textLength=searchString.length();
                for(int i=0;i<alist.size();i++) {
                    state_model vvdalist=new state_model();
                    vvdalist=alist.get(i);
                    String rname = vvdalist.state_name.toString();
                    // System.out.println("recipe name "+rname);
                    if (textLength <= rname.length()) {
                        if(searchString.equalsIgnoreCase(rname.substring(0,textLength))) {
                            dalist.add(alist.get(i));

                            mAdaptertrailer= new SearchstateAdapter(dalist,getApplicationContext());

                            listView.setAdapter(mAdaptertrailer);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


}

