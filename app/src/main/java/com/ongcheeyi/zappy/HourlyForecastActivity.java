package com.ongcheeyi.zappy;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ongcheeyi.zappy.R;
import com.ongcheeyi.zappy.weather.WeatherHourly;

import java.util.Arrays;

public class HourlyForecastActivity extends Activity {

    private WeatherHourly[] hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY);
        hours = Arrays.copyOf(parcelables,parcelables.length,WeatherHourly[].class);
    }
}
