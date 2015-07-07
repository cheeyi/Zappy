package com.ongcheeyi.zappy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ongcheeyi.zappy.R;
import com.ongcheeyi.zappy.weather.WeatherHourly;

/**
 * Created by CheeYi on 7/6/15.
 * NOTE: RecyclerView consolidates code from getView() that is typically needed in a ListView
 * Adapter implementation.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private WeatherHourly[] hours;
    private Context context;

    public HourAdapter(Context context, WeatherHourly[] hours) {
        this.hours = hours;
        this.context = context;

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

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView timeLabel, summaryLabel, tempLabel;
        public ImageView iconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            timeLabel = (TextView)itemView.findViewById(R.id.timeLabel);
            summaryLabel = (TextView)itemView.findViewById(R.id.summaryLabel);
            tempLabel = (TextView)itemView.findViewById(R.id.tempLabel);
            iconImageView = (ImageView)itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(this);
        }

        // bind data to view
        public void bindHour(WeatherHourly hour) {
            timeLabel.setText(hour.getHour());
            summaryLabel.setText(hour.getSummary());
            tempLabel.setText(hour.getTemp()+"");
            iconImageView.setImageResource(hour.getIconId());
        }

        @Override
        public void onClick(View v) {
            String time = timeLabel.getText().toString();
            String temp = tempLabel.getText().toString();
            String summary = summaryLabel.getText().toString();
            String msg = String.format("At %s, it will be %s and %s.", time, temp, summary.toLowerCase());

            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

        }
    }

}
