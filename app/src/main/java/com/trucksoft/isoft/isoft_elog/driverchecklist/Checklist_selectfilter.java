package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isoft on 4/1/17.
 */

public class Checklist_selectfilter extends Activity implements View.OnClickListener {

    TextView mTextView, mTextView2;
    ListView mListView;
    ProgressDialog dialog;
    ArrayList<String> status_array;
    ArrayList<String> status_array_id = new ArrayList<String>();
    ArrayList<String> status_array_mainenance = new ArrayList<String>();
    String tag = "client", value, title;

    SharedPreferences.Editor editor;
    String MyPREFERENCES = "filter_status";
    SharedPreferences sharedpreferences;
    int count = 0;
    ArrayList<String> status_active;
    private EditText inputSearch;
    private ChecklistCustomerActivity customadapter;
    private String detailid;
    private int mStatusCode;
    private Context context;
    Eld_api api;
    Preference pref;
    Dialog dialogupdateapp;
    Font_manager font_manager;
    private TextView txtaddtrailor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.status_select_screen);
context=this;
        findViewById();
pref=Preference.getInstance(context);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                this.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        font_manager=new Font_manager();

        Intent mIntent = getIntent();
        tag = mIntent.getStringExtra("tag");
        value = mIntent.getStringExtra("value");
        title = mIntent.getStringExtra("title");
        if(mIntent.hasExtra("detailid")) {
            detailid = mIntent.getStringExtra("detailid");
        }
        mTextView2.setText(title);

        selectFilter(tag);

        // selectFilter("shipper");

        mTextView.setOnClickListener(this);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        if(tag.contains("truck_d_checklist")||tag.contains("client")|| tag.contentEquals("shipper_from") || tag.contentEquals("trailer_d_checklist"))
        {
           // inputSearch.setVisibility(View.VISIBLE);
            if(tag.contentEquals("shipper_from"))
            {
                inputSearch.setHint("Select Shipper From");
            }else if(tag.contains("truck_d_checklist"))
            {
                inputSearch.setHint("Select Truck");
            }else if(tag.contains("client"))
            {
                inputSearch.setHint("Select Client");
            }
            else
            {
                inputSearch.setHint("Select Trailer");
            }
        }else
        {
            inputSearch.setVisibility(View.GONE);
        }
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //SelectFilter.this.AddDispatchCustomerActivity.getFilter().filter(cs);
                //customadapter.getFilter().filter(cs);
                String searchString=inputSearch.getText().toString();
                int textLength=searchString.length();
                ArrayList< String> searchResults = new ArrayList< String>();
                ArrayList< String> searchResultsid = new ArrayList< String>();
                ArrayList< String> searchResultmaint = new ArrayList< String>();


                for(int i=0;i<status_array.size();i++)
                {
                    String rname = status_array.get(i).toString();
                    // Log.e("rname", ""+rname.toString());
                    // System.out.println("recipe name "+rname);
                    if(textLength<=rname.length())
                    {
                        //compare the String in EditText with Names in the ArrayList
                        if(searchString.equalsIgnoreCase(rname.substring(0,textLength)))
                        {
                            searchResults.add(status_array.get(i));
                            searchResultsid.add(status_array_id.get(i));

                            if(status_array_mainenance !=null && status_array_mainenance.size()>0) {
                                searchResultmaint.add(status_array_mainenance.get(i));
                            }
                            // Log.e("searchResults", ""+searchResults.toString());
                            // Log.e("searchResults", ""+searchResultsid.toString());

                            customadapter=new ChecklistCustomerActivity(Checklist_selectfilter.this, tag,
                                    count, searchResults, searchResultsid,value,searchResultmaint);
                            mListView.setAdapter(customadapter);

                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        txtaddtrailor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calladdtrailor();
            }
        });

    }

    private void findViewById() {
        // TODO Auto-generated method stub
        txtaddtrailor=findViewById(R.id.txtaddtrailor);
        mListView =  findViewById(R.id.status_select_listview);
        mTextView = findViewById(R.id.textView2_header_status_select);
        mTextView2 =  findViewById(R.id.textView1_header_status_select);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.textView2_header_status_select:
                finish();
                break;

            case R.id.imageView_back_status_select:

                back_click();

                finish();
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();

        // overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        back_click();
    }

    private void back_click() {
        // TODO Auto-generated method stub
        if (tag.equalsIgnoreCase("client")) {
            editor.putString("client", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("shipper_from")) {
            editor.putString("shipper_from", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("shipper_to")) {
            editor.putString("shipper_to", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("status")) {
            editor.putString("status", value);
            editor.commit();

        } else if (tag.equalsIgnoreCase("dispatch_f_ticket")) {
            editor.putString("dispatch_f_ticket", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("shipper_f_ticket")) {
            editor.putString("shipper_f_ticket", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("consignee_f_ticket")) {
            editor.putString("consignee_f_ticket", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("driver_f_ticket")) {
            editor.putString("driver_f_ticket", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("truck_f_ticket")
                || tag.equalsIgnoreCase("truck_d_checklist")) {
            editor.putString("truck_f_ticket", value);
            editor.commit();
        } else if (tag.equalsIgnoreCase("trailer_d_checklist")) {
            editor.putString("trailer_d_checklist", value);
            editor.commit();
        }

    }

//    private void selectFilter(String str) {
//
//        dialog = new ProgressDialog(Checklist_selectfilter.this,
//                AlertDialog.THEME_HOLO_LIGHT);
//
//        if (OnlineCheck.isOnline(this)) {
//            dialog.setMessage("Please wait...");
//            dialog.setCancelable(false);
//            dialog.show();
//            WebServices.dp_select_filter(this,str, new JsonHttpResponseHandler() {
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      String responseString, Throwable throwable) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, responseString,
//                            throwable);
//
//                    dialog.dismiss();
//                    // CommonMethod.showMsg(getActivity(), ""+ responseString);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      Throwable throwable, JSONArray errorResponse) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable,
//                            errorResponse);
//
//                    dialog.dismiss();
//                    // CommonMethod.showMsg(getActivity(), ""+ errorResponse);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      Throwable throwable, JSONObject errorResponse) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable,
//                            errorResponse);
//
//                    dialog.dismiss();
//
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      JSONArray response) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, response);
//                    dialog.dismiss();
//
//                    //Log.e("responsk", ""+response.toString());
//                    if (response != null) {
//                        filterList(response);
//                    }
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      JSONObject response) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, response);
//                    dialog.dismiss();
//                    errorMessage(response);
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      String responseString) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, responseString);
//                    dialog.dismiss();
//                }
//
//            });
//        }
//
//    }




    private void selectFilter(String str) {
        //dialog = new ProgressDialog(LoginActivity.this,
        //       AlertDialog.THEME_HOLO_LIGHT);
        api = DispatchServiceGenerator.createService(Eld_api.class,context);
        dialog= new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        // dialog.setTitle("Title of progress dialog.");
        dialog.setMessage("Loading.........");

        // Set the progress dialog background color
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B53391")));

        dialog.setIndeterminate(false);
        if (OnlineCheck.isOnline(this)) {
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

//Log.e("did",""+pref.getString(Constant.DRIVER_ID));
            Log.e("drlic",""+pref.getString(Constant.LICENSE_NUMBER));
            Log.e("ccode",""+pref.getString(Constant.COMPANY_CODE));

            RequestBody typebody = RequestBody.create(MediaType.parse("text/plain"), "driver");
            RequestBody didbody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString(Constant.DRIVER_ID));
            RequestBody licensebody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString(Constant.LICENSE_NUMBER));
            RequestBody compbody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString(Constant.COMPANY_CODE));

            Call<ResponseBody> call = api.gettrailerdetails(typebody,didbody,licensebody,compbody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        mStatusCode = response.code();
                        Log.e("mStatusCoded", "mStatusCode==" + mStatusCode);
                        if (mStatusCode == Constant.SUCEESSRESPONSECODE) {
                            dialog.dismiss();
                            // JSONObject result = new JSONObject(response.body().string());
                            try {
                                JSONArray resultarray = new JSONArray(response.body().string());
                                if (resultarray.length() > 0) {
                                    filterList(resultarray);
                                }
                            }catch (JSONException e)
                            {
                                Log.e("qw2","@"+e.toString());
                            }
                            catch (Exception e)
                            {
                                Log.e("qw23","@"+e.toString());
                            }

                        } else if (mStatusCode == Constant.FAILURERESPONSECODE) {
                            dialog.dismiss();
//                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
//                                    "Invalid username or password!", Snackbar.LENGTH_LONG);
//                            snackbar.show();

                        } else {
                            dialog.dismiss();
                            if (mStatusCode == Constant.INTERNALERRORRESPONSECODE) {

//                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
//                                        response.errorBody().string(), Snackbar.LENGTH_LONG);
//                                snackbar.show();
                            }
                        }
                    }catch (Exception e) {
                        Log.e("qw2","@"+e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (t instanceof SocketTimeoutException) {
                        Log.v("SocketTimeOut", "SocketTimeOutError");
                        Toast.makeText(context, getString(R.string.socket_timeout)
                                , Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }

    }



    public void filterList(JSONArray response) {
        //Log.e("taggggg","@"+tag);
        try {

            status_active=new ArrayList<String>();
            status_array = new ArrayList<String>();

            if (tag.equalsIgnoreCase("shipper_f_ticket")) {
                status_array.add("New Shipper");
                status_array_id.add("");

            } else if (tag.equalsIgnoreCase("consignee_f_ticket")) {
                status_array.add("New Consignee");
                status_array_id.add("");
            } else if (tag.equalsIgnoreCase("truck_f_ticket")) {
                status_array.add("New Truck");
                status_array_id.add("");
                status_array_mainenance.add("0");
            }

            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject mResultJsonObject = response.getJSONObject(i);

                    String status = mResultJsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        String name;
                        String id;
                        String status_st = null;
                        if (tag.equalsIgnoreCase("status")) {
                            name = mResultJsonObject.getString("status_name");
                            id = mResultJsonObject.getString("id");
                            status_st = mResultJsonObject.getString("status_off");
                            if (value.equalsIgnoreCase(id)) {
                                count = i;
                            }
                        }else if (tag.equalsIgnoreCase("client")) {
                            name = mResultJsonObject.getString("client_name");
                            id = mResultJsonObject.getString("id");
                            if (value.equalsIgnoreCase(name)) {
                                count = i;
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        }

                        else if (tag.equalsIgnoreCase("driver_f_ticket")) {
                            name = mResultJsonObject.getString("driver_name");
                            id = mResultJsonObject.getString("id");
                            if (value.equalsIgnoreCase(name)) {
                                count = i;
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        } else if (tag.equalsIgnoreCase("truck_f_ticket")) {
                            name = mResultJsonObject.getString("Truck_name");

                            id = mResultJsonObject.getString("id");

                            if (value.equalsIgnoreCase(name)) {
                                count = i + 1;
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        } else if (tag.equalsIgnoreCase("truck_d_checklist")) {

                            name = mResultJsonObject.getString("Truck_name");
                            id = mResultJsonObject.getString("id");
                            if (value.equalsIgnoreCase(name)) {
                                count = i;
                            }
                            if(mResultJsonObject.has("repair")) {
                                String rpair = mResultJsonObject.getString("repair");
                                status_array_mainenance.add(rpair);
                            }else{
                                status_array_mainenance.add("0");
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        } else if (tag.equalsIgnoreCase("dispatch_f_ticket")) {
                            id = mResultJsonObject.getString("id");
                            name = id;
                            if (value.equalsIgnoreCase(name)) {
                                count = i;
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        } else if (tag.equalsIgnoreCase("shipper_f_ticket")
                                || tag.equalsIgnoreCase("consignee_f_ticket")) {
                            name = mResultJsonObject.getString("client_name");
                            id = mResultJsonObject.getString("id");
                            if (value.equalsIgnoreCase(name)) {
                                count = i + 1;
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        } else if (tag.equalsIgnoreCase("trailer_d_checklist")) {
                            name = mResultJsonObject.getString("Trailer_name");
                            id = mResultJsonObject.getString("id");

                            if (value.equalsIgnoreCase(name)) {
                                count = i;
                            }
                            if(mResultJsonObject.has("repair")) {
                                String rpair = mResultJsonObject.getString("repair");
                                status_array_mainenance.add(rpair);
                            }else{
                                status_array_mainenance.add("0");
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        } else {
                            name = mResultJsonObject.getString("client_name");
                            id = mResultJsonObject.getString("id");
                            if (value.equalsIgnoreCase(name)) {
                                count = i;
                            }
                            //status_st = mResultJsonObject.getString("status_off");
                        }
                        //Log.e("status_st", "gfgk");
                        status_array_id.add(id);
                        status_array.add(name);

                        if(status_st !=null && status_st.length()>0)
                        {
                            status_st.trim();
                            //Log.e("status_st", "gk"+status_st);
                            status_active.add(status_st);
                        }
                    }

                }

                if (tag.equalsIgnoreCase("client")) {
                    if (value.equalsIgnoreCase("Select Client")) {
                        editor.putString("client", status_array.get(0));
                        editor.putString("client_id", status_array_id.get(0));
                        editor.commit();
                    }
                }  else if (tag.equalsIgnoreCase("client")) {
                    if (value.equalsIgnoreCase("Select Client")) {
                        editor.putString("client", status_array.get(0));
                        editor.putString("client_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                }

                else if (tag.equalsIgnoreCase("shipper_from")) {
                    if (value.equalsIgnoreCase("Select Shipper From")) {
                        editor.putString("shipper_from", status_array.get(0));
                        editor.putString("shipper_from_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("shipper_to")) {
                    if (value.equalsIgnoreCase("Select Shipper To")) {
                        editor.putString("shipper_to", status_array.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("dispatch_f_ticket")) {
                    if (value.equalsIgnoreCase("Select Dispatch")) {
                        editor.putString("dispatch_f_ticket",
                                status_array.get(0));
                        editor.putString("dispatch_f_ticket_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("shipper_f_ticket")) {
                    if (value.equalsIgnoreCase("Select Shipper From")) {
                        editor.putString("shipper_f_ticket",
                                status_array.get(0));
                        editor.putString("shipper_f_ticket_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("consignee_f_ticket")) {
                    if (value.equalsIgnoreCase("Select Consignee")) {
                        editor.putString("consignee_f_ticket",
                                status_array.get(0));
                        editor.putString("consignee_f_ticket_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("driver_f_ticket")) {
                    if (value.equalsIgnoreCase("Select Driver")) {
                        editor.putString("driver_f_ticket", status_array.get(0));
                        editor.putString("driver_f_ticket_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("truck_f_ticket")
                        || tag.equalsIgnoreCase("truck_d_checklist")) {
                    if (value.equalsIgnoreCase("Select Truck")) {
                        editor.putString("truck_f_ticket", status_array.get(0));
                        editor.putString("truck_f_ticket_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                } else if (tag.equalsIgnoreCase("status")) {
                    editor.putString("shipper_to", value);
                    editor.commit();
                    editor.putString("detailid", detailid);
                    editor.commit();

                } else if (tag.equalsIgnoreCase("trailer_d_checklist")) {
                    if (value.equalsIgnoreCase("Select Trailer")) {
                        editor.putString("trailer_d_checklist",
                                status_array.get(0));
                        editor.putString("trailer_d_checklist_id",
                                status_array_id.get(0));
                        editor.commit();
                    }
                }

            }
            //	Log.e("status_active", ""+status_active.toString());
            if(status_active.size()>0)
            {

                //Log.e("status_ 1", ""+status_array_mainenance.toString());

                customadapter=new ChecklistCustomerActivity(this, tag,
                        count, status_array, status_array_id,value,status_active,status_array_mainenance);
                mListView.setAdapter(customadapter);
            }else
            {
               // Log.e("status_ 2", ""+status_array_mainenance.toString());
                customadapter=new ChecklistCustomerActivity(this, tag,
                        count, status_array, status_array_id,value,status_array_mainenance);
                mListView.setAdapter(customadapter);
            }

        }

        catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void errorMessage(JSONObject response) {
        try {

            if (response != null) {

                String status = response.getString("status");

                if (status.equalsIgnoreCase("0")) {
                    String message = response.getString("message");
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_SHORT).show();

                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
    private void calladdtrailor() {

        if (dialogupdateapp != null) {
            if (dialogupdateapp.isShowing()) {
                dialogupdateapp.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;

        dialogView= inflater.inflate(R.layout.add_trailor, null);



        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);


        final TextView trailorno = dialogView.findViewById(R.id.t_trailor);
        final TextView licenseno = dialogView.findViewById(R.id.licenseno);
        final TextView tyear = dialogView.findViewById(R.id.tyear);
        final TextView tmake = dialogView.findViewById(R.id.tmake);
        final TextView tmodel = dialogView.findViewById(R.id.tmodel);
        final TextView tname=dialogView.findViewById(R.id.t_name);


        final TextView tclose=dialogView.findViewById(R.id.txtalert);
        dialogupdateapp = new Dialog(context, R.style.DialogTheme);
        dialogupdateapp.setCancelable(false);
        tclose.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        tname.setText(""+pref.getString(Constant.COMPANY_NAME));
        dialogupdateapp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogupdateapp.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogupdateapp.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogupdateapp.getWindow().setAttributes(lp);
        dialogupdateapp.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String trailerno=trailorno.getText().toString();
                String license=licenseno.getText().toString();
                String stryear=tyear.getText().toString();
                String strmake=tmake.getText().toString();
                String strmodel=tmodel.getText().toString();

                if(trailorno.getText().toString() !=null && trailorno.getText().toString().length()>0)
                {

                    savetrailor(trailerno,license,stryear,strmake,strmodel,dialogupdateapp);
                }else{
                    trailorno.setError("Enter the Trailor No");
                }


            }
        });
        tclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogupdateapp.dismiss();
            }
        });


    }


    private void savetrailor(String trailerno,String licenseno,String  year, String make,String model,Dialog dk){

//Log.e("kd",""+datess);
        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(context,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        api = DispatchServiceGenerator.createService(Eld_api.class,context);
       // Call<Trailor_model> call = api.savetrailor(trailerno,licenseno,make,year,model,pref.getString(Constant.DRIVER_ID),pref.getString(Constant.COMPANY_CODE));
        Map<String,String> params = new HashMap<>();
        params.put("cc", pref.getString(Constant.COMPANY_CODE));
        params.put("trailorno", trailerno);
        params.put("licenseplateno",licenseno);
        params.put("year", year);
        params.put("make", make);
        params.put("model", model);
        params.put("did", pref.getString(Constant.DRIVER_ID));
Log.e("params",""+params.toString());
        Call<Trailor_model> call = api.getUserInfoRequest(params);


        call.enqueue(new Callback<Trailor_model>() {
            @Override
            public void onResponse(Call<Trailor_model> call, Response<Trailor_model> response) {
                //Log.e(" Responsev"," "+response.toString());
                //Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                    if(dialog !=null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    if(response.body() !=null) {
                        // movies.addAll(response.body());
                       // Log.e(" Responsecqevv","z "+response.body());

                        Trailor_model tr=new Trailor_model();
                        tr=response.body();
                        if(tr.status.contentEquals("false"))
                        {
                           Toast.makeText(context,""+tr.error_msg,Toast.LENGTH_SHORT).show();
                        }else {
                            dk.dismiss();
                            // Log.e(" message@@","z "+tr.error_msg);
                            //selectFilter(tag);
                            Toast.makeText(context,""+tr.error_msg,Toast.LENGTH_SHORT).show();
                            editor.putString("trailer_d_checklist",
                                    "" + trailerno);
                            editor.putString("trailer_d_checklist_id",
                                    "" + tr.id);
                            editor.commit();
                            finish();
                        }
                    }else{

                    }

                }else{
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<Trailor_model> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}

