package com.example.project_assignment;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_assignment.comparators.SortByLocation;
import com.example.project_assignment.comparators.SortByName;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        FloatingActionButton showSortingOptionsButton = findViewById(R.id.showSortingOptionsButton);
        FloatingActionButton locationSortButton = findViewById(R.id.locationSortButton);
        FloatingActionButton nameSortButton = findViewById(R.id.nameSortButton);
        FloatingActionButton masSortButton = findViewById(R.id.masSortButton);
        showSortingOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationSortButton.getVisibility() == View.VISIBLE) {
                    locationSortButton.setVisibility(View.INVISIBLE);
                    nameSortButton.setVisibility(View.INVISIBLE);
                    masSortButton.setVisibility(View.INVISIBLE);
                } else {
                    locationSortButton.setVisibility(View.VISIBLE);
                    nameSortButton.setVisibility(View.VISIBLE);
                    masSortButton.setVisibility(View.VISIBLE);
                }
            }
        });

        locationSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(meteorites, new SortByLocation(MainActivity.this));
                adapter.notifyDataSetChanged();
            }
        });

        nameSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(meteorites, new SortByName());
                adapter.notifyDataSetChanged();
            }
        });

        masSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterOptions(findViewById(android.R.id.content));
                //Collections.sort(meteorites, new SortByWeight());
                //adapter.notifyDataSetChanged();
            }
        });
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

    private void showFilterOptions(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.filter_popup, null);

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView distanceProgress = popupView.findViewById(R.id.distanceProgress);

        Button confirmButton = popupView.findViewById(R.id.confirmFilterButton);
        Button cancelButton = popupView.findViewById(R.id.cancelFilterButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                popupWindow.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                popupWindow.dismiss();
            }
        });

        SeekBar seekBar = popupView.findViewById(R.id.distanceBar);
        seekBar.setMax(10000); //set max value of seekbar to 20000km
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                distanceProgress.setText(seekBar.getProgress() + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {

        preferenceEditor.putString("latitude", String.valueOf(location.getLatitude()));
        preferenceEditor.putString("longitude", String.valueOf(location.getLongitude()));
        preferenceEditor.apply();


    }
}

