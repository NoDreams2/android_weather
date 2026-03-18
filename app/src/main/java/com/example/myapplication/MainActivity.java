package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupStaticData();

        findViewById(R.id.memories_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });
    }

    private void setupStaticData() {
        TextView city = findViewById(R.id.city);
        TextView temp = findViewById(R.id.temperature);
        TextView description = findViewById(R.id.description);
        TextView wind = findViewById(R.id.wind);

        city.setText("Москва");
        temp.setText("15°C");
        description.setText("Облачно с прояснениями");
        wind.setText("Ветер: 3 м/с");

        setupHourlyForecast();
    }

    private void setupHourlyForecast() {
        LinearLayout container = findViewById(R.id.hourly_forecast_container);
        container.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        String[] times = {"Сейчас", "12:00", "15:00", "18:00", "21:00"};
        int[] temps = {15, 17, 16, 14, 12};

        for (int i = 0; i < times.length; i++) {
            View view = inflater.inflate(R.layout.item_hourly_forecast, container, false);

            TextView time = view.findViewById(R.id.time);
            TextView temp = view.findViewById(R.id.temp);
            ImageView icon = view.findViewById(R.id.icon);

            time.setText(times[i]);
            temp.setText(temps[i] + "°");

            icon.setImageResource(R.drawable.ic_launcher_foreground);

            container.addView(view);
        }
    }
}