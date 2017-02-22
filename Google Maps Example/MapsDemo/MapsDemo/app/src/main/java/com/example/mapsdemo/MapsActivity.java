package com.example.mapsdemo;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocationMarker;
    private MarkerOptions marker;
    private LocationRequest mLocationRequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    YelpAPIFactory mApiFactory;
    Location yelpLocation;
    double yelpLong, yelpLat;
    YelpAPI mYelpAPI;
    Map<String, String> mParams;
    private GetCurrentLocation gct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        gct = new GetCurrentLocation(MapsActivity.this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //yelpLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mApiFactory = new YelpAPIFactory(
                getString(R.string.consumerKey),
                getString(R.string.consumerSecret),
                getString(R.string.token),
                getString(R.string.tokenSecret));

        //create yelp object
        mYelpAPI = mApiFactory.createAPI();

        //map of params
        mParams = new HashMap<>();

        //search terms
        mParams.put("term", "food");

        if(gct.canGetLocation()){
            yelpLong = gct.getLongitude();
            yelpLat = gct.getLatitude();
        }
//        try {
//            String test= new FetchPictures().execute().get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public class GetCurrentLocation extends Service implements LocationListener{
        public LocationManager locationManager;
        private Context cntxt;
        Location yelpLocation;
        double yelp_lat, yelp_long;
        boolean checkGPS = false;
        boolean checkNetwork = false;
        boolean canGetLocation = false;

        public GetCurrentLocation(Context cntxt){
            this.cntxt = cntxt;
            GetLocation();
        }

        private Location GetLocation(){
            try{
                Criteria criteria = new Criteria();
                locationManager = (LocationManager) cntxt.getSystemService(Context.LOCATION_SERVICE);
                checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                String provider = locationManager.getBestProvider(criteria, true);

                if(!checkGPS && !checkNetwork)
                    Toast.makeText(cntxt, "No Service Provider Available", Toast.LENGTH_SHORT).show();
                else{
                    this.canGetLocation=true;
                    //Get location from network provider
                    if(checkNetwork){
                        Toast.makeText(cntxt, "Network", Toast.LENGTH_SHORT).show();
                        try{
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                            Log.d("Network", "Network");
                            yelpLocation = locationManager.getLastKnownLocation(provider);
                            if(locationManager != null){
                                onLocationChanged(yelpLocation);
                            }
                            if(yelpLocation != null){
                                yelp_lat = yelpLocation.getLatitude();
                                yelp_long = yelpLocation.getLongitude();
                            }
                        }catch (SecurityException e){}
                    }
                }

                if(checkGPS){
                    Toast.makeText(cntxt, "GPS", Toast.LENGTH_SHORT).show();
                    if(yelpLocation == null){
                        try{
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if(locationManager != null)
                                yelpLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(yelpLocation != null){
                                yelp_lat = yelpLocation.getLatitude();
                                yelp_long = yelpLocation.getLongitude();
                            }
                        }catch(SecurityException e){}
                    }
                }

            }catch(Exception e){}
            return yelpLocation;
        }


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        public double getLongitude() {
            if(yelpLocation != null)
                yelp_long = yelpLocation.getLongitude();
            return yelp_long;
        }

        public double getLatitude() {
            if(yelpLocation != null)
                yelp_lat = yelpLocation.getLatitude();
            return yelp_lat;
        }
    }

    class FetchPictures extends AsyncTask<String, String, String> {
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            //find user coordinates
            CoordinateOptions coordinate = CoordinateOptions.builder()
                    .latitude(yelpLat)
                    .longitude(yelpLong).build();
            Call<SearchResponse> call = mYelpAPI.search(coordinate, mParams);
            Response<SearchResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e){
                e.printStackTrace();
            }
            if (response != null) {
                double rest_lat = response.body().businesses().get(1).location().coordinate().latitude();
                double rest_long = response.body().businesses().get(1).location().coordinate().longitude();
                double rest_distance = response.body().businesses().get(1).distance();
                double rating = response.body().businesses().get(1).rating();
                System.out.println(""+rest_lat+" "+rest_long+" "+rest_distance+" "+rating);
                String rest_name =response.body().businesses().get(1).name();
                LatLng latLng = new LatLng(rest_lat, rest_long);
                MarkerOptions markOpts = new MarkerOptions();
                markOpts.position(latLng)
                        .title(rest_name)
                        .snippet(Double.toString(rating))
                        .snippet(Double.toString(rest_distance))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markOpts);
            }
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Location mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng)
                .title("Current Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(yelpLat)
                .longitude(yelpLong).build();
        Call<SearchResponse> call = mYelpAPI.search(coordinate, mParams);
        Response<SearchResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e){
            e.printStackTrace();
        }
        if (response != null) {
            double rest_lat = response.body().businesses().get(1).location().coordinate().latitude();
            double rest_long = response.body().businesses().get(1).location().coordinate().longitude();
            double rest_distance = response.body().businesses().get(1).distance();
            double rating = response.body().businesses().get(1).rating();
            System.out.println("" + rest_lat + " " + rest_long + " " + rest_distance + " " + rating);
            String rest_name = response.body().businesses().get(1).name();
            LatLng yelp_latLng = new LatLng(rest_lat, rest_long);
            MarkerOptions markOpts = new MarkerOptions();
            markOpts.position(yelp_latLng)
                    .title(rest_name)
                    .snippet(Double.toString(rating))
                    .snippet(Double.toString(rest_distance))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(markOpts);
        }


            //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),13.5f));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                marker = new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("Chosen Location")
                        .snippet("Tap here to remove");
                mMap.addMarker(marker);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
            }
        });

    }

    @Override
    public boolean onMarkerClick(final Marker marker){
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }//    @Override

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude)).title("Chosen Location");
        mMap.addMarker(marker);
    }


//    public void onRequestPermissionsResults(int requestCode, String permissions[], int[] grantResults){
//        switch(requestCode){
//            case MY_PERMISSION_REQUEST_LOCATION:{
//                //If request is cancelled, the results arrays are emptied
//                if(grantResults.length >0
//                        && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    //permission is granted
//                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED){
//                        if(mGoogleApiClient == null)
//                            buildGoogleApiClient();
//                    }
//                    mMap.setMyLocationEnabled(true);
//                }
//                else
//                    Toast.makeText(this,"permission denied", Toast.LENGTH_LONG);
//            }
//            return;
//        }
//    }
}
