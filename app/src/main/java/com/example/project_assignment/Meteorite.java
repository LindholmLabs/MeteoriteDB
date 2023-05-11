
package com.example.project_assignment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

public class Meteorite implements Parcelable {
    protected String name;

    @SerializedName(value = "ID", alternate = "id")
    protected String id;
    @SerializedName(value = "size", alternate = "mass")
    protected String mass;

    protected String location;

    private String latitude;

    private String date;

    private String longitude;

    protected JsonObject auxdata;

    private double distance;

    @Override
    public String toString() {
        return "The meteorite " + name +
                " has a mass of " + mass + "kg " +
                "and touched the surface of the earth at " +
                getDate() + "\n\n" +
                "It landed at lat: " + getLatitude() +
                " long: " + getLongitude() +
                " Roughly " + Math.round(distance) + "km from your location";
    }

    protected Meteorite(Parcel in) {
        name = in.readString();
        id = in.readString();
        mass = in.readString();
        date = in.readString();
        location = in.readString();
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
        parcel.writeString(getDate());
        parcel.writeString(location);
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

        String latLong[] = location.split(",");
        latitude = latLong[0];
        longitude = latLong[1];

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
        double tempMass = Double.parseDouble(mass);
        if (tempMass < minMass && minMass != 0) {
            System.out.println("mass not matching filter");
            return false;
        }
        if (tempMass > maxMass && maxMass != 0) {
            System.out.println("mass not matching filter");
            return false;
        }
        //filter year the meteorite fell.
        if (getDate() != null) {
            int year = Integer.parseInt(getDate());
            if (year < minYear && minYear != 0) {
                System.out.println("date not matching filter");
                return false;
            }
            if (year > maxYear && maxYear != 0) {
                System.out.println("date not matching filter");
                return false;
            }
        } else {
            System.out.println("date null");
            return false;
        }

        if (distance > maxDistance && maxDistance != 0) {
            System.out.println("distance not matching filter");
            return false;
        }

        //if none of the filters trigger, return true
        return true;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getDate() {
        if (date == null) {
            date = auxdata.get("date").getAsString().substring(0, 4);
            return auxdata.get("date").getAsString().substring(0, 4);
        }
        return date;
    }

    public String getLatitude() {
        String latLong[] = location.split(",");
        return latLong[0];
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        String latLong[] = location.split(",");
        return latLong[1];
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
