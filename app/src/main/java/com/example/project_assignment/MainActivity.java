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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_assignment.comparators.SortByLocation;
import com.example.project_assignment.comparators.SortByName;
import com.example.project_assignment.comparators.SortByWeight;
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
    private ArrayList<Meteorite> unalteredMeteoriteList;

    private RecyclerViewAdapter adapter;

    private boolean distanceSet = false;

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
        showSortingOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showFilterOptions(view);
            }
        });
    }

    @Override
    public void onPostExecute(String json) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Meteorite>>() {}.getType();
        meteorites = gson.fromJson(json, type);
        unalteredMeteoriteList = new ArrayList<Meteorite>();
        unalteredMeteoriteList.addAll(meteorites);

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

        //initialize layoutInflater
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.filter_popup, null);

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //fetch all necessary views and widgets in popup.
        TextView distanceProgress = popupView.findViewById(R.id.distanceProgress);
        Button confirmButton = popupView.findViewById(R.id.confirmFilterButton);
        Button cancelButton = popupView.findViewById(R.id.cancelFilterButton);
        SeekBar seekBar = popupView.findViewById(R.id.distanceBar);
        EditText minMass = popupView.findViewById(R.id.setMinimumMass);
        EditText maxMass = popupView.findViewById(R.id.setMaximumMass);
        EditText minYear = popupView.findViewById(R.id.setMinimumYear);
        EditText maxYear = popupView.findViewById(R.id.setMaximumYear);
        RadioGroup sorting = popupView.findViewById(R.id.sortingGroup);


        //set all listeners
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reinitialize saved meteorites.
                meteorites.clear();
                meteorites.addAll(unalteredMeteoriteList);

                //fetch all editText data.
                int intMinMass = fetchEditTextData(minMass);
                int intMaxMass = fetchEditTextData(maxMass);
                int intMinYear = fetchEditTextData(minYear);
                int intMaxYear = fetchEditTextData(maxYear);
                int distance = Integer.parseInt(String.valueOf(seekBar.getProgress()));
                int selectedSortingButton = sorting.getCheckedRadioButtonId();

                ArrayList<Meteorite> toRemove = new ArrayList<Meteorite>();

                //check if meteorites match filter settings.
                for (Meteorite m : meteorites) {
                    boolean matchingFilter = m.matchingFilter(intMinMass, intMaxMass, intMinYear, intMaxYear, distance);
                    if (!matchingFilter) {
                        toRemove.add(m);
                    }
                }

                //remove meteorites that did not match the filter settings.
                for (Meteorite m : toRemove) {
                    meteorites.remove(m);
                }

                switch (selectedSortingButton) {
                    case R.id.radioButtonSortLocation:
                        Collections.sort(meteorites, new SortByLocation());
                        System.out.println("sorting location");
                        break;
                    case R.id.radioButtonSortWeight:
                        Collections.sort(meteorites, new SortByWeight());
                        System.out.println("Sort by mass");
                        break;
                    default:
                        Collections.sort(meteorites, new SortByName());
                        System.out.println("sort by name");
                }


                adapter.notifyDataSetChanged();

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

    private int fetchEditTextData(EditText editText) {
        int output = 0;
        if(!String.valueOf(editText.getText()).isEmpty()) {
            output = Integer.parseInt(String.valueOf(editText.getText()));
            return output;
        }
        return output;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (distanceSet) {
            return;
        }
        for (Meteorite m : meteorites) {
            m.generateDistanceFrom(location.getLatitude(), location.getLongitude());
            distanceSet = true;
        }
    }
}

