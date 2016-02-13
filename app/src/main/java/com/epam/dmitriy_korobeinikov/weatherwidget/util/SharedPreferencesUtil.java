package com.epam.dmitriy_korobeinikov.weatherwidget.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dmitriy_Korobeinikov on 12/2/2015.
 */
public final class SharedPreferencesUtil {
    public static final String PREF_CONFIG_COMPLETED = "_config_completed";
    public static final String PREF_CITY_ID = "_city_id";
    public static final String PREF_LAST_CHOSEN_CITY_POSITION = "_city_position";
    public static final String PREF_LAST_UPDATE_TIME = "_last_update_time";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void writeStringToPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    public static void writeBooleanToPref(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void writeIntToPref(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putInt(key, value);
        editor.apply();
    }

    public static void writeLongToPref(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putLong(key, value);
        editor.apply();
    }

    public static String getStringFromPref(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static boolean getBooleanFromPref(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public static int getIntFromPref(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(key, -1);
    }

    public static long getLongFromPref(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getLong(key, -1);
    }

    public static void clearPreferences(Context context) {
        getSharedPreferencesEditor(context).clear().apply();
    }
}
