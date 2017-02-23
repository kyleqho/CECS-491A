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

import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by Kevin on 2/19/17.
 */
public class List extends AppCompatActivity {
    ListView list;
    String[] name = new String[10];
    String[] img = new String[10];
    Double[] rating = new Double[10];
    Double[] distance = new Double[10];
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
        mParams.put("price", "1,2,3");


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
                    .latitude(33.782582)
                    .longitude(-118.122398).build();
            Call<SearchResponse> call = mYelpAPI.search(coordinate, mParams);
            Response<SearchResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e){
                e.printStackTrace();
            }
            if (response != null) {
                for (int i = 0; i < 10; i++) {

                    //Log.v("Businesses", response.body().businesses().get(i).imageUrl());
                    //Log.v("Businesses", response.body().businesses().get(i).name());

//                    System.out.println(i);
                    //Log.v("Businesses", response.body().businesses().get(i).location().toString());
                    //System.out.println(response.body().businesses().get(i).location().coordinate().latitude());
//                    for (int j = 0; j < 3; j++) {
////                        System.out.println(response.body().businesses().get(i).categories().get(j));
//
//                    }
                    img[i] = response.body().businesses().get(i).imageUrl();
                    name[i] = response.body().businesses().get(i).name();
                    rating[i] = response.body().businesses().get(i).rating();
                    distance[i] = MeterToMileConverter(response.body().businesses().get(i).distance());
                   // System.out.println(response.body().businesses().get(0));

                    //System.out.println("%");
                    //response.body().businesses().get(0).name();
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

}
