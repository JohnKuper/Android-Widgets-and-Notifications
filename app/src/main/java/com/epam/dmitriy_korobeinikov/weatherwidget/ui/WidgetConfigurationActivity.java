package com.epam.dmitriy_korobeinikov.weatherwidget.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.weatherwidget.R;
import com.epam.dmitriy_korobeinikov.weatherwidget.model.City;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.ServiceHelper;
import com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_CITY_ID;
import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_CONFIG_COMPLETED;
import static com.epam.dmitriy_korobeinikov.weatherwidget.util.SharedPreferencesUtil.PREF_LAST_CHOSEN_CITY_POSITION;

public class WidgetConfigurationActivity extends AppCompatActivity {
    private static final String TAG = WidgetConfigurationActivity.class.getSimpleName();

    private String mCityId;
    private int mWidgetId;
    private int mLastChosenPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResult(RESULT_CANCELED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWidgetId = getWidgetIdFromIntent();
        mLastChosenPosition = SharedPreferencesUtil.getIntFromPref(this, mWidgetId + PREF_LAST_CHOSEN_CITY_POSITION);

        Spinner spinner = (Spinner) findViewById(R.id.spCities);
        ArrayAdapter<City> spinnerAdapter = new CitiesAdapter(this, getCities());
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) parent.getItemAtPosition(position);
                mCityId = city.id;
                mLastChosenPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //because Android has a very old bug, related to the method call AppWidgetProvider.onUpdate(), before the configuration is done,
                //we have to maintain some indicator, which will show the config status
                SharedPreferencesUtil.writeBooleanToPref(WidgetConfigurationActivity.this, mWidgetId + PREF_CONFIG_COMPLETED, true);
                SharedPreferencesUtil.writeIntToPref(WidgetConfigurationActivity.this, mWidgetId + PREF_LAST_CHOSEN_CITY_POSITION, mLastChosenPosition);

                sendSuccessResult();
                ServiceHelper.startWidgetService(WidgetConfigurationActivity.this, mWidgetId, mCityId);
                finish();
            }
        });

        if (mLastChosenPosition != -1) {
            spinner.setSelection(mLastChosenPosition);
        }
    }

    private void sendSuccessResult() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_OK, resultValue);
    }

    private int getWidgetIdFromIntent() {
        Intent openIntent = getIntent();
        Bundle extras = openIntent.getExtras();
        int appWidgetId = 0;
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        return appWidgetId;
    }

    private List<City> getCities() {
        List<City> cities = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            cities = Arrays.asList(mapper.readValue(this.getResources().openRawResource(R.raw.cities), City[].class));
        } catch (IOException e) {
            Log.e(TAG, "Error during reading the cities.json", e);
        }
        return cities;
    }

    private class CitiesAdapter extends ArrayAdapter<City> {
        private List<City> mCities;
        private LayoutInflater mInflater;

        public CitiesAdapter(Context context, List<City> objects) {
            super(context, 0, objects);
            mCities = objects;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            TextView townName = (TextView) v.findViewById(android.R.id.text1);
            townName.setText(mCities.get(position).name);
            return v;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            TextView townName = (TextView) v.findViewById(android.R.id.text1);
            townName.setText(mCities.get(position).name);
            return v;
        }

        @Override
        public City getItem(int position) {
            return mCities.get(position);
        }
    }
}
