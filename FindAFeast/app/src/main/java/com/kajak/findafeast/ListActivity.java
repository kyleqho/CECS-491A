package com.kajak.findafeast;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Kevin on 2/19/17.
 */
public class ListActivity extends AppCompatActivity {
    ListView list;
    LatLng current_position;
    double mLatitude;
    double mLongitude;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> img = new ArrayList<String>();
    ArrayList<Double> rating = new ArrayList<Double>();
    ArrayList<Double> distance = new ArrayList<Double>();
    ArrayList<LatLng> coordinates = new ArrayList<>();
    ArrayList<ArrayList<String>> addresses = new ArrayList<>();
    ArrayList<Restaurant> rest = new ArrayList<Restaurant>();
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();
    ArrayList<Map<String, String>> tags = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<String>();
    final float LOCATION_REFRESH = 10;
    final long LOCATION_DISTANCE = 100;

    LocationManager locationManager;

    private final double METER_MILE_CONVERSION = 1609.344;

    YelpAPIFactory mApiFactory;
    YelpAPI mYelpAPI;
    Map<String, String> mParams;
    MapsActivity mAct;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

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
        //mParams.put("term", "food");
        Intent intent = getIntent();

        temp = intent.getStringArrayListExtra("tags");
        for (int i = 0; i < temp.size(); i++){
            mParams.put("term", temp.get(i));
            tags.add(mParams);
        }

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mLongitude = currentLocation.getLongitude();
        mLatitude = currentLocation.getLatitude();
//        new FetchPictures().execute();
//

        try {
            String str_result= new FetchPictures().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final Intent startMap = new Intent(ListActivity.this, MapsActivity.class);

        ListAdapter adapt = new ListAdapter(this, name, img, rating, distance);
            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapt);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToSelection(position);
                //startMap.putExtra("selected", selectedRest);
                //startActivity(startMap);
            }
        });

        btn = (Button) findViewById(R.id.mapBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startMap.putExtra("selected", selectedRest);
//                startActivity(startMap);
//                startMap.putParcelableArrayListExtra("selected", selectedRest);
//                startActivity(startMap);
                Intent startWheel = new Intent(ListActivity.this, WheelActivity.class);
                startWheel.putParcelableArrayListExtra("selected", selectedRest);
                startActivity(startWheel);
            }
        });


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

                    for (int i = 0; i < 3; i++) {
                        img.add(response.body().businesses().get(i).imageUrl());
                        name.add(response.body().businesses().get(i).name());
                        rating.add(response.body().businesses().get(i).rating());
                        distance.add(MeterToMileConverter(response.body().businesses().get(i).distance()));
                        coordinates.add(new LatLng(
                                response.body().businesses().get(i).location().coordinate().latitude(),
                                response.body().businesses().get(i).location().coordinate().longitude()));

                        addresses.add(response.body().businesses().get(i).location().address());
                        rest.add(new Restaurant(name.get(i), coordinates.get(i), addresses.get(i), rating.get(i)));

                    }
                }
                tags.remove(0);

            }

            return null;
        }
    }

    private double MeterToMileConverter(double meters){
        double miles = meters/METER_MILE_CONVERSION;
        DecimalFormat dFormat = new DecimalFormat();
        dFormat.setMaximumFractionDigits(1);
        return Double.valueOf(dFormat.format(miles));
    }

    public void addToSelection(int pos) {

            if (!selectedRest.contains(rest.get(pos))) {
                Toast.makeText(this, "Added into list", Toast.LENGTH_SHORT).show();
                selectedRest.add(rest.get(pos));
            } else
                Toast.makeText(this, "Already in list", Toast.LENGTH_SHORT).show();

        Log.v("test", selectedRest.toString());
    }
}
