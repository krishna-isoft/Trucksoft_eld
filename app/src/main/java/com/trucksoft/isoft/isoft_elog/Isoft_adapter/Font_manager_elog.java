package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by isoft on 14/8/17.
 */

public class Font_manager_elog {
    private  static Hashtable<String,Typeface> font_icons=new Hashtable<>();
    public static Typeface get_icons(String path, Context context)
    {
        Typeface icons=font_icons.get(path);
        if(icons==null)
        {
            icons= Typeface.createFromAsset(context.getAssets(),path);
            font_icons.put(path,icons);
        }
        return  icons;
    }
}
