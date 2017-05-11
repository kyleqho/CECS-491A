package com.kajak.findafeast;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<String> imgid;
    private final ArrayList<Double> rating;
    private final ArrayList<ArrayList<String>> address;

    public ListAdapter(Activity context, ArrayList<String> itemname, ArrayList<String> imgid,
                       ArrayList<Double> rating, ArrayList<ArrayList<String>> address) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.rating = rating;
        this.address = address;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.restaurant_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txtRating = (TextView) rowView.findViewById(R.id.restaurant_rating);
        TextView txtDistance = (TextView) rowView.findViewById(R.id.restaurant_distance);
        txtTitle.setText(itemname.get(position));
        txtRating.setText("Rating: "+ rating.get(position).toString() +"/5");
        txtDistance.setText("Address: "+ address.get(position).toString());
        Picasso
                .with(this.context)
                .load(imgid.get(position))
                .into(imageView);
        return rowView;

    };
}