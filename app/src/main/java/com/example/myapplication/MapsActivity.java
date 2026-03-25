package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity {
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Button backButton;

    // Класс для хранения информации о метке
    private static class MarkerInfo {
        Point point;
        String title;
        String description;

        MarkerInfo(double lat, double lng, String title, String description) {
            this.point = new Point(lat, lng);
            this.title = title;
            this.description = description;
        }
    }

    private List<MarkerInfo> markers = new ArrayList<>();
    // Хранилище для связи PlacemarkMapObject с его данными
    private Map<PlacemarkMapObject, MarkerInfo> markerDataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("6d4e6fe0-b0af-42d8-b8a1-8636ea55edff");
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapView);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        setupMap();
        addMarkers();

        // Добавляем глобальный обработчик нажатий на коллекцию
        setupTapListener();
    }

    private void setupMap() {
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 12, 0, 0)
        );
    }

    private void addMarkers() {
        // Добавляем все метки в список
        markers.add(new MarkerInfo(55.753215, 37.620393,
                "Красная площадь",
                "Красная площадь – главная площадь Москвы. Здесь находятся Собор Василия Блаженного, Исторический музей, Мавзолей Ленина."));

        markers.add(new MarkerInfo(55.751244, 37.618423,
                "Московский Кремль",
                "Московский Кремль – древнейшая часть столицы, резиденция Президента. Уникальный архитектурный ансамбль с соборами и дворцами."));

        markers.add(new MarkerInfo(55.744629, 37.605377,
                "Храм Христа Спасителя",
                "Храм Христа Спасителя – главный кафедральный собор России. Восстановлен в 1990-х годах. Высота 103 метра."));

        markers.add(new MarkerInfo(55.703911, 37.528587,
                "МГУ имени М.В. Ломоносова",
                "МГУ имени М.В. Ломоносова – главный университет страны. Одна из сталинских высоток, символ образования и науки."));

        markers.add(new MarkerInfo(55.731483, 37.603202,
                "Парк Горького",
                "Парк Горького – центральный парк Москвы. Любимое место отдыха москвичей и гостей столицы. Здесь есть велодорожки, кафе и зоны для пикников."));

        // Добавляем каждую метку на карту
        for (MarkerInfo marker : markers) {
            addMarkerToMap(marker);
        }
    }

    private void addMarkerToMap(MarkerInfo marker) {
        PlacemarkMapObject placemark = mapObjects.addPlacemark(marker.point);
        placemark.setIcon(ImageProvider.fromResource(this, android.R.drawable.star_on));

        // Сохраняем связь между меткой и данными в HashMap
        markerDataMap.put(placemark, marker);

        // Убираем индивидуальный tapListener
    }

    private void setupTapListener() {
        // Добавляем обработчик нажатий на всю коллекцию
        mapObjects.addTapListener((mapObject, point) -> {
            // Проверяем, что нажатый объект - это PlacemarkMapObject
            if (mapObject instanceof PlacemarkMapObject) {
                PlacemarkMapObject placemark = (PlacemarkMapObject) mapObject;
                MarkerInfo clickedMarker = markerDataMap.get(placemark);
                if (clickedMarker != null) {
                    showMarkerInfo(clickedMarker);
                    return true;
                }
            }
            return false;
        });
    }

    private void showMarkerInfo(MarkerInfo marker) {
        new AlertDialog.Builder(this)
                .setTitle(marker.title)
                .setMessage(marker.description)
                .setPositiveButton("Закрыть", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}