package com.ongcheeyi.zappy.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ongcheeyi.zappy.R;
import com.ongcheeyi.zappy.weather.WeatherDaily;

/**
 * Created by CheeYi on 7/5/15.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private WeatherDaily[] days;
    SharedPreferences sharedPref;
    boolean metric;

    public DayAdapter(Context context, WeatherDaily[] days) {
        mContext = context;
        this.days = days;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        metric = sharedPref.getBoolean("metric", false);
    }
    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // Tag items for easy reference
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View someView = LayoutInflater.from(mContext).inflate(R.layout.activity_daily_forecast, null);
        ViewHolder crap = new ViewHolder();
        crap.locationLabel = (TextView)someView.findViewById(R.id.locationLabel);

        if(convertView == null) {
            // new view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView)convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView)convertView.findViewById(R.id.weatherTextView);



            convertView.setTag(holder);
        } else {
            // views already set up, recycle!
            holder = (ViewHolder)convertView.getTag();
        }

        // populate UI with info of current day (at position position)
        WeatherDaily day = days[position];
        holder.iconImageView.setImageResource(day.getIconId());
        if(metric) {
            holder.temperatureLabel.setText(day.getCelsius()+"");
        }
        else {
            holder.temperatureLabel.setText(day.getMaxTemp() + "");
        }
        crap.locationLabel.setText(day.getLocation());
        if(position == 0) {
            // current day
            holder.dayLabel.setText("Today");
        } else { // use normal day name
            holder.dayLabel.setText(day.getDay());
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView; // public by default
        TextView temperatureLabel;
        TextView dayLabel;
        TextView locationLabel;
    }

}
