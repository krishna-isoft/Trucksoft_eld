package com.trucksoft.isoft.isoft_elog.isoft_eloggcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;


public class NotificationUtilsoreo_elog extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.trucksoft.isoft.isoft_elog.ANDROID";
    public static final String IOS_CHANNEL_ID = "com.trucksoft.isoft.isoft_elog.IOS";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    public static final String IOS_CHANNEL_NAME = "IOS CHANNEL";
    Preference pref;

    public NotificationUtilsoreo_elog(Context base) {
        super(base);
        pref=Preference.getInstance(base);
        createChannels();
    }

    public void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            String status=pref.getString(Constant.STATUS_DD);
            Log.e("statussss",""+status);
//            try {
//                Uri alarmSound =null;
//                if(status.contentEquals("ON DUTY")) {
//                   alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/ondutyy");
//                }else if(status.contentEquals("DRIVING"))
//                {
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/drivv");
//                }else if(status.contentEquals("SLEEP"))
//                {
//                    Log.e("statusssskk",""+status);
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/sleepp");
//                }else if(status.contentEquals("OFF DUTY"))
//                {
//                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + getApplicationContext().getPackageName() + "/raw/ofdutyy");
//                }
//
//
////                // Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
////                r.play();
//
//                AudioAttributes att = new AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                        .build();
//                androidChannel.setSound(alarmSound,att);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(androidChannel);

            // create ios channel
            NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID,
                    IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            iosChannel.enableLights(true);
            iosChannel.enableVibration(true);
            iosChannel.setLightColor(Color.GRAY);
            iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(iosChannel);
        }
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public Notification.Builder getAndroidChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)

                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
    }

    public Notification.Builder getIosChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), IOS_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    }
