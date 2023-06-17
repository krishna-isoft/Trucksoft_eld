package com.trucksoft.isoft.isoft_elog.fuel_receipt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.driverchecklist.DispatchServiceGenerator;
import com.isoft.trucksoft_elog.driverchecklist.Dispatchline_decoder;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Upload_fuelbill extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener{
    Context contexts;
    Preference pref;
    Dialog dialogprivacy;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ProgressDialog dialog;
    Eld_api api;
    RecyclerView recyclerView;
    private TextView taddbill;
    private static byte[] img;
    ProgressDialog progressdlog;
    private String vin;
    private String driverid;
    private String strdate;

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String straddress="";
    private String strstate="";
    Fuel_receyadap adapter;
    List<Receipt_model> movies;
    private ImageView imgback;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuelreceipt);
        contexts=this;
        movies=new ArrayList<>();
        pref=Preference.getInstance(contexts);
        vin=pref.getString(Constant.VIN_NUMBER);
        driverid=pref.getString(Constant.DRIVER_ID);
        taddbill=findViewById(R.id.addbill);
        pref.putString(Constant.LAST_INDEX,"0");
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        SimpleDateFormat formatdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        strdate= formatdatetime.format(new Date());
        taddbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeipicture();
            }
        });
        imgback=findViewById(R.id.driver_list_iv_back);
        recyclerView =findViewById(R.id.driver_list_listView);



        adapter = new Fuel_receyadap(contexts, movies);


        adapter.setLoadMoreListener(new Fuel_receyadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
        recyclerView.addItemDecoration(new Dispatchline_decoder(15));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = DispatchServiceGenerator.createService(Eld_api.class, contexts);

        load(0,"","");
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(contexts, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
                    finish();
//                }
            }
        });



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Log.e("call","........."+"fcm..............");
                // checking for type intent filter
                if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
                    // new push notification is received
                    //handlePushNotification(intent);
                    String msg = intent.getStringExtra("message");
                    //  Log.e("msg","........."+msg);
                    if (msg != null && msg.length() > 0 && !msg.contentEquals("null")) {

                        if (msg.contentEquals("app_logout")) {
                            //Log.e("callv","#"+msg);
                            applogout();
                        }



                    }
                }
            }
        };
    }

    private void load(int index,String strfromdate,String strtodate){

        //Log.e("kd",""+strfromdate);
       // Log.e("kdzz",""+strtodate);
        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        String page="0";
//        Log.e("cval","driver_trips.php?driver="+pref.getString(Constant.DRIVER_ID)+"&from="+strfromdate+"&to="
//                +strtodate+"&page="+page+"&cc="+pref.getString(Constant.COMPANY_CODE));
        try{
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
//Log.e("urlvalz","list_fuel_bill.php?driver="+pref.getString(Constant.DRIVER_ID)+"&page="+page+"&cc="+pref.getString(Constant.COMPANY_CODE)
//);
        Call<List<Receipt_model>> call = api.getFuellist(pref.getString(Constant.DRIVER_ID),""+page,pref.getString(Constant.COMPANY_CODE),
                ""+lat,""+lon,straddress,strstate,"Upload Fuel Bill");

        call.enqueue(new Callback<List<Receipt_model>>() {
            @Override
            public void onResponse(Call<List<Receipt_model>> call, Response<List<Receipt_model>> response) {
              //  Log.e(" Responsev"," "+response.toString());
                //Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                    if(dialog !=null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                      //Log.e(" Responsecqevv","z "+response.body().size());
                    if(response.body() !=null) {
                        movies.addAll(response.body());
                    }else{

                    }

                    //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                    dialog.dismiss();
                    // load(0);
                    // Log.e("ggg"," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Receipt_model>> call, Throwable t) {
                // Log.e("tttt"," Response Error "+t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void loadMore(int index){
//Log.e("callvvv","loadmpre");
        //add loading progress view
        movies.add(new Receipt_model("load"));
        adapter.notifyItemInserted(movies.size()-1);
      //  Log.e("indexc","loadmpre"+index);
        String page=pref.getString(Constant.LAST_INDEX);
        int ab= Integer.parseInt(pref.getString(Constant.LAST_INDEX));
        ab++;
        pref.putString(Constant.LAST_INDEX,""+ab);
        String pagez=pref.getString(Constant.LAST_INDEX);
        //Log.e("urlvalz","list_fuel_bill.php?driver="+pref.getString(Constant.DRIVER_ID)+"&page="+pagez+"&cc="+pref.getString(Constant.COMPANY_CODE)
      //  );
        Call<List<Receipt_model>> call = api.getFuellist(pref.getString(Constant.DRIVER_ID),""+pagez,pref.getString(Constant.COMPANY_CODE),""+lat,""+lon,straddress,strstate,"Upload Fuel Bill");

        call.enqueue(new Callback<List<Receipt_model>>() {
            @Override
            public void onResponse(Call<List<Receipt_model>> call, Response<List<Receipt_model>> response) {
                // Log.e(" Responsekk","z "+response.toString());
                //Log.e(" Responsesskk"," z"+String.valueOf(response.code()));

                if(response.isSuccessful()){
                    //  Log.e(" Responsec","z "+response.body());
                    //remove loading view
                    movies.remove(movies.size()-1);

                    List<Receipt_model> result = response.body();
                    if(result.size()>0){
                        //add loaded data
                        movies.addAll(result);
                    }else{//result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(contexts,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                    //  Log.e(TAG," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Receipt_model>> call, Throwable t) {
                //  Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }






















    private void takeipicture() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(contexts,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                callprivacy();

            }else {
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(Upload_fuelbill.this);
            }
        }else {
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(Upload_fuelbill.this);
        }



    }


    protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {
                    InputStream iStream = getContentResolver().openInputStream(result.getUri());
                    img = getBytes(iStream);
                    Bitmap bitmaph = BitmapFactory.decodeByteArray(img, 0, img.length);
                    // store(bitmaph, driverid);

                    uploadphoto();

                } catch (Exception e) {

                }
                //  Log.e("ddd",""+result.getUri().toString());
                // decodeFile(result.getUri().toString());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void uploadphoto() {

        progressdlog = new ProgressDialog(Upload_fuelbill.this,
                AlertDialog.THEME_HOLO_LIGHT);
        if (OnlineCheck.isOnline(this)) {

            progressdlog = new ProgressDialog(Upload_fuelbill.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            if (OnlineCheck.isOnline(this)) {
                progressdlog = new ProgressDialog(Upload_fuelbill.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                api = ApiServiceGenerator.createService(Eld_api.class);
                byte[] image=img;
                String strPhoto = "";
                if (image != null) {
                    if (image.length > 0) {
                        try {
                            CommonUtil commonUtil = new CommonUtil(contexts);
                            strPhoto = commonUtil.ecode64(image);
                            commonUtil = null;
                        } catch (Exception e) {
                            //Log.e("Image Exception", e.getMessage());
                        }
                    }
                } else {
                    strPhoto = null;
                }



                Call<JsonObject> call=null;

                    call = api.uploadfuelbill(pref.getString(Constant.DRIVER_ID),strPhoto,vin,strdate,straddress);



                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                       // Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {
                            cancelprogresssdialog();
                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();

                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

                                        String status = resp
                                                .getString("status");
                                        if(status !=null && status.contentEquals("1"))
                                        {
                                            String message=resp
                                                    .getString("message");
                                            Toast.makeText(contexts,""+message,Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }
                                    }
                                }catch (Exception e)
                                {

                                }


                            } else {
                              //  Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            cancelprogresssdialog();
                          //  Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        cancelprogresssdialog();
                       // Log.e("imageresponseerrr",""+t.toString());
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
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Log.e("lattttdd", "########" + lat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //pd.dismiss();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
            try {
                 //Log.e("latlat1", "@" + lat);
                if (lat != null) {
                    // Log.e("calling", "" + address);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
                    getAddressFromLocation(latitude, longitude);
                }
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        if (lat != null && lat.length() > 0 && !lat.contentEquals("null")) {

            try {
                //  Log.e("latlat2", "@" + lat);
                if (lat != null) {
                    // Log.e("calling", "" + address);
                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lon);
                    getAddressFromLocation(latitude, longitude);
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.e("calling","onstart");
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {
            return true;
        }
    }
    public String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(contexts, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                if (address.getAddressLine(0) != null && address.getAddressLine(0).length() > 0 && !address.getAddressLine(0).contentEquals("null")) {
                    sb.append(address.getAddressLine(0)).append("\n");


                } else {

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());

                }

                straddress = sb.toString();

              //  Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }



        return straddress;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();


        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent ink = new Intent(contexts, Home_activity.class);
//            startActivity(ink);
//            finish();
//        }else{
            finish();
//        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

        // clearing the notification tray
        Trucksoft_elog_Notify_Utils.clearNotifications(contexts);
    }


    private void applogout()
    {
       // Log.e("called","kk************logout");
        api = ApiServiceGenerator.createService(Eld_api.class);
        String did=pref.getString(Constant.DRIVER_ID);
        String vin=pref.getString(Constant.VIN_NUMBER);
        cancelnotification();

        //  Log.e("url","&did="+did+"&name="+type);
        Call<List<Remark_model>> call = api.applogout(did,""+vin,did,""+pref.getString(Constant.CURRENT_LATITUDE),""+pref.getString(Constant.CURRENT_LOGINGITUDE));

        call.enqueue(new Callback<List<Remark_model>>() {
            @Override
            public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                if(response.isSuccessful()){
//Log.e("msg","logout successfully");

                }else{


                }
            }

            @Override
            public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());

//setdistancealert(mils);
            }
        });
        //Log.e("called","************logout1");
        pref = Preference.getInstance(contexts);
        //Log.e("login", "fail");
        pref.putString(Constant.LOGIN_CHECK,
                "logged_off");
        pref.putString(Constant.BLUETOOTH_CONNECTED,"0");
        pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
        pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
        pref.putString(Constant.DRIVE_NOTIFICATON, "0");
        pref.putString(Constant.VIN_NUMBER, "");
        pref.putString(Constant.BLUETOOTH_ADDRESS,"");
        pref.putString(Constant.BLUETOOTH_NAME,"");
        pref.putString(Constant.NETWORK_TYPE,Constant.CELLULAR);


        try {
           // finish();
            finishAffinity();
            Intent mIntent = new Intent(contexts,
                    Loginactivitynew.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("EXIT", true);
            startActivity(mIntent);
        }catch (Exception e)
        {
           // Log.e("serviceer","@"+e.toString());
        }




        // ((Activity) context). finishAndRemoveTask();
    }
    public void cancelnotification() {


        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }


    private void callprivacy()
    {
        if (dialogprivacy != null) {
            if (dialogprivacy.isShowing()) {
                dialogprivacy.dismiss();
            }
        }
        String val2=" \n  * When uploading images. \n \n " +
                " * Capture Images";
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.file_privacy, null);
        final TextView btnsubmitz = dialogView.findViewById(R.id.tsubmit);
        final TextView tcancel = dialogView.findViewById(R.id.tcancel);
        final TextView lsub = dialogView.findViewById(R.id.lsub);
        lsub.setText(""+val2);
        dialogprivacy = new Dialog(contexts, R.style.DialogTheme);
        dialogprivacy.setCancelable(false);
        dialogprivacy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogprivacy.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogprivacy.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogprivacy.getWindow().setAttributes(lp);
        dialogprivacy.show();
        btnsubmitz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogprivacy.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    insertDummyContactWrapper();
                }

            }
        });
        tcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogprivacy.dismiss();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(Manifest.permission.CAMERA);
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//            permissionsNeeded.add("Read phone state");
//        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
//            permissionsNeeded.add("Send sms");
//        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
//            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
//        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
//            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean bool=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    bool= false;
            }
            bool=true;
        }
        return bool;
    }
}
