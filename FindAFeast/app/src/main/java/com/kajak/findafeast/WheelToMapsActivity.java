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
    Button spin_btn, gothere_btn;
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();
    Activity context;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wheel_maps_transition);
        setTitle("Choose Your Fate");
        Intent intent = this.getIntent();
        selectedRest = intent.getParcelableArrayListExtra("selected");
        name = (TextView) findViewById(R.id.restaurant_name);
        System.out.println("test");
        ImageView imageView = (ImageView) findViewById(R.id.icon);


        Picasso
                .with(this.context)
                .load(selectedRest.get(0).getImage())
                .into(imageView);

        name.setText(selectedRest.get(0).getName());
        spin_btn = (Button) this.findViewById(R.id.spin_again);
        spin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
    }
}
