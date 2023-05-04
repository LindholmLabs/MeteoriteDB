package com.example.project_assignment;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project_assignment.comparators.SortByLocation;
import com.example.project_assignment.comparators.SortByWeight;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements JsonTask.JsonTaskListener, LocationListener {

    private final String JSON_URL = "https://data.nasa.gov/resource/y77d-th95.json";
    private LocationManager locationManager;
    private SharedPreferences preference;
    private SharedPreferences.Editor preferenceEditor;

    private ArrayList<Meteorite> meteorites;

    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JsonTask(this).execute(JSON_URL);

        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        preferenceEditor = preference.edit();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //request permission to use location services: fine location and coarse location.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this, "The app needs access to location services.", Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onPostExecute(String json) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Meteorite>>() {}.getType();
        meteorites = gson.fromJson(json, type);



        for (Meteorite m : meteorites) {
            Log.d("Meteorite: ", m.toString());
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(this, meteorites);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("LATITUDE = " + location.getLatitude());

        preferenceEditor.putString("latitude", String.valueOf(location.getLatitude()));
        preferenceEditor.putString("longitude", String.valueOf(location.getLongitude()));
        preferenceEditor.apply();

        Collections.sort(meteorites, new SortByLocation(MainActivity.this));
        adapter.notifyDataSetChanged();
    }
}

