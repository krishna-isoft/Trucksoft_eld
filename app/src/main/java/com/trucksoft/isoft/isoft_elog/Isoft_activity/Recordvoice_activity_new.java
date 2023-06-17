package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.Record.ViewProxy;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recordvoice_activity_new extends Activity {

    private String filepath = null;
    MediaRecorder mediaRecorder;
    private String strfile="";
    private Context context;

    TextView tslide ;
    TextView txtalert;
    ImageButton audioSendButton ;
    View slideText;
    View recordPanel;
    TextView recordTimeText;
    Preference pref;
    Intent intent;
    private String breakid,datetime,fnam;
    private String mFileName,driverid;
    Font_manager font_manager;
    private String upLoadServerUri = null;
    private Animation animBlinkrec;
    ProgressDialog progressdlog;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    private boolean boolvoicerecord=true;
    Eld_api api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordvoice_main);
        context=this;
        pref=Preference.getInstance(context);
        driverid=pref.getString(Constant.DRIVER_ID);
        animBlinkrec = AnimationUtils.loadAnimation(context,
                R.anim.blink_new);
        intent=getIntent();
        if(intent.hasExtra("breakid")) {
            breakid = intent.getStringExtra("breakid");
        }
       // Log.e("rvoice","call record");
       // Log.e("breakid",""+breakid);
        font_manager=new Font_manager();
         tslide =findViewById(R.id.tslide);
         txtalert=findViewById(R.id.txtalert);
        tslide.startAnimation(animBlinkrec);
         audioSendButton = findViewById(R.id.chat_audio_send_button);
         slideText =findViewById(R.id.slideText);
         recordPanel = findViewById(R.id.record_panel);
        recordTimeText = findViewById(R.id.recording_time_text);


        SimpleDateFormat formatdatetime= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        datetime =formatdatetime.format(new Date());
        upLoadServerUri = "http://eld.e-logbook.info/elog_app/upload_to_server.php";






        Calendar now = Calendar.getInstance();
        fnam=now.get(Calendar.HOUR_OF_DAY)+""
                +now.get(Calendar.MINUTE) +""+ now.get(Calendar.SECOND);

        txtalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        audioSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = dp(30);
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;
                    // startRecording();
                    startrecord();
                    audioSendButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Log.e("val"," up cancel");
                    startedDraggingX = -1;
                    // stoprecord();
                    sendrecord();
                    // stopRecording(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    if (x < -distCanMove) {
                        stoprecord();
                        Log.e("val"," move 0");
                        // stopRecording(false);
                    }
                    x = x + ViewProxy.getX(audioSendButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        params.leftMargin = dp(30) + (int) dist;
                        slideText.setLayoutParams(params);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }
                        ViewProxy.setAlpha(slideText, alpha);
                    }
                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                            + dp(30)) {
                        if (startedDraggingX == -1) {
                            startedDraggingX = x;
                            distCanMove = (recordPanel.getMeasuredWidth()
                                    - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                            if (distCanMove <= 0) {
                                distCanMove = dp(80);
                            } else if (distCanMove > dp(80)) {
                                distCanMove = dp(80);
                            }
                        }
                    }
                    if (params.leftMargin > dp(30)) {
                        params.leftMargin = dp(30);
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });





    }

    private void uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(filepath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody did = RequestBody.create(MediaType.parse("text/plain"), "" + pref.getString(Constant.DRIVER_ID));
        RequestBody msgid = RequestBody.create(MediaType.parse("text/plain"), "" + pref.getString(Constant.BREAK_MSG_ID));
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), "" );
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), "" );
        RequestBody testbreak = RequestBody.create(MediaType.parse("text/plain"), "breakk");

        api = ApiServiceGenerator.createService(Eld_api.class);
       // Eld_api getResponse = AppConfig.getRetrofit().create(Eld_api.class);
        Call call = api.uploadFile(fileToUpload, filename, did, msgid, state, address, testbreak);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("responseaudio", "success");
                boolvoicerecord=true;
                cancelprogresssdialog();
                pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
//                ServerResponse serverResponse = response.body();
//                if (serverResponse != null) {
//                    if (serverResponse.getSuccess()) {
//                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    assert serverResponse != null;
//                    Log.v("Response", serverResponse.toString());
//                }
                // progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                boolvoicerecord=true;
                cancelprogresssdialog();
                pref.putString(Constant.BREAK_ALERT_DISPLAY, "taken");
                Log.e("responseaudioer", "fail" + t.toString());
            }
        });
    }

    private void recordvoice()
    {
        SimpleDateFormat formatdatetime= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //Log.e("datetime",""+formatdatetime.getTimeZone());
        datetime =formatdatetime.format(new Date());

        PackageManager pmanager = this.getPackageManager();
        if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            // Set the file location for the audio
            //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

            final File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "E-logbook"+ File.separator);


            if (!mFile.exists()) {
                mFile.mkdirs();
            }
            mFileName =Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "E-logbook"+File.separator+datetime+fnam+"_"+driverid+".mp3";

            Log.e("path",""+Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + "E-logbook"+File.separator+datetime+fnam+"_"+driverid+".mp3");

            strfile=datetime+fnam+"_"+driverid+".mp3";




            //mFileName += "/elog_trucksoft.3gp";
            // mFileName += "/"+datetime+"_"+driverid+".mp3";
            filepath=mFileName;
            //filepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/01.jpg";
            // Create the recorder
            mediaRecorder = new MediaRecorder();
            // Set the audio format and encoder
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Setup the output location
            mediaRecorder.setOutputFile(mFileName);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // Start the recording
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        } else { // no mic on device
            Toast.makeText(this, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
        }
    }
    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }
    private void startrecord() {
        // TODO Auto-generated method stub

        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
       MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();
        recordvoice();
    }
    private void sendrecord()
    {

        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
            }

        } catch (Exception e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
        try {



            progressdlog = ProgressDialog.show(Recordvoice_activity_new.this, "Uploading",
                    "Please wait...", true);
            // new ImageUploadTask().execute();
            if(boolvoicerecord) {
                boolvoicerecord=false;
                uploadFile();
            }
        } catch (Exception e) {
            //Log.e("errrrr", "@" + e.toString());
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void stoprecord() {
        // TODO Auto-generated method stub
        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        recordTimeText.setText("00:00");
        vibrate();
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
            }

        } catch (Exception e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }

   // private void stopPlaying()

    private void cancelprogresssdialog() {
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
}
