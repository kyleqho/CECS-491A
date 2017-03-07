package com.kajak.findafeast;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Location;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Kevin on 2/8/17.
 */
public class MainActivity extends AppCompatActivity{

    Button mBtnStart;
    ImageView mIVLogo;
    YelpAPIFactory mApiFactory;
    YelpAPI mYelpAPI;
    Map<String, String> mParams;
    ArrayList<Restaurant> rest = new ArrayList<Restaurant>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBtnStart = (Button) findViewById(R.id.btnStart);
        mIVLogo = (ImageView) findViewById(R.id.ivLogo);

        //load image to home screen
        Picasso
                .with(this)
                .load("https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/14906914_670103693146671_3815703461570936983_n.jpg?oh=d27a59e838eebc70f1f2f9412ec359b9&oe=590CA285")
                .into(mIVLogo);

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

        mBtnStart.setOnClickListener(handler);

        rest.clear();

        //new FetchPictures().execute();
    }

    View.OnClickListener handler = new View.OnClickListener(){
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    };

//    class FetchPictures extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onProgressUpdate(String... values){
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            //find user coordinates
//            CoordinateOptions coordinate = CoordinateOptions.builder()
//                    .latitude(33.782582)
//                    .longitude(-118.122398).build();
//            Call<SearchResponse> call = mYelpAPI.search(coordinate, mParams);
//            Response<SearchResponse> response = null;
//            try {
//                response = call.execute();
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//            if (response != null) {
//                for (int i = 0; i < 9; i++) {
//
//                    //Log.v("Businesses", response.body().businesses().get(i).imageUrl());
//                    Log.v("Businesses", response.body().businesses().get(i).name());
//
//                    System.out.println(i);
//                    //Log.v("Businesses", response.body().businesses().get(i).location().toString());
//                    //System.out.println(response.body().businesses().get(i).location().coordinate().latitude());
//                    for (int j = 0; j < 3; j++) {
//                        System.out.println(response.body().businesses().get(i).categories().get(j));
//
//                  }
//
//                    System.out.println(response.body().businesses().get(0));
//
//                    System.out.println("%");
//                    //response.body().businesses().get(0).name();
//                }
//            }
//            return null;
//        }
//    }
}
