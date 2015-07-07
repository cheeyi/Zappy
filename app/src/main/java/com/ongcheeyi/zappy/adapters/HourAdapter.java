package com.ongcheeyi.zappy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ongcheeyi.zappy.R;
import com.ongcheeyi.zappy.weather.WeatherHourly;

/**
 * Created by CheeYi on 7/6/15.
 * NOTE: RecyclerView consolidates code from getView() that is typically needed in a ListView
 * Adapter implementation.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private WeatherHourly[] hours;

    public HourAdapter(WeatherHourly[] hours) {
        this.hours = hours;

    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        // Bridge between adapter and bind method below
        // Bind current hour located at position 'position'
        holder.bindHour(hours[position]);
    }

    @Override
    public int getItemCount() {
        return hours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {
        public TextView timeLabel, summaryLabel, tempLabel;
        public ImageView iconImageView;
        public HourViewHolder(View itemView) {
            super(itemView);

            timeLabel = (TextView)itemView.findViewById(R.id.timeLabel);
            summaryLabel = (TextView)itemView.findViewById(R.id.summaryLabel);
            tempLabel = (TextView)itemView.findViewById(R.id.tempLabel);
            iconImageView = (ImageView)itemView.findViewById(R.id.iconImageView);
        }

        // bind data to view
        public void bindHour(WeatherHourly hour) {
            timeLabel.setText(hour.getHour());
            summaryLabel.setText(hour.getSummary());
            tempLabel.setText(hour.getTemp()+"");
            iconImageView.setImageResource(hour.getIconId());
        }
    }

}
