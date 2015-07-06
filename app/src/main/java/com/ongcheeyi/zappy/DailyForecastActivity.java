package com.ongcheeyi.zappy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ongcheeyi.zappy.adapters.DayAdapter;
import com.ongcheeyi.zappy.weather.WeatherDaily;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DailyForecastActivity extends ListActivity {

    @Bind(R.id.locationLabel) TextView locationLabel;
    private WeatherDaily[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY);

        // get array of items from parcelable extra on the intent
        days = Arrays.copyOf(parcelables,parcelables.length,WeatherDaily[].class);

        DayAdapter adapter = new DayAdapter(this, days);
        setListAdapter(adapter);
        setLocation();
    }

    public void setLocation() {
        if (days.length > 0) {
            locationLabel.setText(days[0].getLocation());
        }
    }

}
