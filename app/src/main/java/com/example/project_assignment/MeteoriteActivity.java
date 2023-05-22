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

        // fetch all data sent using the intent.
        Intent intent = getIntent();
        // unpack getParcelableExtra
        meteorite = (Meteorite) intent.getParcelableExtra("meteorite");

        TextView title = findViewById(R.id.textview_meteoriteTitle);
        TextView info = findViewById(R.id.textview_meteoriteInfo);

        title.setText(meteorite.name);
        info.setText(meteorite.getDescription());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    /**
     * Initialize a google maps mapview, and set the location and marker to the meteorites location.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Double.parseDouble(meteorite.getLatitude());
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(meteorite.getLatitude()), Double.parseDouble(meteorite.getLongitude()))).title("Marker"));
        float zoom = 4;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(meteorite.getLatitude()), Double.parseDouble(meteorite.getLongitude())), zoom));
    }
}