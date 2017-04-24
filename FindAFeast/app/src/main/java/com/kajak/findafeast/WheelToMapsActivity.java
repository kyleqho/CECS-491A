package com.kajak.findafeast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Kyle Ho on 4/19/2017.
 */
public class WheelToMapsActivity extends AppCompatActivity{
    Button spin_btn, gothere_btn;
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        selectedRest = intent.getParcelableArrayListExtra("selected");

        //img.add(response.body().businesses().get(i).imageUrl());

        setContentView(R.layout.wheel_maps_transition);

        spin_btn = (Button) this.findViewById(R.id.spin_again);
        spin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startMap.putExtra("selected", selectedRest);
//                startActivity(startMap);
//                startMap.putParcelableArrayListExtra("selected", selectedRest);
//                startActivity(startMap);
                Intent spinAgain = new Intent(WheelToMapsActivity.this, WheelActivity.class);
                //spinAgain.putParcelableArrayListExtra("selected", selectedRest);
                startActivity(spinAgain);
            }
        });

        gothere_btn = (Button) this.findViewById(R.id.go_there);
        gothere_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startMap.putExtra("selected", selectedRest);
//                startActivity(startMap);
//                startMap.putParcelableArrayListExtra("selected", selectedRest);
//                startActivity(startMap);
                Intent goThere = new Intent(WheelToMapsActivity.this, MapsActivity.class);
                goThere.putParcelableArrayListExtra("selected", selectedRest);
                startActivity(goThere);
            }
        });
    }
}
