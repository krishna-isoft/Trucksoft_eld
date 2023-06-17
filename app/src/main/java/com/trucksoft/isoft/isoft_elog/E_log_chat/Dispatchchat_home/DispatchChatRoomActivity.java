package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_adapter.DispatcherThreadAdapter;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.Dispat_Application;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatchConfig;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatcherEndPoints;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_message;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatchuser;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_service.DispatchNotificationUtils;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//import android.util.Log;

public class DispatchChatRoomActivity extends AppCompatActivity {

    private String TAG = DispatchChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private DispatcherThreadAdapter mAdapter;
    private ArrayList<Dispatch_message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private EditText inputMessage;
    private ImageView btnSend;
    ProgressDialog dialog;
    Preference pref;
    Context context;
    private ImageView imgback;
    private TextView txtname;
    private int backstatus=1;
    private ImageView txtsmile;
    private TextView txtgal;
    private TextView txtcamera;
//    private TextView txtattach;
    //private TextView txtsend;
    Font_manager font_manager;
    private Uri fileUri;
    String filePath = "";
    private static final int PICK_Camera_IMAGE = 8;
    public static final int MEDIA_TYPE_IMAGE = 9;
    private static final int RESULT_LOAD_IMAGE = 10;
    private Bitmap bitmap;
    private byte[] image;
    String updatedStringimage="";
    private static final  int PICK_PDF_REQUEST = 1;
    Uri pdfpath=null;
    ProgressBar progressBar;
    public static final String UPLOAD_URL = "http://internetfaqs.net/AndroidPdfUpload/upload.php";
  //  EmojIconActions emojIcon;
    View rootView;
   // EmojiconEditText emojiconEditText;
private EditText txtmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        font_manager=new Font_manager();
         progressBar =findViewById(R.id.progress);
        rootView = findViewById(R.id.root_view);
        txtmessage=findViewById(R.id.txt_message);
      //  emojiconEditText =findViewById(R.id.emojicon_edit_text);
       // emojiconEditText.addTextChangedListener(tc);
        imgback =findViewById(R.id.chat_list_iv_back);
        txtname=findViewById(R.id.chat_list_tv_title) ;
        txtsmile=findViewById(R.id.txt_sml) ;
        txtcamera=findViewById(R.id.txt_camera) ;
        txtgal=findViewById(R.id.txt_gal) ;
        btnSend =  findViewById(R.id.btn_send);
        pref=Preference.getInstance(context);
        updatedStringimage="";
      //  inputMessage = (EditText)  findViewById(R.id.message);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       // imm.showSoftInput(inputMessage, InputMethodManager.SHOW_IMPLICIT);
       // txtsmile.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        txtcamera.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        txtgal.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
       // txtattach.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
       // txtsend.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        //btnSend.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));
        txtmessage.addTextChangedListener(tc);
        dialog = new ProgressDialog(DispatchChatRoomActivity.this,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
       // Log.e("chatRoomId",""+chatRoomId);
        if(intent.hasExtra("backcheck"))
        {
           backstatus=0;
        }
        String title = intent.getStringExtra("name");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();
        if(title !=null && !title.contentEquals("null") && title.length()>0)
        {
            txtname.setText("" + title);
        }else {
            txtname.setText("");
        }
        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
       // String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId(); old
        String selfUserId = pref.getString(Constant.DRIVER_ID);//new
        //Log.e("selfUserId",""+selfUserId);
        mAdapter = new DispatcherThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DispatchConfig.PUSH_NOTIFICATION)) {
                    // new push message is received
                    sendupdatetoServer();
                    handlePushNotification(intent);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
               // btnSend.setVisibility(View.VISIBLE);
                sendMessage();
            }
        });
        txtcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnSend.setVisibility(View.VISIBLE);
                sendCamera();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  sendupdatetoServer();
                if(backstatus==0) {

                    Intent mIntent2 = new Intent(DispatchChatRoomActivity.this,
                            Dispatch_chathome.class);
                    mIntent2.putExtra("backhome", "backhome");
                    startActivity(mIntent2);
                }else
                {
                    Intent mIntent2 = new Intent(DispatchChatRoomActivity.this,
                            Dispatch_chathome.class);

                    startActivity(mIntent2);
                }
                finish();
            }
        });

        fetchChatThread();
        txtgal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String dpid = pref.getString(Constant.DISPATCH_ID_NEW);
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
//        txtattach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showFileChooser();
//            }
//        });
//        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, txtsmile);
//        emojIcon.ShowEmojIcon();
//        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
//        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
//            @Override
//            public void onKeyboardOpen() {
//               // Log.e(TAG, "Keyboard opened!");
//            }
//
//            @Override
//            public void onKeyboardClose() {
//               // Log.e(TAG, "Keyboard closed");
//            }
//        });

//        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                emojIcon.setUseSystemEmoji(b);
//                // textView.setUseSystemDefault(b);
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(DispatchConfig.PUSH_NOTIFICATION));

        DispatchNotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     * */
    private void handlePushNotification(Intent intent) {
        Dispatch_message message = (Dispatch_message) intent.getSerializableExtra("message");
       if(intent.hasExtra("rtitle")) {
           String title = intent.getStringExtra("rtitle");
           if (title != null && title.length() > 0 && !title.contentEquals("null")) {
               if (title.contentEquals("read_status")) {
                  // Log.e("kk", "not call view");
                 //  fetchChatThread();
                   pref.putString(Constant.READ_STATUSS,"1");
                   mAdapter.notifyDataSetChanged();
               } else {
                   pref.putString(Constant.READ_STATUSS,"0");
                   String chatRoomId = intent.getStringExtra("chat_room_id");

                   if (message != null && chatRoomId != null) {
                       messageArrayList.add(message);
                       mAdapter.notifyDataSetChanged();
                       if (mAdapter.getItemCount() > 1) {
                           recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                       }
                   }
               }
           }else
           { pref.putString(Constant.READ_STATUSS,"0");
               String chatRoomId = intent.getStringExtra("chat_room_id");

               if (message != null && chatRoomId != null) {
                   messageArrayList.add(message);
                   mAdapter.notifyDataSetChanged();
                   if (mAdapter.getItemCount() > 1) {
                       recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                   }
               }
           }
       }else
       {
           pref.putString(Constant.READ_STATUSS,"0");
           String chatRoomId = intent.getStringExtra("chat_room_id");

           if (message != null && chatRoomId != null) {
               messageArrayList.add(message);
               mAdapter.notifyDataSetChanged();
               if (mAdapter.getItemCount() > 1) {
                   recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
               }
           }
       }



    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     * */
    private void sendMessage() {
        final String message = this.txtmessage.getText().toString().trim();
if(updatedStringimage !=null && updatedStringimage.length()>0 && !updatedStringimage.contentEquals("null"))

{

}else {
    if (TextUtils.isEmpty(message)) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
        return;
    }
}

      //  String endPoint = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);
        String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();

        //String endPoint = EndPoints.CHAT_ROOM_MESSAGE+"?"+cmpcode;
        String endPoint = DispatcherEndPoints.CHAT_ROOM_MESSAGE;
        Log.e("111", "endpo: " + endPoint);

       // this.emojiconEditText.setText("");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               Log.e(TAG, "responsekk: " + response);
                progressBar.setVisibility(View.GONE);
                txtmessage.setText("");
                pref.putString(Constant.READ_STATUSS,"0");
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                       // JSONObject commentObj = obj.getJSONObject("message");

                        String commentId = obj.getString("message_id");
                        String commentText = obj.getString("message");
                        //Log.e("commentText",""+commentText);
                        String createdAt = obj.getString("created_at");
                        String chstatus = obj.getString("chstatus");
                     //   String attachment= obj.getString("attachment");
                      //  String filed= obj.getString("file");
                        JSONObject userObj = obj.getJSONObject("user");
                        String userId = userObj.getString("user_id");
                        String userName = userObj.getString("name");
                        Dispatchuser user = new Dispatchuser(userId, userName, null);

                        Dispatch_message message = new Dispatch_message();
                        message.setId(commentId);
                        message.setMessage(commentText);
                        message.setchstatus(chstatus);
                        message.setCreatedAt(createdAt);
                       // Log.e("attachment","dd"+attachment);
                       // message.setattachment(attachment);
                       // message.setfile(filed);
                        message.setUser(user);

                        messageArrayList.add(message);
                        updatedStringimage="";
                        progressBar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            // scrolling to bottom of the recycler view
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                       // Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("ee", "json parsing error: " + e.getMessage());
                   // Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Volley errorok: " + error.getMessage() + ", code: " + networkResponse.toString());
                //Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
               // emojiconEditText.setText(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());//old
                String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();

                params.put("user_id",pref.getString(Constant.DRIVER_ID));
                params.put("message", message);
                params.put("toid",chatRoomId);
                params.put("to","comp");
                params.put("sender","driver");
                params.put("cc",""+cmpcode);
               // Log.e(TAG, "Params: " + params.toString());
                params.put("bolimage",""+ updatedStringimage);
               // Log.e("bolimage",""+ updatedStringimage);
                Log.e(TAG, "Params: " + params.toString());


                return params;
            }
        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        Dispat_Application.getInstance().addToRequestQueue(strReq);
    }


    /**
     * Fetching all the messages of a single chat room
     * */
    private void fetchChatThread() {
//kk
        //String endPoint = EndPoints.CHAT_THREAD.replace("_ID_", chatRoomId)+"/"+MyApplication.getInstance().getPrefManager().getUser().getId();//old
        //Log.e("esd", "@ " + EndPoints.CHAT_THREAD.replace("_ID_", chatRoomId)+"/"+pref.getString(Constant.DRIVER_ID));
        String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();
        String endPoint = DispatcherEndPoints.CHAT_THREAD.replace("_ID_", chatRoomId)+"/"+pref.getString(Constant.DRIVER_ID)+"/"+cmpcode;

       Log.e(TAG, "endPointzzkknnnn: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //  Log.e(TAG, "response: " + response);
                dialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");

                        for (int i = 0; i < commentsObj.length(); i++) {
                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            String commentId = commentObj.getString("message_id");
                            String commentText = commentObj.getString("message");
                            String createdAt = commentObj.getString("created_at");
                            String chstatus=commentObj.getString("chstatus");
                           // String attachment= commentObj.getString("attachment");
                           // String filed= commentObj.getString("file");
                            JSONObject userObj = commentObj.getJSONObject("user");
                            String userId = userObj.getString("user_id");
                            String userName = userObj.getString("username");

                            Dispatchuser user = new Dispatchuser(userId, userName, null);

                            Dispatch_message message = new Dispatch_message();
                            message.setId(commentId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                            message.setchstatus(chstatus);
                           // Log.e("attachment","dd"+attachment);
                            //message.setattachment(attachment);
                          //  message.setfile(filed);
                            message.setUser(user);


                            messageArrayList.add(message);
                        }
progressBar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                       // Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    dialog.dismiss();
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    //Toast.makeText(getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
               // Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
               // Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        //Log.e("stkkkkkkkkkk",""+strReq.toString());
        Dispat_Application.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(backstatus==0) {
            Intent mIntent2 = new Intent(DispatchChatRoomActivity.this,
                    Dispatch_chathome.class);
            mIntent2.putExtra("backhome", "backhome");
            startActivity(mIntent2);
        }else
        {
            Intent mIntent2 = new Intent(DispatchChatRoomActivity.this,
                    Dispatch_chathome.class);

            startActivity(mIntent2);
        }
        finish();
    }

    private void sendupdatetoServer() {

        pref=Preference.getInstance(getApplicationContext());
        final String driverid=pref.getString(Constant.DRIVER_ID);
        if(driverid==null)
        {
            return;
        }
        String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();
        String endPoint = DispatcherEndPoints.CHAT_ROOM_MESSAGE_UPDATE.replace("_ID_", driverid)+"?cc="+cmpcode;
       // Log.e(TAG, "endpointzd: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.PUT,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                         } else {
                        }

                } catch (JSONException e) {
                      }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
              //  Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
              //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();
                params.put("cc", ""+cmpcode);
                params.put("driver_id", driverid);
                params.put("receipt_id", chatRoomId);
                params.put("toid", chatRoomId);

                //Log.e("receipt_id " ,""+chatRoomId);
               // Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        Dispat_Application.getInstance().addToRequestQueue(strReq);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
    private void sendCamera()
    {
        ContentValues values;
        //captureImage();
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

        fileUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, PICK_Camera_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Log.e("requestcode",""+requestCode);
        // Log.e("resultCode",""+resultCode);
        // Log.e("RESULT_OK",""+RESULT_OK);
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;

        if (resultCode == RESULT_OK) {

            filePath = null;
            //Log.e("RESULT_LOAD_IMAGE", "" + RESULT_LOAD_IMAGE);
            switch (requestCode) {
                case RESULT_LOAD_IMAGE:

                    selectedImageUri = data.getData();

                    if (selectedImageUri != null) {
                        try {
                            // OI FILE Manager
                            String filemanagerstring = selectedImageUri.getPath();

                            // MEDIA GALLERY
                            String selectedImagePath = getPath(selectedImageUri);

                            if (selectedImagePath != null) {
                                filePath = selectedImagePath;
                            } else if (filemanagerstring != null) {
                                filePath = filemanagerstring;
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Unknown path", Toast.LENGTH_LONG).show();
                                //Log.e("Bitmap", "Unknown path");
                            }

                            if (filePath != null) {
                                decodeFile(filePath);

                            } else {
                                bitmap = null;
                                Toast.makeText(getApplicationContext(),
                                        "Retake Image", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Retake Image",
                                    Toast.LENGTH_LONG).show();
                            //Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Retake Image",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case PICK_Camera_IMAGE:
                    if (resultCode == RESULT_OK) {
                        selectedImageUri = fileUri;

                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(getApplicationContext(),
                                "Picture was not taken", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Picture was not taken", Toast.LENGTH_SHORT).show();
                    }

                    if (selectedImageUri != null) {
                        try {
                            // OI FILE Manager
                            String filemanagerstring = selectedImageUri.getPath();

                            // MEDIA GALLERY
                            String selectedImagePath = getPath(selectedImageUri);

                            if (selectedImagePath != null) {
                                filePath = selectedImagePath;
                            } else if (filemanagerstring != null) {
                                filePath = filemanagerstring;
                            } else {
                                Toast.makeText(getApplicationContext(), "Unknown path",
                                        Toast.LENGTH_LONG).show();
                                // Log.e("Bitmap", "Unknown path");
                            }

                            if (filePath != null) {
                                decodeFile(filePath);

                            } else {
                                bitmap = null;
                                Toast.makeText(getApplicationContext(), "Retake Image",
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Retake Image",
                                    Toast.LENGTH_LONG).show();
                            // Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Retake Image",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                case PICK_PDF_REQUEST:
                    if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                        pdfpath = data.getData();

                        String filePath = getFileNameByUri(context, pdfpath);
                        //Log.e("dddkkk","#"+filePath);


                        String stringUri;
                        stringUri = pdfpath.toString();

//                                    Log.e("pdfpath","#"+pdfpath);
//                                    try{
//                                        String filePath= PathUtil.getPath(context,pdfpath);
//                                       // String filePath= FilePath.getPath(context,pdfpath);
//
//
//                                        Log.e("ddd","#"+filePath);
//                                        uploadMultipart(filePath);
//                                    }catch (Exception e)
//                                    {
//                                        Log.e("eee","#"+e.toString());
//                                    }

                    }
                    break;
            }


        }

    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        Bitmap scaledBitmap = null;
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//		      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 612.0f;
        float maxWidth = 816.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//		      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        // ////////////////////////////////////////////////////////////////////////

        // BitmapFactory.Options o2 = new BitmapFactory.Options();
        //  setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//		      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//		      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//		          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//		      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);

            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);

            } else if (orientation == 3) {
                matrix.postRotate(180);

            } else if (orientation == 8) {
                matrix.postRotate(270);

            }


            bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // bitmap = BitmapFactory.decodeFile(filePath, o2);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //new FreightNumDispatch_wwe.ImageUploadTask().execute();kl
        image = stream.toByteArray();
        updatedStringimage = Base64.encodeToString(image, Base64.DEFAULT);
        progressBar.setVisibility(View.VISIBLE);
        sendMessage();
//Log.e("calling","bitmap");
        //uploadweightticket(updatedStringimage, dispatch_id, dsp_detailid,strtype);
//


    }



    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }


    private String getFileNameByUri(Context context, Uri uri)
    {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0)
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;
//Log.e("filepath","##"+filepath);
        }
        else
        if (uri.getScheme().compareTo("file") == 0)
        {
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            }
            catch (URISyntaxException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            filepath = uri.getPath();
        }
        return filepath;
    }
//    private fun resetInput() {
//        // Clean text box
//        txtMessage.text.clear()
//
//        // Hide keyboard
//        val inputManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputManager.hideSoftInputFromWindow(
//                currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
//          )
//    }
    TextWatcher tc=new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
String strtext=txtmessage.getText().toString().trim();

if(strtext !=null && strtext.length()>0 && !strtext.contentEquals("null"))
{
    if(strtext.length()>0)
    {
      txtcamera.setVisibility(View.GONE);
      txtgal.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);
    }else{
        txtcamera.setVisibility(View.VISIBLE);
        txtgal.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
    }
}else{
    txtcamera.setVisibility(View.VISIBLE);
    txtgal.setVisibility(View.VISIBLE);
    btnSend.setVisibility(View.GONE);
}
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
};
}
