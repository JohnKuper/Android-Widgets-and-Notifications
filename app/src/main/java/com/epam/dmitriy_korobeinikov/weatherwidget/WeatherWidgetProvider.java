package com.epam.dmitriy_korobeinikov.weatherwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.weatherwidget.ui.WidgetConfigurationActivity;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.ServiceHelper;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_CITY_ID;
import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_CONFIG_COMPLETED;

/**
 * Created by Dmitriy_Korobeinikov on 11/26/2015.
 */
public class WeatherWidgetProvider extends AppWidgetProvider {
    private static final String TAG = WeatherWidgetProvider.class.getSimpleName();
    public static final String ACTION_UPDATE_CURRENT_WEATHER = "com.johnkuper.ACTION_UPDATE_CURRENT_WEATHER";
    public static final String ACTION_CHANGE_CITY = "com.johnkuper.ACTION_CHANGE_CITY";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            if (SharedPreferencesUtil.getBooleanFromPref(context, widgetId + PREF_CONFIG_COMPLETED)) {
                Log.i(TAG, ".onUpdate" + " has been called");
                String cityId = SharedPreferencesUtil.getStringFromPref(context, widgetId + PREF_CITY_ID);
                ServiceHelper.startWidgetService(context, widgetId, cityId);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int widgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        switch (intent.getAction()) {
            case ACTION_UPDATE_CURRENT_WEATHER:
                Log.i(TAG, ACTION_UPDATE_CURRENT_WEATHER + " has been received");
                String cityId = SharedPreferencesUtil.getStringFromPref(context, widgetId + PREF_CITY_ID);
                ServiceHelper.startWidgetService(context, widgetId, cityId);
                break;
            case ACTION_CHANGE_CITY:
                context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                Intent configIntent = new Intent(context, WidgetConfigurationActivity.class);
                configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                context.startActivity(configIntent);
                break;
        }
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, ".onDisabled" + " has been called");
        SharedPreferencesUtil.clearPreferences(context);
    }
}
