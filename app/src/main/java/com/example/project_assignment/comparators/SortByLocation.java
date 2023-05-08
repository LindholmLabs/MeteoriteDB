package com.example.project_assignment.comparators;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.project_assignment.Meteorite;

import java.util.Comparator;

public class SortByLocation implements Comparator<Meteorite> {

    @Override
    public int compare(Meteorite m1, Meteorite m2) {
        return Double.compare(m1.getDistance(), m2.getDistance());
    }
}
