package com.example.project_assignment.comparators;

import com.example.project_assignment.Meteorite;

import java.util.Comparator;

public class SortByWeight implements Comparator<Meteorite> {

    @Override
    public int compare(Meteorite m1, Meteorite m2) {
        //place null values last.
        if (m1.getMass() == 0) {
            return 1;
        } else if (m2.getMass() == 0) {
            return -1;
        } else if (m1.getMass() == 0 && m2.getMass() == 0) {
            return 0;
        }

        return Double.compare(m1.getMass(), m2.getMass());
    }
}
