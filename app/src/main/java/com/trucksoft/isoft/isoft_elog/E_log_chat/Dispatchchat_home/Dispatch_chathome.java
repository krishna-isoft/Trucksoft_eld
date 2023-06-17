package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_adapter.DispatcherChatRoomsAdapter;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_adapter.DispatchnotificationAdapter;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.Dispat_Application;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatchConfig;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatcherEndPoints;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_helper.DispatcherDividerItem;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_chat_mod;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_message;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_service.DispatchNotificationUtils;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_service.Dispatch_GcmIntentService;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
//import android.util.Log;


public class Dispatch_chathome extends AppCompatActivity {

    private String TAG = Dispatch_chathome.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<Dispatch_chat_mod> chatRoomArrayList;
    private ArrayList<Dispatch_chat_mod> unreadchatroom;
    private DispatcherChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ImageView inmlogout;
    Preference pref;
    Context context;
    private ImageView imgback;
    private ImageView imgchathead;
    private Dialog dialogn;
    private View inflate;
    private ListView list;
    private  int totcount=0;
    private TextView txtchatcount;
    private static final int NOTIFY_ME_ID_DISPATCH=1345;
    ProgressDialog dialog;
    private  int statusback=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.chat_fcm);
        context = this;
        deleteCache(context);
        cancelnotificationdispatch();
        Intent intent = getIntent();
        if(intent.hasExtra("backhome"))
        {
            statusback=1;
        }
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        pref = Preference.getInstance(context);
        pref.putInt(Constant.CHAT_COUNT,0);
        inmlogout = (ImageView) findViewById(R.id.chat_notify);
        imgback = (ImageView) findViewById(R.id.chat_list_iv_back);
        imgchathead = (ImageView) findViewById(R.id.chat_notify);
        dialog = new ProgressDialog(Dispatch_chathome.this,
                AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        txtchatcount = (TextView) findViewById(R.id.badge_notification_1);
        imgchathead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                pref.putString(Constant.NOTIFICATION_CLICK_STATUS, "0");

                boolean bolok = false;
                // TODO Auto-generated method stub
                inflate = View.inflate(context, R.layout.list_view, null);
                list = (ListView) inflate.findViewById(R.id.list);

                dialogn = new Dialog(context);
                dialogn.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogn.setContentView(inflate);
                //dialogn.setTitle("Choose anyone");
                //inflate.setPadding(0, 40, 2, 0);
                Window window = dialogn.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                int a = 2;
                a = getsize();
                //Log.e("aaaaa",""+a);
                if (a == 2) {
                    //medium
                    wlp.y = 100;
                } else if (a == 3) {
                    //too small
                   // wlp.y = 205;
                    wlp.y = 115;
                } else {
                    //large
                    wlp.y = 80;
                }

                dialogn.getWindow().setAttributes(wlp);
                wlp.gravity = Gravity.TOP;
                wlp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                wlp.horizontalMargin = 60;


                window.setAttributes(wlp);
                if(unreadchatroom.size()>0)
                {
                    bolok=true;
                }

                list.setAdapter(new DispatchnotificationAdapter(context,
                        unreadchatroom, dialogn));
                if (bolok) {
                    dialogn.show();
                }


            }
        });











        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(statusback==0) {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent mIntent2 = new Intent(Dispatch_chathome.this,
//                            Home_activity_bluetooth.class);
//                    mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(mIntent2);
//                    //  }
//                    finish();
//                }else{
                    finish();
//                }
            }
        });

//        inmlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //MyApplication.getInstance().getPrefManager().storeUser(null);
//                MyApplication.getInstance().logout();
//                launchLoginActivity();
//            }
//        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        /**
         * Broadcast receiver calls in two scenarios
         * 1. gcm registration is completed
         * 2. when new push notification is received
         * */
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(DispatchConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(DispatchConfig.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                   // Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(DispatchConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };

        chatRoomArrayList = new ArrayList<>();
        unreadchatroom= new ArrayList<>();
        mAdapter = new DispatcherChatRoomsAdapter(this, chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DispatcherDividerItem(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new DispatcherChatRoomsAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new DispatcherChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                Dispatch_chat_mod chatRoom = chatRoomArrayList.get(position);
                Intent intent = new Intent(Dispatch_chathome.this, DispatchChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("backhome", "backhome");
                intent.putExtra("backcheck", "backcheck");
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        /**
         * Always check for google play services availability before
         * proceeding further with GCM
         * */
        if (checkPlayServices()) {
            registerGCM();
            fetchChatRooms();
        }
    }

    public int getsize()
    {
        int ik=2;
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                ik=1;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                ik=2;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:


                ik=3;
                break;
            default:
                ik=2;
        }

        return ik;
    }




    /**
     * Handles new push notification
     */
    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == DispatchConfig.PUSH_TYPE_CHATROOM) {
            Dispatch_message message = (Dispatch_message) intent.getSerializableExtra("message");
            Log.e("titl","##"+intent.getStringExtra("rtitle"));
String title=intent.getStringExtra("rtitle");
            if(title !=null && title.length()>0 && !title.contentEquals("null")) {
                if (title.contentEquals("read_status")) {
                    Log.e("kk", "not call dsp");
                }else{
                    String chatRoomId = intent.getStringExtra("chat_room_id");
                    // Log.e("chatRoomIdkt","#"+chatRoomId);

                    if (message != null && chatRoomId != null) {
                        updateRow(chatRoomId, message);
                    }
                }
            }else {
                String chatRoomId = intent.getStringExtra("chat_room_id");
                // Log.e("chatRoomIdkt","#"+chatRoomId);

                if (message != null && chatRoomId != null) {
                    updateRow(chatRoomId, message);
                }
            }
        } else if (type == DispatchConfig.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            Dispatch_message message = (Dispatch_message) intent.getSerializableExtra("message");
            Log.e("message1","##"+message);

            String chatRoomId = intent.getStringExtra("chat_room_id");
            Log.e("chatRoomId1","#"+chatRoomId);

            if (message != null && chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
            //Toast.makeText(getApplicationContext(), "New push: " + message.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Updates the chat list unread count and the last message
     */
    private void updateRow(String chatRoomId, Dispatch_message message) {
        for (Dispatch_chat_mod cr : chatRoomArrayList) {
            Log.e("success","-"+"success");
            Log.e("cr.getId()","-"+cr.getId());
            Log.e("cr.getOnline","&& "+cr.getOnline());
            Log.e("cr.getId()ds","-"+cr.getName());
            Log.e("chatRoomId","-"+chatRoomId);

            if (cr.getId().equals(chatRoomId)) {
               // Log.e("message","-"+message.getOnline());
                int index = chatRoomArrayList.indexOf(cr);
               // Log.e("crmmmmm","&& "+cr.getOnline());
                String online_status=message.getOnline();
                if(online_status !=null && online_status.length()>0 && !online_status.contentEquals("null"))
                {
                    if(!cr.getOnline().contentEquals(online_status))
                    {
                        finish();
                        startActivity(new Intent(getApplicationContext(), Dispatch_chathome.class));
                    }
                }
                cr.setLastMessage(message.getMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);


                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * fetching the chat rooms by making http call
     */
    private void fetchChatRooms() {

       //        StringRequest strReq = new StringRequest(Request.Method.GET,
//                EndPoints.CHAT_ROOMS+"/"+MyApplication.getInstance().getPrefManager().getUser().getId(), new Response.Listener<String>() {

        String cmpcode=pref.getString(Constant.COMPANY_CODE).trim();

      String driverid=pref.getString(Constant.DRIVER_ID);
        Log.e("chroomkk",""+DispatcherEndPoints.CHAT_ROOMS+"/"+driverid+"/"+cmpcode);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                DispatcherEndPoints.CHAT_ROOMS+"/"+driverid+"/"+cmpcode, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("rg", "response: " + response);
                dialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
//Log.e("obj","*"+obj.toString());
                  //  Log.e("objer","*"+obj.getBoolean("error"));
                    // check for error flag

                    if (obj.getBoolean("error") == false) {
                      //  Log.e("##","*");
                        totcount=0;
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        //Log.e("chatRoomsArray","*"+chatRoomsArray.toString());
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                           // Log.e("1","*"+i);
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            Dispatch_chat_mod cr = new Dispatch_chat_mod();
                            cr.setId(chatRoomsObj.getString("chat_room_id"));
                            //Log.e("chat_room_id",""+chatRoomsObj.getString("chat_room_id"));

                           // Log.e("1",i+chatRoomsObj.getString("name"));
                            String skz=chatRoomsObj.getString("name");
                            cr.setName(skz);

                            String img_url=chatRoomsObj.getString("img_src");
                            cr.setImage(img_url);
                            //cr.setLastMessage("");
                            //cr.setUnreadCount(0);
                            cr.setOnline(chatRoomsObj.getString("online"));
                            cr.setTimestamp(chatRoomsObj.getString("created_at"));
                            cr.setUnreadCount(Integer.parseInt(chatRoomsObj.getString("unread_count")));

                            int scount=0;
                                    scount=Integer.parseInt(chatRoomsObj.getString("unread_count"));
                            Log.e("scontz",""+scount);
                            //cr.setUnreadCount(Integer.parseInt("335"));
                            String sk=chatRoomsObj.getString("last_message");
                            Log.e("sk","@"+sk);
                            if(sk !=null && sk.length()>0 && !sk.contentEquals("null"))
                            {
                                cr.setLastMessage(sk);
                            }
                            else
                            {
                                cr.setLastMessage("");
                            }
                            if(scount !=0)
                            {
                               // Log.e("scount","@"+scount);
                               // Log.e("sk","@"+sk);
                                //Log.e("totcount","&"+totcount);
                                totcount+=scount;
                                unreadchatroom.add(cr);
                               // Log.e("totcount","&"+totcount);
                                if (totcount == 0) {
                                    txtchatcount.setVisibility(View.INVISIBLE);
                                    // imgchathead.setVisibility(View.INVISIBLE);
                                } else {
                                   // Log.e("totcounttotcount","&"+totcount);
                                    txtchatcount.setText("" + totcount);
                                    txtchatcount.setVisibility(View.VISIBLE);
                                    // imgchathead.setVisibility(View.VISIBLE);
                                }
                            }
                            chatRoomArrayList.add(cr);
                            Log.e("chatRoomArrayList","*"+chatRoomArrayList.toString());
                        }

                    } else {
                        dialog.dismiss();
                        Log.e("chroomerrsss",""+obj.getJSONObject("error").getString("message"));
                        // error in fetching chat rooms
                        totcount=0;
                      //  Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    dialog.dismiss();
                    Log.e("kk", "json parsing error: " + e.getMessage());
                   // Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mAdapter.notifyDataSetChanged();

                // subscribing to all chat room topics
                subscribeToAllTopics();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("h", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
               // Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        //Log.e("strReq",""+strReq);
        Dispat_Application.getInstance().addToRequestQueue(strReq);
    }

    // subscribing to global topic
    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(this, Dispatch_GcmIntentService.class);
        intent.putExtra(Dispatch_GcmIntentService.KEY, Dispatch_GcmIntentService.SUBSCRIBE);
        intent.putExtra(Dispatch_GcmIntentService.TOPIC, DispatchConfig.TOPIC_GLOBAL);
        startService(intent);
    }

    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {
        for (Dispatch_chat_mod cr : chatRoomArrayList) {

            Intent intent = new Intent(this, Dispatch_GcmIntentService.class);
            intent.putExtra(Dispatch_GcmIntentService.KEY, Dispatch_GcmIntentService.SUBSCRIBE);
            intent.putExtra(Dispatch_GcmIntentService.TOPIC, "topic_" + cr.getId());
            startService(intent);
        }
    }

    private void launchLoginActivity() {

       // User us=new User();
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(DispatchConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(DispatchConfig.PUSH_NOTIFICATION));

        // clearing the notification tray
        DispatchNotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, Dispatch_GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
               // Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
////            case R.id.action_logout:
////                Dispat_Application.getInstance().logout();
////                break;
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }









    public void cancelnotificationdispatch()
    {

        final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mgr.cancel(NOTIFY_ME_ID_DISPATCH);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent mIntent2 = new Intent(Dispatch_chathome.this,
//                    Home_activity.class);
//            mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(mIntent2);
//            finish();
//        }else{
            finish();
//        }



    }
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
