/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_service;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.Dispat_Application;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_config.DispatchConfig;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_home.DispatchChatRoomActivity;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_home.Dispatch_chathome;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_message;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatchuser;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;

import org.json.JSONException;
import org.json.JSONObject;
public class Dispatch_GcmPushReceiver extends GcmListenerService {
   private static final String TAG = "mygcmpushreceiver";
    private DispatchNotificationUtils notificationUtils;
    Preference pref;
    private  Context context;
    NotificationUtilschatoreo mNotificationUtils;
    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        //Log.e( "Fromkd: ","" + "From: ");
        context=this;
        pref=Preference.getInstance(context);
        mNotificationUtils=new NotificationUtilschatoreo(context);
        //Log.e("login status1",""+pref.getString(Constant.LOGIN_CHECK));

        String title = bundle.getString("title");
        Boolean isBackground = Boolean.valueOf(bundle.getString("is_background"));
        String flag = bundle.getString("flag");
        String data = bundle.getString("data");
        //Log.e(TAG, "From: " + from);
        //Log.e(TAG, "title: " + title);
       // Log.e(TAG, "isBackground: " + isBackground);
        //Log.e(TAG, "flag: " + flag);
       // Log.e(TAG, "data: " + data);

        if (flag == null)
            return;
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        switch (Integer.parseInt(flag)) {
            case DispatchConfig.PUSH_TYPE_CHATROOM:
                processChatRoomPush(title, isBackground, data);
                break;
            case DispatchConfig.PUSH_TYPE_USER:
                processUserMessage(title, isBackground, data);
                //processChatRoomPush(title, isBackground, data);
                break;
        }
    }


    private void processChatRoomPush(String title, boolean isBackground, String data) {
      //  Log.e("push notification",""+"pushnotification");
        Log.e("datachat",""+data.toString());
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);
                String chatRoomId = datObj.getString("chat_room_id");
               // Log.e("datObj0o",""+"datObj"+datObj.toString());
                JSONObject mObj = datObj.getJSONObject("message");
                Dispatch_message message = new Dispatch_message();//
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setCreatedAt(mObj.getString("created_at"));
                message.setonline(mObj.getString("disp_online"));
                message.setattachment(mObj.getString("attachment"));
                message.setfile(mObj.getString("file"));

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario
                if (uObj.getString("user_id").equals(Dispat_Application.getInstance().getPrefManager().getUser().getId())) {
                   // Log.e("dd", "Skipping the push message as it belongs to same user");
                    return;
                }

                Dispatchuser user = new Dispatchuser();
                user.setId(uObj.getString("user_id"));
                user.setEmail(uObj.getString("email"));
                user.setName(uObj.getString("name"));
                message.setUser(user);

                context=this;
                pref=Preference.getInstance(context);
                mNotificationUtils=new NotificationUtilschatoreo(context);

                String logststus=pref.getString(Constant.LOGIN_CHECK).trim();

                if(logststus.contentEquals("logged_inn")) {
                    // verifying whether the app is in background or foreground
                    if (!DispatchNotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        // Log.e("1z",""+"a................");
                        // app is in foreground, broadcast the push message
                        Intent pushNotification = new Intent(DispatchConfig.PUSH_NOTIFICATION);
                        pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_USER);
                        pushNotification.putExtra("message", message);
                        pushNotification.putExtra("chat_room_id", chatRoomId);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                        // play notification sound
                        DispatchNotificationUtils notificationUtils = new DispatchNotificationUtils();
                        notificationUtils.playNotificationSound();


                    } else {
                         //Log.e("2z",""+"bbbbbbbbbbbbbb");
                        if(title !=null && title.length()>0 && !title.contentEquals("null")) {
                            if (title.contentEquals("read_status")) {
                                //Log.e("kk", "not call notification");
                            }else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Notification.Builder nb = new Notification.Builder(this);
                                    Intent intent = new Intent(this, Dispatch_chathome.class);
                                    intent.putExtra("chat_room_id", chatRoomId);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
                                    nb = mNotificationUtils.
                                            getAndroidChannelNotification("Trucksoft-Dispatcher", "" + user.getName() + " : " + message.getMessage() + "       " + message.getCreatedAt());
                                    nb.setContentIntent(pendingIntent);
                                    mNotificationUtils.getManager().notify(102, nb.build());

                                } else {
                                   // Log.e("dddd",""+"bbbbbbbbbbbbbb");
                                    Intent resultIntent;
                                    pref.putInt(Constant.CHAT_COUNT, 1);
                                    resultIntent = new Intent(getApplicationContext(), Dispatch_chathome.class);
                                    resultIntent.putExtra("chat_room_id", chatRoomId);
                                    showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                                }
                            }
                        }else
                        {
                           // Log.e("2z",""+"cccccccccccc");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Notification.Builder nb = new Notification.Builder(this);
                                Intent intent = new Intent(this, Dispatch_chathome.class);
                                intent.putExtra("chat_room_id", chatRoomId);
                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
                                nb = mNotificationUtils.
                                        getAndroidChannelNotification("Trucksoft-Dispatcher", "" + user.getName() + " : " + message.getMessage() + "       " + message.getCreatedAt());
                                nb.setContentIntent(pendingIntent);
                                mNotificationUtils.getManager().notify(102, nb.build());

                            } else {
                               // Log.e("2zzzzzzzzzzzzz",""+"cccccccccccc");
                                Intent resultIntent;
                                pref.putInt(Constant.CHAT_COUNT, 1);
                                resultIntent = new Intent(getApplicationContext(), Dispatch_chathome.class);
                                resultIntent.putExtra("chat_room_id", chatRoomId);
                                showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                            }
                        }
                        }
                }
            } catch (JSONException e) {
                //Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
          //  Log.e("3",""+"c");
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }











    /**
     * Processing user specific push message
     * It will be displayed with / without image in push notification tray
     * */
    private void processUserMessage(String title, boolean isBackground, String data){
       Log.e("push notification@@@",""+"pushnotification");
        Log.e("data",""+data.toString());
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);
                Log.e("obj",""+datObj.toString());
               // Log.e("title","@"+title);

                JSONObject mfj = datObj.getJSONObject("user");
                String chatRoomId = mfj.getString("user_id");
               // Log.e("chatRoomId","#"+chatRoomId);
                JSONObject mObj = datObj.getJSONObject("message");
                Dispatch_message message = new Dispatch_message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setonline(mObj.getString("disp_online"));
                message.setattachment(mObj.getString("attachment"));
                message.setfile(mObj.getString("file"));

                if(mObj.has("chstatus")) {
                    message.setchstatus(mObj.getString("chstatus"));
                }else
                {
                    message.setchstatus("0");
                }

                message.setCreatedAt(mObj.getString("created_at"));

                JSONObject uObj = datObj.getJSONObject("user");

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario
//                if (uObj.getString("user_id").equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {
//                    //Log.e("dd", "Skipping the push message as it belongs to same user");
//                    return;
//                }

                Dispatchuser user = new Dispatchuser();
                user.setId(uObj.getString("user_id"));
                user.setEmail(uObj.getString("email"));
                user.setName(uObj.getString("name"));
                message.setUser(user);

                context=this;
                pref=Preference.getInstance(context);

                String logststus=pref.getString(Constant.LOGIN_CHECK).trim();

                if(logststus.contentEquals("logged_inn")) {
                    if (!DispatchNotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        // Log.e("1",""+"a");
                      // Log.e("2",""+"b................");
                        // app is in foreground, broadcast the push message
                        Intent pushNotification = new Intent(DispatchConfig.PUSH_NOTIFICATION);
                        pushNotification.putExtra("type", DispatchConfig.PUSH_TYPE_CHATROOM);
                        pushNotification.putExtra("message", message);
                        pushNotification.putExtra("chat_room_id", chatRoomId);
                        pushNotification.putExtra("rtitle", ""+title);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
String cc=pref.getString(Constant.COMPANY_CODE);


if(title !=null && title.length()>0 && !title.contentEquals("null")) {
if(title.contentEquals("read_status"))
{
    //Log.e("kk","not call notification");
}else {
    pref.putInt(Constant.CHAT_COUNT, 1);
   // Log.e("33",""+"aaaaaa");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
       // Log.e("call",""+"oreo");
        Notification.Builder nb=new Notification.Builder(this);
        Intent intent = new Intent(this, Dispatch_chathome.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);


        nb= mNotificationUtils.
                getAndroidChannelNotification("Trucksoft-Dispatcher", "" + user.getName() + " : " + message.getMessage()+"       "+ message.getCreatedAt() );
        nb.setContentIntent(pendingIntent);
        //nb.setDefaults(0);
        //nb.setOngoing(true);
        mNotificationUtils.getManager().notify(102, nb.build());

    }else {
        //Log.e("call",""+"below oreo");
        DispatchNotificationUtils notificationUtils = new DispatchNotificationUtils();
        notificationUtils.playNotificationSound();
        Intent resultIntent;

        resultIntent = new Intent(getApplicationContext(), Dispatch_chathome.class);


        //Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
        resultIntent.putExtra("chat_room_id", chatRoomId);
        // Log.e("message1fx",""+message.getMessage());
        showNotificationMessage(getApplicationContext(), "Trucksoft-Dispatcher", user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
    }
}
//
}else
{
    //Log.e("33",""+"cccc");
    pref.putInt(Constant.CHAT_COUNT, 1);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //Log.e("call",""+"oreo1");
        Notification.Builder nb=new Notification.Builder(this);
        Intent intent = new Intent(this, Dispatch_chathome.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);


        nb= mNotificationUtils.
                getAndroidChannelNotification("Trucksoft-Dispatcher", "" + user.getName() + " : " + message.getMessage()+"       "+ message.getCreatedAt() );
        nb.setContentIntent(pendingIntent);
        //nb.setDefaults(0);
        //nb.setOngoing(true);
        mNotificationUtils.getManager().notify(102, nb.build());

    }else {
        //Log.e("call",""+"below oreo1");
        DispatchNotificationUtils notificationUtils = new DispatchNotificationUtils();
        notificationUtils.playNotificationSound();
        Intent resultIntent;

        resultIntent = new Intent(getApplicationContext(), Dispatch_chathome.class);


        //Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
        resultIntent.putExtra("chat_room_id", chatRoomId);
        // Log.e("message1fx",""+message.getMessage());
        showNotificationMessage(getApplicationContext(), "Trucksoft-Dispatcher", user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
    }
}


                    } else {
                        if(title !=null && title.length()>0 && !title.contentEquals("null")) {
                            if (title.contentEquals("read_status")) {
                               // Log.e("kk", "not  notification");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    //Log.e("call",""+"oreo1");
                                    Notification.Builder nb=new Notification.Builder(this);
                                    Intent intent = new Intent(this, Dispatch_chathome.class);

                                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);


                                    nb= mNotificationUtils.
                                            getAndroidChannelNotification("Trucksoft-Dispatcher", "" + user.getName() + " : " + message.getMessage()+"       "+ message.getCreatedAt() );
                                    nb.setContentIntent(pendingIntent);
                                    //nb.setDefaults(0);
                                    //nb.setOngoing(true);
                                    mNotificationUtils.getManager().notify(102, nb.build());

                                }else {
                                   // Log.e("3", "" + "c................." + title);
                                    pref.putInt(Constant.CHAT_COUNT, 1);
                                    Intent resultIntent = new Intent(getApplicationContext(), DispatchChatRoomActivity.class);
                                    resultIntent.putExtra("chat_room_id", chatRoomId);
                                    showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                                }
                            }
                        }else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                //Log.e("call",""+"oreo1");
                                Notification.Builder nb=new Notification.Builder(this);
                                Intent intent = new Intent(this, Dispatch_chathome.class);

                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);


                                nb= mNotificationUtils.
                                        getAndroidChannelNotification("Trucksoft-Dispatcher", "" + user.getName() + " : " + message.getMessage()+"       "+ message.getCreatedAt() );
                                nb.setContentIntent(pendingIntent);
                                //nb.setDefaults(0);
                                //nb.setOngoing(true);
                                mNotificationUtils.getManager().notify(102, nb.build());

                            }else {
                                pref.putInt(Constant.CHAT_COUNT, 1);
                                Intent resultIntent = new Intent(getApplicationContext(), DispatchChatRoomActivity.class);
                                resultIntent.putExtra("chat_room_id", chatRoomId);
                                showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                Log.e("gg", "json parsing error: " + e.getMessage());
               // Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
           // Log.e("3",""+"c");
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }
    /*{
        Log.e("push notification",""+"pushnotification");
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);
                Log.e("push notification11",""+"pushnotification11");
                Log.e("datObj",""+"datObj"+datObj.toString());
                String imageUrl = datObj.getString("image");

                JSONObject mObj = datObj.getJSONObject("message");
                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                String chatRoomId = mObj.getString("chat_room_id");
                Log.e("chatRoomId",""+chatRoomId);
                message.setId(mObj.getString("message_id"));
                message.setCreatedAt(mObj.getString("created_at"));

                JSONObject uObj = datObj.getJSONObject("user");
                User user = new User();
                user.setId(uObj.getString("user_id"));
                user.setEmail(uObj.getString("email"));
                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("chat_room_id", chatRoomId);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils();
                    notificationUtils.playNotificationSound();
                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                    // check for push notification image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                    } else {
                        // push notification contains image
                        // show it with the image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message.getMessage(), message.getCreatedAt(), resultIntent, imageUrl);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
              //  Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {

            Log.e("push notification",""+"@@@@@@@@@@@@@@@@@@@@@@@@");
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }*/

    /**
     * Showing notification with text only
     * */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
       // Log.e("cd",""+"dnotiff");

        notificationUtils = new DispatchNotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     * */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new DispatchNotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
