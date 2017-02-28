package com.kajak.findafeast;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Kevin on 2/27/17.
 */
public class Restaurant {

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

    public Restaurant(String name, double lat, double lon, ArrayList<String> address, double rating) {
        this.name = name;
        latLng = new LatLng(lat, lon);
        this.address = address;
        this.rating = rating;
    }

    public Restaurant(String name, LatLng latLng, ArrayList<String> address) {
        this.name = name;
        this.latLng = latLng;
        this.address = address;
    }

    public Restaurant(String name, double lat, double lon, ArrayList<String> address) {
        this.name = name;
        latLng = new LatLng(lat, lon);
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
        return "name: " + name + "\t(lat, lon): " + latLng.toString() + "\taddress: " + address.toString()
                + "\trating: " + rating;
    }
}
