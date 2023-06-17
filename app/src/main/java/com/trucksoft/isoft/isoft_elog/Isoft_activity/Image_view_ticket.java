package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trucksoft.isoft.isoft_elog.R;

/**
 * Created by isoft on 27/7/17.
 */

public class Image_view_ticket extends Activity{

    private ImageView imgview;
    private Context context;
    private ProgressDialog progress;

    Bundle bundle;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bol_imgview);
        context=this;
        imgview=(ImageView)findViewById(R.id.image_ttt);
        String title = getIntent().getStringExtra("img_url");





//        Picasso.with(context)
//                .load(title + "?.time();")
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .placeholder( R.drawable.prog_animation )
//                .error(R.drawable.whitekt)
//                .into(imgview);


        progress=new ProgressDialog(this);
        progress.setMessage("Loading.......");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        //start progressbar here
        Picasso.with(context)
                .load(title + "?.time();")
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imgview.setImageBitmap(bitmap);
                        //stop progressbar here
                        progress.dismiss();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        //stop progressbar here
                        progress.dismiss();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }
}
