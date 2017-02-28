package com.kajak.findafeast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kevin on 2/27/17.
 */
public class LocClass {

    private LatLng location;

    public LocClass(LatLng latLng) {
        location = latLng;
    }

    public LocClass(double lat, double lon) {
        location = new LatLng(lat, lon);
    }

    public LatLng getLocation() {
        return location;
    }

    public double getLatitude() {
        return location.latitude;
    }

    public double getLongitude() {
        return location.longitude;
    }
}
