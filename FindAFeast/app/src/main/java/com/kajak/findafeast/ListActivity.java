package com.kajak.findafeast;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Kevin on 2/19/17.
 */
public class ListActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {
    ListView list;
    LatLng current_position;
    double mLatitude;
    double mLongitude;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> img = new ArrayList<String>();
    ArrayList<Double> rating = new ArrayList<Double>();
    ArrayList<LatLng> coordinates = new ArrayList<>();
    ArrayList<ArrayList<String>> addresses = new ArrayList<>();
    ArrayList<Restaurant> rest = new ArrayList<Restaurant>();
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();
    ArrayList<Map<String, String>> tags = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<String>();

    final float LOCATION_REFRESH = 10;
    final long LOCATION_DISTANCE = 100;

    LocationManager locationManager;

    private GoogleApiClient googleApiClient;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;

    YelpAPIFactory mApiFactory;
    YelpAPI mYelpAPI;
    Map<String, String> mParams;
    MapsActivity mAct;
    Button btn;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        setTitle("Restaurant Selection");
        mApiFactory = new YelpAPIFactory(
                getString(R.string.consumerKey),
                getString(R.string.consumerSecret),
                getString(R.string.token),
                getString(R.string.tokenSecret));

        //create yelp object
        mYelpAPI = mApiFactory.createAPI();

        Intent intent = getIntent();
        temp = intent.getStringArrayListExtra("tags");
        System.out.println(temp);
        for (int i = 0; i < temp.size(); i++){
            mParams = new HashMap<>();
            mParams.put("term", temp.get(i));
            tags.add(mParams);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(5000)
                .setFastestInterval(1000);

        btn = (Button) findViewById(R.id.mapBtn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startWheel = new Intent(ListActivity.this, WheelActivity.class);
                    startWheel.putParcelableArrayListExtra("selected", selectedRest);
                    startActivity(startWheel);
                }
            });



        btn2 = (Button) findViewById(R.id.backBtn);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent goBack = new Intent(ListActivity.this, TagsActivity.class);
                goBack.putStringArrayListExtra("tags", temp);
                startActivity(goBack);
            }
        });
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("connection_failed", "Location services failed with code " + connectionResult.getErrorCode());
            Log.i("connection_fail", connectionResult.getErrorMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) throws SecurityException {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this).await();
        } else {
            handleNewLocation(lastLocation);
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        try {
            new FetchPictures().execute().get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        ListAdapter adapt = new ListAdapter(this, name, img, rating, addresses);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapt);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedRest.contains(rest.get(position))){
                    view.setBackgroundColor(Color.WHITE);
                } else
                    view.setBackgroundColor(Color.GRAY);
                addToSelection(position);

            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location loc) {
        mLatitude = loc.getLatitude();
        mLongitude = loc.getLongitude();
    }


    class FetchPictures extends AsyncTask<String, String, String> {

        @Override
        protected void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            //find user coordinates
            CoordinateOptions coordinate = CoordinateOptions.builder()
                    .latitude(mLatitude)
                    .longitude(mLongitude).build();
            while(!tags.isEmpty()) {
                Call<SearchResponse> call = mYelpAPI.search(coordinate, tags.get(0));
                Response<SearchResponse> response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response != null) {

                    for (int i = 0; i < 5; i++) {
                        if(!name.contains(response.body().businesses().get(i).name())) {
                            img.add(response.body().businesses().get(i).imageUrl());
                            name.add(response.body().businesses().get(i).name());
                            rating.add(response.body().businesses().get(i).rating());
                            coordinates.add(new LatLng(
                                    response.body().businesses().get(i).location().coordinate().latitude(),
                                    response.body().businesses().get(i).location().coordinate().longitude()));
                            addresses.add(response.body().businesses().get(i).location().address());
                        }
                    }

                }
                tags.remove(0);
            }
            for(int i = 0; i < name.size(); i++){

                rest.add(new Restaurant(name.get(i), coordinates.get(i), addresses.get(i), rating.get(i), img.get(i)));

            }
            return null;
        }
    }

    public void addToSelection(int pos) {

            if (!selectedRest.contains(rest.get(pos))) {
                Toast.makeText(this, "Added into list", Toast.LENGTH_SHORT).show();

                selectedRest.add(rest.get(pos));
            } else {
                Toast.makeText(this, "Removing from list", Toast.LENGTH_SHORT).show();
                selectedRest.remove(rest.get(pos));
            }
        Log.v("test", selectedRest.toString());
    }
}
