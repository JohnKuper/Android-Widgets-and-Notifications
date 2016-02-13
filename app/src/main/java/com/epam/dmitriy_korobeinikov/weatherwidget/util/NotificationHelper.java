package com.epam.dmitriy_korobeinikov.weatherwidget.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.epam.dmitriy_korobeinikov.weatherwidget.R;
import com.epam.dmitriy_korobeinikov.weatherwidget.WeatherWidgetProvider;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.Constants;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.CurrentWeather;

/**
 * Created by Dmitriy_Korobeinikov on 12/4/2015.
 */
public class NotificationHelper {

    private Context mContext;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public Notification buildWeatherNotification(CurrentWeather currentWeather, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.weather_notification);
        WeatherIconHelper.setWeatherIcon(remoteViews, currentWeather.getWeatherDescription().iconCode, R.id.ivNFIcon);
        remoteViews.setTextViewText(R.id.tvNFTemp, mContext.getString(R.string.current_temperature, currentWeather.mainData.temp));
        remoteViews.setTextViewText(R.id.tvNFWeatherDescription, currentWeather.getWeatherDescription().weatherDescription);
        remoteViews.setTextViewText(R.id.tvNFCity, currentWeather.cityName);

        String lastUpdateTime = mContext.getString(R.string.last_update_time_notification, DateFormat.format(Constants.LAST_UPDATE_TIME_FORMAT,
                currentWeather.lastUpdateTime * 1000));
        remoteViews.setTextViewText(R.id.tvNFLastUpdated, lastUpdateTime);

        RemoteViewsUpdater remoteViewsUpdater = new RemoteViewsUpdater(mContext);
        PendingIntent openConfigIntent = remoteViewsUpdater.getPendingWidgetIntent(WeatherWidgetProvider.ACTION_CHANGE_CITY, widgetId);
        remoteViews.setOnClickPendingIntent(R.id.btnNFOpenConfig, openConfigIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(WeatherIconHelper.getIconResourceId(currentWeather.getWeatherDescription().iconCode))
                .setContentTitle("My notification")
                .setContentText("Good weather")
                .setContent(remoteViews)
                .setAutoCancel(true);

        return builder.build();
    }

    public void sendNotification(Notification notification, int widgetId) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(widgetId, notification);
    }
}
