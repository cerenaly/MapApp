package com.example.mapapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Harita başlangıç noktasını ayarlayalım (Türkiye'nin koordinatları)
        GeoPoint startPoint = new GeoPoint(39.9334, 32.8597); // Türkiye'nin merkezi koordinatları
        mapView.getController().setCenter(startPoint);  // Haritayı bu noktaya yakınlaştır
        mapView.getController().setZoom(6);  // Zoom seviyesini ayarla (yakınlaştır)
        // Harita kaydırılabilir alan sınırlarını ayarla
        mapView.setScrollableAreaLimitLatitude(39.0, 37.0, 0); // Latitude sınırları
        mapView.setScrollableAreaLimitLongitude(20.0, 50.0, 0); // Longitude sınırları



        // Retrofit ile Overpass API'ye bağlanalım
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://overpass-api.de/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OverpassAPI api = retrofit.create(OverpassAPI.class);

        // Bornova'daki süpermarketleri sorgulamak için Overpass API sorgusu
        String query = "[out:json][timeout:25];area[\"name\"=\"Bornova\"];nwr[\"shop\"=\"supermarket\"](area);out center;";

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
        if (response != null && response.getElements() != null) {
            for (OverpassResponse.Element element : response.getElements()) {
                double lat = element.getLatitude();
                double lon = element.getLongitude();

                // Haritada her market için işaretçi ekle
                Marker marketMarker = new Marker(mapView);
                marketMarker.setPosition(new GeoPoint(lat, lon));
                marketMarker.setTitle("SuperMarketler");
                mapView.getOverlays().add(marketMarker);
            }
        }
    }
}
