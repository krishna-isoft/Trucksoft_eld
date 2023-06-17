package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.TouchDelegate;
import android.view.View;

import java.util.Map;

public class DUtil {
    private static final String TAG = "Util";

    /* renamed from: com.keeptruckin.android.util.Util.1 */
    static class C03741 implements Runnable {
        final /* synthetic */ View val$bigView;
        final /* synthetic */ int val$extraPadding;
        final /* synthetic */ View val$smallView;

        C03741(View view, int i, View view2) {
            this.val$smallView = view;
            this.val$extraPadding = i;
            this.val$bigView = view2;
        }

        public void run() {
            Rect rect = new Rect();
            this.val$smallView.getHitRect(rect);
            rect.top -= this.val$extraPadding;
            rect.left -= this.val$extraPadding;
            rect.right += this.val$extraPadding;
            rect.bottom += this.val$extraPadding;
            this.val$bigView.setTouchDelegate(new TouchDelegate(rect, this.val$smallView));
        }
    }

  
        
    

    public static boolean isValidEmail(CharSequence emailAddress) {
        if (emailAddress == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    public static boolean isPhoneValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber);
    }

    public static void safePut(Map<String, String> map, String key, boolean value, boolean encode) {
        safePut((Map) map, key, String.valueOf(value), encode);
    }

    public static void safePut(Map<String, String> map, String key, int value, boolean encode) {
        safePut((Map) map, key, String.valueOf(value), encode);
    }

    public static void safePut(Map<String, String> map, String key, String value, boolean encode) {
        if (key != null && key.length() > 0 && value != null && value.length() > 0) {
            if (encode) {
                map.put(Uri.encode(key), Uri.encode(value));
            } else {
                map.put(key, value);
            }
        }
    }

    public static Bitmap getResizedBitmapFromWidth(Bitmap bitmap, float newWidth) {
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float newHeight = height * (newWidth / width);
        if (newWidth == width) {
            newHeight = height;
        }
        return Bitmap.createScaledBitmap(bitmap, (int) newWidth, (int) newHeight, false);
    }

    public static Bitmap getResizedBitmapFromHeight(Bitmap bitmap, float newHeight) {
        return Bitmap.createScaledBitmap(bitmap, (int) (((float) bitmap.getWidth()) * (newHeight / ((float) bitmap.getHeight()))), (int) newHeight, false);
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, float newWidth, float newHeight) {
        return Bitmap.createScaledBitmap(bitmap, (int) newWidth, (int) newHeight, false);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        int heightRatio = Math.round(((float) height) / ((float) reqHeight));
        int widthRatio = Math.round(((float) width) / ((float) reqWidth));
        if (heightRatio < widthRatio) {
            return heightRatio;
        }
        return widthRatio;
    }

    public static void safePutAppend(Map<String, String> map, String key, String value, boolean encode) {
        if (key != null && key.length() > 0 && value != null && value.length() > 0) {
            if (map.containsKey(key)) {
                value = ((String) map.get(key)) + "&" + key + "=" + value;
            }
            if (encode) {
                map.put(Uri.encode(key), Uri.encode(value));
            } else {
                map.put(key, value);
            }
        }
    }

    public static boolean isPointInCircle(float x, float y, Point point, int radius) {
        return Math.pow((double) (x - ((float) point.x)), 2.0d) + Math.pow((double) (y - ((float) point.y)), 2.0d) <= Math.pow((double) radius, 2.0d);
    }

    public static boolean isPointInCircle(float x, float y, float circleX, float circleY, int radius) {
        return Math.pow((double) (x - circleX), 2.0d) + Math.pow((double) (y - circleY), 2.0d) <= Math.pow((double) radius, 2.0d);
    }

   
   /* public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }*/
}
