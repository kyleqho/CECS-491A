package com.kajak.findafeast;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Kevin on 2/27/17.
 */
public class Restaurant implements Parcelable {

    private String name;
    private LatLng latLng;
    private ArrayList<String> address;
    private double rating;

    public Restaurant(String name, LatLng latLng, ArrayList<String> address, double rating) {
        this.name = name;
        this.latLng = latLng;
        this.address = address;
        this.rating = rating;
    }

    public Restaurant(String name, double lat, double lng, ArrayList<String> address, double rating) {
        this.name = name;
        latLng = new LatLng(lat, lng);
        this.address = address;
        this.rating = rating;
    }

    public Restaurant(String name, LatLng latLng, ArrayList<String> address) {
        this.name = name;
        this.latLng = latLng;
        this.address = address;
    }

    public Restaurant(String name, double lat, double lng, ArrayList<String> address) {
        this.name = name;
        latLng = new LatLng(lat, lng);
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getLatitude() {
        return latLng.latitude;
    }

    public double getLongitude() {
        return latLng.longitude;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return name;
    }


    protected Restaurant(Parcel in) {
        name = in.readString();
        latLng = (LatLng) in.readValue(LatLng.class.getClassLoader());
        if (in.readByte() == 0x01) {
            address = new ArrayList<String>();
            in.readList(address, String.class.getClassLoader());
        } else {
            address = null;
        }
        rating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(latLng);
        if (address == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(address);
        }
        dest.writeDouble(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}