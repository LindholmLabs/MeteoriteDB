package com.example.project_assignment.comparators;

import com.example.project_assignment.Meteorite;

import java.util.Comparator;

public class SortByName implements Comparator<Meteorite> {

    @Override
    public int compare(Meteorite m1, Meteorite m2) {
        return m2.getName().toLowerCase().compareTo(m2.getName().toLowerCase());
    }
}
