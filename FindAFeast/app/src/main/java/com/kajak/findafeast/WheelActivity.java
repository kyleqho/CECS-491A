package com.kajak.findafeast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class WheelActivity extends AppCompatActivity {
    // TODO: Externalize string-array
    //String wheelMenu1[] = new String[]{"name 1", "name 2", "name 3", "name 4", "name 5", "name 6","name 7","name 8","name 9"};
    ArrayList<Restaurant> restaurants;
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();

    // Wheel scrolled flag
    private boolean wheelScrolled = false;
    private TextView text1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        setTitle("Spin!!");

        Intent intent = this.getIntent();
        restaurants = intent.getParcelableArrayListExtra("selected");

        initWheel(R.id.p1);

        final int size = restaurants.size();

        Button spin = (Button) this.findViewById(R.id.spin);
        spin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedRest.clear();
                Random r = new Random();
                final int x =  r.nextInt(size);

                final WheelView wheel = (WheelView) findViewById(R.id.p1);

                int rotations = 30;
                int dist =  (size * rotations)+x;
                int seconds = 6000;
                wheel.scroll(dist, seconds);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectedRest.add(restaurants.get(wheel.getCurrentItem()));
                        Intent wheelTransition = new Intent(WheelActivity.this, WheelToMapsActivity.class);
                        wheelTransition.putParcelableArrayListExtra("selected", selectedRest);
                        startActivity(wheelTransition);
                    }
                }, 5500);
            }
        });
    }


    private void initWheel(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        Restaurant[] rest_list = restaurants.toArray(new Restaurant[restaurants.size()]);
        wheel.setViewAdapter(new ArrayWheelAdapter<Restaurant>(getApplicationContext(), rest_list));
    }

    private WheelView getWheel(int id)
    {
        return (WheelView) findViewById(id);
    }

    private int getWheelValue(int id)
    {
        return getWheel(id).getCurrentItem();
    }
}
