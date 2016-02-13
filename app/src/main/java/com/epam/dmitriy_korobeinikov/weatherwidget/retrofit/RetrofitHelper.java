package com.epam.dmitriy_korobeinikov.weatherwidget.retrofit;

import com.epam.dmitriy_korobeinikov.weatherwidget.model.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Dmitriy_Korobeinikov on 11/27/2015.
 */
public final class RetrofitHelper {

    public static WeatherAPI createWeatherService(boolean isLoggerOn) {
        ObjectMapper mapper = new ObjectMapper();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_WEATHER_API_URL)
                .addConverterFactory(JacksonConverterFactory.create(mapper));
        if (isLoggerOn) {
            builder.client(createOkHttpClient());
        }
        Retrofit retrofit = builder.build();
        return retrofit.create(WeatherAPI.class);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        return client;
    }
}
