package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyViewHolder> {

    private List<DailyForecast> dailyForecasts;

    public DailyForecastAdapter(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_forecast, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyForecast forecast = dailyForecasts.get(position);
        holder.dailyDate.setText(forecast.getDate());
        holder.dailyTemp.setText(forecast.getTemp());
        Picasso.get().load(forecast.getIconUrl()).into(holder.dailyIcon);
    }

    @Override
    public int getItemCount() {
        return dailyForecasts.size();
    }

    public void updateData(List<DailyForecast> newForecasts) {
        dailyForecasts = newForecasts;
        notifyDataSetChanged();
    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView dailyDate, dailyTemp;
        ImageView dailyIcon;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            dailyDate = itemView.findViewById(R.id.daily_date);
            dailyTemp = itemView.findViewById(R.id.daily_temp);
            dailyIcon = itemView.findViewById(R.id.daily_icon);
        }
    }
}
