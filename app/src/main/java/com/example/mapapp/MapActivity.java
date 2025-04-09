package com.example.mapapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private EditText locationInput;
    private Button searchButton;
    private List<Marker> markers = new ArrayList<>(); // Marker'ları saklayacak liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_map);

        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // EditText ve Button
        locationInput = findViewById(R.id.locationInput);
        searchButton = findViewById(R.id.searchButton);

// Button'a tıklama olayını ekleyelim
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationName = locationInput.getText().toString();
                if (!locationName.isEmpty()) {
                    searchSupermarkets(locationName);
                } else {
                    Toast.makeText(MapActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Harita başlangıç noktasını ayarlayalım (Türkiye'nin koordinatları)
        GeoPoint startPoint = new GeoPoint(39.9334, 32.8597); // Türkiye'nin merkezi koordinatları
        mapView.getController().setCenter(startPoint);  // Haritayı bu noktaya yakınlaştır
        mapView.getController().setZoom(7);  // Zoom seviyesini ayarla (yakınlaştır)
        // Harita kaydırılabilir alan sınırlarını ayarla
        //mapView.setScrollableAreaLimitLatitude(39.0, 37.0, 0); // Latitude sınırları
        //mapView.setScrollableAreaLimitLongitude(20.0, 50.0, 0); // Longitude sınırları

    }
    private void searchSupermarkets(String locationName) {
        // Kullanıcıdan alınan yeri Overpass API sorgusuna yerleştirelim
        String query = "[out:json][timeout:25];area[\"name\"=\"" + locationName + "\"];nwr[\"shop\"=\"supermarket\"](area);out center;";

        // Retrofit ile Overpass API'ye bağlanalım
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://overpass-api.de/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OverpassAPI api = retrofit.create(OverpassAPI.class);

        // API'ye sorguyu gönder
        Call<OverpassResponse> call = api.getSupermarkets(query);

        call.enqueue(new Callback<OverpassResponse>() {
            @Override
            public void onResponse(Call<OverpassResponse> call, Response<OverpassResponse> response) {
                if (response.isSuccessful()) {
                    displayMarketsOnMap(response.body());
                } else {
                    Toast.makeText(MapActivity.this, "Failed to fetch data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OverpassResponse> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void displayMarketsOnMap(OverpassResponse response) {
        clearOldMarkers(markers); // Eski marker'ları temizle

        if (response != null && response.getElements() != null) {
            for (OverpassResponse.Element element : response.getElements()) {
                double lat = element.getLatitude();
                double lon = element.getLongitude();

                // JSON'dan gelen "name" değerini al
                String supermarketName = element.getTags() != null ? element.getTags().getName() : "Supermarket"; // Eğer name yoksa, varsayılan olarak "Supermarket" kullanılacak.

                // Haritada her market için işaretçi ekle
                Marker marketMarker = new Marker(mapView);
                marketMarker.setPosition(new GeoPoint(lat, lon));
                marketMarker.setTitle(supermarketName);
                mapView.getOverlays().add(marketMarker);
                markers.add(marketMarker);
            }
        }
    }
    // Eski marker'ları temizle
    private void clearOldMarkers(List markers) {
        // Remove all old markers from the map
        for (Object overlay : mapView.getOverlays()) {
            if (overlay instanceof Marker) {  // Check if the overlay is a Marker
                mapView.getOverlays().remove(overlay); // Remove the marker
            }
        }
        markers.clear(); // Clear the markers list
    }
}
