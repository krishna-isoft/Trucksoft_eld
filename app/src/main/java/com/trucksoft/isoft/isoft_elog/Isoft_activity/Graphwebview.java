package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.widget.ImageView;

import com.trucksoft.isoft.isoft_elog.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by isoft on 5/7/17.
 */

public class Graphwebview extends Activity{
    private ImageView imggraph;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_web_view);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        myWebView.loadUrl("http://trucksoft.net/e-log/drivergraph.php?did=1&date=2017-07-10");
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://e-logbook.info/app/drivergraph.php?date=2017-07-12&driverid=2&did=2");
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


}
