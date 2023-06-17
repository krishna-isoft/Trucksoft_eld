package com.trucksoft.isoft.isoft_elog.Multiused;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preference {

	private static final String Details = "details";

	private static Preference preference;
	private SharedPreferences sharedPreferences;

	private Preference(Context context) {
		sharedPreferences = context.getSharedPreferences(Details,
				Context.MODE_PRIVATE);

	}

	public static Preference getInstance(Context context) {
		if (preference == null) {
			preference = new Preference(context);
		}
		return preference;
	}

	public void putBolean(String key, boolean value) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putString(String key, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
//	public void putEx(String key, Set<String> value) {
//		Editor editor = sharedPreferences.edit();
//		editor.putStringSet(key, value);
//		editor.commit();
//
//	}

	public void putInt(String key, int value) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void putLong(String key, Long value) {
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public boolean getBolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}

	public String getString(String key) {
		return sharedPreferences.getString(key, null);
	}


	public int getInt(String key) {
		return sharedPreferences.getInt(key, 0);
	}

	public long getLong(String key) {
		return sharedPreferences.getLong(key, 0);
	}

//	public Set<String> getStringSet(String yourKey, Object o) {
//		return sharedPreferences.getStringSet("key", null);
//	}
}
