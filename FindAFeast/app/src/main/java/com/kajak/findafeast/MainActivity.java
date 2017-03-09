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

        rest.clear();
    }

    public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "button press", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
    }

//    View.OnClickListener handler = new View.OnClickListener(){
//        public void onClick(View v){
//            Toast.makeText(getApplicationContext(), "button press", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, ListActivity.class);
//            startActivity(intent);
//        }
//    };

}
