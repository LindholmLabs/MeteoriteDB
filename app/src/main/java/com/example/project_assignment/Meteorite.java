
package com.example.project_assignment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Meteorite implements Parcelable {
    protected String name;

    @SerializedName(value = "ID", alternate = "id")
    protected String id;
    @SerializedName(value = "size", alternate = "mass")
    protected String mass;

    protected String category;

    protected String location;

    private String date;

    protected JsonObject auxdata;

    private double distance;

    public String getDescription() {
        return "The meteorite " + name +
                " has a mass of " + mass + "kg " +
                "and touched the surface of the earth at " +
                getDate() + "\n\n" +
                "It landed at lat: " + getLatitude() +
                " long: " + getLongitude() +
                " Roughly " + Math.round(distance) + "km from your location" +
                "\nIt is part of the category: " + category;
    }

    @Override
    public String toString() {
        return "Meteorite{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", mass='" + mass + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", latitude='" + getLatitude() + '\'' +
                ", date='" + date + '\'' +
                ", longitude='" + getLongitude() + '\'' +
                ", auxdata=" + auxdata.toString() +
                ", distance=" + distance +
                '}';
    }

    /**
     * Used to create parcel for sending Meteorite data as one object throgh intents.
     */
    protected Meteorite(Parcel in) {
        name = in.readString();
        id = in.readString();
        mass = in.readString();
        category = in.readString();
        date = in.readString();
        location = in.readString();
        distance = in.readDouble();
    }

    /**
     * When Meteorite is unpacked from parcel, use below function.
     */
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
        parcel.writeString(category);
        parcel.writeString(getDate());
        parcel.writeString(location);
        parcel.writeDouble(distance);
    }

    /**
     * Return and set distance valeue to the meteorite from coordinate.
     * Code inspired by:
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     * @param extLatitude The external positions latitude.
     * @param extLongitude The external positions longitude.
     * @return Return the distance from the meteorite.
     */
    public double generateDistanceFrom(double extLatitude, double extLongitude) {

        double meteoriteLatitude = Double.parseDouble(getLatitude());
        double meteoriteLongitude = Double.parseDouble(getLongitude());

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(meteoriteLatitude - extLatitude);
        double lonDistance = Math.toRadians(meteoriteLongitude - extLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(extLatitude)) * Math.cos(Math.toRadians(meteoriteLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // convert to meters

        setDistance(Math.sqrt(distance));
        return this.distance;
    }


    /**
     * Used to filter meteorites using the parameters below, if the current meteorite
     * does not match the specified criteria.
     * @param minMass The lowest accepted mass.
     * @param maxMass The highest accepted mass.
     * @param minYear The lowest accepted year.
     * @param maxYear The highest accepted year.
     * @param maxDistance The furthest accepted distance.
     * @return true, if all criteria match, else false.
     */
    public boolean matchingFilter(int minMass, int maxMass, int minYear, int maxYear, int maxDistance) {
        //filter mass
        if (getMass() < minMass && minMass != 0) {
            System.out.println("mass not matching filter");
            return false;
        }
        if (getMass() > maxMass && maxMass != 0) {
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

    /**
     * Since the date is stored in a dateTime format, truncate the String to only return year before
     * returning.
     * @return the year the meteorite landed.
     */
    public String getDate() {
        if (date == null) {
            date = auxdata.get("date").getAsString().substring(0, 4);
            return auxdata.get("date").getAsString().substring(0, 4);
        }
        return date;
    }

    /**
     * Since both lat and long values are stored in one string, separated by ",",
     * split them into separate values before returning.
     * @return latitude.
     */
    public String getLatitude() {
        String latLong[] = location.split(",");
        return latLong[0];
    }

    /**
     * Since both lat and long values are stored in one string, separated by ",",
     * split them into separate values before returning.
     * @return longitude.
     */
    public String getLongitude() {
        String latLong[] = location.split(",");
        return latLong[1];
    }

    /**
     * To more easily handle calculations and comparisons based on mass,
     * convert the value to int before returning.
     * @return Int mass.
     */
    public int getMass() {
        if (mass == null) {
            return 0;
        }
        return Integer.parseInt(mass);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
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
}
