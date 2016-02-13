package com.epam.dmitriy_korobeinikov.weatherwidget.util;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import com.epam.dmitriy_korobeinikov.weatherwidget.retrofit.WidgetService;

/**
 * Created by Dmitriy_Korobeinikov on 12/4/2015.
 */
public final class ServiceHelper {
    private ServiceHelper() {
    }

    public static void startWidgetService(Context context, int widgetId, String cityId) {
        Intent weatherService = new Intent(context, WidgetService.class);
        weatherService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        weatherService.putExtra(SharedPreferencesUtil.PREF_CITY_ID, cityId);
        context.startService(weatherService);
    }
}
