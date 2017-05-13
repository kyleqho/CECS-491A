package com.kajak.findafeast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class WheelActivity extends AppCompatActivity {
    ArrayList<Restaurant> restaurants;
    ArrayList<Restaurant> selectedRest = new ArrayList<Restaurant>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        setTitle("Spin!!");

        //Gets the previously passed values to the wheel
        Intent intent = this.getIntent();
        restaurants = intent.getParcelableArrayListExtra("selected");

        //Allows the wheel to be initialized
        initWheel(R.id.p1);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Gets the size of the array
        final int size = restaurants.size();
        //Button that allows for the spin mechanic
        final Button spin = (Button) this.findViewById(R.id.spin);

        //Enables the button to be pressed
        spin.setEnabled(true);
        spin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Once button has been pressed, disables the button
                spin.setEnabled(false);
                //Clear the list that the selected restaurant goes into in the instance that the user goes back
                selectedRest.clear();
                //Creation of random number
                Random r = new Random();
                //Creates a random number dependent on the size of the list for selection
                final int x =  r.nextInt(size);

                //Creates the wheel iamge
                final WheelView wheel = (WheelView) findViewById(R.id.p1);

                //Creates rotations of the wheel
                int rotations = 30;
                //Distance the wheel travels
                int dist =  (size * rotations)+x;
                //The amount of time the wheel spins for
                int seconds = 6000;
                wheel.scroll(dist, seconds);

                //Creates a delay function
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Adds the current item on the wheel to the selected restaurant list
                        selectedRest.add(restaurants.get(wheel.getCurrentItem()));
                        //Goes to the next activity after the wheel has been spun
                        Intent wheelTransition = new Intent(WheelActivity.this, WheelToMapsActivity.class);
                        //passes the array to the next activity
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
