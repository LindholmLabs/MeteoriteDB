package com.example.project_assignment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Meteorite implements Parcelable {
    protected String name;
    protected String id;
    protected String mass;

    @SerializedName(value = "year", alternate = "date")
    protected String date;

    @SerializedName(value = "reclat", alternate = "latitude")
    protected String latitude;

    @SerializedName(value = "reclong", alternate = "longitude")
    protected String longitude;

    private double distance;

    @Override
    public String toString() {
        return "The meteorite " + name +
                "has a mass of " + mass + "kg " +
                "and touched the surface of the earth at " +
                date + "\n\n" +
                "It landed at lat: " + latitude +
                " long: " + longitude +
                " Roughly " + Math.round(distance) + "km from your location";
    }

    protected Meteorite(Parcel in) {
        name = in.readString();
        id = in.readString();
        mass = in.readString();
        date = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        distance = in.readDouble();
    }

    public static final Creator<Meteorite> CREATOR = new Creator<Meteorite>() {
        @Override
        public Meteorite createFromParcel(Parcel in) {
            return new Meteorite(in);
        }

        @Override
        public Meteorite[] newArray(int size) {
            return new Meteorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(mass);
        parcel.writeString(date);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeDouble(distance);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMass() {
        if (mass == null) {
            return 0;
        }
        double mass_double = Double.parseDouble(mass);
        return mass_double;
    }

    //return the distance of the meteorite from any position.
    public double generateDistanceFrom(double userLatitude, double userLongitude) {
        if (latitude == null) {
            latitude = "0";
        }
        if (longitude == null) {
            longitude = "0";
        }

        //1 = user
        //2 = meteorite

        double meteoriteLatitude = Double.parseDouble(this.latitude);
        double meteoriteLongitude = Double.parseDouble(this.longitude);

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(meteoriteLatitude - userLatitude);
        double lonDistance = Math.toRadians(meteoriteLongitude - userLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(meteoriteLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // convert to meters

        setDistance(Math.sqrt(distance));
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }

    public boolean matchingFilter(int minMass, int maxMass, int minYear, int maxYear, int maxDistance) {
        //filter mass
        if (mass != null) {
            double tempMass = Double.parseDouble(mass);
            if (tempMass < minMass && minMass != 0) {
                return false;
            }
            if (tempMass > maxMass && maxMass != 0) {
                return false;
            }
        } else {
            return false;
        }
        //filter year the meteorite fell.
        if (date != null) {
            int year = Integer.parseInt(date.substring(0, 4));
            if (year < minYear && minYear != 0) {
                return false;
            }
            if (year > maxYear && maxYear != 0) {
                return false;
            }
        } else {
            return false;
        }

        if (distance > maxDistance && maxDistance != 0) {
            return false;
        }

        return true;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
