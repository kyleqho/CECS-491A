package com.kajak.findafeast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;
import com.google.android.gms.location.LocationListener;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by Kevin on 2/19/17.
 */
public class List extends AppCompatActivity implements LocationListener {
    ListView list;
    LatLng current_position;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> img = new ArrayList<String>();
    ArrayList<Double> rating = new ArrayList<Double>();
    ArrayList<Double> distance = new ArrayList<Double>();
    ArrayList<LatLng> coordinates = new ArrayList<>();
    ArrayList<ArrayList<String>> addresses = new ArrayList<>();

    private final double METER_MILE_CONVERSION = 1609.344;


    YelpAPIFactory mApiFactory;
    YelpAPI mYelpAPI;
    Map<String, String> mParams;
    MapsActivity mAct;

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
        mParams.put("term", "food");


        //new FetchPictures().execute();
//

//        try {
//            sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        try {
            String str_result= new FetchPictures().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ListAdapter adapt = new ListAdapter(this, name, img, rating, distance);
            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapt);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    addToSelection(position);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLatitude());
        latLng = current_position;
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
                    .latitude(current_position.latitude)
                    .longitude(current_position.longitude).build();
            Call<SearchResponse> call = mYelpAPI.search(coordinate, mParams);
            Response<SearchResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e){
                e.printStackTrace();
            }
            if (response != null) {
                for (int i = 0; i < 10; i++) {
                    img.add(response.body().businesses().get(i).imageUrl());
                    name.add(response.body().businesses().get(i).name());
                    rating.add(response.body().businesses().get(i).rating());
                    distance.add(MeterToMileConverter(response.body().businesses().get(i).distance()));
                    coordinates.add(new LatLng(
                            response.body().businesses().get(i).location().coordinate().latitude(),
                            response.body().businesses().get(i).location().coordinate().longitude()));

                    addresses.add(response.body().businesses().get(i).location().address());
                }
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
        ArrayList<Restaurant> rest = new ArrayList<Restaurant>();
        rest.add(new Restaurant(name.get(pos), coordinates.get(pos), addresses.get(pos), rating.get(pos)));
        Log.v("test", rest.toString());
    }
}
