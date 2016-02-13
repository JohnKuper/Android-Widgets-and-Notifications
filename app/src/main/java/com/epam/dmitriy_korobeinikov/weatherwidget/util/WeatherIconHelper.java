package com.epam.dmitriy_korobeinikov.weatherwidget.util;

import android.support.annotation.IdRes;
import android.widget.RemoteViews;

import com.epam.dmitriy_korobeinikov.weatherwidget.R;

/**
 * Created by Dmitriy_Korobeinikov on 12/2/2015.
 */
public class WeatherIconHelper {
    private static final String CLEAR_SKY_DAY = "01d";
    private static final String CLEAR_SKY_NIGHT = "01n";
    private static final String FEW_CLOUDS_DAY = "02d";
    private static final String FEW_CLOUDS_NIGHT = "02n";
    private static final String SCATTERED_CLOUDS_DAY = "03d";
    private static final String SCATTERED_CLOUDS_NIGHT = "03n";
    private static final String BROKEN_CLOUDS_DAY = "04d";
    private static final String BROKEN_CLOUDS_NIGHT = "04n";
    private static final String SHOWER_RAIN_DAY = "09d";
    private static final String SHOWER_RAIN_NIGHT = "09n";
    private static final String RAIN_DAY = "10d";
    private static final String RAIN_NIGHT = "10n";
    private static final String THUNDERSTORM_DAY = "11d";
    private static final String THUNDERSTORM_NIGHT = "11n";
    private static final String SNOW_DAY = "13d";
    private static final String SNOW_NIGHT = "13n";
    private static final String MIST_DAY = "50d";
    private static final String MIST_NIGHT = "50n";

    public static RemoteViews setWeatherIcon(RemoteViews views, String iconCode, @IdRes int iconViewId) {
        views.setImageViewResource(iconViewId, getIconResourceId(iconCode));
        return views;
    }

    public static int getIconResourceId(String iconCode) {
        int weatherDrawableId;
        switch (iconCode) {
            case CLEAR_SKY_DAY:
                weatherDrawableId = R.drawable.ic_clear_sky_day;
                break;
            case CLEAR_SKY_NIGHT:
                weatherDrawableId = R.drawable.ic_clear_sky_night;
                break;
            case FEW_CLOUDS_DAY:
                weatherDrawableId = R.drawable.ic_few_clouds_day;
                break;
            case FEW_CLOUDS_NIGHT:
                weatherDrawableId = R.drawable.ic_few_clouds_night;
                break;
            case SCATTERED_CLOUDS_DAY:
            case SCATTERED_CLOUDS_NIGHT:
            case BROKEN_CLOUDS_DAY:
            case BROKEN_CLOUDS_NIGHT:
            case MIST_DAY:
            case MIST_NIGHT:
                weatherDrawableId = R.drawable.ic_scattered_clouds;
                break;
            case SHOWER_RAIN_DAY:
            case SHOWER_RAIN_NIGHT:
                weatherDrawableId = R.drawable.ic_shower_rain;
                break;
            case RAIN_DAY:
                weatherDrawableId = R.drawable.ic_rain_day;
                break;
            case RAIN_NIGHT:
                weatherDrawableId = R.drawable.ic_rain_night;
                break;
            case THUNDERSTORM_DAY:
            case THUNDERSTORM_NIGHT:
                weatherDrawableId = R.drawable.ic_thunderstorm;
                break;
            case SNOW_DAY:
                weatherDrawableId = R.drawable.ic_snow_day;
                break;
            case SNOW_NIGHT:
                weatherDrawableId = R.drawable.ic_snow_night;
                break;
            default:
                throw new RuntimeException("Unknown weather icon code: " + iconCode);
        }
        return weatherDrawableId;
    }
}
