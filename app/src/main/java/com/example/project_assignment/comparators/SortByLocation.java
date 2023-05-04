package com.example.project_assignment.comparators;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.project_assignment.MainActivity;
import com.example.project_assignment.Meteorite;

import java.util.Comparator;

public class SortByLocation implements Comparator<Meteorite> {

    private static SharedPreferences preference;
    private double latitude;
    private double longitude;
    public SortByLocation(Context context) {
        //fetch users current location from shared preference
        preference = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        latitude = Double.parseDouble(preference.getString("latitude", "0"));
        longitude = Double.parseDouble(preference.getString("longitude", "0"));
    }
    @Override
    public int compare(Meteorite m1, Meteorite m2) {
        System.out.println(latitude + ":" + longitude);
        return Double.compare(m1.getDistanceFrom(latitude, longitude), m2.getDistanceFrom(latitude, longitude));
    }
}
