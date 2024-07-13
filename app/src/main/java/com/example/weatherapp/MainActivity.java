package com.example.weatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private Button fetchWeatherButton;

    private TextView locationTextView;
    private TextView temperatureTextView;
    private TextView weatherConditionTextView;
    private TextView temperatureRangeTextView;

    private RecyclerView hourlyRecyclerView;
    private RecyclerView dailyRecyclerView;

    private HourlyForecastAdapter hourlyForecastAdapter;
    private DailyForecastAdapter dailyForecastAdapter;

    private final String API_KEY = "f4ca2ef9344960e56a6353f1648472df";
    private String CITY_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the VideoView in the layout
        VideoView videoView = findViewById(R.id.video_view);

        // Set the path to the video file in the res/raw directory
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.sunnyback;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Set the video to loop
        videoView.setOnCompletionListener(mp -> videoView.start());

        // Start the video automatically
        videoView.start();

        cityInput = findViewById(R.id.city_input);
        fetchWeatherButton = findViewById(R.id.fetch_weather_button);

        locationTextView = findViewById(R.id.location);
        temperatureTextView = findViewById(R.id.temperature);
        weatherConditionTextView = findViewById(R.id.weather_condition);
        temperatureRangeTextView = findViewById(R.id.temperature_range);

        hourlyRecyclerView = findViewById(R.id.hourly_recycler_view);
        dailyRecyclerView = findViewById(R.id.daily_recycler_view);

        hourlyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        hourlyForecastAdapter = new HourlyForecastAdapter(new ArrayList<>());
        dailyForecastAdapter = new DailyForecastAdapter(new ArrayList<>());

        hourlyRecyclerView.setAdapter(hourlyForecastAdapter);
        dailyRecyclerView.setAdapter(dailyForecastAdapter);

        fetchWeatherButton.setOnClickListener(v -> {
            CITY_NAME = cityInput.getText().toString().trim();
            if (!CITY_NAME.isEmpty()) {
                fetchWeatherData();
                fetchHourlyForecast();
                fetchDailyForecast();
            }
        });
    }

    private void fetchWeatherData() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    MainActivity.this.runOnUiThread(() -> {
                        try {
                            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();
                            JsonObject main = jsonObject.getAsJsonObject("main");
                            JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();

                            String temp = main.get("temp").getAsString() + "°C";
                            String tempMin = main.get("temp_min").getAsString() + "°C";
                            String tempMax = main.get("temp_max").getAsString() + "°C";
                            String condition = weather.get("description").getAsString();

                            locationTextView.setText(CITY_NAME);
                            temperatureTextView.setText(temp);
                            weatherConditionTextView.setText(condition);
                            temperatureRangeTextView.setText(tempMin + " ~ " + tempMax);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void fetchHourlyForecast() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + CITY_NAME + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    MainActivity.this.runOnUiThread(() -> {
                        try {
                            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();
                            JsonArray list = jsonObject.getAsJsonArray("list");

                            List<HourlyForecast> hourlyForecasts = new ArrayList<>();

                            for (int i = 0; i < 8; i++) {  // Get forecast for next 24 hours (8 * 3-hour intervals)
                                JsonObject forecast = list.get(i).getAsJsonObject();
                                JsonObject main = forecast.getAsJsonObject("main");
                                JsonObject weather = forecast.getAsJsonArray("weather").get(0).getAsJsonObject();

                                String time = forecast.get("dt_txt").getAsString().split(" ")[1];
                                String temp = main.get("temp").getAsString() + "°C";
                                String iconUrl = "https://openweathermap.org/img/wn/" + weather.get("icon").getAsString() + "@2x.png";

                                hourlyForecasts.add(new HourlyForecast(time, temp, iconUrl));
                            }

                            hourlyForecastAdapter.updateData(hourlyForecasts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void fetchDailyForecast() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + CITY_NAME + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    MainActivity.this.runOnUiThread(() -> {
                        try {
                            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();
                            JsonArray list = jsonObject.getAsJsonArray("list");

                            List<DailyForecast> dailyForecasts = new ArrayList<>();

                            for (int i = 0; i < list.size(); i++) {
                                JsonObject forecast = list.get(i).getAsJsonObject();
                                JsonObject temp = forecast.getAsJsonObject("main");
                                JsonObject weather = forecast.getAsJsonArray("weather").get(0).getAsJsonObject();

                                String date = forecast.get("dt_txt").getAsString().split(" ")[0];
                                String temperature = temp.get("temp").getAsString() + "°C";
                                String iconUrl = "https://openweathermap.org/img/wn/" + weather.get("icon").getAsString() + "@2x.png";

                                dailyForecasts.add(new DailyForecast(date, temperature, iconUrl));
                            }

                            dailyForecastAdapter.updateData(dailyForecasts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
