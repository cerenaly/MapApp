package com.example.mapapp;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map);

        // MapActivity'yi başlat
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
        finish(); // MainActivity'yi bitir
    }
}
