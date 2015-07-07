package com.ongcheeyi.zappy;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ongcheeyi.zappy.adapters.DayAdapter;
import com.ongcheeyi.zappy.weather.WeatherDaily;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DailyForecastActivity extends Activity {
    // if we extended ListViewActivity we needn't redefine listView, setListAdapter and onListItemClick

    @Bind(android.R.id.list) ListView listView; // our ListView in activity_daily_forecast.xml
    @Bind(android.R.id.empty) TextView emptyTextView;
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
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String day = days[position].getDay();
                String conditions = days[position].getSummary();
                String maxTemp = days[position].getMaxTemp() + "";
                String msg = String.format("On %s, the high will be %s and it will be %s.",
                        day, maxTemp, conditions.toLowerCase());
                Toast.makeText(DailyForecastActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
        //setListAdapter(adapter);
        setLocation();
    }

    public void setLocation() {
        if (days.length > 0) {
            locationLabel.setText(days[0].getLocation());
        }
    }

//    Below can be used if we were extending ListViewActivity
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        String day = days[position].getDay();
//        String conditions = days[position].getSummary();
//        String maxTemp = days[position].getMaxTemp() + "";
//        String msg = String.format("On %s, the high will be %s and it will be %s",
//                day, maxTemp, conditions.toLowerCase());
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//    }
}
