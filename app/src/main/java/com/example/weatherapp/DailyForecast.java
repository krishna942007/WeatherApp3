package com.example.weatherapp;

public class DailyForecast {
    private final String date;
    private final String temp;
    private final String iconUrl;

    public DailyForecast(String date, String temp, String iconUrl) {
        this.date = date;
        this.temp = temp;
        this.iconUrl = iconUrl;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}