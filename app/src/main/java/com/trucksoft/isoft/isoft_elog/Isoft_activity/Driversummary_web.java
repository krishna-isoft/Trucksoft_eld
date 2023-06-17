package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Driversummary_web extends AppCompatActivity {
    WebView myWebView;
    private TextView txtback;
    Font_manager font_manager;
    private Context context;
    Preference pref;
     //ProgressDialog pd ;
     FrameLayout frameview;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private String strstate="";
    private String straddress="";
    private String lat="";
    private String lon="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.summary_web);
        font_manager=new Font_manager();
        context=this;
        pref=Preference.getInstance(context);
        txtback=findViewById(R.id.txtback);
        frameview=findViewById(R.id.frame_memecontent);
       // pd = ProgressDialog.show(this, "", "Loading...",true);
        lat=pref.getString(Constant.C_LATITUDE);
        lon=pref.getString(Constant.C_LONGITUDE);
        try {
            getAddressFromLocation(Double.parseDouble(""+lat),Double.parseDouble(""+lon));
        }catch (Exception e)
        {

        }
        txtback.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        myWebView = findViewById(R.id.webview);
//        myWebView.setWebViewClient(new myWebClient());
//        myWebView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...",true);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);

        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);

        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        this.myWebView.getSettings().setDomStorageEnabled(true);
        //myWebView.loadUrl("https://eld.e-logbook.info/test/#/Summary/357/California/California");
        //setTitle("Summary");
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(pd!=null && pd.isShowing())
                {
                    pd.dismiss();
                }
            }
        });
        SimpleDateFormat formatdatetime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String ctime = formatdatetime.format(new Date());
Log.e("urlvv","https://eld.e-logbook.info/ElogApp/#/Summary/"+pref.getString(Constant.DRIVER_ID)+"/"+pref.getString(Constant.STATE_FIELD)+"/"+strstate+"?"+ctime);
        myWebView.loadUrl("https://eld.e-logbook.info/ElogApp/#/Summary/"+pref.getString(Constant.DRIVER_ID)+"/"+pref.getString(Constant.STATE_FIELD)+"/"+strstate+"?"+ctime);








        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(context, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
                    finish();
//                }
            }
        });
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            frameview.setAnimation(inAnimation);
            frameview.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            // dialogz.dismiss();
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            frameview.setAnimation(outAnimation);
            // progressBarHolder.setVisibility(View.GONE);
//cancelprogresssdialog();

//            if(dialog !=null && dialog.isShowing() || dialogz !=null && dialogz.isShowing())
//            {
//
//            }else {
//
//
//                if(((Activity) getActivity())!=null)
//                {
//                    if (!((Activity) getActivity()).isFinishing()) {
//                        // progressDialog = new ProgressDialog(getActivity());
//                        dialogz = new ProgressDialog(contexts,
//                                AlertDialog.THEME_HOLO_LIGHT);
//                        dialogz.setMessage("Graph Loading...");
//                        dialogz.setCancelable(false);
//                        dialogz.show();//issue
//                        callprogress();
//                    }
//                }else {
//                    //Log.e("ckl", "//////////////////////closed");
//                }
//
//
//            }
            //dialogz.dismiss();
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


    private String gettimeonedate()
    {
        String tdate="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        Log.e("timezone","&"+timezone);
        try {
//        TimeZone tz = TimeZone.getTimeZone(""+timezone);
//        Calendar c = Calendar.getInstance(tz);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        tdate=dateFormat.format(c.getTime());

            Calendar c = Calendar.getInstance();
            Date date = c.getTime(); //current date and time in UTC
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setTimeZone(TimeZone.getTimeZone(timezone)); //format in given timezone
            tdate = df.format(date);

            Log.e("tdate","&"+tdate);
        }catch (Exception e)
        {

        }
        return tdate;
    }


    public String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                if (address.getAddressLine(0) != null && address.getAddressLine(0).length() > 0 && !address.getAddressLine(0).contentEquals("null")) {
                    sb.append(address.getAddressLine(0)).append("\n");

                    try {
                        strstate = address.getAdminArea();
                        //Log.e("state","#"+strstate);
                    } catch (Exception e) {

                    }
                } else {

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    try {
                        strstate = address.getAdminArea();
                        pref.putString(Constant.CURRENT_STATE, "" + strstate);
                        //  Log.e("state","@"+strstate);
                    } catch (Exception e) {

                    }
                }

                straddress = sb.toString();

                //Log.e("leaddress","@"+straddress);
            }
        } catch (IOException e) {
            //Log.e(TAG, "Unable connect to Geocoder", e);
        }



        return straddress;
    }
}
