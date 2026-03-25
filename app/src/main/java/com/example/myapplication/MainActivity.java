package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String currentCity = "Moscow";
    private TextView cityTextView;
    private TextView tempTextView;
    private TextView descriptionTextView;
    private TextView windTextView;
    private LinearLayout forecastContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = findViewById(R.id.city);
        tempTextView = findViewById(R.id.temperature);
        descriptionTextView = findViewById(R.id.description);
        windTextView = findViewById(R.id.wind);
        forecastContainer = findViewById(R.id.hourly_forecast_container);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentCity = query;
                fetchWeatherData(currentCity);
                fetchForecastData(currentCity);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Button memoriesButton = findViewById(R.id.memories_button);
        memoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });

        Button mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        // Загрузка данных по умолчанию
        fetchWeatherData(currentCity);
        fetchForecastData(currentCity);
    }

    private void fetchWeatherData(String city) {
        WeatherAPI.WeatherService service = WeatherAPI.getService();
        Call<WeatherResponse> call = service.getCurrentWeather(city, "metric", "ru", WeatherAPI.API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    updateUI(weather);
                } else {
                    Toast.makeText(MainActivity.this, "Город не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchForecastData(String city) {
        WeatherAPI.WeatherService service = WeatherAPI.getService();
        Call<ForecastResponse> call = service.getForecast(city, "metric", "ru", WeatherAPI.API_KEY);

        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful()) {
                    ForecastResponse forecast = response.body();
                    updateForecast(forecast);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ошибка загрузки прогноза", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WeatherResponse weather) {
        cityTextView.setText(weather.getCityName());
        tempTextView.setText(String.format("%.0f°C", weather.getMain().getTemp()));
        descriptionTextView.setText(weather.getWeather().get(0).getDescription());
        windTextView.setText(String.format("Ветер: %.1f м/с", weather.getWind().getSpeed()));
    }

    private void updateForecast(ForecastResponse forecast) {
        forecastContainer.removeAllViews();

        List<ForecastItem> list = forecast.getList();
        LayoutInflater inflater = LayoutInflater.from(this);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        int count = 0;
        for (ForecastItem item : list) {
            if (count >= 5) break;

            try {
                Date date = inputFormat.parse(item.getDateTime());
                String time = timeFormat.format(date);

                View view = inflater.inflate(R.layout.item_hourly_forecast, forecastContainer, false);

                TextView timeView = view.findViewById(R.id.time);
                TextView tempView = view.findViewById(R.id.temp);
                ImageView iconView = view.findViewById(R.id.icon);

                timeView.setText(time);
                tempView.setText(String.format("%.0f°C", item.getMain().getTemp()));

                String iconCode = item.getWeather().get(0).getIcon();
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                Glide.with(this).load(iconUrl).into(iconView);

                forecastContainer.addView(view);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}