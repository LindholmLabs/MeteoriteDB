package com.example.project_assignment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
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
    protected Meteorite(Parcel in) {
        name = in.readString();
        id = in.readString();
        mass = in.readString();
        date = in.readString();
        latitude = in.readString();
        longitude = in.readString();
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
    }
}
