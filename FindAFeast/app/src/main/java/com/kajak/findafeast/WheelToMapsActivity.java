package com.kajak.findafeast;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kyle Ho on 4/19/2017.
 */
public class WheelToMapsActivity extends AppCompatActivity{
    Button spin_btn, gothere_btn, start_over;
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();
    Activity context;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wheel_maps_transition);
        setTitle("Restaurant Confirmation");

        //Gets the data previously sent to this activity
        Intent intent = this.getIntent();
        selectedRest = intent.getParcelableArrayListExtra("selected");

        //Text view to display the name of the restaurant
        name = (TextView) findViewById(R.id.restaurant_name);

        //Gets the image of the restaurant from Yelp
        ImageView imageView = (ImageView) findViewById(R.id.icon);


        Picasso
                .with(this.context)
                .load(selectedRest.get(0).getImage())
                .into(imageView);

        //Gets the name of the restaurant
        name.setText(selectedRest.get(0).getName());

        //If the user spins again, it goes back to the previous activity
        spin_btn = (Button) this.findViewById(R.id.spin_again);
        spin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //If the user goes there, it takes that user directly to Google directions
        gothere_btn = (Button) this.findViewById(R.id.go_there);
        gothere_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rest_lat = (float) selectedRest.get(0).getLatitude();
                float rest_long = (float) selectedRest.get(0).getLongitude();
                Intent goThere = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+rest_lat+","+rest_long));
                        startActivity(goThere);
            }
        });

        //Allows the user to start from the beginning of the application
        start_over = (Button) this.findViewById(R.id.startOver);
        start_over.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent startOver = new Intent(WheelToMapsActivity.this, TagsActivity.class);
                startActivity(startOver);
            }
        });
    }
}
