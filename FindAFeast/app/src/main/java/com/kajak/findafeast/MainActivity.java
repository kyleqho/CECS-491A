package com.kajak.findafeast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kevin on 2/8/17.
 */
public class MainActivity extends AppCompatActivity{

    Button mBtnStart;
    //ImageView mIVLogo;
    ArrayList<Restaurant> rest = new ArrayList<Restaurant>();
    final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request permission for user's location
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }


        mBtnStart = (Button) findViewById(R.id.btnStart);
       // mIVLogo = (ImageView) findViewById(R.id.ivLogo);

        //load image to home screen
//        Picasso
//                .with(this)
//                .load("https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-9/14906914_670103693146671_3815703461570936983_n.jpg?oh=d27a59e838eebc70f1f2f9412ec359b9&oe=590CA285")
//                .into(mIVLogo);

        rest.clear();
    }

    //Testing to check if maps works
    //Status: Maps works correctly
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, TagsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                System.exit(0);
            }
        }
    }
}
