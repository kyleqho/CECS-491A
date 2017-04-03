package com.kajak.findafeast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class WheelActivity extends AppCompatActivity {
    // TODO: Externalize string-array
    String wheelMenu1[] = new String[]{"name 1", "name 2", "name 3", "name 4", "name 5", "name 6","name 7","name 8","name 9"};
    ArrayList<Restaurant> restaurants;

    // Wheel scrolled flag
    private boolean wheelScrolled = false;

    private TextView text;
    private EditText text1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);

        Intent intent = this.getIntent();
        Bundle data = intent.getBundleExtra("list");

        if (data.containsKey("list")) {
            restaurants = (ArrayList<Restaurant>) data.get("list");
        }

        initWheel(R.id.wheel_view);

        text1 = (EditText) this.findViewById(R.id.r1);
        text = (TextView) this.findViewById(R.id.result);
    }

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    // Wheel changed listener
    private final OnWheelChangedListener changedListener = new OnWheelChangedListener()
    {
        public void onChanged(WheelView wheel, int oldValue, int newValue)
        {
            if (!wheelScrolled) {
                updateStatus();
            }
        }
    };

    /**
     * Updates entered PIN status
     */
    private void updateStatus() {
        text1.setText(wheelMenu1[getWheel(R.id.wheel_view).getCurrentItem()]);
        text.setText(wheelMenu1[getWheel(R.id.wheel_view).getCurrentItem()]);
    }

    /**
     * Initializes activity_wheel
     *
     * @param id
     *          the activity_wheel widget Id
     */

    private void initWheel(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter<String>(getApplicationContext(), wheelMenu1));
//        wheel.setVisibleItems(2);
//        wheel.setCurrentItem(0);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
    }

    /**
     * Returns activity_wheel by Id
     *
     * @param id
     *          the activity_wheel Id
     * @return the activity_wheel with passed Id
     */
    private WheelView getWheel(int id)
    {
        return (WheelView) findViewById(id);
    }

    /**
     * Tests activity_wheel value
     *
     * @param id
     *          the activity_wheel Id

     *
     * @return true if activity_wheel value is equal to passed value
     */
    private int getWheelValue(int id)
    {
        return getWheel(id).getCurrentItem();
    }
}
