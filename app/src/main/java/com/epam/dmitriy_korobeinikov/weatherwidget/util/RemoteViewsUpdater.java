package com.epam.dmitriy_korobeinikov.weatherwidget.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.epam.dmitriy_korobeinikov.weatherwidget.R;
import com.epam.dmitriy_korobeinikov.weatherwidget.WeatherWidgetProvider;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.Constants;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.CurrentWeather;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Dmitriy_Korobeinikov on 12/3/2015.
 */
public final class RemoteViewsUpdater {

    private final Context mContext;

    private final int[] mTomorrowIconIds = {R.id.ivTomorrowIcon, R.id.ivAfterTomorrowIcon, R.id.iv2AfterTomorrowIcon};
    private final int[] mTomorrowDayIds = {R.id.tvDayTomorrow, R.id.tvDayAfterTomorrow, R.id.tv2DayAfterTomorrow};
    private final int[] mTomorrowTempIds = {R.id.tvTomorrowTemp, R.id.tvAfterTomorrowTemp, R.id.tv2AfterTomorrowTemp};

    public RemoteViewsUpdater(Context context) {
        mContext = context;
    }

    public void showProgress(AppWidgetManager manager, RemoteViews remoteViews, int widgetId, boolean isShown) {
        remoteViews.setViewVisibility(R.id.pbRefresh, isShown ? View.VISIBLE : View.GONE);
        remoteViews.setViewVisibility(R.id.btnRefresh, isShown ? View.GONE : View.VISIBLE);
        manager.updateAppWidget(widgetId, remoteViews);
    }

    public void updateCurrentWeather(RemoteViews remoteViews, CurrentWeather weather) {
        remoteViews.setTextViewText(R.id.tvWeatherDescription, weather.getWeatherDescription().weatherDescription);
        String currentTemp = mContext.getString(R.string.current_temperature, weather.mainData.temp);
        remoteViews.setTextViewText(R.id.tvCurrentTemp, currentTemp);

        String currentWind = mContext.getString(R.string.current_wind, weather.wind.speed);
        remoteViews.setTextViewText(R.id.tvCurrentWind, currentWind);

        CharSequence currentTime = DateFormat.format(Constants.LAST_UPDATE_TIME_FORMAT, weather.lastUpdateTime * DateUtils.SECOND_IN_MILLIS);
        remoteViews.setTextViewText(R.id.tvLastUpdateTime, currentTime);

        remoteViews.setTextViewText(R.id.tvCity, weather.cityName);
        WeatherIconHelper.setWeatherIcon(remoteViews, weather.getWeatherDescription().iconCode, R.id.ivWeatherIcon);
    }

    public void updateForecast(RemoteViews remoteViews, List<CurrentWeather> weatherForecast) {
        CurrentWeather currentWeather;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);
        for (int i = 1; i < weatherForecast.size(); i++) {
            currentWeather = weatherForecast.get(i);
            WeatherIconHelper.setWeatherIcon(remoteViews, currentWeather.getWeatherDescription().iconCode, mTomorrowIconIds[i - 1]);

            String currentDay = ((String) DateFormat.format("EEE", currentWeather.lastUpdateTime * DateUtils.SECOND_IN_MILLIS)).toUpperCase();
            remoteViews.setTextViewText(mTomorrowDayIds[i - 1], currentDay);

            String minTemp = numberFormat.format(currentWeather.temperature.min);
            String maxTemp = numberFormat.format(currentWeather.temperature.max);
            String tempRange = mContext.getString(R.string.forecast_temperature_range, minTemp, maxTemp);
            remoteViews.setTextViewText(mTomorrowTempIds[i - 1], tempRange);
        }
    }

    public void setOnClickPendingIntents(RemoteViews remoteViews, int widgetId) {
        PendingIntent updatePendingIntent = getPendingWidgetIntent(WeatherWidgetProvider.ACTION_UPDATE_CURRENT_WEATHER, widgetId);
        PendingIntent openConfigPendingIntent = getPendingWidgetIntent(WeatherWidgetProvider.ACTION_CHANGE_CITY, widgetId);
        remoteViews.setOnClickPendingIntent(R.id.btnRefresh, updatePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.ivWeatherIcon, openConfigPendingIntent);
    }

    public PendingIntent getPendingWidgetIntent(String action, int widgetId) {
        Intent intent = new Intent(mContext, WeatherWidgetProvider.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        return PendingIntent.getBroadcast(mContext, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
