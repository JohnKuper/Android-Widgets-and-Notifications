package com.epam.dmitriy_korobeinikov.weatherwidget.retrofit;

import android.app.IntentService;
import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.epam.dmitriy_korobeinikov.weatherwidget.R;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.Constants;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.CurrentWeather;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.Forecast;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.NotificationHelper;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.RemoteViewsUpdater;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil;

import java.io.IOException;

import retrofit.Call;

import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_CITY_ID;
import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_LAST_UPDATE_TIME;

/**
 * Created by Dmitriy_Korobeinikov on 11/27/2015.
 */
public class WidgetService extends IntentService {
    public static final String TAG = WidgetService.class.getSimpleName();
    private static final String DAY_COUNT_FORECAST = "4";

    public WidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, ".onHandleIntent" + " has been called");
        updateWeather(intent);
    }

    private void updateWeather(Intent intent) {
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        String cityId = intent.getStringExtra(SharedPreferencesUtil.PREF_CITY_ID);

        WeatherAPI apiService = RetrofitHelper.createWeatherService(true);
        Call<CurrentWeather> weatherCall = apiService.getCurrentWeather(cityId, Constants.API_KEY);
        Call<Forecast> forecastCall = apiService.getForecast(cityId, DAY_COUNT_FORECAST, Constants.API_KEY);

        try {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.weather_widget);
            RemoteViewsUpdater remoteViewsUpdater = new RemoteViewsUpdater(this);
            remoteViewsUpdater.showProgress(widgetManager, remoteViews, widgetId, true);

            CurrentWeather weather = weatherCall.execute().body();
            Forecast forecast = forecastCall.execute().body();

            remoteViewsUpdater.updateCurrentWeather(remoteViews, weather);
            remoteViewsUpdater.updateForecast(remoteViews, forecast.dailyForecast);
            remoteViewsUpdater.setOnClickPendingIntents(remoteViews, widgetId);

            remoteViewsUpdater.showProgress(widgetManager, remoteViews, widgetId, false);

            if (isLastUpdateTimeChanged(weather, widgetId)) {
                sendNewWeatherNotification(weather, widgetId);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error during retrieving new weather data", e);
        }
    }

    private void sendNewWeatherNotification(CurrentWeather currentWeather, int widgetId) {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        Notification notification = notificationHelper.buildWeatherNotification(currentWeather, widgetId);
        notificationHelper.sendNotification(notification, widgetId);
    }

    private boolean isLastUpdateTimeChanged(CurrentWeather currentWeather, int widgetId) {
        long lastUpdateTime = SharedPreferencesUtil.getLongFromPref(this, widgetId + PREF_LAST_UPDATE_TIME);
        if (isCityChanged(currentWeather, widgetId) || lastUpdateTime != currentWeather.lastUpdateTime) {
            SharedPreferencesUtil.writeLongToPref(this, widgetId + PREF_LAST_UPDATE_TIME, currentWeather.lastUpdateTime);
            return true;
        }
        return false;
    }

    private boolean isCityChanged(CurrentWeather currentWeather, int widgetId) {
        String lastCityId = SharedPreferencesUtil.getStringFromPref(this, widgetId + PREF_CITY_ID);
        if (!lastCityId.equals(currentWeather.cityId)) {
            SharedPreferencesUtil.writeStringToPref(this, widgetId + PREF_CITY_ID, currentWeather.cityId);
            return true;
        }
        return false;
    }
}
