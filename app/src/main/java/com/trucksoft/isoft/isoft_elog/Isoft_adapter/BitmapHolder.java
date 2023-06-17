package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

public class BitmapHolder {
    private static final String TAG = "BitmapHolder";
    private static HashMap<String, Bitmap> cachedBitmaps;

    static {
        cachedBitmaps = new HashMap();
    }

    public static Bitmap getBitmap(Resources resources, int width, int height, int resID) {
        String id = String.format("%d_%dx%d", new Object[]{Integer.valueOf(resID), Integer.valueOf(width), Integer.valueOf(height)});
        if (cachedBitmaps.get(id) == null) {
            Bitmap scaledBitmap;
            EUtil.start("createBitmap: " + id);
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resID);
            if (height == 0) {
                scaledBitmap = DUtil.getResizedBitmapFromWidth(bitmap, (float) width);
            } else if (width == 0) {
                scaledBitmap = DUtil.getResizedBitmapFromHeight(bitmap, (float) height);
            } else {
                scaledBitmap = DUtil.getResizedBitmap(bitmap, (float) width, (float) height);
            }
            cachedBitmaps.put(id, scaledBitmap);
            if (bitmap != scaledBitmap) {
                bitmap.recycle();
            }
            EUtil.stop("createBitmap: " + id);
        }
        return (Bitmap) cachedBitmaps.get(id);
    }

    public static Bitmap getBitmap(Resources resources, int resID) {
        String id = String.format("%d", new Object[]{Integer.valueOf(resID)});
        if (cachedBitmaps.get(id) == null) {
            EUtil.start("createBitmap: " + id);
            cachedBitmaps.put(id, BitmapFactory.decodeResource(resources, resID));
            EUtil.stop("createBitmap: " + id);
        }
        return (Bitmap) cachedBitmaps.get(id);
    }
}
