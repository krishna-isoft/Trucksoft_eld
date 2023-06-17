package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Serviceticket_elog  extends AppCompatActivity {
    private TextView txtback;
    Font_manager font_manager;
    Context context;
    private EditText edtcname;
    private EditText edtuname;
    private EditText edtcnumber;
    ImageView imgticket;
    private EditText edtmessage;
    private Button btnsend,btncancel;
    private Bitmap bitmap;
    private static byte[] img;

    private String strcname;
    private String struname;
    private String cnumber;
    private String stremail;
    private String strmessage;
    ProgressDialog progressDialog;
    private Paint paint;
    Preference pref;
    Bitmap bitmapticket;
    File fileShare;
    private String driverid;
    private EditText edtmail;
    ProgressDialog dialogzz;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int NOTIFY_ME_ID_DISPATCH = 1340;
    Eld_api api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_ticket);
        context=this;
        pref=Preference.getInstance(context);
        driverid=pref.getString(Constant.DRIVER_ID);
        txtback=findViewById(R.id.txtback);
        edtcname=findViewById(R.id.txt_cl);
String cnam=pref.getString(Constant.COMPANY_NAME);
        edtcname.setText(""+cnam);
        edtuname=findViewById(R.id.txt_yname);
        String name=pref.getString(Constant.DRIVER_NAME);
        edtuname.setText(""+name);
        edtcnumber=findViewById(R.id.txt_cnumber);
        String num=pref.getString(Constant.DRIVER_PHONE);
        edtcnumber.setText(""+num);
        imgticket=findViewById(R.id.txt_image);
        edtmail=findViewById(R.id.txt_email);
        String emai=pref.getString(Constant.COMPANYMAIL);
        edtmail.setText(""+emai);
        edtmessage=findViewById(R.id.txt_message);
        btnsend=findViewById(R.id.btn_send);
        btncancel=findViewById(R.id.btn_cancel);
      //  edtcname.setFocusable(true);
        txtback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intenttt = new Intent(context, Serviceticket_home.class);
//                startActivity(intenttt);
                finish();
            }
        });
        imgticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeipicture();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttt = new Intent(context, Serviceticket_home.class);
                startActivity(intenttt);
                finish();
            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strcname=edtcname.getText().toString().trim();
                stremail=edtmail.getText().toString().trim();
                cnumber=edtcnumber.getText().toString().trim();
                struname=edtuname.getText().toString().trim();
                strmessage=edtmessage.getText().toString().trim();
                SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String dfg=formatsec.format(new Date());

                SimpleDateFormat formatz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                String myDatez = formatz.format(new Date());
                // Log.e("dcx", "-" + myDatez);
                //date spilit
                String dta = null;
                StringTokenizer skdte;
                if (myDatez.contains("-")) {
                    //  Log.e("d1", "-");
                    skdte = new StringTokenizer(myDatez, "-");
                    String styr = skdte.nextToken();
                    String stmnth = skdte.nextToken();
                    String stdte = skdte.nextToken();

                    dta = stdte + "/" + getMonth(Integer.parseInt(stmnth)) + "/" + styr;
                }
if((strcname !=null && strcname.length()>0 && !strcname.contentEquals("null"))||
        (struname !=null && struname.length()>0 && !struname.contentEquals("null"))||
        (cnumber !=null && cnumber.length()>0 && !cnumber.contentEquals("null"))||
        (stremail !=null && stremail.length()>0 && !stremail.contentEquals("null"))||
        (strmessage !=null && strmessage.length()>0 && !strmessage.contentEquals("null"))|| (img !=null)
        ) {
    sendmail(strcname, struname, cnumber, stremail, strmessage, dta);
}else{
                    Toast.makeText(context,"Please enter data",Toast.LENGTH_SHORT).show();;
}
//Serviceticket_elog.myAsyncTask myWebFetch = new Serviceticket_elog.myAsyncTask();
//                myWebFetch.execute();
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
                            Log.e("callv","#"+msg);
                            applogout();
                        }



                    }
                }
            }
        };
    }
    private void takeipicture() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(Serviceticket_elog.this);

    }

    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.e("requestCode",""+requestCode);
        // Log.e("resultCode",""+resultCode);
        //Log.e("data",""+data.toString());
        // handle result of CropImageActivity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {
                    InputStream iStream = getContentResolver().openInputStream(result.getUri());
                    img = getBytes(iStream);

                    try {
                        if (result.getUri() != null) {
                            bitmapticket = MediaStore.Images.Media.getBitmap(context.getContentResolver(), result.getUri());


                            store(bitmapticket, driverid);
                        }
                    } catch (Exception e) {
                        //handle exception
                    }
                    (imgticket).setImageURI(result.getUri());


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



    public String getMonth(int month) {
        String monthtext = null;

        switch (month) {
            case 1:
                monthtext = "Jan";
                break;
            case 2:
                monthtext = "Feb";
                break;
            case 3:
                monthtext = "Mar";
                break;
            case 4:
                monthtext = "Apr";
                break;
            case 5:
                monthtext = "May";
                break;
            case 6:
                monthtext = "Jun";
                break;
            case 7:
                monthtext = "Jul";
                break;
            case 8:
                monthtext = "Aug";
                break;
            case 9:
                monthtext = "Sep";
                break;
            case 10:
                monthtext = "Oct";
                break;
            case 11:
                monthtext = "Nov";
                break;
            case 12:
                monthtext = "Dec";
                break;
        }
        return monthtext;
    }
    private void store(Bitmap imageToSave, String fileName) {

//    File mFile = new File(Environment.getExternalStoragePublicDirectory(
//            Environment.DIRECTORY_PICTURES)       + "/FuelBill");

        File mFile = new File(getFilesDir()+ File.separator + "/FuelBill");
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        fileShare = new File(getFilesDir() + File.separator + "/FuelBill", fileName + ".jpg");
        if (fileShare.exists()) {
            fileShare.delete();
        }

        //trying to show up in gallery
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    fileShare.getAbsolutePath(), fileShare.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(fileShare);
            // Bitmap last = ProcessingBitmap(imageToSave);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //SaveImage(MainActivity.this, imageToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sendmail(final String strcnamed, final String strunamed, final String cnumberd, final String stremaild, final String strmessaged, String ctime) {



		String strPhoto = "";
		if (img != null) {
			if (img.length > 0) {
				try {
					CommonUtil commonUtil = new CommonUtil(context);
					strPhoto = commonUtil.ecode64(img);
					commonUtil = null;
				} catch (Exception e) {
					Log.e("Image Exception", e.getMessage());
				}
			}
		} else {
			strPhoto = null;
		}
        dialogzz = new ProgressDialog(Serviceticket_elog.this,
                AlertDialog.THEME_HOLO_LIGHT);
        if (OnlineCheck.isOnline(this)) {

            dialogzz.setMessage("Please wait...");
            dialogzz.setCancelable(false);
            dialogzz.show();

            api = ApiServiceGenerator.createService(Eld_api.class);
            //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
            Call<JsonObject> call = api.sendserviceticketmail("" + pref.getString(Constant.DRIVER_ID)
                    , "" + pref.getString(Constant.VIN_NUMBER), "" + pref.getString(Constant.LICENSE_NUMBER), ""+pref.getString(Constant.DRIVER_NAME),
                    ""+pref.getString(Constant.COMPANY_CODE), "" + pref.getString(Constant.ELD_KEY),
                    "" + strcnamed, ""+strunamed,""+cnumberd,""+stremaild,""+strmessage,""+ctime,""+strPhoto);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                    Log.e("Responsestring", responsez.body().toString());
                    //Toast.makeText()
                    cancelprogresssdialog();
                    if (responsez.isSuccessful()) {

                        if (responsez.body() != null) {
                            String jsonresponse = responsez.body().toString();
                            //Log.e("jsonresponse", jsonresponse.toString());
                            try {
                                JSONObject response = new JSONObject(jsonresponse);
                                try {

                                    Log.e("responselog", "" + response.toString());

                                    edtmessage.setText("");
                                    strmessage="";

                                    if (response != null) {
                                        // Log.e("resp", ""+response.toString());
                                        Toast.makeText(
                                                        context, "Service ticket send successfully", Toast.LENGTH_LONG)
                                                .show();
                                        Intent intenttt = new Intent(context, Serviceticket_home.class);
                                        startActivity(intenttt);
                                        finish();

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                //Log.e("Exceptionwwwwwwww", e.toString());
                            }


                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    //Log.e("Exceptionwttttttt", t.toString());
                     cancelprogresssdialog();
                }
            });
        }
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
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

        // clearing the notification tray
        Trucksoft_elog_Notify_Utils.clearNotifications(context);
    }


    private void applogout()
    {
        Log.e("called","kk************logout");
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
        Log.e("called","************logout1");
        pref = Preference.getInstance(context);
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
            //finish();
            finishAffinity();
            Intent mIntent = new Intent(context,
                    Loginactivitynew.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("EXIT", true);
            startActivity(mIntent);
        }catch (Exception e)
        {
            Log.e("serviceer","@"+e.toString());
        }




        // ((Activity) context). finishAndRemoveTask();
    }
    public void cancelnotification() {


        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }
    private void cancelprogresssdialog()
    {
        try {
            if ((dialogzz != null) && dialogzz.isShowing()) {
                dialogzz.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialogzz = null;
        }

    }
}
