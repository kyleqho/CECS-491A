package com.kajak.findafeast;

/**
 * Created by Ash on 4/23/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TagsActivity extends AppCompatActivity {
    //Creating variables to be used later.
    //Each of the buttons are related to a specific tag
    static Button buttonClick;
    private Button American;
    private Button Asian;
    private Button Latin;
    private Button Seafood;
    private Button Beverages;
    private Button Bars;
    private Button Desserts;
    private ListView listView;
    private Map<String, Integer> hm;
    private AlertDialog.Builder builder;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> clicked_Tags;
    private ActionBar actionBar;
    ArrayList<String> temp;
    Button next_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tags);

        //Sets the title to of the activity
        setTitle("Tags");

        //Creating a hashmap to be used to relate to each tag
        hm = new HashMap<String, Integer>();

        //Creating an arraylist to be used to store tags
        clicked_Tags = new ArrayList<String>();

        //used to create a dialog box
        builder = new AlertDialog.Builder(TagsActivity.this);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Creation of a precondition intent to load pre-existing values back into the array list
        Intent preCondition = this.getIntent();
        temp = preCondition.getStringArrayListExtra("tags");

        //As long as temp is not null, add the values back into the main array list
        if(temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                clicked_Tags.add(temp.get(i));
            }
        }

        //Creation of generalized subtags as buttons
        American = (Button) findViewById(R.id.button4);
        Asian = (Button) findViewById(R.id.button6);
        Latin = (Button) findViewById(R.id.button11);
        Seafood = (Button) findViewById(R.id.button3);
        Beverages = (Button) findViewById(R.id.button2);
        Bars = (Button) findViewById(R.id.button5);
        Desserts = (Button) findViewById(R.id.button9);

        //Creation of subtags via a long click
        SetOnLongClickListener(American, getResources().getStringArray(R.array.American_Tags));
        SetOnLongClickListener(Asian, getResources().getStringArray(R.array.Asian_Tags));
        SetOnLongClickListener(Latin, getResources().getStringArray(R.array.Latin_Tags));
        SetOnLongClickListener(Seafood, getResources().getStringArray(R.array.Seafood_Tags));
        SetOnLongClickListener(Beverages, getResources().getStringArray(R.array.Beverages_Tags));
        SetOnLongClickListener(Bars, getResources().getStringArray(R.array.Bars_Tags));
        SetOnLongClickListener(Desserts, getResources().getStringArray(R.array.Desserts_Tags));

        //Button that acts as the transition to the next activity
        next_btn = (Button) this.findViewById(R.id.next);
        next_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Loading...", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                //Delaying function
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run()
                    {
                        //Intent created to pass onto the next activity
                        Intent tagsToList = new Intent(TagsActivity.this, ListActivity.class);
                        //Passes in an array list to the next activity
                        tagsToList.putStringArrayListExtra("tags", clicked_Tags);
                        startActivity(tagsToList);
                    }
                }, 2500);
            }
        });

        //Sets the next button to unclickable until there are at least two tags within the list
        if (clicked_Tags.size() < 2)
            next_btn.setEnabled(false);

        //Button that clears the list that contains the tags
        Button clear_btn = (Button) this.findViewById(R.id.clear);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_Tags.clear();
                Toast.makeText(getBaseContext(), "Tags have been cleared.", Toast.LENGTH_SHORT).show();
                //Displays the current tags to the user
                Snackbar.make(v,"Current tags:"+clicked_Tags,Snackbar.LENGTH_INDEFINITE).show();
                next_btn.setEnabled(false);
            }
        });

        //Instructions for the user
        Toast.makeText(getBaseContext(), "Long press any tag to open more options", Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), "Press any tag to remove it from the list", Toast.LENGTH_SHORT).show();
    }

    //Allows the tag to be highlighted if it has been selected
    public void createListView(final ListView lv, AlertDialog.Builder build) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                String listViewItem = lv.getItemAtPosition(position).toString();
                if(hm.get(listViewItem) == null) {
                    hm.put(listViewItem, 1);
                    tapTag(listViewItem);
                    view.setBackgroundColor(Color.GRAY);
                    view.setSelected(true);
                }

                else {
                    hm.put(listViewItem, null);
                    tapTag(listViewItem);
                    view.setBackgroundColor(Color.WHITE);
                    view.setSelected(false);
                }
                ViewGroup vg = (ViewGroup) view;
                //Shows the current items within the list
                Snackbar.make(view,"Current tags:"+clicked_Tags,500).show();
            }
        });

        //Option in the sub tags menus
        build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                    String key = entry.getKey();
                    int value = 1;

                    if(entry.getValue() != null) {
                        System.out.println(key);
                    }
                }
            }
        });

        //Option to cancel the dialog box
        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = build.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    //Onclick function that allows the buttons to be clicked and updates the list
    public void onClick(View view) {
        Button clicked = (Button) findViewById(view.getId());
        String buttonText = clicked.getText().toString();
        tapTag(buttonText);
        Snackbar.make(view,"Current tags:"+clicked_Tags,Snackbar.LENGTH_INDEFINITE).show();
    }

    //Function that adds to the list as string values
    public void tapTag(String buttonText) {
        if(clicked_Tags.contains(buttonText)) {
            clicked_Tags.remove(buttonText);
        }
        else {
            clicked_Tags.add(buttonText);
        }

        if(clicked_Tags.size() >= 2)
        {
            next_btn.setEnabled(true);
        }
        else
            next_btn.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper method to attach long click functionality to any button
    private void SetOnLongClickListener(Button button, final String[] subtags) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());
                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, subtags);
                listView.setAdapter(adapter);
                builder.setView(listView);
                createListView(listView, builder);
                return true;
            }
        });
    }

}