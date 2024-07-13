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

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder> {

    private List<HourlyForecast> hourlyForecasts;

    public HourlyForecastAdapter(List<HourlyForecast> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_forecast, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        HourlyForecast forecast = hourlyForecasts.get(position);
        holder.hourlyTime.setText(forecast.getTime());
        holder.hourlyTemp.setText(forecast.getTemp());
        Picasso.get().load(forecast.getIconUrl()).into(holder.hourlyIcon);
    }

    @Override
    public int getItemCount() {
        return hourlyForecasts.size();
    }

    public void updateData(List<HourlyForecast> newForecasts) {
        hourlyForecasts = newForecasts;
        notifyDataSetChanged();
    }

    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView hourlyTime, hourlyTemp;
        ImageView hourlyIcon;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            hourlyTime = itemView.findViewById(R.id.hourly_time);
            hourlyTemp = itemView.findViewById(R.id.hourly_temp);
            hourlyIcon = itemView.findViewById(R.id.hourly_icon);
        }
    }
}
