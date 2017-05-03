package com.kajak.findafeast;

/**
 * Created by Ash on 4/23/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TagsActivity extends AppCompatActivity {
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
    ArrayList<Restaurant> restaurant;
    private ActionBar actionBar;
    ArrayList<String> temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tags);
        setTitle("Tags");
        hm = new HashMap<String, Integer>();
        builder = new AlertDialog.Builder(TagsActivity.this);
        clicked_Tags = new ArrayList<String>();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Intent preCondition = this.getIntent();
        temp = preCondition.getStringArrayListExtra("tags");

        if(temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                clicked_Tags.add(temp.get(i));
                System.out.println(clicked_Tags.get(i));
            }
        }


        American = (Button) findViewById(R.id.button4);
        Asian = (Button) findViewById(R.id.button6);
        Latin = (Button) findViewById(R.id.button11);
        Seafood = (Button) findViewById(R.id.button3);
        Beverages = (Button) findViewById(R.id.button2);
        Bars = (Button) findViewById(R.id.button5);
        Desserts = (Button) findViewById(R.id.button9);

        SetOnLongClickListener(American, getResources().getStringArray(R.array.American_Tags));
        SetOnLongClickListener(Asian, getResources().getStringArray(R.array.Asian_Tags));
        SetOnLongClickListener(Latin, getResources().getStringArray(R.array.Latin_Tags));
        SetOnLongClickListener(Seafood, getResources().getStringArray(R.array.Seafood_Tags));
        SetOnLongClickListener(Beverages, getResources().getStringArray(R.array.Beverages_Tags));
        SetOnLongClickListener(Bars, getResources().getStringArray(R.array.Bars_Tags));
        SetOnLongClickListener(Desserts, getResources().getStringArray(R.array.Desserts_Tags));

        Button tag_btn = (Button) this.findViewById(R.id.next);
        tag_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent tagsToList = new Intent(TagsActivity.this, ListActivity.class);
                tagsToList.putStringArrayListExtra("tags", clicked_Tags);
                startActivity(tagsToList);
            }
        });

        Button clear_btn = (Button) this.findViewById(R.id.clear);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_Tags.clear();
                Toast.makeText(getBaseContext(), "Tags have been cleared.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void createListView(final ListView lv, AlertDialog.Builder build) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                String listViewItem = lv.getItemAtPosition(position).toString();
                if(hm.get(listViewItem) == null) {
                    hm.put(listViewItem, 1);
                    clicked_Tags.add(listViewItem);
                    view.setBackgroundColor(Color.GRAY);
                    view.setSelected(true);
                }

                else {
                    hm.put(listViewItem, null);
                    clicked_Tags.remove(listViewItem);
                    view.setBackgroundColor(Color.WHITE);
                    view.setSelected(false);
                }
                ViewGroup vg = (ViewGroup) view;
                TextView tv = (TextView) vg.findViewById(R.id.text_item);
                Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
            }
        });

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

    public void onClick(View view) {
        Button clicked = (Button) findViewById(view.getId());
        String buttonText = clicked.getText().toString();
        tapTag(buttonText);
        Toast.makeText(getBaseContext(), buttonText, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
    }

    public void tapTag(String buttonText) {
        if(clicked_Tags.contains(buttonText)) {
            clicked_Tags.remove(buttonText);
        }
        else {
            clicked_Tags.add(buttonText);
        }
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