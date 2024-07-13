package com.example.weatherapp;
public class HourlyForecast {
    private final String time;
    private final String temp;
    private final String iconUrl;

    public HourlyForecast(String time, String temp, String iconUrl) {
        this.time = time;
        this.temp = temp;
        this.iconUrl = iconUrl;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}