package com.example.project_assignment;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Meteorite {
    protected String name;
    protected String id;
    protected String mass;

    @SerializedName(value = "year", alternate = "date")
    protected String date;

    @SerializedName(value = "reclat", alternate = "latitude")
    protected String latitude;

    @SerializedName(value = "reclong", alternate = "longitude")
    protected String longitude;

    @Override
    public String toString() {
        return "Meteorite{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", mass='" + mass + '\'' +
                ", date='" + date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
