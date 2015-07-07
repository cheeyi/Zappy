package com.ongcheeyi.zappy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by CheeYi on 7/6/15.
 * NOTE: RecyclerView consolidates code from getView() that is typically needed in a ListView
 * Adapter implementation.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {
        public TextView timeLabel, summaryLabel, tempLabel;
        public ImageView iconImageView;
        public HourViewHolder(View itemView) {
            super(itemView);
        }
    }

}
