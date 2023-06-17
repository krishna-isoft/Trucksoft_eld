package com.trucksoft.isoft.isoft_elog.Multiused;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_activity.Responsemodel;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager_elog;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActhomeDialog_notification {

   static String lat,lon;
    static Preference pref;
    private static final int NOTIFY_ME_ID_DISPATCH=1340;
    public static final int RequestPermissionCode = 1;
//     static Fragment activity=null;
    static ProgressDialog dialogz;
     static String vinnumber;
    static Font_manager_elog font_manager;
    static Chronometer myChronometer;
    static Timer Tk;
    private static int medialevel=0;
    static MediaRecorder mediaRecorder;
    private static String strnorowid;
    String filePath = "";
    private static String filepath = null;
    private static int serverResponseCode = 0;
    private static Context constk;
    private static String breakid=null;
    private static String mFileName;
    static String datetime;
    static Eld_api api;
    private static String driverid;
    static Activity activityfra;
    private  static String   upLoadServerUri = "http://eld.e-logbook.info/elog_app/upload_to_server.php";
    public static void setpopupnotification(final Activity activity, final Context context, String msgg, final String status, final String breakid)
    {
        if(activity !=null) {
            activityfra = activity;
            constk = context;

            font_manager = new Font_manager_elog();
            pref = Preference.getInstance(context);
            pref.putString(Constant.NOTIFICATION_MESSAGE, "");
            pref.putString(Constant.NOTIFICATION_ESTATUS, "");
            pref.putString(Constant.NOTIFICATION_STATUSMK, "");
            pref.putString(Constant.BR_ID, "" + breakid);
            driverid = pref.getString(Constant.DRIVER_ID);
            vinnumber = pref.getString(Constant.VIN_NUMBER);
//        cancelnotification();
            View view = View.inflate(context, R.layout.break_notification, null);
            TextView txtmsg = (TextView) view.findViewById(R.id.txtmsg);
            Button btn_yes = (Button) view.findViewById(R.id.btn_yes);
            Button btn_no = (Button) view.findViewById(R.id.btn_no);
//        String msg=pref.getString(Constant.NOTIFY_MESSAGE);
            if (msgg != null && msgg.length() > 0) {
                txtmsg.setText(Html.fromHtml(msgg));
            } else {
                txtmsg.setText("take break...........");
            }
            //
            final Dialog dialog;
            // dialog = new Dialog(context,R.style.DialogTheme);
            dialog = new Dialog(activity, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            if (dialog != null && dialog.isShowing()) {

            } else {
                dialog.show();
            }

            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            // The absolute width of the available display size in pixels.
            int displayWidth = displayMetrics.widthPixels;
            // The absolute height of the available display size in pixels.
            int displayHeight = displayMetrics.heightPixels;

            // Initialize a new window manager layout parameters
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

            // Copy the alert dialog window attributes to new layout parameter instance
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            // Set alert dialog width equal to screen width 70%
            int dialogWindowWidth = (int) (displayWidth * 0.75f);
            // Set alert dialog height equal to screen height 70%
            // int dialogWindowHeight = (int) (displayHeight * 0.30f);
            layoutParams.width = dialogWindowWidth;
            // layoutParams.height = dialogWindowHeight;

            // Apply the newly created layout parameters to the alert dialog window
            dialog.getWindow().setAttributes(layoutParams);
            btn_yes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    pref.putString(Constant.NOTIFICATION_CLICKSTATUS_VALUE,
                            "0");
                    cancelnotification(context);

                    if (lat == null) {
                        lat = pref.getString(Constant.LATITUDE);
                        lon = pref.getString(Constant.LONGITUDE);
                    }
                    setautobreakaccept(""+status,0,"3",breakid);
                   // setcurrentstatusbreak("" + status, 0, "", breakid, context);

                }
            });

            btn_no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    cancelnotification(context);
                    pref.putString(Constant.NOTIFICATION_CLICKSTATUS_VALUE,
                            "0");
                    if (checkPermission(context)) {
                        dialog.dismiss();
                        medialevel = 0;
                        callrecorddialog(activity, context);
                    } else {
                        requestPermission(activity);
                    }
                }
            });
        }
    }
    public static void cancelnotification(Context context)
    {



        final NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);

    }
    public static boolean checkPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context,
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context,
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
    private static void requestPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new
                    String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
        }
    }

    private static void setautobreakaccept(final String field,final int statusid,final String statuss,final String breakid)
    {




        String did=pref.getString(Constant.DRIVER_ID);
        //Log.e("did",""+did);
        dialogz = new ProgressDialog(constk,
                AlertDialog.THEME_HOLO_LIGHT);
        dialogz.setMessage("Please wait...");
        dialogz.setCancelable(false);
        dialogz.show();
        api = ApiServiceGenerator.createService(Eld_api.class);
        // Log.e("urldd",""+"did="+did+"&vin="+vinnumber+"&fname="+field+"$pc_status="+""+"&statusid="+statusid+"&breakid="+breakid+"&lat="+lat+"&lon="+lon);
        Call<List<Responsemodel>> call = api.getautobreak_eld(did,vinnumber,field,""+statusid,statuss,breakid,lat,lon,"","","","");

        call.enqueue(new Callback<List<Responsemodel>>() {
            @Override
            public void onResponse(Call<List<Responsemodel>> call, Response<List<Responsemodel>> response) {
                if(response.isSuccessful()){
                    dialogz.dismiss();
                    // movies=response.body();
                    //setVal(movies);
                    //Log.e("respkk","success");
                    //settodayval(movies);
                }else{
                    dialogz.dismiss();
                    //  getvehicle();
                    // callautonotif(field,statusid,statuss,breakid);
                    //Log.e("ss"," Response "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Responsemodel>> call, Throwable t) {
                //Log.e("dd"," Response Error "+t.getMessage());
                dialogz.dismiss();
                // getvehicle();
                // callautonotif( field,statusid,statuss,breakid);
            }
        });
    }

    private static void callrecorddialog(final Activity activity,final Context context)
    {
        final Timer T;

        //kt
        View view = View.inflate(context, R.layout.recording_voice, null);
        TextView txtmsg = (TextView) view.findViewById(R.id.txtmsg);
        final  TextView btn_yes = (TextView) view.findViewById(R.id.btn_start);
        final TextView btn_no = (TextView) view.findViewById(R.id.btn_stop);

        btn_yes.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        btn_no.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        myChronometer = (Chronometer)view.findViewById(R.id.chronometer);
        //final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        final Dialog dialog;
        dialog = new Dialog(activity,R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.80f);
        // Set alert dialog height equal to screen height 70%
        // int dialogWindowHeight = (int) (displayHeight * 0.90f);
        layoutParams.width = dialogWindowWidth;
        // layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
        btn_no.setEnabled(false);
        btn_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //dialog.dismiss();//293244859

                long stoptime=60000;
                myChronometer.setBase(SystemClock.elapsedRealtime());
                myChronometer.start();

                btn_no.setEnabled(true);
                btn_yes.setEnabled(false);
                recordvoice();
                Tk=new Timer();

                Tk.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                long elapsedMillis = SystemClock.elapsedRealtime() - myChronometer.getBase();
                                //Log.e("clktime","@"+elapsedMillis);

                                if (elapsedMillis >= 60000)
                                {
                                    if (medialevel == 0)
                                    {
                                        dialog.dismiss();
                                        mediaRecorder.stop();
                                        myChronometer.stop();
                                        strnorowid=pref.getString(Constant.NOTIFICATION_STATUS_ACCEPT);
                                        dialogz = ProgressDialog.show(context, "Uploading", "Please wait...", true);
                                        new ImageUploadTask().execute();
                                    }
                                }
                            }
                        });
                    }
                }, 1000, 1000);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                medialevel=1;
                mediaRecorder.stop();
                myChronometer.stop();
                strnorowid=pref.getString(Constant.NOTIFICATION_STATUS_ACCEPT);
//                if(strnorowid !=null && strnorowid.length()>0 && !strnorowid.contentEquals("null")) {
//                    String ridd = dbUtil.getlastrowide(driverid);
//                    dbUtil.updatebreaktime(ridd, strnorowid);
//                    syncAfterSave();
//                }

                dialogz = ProgressDialog.show(activity, "Uploading",
                        "Please wait...", true);
                new ImageUploadTask().execute();
            }
        });
    }

    //////////////////////////////////////////////////////////////////

    static class ImageUploadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... unsued) {
            {
//uploadFile(filepath);
                //   String fileName = sourceFileUri;
                //String fileName="elog_trucksoft.Wav";
                // Log.e("filename",""+filepath);

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(filepath);

                if (!sourceFile.isFile()) {

                    dialogz.dismiss();

                    //Log.e("uploadFile", "Source File not exist :" + filepath);

                    activityfra.runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("Source File not exist :" + filepath);
                        }
                    });

                    return ""+0;

                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);
                        // Log.e("url",""+url);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("uploaded_file", filepath);
                        //conn.setRequestProperty("bid", ""+breakid);
                        //conn.setRequestProperty("did", ""+pref.getString(Constant.DRIVER_ID));
                        // Log.e("gc","@"+new DataOutputStream(conn.getOutputStream()));
                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                + filepath + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

// create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

// read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        }

// send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        serverResponseCode = conn.getResponseCode();
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (MalformedURLException ex) {

                        dialogz.dismiss();
                        ex.printStackTrace();

                        activityfra.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(constk,
                                        "MalformedURLException", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    } catch (Exception e) {

                        dialogz.dismiss();
                        e.printStackTrace();

                        activityfra.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(constk,
                                        "Got Exception : see logcat ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    return ""+serverResponseCode;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialogz.isShowing())
                    dialogz.dismiss();
                if (sResponse != null) {
                    if(serverResponseCode==0) {
                        Toast.makeText(constk,
                                "message uploaded failed",
                                Toast.LENGTH_SHORT).show();
                    }else
                    { breakid=pref.getString(Constant.BR_ID);
                        updatenotifystatus(breakid);
                        Toast.makeText(constk,
                                "message uploaded successfully",

                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(constk,
                        e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private static void recordvoice()
    {
        SimpleDateFormat formatdatetime= new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        //Log.e("datetime",""+formatdatetime.getTimeZone());
        datetime =formatdatetime.format(new Date());

        PackageManager pmanager = constk.getPackageManager();
        if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            // Set the file location for the audio
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            //mFileName += "/elog_trucksoft.3gp";
            mFileName += "/"+datetime+"_"+driverid+".mp3";
            filepath=mFileName;
            //filepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/01.jpg";
            // Create the recorder
            mediaRecorder = new MediaRecorder();
            // Set the audio format and encoder
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Setup the output location
            mediaRecorder.setOutputFile(mFileName);
            // Start the recording
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        } else { // no mic on device
            Toast.makeText(constk, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
        }
    }

    public static void updatenotifystatus(String vcode) {
        if (OnlineCheck.isOnline(constk)) {
            api = ApiServiceGenerator.createService(Eld_api.class);
            Call<JsonObject> call = api.updatenotify("" + pref.getString(Constant.DRIVER_ID)
                    , "" + pref.getString(Constant.VIN_NUMBER), "" + vcode);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                   // Log.e("Responsestring", responsez.body().toString());
                    //Toast.makeText()
                    if (responsez.isSuccessful()) {


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        }
    }

}
