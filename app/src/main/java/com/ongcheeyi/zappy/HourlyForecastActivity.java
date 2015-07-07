package com.ongcheeyi.zappy;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ongcheeyi.zappy.R;
import com.ongcheeyi.zappy.adapters.HourAdapter;
import com.ongcheeyi.zappy.weather.WeatherHourly;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends Activity {

    private WeatherHourly[] hours;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY);
        hours = Arrays.copyOf(parcelables,parcelables.length,WeatherHourly[].class);

        HourAdapter adapter = new HourAdapter(hours);
        recyclerView.setAdapter(adapter);

        // According to the developer API, we need a LayoutManager to position items inside RecyclerView
        // LinearLayoutManager shows items in a vertical or horizontal scrolling list.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true); // optimizes performance, no dynamic resizing, always same-sized
    }
}
