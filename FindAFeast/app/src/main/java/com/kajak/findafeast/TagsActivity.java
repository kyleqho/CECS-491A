package com.kajak.findafeast;

/**
 * Created by Ash on 4/23/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
    private ArrayList<View> clicked_SubTags;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tags);
        setTitle("Tags");
        hm = new HashMap<String, Integer>();
        builder = new AlertDialog.Builder(TagsActivity.this);
        clicked_SubTags = new ArrayList<View>();
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        American = (Button) findViewById(R.id.button4);
        American.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                Parcelable state = listView.onSaveInstanceState();
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.American_Tags));
                listView.setAdapter(adapter);

                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);

                /*for(int i = 0;i < clicked_SubTags.size();i++) {
                    clicked_SubTags.get(i).setBackgroundColor(Color.GRAY);
                }*/



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        /*for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                view.setBackgroundColor(Color.WHITE);
                                System.out.println(entry.getKey());
                                System.out.println(entry.getValue());
                            }

                            if(entry.getValue() == 1) {
                                //System.out.println(key);
                                System.out.println(entry.getKey());
                                System.out.println(entry.getValue());
                                view.setBackgroundColor(Color.GRAY);
                            }
                        }*/
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            clicked_SubTags.add(view);
                            //view.setEnabled(false);
                            view.setBackgroundColor(Color.GRAY);
                            view.setSelected(true);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            clicked_SubTags.remove(view);
                            //view.setEnabled(true);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                            view.setSelected(false);
                        }
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);
                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;

                            if(entry.getValue() != null) {
                                System.out.println(key);
                                //view.setBackground(Color.WHITE);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        for(int i = 0;i < clicked_SubTags.size();i++) {
                            clicked_SubTags.get(i).setBackgroundColor(Color.GRAY);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });
        Asian = (Button) findViewById(R.id.button6);
        Asian.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.Asian_Tags));
                listView.setAdapter(adapter);
                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            view.setBackgroundColor(Color.GRAY);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                        }
                        System.out.println(hm.get("Steak House"));
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);

                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                value = 0;
                            }

                            if(value == 1) {
                                System.out.println(key);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });
        Latin = (Button) findViewById(R.id.button11);
        Latin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.Latin_Tags));
                listView.setAdapter(adapter);
                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            view.setBackgroundColor(Color.GRAY);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                        }
                        System.out.println(hm.get("Steak House"));
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);

                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                value = 0;
                            }

                            if(value == 1) {
                                System.out.println(key);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });
        Seafood = (Button) findViewById(R.id.button3);
        Seafood.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.Seafood_Tags));
                listView.setAdapter(adapter);
                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            view.setBackgroundColor(Color.GRAY);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                        }
                        System.out.println(hm.get("Steak House"));
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);

                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                value = 0;
                            }

                            if(value == 1) {
                                System.out.println(key);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });
        Beverages = (Button) findViewById(R.id.button2);
        Beverages.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.Beverages_Tags));
                listView.setAdapter(adapter);
                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            view.setBackgroundColor(Color.GRAY);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                        }
                        System.out.println(hm.get("Steak House"));
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);

                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                value = 0;
                            }

                            if(value == 1) {
                                System.out.println(key);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });
        Bars = (Button) findViewById(R.id.button5);
        Bars.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.Bars_Tags));
                listView.setAdapter(adapter);
                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            view.setBackgroundColor(Color.GRAY);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                        }
                        System.out.println(hm.get("Steak House"));
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);

                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                value = 0;
                            }

                            if(value == 1) {
                                System.out.println(key);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });
        Desserts = (Button) findViewById(R.id.button9);
        Desserts.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listView = new ListView(TagsActivity.this);
                buttonClick = (Button) findViewById(view.getId());

                adapter = new ArrayAdapter<String>(TagsActivity.this, R.layout.list_item, R.id.text_item, getResources().getStringArray(R.array.Desserts_Tags));
                listView.setAdapter(adapter);
                //builder = new AlertDialog.Builder(view.getContext());

                builder.setView(listView);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long arg) {
                        //itemView = view;
                        String listViewItem = listView.getItemAtPosition(position).toString();
                        if(hm.get(listViewItem) == null) {
                            hm.put(listViewItem, 1);
                            view.setBackgroundColor(Color.GRAY);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,true);
                        }

                        else {
                            hm.put(listViewItem, null);
                            //listView.setSelector(R.drawable.selector);
                            //listView.setItemChecked(position,false);
                            view.setBackgroundColor(Color.WHITE);
                        }
                        System.out.println(hm.get("Steak House"));
                        ViewGroup vg = (ViewGroup) view;
                        TextView tv = (TextView) vg.findViewById(R.id.text_item);

                        Toast.makeText(getBaseContext(), listViewItem, Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Map.Entry<String,Integer> entry : hm.entrySet()) {
                            String key = entry.getKey();
                            int value = 1;
                            if(entry.getValue() == null) {
                                value = 0;
                            }

                            if(value == 1) {
                                System.out.println(key);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                return true;
            }
        });

        Button tag_btn = (Button) this.findViewById(R.id.next);
        tag_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent tagsToList = new Intent(TagsActivity.this, ListActivity.class);
                startActivity(tagsToList);
            }
        });

    }

    public void onClick(View view) {

        Button clicked = (Button) findViewById(view.getId());
        Toast.makeText(getBaseContext(), "TAP", Toast.LENGTH_SHORT).show(); //Toast message if button is tapped
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
}

