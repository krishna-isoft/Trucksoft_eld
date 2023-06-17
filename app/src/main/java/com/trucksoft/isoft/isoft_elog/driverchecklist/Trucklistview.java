package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Trucklistview extends Activity {
    private Context context;
    private Preference pref;
    private ListView list;
    ProgressDialog dialog;
    Eld_api api;
    List<Chlitem_model> movies;
    checklist_adapter adapter;
    int posit=0;
    ArrayList<Uri> array_img ;
    ArrayList<String> selectid=new ArrayList<>();
    ArrayList<String> selectidgood=new ArrayList<>();
    ArrayList<String> truck_item_old=new ArrayList<>();
    List<Chlitem_model> selectedmodel;
    private TextView txt_done;
    LinearLayout mContent;

    private String tag;
    private Intent mIntent;
    byte[] img=null;
    Dialog dialogfederal;
    String id="";
    ArrayList<Uri> array_imgtrailer=new ArrayList<>() ;
    ArrayList<String> selectidtrailer=new ArrayList<>();
    ArrayList<String> selectidgoodtrailer=new ArrayList<>();
    ArrayList<String> truck_item_oldtrailer=new ArrayList<>();
    List<Chlitem_model> selectedmodeltrailer=new ArrayList<>();
    private TextView txttitle;
    String updatedStringimage;
    String updatedStringimage1;
    private Paint paint;

    View view;
    String DIRECTORY;
    String StoredPath;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private byte[] image;
    private byte[] image1;

    private String driverid="";



    private int truckitemcount=40;
    private int traileritemcount=15;
    Font_manager font_manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_checklisthome);
        context=this;
        pref=Preference.getInstance(context);
        movies=new ArrayList<>();
        array_img=new ArrayList<>();
        mIntent=getIntent();
        selectedmodel=new ArrayList<>();
        font_manager=new Font_manager();
        selectidgood=new ArrayList<>();
        list=findViewById(R.id.checklist);
        driverid=pref.getString(Constant.DRIVER_ID);
        txttitle=findViewById(R.id.checklist_tv_title);
        txt_done=findViewById(R.id.checklist_tv_done);
        DIRECTORY = Environment.getExternalStorageDirectory().getPath() + File.separator + getResources().getString(R.string.app_name);
        StoredPath = DIRECTORY + "/" + driverid + ".png";
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            dir.mkdirs();
if(mIntent.hasExtra("count"))
{
    traileritemcount= Integer.parseInt(mIntent.getStringExtra("count"));
    truckitemcount= Integer.parseInt(mIntent.getStringExtra("count"));
}

        tag = mIntent.getStringExtra("tag");
        if (tag.equalsIgnoreCase("truck")) {
            txttitle.setText("Truck Items");
           // mTextView2.setText("Truck Items");
            selectidgood = mIntent
                    .getStringArrayListExtra("sel_item_ids_good");
            truck_item_old=mIntent
                    .getStringArrayListExtra("sel_item_ids_bad");
            selectid= mIntent
                    .getStringArrayListExtra("sel_item_ids");

            //Log.e("selectidgood",""+selectidgood.toString());

            if(mIntent.hasExtra("id"))
            {
                id=mIntent
                        .getStringExtra("id");
            }
        }else{
            txttitle.setText("Trailer Items");

                // mTextView2.setText("Truck Items");
                selectidgoodtrailer = mIntent
                        .getStringArrayListExtra("sel_item_ids_good");

                selectidtrailer= mIntent
                        .getStringArrayListExtra("sel_item_ids");
            truck_item_oldtrailer=mIntent
                    .getStringArrayListExtra("sel_item_ids_bad");
                //Log.e("selectidgood",""+selectidgood.toString());

                if(mIntent.hasExtra("id"))
                {
                    id=mIntent
                            .getStringExtra("id");
                }

        }
        if(selectidgood==null || selectidgood.size()<=0)
        {

            selectidgood=new ArrayList<>();
        }else{

        }

        if(pref.getString(Constant.CHECKLIST_MODE).contentEquals("edit"))
        {
            if (tag.equalsIgnoreCase("truck")) {
                truck_item_old = mIntent
                        .getStringArrayListExtra("edit_item");

            }else{
                truck_item_oldtrailer = mIntent
                        .getStringArrayListExtra("edit_item");

            }

        }

        getitem();

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tag.equalsIgnoreCase("truck")) {

                    if(truckitemcount==selectid.size()+selectidgood.size()) {
                        Gson gson = new Gson();
                        String json1 = gson.toJson(selectedmodel);
                        Log.e("json1", "@" + json1);
                        pref.putString("seletecteditem", json1);

                        Bundle b = new Bundle();
                        b.putStringArrayList("truck_item_old", truck_item_old);
                        b.putStringArrayList("sel_item_ids", selectid);
                        b.putStringArrayList("sel_item_ids_good", selectidgood);
                        b.putString("tag", "truck");
                        //b.putStringArrayList("checked_status_array", checked_status_array);
                        Intent intent = new Intent();
                        intent.putExtras(b);
                        setResult(RESULT_OK, intent);
                        Log.e("call", "@" + "finish");
                        finish();
                    }else{

                        itemalert("truck","Please choose all TRUCK items");
                    }


                }else{

                    if(traileritemcount==selectidtrailer.size()+selectidgoodtrailer.size())
                    {

                    Gson gson = new Gson();
                    String json1 = gson.toJson(selectedmodeltrailer);

                    pref.putString("seletecteditemtrailer", json1);

                    Bundle b = new Bundle();
                    b.putStringArrayList("sel_item_ids", selectidtrailer);
                    b.putStringArrayList("truck_item_old", truck_item_oldtrailer);
                    b.putStringArrayList("sel_item_ids_good", selectidgoodtrailer);
                    b.putString("tag", "trailer");
                    //b.putStringArrayList("checked_status_array", checked_status_array);
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    setResult(RESULT_OK, intent);
                    finish();
                    }else{
                        itemalert("trailer","Please choose all TRAILER items");
                    }
                }

            }
        });
    }


    private void getitem()
    {
String did=pref.getString(Constant.DRIVER_ID);
String cc=pref.getString(Constant.COMPANY_CODE);
        dialog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        api = DispatchServiceGenerator.createService(Eld_api.class,context);
        Call<List<Chlitem_model>> call;
if(tag.contentEquals("truck")) {
    call = api.gettruckitems(did, cc, "driver");
}else{
    call = api.gettraileritems(did, cc, "driver");
}

        call.enqueue(new Callback<List<Chlitem_model>>() {
            @Override
            public void onResponse(Call<List<Chlitem_model>> call, Response<List<Chlitem_model>> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    movies=new ArrayList<>();
                    movies=response.body();
                    setVal(movies);
                }else{
                    dialog.dismiss();

                    //Log.e("ss"," Response "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Chlitem_model>> call, Throwable t) {
               // Log.e("dd"," Response Error "+t.getMessage());
                dialog.dismiss();
                //  gethistory();
            }
        });
    }

    private void setVal(final List<Chlitem_model> movies)
    {



if(tag.contentEquals("truck")) {
    try {
        for (int kz = 0; kz < movies.size(); kz++) {
            if (selectidgood.contains(movies.get(kz).getId())) {

            } else {
                //Log.e("truck_item_old","@"+truck_item_old.toString());
                if (truck_item_old.contains(movies.get(kz).getId())) {

                } else {
                    selectidgood.add(movies.get(kz).getId());
                }
            }
        }
    }catch (Exception e)
    {

    }
    adapter = new checklist_adapter(context, movies, selectid, selectidgood, truck_item_old, id,tag, new checklist_adapter.OnItemCheckListener() {
        @Override
        public void OnItemCheck(Chlitem_model cvc, int position) {
            if (selectid.contains(cvc.getId())) {

            } else {
                selectid.add(cvc.getId());
                //Log.e("slct1","#"+selectid.toString());
                selectedmodel.add(cvc);
            }
        }

        @Override
        public void OnItemUnCheck(Chlitem_model cvc, int position) {

            String svc=cvc.getId()+">>"+cvc.getUrimg();

            if(truck_item_old !=null && truck_item_old.size()>0)
            {
                if(truck_item_old.contains(""+svc)) {
                    truck_item_old.remove(svc);
                    Log.e("truck_item_oldll","@"+truck_item_old.toString());
                    //movies.get(position).setSelectstatus(0);
                }
            }
            Log.e("svc","@"+svc);
            if(selectid.size()>0 && selectid!=null) {
                if (selectid.contains(cvc.getId())) {
                    selectid.remove(cvc.getId());
                }
            }


if(selectedmodel.contains(cvc))
{
    selectedmodel.remove(cvc);
}
            if (cvc.getSelectstatus() == 2) {
                if (selectidgood.contains(cvc.getId())) {

                } else {
                    selectidgood.add(cvc.getId());
                }
            } else {
                if(!truck_item_old.contains(""+svc)) {
                    truck_item_old.add(cvc.getId());
                }
                if (selectidgood.contains(cvc.getId())) {
                    selectidgood.remove(cvc.getId());
                }
            }

            if (selectedmodel.size() > 0) {
                for (int j = 0; j < selectedmodel.size(); j++) {
                    Chlitem_model sc = new Chlitem_model();
                    sc = selectedmodel.get(j);
                    if (sc.getId().contentEquals(cvc.getId())) {
                        selectedmodel.remove(j);
                    }
                }
            }


        }

        @Override
        public void OnImageClick(Chlitem_model cvc, int position) {
            posit = position;
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(Trucklistview.this);


        }

    });

}else
{
    try{
        for (int kz=0;kz<movies.size();kz++)
        {
            if (selectidgoodtrailer.contains(movies.get(kz).getId())) {

            } else {

                if(truck_item_oldtrailer.contains(movies.get(kz).getId()))
                {

                }else{
                    selectidgoodtrailer.add(movies.get(kz).getId());
                }
            }
        }
    }catch (Exception e)
    {

    }

    adapter = new checklist_adapter(context, movies,selectidtrailer,selectidgoodtrailer,truck_item_oldtrailer,id,tag, new checklist_adapter.OnItemCheckListener() {
        @Override
        public void OnItemCheck(Chlitem_model cvc, int position) {
            String svc=cvc.getId()+">>"+cvc.getUrimg();

            if(truck_item_oldtrailer !=null && truck_item_oldtrailer.size()>0)
            {
                if(truck_item_oldtrailer.contains(""+svc)) {
                    truck_item_oldtrailer.remove(svc);
                }
            }


            if(selectidtrailer.contains(cvc.getId()))
            {

            }else {
                selectidtrailer.add(cvc.getId());

                selectedmodeltrailer.add(cvc);

            }
        }

        @Override
        public void OnItemUnCheck(Chlitem_model cvc, int position) {
            if(selectidtrailer.size()>0 && selectidtrailer!=null) {
                if (selectidtrailer.contains(cvc.getId())) {
                    selectidtrailer.remove(cvc.getId());
                }
            }

            if(cvc.getSelectstatus()==2)
            {
                if(selectidgoodtrailer.contains(cvc.getId()))
                {

                }else {
                    selectidgoodtrailer.add(cvc.getId());
                }
            }else{
                if(!truck_item_oldtrailer.contains(""+cvc.getId())) {
                    truck_item_oldtrailer.add(cvc.getId());
                }
                if(selectidgoodtrailer.contains(cvc.getId()))
                {
                    selectidgoodtrailer.remove(cvc.getId());
                }
            }

            if(selectedmodeltrailer.size()>0)
            {
                for(int j=0;j<selectedmodeltrailer.size();j++)
                {
                    Chlitem_model sc=new Chlitem_model();
                    sc=selectedmodeltrailer.get(j);
                    if(sc.getId().contentEquals(cvc.getId()))
                    {
                        selectedmodeltrailer.remove(j);
                    }
                }
            }
        }
        @Override
        public void OnImageClick(Chlitem_model cvc, int position) {
            posit=position;
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(Trucklistview.this);


        }

    });

}
        list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        //Log.e("requesrcc","@@@"+requestCode);
        switch (requestCode) {
            case 203:
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {

                        try {
                            InputStream iStream = getContentResolver().openInputStream(result.getUri());
                            img = getBytes(iStream);
                            convertstring(img);
                          // result.getUri();

                           movies.get(posit).setUrimg(result.getUri().toString());




                           adapter.notifyDataSetChanged();




                        } catch (Exception e) {

                        }
                        //  Log.e("ddd",""+result.getUri().toString());
                        // decodeFile(result.getUri().toString());
                    }

                    else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                    }}
                break;
        }

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.checklist_iv_back:
                finish();
                break;


            case R.id.checklist_tv_done:
                break;
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

    private void convertstring(byte[] imgkk)
    {
        String strPhoto = "";
        if (imgkk != null) {
            if (imgkk.length > 0) {
                try {
                    CommonUtil commonUtil = new CommonUtil(context);
                    strPhoto = commonUtil.ecode64(imgkk);
                    commonUtil = null;
                } catch (Exception e) {
                    //Log.e("Image Exception", e.getMessage());
                }
            }
        } else {
            strPhoto = null;
        }

        String vb=pref.getString(Constant.DLIST_STATUS);
        Log.e("vb","###"+vb);
        if(vb.contentEquals("add"))
        {

movies.get(posit).setStrconvert(strPhoto);

            // setimage();
        }else  if(pref.getString(Constant.CHECKLIST_MODE).contentEquals("edit"))
        {
//savimages(movies.get(posit).getId(),strPhoto);
        }

    }

 //   private void savimages(String itemid, String strPhoto)
//    {
//        //Log.e("call","savimages");
//        dialog = new ProgressDialog(Trucklistview.this,
//                AlertDialog.THEME_HOLO_LIGHT);
//
//        if (OnlineCheck.isOnline(this)) {
//            //Log.e("call","savimages11");
//            dialog.setMessage("Please wait...");
//            dialog.setCancelable(false);
//            dialog.show();
//            WebServices.saveitemimg(context, ""+id,itemid,strPhoto,tag, new JsonHttpResponseHandler() {
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      String responseString, Throwable throwable) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers,
//                            responseString, throwable);
//
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      Throwable throwable, JSONArray errorResponse) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable,
//                            errorResponse);
//                    //Log.e("res2",""+errorResponse.toString());
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers,
//                                      Throwable throwable, JSONObject errorResponse) {
//                    // TODO Auto-generated method stub
//                    super.onFailure(statusCode, headers, throwable,
//                            errorResponse);
//                    //Log.e("res3",""+errorResponse.toString());
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
//                    //Log.e("res4",""+response.toString());
//
//                    if (response != null) {
//                        setimageval(response);
//                    }
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers,
//                                      JSONObject response) {
//                    // TODO Auto-generated method stub
//                    super.onSuccess(statusCode, headers, response);
//                    dialog.dismiss();
//                    //Log.e("res5",""+response.toString());
//
//                    if (response != null) {
//
//                    }
//
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
//    }
    private void setimageval(JSONArray response) {

        if (response != null) {
            if (response.length() > 0) {
                try {
                    for (int j = 0; j < response.length(); j++) {
                        JSONObject mResultJsonimg = response
                                .getJSONObject(j);
                        String imgid = mResultJsonimg
                                .getString("img_id");
                        String imgurl = mResultJsonimg
                                .getString("img_url");
                        movies.get(posit).setUrimg(imgurl);
                    }
                }catch (Exception e)
                {

                }


            }
        }
    }

    private void itemalert(String strval,String msg) {

        if (dialogfederal != null) {
            if (dialogfederal.isShowing()) {
                dialogfederal.dismiss();
            }
        }
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;
        if(strval.contentEquals("truck"))
        {
            dialogView= inflater.inflate(R.layout.item_notification, null);
        }else {
            dialogView = inflater.inflate(R.layout.item_trailernotification, null);
        }


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogfederal = new Dialog(context, R.style.DialogTheme);
        dialogfederal.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
        txtstatus.setText(""+msg);
        dialogfederal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfederal.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogfederal.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogfederal.getWindow().setAttributes(lp);
        dialogfederal.show();
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfederal.dismiss();
            }
        });


    }

}
