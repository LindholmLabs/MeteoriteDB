package com.example.project_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MeteoriteActivity extends AppCompatActivity implements OnMapReadyCallback {


    private Meteorite meteorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteorite);

        Intent intent = getIntent();
        meteorite = (Meteorite) intent.getParcelableExtra("meteorite");

        TextView title = findViewById(R.id.textview_meteoriteTitle);
        TextView info = findViewById(R.id.textview_meteoriteInfo);

        title.setText(meteorite.name);
        info.setText(meteorite.toString());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Double.parseDouble(meteorite.latitude);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(meteorite.latitude), Double.parseDouble(meteorite.longitude))).title("Marker"));
        float zoom = 4;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(meteorite.latitude), Double.parseDouble(meteorite.longitude)), zoom));
    }
}